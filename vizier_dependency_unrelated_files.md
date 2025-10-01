# Vizier 코드베이스 - 종속관계와 무관한 파일 목록

> **분석 대상**: sample_code/vizier(sample)/  
> **분석 기준**: 리더-팔로우 종속관계 기능과 **전혀 무관한** 파일들
> **생성일**: 2025-10-01

---

## 📊 분석 결과 요약

vizier 코드베이스는 **제품 관리 플랫폼**으로, 다양한 기능이 구현되어 있습니다.
종속관계(리더-팔로우) 기능은 전체 기능 중 **일부**이며, 대부분의 파일은 다른 기능을 담당합니다.

---

## ❌ 종속관계와 무관한 기능 영역

### 1. 📋 Benefit (혜택 관리)

**역할**: 할인, 혜택, 등급 할인 등의 혜택 정보 관리

**파일 목록**:
```
be/src/main/resources/sql/
├─ mariadb/sql-benefit.xml
└─ postgresql/sql-benefit.xml
```

**주요 기능**:
- Allowance (할인/혜택) 목록 조회 및 관리
- RatingDiscount (등급 할인) 조회 및 관리
- 아이템 코드: `AW` (Allowance), `RD` (RatingDiscount)

**종속관계와의 연관성**: **없음** ❌

---

### 2. 📂 Category (카테고리 관리)

**역할**: 제품 카테고리 트리 구조 관리

**파일 목록**:
```
be/src/main/resources/sql/
├─ mariadb/
│  ├─ sql-category.xml
│  └─ sql-ui-category.xml
└─ postgresql/
   ├─ sql-category.xml
   └─ sql-ui-category.xml
```

**주요 기능**:
- 카테고리 트리 구조 생성/조회/수정
- 카테고리별 제품 카운트
- 계층적 카테고리 관리 (WITH RECURSIVE)
- 카테고리 탭/노드 관리

**종속관계와의 연관성**: **없음** ❌

---

### 3. 🔧 Characteristic (특성 관리)

**역할**: 제품 특성(Sales, Billing, Discount) 관리

**파일 목록**:
```
be/src/main/resources/sql/
├─ mariadb/sql-characteristic.xml
└─ postgresql/sql-characteristic.xml
```

**주요 기능**:
- Sales Information (판매 정보) - 아이템 코드: `SI`
- Billing Information (과금 정보) - 아이템 코드: `BI`
- Discount Configuration (할인 설정) - 아이템 코드: `DI`

**종속관계와의 연관성**: **없음** ❌

---

### 4. 📊 Dashboard (대시보드)

**역할**: 통계, 모니터링, 최근 작업 내역 표시

**파일 목록**:
```
be/src/main/resources/sql/mariadb/
├─ sql-ui-dsbd-item-stat-m.xml          # 아이템 통계
├─ sql-ui-dsbd-monthly-offer-m.xml       # 월별 오퍼 통계
├─ sql-ui-dsbd-monthly-user-group-offer-m.xml  # 사용자 그룹별 오퍼 통계
├─ sql-ui-dsbd-offer-sub-cnt.xml         # 오퍼 구독자 카운트
├─ sql-ui-dsbd-recently-work-d.xml       # 최근 작업 내역
├─ sql-ui-dsbd-user-set-m.xml            # 사용자 설정
├─ sql-ui-dsbd-view-m.xml                # 대시보드 뷰
└─ sql-ui-dsbd-view-pst-d.xml            # 대시보드 뷰 포스트

be/src/main/resources/sql/postgresql/
└─ (동일한 파일들)
```

**주요 기능**:
- Top 10 구독자 오퍼 통계
- 최근 작업 내역 조회
- 월별/그룹별 통계
- 대시보드 커스터마이징

**종속관계와의 연관성**: **없음** ❌

---

### 5. ⚙️ Rule Engine (비즈니스 룰 엔진)

**역할**: 조건 기반 비즈니스 룰 설정 및 관리

**파일 목록**:
```
be/src/main/resources/sql/
├─ mariadb/
│  ├─ sql-rule-engine-category.xml       # 룰 카테고리
│  ├─ sql-rule-engine-condition.xml      # 룰 조건
│  ├─ sql-rule-engine-field.xml          # 룰 필드
│  └─ sql-rule-engine-rule.xml           # 룰 관리
└─ postgresql/
   └─ (동일한 파일들)
```

