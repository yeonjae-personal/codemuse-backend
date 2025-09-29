# 📄 UIExtendsService.java

> **파일 경로**: `vizier(sample)/be/src/main/java/com/lgcns/svcp/prod/ui/prod/service/UIExtendsService.java`  
> **생성일**: 2025-09-26  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`UIExtendsService`](#class-uiextendsservice) - 복잡도: 249 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `com.lgcns.svcp.prod.constant.SystemConstant.OBJ_UUID` • `java.util.ArrayList` • `java.util.Collections` • `java.util.List` • `java.util.Map` • `java.util.Objects` 외 36개 | ⚡ **총 복잡도**: 249 |
| 📊 **총 토큰 수**: 2,072 |  |



## 🏗️ 클래스

### <a id="class-uiextendsservice"></a>🎯 `UIExtendsService`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 249 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 249 || 📍 **라인 범위** | 53-53 |
| 🏷️ **태그** | `class, java, transactional, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class UIExtendsService {

	private final CommonDao commonDao;
	private final UIHistoryService uiHistoryService;
	private final UiTableService uiTableService;
	private final RelationManagerExcelHelper excelHelper;
	private final RelationManagerMapper managerMapper;
	private final MessageSource messageSource;

	private static final String OFFER_GROUP_UUIDS_KEY = "offerGroupUuids";
	private static final String REFERENCE_UUIDS_KEY = "referenceUuids";

	public TargetResDto getTarget(TargetReqDto req) {
		List<OffrGrpResDto> leaderGroups = commonDao.selectList("Ui-extends.getTargetLeader", req);
		List<OffrGrpResDto> followerGroups = commonDao.selectList("Ui-extends.getTargetFollower", req);

		// For-each leaderGroups
		for (OffrGrpResDto group : leaderGroups) {
			RelationViewReqDto rel...
```

**Chunk 정보**
- 🆔 **ID**: `b56a0d8d06ee`
- 📍 **라인**: 53-53
- 📊 **토큰**: 991
- 🏷️ **태그**: `class, java, transactional, spring-boot`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **2,072개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 1,081 | 52.2% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 249.0 | 991 | 47.8% |

