# 📊 Vizier 프로젝트의 종속관계 (Dependency Relationships)

> **생성일**: 2025-09-26  
> **프로젝트**: Vizier (제품/서비스 관리 플랫폼)  
> **주제**: 제품 간 종속관계 및 리더-팔로우 관계

---

## 🎯 개요

Vizier 프로젝트에서 **종속관계**는 제품 간의 계층적 관계와 영향 분석을 중심으로 구현되어 있습니다. 특히 **리더-팔로우 관계(Leader-Follower Relationship)**를 통해 제품 간의 의존성을 세밀하게 관리합니다.

---

## 🏗️ 핵심 데이터 모델

### 📋 제품 관계 테이블 구조

#### **1. 제품 매핑 테이블 (핵심 관계 테이블)**
```sql
-- 제품 매핑 테이블
tb_item_mpng_d:
- obj_uuid: 제품 고유 ID
- obj_code: 제품 코드  
- obj_name: 제품 명칭
- dplc_trgt_uuid: 배치 대상 UUID (부모 제품)
- valid_start_dtm: 유효 시작일
- valid_end_dtm: 유효 종료일
```

#### **2. 제품 의존성 관계 테이블**
```sql
-- 제품 의존성 관계 테이블
tb_offer_dpdc_rel_d:
- base_uuid: 리더 제품 UUID (의존하는 제품)
- trgt_uuid: 팔로워 제품 UUID (의존받는 제품)  
- dpdc_rel_uuid: 관계 유형 UUID
- valid_start_dtm: 관계 시작일
- valid_end_dtm: 관계 종료일
```

#### **3. 제품 구조 테이블**
```sql
-- 제품 구조 테이블
tb_offer_strc_d:
- base_uuid: 기준 제품 UUID
- trgt_uuid: 대상 제품 UUID
- valid_start_dtm: 유효 시작일
- valid_end_dtm: 유효 종료일
```

---

## 🎯 종속관계의 4가지 유형

### **👨‍👩‍👧‍👦 1. 부모-자식 관계 (Parent-Child)**
- **정의**: 계층적 제품 구조에서 상위-하위 관계
- **특징**: `dplc_trgt_uuid`를 통한 부모 제품 참조
- **API**: `getParentProdM`, `getChildrenProdM`

```sql
-- 부모 제품 조회
SELECT parent.obj_uuid, parent.obj_code, parent.obj_name
FROM tb_item_mpng_d parent
JOIN tb_item_mpng_d child ON child.dplc_trgt_uuid = parent.obj_uuid
WHERE child.obj_uuid = #{productUuid}
```

### **👥 2. 형제 관계 (Sibling)**
- **정의**: 같은 부모를 가진 제품들 간의 관계
- **특징**: 동일한 `dplc_trgt_uuid`를 가진 제품들
- **API**: `getSiblingsProdM`

```sql
-- 형제 제품 조회
SELECT sibling.obj_uuid, sibling.obj_code, sibling.obj_name
FROM tb_item_mpng_d sibling
WHERE sibling.dplc_trgt_uuid = (
    SELECT dplc_trgt_uuid 
    FROM tb_item_mpng_d 
    WHERE obj_uuid = #{productUuid}
)
AND sibling.obj_uuid != #{productUuid}
```

### **👑 3. 리더-팔로워 관계 (Leader-Follower)**
- **정의**: 제품 간의 의존성 관계에서 영향력을 가진 제품과 영향받는 제품
- **특징**: `tb_offer_dpdc_rel_d` 테이블을 통한 관계 관리
- **API**: `getTargetLeader`, `getTargetFollower`, `getLeaderView`, `getFollowerView`

#### **리더(Leader)**
- **역할**: 다른 제품들이 의존하는 기준 제품
- **특징**: 변경 시 팔로워들에게 영향을 미치는 제품
- **식별**: `base_uuid`로 식별

