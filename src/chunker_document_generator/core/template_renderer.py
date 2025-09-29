"""
Jinja2 기반 템플릿 렌더링 엔진
"""

import os
from typing import Dict, List, Any, Tuple, Optional
from jinja2 import Environment, FileSystemLoader
from datetime import datetime


class ChunkTemplateRenderer:
    """Chunk 문서 템플릿 렌더링"""
    
    def __init__(self, template_dir: str = None):
        if template_dir is None:
            # 현재 파일과 같은 디렉토리의 templates 폴더
            current_dir = os.path.dirname(os.path.abspath(__file__))
            template_dir = os.path.join(current_dir, "templates")
        
        self.env = Environment(
            loader=FileSystemLoader(template_dir),
            trim_blocks=True,
            lstrip_blocks=True,
            auto_reload=True,
            cache_size=0
        )
        
        # 커스텀 필터 등록
        self.env.filters['number_format'] = self._number_format
        self.env.filters['round'] = round
    
    def _number_format(self, value: int) -> str:
        """숫자를 천 단위 구분자로 포맷팅"""
        if isinstance(value, (int, float)):
            return f"{value:,}"
        return str(value)
    
    def render_file_document(self, file_path: str, chunks: List[Any], source_dir: str) -> str:
        """파일 문서 렌더링 (언어별 템플릿 선택)"""
        
        # 언어별 템플릿 선택
        template_name = self._get_template_for_file(file_path, chunks)
        template = self.env.get_template(template_name)
        
        # 템플릿 데이터 준비
        context = self._prepare_file_context(file_path, chunks, source_dir)
        
        # 엔터프라이즈 정보 추가
        if self._is_enterprise_application(file_path, chunks):
            context.update(self._extract_enterprise_info(chunks))
        
        return template.render(**context)
    
    def _get_template_for_file(self, file_path: str, chunks: List[Any]) -> str:
        """파일 확장자와 내용에 따라 적절한 템플릿 선택"""
        
        file_ext = os.path.splitext(file_path)[1].lower()
        
        # 엔터프라이즈 애플리케이션 감지
        if self._is_enterprise_application(file_path, chunks):
            if file_ext == '.java':
                return 'java/enterprise_java_file_document.j2'
            elif file_ext in ['.js', '.jsx', '.ts', '.tsx', '.vue']:
                return 'javascript/enterprise_vue_file_document.j2'
        
        # 언어별 템플릿 매핑
        if file_ext == '.py':
            return 'python/file_document.j2'
        elif file_ext in ['.js', '.jsx', '.ts', '.tsx']:
            return 'javascript/js_file_document.j2'
        elif file_ext == '.vue':
            return 'javascript/js_file_document.j2'  # Vue도 JavaScript 템플릿 사용
        elif file_ext == '.java':
            return 'java/java_file_document.j2'
        elif file_ext in ['.css', '.scss', '.sass', '.less']:
            return 'css/css_file_document.j2'
        else:
            # 기본값은 Python 템플릿
            return 'python/file_document.j2'
    
    def _is_enterprise_application(self, file_path: str, chunks: List[Any]) -> bool:
        """엔터프라이즈 애플리케이션인지 감지"""
        file_path_lower = file_path.lower()
        
        # Spring Boot + Vue.js 조합 감지
        enterprise_patterns = [
            'controller', 'service', 'entity', 'dto', 'repository',
            'component', 'store', 'api', 'business', 'domain'
        ]
        
        pattern_count = sum(1 for pattern in enterprise_patterns if pattern in file_path_lower)
        
        # Chunk에서 엔터프라이즈 패턴 감지
        enterprise_chunk_count = 0
        for chunk in chunks:
            if hasattr(chunk, 'framework') and chunk.framework in ['spring-boot', 'vue']:
                enterprise_chunk_count += 1
            if hasattr(chunk, 'chunk_type') and chunk.chunk_type in ['class', 'component']:
                if any(pattern in chunk.name.lower() for pattern in enterprise_patterns):
                    enterprise_chunk_count += 1
        
        return pattern_count >= 2 or enterprise_chunk_count >= 3
    
    def _extract_enterprise_info(self, chunks: List[Any]) -> Dict[str, Any]:
        """엔터프라이즈 정보 추출"""
        try:
            from .chunkers.enterprise_chunker import EnterpriseChunker
            enterprise_chunker = EnterpriseChunker()
            
            # 각 청크에서 엔터프라이즈 정보 추출
            all_enterprise_info = {
                'api_endpoints': [],
                'database_entities': [],
                'business_logic': [],
                'component_info': None,
                'store_usage': [],
                'api_integration': []
            }
            
            for chunk in chunks:
                enterprise_info = enterprise_chunker.extract_enterprise_info(chunk)
                for key, value in enterprise_info.items():
                    if value:
                        if isinstance(value, list):
                            all_enterprise_info[key].extend(value)
                        else:
                            all_enterprise_info[key] = value
            
            return all_enterprise_info
        except ImportError:
            return {}
    
    def render_project_summary(self, project_data: Dict[str, Any]) -> str:
        """프로젝트 요약 렌더링"""
        
        template = self.env.get_template('common/project_summary.j2')
        
        # 현재 날짜 추가
        project_data['current_date'] = datetime.now().strftime("%Y-%m-%d")
        
        return template.render(**project_data)
    
    def render_project_stack_summary(self, project_analysis: Dict[str, Any]) -> str:
        """프로젝트 스택 정보 렌더링"""
        
        template = self.env.get_template('common/project_stack_summary.j2')
        
        # 현재 날짜 추가
        project_analysis['current_date'] = datetime.now().strftime("%Y-%m-%d")
        
        return template.render(**project_analysis)
    
    def render_tech_stack_detector(self, tech_analysis: Dict[str, Any]) -> str:
        """기술 스택 감지 결과 렌더링"""
        
        template = self.env.get_template('common/tech_stack_detector.j2')
        
        return template.render(**tech_analysis)
    
    def render_enterprise_architecture(self, architecture_data: Dict[str, Any]) -> str:
        """엔터프라이즈 아키텍처 분석 렌더링"""
        
        template = self.env.get_template('common/enterprise_architecture.j2')
        
        # 현재 날짜 추가
        architecture_data['current_date'] = datetime.now().strftime("%Y-%m-%d")
        
        return template.render(**architecture_data)
    
    def render_business_logic_analysis(self, business_data: Dict[str, Any]) -> str:
        """비즈니스 로직 분석 렌더링"""
        
        template = self.env.get_template('common/business_logic_analysis.j2')
        
        # 현재 날짜 추가
        business_data['current_date'] = datetime.now().strftime("%Y-%m-%d")
        
        return template.render(**business_data)
    
    def _prepare_file_context(self, file_path: str, chunks: List[Any], source_dir: str) -> Dict[str, Any]:
        """파일 템플릿 컨텍스트 준비"""
        
        file_name = os.path.basename(file_path)
        relative_path = os.path.relpath(file_path, source_dir)
        
        # 언어 및 프레임워크 정보 추출
        language, framework = self._extract_language_framework(file_path, chunks)
        
        # Chunk 분류 (언어별로 다르게 처리)
        overview_chunk = next((c for c in chunks if c.chunk_type == "overview"), None)
        
        if language == 'java':
            class_chunks = [c for c in chunks if c.chunk_type == "class"]
            interface_chunks = [c for c in chunks if c.chunk_type == "interface"]
            enum_chunks = [c for c in chunks if c.chunk_type == "enum"]
            function_chunks = [c for c in chunks if c.chunk_type in ["function", "method"]]
            method_chunks = []
        elif language in ['javascript', 'vue']:
            class_chunks = [c for c in chunks if c.chunk_type == "class"]
            component_chunks = [c for c in chunks if c.chunk_type in ["vue_script", "vue_template", "vue_style"]]
            function_chunks = [c for c in chunks if c.chunk_type in ["function", "method"]]
            method_chunks = []
        else:  # Python
            class_chunks = [c for c in chunks if c.chunk_type == "class"]
            function_chunks = [c for c in chunks if c.chunk_type in ["function", "async_function"]]
            method_chunks = [c for c in chunks if c.chunk_type == "method"]
            interface_chunks = []
            enum_chunks = []
            component_chunks = []
        
        # 클래스별 처리
        processed_classes = []
        for class_chunk in class_chunks:
            processed_class = self._process_class_chunk(class_chunk, method_chunks, relative_path)
            processed_classes.append(processed_class)
        
        # 함수별 처리
        processed_functions = []
        for func_chunk in function_chunks:
            processed_func = self._process_function_chunk(func_chunk, relative_path)
            processed_functions.append(processed_func)
        
        # 인터페이스별 처리 (Java)
        processed_interfaces = []
        if language == 'java' and 'interface_chunks' in locals():
            for interface_chunk in interface_chunks:
                processed_interface = self._process_class_chunk(interface_chunk, [], relative_path)
                processed_interfaces.append(processed_interface)
        
        # 열거형별 처리 (Java)
        processed_enums = []
        if language == 'java' and 'enum_chunks' in locals():
            for enum_chunk in enum_chunks:
                processed_enum = self._process_class_chunk(enum_chunk, [], relative_path)
                processed_enums.append(processed_enum)
        
        # 컴포넌트별 처리 (Vue/React)
        processed_components = []
        if language in ['javascript', 'vue'] and 'component_chunks' in locals():
            for component_chunk in component_chunks:
                processed_component = self._process_class_chunk(component_chunk, [], relative_path)
                processed_components.append(processed_component)
        
        # 파일 개요 정보
        info_cards, info_table = self._prepare_overview_info(chunks, overview_chunk)
        
        # 시각화 데이터
        visualizations = self._prepare_visualizations(chunks, class_chunks, function_chunks, method_chunks, file_name, overview_chunk)
        
        # 퍼포먼스 메트릭스
        performance_metrics, metrics_table, quality_chart = self._prepare_performance_metrics(chunks)
        
        # Chunk 요약
        chunk_summary = self._prepare_chunk_summary(chunks)
        
        return {
            'file_name': file_name,
            'relative_path': relative_path,
            'current_date': datetime.now().strftime("%Y-%m-%d"),
            'total_chunks': len(chunks),
            'language': language,
            'framework': framework,
            'overview_chunk': overview_chunk,
            'class_chunks': processed_classes,
            'function_chunks': processed_functions,
            'interface_chunks': processed_interfaces if 'interface_chunks' in locals() else [],
            'enum_chunks': processed_enums if 'enum_chunks' in locals() else [],
            'component_chunks': processed_components if 'component_chunks' in locals() else [],
            'info_cards': info_cards,
            'info_table': info_table,
            'visualizations': bool(visualizations),
            'performance_metrics': bool(performance_metrics),
            'metrics_table': metrics_table,
            'quality_chart': quality_chart,
            'chunk_summary': chunk_summary,
            **visualizations
        }
    
    def _process_class_chunk(self, class_chunk: Any, method_chunks: List[Any], relative_path: str) -> Dict[str, Any]:
        """클래스 chunk 처리"""
        
        # 배지 생성
        badges = []
        if hasattr(class_chunk, 'base_classes') and class_chunk.base_classes:
            badges.append(f"![상속](https://img.shields.io/badge/상속-{len(class_chunk.base_classes)}개-blue)")
        
        if hasattr(class_chunk, 'decorators') and class_chunk.decorators:
            badges.append(f"![데코레이터](https://img.shields.io/badge/데코레이터-{len(class_chunk.decorators)}개-green)")
        
        class_methods = [m for m in method_chunks if hasattr(m, 'parent') and m.parent == class_chunk.name]
        if class_methods:
            badges.append(f"![메서드](https://img.shields.io/badge/메서드-{len(class_methods)}개-orange)")
        
        # 정보 테이블
        info_table = self._create_class_info_table(class_chunk)
        
        # 메서드 테이블
        methods_table = self._create_methods_table(class_methods) if class_methods else None

        # 메서드 상세 (함수 처리 로직 재사용)
        methods_details = None
        if class_methods:
            processed = [self._process_function_chunk(m, relative_path) for m in class_methods]
            # 복잡도 높은 순으로 정렬 (모든 메서드 포함)
            processed.sort(key=lambda x: (x.get('complexity') or 0), reverse=True)
            methods_details = processed  # 모든 메서드 포함
        
        # 태그 표시
        tags_display = ', '.join(class_chunk.tags[:5]) if hasattr(class_chunk, 'tags') else ""
        if hasattr(class_chunk, 'tags') and len(class_chunk.tags) > 5:
            tags_display += '...'
        
        return {
            **class_chunk.__dict__,
            'badges': ' '.join(badges) if badges else None,
            'info_table': info_table,
            'methods_table': methods_table,
            'methods_details': methods_details,
            'tags_display': tags_display
        }
    
    def _process_function_chunk(self, func_chunk: Any, relative_path: str = None) -> Dict[str, Any]:
        """함수 chunk 처리"""
        
        # 배지 생성
        func_badges = []
        if hasattr(func_chunk, 'is_async') and func_chunk.is_async:
            func_badges.append("![Async](https://img.shields.io/badge/async-함수-purple)")
        
        if hasattr(func_chunk, 'is_generator') and func_chunk.is_generator:
            func_badges.append("![Generator](https://img.shields.io/badge/generator-함수-yellow)")
        
        if hasattr(func_chunk, 'decorators') and func_chunk.decorators:
            func_badges.append(f"![데코레이터](https://img.shields.io/badge/데코레이터-{len(func_chunk.decorators)}개-green)")
        
        complexity = getattr(func_chunk, 'complexity', 0) or 0
        complexity_color = "red" if complexity > 10 else "orange" if complexity > 5 else "green"
        func_badges.append(f"![복잡도](https://img.shields.io/badge/복잡도-{complexity}-{complexity_color})")
        
        # 정보 테이블
        info_table = self._create_function_info_table(func_chunk)
        
        # 태그 표시
        tags_display = ', '.join(func_chunk.tags[:5]) if hasattr(func_chunk, 'tags') else ""
        if hasattr(func_chunk, 'tags') and len(func_chunk.tags) > 5:
            tags_display += '...'
        
        return {
            **func_chunk.__dict__,
            'badges': ' '.join(func_badges) if func_badges else None,
            'info_table': info_table,
            'tags_display': tags_display
        }
    
    def _create_class_info_table(self, class_chunk: Any) -> str:
        """클래스 정보 테이블 생성"""
        
        rows = []
        
        if hasattr(class_chunk, 'base_classes') and class_chunk.base_classes:
            rows.append(("🧬 상속", f"`{' → '.join(class_chunk.base_classes)}`"))
        
        if hasattr(class_chunk, 'decorators') and class_chunk.decorators:
            rows.append(("🎨 데코레이터", f"`{'`, `'.join(class_chunk.decorators)}`"))
        
        if hasattr(class_chunk, 'complexity') and class_chunk.complexity:
            rows.append(("⚡ 복잡도", str(class_chunk.complexity)))
        
        if not rows:
            return None
        
        table = "| 속성 | 값 |\n|------|----|\n"
        for key, value in rows:
            table += f"| {key} | {value} |\n"
        
        return table
    
    def _create_function_info_table(self, func_chunk: Any) -> str:
        """함수 정보 테이블 생성"""
        
        table = "| 속성 | 값 |\n|------|----|\n"
        
        if hasattr(func_chunk, 'decorators') and func_chunk.decorators:
            table += f"| 🎨 데코레이터 | `{'`, `'.join(func_chunk.decorators)}` |\n"
        
        complexity = getattr(func_chunk, 'complexity', 0) or 0
        table += f"| ⚡ 복잡도 | {complexity} |\n"
        
        token_count = getattr(func_chunk, 'token_count', 0) or 0
        table += f"| 📊 토큰 수 | {token_count} |\n"
        
        line_range = getattr(func_chunk, 'line_range', 'N/A')
        table += f"| 📍 라인 범위 | {line_range} |\n"
        
        return table
    
    def _create_methods_table(self, methods: List[Any]) -> str:
        """메서드 목록 테이블 생성"""
        
        table = "| 메서드 | 타입 | 복잡도 | 설명 |\n"
        table += "|--------|------|--------|------|\n"
        
        for method in sorted(methods, key=lambda x: x.name):
            method_type = []
            if hasattr(method, 'is_async') and method.is_async:
                method_type.append("async")
            if hasattr(method, 'is_generator') and method.is_generator:
                method_type.append("generator")
            
            if method.name.startswith('__'):
                method_type.append("magic")
            elif method.name.startswith('_'):
                method_type.append("private")
            else:
                method_type.append("public")
            
            type_str = " ".join(method_type)
            complexity_str = str(method.complexity) if hasattr(method, 'complexity') and method.complexity else "-"
            
            # docstring 첫 줄
            description = ""
            if hasattr(method, 'docstring') and method.docstring:
                first_line = method.docstring.split('\n')[0].strip()
                description = first_line[:50] + "..." if len(first_line) > 50 else first_line
            
            table += f"| `{method.name}` | {type_str} | {complexity_str} | {description} |\n"
        
        return table
    
    def _prepare_overview_info(self, chunks: List[Any], overview_chunk: Any) -> tuple:
        """파일 개요 정보 준비"""
        
        if not overview_chunk:
            return None, None
        
        info_cards = []
        
        # 의존성 정보
        if hasattr(overview_chunk, 'dependencies') and overview_chunk.dependencies:
            deps_formatted = " • ".join(f"`{dep}`" for dep in overview_chunk.dependencies[:6])
            if len(overview_chunk.dependencies) > 6:
                deps_formatted += f" 외 {len(overview_chunk.dependencies) - 6}개"
            info_cards.append(f"📦 **의존성**: {deps_formatted}")
        
        # 복잡도 정보
        total_complexity = sum(getattr(c, 'complexity', 0) or 0 for c in chunks)
        if total_complexity > 0:
            info_cards.append(f"⚡ **총 복잡도**: {total_complexity}")
        
        # 토큰 정보
        total_tokens = sum(getattr(c, 'token_count', 0) or 0 for c in chunks)
        if total_tokens > 0:
            info_cards.append(f"📊 **총 토큰 수**: {total_tokens:,}")
        
        # 비동기 함수 수
        async_count = len([c for c in chunks if hasattr(c, 'is_async') and c.is_async])
        if async_count > 0:
            info_cards.append(f"🔄 **비동기 함수**: {async_count}개")
        
        if not info_cards:
            return None, None
        
        # 테이블 생성
        table = "| | |\n|--|--|\n"
        for i in range(0, len(info_cards), 2):
            left = info_cards[i]
            right = info_cards[i+1] if i+1 < len(info_cards) else ""
            table += f"| {left} | {right} |\n"
        
        return info_cards, table
    
    def _prepare_visualizations(self, chunks: List[Any], class_chunks: List[Any], 
                              function_chunks: List[Any], method_chunks: List[Any], 
                              file_name: str, overview_chunk: Any) -> Dict[str, Any]:
        """시각화 데이터 준비"""
        
        visualizations = {}
        
        # 복잡도 분포 차트
        complexity_data = self._analyze_complexity_distribution(chunks)
        if complexity_data:
            chart = "pie title 복잡도 분포\n"
            for level, count in complexity_data.items():
                chart += f'    "{level}" : {count}\n'
            visualizations['complexity_chart'] = chart
        
        # 함수 유형 분석
        function_types = self._analyze_function_types(chunks)
        if function_types:
            chart = "pie title 함수 유형 분포\n"
            for func_type, count in function_types.items():
                chart += f'    "{func_type}" : {count}\n'
            visualizations['function_types_chart'] = chart

        # 파일 단위 호출 시퀀스 다이어그램(요약) - 함수 + 메서드 모두 포함
        combined_fn = (function_chunks or []) + (method_chunks or [])
        seq = self._build_sequence_diagram(combined_fn)
        if seq:
            visualizations['file_sequence'] = seq
        
        return visualizations

    def _build_sequence_diagram(self, function_chunks: List[Any]) -> str:
        try:
            if not function_chunks:
                return None
            # 참가자 수 제한
            participants = set()
            edges = []
            for f in function_chunks:
                fname = getattr(f, 'name', None)
                if not fname:
                    continue
                participants.add(fname)
                calls = getattr(f, 'calls', []) or []
                for c in calls[:5]:  # 함수당 상위 5개만
                    participants.add(c)
                    edges.append((fname, c))
            # 제한
            participants = list(participants)[:12]
            limited_edges = []
            seen = set()
            for a,b in edges:
                if a in participants and b in participants:
                    key = (a,b)
                    if key not in seen:
                        limited_edges.append(key)
                        seen.add(key)
                if len(limited_edges) >= 25:
                    break
            # 빌드
            lines = ["sequenceDiagram"]
            for p in participants:
                alias = p if len(p) <= 24 else p[:24]
                lines.append(f"  participant {alias} as {p}")
            for a,b in limited_edges:
                lines.append(f"  {a}->>{b}: call")
            return "\n".join(lines)
        except Exception:
            return None
    
    def _prepare_performance_metrics(self, chunks: List[Any]) -> tuple:
        """퍼포먼스 메트릭스 준비"""
        
        metrics = self._calculate_file_metrics(chunks)
        
        # 메트릭스 테이블
        table = f"| 🎯 메트릭 | 📊 값 | 🚦 상태 |\n"
        table += "|-----------|-------|--------|\n"
        table += f"| **총 라인 수** | {metrics['total_lines']} | {self._get_status_indicator(metrics['total_lines'], [100, 500])} |\n"
        table += f"| **평균 복잡도** | {metrics['avg_complexity']:.1f} | {self._get_status_indicator(metrics['avg_complexity'], [3, 7])} |\n"
        table += f"| **최대 복잡도** | {metrics['max_complexity']} | {self._get_status_indicator(metrics['max_complexity'], [10, 20])} |\n"
        table += f"| **함수 밀도** | {metrics['function_density']:.1f}% | {self._get_status_indicator(metrics['function_density'], [20, 40])} |\n"
        
        # 품질 차트 (Mermaid pie chart로 변경 - 더 안정적)
        quality_chart = "pie title 코드 복잡도 분포\n"
        
        # 복잡도별 함수 분포 계산
        low_complexity = sum(1 for chunk in chunks if hasattr(chunk, 'complexity') and chunk.complexity and chunk.complexity <= 3)
        medium_complexity = sum(1 for chunk in chunks if hasattr(chunk, 'complexity') and chunk.complexity and 3 < chunk.complexity <= 7)
        high_complexity = sum(1 for chunk in chunks if hasattr(chunk, 'complexity') and chunk.complexity and chunk.complexity > 7)
        
        if low_complexity + medium_complexity + high_complexity > 0:
            quality_chart += f'    "낮은 복잡도 (1-3)" : {low_complexity}\n'
            quality_chart += f'    "중간 복잡도 (4-7)" : {medium_complexity}\n'
            quality_chart += f'    "높은 복잡도 (8+)" : {high_complexity}\n'
        else:
            quality_chart += '    "복잡도 데이터 없음" : 1\n'
        
        return True, table, quality_chart
    
    def _prepare_chunk_summary(self, chunks: List[Any]) -> str:
        """Chunk 요약 준비"""
        
        chunk_types = {}
        total_tokens = 0
        
        for chunk in chunks:
            chunk_type = chunk.chunk_type
            if chunk_type not in chunk_types:
                chunk_types[chunk_type] = {"count": 0, "total_complexity": 0, "total_tokens": 0}
            
            chunk_types[chunk_type]["count"] += 1
            chunk_types[chunk_type]["total_complexity"] += getattr(chunk, 'complexity', 0) or 0
            chunk_types[chunk_type]["total_tokens"] += getattr(chunk, 'token_count', 0) or 0
            total_tokens += getattr(chunk, 'token_count', 0) or 0
        
        # 평균 계산
        for data in chunk_types.values():
            data["avg_complexity"] = data["total_complexity"] / data["count"] if data["count"] > 0 else 0
        
        summary = f"이 파일은 총 **{len(chunks)}개의 chunk**로 구성되어 있으며, **{total_tokens:,}개의 토큰**을 포함합니다.\n\n"
        summary += "| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |\n"
        summary += "|---------------|--------|-------------|----------|--------|\n"
        
        type_names = {
            "overview": "📋 파일 개요",
            "class": "🏗️ 클래스",
            "function": "⚙️ 함수",
            "async_function": "🔄 비동기 함수",
            "method": "🔧 메서드"
        }
        
        for chunk_type, data in chunk_types.items():
            type_name = type_names.get(chunk_type, chunk_type)
            percentage = (data["total_tokens"] / total_tokens * 100) if total_tokens > 0 else 0
            summary += f"| {type_name} | {data['count']} | {data['avg_complexity']:.1f} | {data['total_tokens']:,} | {percentage:.1f}% |\n"
        
        return summary
    
    def _analyze_complexity_distribution(self, chunks: List[Any]) -> Dict[str, int]:
        """복잡도 분포 분석"""
        
        distribution = {"낮음 (1-3)": 0, "보통 (4-7)": 0, "높음 (8-15)": 0, "매우 높음 (16+)": 0}
        
        for chunk in chunks:
            complexity = getattr(chunk, 'complexity', 0) or 1
            if complexity <= 3:
                distribution["낮음 (1-3)"] += 1
            elif complexity <= 7:
                distribution["보통 (4-7)"] += 1
            elif complexity <= 15:
                distribution["높음 (8-15)"] += 1
            else:
                distribution["매우 높음 (16+)"] += 1
        
        return {k: v for k, v in distribution.items() if v > 0}
    
    def _analyze_function_types(self, chunks: List[Any]) -> Dict[str, int]:
        """함수 유형 분석"""
        
        types = {"일반 함수": 0, "비동기 함수": 0, "메서드": 0, "매직 메서드": 0, "프라이빗 메서드": 0}
        
        for chunk in chunks:
            if chunk.chunk_type in ["function", "async_function"]:
                if hasattr(chunk, 'is_async') and chunk.is_async:
                    types["비동기 함수"] += 1
                else:
                    types["일반 함수"] += 1
            elif chunk.chunk_type == "method":
                if chunk.name.startswith('__') and chunk.name.endswith('__'):
                    types["매직 메서드"] += 1
                elif chunk.name.startswith('_'):
                    types["프라이빗 메서드"] += 1
                else:
                    types["메서드"] += 1
        
        return {k: v for k, v in types.items() if v > 0}
    
    def _calculate_file_metrics(self, chunks: List[Any]) -> Dict[str, float]:
        """파일 메트릭스 계산"""
        
        total_lines = 0
        complexities = []
        function_count = 0
        
        for chunk in chunks:
            if hasattr(chunk, 'line_range') and chunk.line_range and '-' in chunk.line_range:
                try:
                    start, end = chunk.line_range.split('-')
                    if end != 'end':
                        lines = int(end) - int(start) + 1
                        total_lines += lines
                except:
                    pass
            
            if hasattr(chunk, 'complexity') and chunk.complexity:
                complexities.append(chunk.complexity)
            
            if chunk.chunk_type in ["function", "async_function", "method"]:
                function_count += 1
        
        avg_complexity = sum(complexities) / len(complexities) if complexities else 0
        max_complexity = max(complexities) if complexities else 0
        function_density = (function_count / len(chunks) * 100) if chunks else 0
        
        return {
            "total_lines": total_lines,
            "avg_complexity": avg_complexity,
            "max_complexity": max_complexity,
            "function_density": function_density
        }
    
    def _get_status_indicator(self, value: float, thresholds: List[float]) -> str:
        """상태 표시기 반환"""
        
        if value <= thresholds[0]:
            return "🟢 양호"
        elif value <= thresholds[1]:
            return "🟡 보통"
        else:
            return "🔴 주의"
    
    def _extract_language_framework(self, file_path: str, chunks: List[Any]) -> Tuple[str, Optional[str]]:
        """파일에서 언어와 프레임워크 정보 추출"""
        
        file_ext = os.path.splitext(file_path)[1].lower()
        
        # 파일 확장자 기반 언어 감지
        if file_ext == '.py':
            language = 'python'
        elif file_ext in ['.js', '.jsx', '.ts', '.tsx']:
            language = 'javascript'
        elif file_ext == '.vue':
            language = 'vue'
        elif file_ext == '.java':
            language = 'java'
        elif file_ext in ['.css', '.scss', '.sass', '.less']:
            language = 'css'
        else:
            language = 'unknown'
        
        # Chunk에서 프레임워크 정보 추출
        framework = None
        if chunks:
            # 첫 번째 chunk에서 언어/프레임워크 정보 확인
            first_chunk = chunks[0]
            if hasattr(first_chunk, 'language'):
                language = first_chunk.language
            if hasattr(first_chunk, 'framework'):
                framework = first_chunk.framework
        
        return language, framework