**주요 테이블**:
- `tb_rule_ctgr_m` - 룰 카테고리 마스터
- `tb_rule_m` - 룰 마스터
- `tb_cond_group_m` - 조건 그룹 마스터
- `tb_cond_m` - 조건 마스터
- `tb_field_m` - 필드 마스터

**주요 기능**:
- 룰 카테고리 트리 구조 관리
- 조건/필드 기반 비즈니스 룰 정의
- 룰 실행 및 검증
- AND/OR 논리 조합

**종속관계와의 연관성**: **없음** ❌

---

### 6. 📝 Custom Validation (커스텀 검증)

**역할**: 사용자 정의 데이터 검증 규칙 관리

**파일 목록**:
```
be/src/main/resources/sql/mariadb/
├─ sql-ui-cust-valid-attr-h.xml          # 검증 속성 헤더
├─ sql-ui-cust-valid-m.xml                # 검증 마스터
├─ sql-ui-cust-valid-multi-val-d.xml      # 멀티 값 검증
├─ sql-ui-cust-valid-val-d.xml            # 값 검증
└─ sql-ui-cust-valid-val-h.xml            # 값 헤더

be/src/main/resources/sql/postgresql/
└─ (동일한 파일들)
```

**종속관계와의 연관성**: **없음** ❌

---

### 7. 🔤 Multi-Language (다국어 지원)

**역할**: 레이블 및 메시지 다국어 관리

**파일 목록**:
```
be/src/main/resources/sql/mariadb/
└─ sql-ui-multi-lang-label-m.xml

be/src/main/resources/sql/postgresql/
└─ sql-ui-multi-lang-label-m.xml
```

**종속관계와의 연관성**: **없음** ❌

---

### 8. 📊 Matrix (매트릭스 관리)

**역할**: 데이터 매트릭스 뷰 관리

**파일 목록**:
```
be/src/main/resources/sql/mariadb/
└─ sql-ui-matrix.xml

be/src/main/resources/sql/postgresql/
└─ sql-ui-matrix.xml
```

**종속관계와의 연관성**: **없음** ❌

---

### 9. 🗂️ User Pocket (사용자 포켓)

**역할**: 사용자별 즐겨찾기/바로가기 관리

**파일 목록**:
```
be/src/main/resources/sql/mariadb/
└─ sql-ui-user-pocket-m.xml

be/src/main/resources/sql/postgresql/
└─ sql-ui-user-pocket-m.xml
```

**종속관계와의 연관성**: **없음** ❌

---

### 10. 📜 History (이력 관리)

**역할**: 데이터 변경 이력 추적

**파일 목록**:
```
be/src/main/resources/sql/mariadb/
└─ sql-ui-history.xml

be/src/main/resources/sql/postgresql/
└─ sql-ui-history.xml
```

**종속관계와의 연관성**: **없음** ❌

---

### 11. 🔗 Service & Resource (서비스 및 리소스 관리)

**역할**: 서비스 및 리소스 정보 관리

**파일 목록**:
```
be/src/main/resources/sql/
├─ mariadb/
│  ├─ sql-service.xml
│  ├─ sql-resource.xml
│  └─ sql-ui-resource.xml
└─ postgresql/
   └─ (동일한 파일들)
```

**종속관계와의 연관성**: **없음** ❌

---

### 12. 📦 Offer & Group (오퍼 및 그룹 관리 - 일부)

**역할**: 오퍼 자체 관리 (종속관계 제외)

**파일 목록**:
```
be/src/main/resources/sql/
├─ mariadb/
│  ├─ sql-offer.xml                # 오퍼 기본 관리
│  ├─ sql-ui-offer.xml             # 오퍼 UI
│  └─ sql-group.xml                # 그룹 기본 관리
└─ postgresql/
   └─ (동일한 파일들)
```

**주의**: 
- 그룹과 오퍼 자체는 종속관계 기능에서도 사용되지만,
- 이 파일들은 오퍼/그룹의 **기본 CRUD** 기능만 담당
- 종속관계 조회는 `sql-ui-extends.xml`에서 별도 관리

**종속관계와의 직접적 연관성**: **부분적** ⚠️

---

### 13. 📋 Component (컴포넌트 관리)

**역할**: UI 컴포넌트 메타데이터 관리