#### **팔로워(Follower)**
- **역할**: 리더 제품에 의존하는 제품들
- **특징**: 리더의 변경에 따라 영향받는 제품
- **식별**: `trgt_uuid`로 식별

```sql
-- 리더 제품 조회
SELECT leader.obj_uuid, leader.obj_code, leader.obj_name
FROM tb_item_mpng_d leader
JOIN tb_offer_dpdc_rel_d rel ON rel.base_uuid = leader.obj_uuid
WHERE rel.trgt_uuid = #{productUuid}
```

```sql
-- 팔로워 제품 조회
SELECT follower.obj_uuid, follower.obj_code, follower.obj_name
FROM tb_item_mpng_d follower
JOIN tb_offer_dpdc_rel_d rel ON rel.trgt_uuid = follower.obj_uuid
WHERE rel.base_uuid = #{productUuid}
```

### **🔗 4. 제품 그룹 관계 (Product Group)**
- **정의**: 관련된 제품들을 그룹으로 묶는 관계
- **특징**: `tb_offer_group_rel_d` 테이블을 통한 그룹 관리
- **API**: `getProductGroups`, `getGroupProducts`

```sql
-- 제품 그룹 관계 테이블
tb_offer_group_rel_d:
- offer_group_uuid: 제품 그룹 UUID
- offer_uuid: 개별 제품 UUID
- valid_start_dtm: 관계 시작일
- valid_end_dtm: 관계 종료일
```

---

## 🔍 영향 분석 (Impact Analysis)

### **📊 영향 분석 프로세스**

1. **변경 영향도 계산**
   - 리더 제품 변경 시 모든 팔로워 제품에 미치는 영향 분석
   - 계층적 구조를 통한 연쇄 영향 분석

2. **의존성 체인 추적**
   - 직접 의존성: 1차 팔로워 제품들
   - 간접 의존성: 2차, 3차 팔로워 제품들

3. **리스크 평가**
   - 높은 의존도를 가진 제품 식별
   - 변경 시 영향받을 수 있는 제품 범위 예측

### **🎯 영향 분석 API**

```java
// 영향 분석 응답 DTO
public class ImpactAnalysisResponseDto {
    private String productUuid;
    private String productCode;
    private String productName;
    private List<ImpactItem> directImpacts;    // 직접 영향
    private List<ImpactItem> indirectImpacts;  // 간접 영향
    private int totalImpactCount;
    private String riskLevel;                  // HIGH, MEDIUM, LOW
}

// 영향 아이템 DTO
public class ImpactItem {
    private String targetUuid;
    private String targetCode;
    private String targetName;
    private String relationshipType;           // PARENT, CHILD, LEADER, FOLLOWER
    private int impactLevel;                   // 1-5 (높을수록 영향도 큼)
}
```

---

## 🎨 프론트엔드 구현

### **📱 영향 분석 컴포넌트**

#### **1. 영향 분석 대시보드**
- **파일**: `src/views/prod/impact-analysis/ImpactAnalysisView.vue`
- **기능**: 제품 간 종속관계 시각화
- **특징**: 트리 구조로 계층적 관계 표시

#### **2. 관계 관리 컴포넌트**
- **파일**: `src/components/prod/relationship/RelationshipManager.vue`
- **기능**: 제품 간 관계 생성/수정/삭제
- **특징**: 드래그 앤 드롭으로 관계 설정

#### **3. 의존성 그래프**
- **파일**: `src/components/prod/dependency/DependencyGraph.vue`
- **기능**: 제품 간 의존성 시각화
- **특징**: 네트워크 그래프로 복잡한 관계 표현

---

## 🔧 백엔드 서비스

### **📋 핵심 서비스 클래스**

