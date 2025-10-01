"""
언어별 Chunker 팩토리 및 통합 관리자
"""

import os
import logging
from typing import List, Dict, Any, Optional, Tuple
from .base_chunker import BaseChunker, CodeChunk
from .python_chunker import ASTChunker as PythonChunker
from .javascript_chunker import JavaScriptChunker
from .java_chunker import JavaChunker
from .enterprise_chunker import EnterpriseChunker
from ..project_detector import ProjectDetector

# 로거 설정
logger = logging.getLogger("chunker_document_generator")


class ChunkerFactory:
    """언어별 Chunker 팩토리 (프로젝트 구조 기반)"""
    
    _chunkers = {
        'python': PythonChunker,
        'javascript': JavaScriptChunker,
        'java': JavaChunker,
        'enterprise': EnterpriseChunker
    }
    
    def __init__(self, project_root: str = None):
        self.project_root = project_root or os.getcwd()
        self.project_detector = ProjectDetector(self.project_root)
        self._processing_hints = self.project_detector.get_processing_hints()
    
    def get_chunker(self, file_path: str) -> Tuple[Optional[BaseChunker], str, Optional[str]]:
        """파일 경로에 따라 적절한 Chunker 반환 (언어, 프레임워크 정보 포함)"""
        should_process, language, framework = self.project_detector.should_process_file(file_path)
        
        if not should_process:
            return None, language, framework
        
        # 엔터프라이즈 애플리케이션 감지
        if self._is_enterprise_application(file_path, language, framework):
            chunker = EnterpriseChunker(self.project_root)
            return chunker, language, framework
        
        if language in self._chunkers:
            chunker_class = self._chunkers[language]
            chunker = chunker_class()
            
            # 프레임워크별 특화 설정 적용
            if framework:
                self._apply_framework_config(chunker, language, framework)
            
            return chunker, language, framework
        
        return None, language, framework
    
    def _is_enterprise_application(self, file_path: str, language: str, framework: str) -> bool:
        """엔터프라이즈 애플리케이션인지 감지"""
        # Spring Boot + Vue.js 조합 감지
        if (language == 'java' and framework == 'spring-boot') or \
           (language in ['javascript', 'typescript', 'vue'] and framework == 'vue'):
            return True
        
        # 엔터프라이즈 패턴 감지
        enterprise_patterns = [
            'controller', 'service', 'entity', 'dto', 'repository',
            'component', 'store', 'api', 'business', 'domain'
        ]
        
        file_path_lower = file_path.lower()
        pattern_count = sum(1 for pattern in enterprise_patterns if pattern in file_path_lower)
        
        return pattern_count >= 2
    
    def _apply_framework_config(self, chunker: BaseChunker, language: str, framework: str):
        """프레임워크별 특화 설정 적용"""
        if language == 'java' and framework == 'spring-boot':
            # Spring Boot 특화 설정
            spring_info = self.project_detector.get_spring_boot_info()
            if spring_info:
                # Spring Boot 관련 태그 우선순위 설정
                if hasattr(chunker, 'spring_annotation_priority'):
                    chunker.spring_annotation_priority = True
        
        elif language == 'javascript' and framework == 'vue':
            # Vue.js 특화 설정
            vue_info = self.project_detector.get_vue_info()
            if vue_info:
                # Vue 컴포넌트 구조 우선순위 설정
                if hasattr(chunker, 'vue_component_priority'):
                    chunker.vue_component_priority = True
    
    @classmethod
    def get_supported_extensions(cls) -> List[str]:
        """지원하는 모든 파일 확장자 목록 반환"""
        extensions = []
        for chunker_class in cls._chunkers.values():
            chunker = chunker_class()
            extensions.extend(chunker.supported_extensions)
        return list(set(extensions))
    
    def get_project_analysis(self) -> Dict[str, Any]:
        """프로젝트 분석 결과 반환"""
        return {
            'project_type': self._processing_hints['project_type'],
            'framework_info': self._processing_hints['framework_info'],
            'detected_configs': self._processing_hints['detected_configs'],
            'processing_hints': self._processing_hints
        }


