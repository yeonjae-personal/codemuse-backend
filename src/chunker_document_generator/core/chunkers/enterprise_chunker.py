"""
엔터프라이즈 애플리케이션 특화 Chunker
Vizier(sample)와 같은 엔터프라이즈 웹 애플리케이션을 위한 고급 분석 기능
"""

import re
import ast
from typing import Dict, List, Any, Optional, Tuple
from dataclasses import dataclass
from .base_chunker import BaseChunker, CodeChunk


@dataclass
class APIEndpoint:
    """API 엔드포인트 정보"""
    method: str
    path: str
    description: str
    parameters: List[str]
    response_type: str
    auth_required: bool = False
    line_number: int = 0


@dataclass
class DatabaseEntity:
    """데이터베이스 엔티티 정보"""
    name: str
    table_name: str
    key_fields: List[str]
    relationships: List[str]
    line_number: int = 0


@dataclass
class BusinessLogic:
    """비즈니스 로직 정보"""
    name: str
    domain: str
    purpose: str
    complexity: int
    related_files: List[str]
    line_number: int = 0


@dataclass
class ComponentInfo:
    """Vue.js 컴포넌트 정보"""
    name: str
    props: List[str]
    emits: List[str]
    api_calls: List[str]
    store_usage: List[str]
    parent: Optional[str] = None
    children: List[str] = None


