"""
템플릿 기반 Chunk 문서 생성기
"""

import os
import json
import re
import aiohttp
from typing import List, Dict, Any
from dataclasses import asdict

try:
    from .ast_chunker import CodeChunk, chunk_directory
    from .template_renderer import ChunkTemplateRenderer
    from .chunkers import MultiLanguageChunker
except ImportError:
    from ast_chunker import CodeChunk, chunk_directory
    from template_renderer import ChunkTemplateRenderer
    from chunkers import MultiLanguageChunker


class TemplateChunkGenerator:
    """템플릿 기반 Chunk 문서 생성기"""
    
    def __init__(self, source_dir: str, output_dir: str, template_dir: str = None):
        self.source_dir = source_dir
        self.output_dir = output_dir
        self.renderer = ChunkTemplateRenderer(template_dir)
        
        # 다중 언어 Chunker 초기화
        self.multi_chunker = MultiLanguageChunker(project_root=source_dir)
        
        # 출력 디렉토리 생성
        os.makedirs(output_dir, exist_ok=True)
    
    async def generate_documents(self, upload_to_rag: bool = True) -> Dict[str, Any]:
        """문서 생성 메인 함수"""
        import time
        total_start = time.time()
        
        print(f"🚀 템플릿 기반 Chunk 문서 생성 시작...")
        print(f"   📂 소스: {self.source_dir}")
        print(f"   📂 출력: {self.output_dir}")
        
        # 1. Chunk 생성 (다중 언어 지원)
        step1_start = time.time()
        all_chunks = self.multi_chunker.chunk_directory(self.source_dir)
        step1_time = time.time() - step1_start
        print(f"   🎯 총 {len(all_chunks)}개의 chunk 생성 완료 [⏱️ {step1_time:.2f}초]")
        
        # 프로젝트 분석 결과 출력
        project_summary = self.multi_chunker.get_project_summary(all_chunks)
        print(f"   📊 프로젝트 분석:")
        print(f"      - 타입: {project_summary['project_analysis']['project_type']}")
        print(f"      - 프레임워크: {project_summary['project_analysis']['framework_info']}")
        print(f"      - 언어 분포: {project_summary['language_distribution']}")
        print(f"      - 프레임워크 분포: {project_summary['framework_distribution']}")
        
        # 2. 파일별 그룹화
        step2_start = time.time()
        file_chunks = self._group_chunks_by_file(all_chunks)
        step2_time = time.time() - step2_start
        print(f"   📂 파일별 그룹화 완료: {len(file_chunks)}개 파일 [⏱️ {step2_time:.2f}초]")
        
        # 3. 구조화된 문서 생성
        step3_start = time.time()
        generated_files = self._generate_structured_documents(file_chunks)
        step3_time = time.time() - step3_start
        print(f"   📝 MD 문서 생성 완료: {len(generated_files)}개 파일 [⏱️ {step3_time:.2f}초]")
        
        # 4. Chunk 메타데이터 저장
        step4_start = time.time()
        metadata_file = self._save_chunk_metadata(all_chunks)
        step4_time = time.time() - step4_start
        print(f"   💾 메타데이터 저장 완료 [⏱️ {step4_time:.2f}초]")
        
        # 5. 프로젝트 요약 생성
        step5_start = time.time()
        summary_file = self._generate_project_summary(all_chunks, file_chunks)
        step5_time = time.time() - step5_start
        print(f"   📊 프로젝트 요약 생성 완료 [⏱️ {step5_time:.2f}초]")
        
        # 6. 용어집 생성
        step6_start = time.time()
        vocabulary_file = self._generate_vocabulary(all_chunks)
        step6_time = time.time() - step6_start
        print(f"   📚 용어집 생성 완료 [⏱️ {step6_time:.2f}초]")
        
        # 7. 엔터프라이즈 아키텍처 분석 (엔터프라이즈 애플리케이션인 경우)
        step7_start = time.time()
        architecture_file = None
        business_logic_file = None
        if self._is_enterprise_project(all_chunks):
            architecture_file = self._generate_enterprise_architecture(all_chunks, file_chunks)
            business_logic_file = self._generate_business_logic_analysis(all_chunks)
        step7_time = time.time() - step7_start
        if architecture_file:
            print(f"   🏗️ 엔터프라이즈 아키텍처 분석 완료 [⏱️ {step7_time:.2f}초]")
        
        result = {
            "source_dir": self.source_dir,
            "output_dir": self.output_dir,
            "generated_files": generated_files,
            "total_chunks": len(all_chunks),
            "total_files": len(file_chunks),
            "chunk_metadata_file": metadata_file,
            "project_summary_file": summary_file,
            "vocabulary_file": vocabulary_file,
            "architecture_file": architecture_file,
            "business_logic_file": business_logic_file
        }
        
        step6_time = 0.0
        if upload_to_rag:
            # 🎯 RAG에 MD 문서들을 섹션별로 분할하여 업로드
            step6_start = time.time()
            print(f"\n🔄 RAG 업로드 시작...")
            rag_upload_result = await self._upload_md_sections_to_rag(all_chunks, generated_files, summary_file)
            result["rag_upload_result"] = rag_upload_result
            step6_time = time.time() - step6_start
            print(f"   📤 RAG 업로드 완료 [⏱️ {step6_time:.2f}초]")
        else:
            print("\n⏭️ RAG 업로드 건너뜀 (generate-local 모드)")
        
        total_time = time.time() - total_start
        print(f"\n✅ 전체 문서 생성 완료 [⏱️ 총 {total_time:.2f}초]")
        print(f"   📈 단계별 시간:")
        print(f"     1. Chunk 생성: {step1_time:.2f}초 ({step1_time/total_time*100:.1f}%)")
        print(f"     2. 파일 그룹화: {step2_time:.2f}초 ({step2_time/total_time*100:.1f}%)")
        print(f"     3. MD 문서 생성: {step3_time:.2f}초 ({step3_time/total_time*100:.1f}%)")
        print(f"     4. 메타데이터 저장: {step4_time:.2f}초 ({step4_time/total_time*100:.1f}%)")
        print(f"     5. 프로젝트 요약: {step5_time:.2f}초 ({step5_time/total_time*100:.1f}%)")
        print(f"     6. RAG 업로드: {step6_time:.2f}초 ({(step6_time/total_time*100 if total_time>0 else 0):.1f}%)")
        
        return result
    
    def _group_chunks_by_file(self, chunks: List[CodeChunk]) -> Dict[str, List[CodeChunk]]:
        """파일별로 chunk 그룹화"""
        
        file_chunks = {}
        for chunk in chunks:
            if chunk.file_path not in file_chunks:
                file_chunks[chunk.file_path] = []
            file_chunks[chunk.file_path].append(chunk)
        
        return file_chunks
    
    def _generate_structured_documents(self, file_chunks: Dict[str, List[CodeChunk]]) -> List[str]:
        """구조화된 문서 생성"""
        
        generated_files = []
        
        for file_path, chunks in file_chunks.items():
            # MD 파일 경로 생성
            md_file_path = self._get_md_file_path(file_path)
            
            # 디렉토리 생성
            os.makedirs(os.path.dirname(md_file_path), exist_ok=True)
            
            # 템플릿으로 문서 생성
            md_content = self.renderer.render_file_document(file_path, chunks, self.source_dir)
            
            # 파일 저장
            with open(md_file_path, 'w', encoding='utf-8') as f:
                f.write(md_content)
            
            generated_files.append(md_file_path)
            
            # 상대 경로로 출력
            rel_path = os.path.relpath(md_file_path, self.output_dir)
            print(f"   ✅ {rel_path}")
        
        return generated_files
    
    def _get_md_file_path(self, source_file_path: str) -> str:
        """소스 파일 경로를 MD 파일 경로로 변환 (다중 언어 지원)"""
        
        # 소스 디렉토리로부터의 상대 경로
        rel_path = os.path.relpath(source_file_path, self.source_dir)
        
        # 파일 확장자를 .md로 변경
        name, ext = os.path.splitext(rel_path)
        md_rel_path = name + '.md'
        
        # 전체 경로 생성
        return os.path.join(self.output_dir, md_rel_path)
    
    def _save_chunk_metadata(self, chunks: List[CodeChunk]) -> str:
        """RAG용 chunk 메타데이터 저장"""
        
        metadata_file = os.path.join(self.output_dir, "chunk_metadata.json")
        
        # CodeChunk를 딕셔너리로 변환
        chunks_data = []
        for chunk in chunks:
            chunk_dict = asdict(chunk)
            chunks_data.append(chunk_dict)
        
        metadata = {
            "total_chunks": len(chunks),
            "generation_timestamp": "2024-01-01T00:00:00Z",  # 실제로는 현재 시간
            "source_directory": self.source_dir,
            "chunks": chunks_data
        }
        
        with open(metadata_file, 'w', encoding='utf-8') as f:
            json.dump(metadata, f, ensure_ascii=False, indent=2)
        
        return metadata_file
    
    def _generate_project_summary(self, all_chunks: List[CodeChunk], file_chunks: Dict[str, List[CodeChunk]]) -> str:
        """프로젝트 요약 생성"""
        
        # 프로젝트 데이터 준비
        project_data = self._prepare_project_data(all_chunks, file_chunks)
        
        # 템플릿으로 요약 생성
        summary_content = self.renderer.render_project_summary(project_data)
        
        # 파일 저장
        summary_file = os.path.join(self.output_dir, "project_summary.md")
        with open(summary_file, 'w', encoding='utf-8') as f:
            f.write(summary_content)
        
        return summary_file
    
    def _prepare_project_data(self, all_chunks: List[CodeChunk], file_chunks: Dict[str, List[CodeChunk]]) -> Dict[str, Any]:
        """프로젝트 데이터 준비"""
        
        project_name = os.path.basename(self.source_dir)
        
        # 기본 통계
        total_files = len(file_chunks)
        total_chunks = len(all_chunks)
        total_tokens = sum(chunk.token_count or 0 for chunk in all_chunks)
        
        # 복잡도 통계
        complexities = [chunk.complexity for chunk in all_chunks if chunk.complexity]
        avg_complexity = sum(complexities) / len(complexities) if complexities else 0
        max_complexity = max(complexities) if complexities else 0
        
        # Chunk 분포
        chunk_distribution = self._analyze_chunk_distribution(all_chunks)
        
        # 복잡도 분포
        complexity_distribution = self._analyze_complexity_distribution(all_chunks)
        
        # 복잡한 함수 TOP 10
        top_complex_functions = self._get_top_complex_functions(all_chunks, 10)
        
        # 인기 태그 TOP 20
        popular_tags = self._get_popular_tags(all_chunks, 20)
        
        # 디렉토리 구조
        directory_tree = self._generate_directory_tree()
        directories = self._analyze_directories(all_chunks)
        
        # 정성 요약/품질/개선 제안 추론
        project_summary, main_purpose, main_domain, core_features = self._infer_project_overview(all_chunks)
        main_modules = self._infer_main_modules(all_chunks)
        risky_files, duplicate_code_count = self._find_risky_files(all_chunks)
        test_code_percentage = self._estimate_test_ratio()
        refactoring_priorities, recommended_practices, modernization_suggestions = self._recommend_refactoring(risky_files, avg_complexity)
        
        return {
            "project_name": project_name,
            "total_files": total_files,
            "total_chunks": total_chunks,
            "total_tokens": total_tokens,
            "avg_complexity": avg_complexity,
            "max_complexity": max_complexity,
            "chunk_distribution": chunk_distribution,
            "complexity_distribution": complexity_distribution,
            "top_complex_functions": top_complex_functions,
            "popular_tags": popular_tags,
            "directory_tree": directory_tree,
            "directories": directories,
            # 보강 섹션 데이터
            "project_summary": project_summary,
            "main_purpose": main_purpose,
            "main_domain": main_domain,
            "core_features": core_features,
            "main_modules": main_modules,
            "risky_files": risky_files,
            "duplicate_code_count": duplicate_code_count,
            "test_code_percentage": test_code_percentage,
            "refactoring_priorities": refactoring_priorities,
            "recommended_practices": recommended_practices,
            "modernization_suggestions": modernization_suggestions
        }

    def _infer_project_overview(self, chunks: List[CodeChunk]) -> tuple:
        """프로젝트 개요/목적/도메인/핵심 기능을 간단 규칙으로 추론"""
        try:
            file_paths = [c.file_path for c in chunks]
            text = " ".join(file_paths).lower()
            domain = os.path.basename(self.source_dir)
            purpose = "코드 분석/검증" if ("analyzer" in text or "analyz" in text) else "일반 애플리케이션"
            features = []
            if any("issue_detector" in p for p in file_paths):
                features.append("이슈/오류 검출")
            if any("metrics" in p for p in file_paths):
                features.append("지표/메트릭 생성")
            if any("condition" in p for p in file_paths):
                features.append("조건 파싱/분석")
            if not features:
                features = ["핵심 기능 자동 추출 대기"]
            summary = f"'{domain}' 코드베이스는 {purpose}을(를) 수행하며, {', '.join(features)}를(을) 포함합니다."
            return summary, purpose, domain, features
        except Exception:
            return "", "", os.path.basename(self.source_dir), []

    def _infer_main_modules(self, chunks: List[CodeChunk]) -> List[Dict[str, Any]]:
        """주요 디렉토리명을 모듈로 요약"""
        by_dir = {}
        for c in chunks:
            rel = os.path.relpath(c.file_path, self.source_dir)
            d = os.path.dirname(rel).split(os.sep)
            key = d[0] if d and d[0] else "root"
            by_dir.setdefault(key, set()).add(os.path.basename(c.file_path))
        modules = []
        desc_map = {
            "analyzers": "분석/검증 로직",
            "formatters": "출력/서식 처리",
            "streaming": "스트리밍/실시간 처리",
            "shared": "공통 유틸/모델",
            "models": "데이터/도메인 모델",
            "utils": "도우미 함수 모음"
        }
        for name, files in by_dir.items():
            modules.append({
                "name": name,
                "description": desc_map.get(name, "모듈 기능 요약"),
                "key_components": sorted(list(files))[:6]
            })
        # 가장 의미있는 상위 6개만
        return sorted(modules, key=lambda m: len(m["key_components"]), reverse=True)[:6]

    def _find_risky_files(self, chunks: List[CodeChunk]) -> tuple:
        """파일별 복잡도 합으로 상위 위험 파일 추정 + 중복 추정 개수"""
        score = {}
        fn_hashes = {}
        for c in chunks:
            score[c.file_path] = score.get(c.file_path, 0) + (c.complexity or 0)
            if c.code_hash:
                fn_hashes.setdefault(c.code_hash, 0)
                fn_hashes[c.code_hash] += 1
        risky = [
            os.path.relpath(fp, self.source_dir) for fp, s in sorted(score.items(), key=lambda x: x[1], reverse=True)[:8]
        ]
        duplicates = sum(1 for k,v in fn_hashes.items() if v > 1)
        return risky, duplicates

    def _estimate_test_ratio(self) -> int:
        """테스트 코드 비율 근사치(간단 규칙)"""
        try:
            total, tests = 0, 0
            for root, dirs, files in os.walk(self.source_dir):
                for f in files:
                    if f.endswith('.py'):
                        total += 1
                        rel = os.path.relpath(os.path.join(root, f), self.source_dir)
                        if rel.startswith('tests') or f.startswith('test_'):
                            tests += 1
            return int((tests / total * 100)) if total else 0
        except Exception:
            return 0

    def _recommend_refactoring(self, risky_files: List[str], avg_complexity: float) -> tuple:
        """리팩토링/현대화 제안 생성"""
        priorities = risky_files[:5]
        practices = [
            "큰 함수 분리(SRP)",
            "타입 힌트/정적 분석 강화",
            "단위 테스트 추가",
            "복잡도 높은 분기 단순화"
        ]
        modernization = [
            "CI 도입 및 자동 테스트",
            "로깅/옵저버빌리티 강화",
            "설정의 환경변수화/보안 비밀 분리"
        ]
        if avg_complexity > 7:
            practices.append("핵심 경로 우선 리팩토링")
        return priorities, practices, modernization
    
    def _analyze_chunk_distribution(self, chunks: List[CodeChunk]) -> Dict[str, Dict[str, Any]]:
        """Chunk 타입별 분포 분석"""
        
        distribution = {}
        total_chunks = len(chunks)
        
        for chunk in chunks:
            chunk_type = chunk.chunk_type
            if chunk_type not in distribution:
                distribution[chunk_type] = {"count": 0}
            distribution[chunk_type]["count"] += 1
        
        # 비율 계산
        type_names = {
            "overview": "📋 파일 개요",
            "class": "🏗️ 클래스",
            "function": "⚙️ 함수",
            "async_function": "🔄 비동기 함수",
            "method": "🔧 메서드"
        }
        
        for chunk_type, data in distribution.items():
            data["percentage"] = (data["count"] / total_chunks * 100) if total_chunks > 0 else 0
            data["display_name"] = type_names.get(chunk_type, chunk_type)
        
        return distribution
    
    def _analyze_complexity_distribution(self, chunks: List[CodeChunk]) -> Dict[str, int]:
        """복잡도 분포 분석"""
        
        distribution = {"낮음(1-3)": 0, "보통(4-7)": 0, "높음(8-15)": 0, "매우높음(16+)": 0}
        
        for chunk in chunks:
            if chunk.complexity:
                if chunk.complexity <= 3:
                    distribution["낮음(1-3)"] += 1
                elif chunk.complexity <= 7:
                    distribution["보통(4-7)"] += 1
                elif chunk.complexity <= 15:
                    distribution["높음(8-15)"] += 1
                else:
                    distribution["매우높음(16+)"] += 1
        
        return {k: v for k, v in distribution.items() if v > 0}
    
    def _get_top_complex_functions(self, chunks: List[CodeChunk], limit: int) -> List[Dict[str, Any]]:
        """복잡한 함수 TOP N"""
        
        functions = []
        for chunk in chunks:
            if chunk.chunk_type in ["function", "async_function", "method"] and chunk.complexity:
                functions.append({
                    "name": chunk.name,
                    "file_name": os.path.basename(chunk.file_path),
                    "complexity": chunk.complexity,
                    "token_count": chunk.token_count or 0
                })
        
        # 복잡도 순으로 정렬
        functions.sort(key=lambda x: x["complexity"], reverse=True)
        
        return functions[:limit]
    
    def _get_popular_tags(self, chunks: List[CodeChunk], limit: int) -> List[tuple]:
        """인기 태그 TOP N"""
        
        tag_counts = {}
        for chunk in chunks:
            if chunk.tags:
                for tag in chunk.tags:
                    tag_counts[tag] = tag_counts.get(tag, 0) + 1
        
        # 빈도순으로 정렬
        popular_tags = sorted(tag_counts.items(), key=lambda x: x[1], reverse=True)
        
        return popular_tags[:limit]
    
    def _generate_directory_tree(self) -> str:
        """디렉토리 트리 생성"""
        
        # 간단한 트리 구조 (실제로는 더 복잡한 로직 필요)
        tree_lines = []
        tree_lines.append(f"{os.path.basename(self.source_dir)}/")
        
        # 디렉토리 구조 수집
        dirs = set()
        for root, dirnames, filenames in os.walk(self.source_dir):
            rel_root = os.path.relpath(root, self.source_dir)
            if rel_root != ".":
                dirs.add(rel_root)
        
        # 정렬하여 트리 생성
        for dir_path in sorted(dirs):
            depth = dir_path.count(os.sep)
            indent = "  " * (depth + 1)
            dir_name = os.path.basename(dir_path)
            tree_lines.append(f"{indent}├── {dir_name}/")
        
        return "\n".join(tree_lines)
    
    def _analyze_directories(self, chunks: List[CodeChunk]) -> List[Dict[str, Any]]:
        """디렉토리별 분석"""
        
        dir_info = {}
        
        for chunk in chunks:
            rel_path = os.path.relpath(chunk.file_path, self.source_dir)
            rel_dir = os.path.dirname(rel_path)
            
            if rel_dir == "":
                rel_dir = "root"
            
            if rel_dir not in dir_info:
                dir_info[rel_dir] = {
                    "name": rel_dir,
                    "path": rel_dir,
                    "files": set(),
                    "chunks": 0
                }
            
            dir_info[rel_dir]["files"].add(os.path.basename(chunk.file_path))
            dir_info[rel_dir]["chunks"] += 1
        
        # 세트를 개수로 변환
        directories = []
        for dir_data in dir_info.values():
            dir_data["files"] = len(dir_data["files"])
            directories.append(dir_data)
        
        return directories
    
    def _generate_vocabulary(self, all_chunks: List[Any]) -> str:
        """Chunk에서 용어집 생성"""
        
        vocabulary = {
            "class_names": [],
            "method_names": [],
            "function_names": [],
            "domain_concepts": [],
            "korean_terms": [],
            "technical_terms": []
        }
        
        # Chunk에서 용어 추출
        for chunk in all_chunks:
            self._extract_terms_from_chunk(chunk, vocabulary)
        
        # 중복 제거 및 정렬
        for key in vocabulary:
            vocabulary[key] = sorted(list(set(vocabulary[key])))
        
        # 용어집 파일 저장
        vocabulary_file = os.path.join(self.output_dir, "vocabulary.json")
        with open(vocabulary_file, 'w', encoding='utf-8') as f:
            json.dump(vocabulary, f, ensure_ascii=False, indent=2)
        
        print(f"   📚 용어집 저장: {vocabulary_file}")
        print(f"   📊 용어 통계:")
        for key, terms in vocabulary.items():
            print(f"      {key}: {len(terms)}개")
        
        return vocabulary_file
    
    def _is_enterprise_project(self, chunks: List[Any]) -> bool:
        """엔터프라이즈 프로젝트인지 감지"""
        enterprise_count = 0
        for chunk in chunks:
            if hasattr(chunk, 'framework') and chunk.framework in ['spring-boot', 'vue']:
                enterprise_count += 1
            if hasattr(chunk, 'chunk_type') and chunk.chunk_type in ['class', 'component']:
                enterprise_patterns = ['controller', 'service', 'entity', 'dto', 'repository', 'component', 'store']
                if any(pattern in chunk.name.lower() for pattern in enterprise_patterns):
                    enterprise_count += 1
        
        return enterprise_count >= 5
    
    def _generate_enterprise_architecture(self, chunks: List[Any], file_chunks: Dict[str, List[Any]]) -> str:
        """엔터프라이즈 아키텍처 분석 문서 생성"""
        try:
            from .chunkers.enterprise_chunker import EnterpriseChunker
            enterprise_chunker = EnterpriseChunker(self.source_dir)
            
            # 아키텍처 분석 데이터 생성
            architecture_data = enterprise_chunker.analyze_architecture(chunks, file_chunks)
            architecture_data['source_directory'] = self.source_dir
            architecture_data['project_name'] = os.path.basename(self.source_dir)
            
            # 템플릿 렌더링
            content = self.renderer.render_enterprise_architecture(architecture_data)
            
            # 파일 저장
            architecture_file = os.path.join(self.output_dir, "enterprise_architecture.md")
            with open(architecture_file, 'w', encoding='utf-8') as f:
                f.write(content)
            
            return architecture_file
        except Exception as e:
            print(f"   ⚠️ 엔터프라이즈 아키텍처 분석 실패: {e}")
            return None
    
    def _generate_business_logic_analysis(self, chunks: List[Any]) -> str:
        """비즈니스 로직 분석 문서 생성"""
        try:
            from .chunkers.enterprise_chunker import EnterpriseChunker
            enterprise_chunker = EnterpriseChunker(self.source_dir)
            
            # 비즈니스 로직 분석 데이터 생성
            business_data = enterprise_chunker.analyze_business_logic(chunks)
            business_data['source_directory'] = self.source_dir
            business_data['project_name'] = os.path.basename(self.source_dir)
            
            # 템플릿 렌더링
            content = self.renderer.render_business_logic_analysis(business_data)
            
            # 파일 저장
            business_file = os.path.join(self.output_dir, "business_logic_analysis.md")
            with open(business_file, 'w', encoding='utf-8') as f:
                f.write(content)
            
            return business_file
        except Exception as e:
            print(f"   ⚠️ 비즈니스 로직 분석 실패: {e}")
            return None
    
    def _extract_terms_from_chunk(self, chunk: Any, vocabulary: Dict[str, List[str]]):
        """Chunk에서 용어 추출"""
        
        # 클래스명 추출
        if chunk.chunk_type == "class" and chunk.name:
            vocabulary["class_names"].append(chunk.name)
        
        # 메서드명 추출
        if chunk.chunk_type == "method" and chunk.name:
            vocabulary["method_names"].append(chunk.name)
        
        # 함수명 추출
        if chunk.chunk_type in ["function", "async_function"] and chunk.name:
            vocabulary["function_names"].append(chunk.name)
        
        # 독스트링에서 도메인 개념 추출
        if chunk.docstring:
            docstring = chunk.docstring.lower()
            
            # 한국어 기술 용어
            korean_terms = [
                '검출', '분석', '처리', '생성', '변환', '검증', '오류', '이슈',
                '조건', '규칙', '로직', '타입', '불일치', '중복', '복잡성',
                '파싱', '렌더링', '포맷팅', '스트리밍', '임베딩', '벡터',
                '검색', '인덱싱', '토큰화', '전처리', '후처리'
            ]
            
            for term in korean_terms:
                if term in docstring:
                    vocabulary["korean_terms"].append(term)
            
            # 영어 기술 용어
            english_terms = [
                'detection', 'analysis', 'processing', 'generation', 'transformation',
                'validation', 'error', 'issue', 'condition', 'rule', 'logic', 'type',
                'mismatch', 'duplicate', 'complexity', 'parsing', 'rendering', 'formatting',
                'streaming', 'embedding', 'vector', 'search', 'indexing', 'tokenization',
                'preprocessing', 'postprocessing', 'chunking', 'rag', 'llm', 'ai'
            ]
            
            for term in english_terms:
                if term in docstring:
                    vocabulary["technical_terms"].append(term)
        
        # 태그에서 도메인 개념 추출
        if hasattr(chunk, 'tags') and chunk.tags:
            for tag in chunk.tags:
                if isinstance(tag, str) and len(tag) > 2:
                    vocabulary["domain_concepts"].append(tag)
        
        # 의존성에서 기술 용어 추출
        if hasattr(chunk, 'dependencies') and chunk.dependencies:
            for dep in chunk.dependencies:
                if isinstance(dep, str):
                    vocabulary["technical_terms"].append(dep)

    async def _upload_md_sections_to_rag(self, all_chunks: List[CodeChunk], generated_files: List[str], summary_file: str) -> Dict[str, Any]:
        """생성된 MD 파일들을 섹션별로 분할하여 RAG에 업로드"""
        
        rag_base_url = os.getenv("RAG_SERVICE_URL", "http://localhost:8003")
        upload_results = []
        
        try:
            async with aiohttp.ClientSession() as session:
                # 1. 개별 MD 파일들을 섹션별로 분할하여 업로드
                for md_file_path in generated_files:
                    file_upload_result = await self._upload_single_md_file_sections(
                        session, rag_base_url, md_file_path, all_chunks
                    )
                    upload_results.append(file_upload_result)
                
                # 2. 프로젝트 요약 파일 섹션별 업로드
                summary_upload_result = await self._upload_project_summary_sections(
                    session, rag_base_url, summary_file
                )
                upload_results.append(summary_upload_result)
            
            # 총 업로드된 섹션 수 계산
            total_sections = sum(detail.get("sections_uploaded", 0) for detail in upload_results)
            successful_files = sum(1 for detail in upload_results if detail.get("success", False))
            
            print(f"✅ RAG 업로드 완료: {successful_files}/{len(upload_results)}개 파일 처리")
            print(f"   📊 총 업로드된 섹션: {total_sections}개")
            
            return {
                "success": True,
                "uploaded_files": len(upload_results),
                "successful_files": successful_files,
                "total_sections": total_sections,
                "details": upload_results
            }
            
        except Exception as e:
            print(f"❌ RAG 업로드 실패: {e}")
            return {
                "success": False,
                "error": str(e),
                "details": []
            }
    
    async def _upload_single_md_file_sections(self, session: aiohttp.ClientSession, rag_base_url: str, 
                                            md_file_path: str, all_chunks: List[CodeChunk]) -> Dict[str, Any]:
        """단일 MD 파일을 섹션별로 분할하여 RAG에 업로드"""
        
        try:
            # MD 파일 읽기
            with open(md_file_path, 'r', encoding='utf-8') as f:
                content = f.read()
            
            # 파일명에서 Python 파일 경로 추출 (프로젝트 루트 prefix 유지)
            relative_path = os.path.relpath(md_file_path, self.output_dir)
            # 절대 소스 경로로 변환하여 chunk.file_path 와 정확히 매칭
            python_file = os.path.join(self.source_dir, relative_path.replace('.md', '.py'))
            
            # 해당 파일의 chunk들 찾기
            file_chunks = [chunk for chunk in all_chunks if chunk.file_path == python_file]
            
            # MD 파일을 섹션별로 분할
            sections = self._split_md_into_sections(content, file_chunks, md_file_path)
            
            upload_count = 0
            for section in sections:
                # 안정적 ID 생성: project+source_file+section_title
                meta = section.get("metadata", {})
                stable_id = f"{meta.get('project','')}/{meta.get('source_file','')}/{meta.get('section_title','')}".lower()
                meta["id_hint"] = stable_id
                upload_success = await self._upload_section_to_rag(session, rag_base_url, {"content": section["content"], "metadata": meta})
                if upload_success:
                    upload_count += 1
            
            print(f"   📄 {os.path.basename(md_file_path)}: {upload_count}/{len(sections)} 섹션 업로드")
            if upload_count != len(sections):
                print(f"   ⚠️ 일부 섹션 업로드 실패: {len(sections) - upload_count}개 실패")
            
            return {
                "file": md_file_path,
                "sections_total": len(sections),
                "sections_uploaded": upload_count,
                "success": upload_count > 0
            }
            
        except Exception as e:
            print(f"   ❌ {os.path.basename(md_file_path)}: {e}")
            return {
                "file": md_file_path,
                "sections_total": 0,
                "sections_uploaded": 0,
                "success": False,
                "error": str(e)
            }
    
    async def _upload_project_summary_sections(self, session: aiohttp.ClientSession, 
                                             rag_base_url: str, summary_file: str) -> Dict[str, Any]:
        """프로젝트 요약을 섹션별로 분할하여 RAG에 업로드"""
        
        try:
            # 프로젝트 요약 파일 읽기
            with open(summary_file, 'r', encoding='utf-8') as f:
                content = f.read()
            
            # 프로젝트 요약을 섹션별로 분할
            sections = self._split_project_summary_into_sections(content)
            
            upload_count = 0
            for section in sections:
                meta = section.get("metadata", {})
                stable_id = f"{meta.get('project','')}/{meta.get('source_file','')}/{meta.get('section_title','')}".lower()
                meta["id_hint"] = stable_id
                upload_success = await self._upload_section_to_rag(session, rag_base_url, {"content": section["content"], "metadata": meta})
                if upload_success:
                    upload_count += 1
            
            print(f"   📊 프로젝트 요약: {upload_count}/{len(sections)} 섹션 업로드")
            
            return {
                "file": summary_file,
                "sections_total": len(sections),
                "sections_uploaded": upload_count,
                "success": upload_count > 0
            }
            
        except Exception as e:
            print(f"   ❌ 프로젝트 요약: {e}")
            return {
                "file": summary_file,
                "sections_total": 0,
                "sections_uploaded": 0,
                "success": False,
                "error": str(e)
            }
    
    def _split_md_into_sections(self, content: str, file_chunks: List[CodeChunk], md_file_path: str = None) -> List[Dict[str, Any]]:
        """MD 파일을 의미있는 섹션별로 분할 (다양한 제목 레벨 지원)"""
        
        sections = []
        print(f"   📄 섹션 분할 시작: {os.path.basename(md_file_path) if md_file_path else 'unknown'}")
        
        # 모든 제목 레벨 지원 (#, ##, ###, ####, #####, ######)
        # H1, H2는 큰 섹션, H3~H6는 작은 섹션으로 분류
        section_patterns = [
            (r'^(#{1,2}\s+.*?)(?=^#{1,2}|\Z)', 'major'),  # H1, H2 - 주요 섹션
            (r'^(#{3,6}\s+.*?)(?=^#{1,6}|\Z)', 'minor')   # H3~H6 - 세부 섹션
        ]
        
        total_sections_found = 0
        total_sections_processed = 0
        
        for pattern, section_type in section_patterns:
            matches = re.finditer(pattern, content, re.MULTILINE | re.DOTALL)
            
            for match in matches:
                total_sections_found += 1
                section_content = match.group(1).strip()
                
                # 최소 길이 기준 완화 (20자로 감소)
                if len(section_content) < 20:
                    print(f"     ⏭️ 섹션 건너뜀 (너무 짧음: {len(section_content)}자): {section_content[:50]}...")
                    continue
                
                total_sections_processed += 1
            
                # 섹션 제목 추출 (모든 제목 레벨 지원)
                title_match = re.match(r'^(#{1,6})\s+(.+)', section_content)
                if title_match:
                    title_level = len(title_match.group(1))
                    title = title_match.group(2).strip()
                    print(f"     📝 {section_type} 섹션 발견 (H{title_level}): {title[:60]}...")
                else:
                    title = "Unknown Section"
                    print(f"     ⚠️ 제목 추출 실패: {section_content[:50]}...")
            
                # 관련 chunk 찾기
                related_chunk = self._find_related_chunk(title, section_content, file_chunks)
                
                # chunk_type 결정 로직 개선 + Sequence 섹션 인식
                lowered_title = title.lower()
                lowered_content = section_content.lower()
                if related_chunk:
                    chunk_type = related_chunk.chunk_type
                    print(f"       🔗 관련 chunk 발견: {chunk_type} - {related_chunk.name}")
                elif 'sequencediagram' in lowered_content or '### 🔗 호출 순서' in title or 'sequence' in lowered_title:
                    chunk_type = 'sequence'
                    print(f"       🔗 시퀀스 섹션으로 분류")
                elif "function" in lowered_title or "def " in lowered_content:
                    chunk_type = "function"
                    print(f"       🔧 함수 섹션으로 분류")
                elif "class" in lowered_title or "class " in lowered_content:
                    chunk_type = "class"
                    print(f"       🏗️ 클래스 섹션으로 분류")
                else:
                    chunk_type = "overview"
                    print(f"       📋 개요 섹션으로 분류")
            
                # 파일 경로에서 정보 추출 (개선됨)
                # project_name을 먼저 초기화
                project_name = os.path.basename(self.source_dir)
                
                if file_chunks and file_chunks[0].file_path:
                    file_path = file_chunks[0].file_path
                    filename = os.path.basename(file_path).replace('.py', '.md')
                    # self.source_dir 이후 경로 추출
                    rel_base = self.source_dir.rstrip(os.sep) + os.sep
                    relative_path = file_path[len(rel_base):] if file_path.startswith(rel_base) else os.path.basename(file_path)
                else:
                    # file_chunks가 없어도 md 파일 경로에서 추출 시도
                    if md_file_path and md_file_path != 'unknown':
                        # generated_docs/<project>/<path>.md → <source_dir>/<path>.py
                        try:
                            md_rel = os.path.relpath(md_file_path, self.output_dir)
                        except Exception:
                            md_rel = os.path.basename(md_file_path)
                        file_path = os.path.join(self.source_dir, md_rel.replace('.md', '.py'))
                        filename = os.path.basename(md_file_path)
                        # metadata용 소스 상대 경로
                        rel_base = self.source_dir.rstrip(os.sep) + os.sep
                        relative_path = file_path[len(rel_base):] if file_path.startswith(rel_base) else os.path.basename(file_path)
                    else:
                        file_path = "unknown"
                        filename = "unknown"
                        relative_path = "unknown"
            
                # source 경로(브라우징/가중치용) 구성: generated_docs/<project>/<relative_md_path>
                md_relative_path = None
                if md_file_path and self.output_dir in md_file_path:
                    try:
                        md_relative_path = os.path.relpath(md_file_path, self.output_dir)
                    except Exception:
                        md_relative_path = os.path.basename(md_file_path)

                # 색인 강화: 동의어 키워드 프리픽스 추가
                enable_prefix = os.getenv("ENABLE_CONTENT_KEYWORD_PREFIX", "false").lower() == "true"
                keyword_prefix = (
                    "키워드: 오류, 에러, 이슈, 유형, 타입, 종류, 검출, 탐지, 감지, 분석, 추가, 확장, 등록, detect, detection, analyze, analysis, overview, summary, system, class, function, method, complexity, metrics\n\n"
                )
                content_to_store = (keyword_prefix + section_content) if enable_prefix else section_content

                # ID 생성 개선 (더 고유한 ID)
                section_id = f"section_{len(sections)}_{hash(title) % 10000:04d}"
                if related_chunk:
                    section_id = f"{related_chunk.chunk_id}_{len(sections)}"

                sections.append({
                    "content": content_to_store,
                    "metadata": {
                        "chunk_type": chunk_type,
                        "filename": filename,
                        "source_file": relative_path,
                        "project": project_name,
                        "section_title": title,
                        "section_level": title_level if 'title_level' in locals() else 3,
                        "section_type": section_type,
                        "chunk_id": related_chunk.chunk_id if related_chunk else section_id,
                        "id_hint": section_id,
                        "folder_priority": self._get_folder_priority(file_path),
                        "tags": related_chunk.tags if related_chunk else [],
                        "keywords": "오류, 에러, 이슈, 유형, 타입, 종류, 검출, 탐지, 감지, 분석, 추가, 확장, 등록, detect, detection, analyze, analysis, overview, summary, system, class, function, method, complexity, metrics",
                        "docstring": related_chunk.docstring if related_chunk and related_chunk.docstring else "",
                        "description": related_chunk.docstring if related_chunk and related_chunk.docstring else "",
                        "source": f"generated_docs/{project_name}/{md_relative_path}" if md_relative_path else f"generated_docs/{project_name}/{filename}",
                        "name": related_chunk.name if related_chunk else title
                    }
                })
                
                print(f"       ✅ 섹션 추가됨: {chunk_type} - {title[:40]}... (ID: {section_id})")
        
        print(f"   📊 섹션 분할 완료: {total_sections_processed}/{total_sections_found}개 처리됨")
        
        # 개요 섹션도 추가 (파일 상단 부분) - 개선된 패턴
        overview_pattern = r'^(.*?)(?=^#{1,6}|\Z)'
        overview_match = re.match(overview_pattern, content, re.MULTILINE | re.DOTALL)
        if overview_match:
            overview_content = overview_match.group(1).strip()
            if len(overview_content) > 50:  # 최소 길이 완화
                print(f"     📋 개요 섹션 발견: {len(overview_content)}자")
                # 개요 섹션의 메타데이터도 통일
                file_path = file_chunks[0].file_path if file_chunks else "unknown"
                filename = os.path.basename(file_path).replace('.py', '.md') if file_chunks else "unknown"
                relative_path = file_path.split('sample_code/')[-1] if 'sample_code/' in file_path else file_path
                project_name = "sample_code" if file_chunks else "unknown"
                
                # 프로젝트 루트 기준 source 경로 계산
                md_relative_path = None
                if md_file_path and self.output_dir in md_file_path:
                    try:
                        md_relative_path = os.path.relpath(md_file_path, self.output_dir)
                    except Exception:
                        md_relative_path = os.path.basename(md_file_path)

                enable_prefix = os.getenv("ENABLE_CONTENT_KEYWORD_PREFIX", "false").lower() == "true"
                keyword_prefix = (
                    "키워드: 개요, overview, summary, 시스템, system, 프로젝트, project, 구조, 구조도, analyzers, formatters, streaming, shared, utils\n\n"
                )
                overview_content_to_store = (keyword_prefix + overview_content) if enable_prefix else overview_content
                
                # 개요 섹션 ID 생성
                overview_id = f"overview_{len(sections)}_{hash(overview_content[:100]) % 10000:04d}"
                
                sections.insert(0, {
                    "content": overview_content_to_store,
                    "metadata": {
                        "chunk_type": "overview",
                        "filename": filename,
                        "source_file": relative_path,
                        "project": project_name,
                        "section_title": "File Overview",
                        "section_level": 0,
                        "section_type": "overview",
                        "chunk_id": overview_id,
                        "id_hint": overview_id,
                        "folder_priority": self._get_folder_priority(file_path),
                        "tags": ["overview", "file_info"],
                        "keywords": "개요, overview, summary, 시스템, system, 프로젝트, project, 구조, 구조도, analyzers, formatters, streaming, shared, utils",
                        "source": f"generated_docs/{project_name}/{md_relative_path}" if md_relative_path else f"generated_docs/{project_name}/{filename}",
                        "name": "file_overview"
                    }
                })
                
                print(f"       ✅ 개요 섹션 추가됨: overview - File Overview (ID: {overview_id})")
        
        return sections
    
    def _split_project_summary_into_sections(self, content: str) -> List[Dict[str, Any]]:
        """프로젝트 요약을 섹션별로 분할"""
        
        sections = []
        
        # 제목별로 섹션 분할 (## 기준)
        section_pattern = r'^(##\s+.*?)(?=^##|\Z)'
        matches = re.finditer(section_pattern, content, re.MULTILINE | re.DOTALL)
        
        for i, match in enumerate(matches):
            section_content = match.group(1).strip()
            if len(section_content) < 50:  # 너무 짧은 섹션 제외
                continue
            
            # 섹션 제목 추출
            title_match = re.match(r'^##\s+(.+)', section_content)
            title = title_match.group(1) if title_match else f"Section {i+1}"
            
            enable_prefix = os.getenv("ENABLE_CONTENT_KEYWORD_PREFIX", "false").lower() == "true"
            keyword_prefix = (
                "키워드: 프로젝트, 요약, 개요, overview, summary, 시스템, 구조, 구조도, 분석, analyzers, formatters, streaming, shared, utils\n\n"
            )
            section_content_to_store = (keyword_prefix + section_content) if enable_prefix else section_content
            sections.append({
                "content": section_content_to_store,
                "metadata": {
                    "chunk_type": "overview",
                    "filename": "project_summary.md",
                    "source_file": "project_summary.md",
                    "project": os.path.basename(self.source_dir),
                    "section_title": title,
                    "chunk_id": f"project_summary_section_{i}",
                    "folder_priority": 10,  # 프로젝트 요약은 최고 우선순위
                    "tags": ["project", "summary", "overview"],
                    "keywords": "프로젝트, 요약, 개요, overview, summary, 시스템, 구조, 분석, analyzers, formatters, streaming, shared, utils",
                    "source": f"generated_docs/{os.path.basename(self.source_dir)}/project_summary.md",
                    "name": title
                }
            })
        
        return sections
    
    def _find_related_chunk(self, title: str, content: str, file_chunks: List[CodeChunk]) -> CodeChunk:
        """섹션과 관련된 chunk 찾기"""
        
        # 제목에서 함수/클래스명 추출
        function_match = re.search(r'`([^`]+)`', title)
        if function_match:
            function_name = function_match.group(1)
            for chunk in file_chunks:
                if chunk.name == function_name:
                    return chunk
        
        # 내용에서 chunk ID 찾기
        chunk_id_match = re.search(r'🆔\s*\*\*ID\*\*:\s*`([^`]+)`', content)
        if chunk_id_match:
            chunk_id = chunk_id_match.group(1)
            for chunk in file_chunks:
                if chunk.chunk_id == chunk_id:
                    return chunk
        
        return None
    
    def _get_folder_priority(self, file_path: str) -> int:
        """폴더 우선순위 계산"""
        
        priority_map = {
            "analyzers": 9,
            "formatters": 8,
            "streaming": 7,
            "shared": 6,
            "service": 6,
            "models": 5,
            "utils": 4,
            "exceptions": 3
        }
        
        for folder, priority in priority_map.items():
            if folder in file_path:
                return priority
        
        return 1  # 기본 우선순위
    
    async def _upload_section_to_rag(self, session: aiohttp.ClientSession, 
                                   rag_base_url: str, section: Dict[str, Any]) -> bool:
        """개별 섹션을 RAG에 업로드"""
        
        try:
            upload_data = {
                "id": section.get("metadata", {}).get("id_hint"),
                "content": section["content"],
                "metadata": section["metadata"]
            }
            
            async with session.post(
                f"{rag_base_url}/api/v1/documents",
                json=upload_data,
                timeout=aiohttp.ClientTimeout(total=10)
            ) as response:
                
                if response.status == 200:
                    return True
                else:
                    error_text = await response.text()
                    print(f"     ❌ RAG 업로드 실패 (HTTP {response.status}): {error_text}")
                    print(f"     📝 실패한 섹션 ID: {section.get('metadata', {}).get('id_hint', 'unknown')}")
                    return False
                    
        except Exception as e:
            print(f"     ❌ RAG 업로드 예외: {e}")
            return False