class MultiLanguageChunker:
    """다중 언어 파일 처리 통합 관리자 (프로젝트 구조 기반)"""
    
    def __init__(self, max_tokens: int = 600, project_root: str = None):
        self.max_tokens = max_tokens
        self.factory = ChunkerFactory(project_root)
        self.project_analysis = self.factory.get_project_analysis()
        
        logger.info(f"🔍 프로젝트 분석 결과:")
        logger.info(f"   📁 프로젝트 타입: {self.project_analysis['project_type']}")
        logger.info(f"   🏗️ 프레임워크: {self.project_analysis['framework_info']}")
        logger.info(f"   ⚙️ 감지된 설정: {self.project_analysis['detected_configs']}")
    
    def chunk_file(self, file_path: str) -> List[CodeChunk]:
        """파일을 처리하여 코드 청크들을 생성 (프로젝트 구조 기반)"""
        chunker, language, framework = self.factory.get_chunker(file_path)
        
        if not chunker:
            logger.warning(f"⚠️ 지원하지 않는 파일 형식: {file_path}")
            return []
        
        logger.info(f"📄 Processing: {file_path} [{language}" + (f"/{framework}" if framework else "") + "]")
        
        chunks = chunker.chunk_file(file_path)
        
        # 프레임워크 정보를 청크에 추가
        for chunk in chunks:
            if framework:
                chunk.framework = framework
        
        logger.info(f"   ✅ Generated {len(chunks)} chunks")
        return chunks
    
    def chunk_directory(self, directory_path: str) -> List[CodeChunk]:
        """디렉토리 내 모든 지원 파일을 chunk로 변환 (프로젝트 구조 기반)"""
        import os
        all_chunks = []
        
        # 프로젝트 타입에 따른 디렉토리 우선순위 설정
        priority_dirs = self._get_priority_directories()
        
        for root, dirs, files in os.walk(directory_path):
            # 프로젝트별 제외 디렉토리 설정
            dirs[:] = self._filter_directories(dirs, root)
            
            # 우선순위 디렉토리 체크
            is_priority = any(priority in root for priority in priority_dirs)
            
            for file in files:
                file_path = os.path.join(root, file)
                if self._should_process_file(file_path):
                    chunks = self.chunk_file(file_path)
                    all_chunks.extend(chunks)
        
        print(f"\n🎯 총 {len(all_chunks)} 개의 chunk 생성 완료")
        return all_chunks
    
    def _get_priority_directories(self) -> List[str]:
        """프로젝트 타입별 우선순위 디렉토리 반환"""
        project_type = self.project_analysis['project_type']
        
        if project_type == 'java':
            return ['src/main/java', 'src/test/java']
        elif project_type == 'javascript':
            return ['src', 'components', 'pages', 'views']
        elif project_type == 'python':
            return ['src', 'app', 'main']
        else:
            return ['src']
    
    def _filter_directories(self, dirs: List[str], root: str) -> List[str]:
        """제외할 디렉토리 필터링"""
        excluded_dirs = {
            '__pycache__', 'node_modules', '.git', '.vscode', 
            'target', 'build', 'dist', '.next', '.nuxt',
            'venv', 'env', '.env', 'coverage', '.pytest_cache'
        }
        
        return [d for d in dirs if d not in excluded_dirs]
    
    def _should_process_file(self, file_path: str) -> bool:
        """파일을 처리해야 하는지 확인 (프로젝트 구조 기반)"""
        should_process, language, framework = self.factory.project_detector.should_process_file(file_path)
        return should_process
    
    def get_language_stats(self, chunks: List[CodeChunk]) -> Dict[str, int]:
        """언어별 통계 반환"""
        stats = {}
        for chunk in chunks:
            language = getattr(chunk, 'language', 'unknown')
            stats[language] = stats.get(language, 0) + 1
        return stats
    
    def get_framework_stats(self, chunks: List[CodeChunk]) -> Dict[str, int]:
        """프레임워크별 통계 반환"""
        stats = {}
        for chunk in chunks:
            framework = getattr(chunk, 'framework', None)
            if framework:
                stats[framework] = stats.get(framework, 0) + 1
        return stats
    
    def get_project_summary(self, chunks: List[CodeChunk]) -> Dict[str, Any]:
        """프로젝트 요약 정보 반환"""
        language_stats = self.get_language_stats(chunks)
        framework_stats = self.get_framework_stats(chunks)
        
        return {
            'project_analysis': self.project_analysis,
            'total_chunks': len(chunks),
            'language_distribution': language_stats,
            'framework_distribution': framework_stats,
            'processing_insights': self._generate_processing_insights(chunks)
        }
    
    def _generate_processing_insights(self, chunks: List[CodeChunk]) -> Dict[str, Any]:
        """처리 인사이트 생성"""
        insights = {
            'spring_boot_components': 0,
            'vue_components': 0,
            'python_modules': 0,
            'complexity_high': 0,
            'has_tests': 0
        }
        
        for chunk in chunks:
            # Spring Boot 컴포넌트 카운트
            if (chunk.language == 'java' and 
                chunk.framework == 'spring-boot' and 
                chunk.chunk_type == 'class'):
                insights['spring_boot_components'] += 1
            
            # Vue 컴포넌트 카운트
            elif (chunk.language == 'javascript' and 
                  chunk.framework == 'vue' and 
                  chunk.chunk_type in ['template', 'script']):
                insights['vue_components'] += 1
            
            # Python 모듈 카운트
            elif chunk.language == 'python' and chunk.chunk_type == 'overview':
                insights['python_modules'] += 1
            
            # 복잡도 높은 코드
            if chunk.complexity and chunk.complexity > 10:
                insights['complexity_high'] += 1
            
            # 테스트 코드
            if 'test' in chunk.tags or 'test' in chunk.name.lower():
                insights['has_tests'] += 1
        
        return insights
