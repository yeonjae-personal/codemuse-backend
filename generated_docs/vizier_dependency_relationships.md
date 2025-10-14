# 📊 Vizier 프로젝트의 제품 관계 (Product Relationships)

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