#### **1. ProductRelationshipService**
```java
@Service
public class ProductRelationshipService {
    
    // 부모-자식 관계 관리
    public List<ProductDto> getParentProducts(String productUuid);
    public List<ProductDto> getChildProducts(String productUuid);
    public List<ProductDto> getSiblingProducts(String productUuid);
    
    // 리더-팔로워 관계 관리
    public List<ProductDto> getLeaderProducts(String productUuid);
    public List<ProductDto> getFollowerProducts(String productUuid);
    
    // 영향 분석
    public ImpactAnalysisResponseDto analyzeImpact(String productUuid);
    public List<ImpactItem> getImpactChain(String productUuid, int maxDepth);
}
```

#### **2. DependencyAnalysisService**
```java
@Service
public class DependencyAnalysisService {
    
    // 의존성 분석
    public DependencyAnalysisResult analyzeDependencies(String productUuid);
    
    // 순환 의존성 검출
    public List<CircularDependency> detectCircularDependencies();
    
    // 의존성 최적화 제안
    public List<OptimizationSuggestion> suggestOptimizations(String productUuid);
}
```

---

## 📊 비즈니스 가치

### **🎯 핵심 가치**

1. **변경 영향도 예측**
   - 제품 변경 시 영향받을 수 있는 모든 제품 식별
   - 리스크 관리 및 변경 계획 수립 지원

2. **의존성 최적화**
   - 불필요한 의존성 제거
   - 시스템 복잡도 감소

3. **제품 포트폴리오 관리**
   - 제품 간 관계 시각화
   - 전략적 의사결정 지원

4. **품질 관리**
   - 순환 의존성 검출
   - 아키텍처 품질 향상

---

## 🚀 사용 사례

### **📋 실제 활용 시나리오**

1. **제품 변경 시 영향 분석**
   - "A 제품을 변경하면 어떤 제품들이 영향받나요?"
   - 리더-팔로워 관계를 통한 연쇄 영향 분석

2. **제품 계층 구조 관리**
   - "B 제품의 상위/하위 제품들은 무엇인가요?"
   - 부모-자식 관계를 통한 계층 구조 파악

3. **의존성 최적화**
   - "불필요한 의존성이 있나요?"
   - 순환 의존성 검출 및 최적화 제안

4. **제품 그룹 관리**
   - "관련된 제품들을 그룹으로 묶어서 관리하고 싶어요"
   - 제품 그룹 관계를 통한 체계적 관리

---

## 🔍 기술적 특징

### **⚡ 성능 최적화**

1. **인덱스 최적화**
   - UUID 기반 인덱스
   - 복합 인덱스 (base_uuid, trgt_uuid)

2. **캐싱 전략**
   - Redis를 통한 관계 정보 캐싱
   - TTL 기반 캐시 무효화

3. **배치 처리**
   - 대량 관계 데이터 처리
   - 비동기 영향 분석

### **🔒 데이터 무결성**

1. **제약 조건**
   - 외래키 제약
   - 유니크 제약

2. **트랜잭션 관리**
   - 관계 생성/수정 시 원자성 보장
   - 롤백 메커니즘

3. **데이터 검증**
   - 순환 의존성 방지
   - 유효성 검사

---

## 📈 향후 개선 방향

### **🎯 계획된 기능**

1. **실시간 영향 분석**
   - WebSocket을 통한 실시간 업데이트
   - 변경 사항 즉시 반영

2. **AI 기반 의존성 분석**
   - 머신러닝을 통한 패턴 분석
   - 자동 최적화 제안

3. **고급 시각화**
   - 3D 네트워크 그래프
   - 인터랙티브 대시보드

4. **성능 모니터링**
   - 의존성 분석 성능 메트릭
   - 병목 지점 식별

---

## 📚 관련 문서

- [Vizier 프로젝트 전체 구조](./project_summary.md)
- [제품 관리 API 문서](./api/product_management.md)
- [영향 분석 가이드](./guides/impact_analysis.md)
- [데이터베이스 스키마](./database/schema.md)

---

*이 문서는 Vizier 프로젝트의 종속관계 및 리더-팔로우 관계에 대한 종합적인 설명을 제공합니다.*
