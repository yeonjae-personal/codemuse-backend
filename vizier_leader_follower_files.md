# Vizier 리더-팔로우 종속관계 기능 구현 파일 목록

> **분석 대상**: sample_code/vizier(sample)/  
> **분석 주제**: 제품 간 종속관계(리더-팔로우 관계) 관련 기능  
> **생성일**: 2025-10-01

---

## 📋 목차

1. [백엔드 파일](#1-백엔드-파일)
2. [프론트엔드 파일](#2-프론트엔드-파일)
3. [데이터베이스 파일](#3-데이터베이스-파일)
4. [핵심 기능 흐름](#4-핵심-기능-흐름)
5. [데이터 모델](#5-데이터-모델)

---

## 1. 백엔드 파일

### 📂 Controller Layer

#### `UIExtendsController.java`
**경로**: `be/src/main/java/com/lgcns/svcp/prod/ui/prod/controller/UIExtendsController.java`

**역할**: 리더-팔로우 종속관계 관련 REST API 엔드포인트 제공

**핵심 API**:
```java
// 리더/팔로워 그룹 조회
GET /ui/extends/dependency/target
- 파라미터: offerUuid, onlyValidDtm, includeGroup
- 반환: TargetResDto (리더 그룹 + 팔로워 그룹)

// 리더 조회
GET /ui/extends/dependency/leader
- 파라미터: targetUuid, onlyValidDtm, includeGroup
- 반환: List<RelationViewResDto>

// 팔로워 조회
GET /ui/extends/dependency/follower
- 파라미터: targetUuid, onlyValidDtm, includeGroup
- 반환: List<RelationViewResDto>

// 관계 데이터 내보내기
GET /ui/extends/dependency/relation/export
```

---

### 📂 Service Layer

#### `UIExtendsService.java`
**경로**: `be/src/main/java/com/lgcns/svcp/prod/ui/prod/service/UIExtendsService.java`

**역할**: 리더-팔로우 관계 비즈니스 로직 구현

**핵심 메서드**:
```java
// 타겟 조회 (리더 + 팔로워 그룹)
public TargetResDto getTarget(TargetReqDto req)

// 리더 조회
public List<RelationViewResDto> getLeader(RelationViewReqDto req)

// 팔로워 조회
public List<RelationViewResDto> getFollower(RelationViewReqDto req)

// 타겟 저장 (트랜잭션)
@Transactional
public void saveTarget(SaveTargetReqDto req)

// 카운트 조회
public CountTargetResDto getCountTarget(TargetReqDto req)
```

**특징**:
- Stream API를 활용한 데이터 그룹핑 및 변환
- 중첩 관계 데이터를 효율적으로 조회 및 매핑
- 그룹 단위 관계 데이터 조회 시 childOffr 및 referenceUuids 자동 설정

---

### 📂 DTO Layer

#### 핵심 DTO 파일들
**경로**: `be/src/main/java/com/lgcns/svcp/prod/ui/prod/dto/extend/`

**1. `TargetResDto.java`**
- 리더 그룹과 팔로워 그룹을 담는 응답 DTO
```java
public class TargetResDto {
    private List<OffrGrpResDto> leaderGrp;    // 리더 그룹 목록
    private List<OffrGrpResDto> followerGrp;  // 팔로워 그룹 목록
}
```

**2. `RelationViewResDto.java`**
- 관계 뷰 응답 DTO (리더/팔로워 상세 정보)
```java
public class RelationViewResDto {
    private String parentUuid;           // 부모 UUID
    private String dpdcRelUuid;          // 종속관계 UUID
    private String dpdcRelCode;          // 종속관계 코드
    private String dpdcRelName;          // 종속관계 명
    private String targetUuid;           // 대상 UUID
    private String targetCode;           // 대상 코드
    private String targetName;           // 대상 명
    private String offerGroupUuid;       // 오퍼 그룹 UUID
    private String referenceUuid;        // 참조 UUID
    private List<String> referenceUuids; // 참조 UUID 목록
    private List<ItemOffrResDto> childOffr; // 자식 오퍼 목록
    // ... 기타 필드
}
```

**3. `OfferDpdcRelDto.java`**
- 오퍼 종속관계 DTO
```java
public class OfferDpdcRelDto extends BaseDto {
    private String baseUuid;    // 리더 UUID (base)
    private String trgtUuid;    // 팔로워 UUID (target)
    private String dpdcRelUuid; // 종속관계 UUID
    private String validStartDtm;
    private String validEndDtm;
}
```

**4. `OffrGrpResDto.java`**
- 오퍼 그룹 응답 DTO

**5. `RelationViewReqDto.java`**
- 관계 뷰 요청 DTO

**6. `TargetReqDto.java`**
- 타겟 요청 DTO

**7. `SaveTargetReqDto.java`**
- 타겟 저장 요청 DTO

**8. `CountTargetResDto.java`**
- 타겟 카운트 응답 DTO

**9. `ItemOffrResDto.java`**
- 아이템 오퍼 응답 DTO

**10. 기타 관련 DTO**:
- `RelationGridViewDto.java`
- `RelationGridViewEntity.java`
- `RelationGridViewExportDto.java`
- `RelationGridViewSearchDto.java`
- `SearchRelationReqDto.java`

---

## 2. 프론트엔드 파일

### 📂 API Layer

#### `extendsApi.ts`
**경로**: `fe/src/api/prod/extendsApi.ts`

**역할**: 백엔드 API 호출 함수 정의

**핵심 API 함수**:
```typescript
// 타겟 조회 (리더 + 팔로워)
getExtendsDependencyTarget(params: any)

// 타겟 카운트 조회
getExtendsDependencyCountTarget(params: ParamsUIExtendsDependencyTarget)

// 타겟 저장
postExtendsDependencyTarget(params: ParamsUIExtendsDependencyTargetPost)

// 리더 조회
getExtendsDependencyLeader(params: ParamsUIExtendsDependencyTarget)

// 팔로워 조회
getExtendsDependencyFollower(params: ParamsUIExtendsDependencyTarget)

// 관계 조회
getExtendsDependencyRelation(params: ParamsUIExtendsDependencyRelation)

// 유효성 업데이트
putExtendsDependencyValidity(params: ParamsUIExtendsDependencyAddOffer)

// 관계 정의 조회
getExtendsDependencyRelationDefinition(dpdcRelUuid: string)

// 그룹 타겟 조회
getExtendsDependencyGroupTarget(objUuid: string)
```

---

### 📂 Store Layer (상태 관리)

#### `extendManager.store.ts`
**경로**: `fe/src/store/extendManager.store.ts`

**역할**: 리더-팔로워 관계 상태 관리 및 비즈니스 로직

**핵심 기능**:
```typescript
// 리더 목록 조회
async getLeaderList(
    groupUuid: String | null,
    offerDuplicateMode: boolean,
    indexItem: number,
    includeGroup: boolean
)

// 팔로워 목록 조회
async getFollowerList(
    groupUuid: String | null,
    offerDuplicateMode: boolean,
    indexItem: number,
    includeGroup: boolean
)

// 관계 데이터 변환
convertRelationData(data, isDuplicate)

// 세부 뷰 데이터 관리
detailViewData: {
    focusColumnLeaderList: [],
    focusColumnFollowerList: [],
    // ...
}
```

**특징**:
- Pinia를 사용한 상태 관리
- 중복 제거 로직 포함 (uniqueRelArray)
- 그룹 단위 관계 관리
- 복제 모드 지원

#### `extendSearch.store.ts`
**경로**: `fe/src/store/extendSearch.store.ts`

**역할**: 확장 검색 관련 상태 관리

#### `extendCreate.store.ts`
**경로**: `fe/src/store/extendCreate.store.ts`

**역할**: 확장 생성 관련 상태 관리

---

### 📂 Component Layer

#### 1. **뷰어 컴포넌트**

**`ExtendsFocusColumn.vue`**
**경로**: `fe/src/components/prod/extends/relation/manager/relation-viewer/content/ExtendsFocusColumn.vue`

**역할**: 포커스 컬럼 표시 (선택된 항목의 리더/팔로워 표시)

**핵심 기능**:
- 리더/팔로워 목록 표시
- 아이템 추가/삭제
- 드래그 앤 드롭 처리
- 관계 라인 그리기

**`ExtendsExpandColumn.vue`**
**경로**: `fe/src/components/prod/extends/relation/manager/relation-viewer/content/ExtendsExpandColumn.vue`

**역할**: 확장 컬럼 표시 (확장된 관계 정보)

**`ExtendsDetail.vue`**
**경로**: `fe/src/components/prod/extends/relation/manager/relation-viewer/content/ExtendsDetail.vue`

**역할**: 상세 정보 표시

#### 2. **공통 컴포넌트**

**`ExtendAccordionGroupRow.vue`**
**경로**: `fe/src/components/prod/extends/relation/manager/relation-viewer/common/ExtendAccordionGroupRow.vue`

**역할**: 아코디언 그룹 행 컴포넌트

**`ExtendCanvas.vue`**
**경로**: `fe/src/components/prod/extends/relation/manager/relation-viewer/common/ExtendCanvas.vue`

**역할**: 관계 시각화 캔버스 (라인 그리기)

**`ExtendsAccordion.vue`**
**경로**: `fe/src/components/prod/extends/relation/manager/relation-viewer/common/ExtendsAccordion.vue`

**역할**: 아코디언 컴포넌트 (관계 목록 접기/펼치기)

---

### 📂 Interface/Type 정의

#### `extends.ts`
**경로**: `fe/src/interfaces/prod/extends.ts`

**역할**: TypeScript 인터페이스 정의

**핵심 인터페이스**:
```typescript
// 타겟 파라미터
interface ParamsUIExtendsDependencyTarget {
  targetUuid: String;
  onlyValidDtm?: Boolean;
  includeGroup?: Boolean;
}

// 오퍼 추가 파라미터
interface ParamsUIExtendsDependencyAddOffer {
  baseUuid: String;        // 리더 UUID
  trgtUuid: String;        // 팔로워 UUID
  dpdcRelUuid: String;     // 종속관계 UUID
  parentUuid?: String;
  validStartDtm: String;
  validEndDtm?: String | null;
}

// 타겟 저장 파라미터
interface ParamsUIExtendsDependencyTargetPost {
  addOffrDpdcLst: ParamsUIExtendsDependencyAddOffer[];
  updateOffrDpdcLst: ParamsUIExtendsDependencyAddOffer[];
}
```

---

### 📂 Utilities & Constants

#### `extend-utils.ts`
**경로**: `fe/src/utils/extend-utils.ts`

**역할**: 확장 관련 유틸리티 함수

#### `extendsManager.ts`
**경로**: `fe/src/constants/extendsManager.ts`

**역할**: 확장 관련 상수 정의

---

### 📂 Icons

#### `ExtendsIcon.vue`
**경로**: `fe/src/components/prod/icons/ExtendsIcon.vue`

**역할**: 확장 아이콘 컴포넌트

---

### 📂 Mocks

#### `extends.ts`
**경로**: `fe/src/mocks/prod/extends/extends.ts`

**역할**: 테스트용 목 데이터

---

## 3. 데이터베이스 파일

### 📂 SQL Mapper 파일

#### MariaDB용 SQL

**`sql-ui-extends.xml`**
**경로**: `be/src/main/resources/sql/mariadb/sql-ui-extends.xml`

**핵심 쿼리**:

**1. 리더 조회 쿼리 (`getLeaderView`)**
```sql
SELECT
    d.obj_uuid AS dpdc_rel_uuid,
    d.obj_code AS dpdc_rel_code,
    d.obj_name AS dpdc_rel_name,
    b.obj_uuid AS target_uuid,
    b.obj_code AS target_code,
    b.obj_name AS target_name,
    CASE
        WHEN e.lctgr_item_code = 'G' THEN b.obj_uuid
        ELSE NULL
    END AS offer_group_uuid,
    a.base_uuid AS reference_uuid,
    a.trgt_uuid AS parent_uuid
FROM tb_offer_dpdc_rel_d a
JOIN tb_item_mpng_d b ON a.base_uuid = b.obj_uuid
JOIN tb_item_mpng_d d ON a.dpdc_rel_uuid = d.obj_uuid 
WHERE a.trgt_uuid = #{targetUuid}
```

**2. 팔로워 조회 쿼리 (`getFollowerView`)**
```sql
SELECT
    d.obj_uuid AS dpdc_rel_uuid,
    d.obj_code AS dpdc_rel_code,
    d.obj_name AS dpdc_rel_name,
    b.obj_uuid AS target_uuid,
    b.obj_code AS target_code,
    b.obj_name AS target_name,
    CASE
        WHEN e.lctgr_item_code = 'G' THEN b.obj_uuid
        ELSE NULL
    END AS offer_group_uuid,
    a.trgt_uuid AS reference_uuid,
    a.base_uuid AS parent_uuid
FROM tb_offer_dpdc_rel_d a
JOIN tb_item_mpng_d b ON a.trgt_uuid = b.obj_uuid
JOIN tb_item_mpng_d d ON a.dpdc_rel_uuid = d.obj_uuid 
WHERE a.base_uuid = #{targetUuid}
```

**3. 타겟 리더 조회 (`getTargetLeader`)**
```sql
-- 리더 그룹 조회
SELECT DISTINCT
    a.offer_group_uuid,
    -- ...
FROM tb_offer_group_rel_d a
JOIN tb_offer_dpdc_rel_d b ON a.offer_group_uuid = b.trgt_uuid
WHERE a.offer_uuid = #{offerUuid}
```

**4. 타겟 팔로워 조회 (`getTargetFollower`)**
```sql
-- 팔로워 그룹 조회
SELECT DISTINCT
    a.offer_group_uuid,
    -- ...
FROM tb_offer_group_rel_d a
JOIN tb_offer_dpdc_rel_d b ON a.offer_group_uuid = b.base_uuid
WHERE a.offer_uuid = #{offerUuid}
```

**5. Base UUID 조회 (`getBaseUuids`)**
```sql
SELECT
    base_uuid,
    trgt_uuid
FROM tb_offer_dpdc_rel_d
WHERE trgt_uuid IN 
    <foreach collection="referenceUuids" item="uuid" open="(" close=")" separator=",">
        #{uuid}
    </foreach>
```

**6. Target UUID 조회 (`getTargetUuids`)**
```sql
SELECT
    base_uuid,
    trgt_uuid
FROM tb_offer_dpdc_rel_d
WHERE base_uuid IN 
    <foreach collection="referenceUuids" item="uuid" open="(" close=")" separator=",">
        #{uuid}
    </foreach>
```

#### PostgreSQL용 SQL

**`sql-ui-extends.xml`**
**경로**: `be/src/main/resources/sql/postgresql/sql-ui-extends.xml`

**역할**: PostgreSQL 데이터베이스용 동일한 쿼리 구현

---

## 4. 핵심 기능 흐름

### 📊 리더-팔로워 관계 조회 흐름

```
[Frontend]
1. ExtendsFocusColumn.vue
   └─> getLeaderList() 또는 getFollowerList() 호출
       └─> extendManager.store.ts
           └─> getExtendsDependencyLeader() 또는 getExtendsDependencyFollower() API 호출

[API]
2. extendsApi.ts
   └─> httpClient.get('/ui/extends/dependency/leader' 또는 '/follower')

[Backend Controller]
3. UIExtendsController.java
   └─> getLeader() 또는 getFollower()
       └─> UIExtendsService 호출

[Backend Service]
4. UIExtendsService.java
   └─> getLeader(req) 또는 getFollower(req)
       ├─> commonDao.selectList("Ui-extends.getLeaderView") 
       │   또는 commonDao.selectList("Ui-extends.getFollowerView")
       ├─> 그룹 UUID 추출
       ├─> 참조 UUID 추출
       ├─> 아이템 오퍼 조회 (getItemOffrs)
       ├─> 종속관계 조회 (getBaseUuids 또는 getTargetUuids)
       └─> 결과 매핑 및 반환

[Database]
5. sql-ui-extends.xml
   └─> getLeaderView 또는 getFollowerView 쿼리 실행
       ├─> tb_offer_dpdc_rel_d (종속관계 테이블)
       ├─> tb_item_mpng_d (제품 매핑 테이블)
       └─> JOIN 연산 수행

[Frontend Store]
6. extendManager.store.ts
   └─> 응답 데이터 처리
       ├─> 중복 제거 (uniqueRelArray)
       ├─> 관계 목록 생성 (relList)
       └─> detailViewData 업데이트
           ├─> focusColumnLeaderList 또는 focusColumnFollowerList
           └─> 컴포넌트 렌더링 트리거

[Frontend Component]
7. ExtendsFocusColumn.vue
   └─> 화면에 리더/팔로워 목록 렌더링
       ├─> 아코디언 형태로 표시
       ├─> 관계 라인 그리기 (ExtendCanvas)
       └─> 사용자 인터랙션 처리
```

### 🔄 데이터 저장 흐름

```
[Frontend]
1. 사용자가 관계 추가/수정
   └─> ExtendsFocusColumn.vue
       └─> addGroupRelationOffer() 호출
           └─> postExtendsDependencyTarget() API 호출

[Backend]
2. UIExtendsController.java
   └─> POST /ui/extends/dependency/target
       └─> UIExtendsService.saveTarget() 호출

[Backend Service]
3. UIExtendsService.java
   └─> @Transactional saveTarget(SaveTargetReqDto req)
       ├─> addOffrDpdcLst 처리 (신규 관계 추가)
       │   └─> commonDao.insert("Ui-extends.insertOfferDpdc")
       ├─> updateOffrDpdcLst 처리 (기존 관계 업데이트)
       │   └─> commonDao.update("Ui-extends.updateOfferDpdc")
       └─> 트랜잭션 커밋

[Database]
4. tb_offer_dpdc_rel_d 테이블 업데이트
   ├─> base_uuid: 리더 제품 UUID
   ├─> trgt_uuid: 팔로워 제품 UUID
   ├─> dpdc_rel_uuid: 종속관계 유형 UUID
   └─> valid_start_dtm, valid_end_dtm: 유효 기간
```

---

## 5. 데이터 모델

### 📊 핵심 테이블 구조

#### 1. **tb_offer_dpdc_rel_d** (제품 종속관계 테이블)
```
┌─────────────────┬──────────────────────────────────┐
│ 컬럼명           │ 설명                              │
├─────────────────┼──────────────────────────────────┤
│ base_uuid       │ 리더 제품 UUID (의존하는 제품)    │
│ trgt_uuid       │ 팔로워 제품 UUID (의존받는 제품)  │
│ dpdc_rel_uuid   │ 종속관계 유형 UUID                │
│ valid_start_dtm │ 관계 시작일                       │
│ valid_end_dtm   │ 관계 종료일                       │
└─────────────────┴──────────────────────────────────┘
```

#### 2. **tb_item_mpng_d** (제품 매핑 테이블)
```
┌─────────────────┬──────────────────────────────────┐
│ 컬럼명           │ 설명                              │
├─────────────────┼──────────────────────────────────┤
│ obj_uuid        │ 제품 고유 ID                      │
│ obj_code        │ 제품 코드                         │
│ obj_name        │ 제품 명칭                         │
│ dplc_trgt_uuid  │ 배치 대상 UUID (부모 제품)        │
│ item_code       │ 아이템 코드                       │
│ valid_start_dtm │ 유효 시작일                       │
│ valid_end_dtm   │ 유효 종료일                       │
└─────────────────┴──────────────────────────────────┘
```

#### 3. **tb_offer_group_rel_d** (제품 그룹 관계 테이블)
```
┌──────────────────┬──────────────────────────────────┐
│ 컬럼명            │ 설명                              │
├──────────────────┼──────────────────────────────────┤
│ offer_group_uuid │ 제품 그룹 UUID                    │
│ offer_uuid       │ 개별 제품 UUID                    │
│ valid_start_dtm  │ 관계 시작일                       │
│ valid_end_dtm    │ 관계 종료일                       │
└──────────────────┴──────────────────────────────────┘
```

### 🔗 관계 다이어그램

```
                    ┌──────────────────────┐
                    │  tb_item_mpng_d      │
                    │  (제품 매핑)          │
                    │                      │
                    │  obj_uuid (PK)       │
                    │  obj_code            │
                    │  obj_name            │
                    │  dplc_trgt_uuid      │
                    └──────────────────────┘
                             ▲
                             │
                    ┌────────┴────────┐
                    │                 │
    ┌───────────────▼───────┐  ┌──────▼─────────────────┐
    │ tb_offer_dpdc_rel_d   │  │ tb_offer_group_rel_d   │
    │ (종속관계)             │  │ (그룹 관계)             │
    │                       │  │                        │
    │ base_uuid (FK)        │  │ offer_group_uuid       │
    │ trgt_uuid (FK)        │  │ offer_uuid             │
    │ dpdc_rel_uuid         │  │ valid_start_dtm        │
    │ valid_start_dtm       │  │ valid_end_dtm          │
    │ valid_end_dtm         │  │                        │
    └───────────────────────┘  └────────────────────────┘
```

---

## 📌 요약

### 백엔드 핵심 파일 (8개)
1. **Controller**: `UIExtendsController.java`
2. **Service**: `UIExtendsService.java`, `UIRelationService.java`
3. **DTO** (5개):
   - `TargetResDto.java`
   - `RelationViewResDto.java`
   - `OfferDpdcRelDto.java`
   - `RelationViewReqDto.java`
   - `SaveTargetReqDto.java`

### 프론트엔드 핵심 파일 (10개)
1. **API**: `extendsApi.ts`
2. **Store**: `extendManager.store.ts`
3. **Interface**: `extends.ts`
4. **Component** (7개):
   - `ExtendsFocusColumn.vue`
   - `ExtendsExpandColumn.vue`
   - `ExtendsDetail.vue`
   - `ExtendAccordionGroupRow.vue`
   - `ExtendCanvas.vue`
   - `ExtendsAccordion.vue`
   - `ExtendsIcon.vue`

### 데이터베이스 파일 (2개)
1. **MariaDB**: `sql-ui-extends.xml`
2. **PostgreSQL**: `sql-ui-extends.xml`

### 핵심 테이블 (3개)
1. `tb_offer_dpdc_rel_d` - 종속관계 테이블
2. `tb_item_mpng_d` - 제품 매핑 테이블
3. `tb_offer_group_rel_d` - 제품 그룹 관계 테이블

---

## 🎯 구현 특징

### 1. **모듈화된 구조**
- 백엔드: Controller → Service → DAO 레이어 분리
- 프론트엔드: API → Store → Component 레이어 분리

### 2. **효율적인 데이터 처리**
- Stream API를 활용한 데이터 변환 및 그룹핑
- 중복 제거 및 참조 데이터 자동 매핑
- 트랜잭션 관리를 통한 데이터 일관성 보장

### 3. **유연한 관계 관리**
- 리더-팔로워 양방향 조회 지원
- 그룹 단위 관계 관리
- 유효 기간 기반 필터링

### 4. **사용자 친화적 UI**
- 아코디언 방식의 계층적 표시
- 드래그 앤 드롭 지원
- 시각적 관계 라인 표시 (Canvas)

---

*본 문서는 Vizier 프로젝트의 리더-팔로우 종속관계 기능 구현에 관련된 모든 소스 파일을 체계적으로 정리한 것입니다.*