class EnterpriseChunker(BaseChunker):
    """엔터프라이즈 애플리케이션 특화 Chunker"""
    
    def __init__(self, project_root: str = None):
        super().__init__(project_root)
        self.api_patterns = {
            'spring_boot': [
                r'@(GetMapping|PostMapping|PutMapping|DeleteMapping|RequestMapping)\s*\(\s*["\']([^"\']+)["\']',
                r'@RequestMapping\s*\(\s*value\s*=\s*["\']([^"\']+)["\']'
            ],
            'swagger': [
                r'@Operation\s*\(\s*summary\s*=\s*["\']([^"\']+)["\']',
                r'@ApiOperation\s*\(\s*value\s*=\s*["\']([^"\']+)["\']'
            ]
        }
        
        self.business_domains = [
            'product', 'offer', 'price', 'customer', 'order', 'payment',
            'subscription', 'billing', 'user', 'auth', 'admin', 'relation',
            'dependency', 'extend', 'multi-entity', 'impact-analysis'
        ]
    
    def _get_supported_extensions(self) -> List[str]:
        """지원하는 파일 확장자 목록 반환"""
        return ['.py', '.java', '.js', '.jsx', '.vue', '.ts', '.tsx']
    
    def chunk_file(self, file_path: str) -> List[CodeChunk]:
        """파일을 처리하여 코드 청크들을 생성"""
        import os
        from .python_chunker import ASTChunker as PythonChunker
        from .javascript_chunker import JavaScriptChunker
        from .java_chunker import JavaChunker
        
        # 파일 확장자에 따라 적절한 chunker 선택
        file_ext = os.path.splitext(file_path)[1].lower()
        
        if file_ext == '.py':
            chunker = PythonChunker()
        elif file_ext in ['.js', '.jsx', '.vue', '.ts', '.tsx']:
            chunker = JavaScriptChunker()
        elif file_ext == '.java':
            chunker = JavaChunker()
        else:
            return []
        
        # 기본 chunker로 파일 처리
        chunks = chunker.chunk_file(file_path)
        
        # 엔터프라이즈 정보 추가
        for chunk in chunks:
            enterprise_info = self.extract_enterprise_info(chunk)
            chunk.enterprise_info = enterprise_info
        
        return chunks
    
    def extract_enterprise_info(self, chunk: CodeChunk) -> Dict[str, Any]:
        """엔터프라이즈 정보 추출"""
        info = {
            'api_endpoints': [],
            'database_entities': [],
            'business_logic': [],
            'component_info': None,
            'store_usage': [],
            'api_integration': []
        }
        
        if chunk.language == 'java':
            info['api_endpoints'] = self._extract_spring_boot_endpoints(chunk)
            info['database_entities'] = self._extract_database_entities(chunk)
            info['business_logic'] = self._extract_business_logic(chunk)
        elif chunk.language in ['javascript', 'typescript', 'vue']:
            info['component_info'] = self._extract_vue_component_info(chunk)
            info['store_usage'] = self._extract_pinia_usage(chunk)
            info['api_integration'] = self._extract_api_calls(chunk)
        
        return info
    
    def _extract_spring_boot_endpoints(self, chunk: CodeChunk) -> List[APIEndpoint]:
        """Spring Boot API 엔드포인트 추출"""
        endpoints = []
        
        if chunk.chunk_type != 'class' or 'Controller' not in chunk.name:
            return endpoints
        
        content = chunk.content
        lines = content.split('\n')
        
        # 클래스 레벨 RequestMapping 추출
        class_mapping = self._extract_class_mapping(content)
        
        for i, line in enumerate(lines):
            # 메서드 레벨 매핑 추출
            method_mapping = self._extract_method_mapping(line)
            if method_mapping:
                endpoint = self._build_api_endpoint(
                    method_mapping, class_mapping, line, i + 1, lines
                )
                if endpoint:
                    endpoints.append(endpoint)
        
        return endpoints
    
    def _extract_class_mapping(self, content: str) -> str:
        """클래스 레벨 RequestMapping 추출"""
        pattern = r'@RequestMapping\s*\(\s*["\']([^"\']+)["\']'
        match = re.search(pattern, content)
        return match.group(1) if match else ""
    
    def _extract_method_mapping(self, line: str) -> Optional[Tuple[str, str]]:
        """메서드 레벨 매핑 추출"""
        patterns = [
            (r'@GetMapping\s*\(\s*["\']([^"\']+)["\']', 'GET'),
            (r'@PostMapping\s*\(\s*["\']([^"\']+)["\']', 'POST'),
            (r'@PutMapping\s*\(\s*["\']([^"\']+)["\']', 'PUT'),
            (r'@DeleteMapping\s*\(\s*["\']([^"\']+)["\']', 'DELETE'),
            (r'@RequestMapping\s*\(\s*value\s*=\s*["\']([^"\']+)["\']', 'REQUEST')
        ]
        
        for pattern, method in patterns:
            match = re.search(pattern, line)
            if match:
                return method, match.group(1)
        return None
    
    def _build_api_endpoint(self, method_mapping: Tuple[str, str], class_mapping: str, 
                          line: str, line_num: int, lines: List[str]) -> Optional[APIEndpoint]:
        """API 엔드포인트 객체 생성"""
        method, path = method_mapping
        full_path = class_mapping + path if class_mapping else path
        
        # 메서드명 추출
        method_name_match = re.search(r'public\s+\w+\s+(\w+)\s*\(', line)
        method_name = method_name_match.group(1) if method_name_match else "unknown"
        
        # 설명 추출 (Swagger 어노테이션)
        description = self._extract_swagger_description(lines, line_num)
        
        # 파라미터 추출
        parameters = self._extract_method_parameters(line)
        
        # 응답 타입 추출
        response_type = self._extract_response_type(line)
        
        return APIEndpoint(
            method=method,
            path=full_path,
            description=description or f"{method_name} 메서드",
            parameters=parameters,
            response_type=response_type,
            line_number=line_num
        )
    
    def _extract_swagger_description(self, lines: List[str], line_num: int) -> str:
        """Swagger 설명 추출"""
        # 현재 라인부터 위로 5라인까지 검색
        for i in range(max(0, line_num - 5), line_num):
            line = lines[i]
            # @Operation 어노테이션에서 summary 추출
            match = re.search(r'@Operation\s*\(\s*summary\s*=\s*["\']([^"\']+)["\']', line)
            if match:
                return match.group(1)
        return ""
    
    def _extract_method_parameters(self, line: str) -> List[str]:
        """메서드 파라미터 추출"""
        # @RequestParam, @PathVariable, @RequestBody 등 추출
        params = []
        param_patterns = [
            r'@RequestParam\s*\(\s*["\']([^"\']+)["\']',
            r'@PathVariable\s*\(\s*["\']([^"\']+)["\']',
            r'@RequestBody\s+(\w+)',
            r'@RequestParam\s+(\w+)',
            r'@PathVariable\s+(\w+)'
        ]
        
        for pattern in param_patterns:
            matches = re.findall(pattern, line)
            params.extend(matches)
        
        return params
    
    def _extract_response_type(self, line: str) -> str:
        """응답 타입 추출"""
        # 메서드 시그니처에서 반환 타입 추출
        match = re.search(r'public\s+(\w+)\s+\w+\s*\(', line)
        return match.group(1) if match else "void"
    
    def _extract_database_entities(self, chunk: CodeChunk) -> List[DatabaseEntity]:
        """데이터베이스 엔티티 추출"""
        entities = []
        
        if chunk.chunk_type != 'class' or 'Entity' not in chunk.name:
            return entities
        
        content = chunk.content
        lines = content.split('\n')
        
        # 테이블명 추출
        table_name = self._extract_table_name(content)
        
        # 주요 필드 추출
        key_fields = self._extract_key_fields(content)
        
        # 관계 추출
        relationships = self._extract_relationships(content)
        
        # line_range에서 시작 라인 번호 추출
        start_line = int(chunk.line_range.split('-')[0]) if '-' in chunk.line_range else 1
        
        entity = DatabaseEntity(
            name=chunk.name,
            table_name=table_name,
            key_fields=key_fields,
            relationships=relationships,
            line_number=start_line
        )
        
        entities.append(entity)
        return entities
    
    def _extract_table_name(self, content: str) -> str:
        """테이블명 추출"""
        # @Table 어노테이션에서 name 추출
        match = re.search(r'@Table\s*\(\s*name\s*=\s*["\']([^"\']+)["\']', content)
        if match:
            return match.group(1)
        
        # 클래스명을 snake_case로 변환
        class_name = re.search(r'class\s+(\w+)', content)
        if class_name:
            return self._camel_to_snake(class_name.group(1))
        
        return "unknown"
    
    def _extract_key_fields(self, content: str) -> List[str]:
        """주요 필드 추출"""
        fields = []
        
        # @Id 어노테이션이 있는 필드들
        id_pattern = r'@Id\s*\n\s*private\s+\w+\s+(\w+)'
        matches = re.findall(id_pattern, content, re.MULTILINE)
        fields.extend(matches)
        
        # @Column 어노테이션이 있는 필드들
        column_pattern = r'@Column\s*\(\s*name\s*=\s*["\']([^"\']+)["\']'
        matches = re.findall(column_pattern, content)
        fields.extend(matches)
        
        return list(set(fields))  # 중복 제거
    
    def _extract_relationships(self, content: str) -> List[str]:
        """관계 추출"""
        relationships = []
        
        # JPA 관계 어노테이션들
        relation_patterns = [
            r'@OneToMany\s*\(\s*mappedBy\s*=\s*["\']([^"\']+)["\']',
            r'@ManyToOne\s*\(\s*mappedBy\s*=\s*["\']([^"\']+)["\']',
            r'@OneToOne\s*\(\s*mappedBy\s*=\s*["\']([^"\']+)["\']',
            r'@ManyToMany\s*\(\s*mappedBy\s*=\s*["\']([^"\']+)["\']'
        ]
        
        for pattern in relation_patterns:
            matches = re.findall(pattern, content)
            relationships.extend(matches)
        
        return relationships
    
    def _extract_business_logic(self, chunk: CodeChunk) -> List[BusinessLogic]:
        """비즈니스 로직 추출"""
        logic_list = []
        
        if chunk.chunk_type not in ['class', 'function']:
            return logic_list
        
        # 도메인 감지
        domain = self._detect_business_domain(chunk)
        
        # 목적 추출
        purpose = self._extract_business_purpose(chunk)
        
        # 관련 파일 추출
        related_files = self._extract_related_files(chunk)
        
        # line_range에서 시작 라인 번호 추출
        start_line = int(chunk.line_range.split('-')[0]) if '-' in chunk.line_range else 1
        
        logic = BusinessLogic(
            name=chunk.name,
            domain=domain,
            purpose=purpose,
            complexity=chunk.complexity or 0,
            related_files=related_files,
            line_number=start_line
        )
        
        logic_list.append(logic)
        return logic_list
    
    def _detect_business_domain(self, chunk: CodeChunk) -> str:
        """비즈니스 도메인 감지"""
        file_path = chunk.file_path.lower()
        chunk_name = chunk.name.lower()
        
        for domain in self.business_domains:
            if domain in file_path or domain in chunk_name:
                return domain
        
        # 파일 경로에서 도메인 추출
        if 'ui/prod' in file_path:
            return 'product_ui'
        elif 'online/prod' in file_path:
            return 'product_online'
        elif 'extends' in file_path:
            return 'extends_management'
        elif 'relation' in file_path:
            return 'relation_management'
        
        return 'general'
    
    def _extract_business_purpose(self, chunk: CodeChunk) -> str:
        """비즈니스 목적 추출"""
        # 클래스명에서 목적 추출
        if 'Service' in chunk.name:
            return f"{chunk.name} 비즈니스 로직 처리"
        elif 'Controller' in chunk.name:
            return f"{chunk.name} API 엔드포인트 제공"
        elif 'Entity' in chunk.name:
            return f"{chunk.name} 데이터 모델 정의"
        elif 'Dto' in chunk.name:
            return f"{chunk.name} 데이터 전송 객체"
        
        # 메서드명에서 목적 추출
        if chunk.chunk_type == 'function':
            method_name = chunk.name.lower()
            if 'get' in method_name:
                return "데이터 조회"
            elif 'save' in method_name or 'create' in method_name:
                return "데이터 저장"
            elif 'update' in method_name:
                return "데이터 수정"
            elif 'delete' in method_name or 'remove' in method_name:
                return "데이터 삭제"
        
        return "비즈니스 로직 처리"
    
    def _extract_related_files(self, chunk: CodeChunk) -> List[str]:
        """관련 파일 추출"""
        related_files = []
        file_path = chunk.file_path
        
        # 같은 패키지의 다른 파일들
        package_path = '/'.join(file_path.split('/')[:-1])
        if package_path:
            related_files.append(f"{package_path}/*")
        
        return related_files
    
    def _extract_vue_component_info(self, chunk: CodeChunk) -> Optional[ComponentInfo]:
        """Vue.js 컴포넌트 정보 추출"""
        if chunk.language != 'vue' or chunk.chunk_type != 'component':
            return None
        
        content = chunk.content
        
        return ComponentInfo(
            name=self._extract_component_name(content),
            props=self._extract_props(content),
            emits=self._extract_emits(content),
            api_calls=self._extract_api_calls_from_vue(content),
            store_usage=self._extract_pinia_usage_from_vue(content),
            parent=self._extract_parent_component(content),
            children=self._extract_child_components(content)
        )
    
    def _extract_component_name(self, content: str) -> str:
        """컴포넌트명 추출"""
        # <script> 태그에서 name 속성 추출
        match = re.search(r'name\s*:\s*["\']([^"\']+)["\']', content)
        if match:
            return match.group(1)
        
        # 파일명에서 추출
        return "UnknownComponent"
    
    def _extract_props(self, content: str) -> List[str]:
        """Props 추출"""
        props = []
        
        # defineProps에서 추출
        props_match = re.search(r'defineProps<\{([^}]+)\}>', content, re.DOTALL)
        if props_match:
            props_content = props_match.group(1)
            prop_matches = re.findall(r'(\w+)\s*:', props_content)
            props.extend(prop_matches)
        
        # props 옵션에서 추출
        props_match = re.search(r'props\s*:\s*\{([^}]+)\}', content, re.DOTALL)
        if props_match:
            props_content = props_match.group(1)
            prop_matches = re.findall(r'(\w+)\s*:', props_content)
            props.extend(prop_matches)
        
        return list(set(props))
    
    def _extract_emits(self, content: str) -> List[str]:
        """Emits 추출"""
        emits = []
        
        # defineEmits에서 추출
        emits_match = re.search(r'defineEmits<\{([^}]+)\}>', content, re.DOTALL)
        if emits_match:
            emits_content = emits_match.group(1)
            emit_matches = re.findall(r'(\w+)\s*:', emits_content)
            emits.extend(emit_matches)
        
        # emits 옵션에서 추출
        emits_match = re.search(r'emits\s*:\s*\[([^\]]+)\]', content)
        if emits_match:
            emits_content = emits_match.group(1)
            emit_matches = re.findall(r'["\']([^"\']+)["\']', emits_content)
            emits.extend(emit_matches)
        
        return list(set(emits))
    
    def _extract_api_calls_from_vue(self, content: str) -> List[str]:
        """Vue 컴포넌트에서 API 호출 추출"""
        api_calls = []
        
        # httpClient 호출 추출
        http_patterns = [
            r'httpClient\.(get|post|put|delete)\s*\(\s*["\']([^"\']+)["\']',
            r'await\s+httpClient\.(get|post|put|delete)',
            r'\.(get|post|put|delete)\s*\(\s*["\']([^"\']+)["\']'
        ]
        
        for pattern in http_patterns:
            matches = re.findall(pattern, content)
            for match in matches:
                if isinstance(match, tuple):
                    api_calls.append(f"{match[0].upper()} {match[1]}")
                else:
                    api_calls.append(f"{match.upper()} API")
        
        return list(set(api_calls))
    
    def _extract_pinia_usage_from_vue(self, content: str) -> List[str]:
        """Vue 컴포넌트에서 Pinia 사용 추출"""
        store_usage = []
        
        # useStore 패턴 추출
        store_patterns = [
            r'use(\w+)Store',
            r'const\s+(\w+)\s*=\s*use(\w+)Store',
            r'from\s+["\']@/store/(\w+)\.store["\']'
        ]
        
        for pattern in store_patterns:
            matches = re.findall(pattern, content)
            store_usage.extend(matches)
        
        return list(set(store_usage))
    
    def _extract_parent_component(self, content: str) -> Optional[str]:
        """부모 컴포넌트 추출"""
        # import에서 부모 컴포넌트 추출
        import_match = re.search(r'import\s+(\w+)\s+from\s+["\']@/components/([^"\']+)["\']', content)
        if import_match:
            return import_match.group(1)
        return None
    
    def _extract_child_components(self, content: str) -> List[str]:
        """자식 컴포넌트 추출"""
        children = []
        
        # <template>에서 컴포넌트 태그 추출
        template_match = re.search(r'<template[^>]*>(.*?)</template>', content, re.DOTALL)
        if template_match:
            template_content = template_match.group(1)
            component_matches = re.findall(r'<([A-Z][a-zA-Z0-9]+)', template_content)
            children.extend(component_matches)
        
        return list(set(children))
    
    def _extract_api_calls(self, chunk: CodeChunk) -> List[Dict[str, str]]:
        """API 호출 추출"""
        api_calls = []
        content = chunk.content
        
        # HTTP 클라이언트 호출 패턴
        http_patterns = [
            r'httpClient\.(get|post|put|delete)\s*\(\s*["\']([^"\']+)["\']',
            r'\.(get|post|put|delete)\s*\(\s*["\']([^"\']+)["\']'
        ]
        
        for pattern in http_patterns:
            matches = re.findall(pattern, content)
            for match in matches:
                if isinstance(match, tuple):
                    api_calls.append({
                        'method': match[0].upper(),
                        'endpoint': match[1],
                        'name': f"{match[0].upper()} {match[1]}"
                    })
        
        return api_calls
    
    def _extract_pinia_usage(self, chunk: CodeChunk) -> List[str]:
        """Pinia 사용 추출"""
        store_usage = []
        content = chunk.content
        
        # useStore 패턴 추출
        store_patterns = [
            r'use(\w+)Store',
            r'const\s+(\w+)\s*=\s*use(\w+)Store',
            r'from\s+["\']@/store/(\w+)\.store["\']'
        ]
        
        for pattern in store_patterns:
            matches = re.findall(pattern, content)
            store_usage.extend(matches)
        
        return list(set(store_usage))
    
    def _camel_to_snake(self, name: str) -> str:
        """CamelCase를 snake_case로 변환"""
        s1 = re.sub('(.)([A-Z][a-z]+)', r'\1_\2', name)
        return re.sub('([a-z0-9])([A-Z])', r'\1_\2', s1).lower()
    
    def analyze_project_architecture(self, chunks: List[CodeChunk]) -> Dict[str, Any]:
        """프로젝트 아키텍처 분석"""
        architecture = {
            'frontend_stack': self._analyze_frontend_stack(chunks),
            'backend_stack': self._analyze_backend_stack(chunks),
            'database_stack': self._analyze_database_stack(chunks),
            'deployment_stack': self._analyze_deployment_stack(chunks),
            'api_services': self._analyze_api_services(chunks),
            'business_domains': self._analyze_business_domains(chunks),
            'data_flows': self._analyze_data_flows(chunks)
        }
        
        return architecture
    
    def _analyze_frontend_stack(self, chunks: List[CodeChunk]) -> Dict[str, Any]:
        """프론트엔드 스택 분석"""
        frontend_chunks = [c for c in chunks if c.language in ['javascript', 'typescript', 'vue']]
        
        frameworks = set()
        libraries = set()
        
        for chunk in frontend_chunks:
            if 'vue' in chunk.file_path.lower():
                frameworks.add('Vue.js')
            if 'vuetify' in chunk.content.lower():
                libraries.add('Vuetify')
            if 'pinia' in chunk.content.lower():
                libraries.add('Pinia')
            if 'axios' in chunk.content.lower():
                libraries.add('Axios')
        
        return {
            'frameworks': list(frameworks),
            'libraries': list(libraries),
            'file_count': len(frontend_chunks)
        }
    
    def _analyze_backend_stack(self, chunks: List[CodeChunk]) -> Dict[str, Any]:
        """백엔드 스택 분석"""
        backend_chunks = [c for c in chunks if c.language == 'java']
        
        frameworks = set()
        libraries = set()
        
        for chunk in backend_chunks:
            if 'spring' in chunk.content.lower():
                frameworks.add('Spring Boot')
            if 'mybatis' in chunk.content.lower():
                libraries.add('MyBatis')
            if 'swagger' in chunk.content.lower():
                libraries.add('Swagger')
            if 'postgresql' in chunk.content.lower():
                libraries.add('PostgreSQL')
        
        return {
            'frameworks': list(frameworks),
            'libraries': list(libraries),
            'file_count': len(backend_chunks)
        }
    
    def _analyze_database_stack(self, chunks: List[CodeChunk]) -> Dict[str, Any]:
        """데이터베이스 스택 분석"""
        db_entities = []
        db_operations = []
        
        for chunk in chunks:
            if chunk.chunk_type == 'class' and 'Entity' in chunk.name:
                db_entities.append(chunk.name)
            
            if 'select' in chunk.content.lower() or 'insert' in chunk.content.lower():
                db_operations.append(chunk.name)
        
        return {
            'entities': db_entities,
            'operations': db_operations,
            'entity_count': len(db_entities)
        }
    
    def _analyze_deployment_stack(self, chunks: List[CodeChunk]) -> Dict[str, Any]:
        """배포 스택 분석"""
        docker_files = [c for c in chunks if 'Dockerfile' in c.file_path]
        k8s_files = [c for c in chunks if '.yaml' in c.file_path or '.yml' in c.file_path]
        
        return {
            'docker_files': len(docker_files),
            'k8s_files': len(k8s_files),
            'deployment_ready': len(docker_files) > 0
        }
    
    def _analyze_api_services(self, chunks: List[CodeChunk]) -> List[Dict[str, str]]:
        """API 서비스 분석"""
        services = []
        
        for chunk in chunks:
            if chunk.chunk_type == 'class' and 'Controller' in chunk.name:
                endpoints = self._extract_spring_boot_endpoints(chunk)
                if endpoints:
                    services.append({
                        'name': chunk.name,
                        'base_url': f"/{chunk.name.lower().replace('controller', '')}",
                        'endpoint_count': len(endpoints),
                        'auth_type': 'JWT'  # 기본값
                    })
        
        return services
    
    def _analyze_business_domains(self, chunks: List[CodeChunk]) -> List[Dict[str, Any]]:
        """비즈니스 도메인 분석"""
        domains = {}
        
        for chunk in chunks:
            domain = self._detect_business_domain(chunk)
            if domain not in domains:
                domains[domain] = {
                    'name': domain,
                    'description': f"{domain} 관련 기능",
                    'features': [],
                    'complexity': 0,
                    'file_count': 0
                }
            
            domains[domain]['file_count'] += 1
            domains[domain]['complexity'] += chunk.complexity or 0
            
            if chunk.chunk_type == 'function':
                domains[domain]['features'].append(chunk.name)
        
        return list(domains.values())
    
    def _analyze_data_flows(self, chunks: List[CodeChunk]) -> List[Dict[str, str]]:
        """데이터 플로우 분석"""
        flows = []
        
        # 프론트엔드에서 백엔드로의 API 호출 추출
        frontend_chunks = [c for c in chunks if c.language in ['javascript', 'typescript', 'vue']]
        
        for chunk in frontend_chunks:
            api_calls = self._extract_api_calls(chunk)
            for api_call in api_calls:
                flows.append({
                    'name': f"{chunk.name} → {api_call['endpoint']}",
                    'trigger': chunk.name,
                    'api_endpoint': api_call['endpoint'],
                    'data_type': 'JSON'
                })
        
        return flows
