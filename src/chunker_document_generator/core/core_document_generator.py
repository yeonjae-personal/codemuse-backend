"""
핵심 문서 자동 생성 모듈
project_overview.md, project_summary.md 등 핵심 문서를 자동 생성합니다.
"""

import os
import logging
from pathlib import Path
from typing import Dict, Any, List

logger = logging.getLogger("chunker_document_generator")


class CoreDocumentGenerator:
    """핵심 문서 자동 생성기"""
    
    def __init__(self, source_dir: str, output_dir: str):
        self.source_dir = source_dir
        self.output_dir = output_dir
    
    def generate_core_documents(self, analysis_result: Dict[str, Any]) -> Dict[str, str]:
        """
        3개 핵심 문서 생성
        
        Args:
            analysis_result: 코드 분석 결과
            
        Returns:
            생성된 문서 경로 딕셔너리
        """
        logger.info("📚 핵심 문서 생성 시작...")
        
        generated_docs = {}
        
        try:
            # 1. project_overview.md 생성
            overview_path = self._generate_project_overview()
            generated_docs['project_overview'] = overview_path
            logger.info(f"   ✅ project_overview.md 생성: {overview_path}")
            
            # 2. project_summary.md 수정 (상단에 정보 추가)
            summary_path = self._update_project_summary(analysis_result)
            generated_docs['project_summary'] = summary_path
            logger.info(f"   ✅ project_summary.md 업데이트: {summary_path}")
            
            # 3. vizier_dependency_relationships.md 확인 및 수정
            dependency_path = self._update_dependency_relationships()
            generated_docs['vizier_dependency'] = dependency_path
            logger.info(f"   ✅ vizier_dependency_relationships.md 업데이트: {dependency_path}")
            
            logger.info("✅ 핵심 문서 생성 완료!")
            return generated_docs
            
        except Exception as e:
            logger.error(f"❌ 핵심 문서 생성 실패: {e}", exc_info=True)
            return generated_docs
    
    def _generate_project_overview(self) -> str:
        """project_overview.md 생성"""
        
        content = """# 📋 분석 대상 프로젝트 개요

> **작성일**: 2025-10-01
> **목적**: CodeMuse로 분석 중인 프로젝트 정보
> **분석 대상**: 2개 프로젝트

---

## 🎯 현재 분석 대상 프로젝트 (2개)

CodeMuse는 현재 **2개의 프로젝트**를 분석하고 있습니다:

### **1. Rule Analyzer (규칙 분석기)** 📋

#### **프로젝트 정보**
- **프로젝트명**: Rule Analyzer
- **언어**: Python
- **유형**: 코드 분석 및 규칙 검증 도구
- **경로**: `sample_code/rule_analyzer/`

#### **기술 스택**
- **언어**: Python 3.x
- **주요 라이브러리**: 
  - 코드 분석 및 파싱
  - 스트리밍 처리
  - 포매팅 및 템플릿

#### **주요 기능**
1. **이슈/오류 검출**
   - 타입 불일치 검출
   - 중복 조건 감지
   - 복잡도 분석

2. **규칙 분석**
   - 조건 파싱
   - 로직 흐름 분석
   - 메트릭 생성

3. **모듈 구조**
   - `analyzers/`: 분석기 모듈
   - `formatters/`: 포매터 모듈
   - `streaming/`: 스트리밍 모듈
   - `shared/`: 공통 유틸리티

---

### **2. Vizier (비지어)** 🎯

#### **프로젝트 정보**
- **정식 명칭**: Vizier
- **한글명**: 비지어
- **유형**: 제품/서비스 관리 플랫폼
- **목적**: 통신 서비스 제품의 구조, 관계, 종속성 관리
- **경로**: `sample_code/vizier(sample)/`

#### **기술 스택**
- **백엔드**: Java, Spring Boot
  - 경로: `vizier-be-prod-develop/`
  - REST API, JPA/Hibernate
- **프론트엔드**: Vue.js, TypeScript
  - 경로: `vizier-fe-all-develop/`
  - Vue 3, Composition API

#### **주요 기능**
1. **제품 관계 관리**
   - 부모-자식 관계 (계층 구조)
   - 형제 관계
   - 제품 그룹 관계

2. **종속관계 (Dependency) 관리**
   - Leader-Follower 관계
   - 영향 분석 (Impact Analysis)
   - 의존성 추적

3. **데이터베이스 테이블**
   - `tb_item_mpng_d`: 제품 매핑 테이블
   - `tb_offer_dpdc_rel_d`: 제품 종속관계 테이블
   - `tb_offer_strc_d`: 제품 구조 테이블
   - `tb_offer_group_rel_d`: 제품 그룹 관계 테이블

---

## 📁 폴더 구조

```
sample_code/                    ← 테스트용 폴더명 (프로젝트명 아님!)
├── rule_analyzer/              ← 프로젝트 1: Rule Analyzer (Python)
│   ├── analyzers/              ← 분석기 모듈
│   ├── formatters/             ← 포매터 모듈
│   ├── streaming/              ← 스트리밍 모듈
│   └── shared/                 ← 공통 유틸리티
└── vizier(sample)/             ← 프로젝트 2: Vizier (Java + Vue.js)
    ├── vizier-be-prod-develop/ ← 백엔드 (Java, Spring Boot)
    └── vizier-fe-all-develop/  ← 프론트엔드 (Vue.js, TypeScript)
```

**⚠️ 주의사항:**
- `sample_code`는 테스트를 위한 **폴더명**이며, **프로젝트명이 아닙니다**.
- 실제 분석 대상 프로젝트는 **2개**입니다:
  1. **Rule Analyzer** - Python 기반 규칙 분석 도구
  2. **Vizier** - Java/Vue.js 기반 제품 관리 플랫폼

---

## 🔑 핵심 용어

### Rule Analyzer
- **프로젝트명**: Rule Analyzer (규칙 분석기)
- **도메인**: 코드 분석 및 규칙 검증
- **주요 기능**: 이슈 검출, 조건 분석, 메트릭 생성

### Vizier
- **프로젝트명**: Vizier (비지어)
- **도메인**: 통신 서비스 제품 관리
- **종속관계**: Leader-Follower 관계만 해당
- **제품 관계**: 부모-자식, 형제, 그룹 등 포함

---

## 📝 CodeMuse 사용 컨텍스트

이 **2개 프로젝트**는 **CodeMuse**를 통해 분석되고 있으며, CI/CD 파이프라인에서 레거시 시스템 분석 목적으로 사용될 예정입니다.

현재는 테스트 단계로, 다음 프로젝트들을 `sample_code/` 폴더에 배치하여 CodeMuse의 분석 기능을 검증하고 있습니다:

1. **Rule Analyzer** - Python 기반 분석 도구 검증
2. **Vizier** - Java/Vue.js 풀스택 프로젝트 검증

## 📊 프로젝트 비교

| 항목 | Rule Analyzer | Vizier |
|-----|--------------|--------|
| **언어** | Python | Java + Vue.js |
| **유형** | 분석 도구 | 비즈니스 플랫폼 |
| **규모** | 중소형 | 대형 |
| **도메인** | 코드 분석 | 통신 서비스 관리 |
| **아키텍처** | 모듈형 라이브러리 | 풀스택 웹 애플리케이션 |
"""
        
        output_path = os.path.join(self.output_dir, "project_overview.md")
        with open(output_path, 'w', encoding='utf-8') as f:
            f.write(content)
        
        return output_path
    
    def _update_project_summary(self, analysis_result: Dict[str, Any]) -> str:
        """project_summary.md 상단 정보 업데이트"""
        
        summary_path = os.path.join(self.output_dir, "project_summary.md")
        
        # 기존 파일이 있으면 읽기
        if os.path.exists(summary_path):
            with open(summary_path, 'r', encoding='utf-8') as f:
                existing_content = f.read()
            
            # 상단 메타데이터가 이미 수정되어 있는지 확인
            if "⚠️ 주의" not in existing_content or "sample_code`는 폴더명" not in existing_content:
                # 상단에 정보 추가
                header = """# 📊 분석 대상 프로젝트 요약

> **생성일**: 2025-10-01  
> **⚠️ 주의**: `sample_code`는 폴더명이며 프로젝트명이 아닙니다!  
> **실제 프로젝트**: 2개 (Rule Analyzer + Vizier)  
> **총 파일 수**: {total_files}개  
> **총 Chunk 수**: {total_chunks}개

---

## 🎯 개요

이 문서는 CodeMuse로 분석 중인 **2개 프로젝트**의 전체 구조와 통계를 요약합니다.

## 📝 분석 대상 프로젝트

CodeMuse는 현재 다음 **2개 프로젝트**를 분석하고 있습니다:

### **1. Rule Analyzer (규칙 분석기)**
- **유형**: Python 기반 코드 분석 도구
- **경로**: `sample_code/rule_analyzer/`
- **주요 기능**: 이슈/오류 검출, 조건 파싱/분석, 메트릭 생성

### **2. Vizier (비지어)**
- **유형**: Java/Vue.js 제품 관리 플랫폼
- **경로**: `sample_code/vizier(sample)/`
- **주요 기능**: 제품 관계 관리, 종속관계(Leader-Follower) 관리, 영향 분석

**⚠️ 중요**: `sample_code`는 테스트용 폴더명이며, 실제 프로젝트는 위의 2개입니다.

"""
                # 기존 통계 섹션 찾기
                import re
                stats_match = re.search(r'## 📊 전체 통계', existing_content)
                if stats_match:
                    # 통계 섹션부터 나머지 내용 유지
                    remaining_content = existing_content[stats_match.start():]
                    
                    # 통계에서 파일 수와 청크 수 추출
                    total_files = analysis_result.get('total_files', 0)
                    total_chunks = analysis_result.get('total_chunks', 0)
                    
                    header = header.format(total_files=total_files, total_chunks=total_chunks)
                    full_content = header + remaining_content
                else:
                    full_content = header + existing_content
                
                with open(summary_path, 'w', encoding='utf-8') as f:
                    f.write(full_content)
        
        return summary_path
    
    def _update_dependency_relationships(self) -> str:
        """vizier_dependency_relationships.md 확인 및 업데이트"""
        
        dep_path = os.path.join(self.output_dir, "vizier_dependency_relationships.md")
        
        # 파일이 없거나 잘못된 내용이면 재생성
        if not os.path.exists(dep_path):
            content = self._generate_dependency_relationships_content()
            with open(dep_path, 'w', encoding='utf-8') as f:
                f.write(content)
        else:
            # 기존 파일 읽기
            with open(dep_path, 'r', encoding='utf-8') as f:
                existing_content = f.read()
            
            # "종속관계"는 Leader-Follower만" 내용이 없으면 추가
            if "종속관계\"는 이 Leader-Follower 관계만" not in existing_content:
                # 제목 수정
                existing_content = existing_content.replace(
                    "# 📊 Vizier 프로젝트의 종속관계 (Dependency Relationships)",
                    "# 📊 Vizier 프로젝트의 제품 관계 (Product Relationships)"
                )
                
                # 개요 섹션에 용어 정의 추가
                if "## 🎯 개요" in existing_content:
                    overview_addition = """

**⚠️ 중요한 용어 정의:**
- **"종속관계(Dependency Relationship)"**: Vizier에서는 **Leader-Follower 관계만**을 지칭합니다.
- **"제품 관계"**: 부모-자식, 형제, 제품그룹 등 다른 관계들을 포함합니다.

특히 **리더-팔로워 관계(Leader-Follower Relationship)**를 통해 제품 간의 의존성을 세밀하게 관리하며, 이것이 Vizier에서 말하는 **종속관계**입니다.
"""
                    existing_content = existing_content.replace(
                        "## 🎯 개요",
                        "## 🎯 개요" + overview_addition
                    )
                
                # Leader-Follower 섹션에 강조 추가
                existing_content = existing_content.replace(
                    "### **👑 3. 리더-팔로워 관계 (Leader-Follower)**",
                    "### **👑 3. 리더-팔로워 관계 (Leader-Follower)** - ⭐ **종속관계(Dependency Relationship)**"
                )
                
                with open(dep_path, 'w', encoding='utf-8') as f:
                    f.write(existing_content)
        
        return dep_path
    
    def _generate_dependency_relationships_content(self) -> str:
        """vizier_dependency_relationships.md 전체 내용 생성"""
        
        return """# 📊 Vizier 프로젝트의 제품 관계 (Product Relationships)

> **생성일**: 2025-09-26  
> **프로젝트**: Vizier (제품/서비스 관리 플랫폼)  
> **주제**: 제품 간 관계 및 종속관계(Leader-Follower)

---

## 🎯 개요

Vizier 프로젝트에서 제품 간의 다양한 관계를 관리합니다.

**⚠️ 중요한 용어 정의:**
- **"종속관계(Dependency Relationship)"**: Vizier에서는 **Leader-Follower 관계만**을 지칭합니다.
- **"제품 관계"**: 부모-자식, 형제, 제품그룹 등 다른 관계들을 포함합니다.

특히 **리더-팔로워 관계(Leader-Follower Relationship)**를 통해 제품 간의 의존성을 세밀하게 관리하며, 이것이 Vizier에서 말하는 **종속관계**입니다.

---

## 🏗️ 핵심 데이터 모델

### 📋 제품 관계 테이블 구조

#### **1. 제품 매핑 테이블**
```sql
tb_item_mpng_d:
- obj_uuid: 제품 고유 ID
- obj_code: 제품 코드  
- obj_name: 제품 명칭
- dplc_trgt_uuid: 배치 대상 UUID (부모 제품)
- valid_start_dtm: 유효 시작일
- valid_end_dtm: 유효 종료일
```

#### **2. 제품 종속관계 테이블**
```sql
tb_offer_dpdc_rel_d:
- base_uuid: 리더 제품 UUID
- trgt_uuid: 팔로워 제품 UUID  
- dpdc_rel_uuid: 관계 유형 UUID
- valid_start_dtm: 관계 시작일
- valid_end_dtm: 관계 종료일
```

#### **3. 제품 구조 테이블**
```sql
tb_offer_strc_d:
- base_uuid: 기준 제품 UUID
- trgt_uuid: 대상 제품 UUID
- valid_start_dtm: 유효 시작일
- valid_end_dtm: 유효 종료일
```

#### **4. 제품 그룹 관계 테이블**
```sql
tb_offer_group_rel_d:
- offer_group_uuid: 제품 그룹 UUID
- offer_uuid: 개별 제품 UUID
- valid_start_dtm: 관계 시작일
- valid_end_dtm: 관계 종료일
```

---

## 🎯 제품 관계의 4가지 유형

> **참고**: Vizier에서 "종속관계"는 아래의 **3번 Leader-Follower 관계만**을 의미합니다.

### **👨‍👩‍👧‍👦 1. 부모-자식 관계 (Parent-Child)** - 제품 관계
- **정의**: 계층적 제품 구조에서 상위-하위 관계
- **테이블**: `tb_item_mpng_d`
- **필드**: `dplc_trgt_uuid` (부모 제품 참조)
- **API**: `getParentProdM`, `getChildrenProdM`

### **👥 2. 형제 관계 (Sibling)** - 제품 관계
- **정의**: 같은 부모를 가진 제품들 간의 관계
- **테이블**: `tb_item_mpng_d`
- **특징**: 동일한 `dplc_trgt_uuid` 값
- **API**: `getSiblingsProdM`

### **👑 3. 리더-팔로워 관계 (Leader-Follower)** - ⭐ **종속관계(Dependency Relationship)**
- **정의**: 제품 간의 의존성 관계
- **⚠️ 중요**: Vizier에서 **"종속관계"는 이 Leader-Follower 관계만을 지칭**합니다
- **테이블**: `tb_offer_dpdc_rel_d`
- **필드**: 
  - `base_uuid`: 리더 제품 (다른 제품이 의존하는 제품)
  - `trgt_uuid`: 팔로워 제품 (리더에 의존하는 제품)
- **API**: `getTargetLeader`, `getTargetFollower`, `getLeaderView`, `getFollowerView`

#### 리더(Leader)
- 다른 제품들이 의존하는 기준 제품
- 변경 시 팔로워들에게 영향을 미침

#### 팔로워(Follower)
- 리더 제품에 의존하는 제품
- 리더 변경에 따라 영향받음

### **🔗 4. 제품 그룹 관계 (Product Group)** - 제품 관계
- **정의**: 관련 제품들을 그룹으로 묶는 관계
- **테이블**: `tb_offer_group_rel_d`
- **API**: `getProductGroups`, `getGroupProducts`

---

## 🔍 영향 분석 (Impact Analysis)

### 영향 분석 프로세스
1. 변경 영향도 계산
2. 의존성 체인 추적 (직접/간접 의존성)
3. 리스크 평가

### 관련 서비스
- `ProductRelationshipService`: 제품 관계 관리
- `DependencyAnalysisService`: 의존성 분석

---

*이 문서는 Vizier 프로젝트의 제품 관계 및 종속관계(Leader-Follower)에 대한 종합 설명을 제공합니다.*
"""

