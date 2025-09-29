"""
Document Generator Service

MD 문서 생성 서비스
"""

import os
import uuid
from typing import Dict, Any
from datetime import datetime
from pathlib import Path
from jinja2 import Template

from .code_analyzer import CodeAnalyzer
from ..models.document_analysis import DocumentAnalysis
from ..models.generation_request import GenerationRequest, GenerationResponse


class DocumentGeneratorService:
    """문서 생성 서비스"""
    
    def __init__(self):
        self.code_analyzer = CodeAnalyzer()
        self.templates = self._load_templates()
    
    def generate_document(self, request: GenerationRequest) -> GenerationResponse:
        """문서 생성"""
        start_time = datetime.now()
        
        # 프로젝트 분석
        analysis = self.code_analyzer.analyze_project(request.project_path)
        
        # 파일별 개별 문서 생성 모드 확인
        if hasattr(request, 'generate_individual_files') and request.generate_individual_files:
            return self._generate_individual_documents(analysis, request, start_time)
        
        # 기존 통합 문서 생성
        content = self._generate_content(analysis, request)
        
        # 파일 저장
        document_id = str(uuid.uuid4())
        file_path = self._save_document(content, document_id, request.output_format)
        
        generation_time = (datetime.now() - start_time).total_seconds()
        
        return GenerationResponse(
            document_id=document_id,
            status="success",
            generated_content=content,
            file_path=file_path,
            generation_time=generation_time,
            metadata={
                "total_files": analysis.total_files,
                "total_lines": analysis.total_lines,
                "languages": analysis.languages,
                "issues_count": len(analysis.issues)
            }
        )
    
    def _generate_content(self, analysis: DocumentAnalysis, request: GenerationRequest) -> str:
        """문서 내용 생성"""
        template_name = f"{request.template_type}_{request.output_format}"
        template = self.templates.get(template_name, self.templates['standard_markdown'])
        
        return template.render(
            analysis=analysis,
            include_metrics=request.include_metrics,
            include_dependencies=request.include_dependencies,
            include_issues=request.include_issues,
            generated_at=datetime.now().isoformat()
        )
    
    def _save_document(self, content: str, document_id: str, output_format: str) -> str:
        """문서 파일 저장"""
        output_dir = "generated_docs"
        os.makedirs(output_dir, exist_ok=True)
        
        extension = "md" if output_format == "markdown" else output_format
        file_path = os.path.join(output_dir, f"{document_id}.{extension}")
        
        with open(file_path, 'w', encoding='utf-8') as f:
            f.write(content)
        
        return file_path
    
    def _load_templates(self) -> Dict[str, Template]:
        """템플릿 로드"""
        templates = {}
        
        # 표준 마크다운 템플릿
        standard_md_template = """
# {{ analysis.project_name }} 프로젝트 문서

**생성일시**: {{ generated_at }}
**분석 파일 수**: {{ analysis.total_files }}개
**총 코드 라인**: {{ analysis.total_lines }}줄

## 프로젝트 개요

이 문서는 {{ analysis.project_name }} 프로젝트의 자동 분석 결과입니다.

```mermaid
graph TD
    A[{{ analysis.project_name }}] --> B[코드 구조]
    A --> C[의존성]
    A --> D[메트릭]
    A --> E[이슈]
    
    B --> B1[파일: {{ analysis.total_files }}개]
    B --> B2[함수: {{ analysis.metrics.total_functions }}개]
    B --> B3[클래스: {{ analysis.metrics.total_classes }}개]
    
    C --> C1[모듈 의존성]
    C --> C2[외부 라이브러리]
    
    D --> D1[복잡도: {{ "%.2f" | format(analysis.metrics.average_complexity) }}]
    D --> D2[이슈 파일: {{ analysis.metrics.files_with_issues }}개]
    
    style A fill:#f96,stroke:#333,stroke-width:2px
    style D1 fill:{% if analysis.metrics.average_complexity > 15 %}#f66{% else %}#6f6{% endif %},stroke:#333
```

### 사용 언어
{% for language, count in analysis.languages.items() %}
- **{{ language }}**: {{ count }}개 파일
{% endfor %}

## 코드 구조 분석

### 파일별 상세 정보
{% for structure in analysis.code_structures %}
### {{ structure.file_path }}

- **언어**: {{ structure.language }}
- **코드 라인**: {{ structure.lines_of_code }}줄
- **복잡도 점수**: {{ structure.complexity_score }}

#### 함수 목록
{% for func in structure.functions %}
- **{{ func.name }}** (라인 {{ func.line_number }})
  {% if func.docstring %}
  - 설명: {{ func.docstring }}
  {% endif %}
  {% if func.args %}
  - 매개변수: {{ func.args | join(', ') }}
  {% endif %}
{% endfor %}

#### 클래스 목록
{% for cls in structure.classes %}
- **{{ cls.name }}** (라인 {{ cls.line_number }})
  {% if cls.docstring %}
  - 설명: {{ cls.docstring }}
  {% endif %}
  {% if cls.bases %}
  - 상속: {{ cls.bases | join(', ') }}
  {% endif %}
{% endfor %}

{% if structure.imports %}
#### Import 목록
{% for imp in structure.imports %}
- {{ imp }}
{% endfor %}
{% endif %}

---
{% endfor %}

{% if include_dependencies %}
## 의존성 그래프

```mermaid
graph LR
{% for edge in analysis.dependency_graph.edges %}
    {{ edge.from | replace('.', '_') }} --> {{ edge.to | replace('.', '_') }}
{% endfor %}
```

### 파일 간 의존성
{% for edge in analysis.dependency_graph.edges %}
- {{ edge.from }} → {{ edge.to }}
{% endfor %}

{% if analysis.dependency_graph.cycles %}
### 순환 의존성 경고

```mermaid
graph TD
{% for cycle in analysis.dependency_graph.cycles %}
{% for i in range(cycle|length - 1) %}
    {{ cycle[i] | replace('.', '_') }} --> {{ cycle[i+1] | replace('.', '_') }}
{% endfor %}
    {{ cycle[-1] | replace('.', '_') }} --> {{ cycle[0] | replace('.', '_') }}
{% endfor %}

    style {{ cycle[0] | replace('.', '_') }} fill:#f66,stroke:#f00,stroke-width:2px
```

{% for cycle in analysis.dependency_graph.cycles %}
- {{ cycle | join(' → ') }}
{% endfor %}
{% endif %}
{% endif %}

{% if include_metrics %}
## 프로젝트 메트릭

```mermaid
pie title 코드 구성 비율
    "함수" : {{ analysis.metrics.total_functions }}
    "클래스" : {{ analysis.metrics.total_classes }}
    "이슈" : {{ analysis.metrics.files_with_issues }}
```

- **총 함수 수**: {{ analysis.metrics.total_functions }}개
- **총 클래스 수**: {{ analysis.metrics.total_classes }}개
- **평균 복잡도**: {{ "%.2f" | format(analysis.metrics.average_complexity) }}
- **이슈가 있는 파일**: {{ analysis.metrics.files_with_issues }}개
{% endif %}

{% if include_issues %}
## 발견된 이슈

{% for issue in analysis.issues %}
### {{ issue.type | title }} ({{ issue.severity }})
- **파일**: {{ issue.file }}
- **메시지**: {{ issue.message }}
{% if issue.function %}
- **함수**: {{ issue.function }}
{% endif %}

{% endfor %}
{% endif %}

---
*이 문서는 CodeMuse Document Generator에 의해 자동 생성되었습니다.*
"""
        
        templates['standard_markdown'] = Template(standard_md_template)
        
        return templates
    
    def _generate_individual_documents(self, analysis: DocumentAnalysis, request: GenerationRequest, start_time: datetime) -> GenerationResponse:
        """파일별 개별 문서 생성 (템플릿 기반 + RAG 자동 업로드)"""
        
        # 🔄 새로운 템플릿 기반 생성기 사용
        try:
            import asyncio
            from ...chunker.template_chunk_generator import generate_template_chunk_documents
            
            # 소스 디렉토리와 출력 디렉토리 설정
            source_dir = str(Path(request.project_path).resolve())
            output_dir = str(Path("generated_docs").resolve())
            
            # 템플릿 기반 문서 생성 (비동기)
            print(f"🚀 템플릿 기반 문서 생성 시작...")
            result = asyncio.run(generate_template_chunk_documents(source_dir, output_dir))
            
            generation_time = (datetime.now() - start_time).total_seconds()
            
            # 성공 응답
            return GenerationResponse(
                document_id=str(uuid.uuid4()),
                status="success",
                generated_content=f"템플릿 기반으로 {result.get('generated_files', 0)}개 문서 생성 완료",
                file_path=output_dir,
                generation_time=generation_time,
                metadata={
                    "total_files": result.get("generated_files", 0),
                    "total_chunks": result.get("total_chunks", 0),
                    "total_lines": analysis.total_lines,
                    "individual_files_generated": result.get("generated_files", 0),
                    "rag_upload_result": result.get("rag_upload_result", {}),
                    "template_based": True
                }
            )
            
        except Exception as e:
            print(f"❌ 템플릿 기반 생성 실패, 기존 방식으로 폴백: {e}")
            
            # 폴백: 기존 방식 유지
            return self._generate_individual_documents_legacy(analysis, request, start_time)
    
    def _generate_individual_documents_legacy(self, analysis: DocumentAnalysis, request: GenerationRequest, start_time: datetime) -> GenerationResponse:
        """기존 방식의 파일별 개별 문서 생성 (폴백용)"""
        # 프로젝트 루트 경로에서 상대 경로 추출
        project_root = Path(request.project_path).resolve()
        output_base_dir = Path("generated_docs") / project_root.name
        output_base_dir.mkdir(parents=True, exist_ok=True)
        
        generated_files = []
        total_content = ""
        
        # 각 파일별로 개별 문서 생성
        for structure in analysis.code_structures:
            # 파일별 개별 템플릿으로 내용 생성
            content = self._generate_individual_file_content(structure, request)
            
            # 소스 파일의 상대 경로 계산
            source_path = Path(structure.file_path).resolve()
            relative_path = source_path.relative_to(project_root)
            
            # 출력 경로 생성 (폴더 구조 유지)
            doc_filename = f"{relative_path.stem}.md"
            doc_path = output_base_dir / relative_path.parent / doc_filename
            
            # 디렉토리 생성
            doc_path.parent.mkdir(parents=True, exist_ok=True)
            
            # 개별 파일 저장
            with open(doc_path, 'w', encoding='utf-8') as f:
                f.write(content)
            
            generated_files.append(str(doc_path))
            total_content += f"\n\n---\n\n{content}"
        
        # 프로젝트 요약 문서 생성 (루트에)
        project_summary = self._generate_project_summary(analysis, request)
        summary_path = output_base_dir / "project_summary.md"
        with open(summary_path, 'w', encoding='utf-8') as f:
            f.write(project_summary)
        
        generated_files.append(str(summary_path))
        
        generation_time = (datetime.now() - start_time).total_seconds()
        
        return GenerationResponse(
            document_id="individual_files",
            status="success",
            generated_content=total_content,
            file_path=",".join(generated_files),
            generation_time=generation_time,
            metadata={
                "total_files": analysis.total_files,
                "total_lines": analysis.total_lines,
                "languages": analysis.languages,
                "issues_count": len(analysis.issues),
                "individual_files_generated": len(generated_files),
                "output_structure": str(output_base_dir)
            }
        )
    
    def _generate_individual_file_content(self, structure, request: GenerationRequest) -> str:
        """소스코드 분석을 위한 상세한 개별 파일 문서 생성"""
        file_path = Path(structure.file_path)
        
        template = """
# {{ file_path.name }}

**파일 경로**: {{ file_path }}
**언어**: {{ structure.language }}
**코드 라인**: {{ structure.lines_of_code }}줄
**복잡도 점수**: {{ structure.complexity_score }}

## 🎯 파일 개요

이 파일은 {{ file_path.stem }} 관련 기능을 담당하는 {{ structure.language }} 모듈입니다.

```mermaid
graph TD
    A[{{ file_path.name }}] --> B[주요 기능]
    A --> C[의존성]
    A --> D[사용처]
    
    B --> B1[{% if structure.functions %}{{ structure.functions[0].name }}{% else %}기본 기능{% endif %}]
    {% if structure.functions|length > 1 %}B --> B2[{{ structure.functions[1].name }}]{% endif %}
    {% if structure.functions|length > 2 %}B --> B3[{{ structure.functions[2].name }}]{% endif %}
    
    C --> C1[내부 의존성]
    C --> C2[외부 의존성]
    
    D --> D1[호출 모듈]
    D --> D2[사용 사례]
    
    style A fill:#f96,stroke:#333,stroke-width:2px
    {% if structure.complexity_score > 15 %}style B fill:#f66,stroke:#333,stroke-width:2px{% endif %}
```

{% if 'issue_detector' in file_path.stem %}
### 🔍 핵심 기능
이 파일은 **RaaS Rule Analyzer의 핵심 오류 검출 엔진**입니다:

#### 7가지 오류 유형 검출 시스템
1. **duplicate_condition** - 중복 조건 검출
   - 동일한 조건이 반복되는 문제 검출
   - 조건 시그니처 기반 중복 식별
2. **type_mismatch** - 타입 불일치 검출
   - 필드 타입과 값의 호환성 검사
   - 런타임 오류 방지
3. **invalid_operator** - 잘못된 연산자 검출
   - 필드 타입에 맞지 않는 연산자 사용 검출
4. **self_contradiction** - 자기모순 조건 검출
   - 논리적으로 모순되는 조건 조합 검출
5. **missing_condition** - 누락 조건 검출
   - 필수 조건이 누락된 경우 검출
6. **ambiguous_branch** - 분기 불명확성 검출
   - 조건 분기의 모호성 검출
7. **complexity_warning** - 복잡성 경고 검출
   - 과도한 복잡도로 인한 유지보수성 저하 경고

#### 주요 특징
- 각 검출 함수는 독립적으로 작동하며 순서가 중요
- 조건 분석기(ConditionAnalyzer)와 강하게 결합됨
- 비동기 처리로 성능 최적화

{% elif 'condition_analyzer' in file_path.stem %}
### 🔍 핵심 기능
이 파일은 **조건 분석 및 검증의 핵심 엔진**입니다:

#### 조건 분석 시스템
- **타입 검증**: 문자열, 숫자, 불린, 날짜 등 타입별 검증
- **연산자 유효성**: ==, !=, >, <, >=, <=, in, not in 등 검사
- **시그니처 생성**: 조건의 고유 식별자 생성
- **모순 분석**: 논리적 모순 조건 조합 검출

#### 주요 특징
- 타입 시스템이 매우 엄격함
- 연산자와 타입의 조합이 복잡함
- 캐싱 메커니즘으로 성능 최적화

{% elif 'validation' in file_path.stem %}
### 🔍 핵심 기능
이 파일은 **데이터 유효성 검증 시스템**입니다:

#### 검증 기능
- **이메일 검증**: RFC 표준 준수 이메일 형식 검증
- **URL 검증**: HTTP/HTTPS URL 형식 검증
- **전화번호 검증**: 국제 표준 전화번호 형식 검증
- **문자열 검증**: 길이, 패턴, 특수문자 검증
- **숫자 검증**: 범위, 정밀도, 형식 검증
- **JSON 스키마**: JSON 데이터 구조 검증

#### 주요 특징
- 정규식 기반 검증이 많음
- 국제화(i18n) 지원 고려 필요
- 성능을 위한 캐싱 메커니즘 활용

{% elif 'metrics' in file_path.stem %}
### 🔍 핵심 기능
이 파일은 **성능 및 품질 메트릭 생성 시스템**입니다:

#### 메트릭 시스템
- **성능 메트릭**: 실행 시간, 메모리 사용량, CPU 사용률
- **품질 메트릭**: 유지보수성, 가독성, 완성도, 일관성
- **복잡도 분석**: 순환 복잡도, 조건 트리 깊이, 결합도
- **최적화 제안**: 병목 지점 식별, 개선 방향 제시

#### 주요 특징
- 실시간 메트릭 수집으로 성능 영향 최소화
- 히스토리 기반 트렌드 분석
- 임계값 기반 알림 시스템

{% endif %}

## 🏗️ 상세 코드 구조

### 📋 함수별 상세 정보
{% for func in structure.functions %}
#### {{ func.name }}
- **라인**: {{ func.line_number }}
- **비동기**: {% if func.is_async %}예 (async/await 패턴 사용){% else %}아니오 (동기 처리){% endif %}
- **접근성**: {% if func.name.startswith('_') %}내부 함수 (private){% else %}공개 함수 (public){% endif %}
{% if func.docstring %}
- **기능 설명**: {{ func.docstring }}
{% endif %}
{% if func.args %}
- **매개변수**: 
{% for arg in func.args %}
  - `{{ arg }}`: {% if arg == 'self' %}클래스 인스턴스 참조{% elif arg == 'cls' %}클래스 참조{% else %}함수 입력 매개변수{% endif %}
{% endfor %}
{% endif %}

**개발 시 고려사항**:
{% if 'detect' in func.name %}
- 검출 로직의 순서가 중요함
- 예외 처리 패턴 일관성 유지 필요
- 로깅 및 디버깅 정보 추가 고려
{% elif 'analyze' in func.name %}
- 분석 결과의 정확성 검증 필요
- 대용량 데이터 처리 시 메모리 최적화
- 결과 캐싱으로 성능 향상 가능
{% elif 'validate' in func.name %}
- 입력 데이터 사전 검증 필수
- 검증 실패 시 명확한 오류 메시지 제공
- 국제화된 검증 규칙 적용 고려
{% endif %}

{% endfor %}

### 🏛️ 클래스별 상세 정보
{% for cls in structure.classes %}
#### {{ cls.name }}
- **라인**: {{ cls.line_number }}
- **클래스 유형**: {% if cls.name.endswith('Error') or cls.name.endswith('Exception') %}예외 클래스{% elif cls.name.endswith('Manager') %}관리자 클래스{% elif cls.name.endswith('Analyzer') %}분석기 클래스{% elif cls.name.endswith('Detector') %}검출기 클래스{% else %}일반 클래스{% endif %}
{% if cls.docstring %}
- **클래스 설명**: {{ cls.docstring }}
{% endif %}
{% if cls.bases %}
- **상속 구조**: {{ cls.bases | join(', ') }}
  - 상속받은 메서드와 속성 활용 가능
  - 다형성 구현 고려 필요
{% endif %}

**개발 시 고려사항**:
- 클래스 초기화 시 의존성 주입 패턴
- 메서드 체이닝 가능성 검토
- 상태 관리 및 동시성 처리 고려
- 메모리 누수 방지를 위한 적절한 리소스 해제

{% endfor %}

## 🔗 의존성 및 연관성

{% if structure.imports %}
### 📦 Import 의존성
{% for imp in structure.imports %}
- `{{ imp }}`: {% if 'typing' in imp %}타입 힌트 시스템{% elif 'asyncio' in imp %}비동기 처리{% elif 'json' in imp %}JSON 데이터 처리{% elif 'datetime' in imp %}날짜/시간 처리{% elif 're' in imp %}정규식 처리{% elif 'logging' in imp %}로깅 시스템{% elif 'pathlib' in imp %}경로 처리{% elif 'collections' in imp %}컬렉션 자료구조{% else %}외부 라이브러리{% endif %}
{% endfor %}
{% endif %}

{% if structure.dependencies %}
### 🔄 시스템 의존성
{% for dep in structure.dependencies %}
- `{{ dep }}`: {% if 'analyzer' in dep %}분석 모듈{% elif 'model' in dep %}데이터 모델{% elif 'exception' in dep %}예외 처리{% elif 'util' in dep %}유틸리티{% else %}기타 모듈{% endif %}
{% endfor %}
{% endif %}

## 🚀 개발 시나리오

### 시나리오 1: 새로운 기능 추가
1. 새로운 메서드 구현
2. 기존 메서드에서 호출 추가
3. 테스트 케이스 작성
4. 문서 업데이트

### 시나리오 2: 성능 최적화
1. 병목 지점 식별 (프로파일링)
2. 캐싱 메커니즘 도입
3. 비동기 처리 적용
4. 메모리 사용량 최적화

### 시나리오 3: 새로운 검증 규칙 추가
1. 검증 로직 구현
2. 테스트 케이스 작성
3. 에러 메시지 국제화
4. 성능 영향 평가

## 📊 코드 품질 메트릭

- **순환 복잡도**: {{ structure.complexity_score }} ({% if structure.complexity_score > 20 %}높음 - 리팩토링 권장{% elif structure.complexity_score > 10 %}보통 - 모니터링 필요{% else %}낮음 - 양호{% endif %})
- **함수 수**: {{ structure.functions | length }}개
- **클래스 수**: {{ structure.classes | length }}개
- **코드 라인**: {{ structure.lines_of_code }}줄

## 🔍 검색 키워드

{% if 'issue_detector' in file_path.stem %}
오류유형, 7가지, 검출, duplicate_condition, type_mismatch, invalid_operator, self_contradiction, missing_condition, ambiguous_branch, complexity_warning, 이슈검출, 룰검증, 디버깅, 성능최적화, 코드분석
{% elif 'validation' in file_path.stem %}
검증, 유효성, 타입체크, validation, 이메일, URL, 전화번호, 문자열검증, 숫자검증, JSON스키마, 정규식, 국제화, i18n
{% elif 'metrics' in file_path.stem %}
메트릭, 복잡도, 성능, complexity, 품질점수, 최적화, 병목조건, 유지보수성, 가독성, 프로파일링, 모니터링
{% elif 'condition_analyzer' in file_path.stem %}
조건분석, condition, analyzer, 타입검증, 연산자검사, 시그니처, 모순분석, 캐싱, 성능최적화
{% else %}
{{ file_path.stem }}, {{ structure.language }}, 코드분석, 문서화, 디버깅, 성능최적화
{% endif %}

---
*이 문서는 CodeMuse Document Generator에 의해 자동 생성되었습니다. 소스코드 분석을 위한 상세 정보를 포함합니다.*
"""
        
        from jinja2 import Template
        template_obj = Template(template)
        
        return template_obj.render(
            structure=structure,
            file_path=file_path,
            generated_at=datetime.now().isoformat()
        )
    
    def _generate_project_summary(self, analysis: DocumentAnalysis, request: GenerationRequest) -> str:
        """소스코드 분석을 위한 프로젝트 요약 문서 생성"""
        template = """
# {{ analysis.project_name }} 프로젝트 요약 (소스코드 분석)

**생성일시**: {{ generated_at }}
**분석 파일 수**: {{ analysis.total_files }}개
**총 코드 라인**: {{ analysis.total_lines }}줄

## 🎯 프로젝트 개요

이 문서는 {{ analysis.project_name }} 프로젝트의 **소스코드 분석 결과**입니다.

### 🏗️ 시스템 아키텍처

```mermaid
graph TD
    A[프로젝트 루트] --> B[핵심 모듈]
    A --> C[유틸리티]
    A --> D[테스트]
    
    B --> B1[분석기 모듈]
    B --> B2[검출기 모듈]
    B --> B3[관리자 모듈]
    
    B1 --> B1A[조건 분석기]
    B1 --> B1B[규칙 분석기]
    
    B2 --> B2A[이슈 검출기]
    B2 --> B2B[성능 검출기]
    
    C --> C1[유효성 검증]
    C --> C2[데이터 변환]
    C --> C3[로깅/모니터링]
    
    D --> D1[단위 테스트]
    D --> D2[통합 테스트]
    D --> D3[성능 테스트]
    
    style B1A fill:#f9f,stroke:#333,stroke-width:2px
    style B2A fill:#f9f,stroke:#333,stroke-width:2px
    style C1 fill:#bbf,stroke:#333,stroke-width:1px
```

#### 사용 언어 및 기술 스택
{% for language, count in analysis.languages.items() %}
- **{{ language }}**: {{ count }}개 파일
  {% if language == 'python' %}
  - 비동기 처리 (asyncio)
  - 타입 힌트 시스템
  - 모듈화된 구조
  {% endif %}
{% endfor %}

#### 핵심 모듈 구조
{% for structure in analysis.code_structures %}
{% if 'analyzer' in structure.file_path or 'detector' in structure.file_path or 'manager' in structure.file_path %}
### 🔍 {{ structure.file_path }}
- **역할**: {% if 'analyzer' in structure.file_path %}분석 엔진{% elif 'detector' in structure.file_path %}검출 시스템{% elif 'manager' in structure.file_path %}관리 시스템{% endif %}
- **언어**: {{ structure.language }}
- **라인 수**: {{ structure.lines_of_code }}
- **복잡도**: {{ structure.complexity_score }} ({% if structure.complexity_score > 20 %}높음 - 리팩토링 필요{% elif structure.complexity_score > 10 %}보통{% else %}낮음{% endif %})
- **함수**: {{ structure.functions | length }}개
- **클래스**: {{ structure.classes | length }}개
- **중요도**: {% if 'issue_detector' in structure.file_path %}⭐⭐⭐⭐⭐ (핵심){% elif 'condition_analyzer' in structure.file_path %}⭐⭐⭐⭐⭐ (핵심){% elif 'validation' in structure.file_path %}⭐⭐⭐⭐ (중요){% elif 'metrics' in structure.file_path %}⭐⭐⭐⭐ (중요){% else %}⭐⭐⭐ (보통){% endif %}

{% endif %}
{% endfor %}

## 🚀 개발 시나리오별 가이드

### 시나리오 1: 새로운 기능 추가

```mermaid
flowchart LR
    A[분석 단계] --> B[설계 단계]
    B --> C[구현 단계]
    C --> D[통합 단계]
    
    A --> A1[의존성 파악]
    A --> A2[요구사항 분석]
    
    B --> B1[인터페이스 설계]
    B --> B2[호환성 검토]
    
    C --> C1[테스트 작성]
    C --> C2[코드 구현]
    
    D --> D1[성능 평가]
    D --> D2[문서화]
    
    style A fill:#bbf,stroke:#333
    style B fill:#bbf,stroke:#333
    style C fill:#bbf,stroke:#333
    style D fill:#bbf,stroke:#333
```

1. **분석 단계**: 기존 모듈의 의존성 파악
2. **설계 단계**: 인터페이스 호환성 검토
3. **구현 단계**: 테스트 케이스 우선 작성
4. **통합 단계**: 성능 영향 평가

### 시나리오 2: 성능 최적화

```mermaid
flowchart TD
    A[성능 병목 식별] --> B[최적화 전략 수립]
    B --> C[구현 및 테스트]
    C --> D[성능 측정]
    D --> E{목표 달성?}
    E -->|Yes| F[완료]
    E -->|No| A
    
    style A fill:#f96,stroke:#333
    style E fill:#bbf,stroke:#333
    style F fill:#9f6,stroke:#333
```

1. **프로파일링**: 병목 지점 식별
2. **캐싱 전략**: 메모리 vs CPU 트레이드오프
3. **비동기 처리**: I/O 바운드 작업 최적화
4. **모니터링**: 실시간 성능 추적

### 시나리오 3: 버그 수정
1. **재현**: 최소 재현 케이스 작성
2. **분석**: 로그 및 스택 트레이스 분석
3. **수정**: 근본 원인 해결
4. **검증**: 회귀 테스트 수행

## 📊 시스템 메트릭

- **총 함수 수**: {{ analysis.metrics.total_functions }}개
- **총 클래스 수**: {{ analysis.metrics.total_classes }}개
- **평균 복잡도**: {{ "%.2f" | format(analysis.metrics.average_complexity) }}
- **이슈가 있는 파일**: {{ analysis.metrics.files_with_issues }}개

### 복잡도 분석

```mermaid
pie title 파일 복잡도 분포
    "낮음 (< 10)" : {{ analysis.code_structures|selectattr('complexity_score', 'lt', 10)|list|length }}
    "중간 (10-20)" : {{ analysis.code_structures|selectattr('complexity_score', 'ge', 10)|selectattr('complexity_score', 'lt', 20)|list|length }}
    "높음 (> 20)" : {{ analysis.code_structures|selectattr('complexity_score', 'ge', 20)|list|length }}
```

{% for structure in analysis.code_structures %}
{% if structure.complexity_score > 20 %}
- ⚠️ **{{ structure.file_path }}**: 복잡도 {{ structure.complexity_score }} (리팩토링 권장)
{% endif %}
{% endfor %}

## 🔗 모듈 간 의존성

```mermaid
graph LR
    A[IssueDetector] --> B[ConditionAnalyzer]
    B --> C[Validation]
    D[MetricsGenerator] --> B
    E[RuleAnalyzer] --> A
    E --> D
    
    style A fill:#f96,stroke:#333,stroke-width:2px
    style B fill:#f96,stroke:#333,stroke-width:2px
    style E fill:#bbf,stroke:#333,stroke-width:2px
```

### 핵심 의존성 체인
1. **IssueDetector** → **ConditionAnalyzer** → **Validation**
2. **MetricsGenerator** → **ConditionAnalyzer** → **Validation**
3. **RuleAnalyzer** → **IssueDetector** + **MetricsGenerator**

### 개발 시 주의사항
- **순환 의존성 방지**: 모듈 간 순환 참조 금지
- **인터페이스 일관성**: API 변경 시 하위 호환성 유지
- **성능 영향**: 핵심 경로의 성능 저하 최소화

## 🛠️ 개발 환경 및 도구

### 권장 개발 도구
- **IDE**: VS Code, PyCharm (타입 힌트 지원)
- **테스트**: pytest (비동기 테스트 지원)
- **프로파일링**: cProfile, line_profiler
- **문서화**: Sphinx, mkdocs

### 코드 품질 도구
- **린팅**: flake8, black, mypy
- **보안**: bandit, safety
- **의존성**: pip-audit

## 🔍 검색 키워드

시스템파악, 아키텍처, 의존성, 성능최적화, 디버깅, 테스트, 리팩토링, 코드품질, 모니터링, 프로파일링, 비동기처리, 캐싱, 메모리최적화, 병목분석, 오류검출, 조건분석, 메트릭생성, 유효성검증

---
*이 문서는 CodeMuse Document Generator에 의해 자동 생성되었습니다. 소스코드 분석을 위한 완전한 시스템 이해를 제공합니다.*
"""
        
        from jinja2 import Template
        template_obj = Template(template)
        
        return template_obj.render(
            analysis=analysis,
            generated_at=datetime.now().isoformat()
        )