async def generate_template_chunk_documents(source_dir: str, output_dir: str, template_dir: str = None) -> Dict[str, Any]:
    """템플릿 기반 chunk 문서 생성 메인 함수"""
    
    generator = TemplateChunkGenerator(source_dir, output_dir, template_dir)
    return await generator.generate_documents(upload_to_rag=True)


async def upload_generated_documents_to_rag(source_dir: str, output_dir: str, template_dir: str = None) -> Dict[str, Any]:
    """이미 생성된 MD 문서를 RAG에만 업로드 (단독 실행)"""
    try:
        # 청크 재구성 (섹션 매핑을 위해 필요)
        all_chunks = chunk_directory(source_dir, max_tokens=600)

        # 업로드 대상 MD 파일 수집
        md_files = []
        for root, dirs, files in os.walk(output_dir):
            for f in files:
                if f.endswith('.md') and f != 'project_summary.md':
                    md_files.append(os.path.join(root, f))

        summary_file = os.path.join(output_dir, 'project_summary.md')
        if not os.path.exists(summary_file):
            summary_file = None

        renderer = TemplateChunkGenerator(source_dir, output_dir, template_dir)

        rag_base_url = os.getenv("RAG_SERVICE_URL", "http://localhost:8003")
        upload_details = []
        import aiohttp
        async with aiohttp.ClientSession() as session:
            # 1) 개별 파일 업로드
            for md in md_files:
                file_result = await renderer._upload_single_md_file_sections(session, rag_base_url, md, all_chunks)
                upload_details.append(file_result)
            # 2) 프로젝트 요약 업로드
            if summary_file:
                summary_result = await renderer._upload_project_summary_sections(session, rag_base_url, summary_file)
                upload_details.append(summary_result)

        return {
            "success": True,
            "uploaded_files": len(upload_details),
            "details": upload_details
        }
    except Exception as e:
        return {"success": False, "error": str(e), "details": []}


if __name__ == "__main__":
    import asyncio
    
    # 기존 generated_docs 폴더에 업데이트
    source_dir = "/Users/roseline/projects/codemuse-backend/sample_code"
    output_dir = "/Users/roseline/projects/codemuse-backend/generated_docs"
    
    result = asyncio.run(generate_template_chunk_documents(source_dir, output_dir))
    print(f"\n🎯 최종 결과: {result}")