**파일 목록**:
```
be/src/main/resources/sql/mariadb/
└─ sql-ui-component.xml

be/src/main/resources/sql/postgresql/
└─ sql-ui-component.xml
```

**종속관계와의 연관성**: **없음** ❌

---

### 14. 📊 Table Management (테이블 관리)

**역할**: 동적 테이블 뷰 관리

**파일 목록**:
```
be/src/main/resources/sql/mariadb/
└─ sql-ui-table.xml

be/src/main/resources/sql/postgresql/
└─ sql-ui-table.xml
```

**종속관계와의 연관성**: **없음** ❌

---

### 15. 🔍 Multi-Entity (멀티 엔티티 관리)

**역할**: 복합 엔티티 관계 관리

**파일 목록**:
```
be/src/main/java/com/lgcns/svcp/prod/ui/prod/
├─ controller/UIMultiEntityController.java
├─ service/UIMultiEntityService.java
└─ dto/multiEntity/ (여러 DTO 파일)

be/src/main/resources/sql/mariadb/
└─ sql-ui-multiEntity.xml

be/src/main/resources/sql/postgresql/
└─ sql-ui-multiEntity.xml

fe/src/api/prod/extendsApi.ts (일부 함수만)
├─ getMultiEntitySearchInfo()
├─ getMultiEntitySearch()
├─ getMultiEntityDetail()
├─ postMultiEntityDetail()
├─ putMultiEntityDetail()
└─ getMultiEntityCreateInfo()
```

**주의**: `extendsApi.ts` 파일에 포함되어 있지만, 이 함수들은 종속관계와 무관

**종속관계와의 연관성**: **없음** ❌

---

### 16. 📑 Relation (일반 관계 관리)

**역할**: 종속관계가 아닌 일반적인 아이템 간 관계 관리

**파일 목록**:
```
be/src/main/java/com/lgcns/svcp/prod/ui/prod/
├─ controller/UIRelationController.java
├─ service/UIRelationService.java

be/src/main/resources/sql/mariadb/
└─ sql-ui-relation.xml

be/src/main/resources/sql/postgresql/
└─ sql-ui-relation.xml

fe/src/api/prod/extendsApi.ts (일부 함수만)
├─ initCreateInfoApi()
├─ getRelationSearchAdvanced()
├─ putRelation()
├─ postRelation()
├─ getRelationCreateInfo()
└─ getRelationListDataTable()
```

**주의**: 
- **UIRelationController**는 **종속관계(dependency)**가 아닌 **일반 관계(relation)** 관리
- 종속관계는 **UIExtendsController**에서 관리
- `extendsApi.ts` 파일에 포함되어 있지만, 이 함수들은 종속관계와 구분됨

**종속관계와의 연관성**: **다른 타입의 관계 관리** ⚠️

---

### 17. 🔗 Online Relation (온라인 관계 API)

**역할**: 온라인 API용 관계 조회

**파일 목록**:
```
be/src/main/java/com/lgcns/svcp/prod/online/prod/
├─ controller/RelationController.java
├─ service/RelationService.java
└─ dto/
   ├─ ProdCstcRelDDto.java
   └─ ProdDpndRelDDto.java
```

**주의**: 온라인 API용이며, UI 종속관계 관리와는 별도

**종속관계와의 연관성**: **별도 API** ⚠️

---

### 18. 🗃️ Common & Item (공통 및 아이템 관리)

**파일 목록**:
```
be/src/main/resources/sql/mariadb/
├─ sql-common.xml              # 공통 쿼리
├─ sql-ui-common.xml           # UI 공통
├─ sql-ui-item.xml             # 아이템 관리
├─ sql-ui-item-strc-d.xml      # 아이템 구조
├─ sql-ui-add-attr-header-m.xml # 추가 속성 헤더
└─ sql-ui-factor.xml           # 요인 관리

be/src/main/resources/sql/postgresql/
└─ (동일한 파일들)
```

**종속관계와의 연관성**: **기본 인프라** (간접 사용) ⚠️

---

## ✅ 종속관계 관련 파일 (참고용)

### 핵심 종속관계 파일만 정리

