"""
Java 파일 Chunker
"""

import re
import os
from typing import List, Dict, Any, Optional
from .base_chunker import BaseChunker, CodeChunk


class JavaChunker(BaseChunker):
    """Java 파일 처리"""
    
    def _get_supported_extensions(self) -> List[str]:
        return ['.java']
    
    def chunk_file(self, file_path: str) -> List[CodeChunk]:
        """Java 파일을 chunk로 변환"""
        try:
            with open(file_path, 'r', encoding='utf-8') as f:
                content = f.read()
            
            chunks = []
            
            # 1. Overview chunk 생성
            overview_chunk = self._create_overview_chunk(file_path, content)
            chunks.append(overview_chunk)
            
            # 2. 패키지 및 import 정보
            package_chunk = self._create_package_chunk(file_path, content)
            if package_chunk:
                chunks.append(package_chunk)
            
            # 3. 클래스 추출
            class_chunks = self._extract_classes(file_path, content)
            chunks.extend(class_chunks)
            
            # 4. 인터페이스 추출
            interface_chunks = self._extract_interfaces(file_path, content)
            chunks.extend(interface_chunks)
            
            # 5. 열거형(Enum) 추출
            enum_chunks = self._extract_enums(file_path, content)
            chunks.extend(enum_chunks)
            
            return chunks
            
        except Exception as e:
            print(f"❌ 파일 처리 실패 {file_path}: {e}")
            return []
    
    def _create_overview_chunk(self, file_path: str, content: str) -> CodeChunk:
        """파일 overview chunk 생성"""
        file_name = os.path.basename(file_path)
        
        # 패키지 정보 추출
        package_match = re.search(r'^package\s+([^;]+);', content, re.MULTILINE)
        package_name = package_match.group(1) if package_match else "default"
        
        # import 문 추출
        imports = self._extract_imports(content)
        
        # 클래스/인터페이스/열거형 목록 추출
        classes = self._extract_class_names(content)
        interfaces = self._extract_interface_names(content)
        enums = self._extract_enum_names(content)
        
        # overview 내용 생성
        overview_content = f"# {file_name}\n\n"
        overview_content += f"**패키지**: `{package_name}`\n\n"
        
        if imports:
            overview_content += f"**Import**: {', '.join(imports[:10])}\n\n"
        
        if classes:
            overview_content += f"**클래스**: {', '.join(classes)}\n\n"
        
        if interfaces:
            overview_content += f"**인터페이스**: {', '.join(interfaces)}\n\n"
        
        if enums:
            overview_content += f"**열거형**: {', '.join(enums)}\n\n"
        
        # 태그 생성
        tags = ["java", "class"]
        if 'spring' in content.lower():
            tags.append('spring')
        if 'jpa' in content.lower():
            tags.append('jpa')
        if 'controller' in content.lower():
            tags.append('controller')
        if 'service' in content.lower():
            tags.append('service')
        if 'repository' in content.lower():
            tags.append('repository')
        
        chunk_id = self._generate_chunk_id(file_path, "overview", "file_overview")
        
        return CodeChunk(
            chunk_id=chunk_id,
            file_path=file_path,
            chunk_type="overview",
            name="file_overview",
            line_range="1-end",
            content=overview_content,
            tags=tags,
            dependencies=imports,
            language="java",
            token_count=len(content.split())
        )
    
    def _create_package_chunk(self, file_path: str, content: str) -> Optional[CodeChunk]:
        """패키지 및 import 정보 chunk 생성"""
        package_match = re.search(r'^package\s+([^;]+);', content, re.MULTILINE)
        if not package_match:
            return None
        
        package_name = package_match.group(1)
        imports = self._extract_imports(content)
        
        package_content = f"## 패키지 정보\n\n"
        package_content += f"**패키지명**: `{package_name}`\n\n"
        
        if imports:
            package_content += f"**Import 목록**:\n"
            for imp in imports[:20]:  # 상위 20개만
                package_content += f"- `{imp}`\n"
        
        chunk_id = self._generate_chunk_id(file_path, "package", "package_info")
        
        return CodeChunk(
            chunk_id=chunk_id,
            file_path=file_path,
            chunk_type="package",
            name="package_info",
            line_range="1-imports",
            content=package_content,
            tags=["java", "package", "import"],
            dependencies=imports,
            language="java"
        )
    
    def _extract_classes(self, file_path: str, content: str) -> List[CodeChunk]:
        """클래스 추출 (Spring Boot 어노테이션 고려)"""
        chunks = []
        
        # 클래스 정의 패턴 (public, private, protected, package-private)
        class_pattern = r'^(?:public|private|protected)?\s*(?:static\s+)?(?:final\s+)?(?:abstract\s+)?class\s+(\w+)(?:\s+extends\s+(\w+))?(?:\s+implements\s+([^{]+))?\s*{'
        
        for match in re.finditer(class_pattern, content, re.MULTILINE):
            class_name = match.group(1)
            extends_class = match.group(2)
            implements_interfaces = match.group(3)
            
            # 클래스 내용 추출
            class_content = self._extract_class_content(content, match.start())
            
            # Javadoc 추출
            docstring = self._extract_javadoc(content, match.start())
            
            # Spring Boot 어노테이션 추출
            spring_annotations = self._extract_spring_annotations(class_content)
            
            # 메서드 추출 (Spring Boot 메서드 포함)
            methods = self._extract_methods(class_content)
            
            # 필드 추출 (Spring Boot 필드 포함)
            fields = self._extract_fields(class_content)
            
            # 복잡도 계산
            complexity = self._calculate_complexity(class_content)
            
            # 상속 및 구현 정보
            base_classes = [extends_class] if extends_class else []
            interfaces = [i.strip() for i in implements_interfaces.split(',')] if implements_interfaces else []
            
            # Spring Boot 태그 생성
            tags = ["class", "java"]
            if 'abstract' in match.group(0):
                tags.append('abstract')
            if 'final' in match.group(0):
                tags.append('final')
            if 'static' in match.group(0):
                tags.append('static')
            
            # Spring Boot 특화 태그
            if spring_annotations:
                tags.extend(spring_annotations)
                tags.append('spring-boot')
            
            # Spring Boot 컴포넌트 타입 결정
            component_type = self._determine_spring_component_type(spring_annotations)
            if component_type:
                tags.append(component_type)
            
            chunk_id = self._generate_chunk_id(file_path, "class", class_name)
            
            chunk = CodeChunk(
                chunk_id=chunk_id,
                file_path=file_path,
                chunk_type="class",
                name=class_name,
                line_range=self._get_line_range(content, match.start()),
                content=class_content,
                docstring=docstring,
                complexity=complexity,
                base_classes=base_classes,
                tags=tags,
                language="java",
                framework="spring-boot" if spring_annotations else None,
                token_count=len(class_content.split())
            )
            
            chunks.append(chunk)
        
        return chunks
    
    def _extract_interfaces(self, file_path: str, content: str) -> List[CodeChunk]:
        """인터페이스 추출"""
        chunks = []
        
        interface_pattern = r'^(?:public\s+)?interface\s+(\w+)(?:\s+extends\s+([^{]+))?\s*{'
        
        for match in re.finditer(interface_pattern, content, re.MULTILINE):
            interface_name = match.group(1)
            extends_interfaces = match.group(2)
            
            # 인터페이스 내용 추출
            interface_content = self._extract_class_content(content, match.start())
            
            # Javadoc 추출
            docstring = self._extract_javadoc(content, match.start())
            
            # 메서드 추출
            methods = self._extract_methods(interface_content)
            
            # 복잡도 계산
            complexity = self._calculate_complexity(interface_content)
            
            # 상속 정보
            base_classes = [i.strip() for i in extends_interfaces.split(',')] if extends_interfaces else []
            
            chunk_id = self._generate_chunk_id(file_path, "interface", interface_name)
            
            chunk = CodeChunk(
                chunk_id=chunk_id,
                file_path=file_path,
                chunk_type="interface",
                name=interface_name,
                line_range=self._get_line_range(content, match.start()),
                content=interface_content,
                docstring=docstring,
                complexity=complexity,
                base_classes=base_classes,
                tags=["interface", "java"],
                language="java",
                token_count=len(interface_content.split())
            )
            
            chunks.append(chunk)
        
        return chunks
    
    def _extract_enums(self, file_path: str, content: str) -> List[CodeChunk]:
        """열거형(Enum) 추출"""
        chunks = []
        
        enum_pattern = r'^(?:public\s+)?enum\s+(\w+)(?:\s+implements\s+([^{]+))?\s*{'
        
        for match in re.finditer(enum_pattern, content, re.MULTILINE):
            enum_name = match.group(1)
            implements_interfaces = match.group(2)
            
            # 열거형 내용 추출
            enum_content = self._extract_class_content(content, match.start())
            
            # Javadoc 추출
            docstring = self._extract_javadoc(content, match.start())
            
            # 열거형 값 추출
            enum_values = self._extract_enum_values(enum_content)
            
            # 복잡도 계산
            complexity = self._calculate_complexity(enum_content)
            
            # 구현 인터페이스
            interfaces = [i.strip() for i in implements_interfaces.split(',')] if implements_interfaces else []
            
            chunk_id = self._generate_chunk_id(file_path, "enum", enum_name)
            
            chunk = CodeChunk(
                chunk_id=chunk_id,
                file_path=file_path,
                chunk_type="enum",
                name=enum_name,
                line_range=self._get_line_range(content, match.start()),
                content=enum_content,
                docstring=docstring,
                complexity=complexity,
                base_classes=interfaces,
                tags=["enum", "java"],
                language="java",
                token_count=len(enum_content.split())
            )
            
            chunks.append(chunk)
        
        return chunks
    
    def _extract_class_content(self, content: str, start_pos: int) -> str:
        """클래스/인터페이스/열거형의 전체 내용 추출"""
        # 중괄호 매칭으로 전체 내용 찾기
        brace_count = 0
        start_brace = content.find('{', start_pos)
        if start_brace == -1:
            return ""
        
        for i in range(start_brace, len(content)):
            if content[i] == '{':
                brace_count += 1
            elif content[i] == '}':
                brace_count -= 1
                if brace_count == 0:
                    return content[start_pos:i + 1]
        
        return content[start_pos:]
    
    def _extract_javadoc(self, content: str, start_pos: int) -> str:
        """Javadoc 주석 추출"""
        # 클래스/메서드 정의 이전의 Javadoc 찾기
        lines = content[:start_pos].split('\n')
        doc_lines = []
        
        for i in range(len(lines) - 1, max(0, len(lines) - 20), -1):
            line = lines[i].strip()
            if line.startswith('/**'):
                doc_lines.insert(0, line)
                break
            elif line.startswith('*') and doc_lines:
                doc_lines.insert(0, line)
            elif line and not line.startswith('*') and not line.startswith('//'):
                break
        
        return '\n'.join(doc_lines).strip()
    
    def _extract_methods(self, class_content: str) -> List[str]:
        """메서드명 추출"""
        methods = []
        method_pattern = r'(?:public|private|protected)?\s*(?:static\s+)?(?:final\s+)?(?:abstract\s+)?(?:synchronized\s+)?(?:[\w<>,\s]+\s+)?(\w+)\s*\([^)]*\)\s*(?:throws\s+[^{]+)?\s*{'
        
        for match in re.finditer(method_pattern, class_content):
            methods.append(match.group(1))
        
        return methods
    
    def _extract_fields(self, class_content: str) -> List[str]:
        """필드명 추출"""
        fields = []
        field_pattern = r'(?:public|private|protected)?\s*(?:static\s+)?(?:final\s+)?(?:transient\s+)?(?:volatile\s+)?(?:[\w<>,\s]+\s+)(\w+)\s*[=;]'
        
        for match in re.finditer(field_pattern, class_content):
            fields.append(match.group(1))
        
        return fields
    
    def _extract_enum_values(self, enum_content: str) -> List[str]:
        """열거형 값 추출"""
        values = []
        # 열거형 값은 보통 첫 번째 중괄호 안에 있음
        enum_body_match = re.search(r'{\s*([^}]+)', enum_content)
        if enum_body_match:
            enum_body = enum_body_match.group(1)
            # 쉼표로 구분된 값들 추출
            value_pattern = r'(\w+)(?:\s*\([^)]*\))?(?:\s*,\s*|$)'
            for match in re.finditer(value_pattern, enum_body):
                values.append(match.group(1))
        
        return values
    
    def _extract_imports(self, content: str) -> List[str]:
        """import 문에서 의존성 추출"""
        imports = []
        import_pattern = r'^import\s+(?:static\s+)?([^;]+);'
        
        for match in re.finditer(import_pattern, content, re.MULTILINE):
            imports.append(match.group(1))
        
        return imports
    
    def _extract_class_names(self, content: str) -> List[str]:
        """클래스명 추출"""
        classes = []
        class_pattern = r'class\s+(\w+)'
        for match in re.finditer(class_pattern, content):
            classes.append(match.group(1))
        return classes
    
    def _extract_interface_names(self, content: str) -> List[str]:
        """인터페이스명 추출"""
        interfaces = []
        interface_pattern = r'interface\s+(\w+)'
        for match in re.finditer(interface_pattern, content):
            interfaces.append(match.group(1))
        return interfaces
    
    def _extract_enum_names(self, content: str) -> List[str]:
        """열거형명 추출"""
        enums = []
        enum_pattern = r'enum\s+(\w+)'
        for match in re.finditer(enum_pattern, content):
            enums.append(match.group(1))
        return enums
    
    def _get_line_range(self, content: str, start_pos: int) -> str:
        """위치를 라인 범위로 변환"""
        lines_before = content[:start_pos].count('\n')
        return f"{lines_before + 1}-{lines_before + 1}"
    
    def _extract_spring_annotations(self, content: str) -> List[str]:
        """Spring Boot 어노테이션 추출"""
        annotations = []
        
        # 주요 Spring Boot 어노테이션들
        spring_annotations = [
            '@SpringBootApplication', '@RestController', '@Controller', '@Service', 
            '@Repository', '@Component', '@Configuration', '@Bean', '@Autowired',
            '@Value', '@RequestMapping', '@GetMapping', '@PostMapping', '@PutMapping',
            '@DeleteMapping', '@PathVariable', '@RequestParam', '@RequestBody',
            '@Valid', '@Transactional', '@PreAuthorize', '@PostAuthorize',
            '@Entity', '@Table', '@Id', '@GeneratedValue', '@Column',
            '@OneToMany', '@ManyToOne', '@ManyToMany', '@JoinColumn',
            '@Query', '@Modifying', '@Param', '@EnableJpaRepositories',
            '@EnableWebSecurity', '@EnableGlobalMethodSecurity',
            '@Profile', '@ConditionalOnProperty', '@ConditionalOnClass',
            '@Scheduled', '@Async', '@EventListener', '@Cacheable',
            '@CacheEvict', '@CachePut', '@EnableCaching'
        ]
        
        for annotation in spring_annotations:
            if annotation in content:
                # 어노테이션에서 @ 제거하고 태그로 추가
                tag = annotation[1:].lower().replace('springboot', 'spring-boot')
                annotations.append(tag)
        
        return annotations
    
    def _determine_spring_component_type(self, annotations: List[str]) -> Optional[str]:
        """Spring Boot 컴포넌트 타입 결정"""
        component_mapping = {
            'restcontroller': 'rest-controller',
            'controller': 'mvc-controller', 
            'service': 'service-layer',
            'repository': 'data-layer',
            'component': 'spring-component',
            'configuration': 'configuration-class',
            'springbootapplication': 'main-application'
        }
        
        for annotation in annotations:
            if annotation in component_mapping:
                return component_mapping[annotation]
        
        return None
    
    def _extract_spring_endpoints(self, content: str) -> List[Dict[str, str]]:
        """Spring Boot 엔드포인트 추출"""
        endpoints = []
        
        # @RequestMapping, @GetMapping, @PostMapping 등 패턴
        endpoint_patterns = [
            r'@(?:GetMapping|PostMapping|PutMapping|DeleteMapping|RequestMapping)\s*\(\s*["\']([^"\']+)["\']',
            r'@RequestMapping\s*\(\s*value\s*=\s*["\']([^"\']+)["\']'
        ]
        
        for pattern in endpoint_patterns:
            for match in re.finditer(pattern, content):
                endpoints.append({
                    'path': match.group(1),
                    'method': self._extract_http_method(content, match.start())
                })
        
        return endpoints
    
    def _extract_http_method(self, content: str, annotation_pos: int) -> str:
        """HTTP 메서드 추출"""
        # 어노테이션 앞의 라인에서 HTTP 메서드 찾기
        lines = content[:annotation_pos].split('\n')
        for line in reversed(lines[-5:]):  # 최근 5줄만 확인
            if '@GetMapping' in line:
                return 'GET'
            elif '@PostMapping' in line:
                return 'POST'
            elif '@PutMapping' in line:
                return 'PUT'
            elif '@DeleteMapping' in line:
                return 'DELETE'
            elif '@RequestMapping' in line and 'method' in line:
                # method = RequestMethod.GET 같은 패턴
                method_match = re.search(r'method\s*=\s*RequestMethod\.(\w+)', line)
                if method_match:
                    return method_match.group(1)
        
        return 'GET'  # 기본값
    
    def _extract_spring_dependencies(self, content: str) -> List[str]:
        """Spring Boot 의존성 추출"""
        dependencies = []
        
        # @Autowired, @Value, @Resource 등
        dependency_patterns = [
            r'@Autowired\s+(?:private|public|protected)?\s*(?:static\s+)?(?:final\s+)?(?:[\w<>,\s]+\s+)?(\w+)',
            r'@Value\s*\(\s*["\']([^"\']+)["\']\s*\)',
            r'@Resource\s+(?:private|public|protected)?\s*(?:static\s+)?(?:final\s+)?(?:[\w<>,\s]+\s+)?(\w+)'
        ]
        
        for pattern in dependency_patterns:
            for match in re.finditer(pattern, content):
                dependencies.append(match.group(1))
        
        return dependencies
    
    def _extract_jpa_entities(self, content: str) -> List[Dict[str, str]]:
        """JPA 엔티티 정보 추출"""
        entities = []
        
        # @Entity 클래스 찾기
        entity_pattern = r'@Entity\s+(?:public|private|protected)?\s*(?:static\s+)?(?:final\s+)?(?:abstract\s+)?class\s+(\w+)'
        
        for match in re.finditer(entity_pattern, content):
            class_name = match.group(1)
            
            # @Table 어노테이션에서 테이블명 추출
            table_match = re.search(r'@Table\s*\(\s*name\s*=\s*["\']([^"\']+)["\']', content)
            table_name = table_match.group(1) if table_match else class_name.lower()
            
            entities.append({
                'class_name': class_name,
                'table_name': table_name
            })
        
        return entities