**백엔드 (Java)**:
```
be/src/main/java/com/lgcns/svcp/prod/ui/prod/
├─ controller/UIExtendsController.java    ✅
├─ service/UIExtendsService.java          ✅
└─ dto/extend/                            ✅
   ├─ TargetResDto.java
   ├─ RelationViewResDto.java
   ├─ OfferDpdcRelDto.java
   ├─ OffrGrpResDto.java
   ├─ RelationViewReqDto.java
   ├─ SaveTargetReqDto.java
   ├─ TargetReqDto.java
   ├─ CountTargetResDto.java
   ├─ ItemOffrResDto.java
   └─ ... (종속관계 전용 DTO)

be/src/main/resources/sql/
├─ mariadb/sql-ui-extends.xml             ✅
└─ postgresql/sql-ui-extends.xml          ✅
```

**프론트엔드 (Vue/TypeScript)**:
```
fe/src/
├─ api/prod/extendsApi.ts                 ✅ (일부 함수만)
│  ├─ getExtendsDependencyTarget()
│  ├─ getExtendsDependencyLeader()
│  ├─ getExtendsDependencyFollower()
│  ├─ postExtendsDependencyTarget()
│  └─ ... (종속관계 전용 함수)
│
├─ store/
│  ├─ extendManager.store.ts              ✅
│  ├─ extendSearch.store.ts               ✅
│  └─ extendCreate.store.ts               ✅
│
├─ components/prod/extends/               ✅
│  └─ relation/manager/
│     └─ relation-viewer/
│        ├─ content/
│        │  ├─ ExtendsFocusColumn.vue
│        │  ├─ ExtendsExpandColumn.vue
│        │  └─ ExtendsDetail.vue
│        └─ common/
│           ├─ ExtendAccordionGroupRow.vue
│           ├─ ExtendCanvas.vue
│           └─ ExtendsAccordion.vue
│
├─ interfaces/prod/extends.ts             ✅
├─ constants/extendsManager.ts            ✅
├─ utils/extend-utils.ts                  ✅
└─ mocks/prod/extends/extends.ts          ✅
```

---

## 📊 통계 요약

### 백엔드 SQL 파일 (MariaDB 기준)

| 분류 | 파일 수 | 종속관계 여부 |
|------|--------|--------------|
| **종속관계 관련** | 1개 | ✅ sql-ui-extends.xml |
| **무관한 파일** | 40개+ | ❌ 나머지 전부 |

### Java 파일

| 분류 | 파일 수 | 종속관계 여부 |
|------|--------|--------------|
| **종속관계 Controller** | 1개 | ✅ UIExtendsController |
| **종속관계 Service** | 1개 | ✅ UIExtendsService |
| **종속관계 DTO** | 17개 | ✅ extend 패키지 |
| **무관한 파일** | 70개+ | ❌ 기타 모든 파일 |

### 프론트엔드 파일

| 분류 | 파일 수 | 종속관계 여부 |
|------|--------|--------------|
| **종속관계 컴포넌트** | 7개 | ✅ extends 디렉토리 |
| **종속관계 Store** | 3개 | ✅ extend*.store.ts |
| **종속관계 API 함수** | 약 10개 | ✅ extendsApi.ts 일부 |
| **무관한 컴포넌트** | 350개+ | ❌ 나머지 전부 |

---

## 🎯 결론

### 핵심 발견 사항:

1. **vizier 프로젝트는 종합 제품 관리 플랫폼**
   - 종속관계는 전체 기능 중 **일부 모듈**에 불과함

2. **무관한 기능 영역 (16개 이상)**:
   - Benefit (혜택 관리)
   - Category (카테고리 관리)
   - Characteristic (특성 관리)
   - Dashboard (대시보드)
   - Rule Engine (비즈니스 룰)
   - Custom Validation (커스텀 검증)
   - Multi-Language (다국어)
   - Matrix (매트릭스)
   - User Pocket (사용자 포켓)
   - History (이력 관리)
   - Service & Resource
   - Component Management
   - Table Management
   - Multi-Entity
   - General Relation (일반 관계)
   - Online API

3. **파일 비율**:
   - **종속관계 관련**: 약 5-10%
   - **무관한 파일**: 약 90-95%

4. **주의 사항**:
   - `extendsApi.ts` 파일은 종속관계 + 기타 기능이 **혼재**
   - Group/Offer 기본 관리는 종속관계에서도 사용하지만 별도 기능
   - `UIRelationController`는 종속관계가 아닌 일반 관계 관리

---

*본 문서는 vizier 코드베이스에서 종속관계(리더-팔로우)와 전혀 무관한 파일들을 체계적으로 정리한 것입니다.*


