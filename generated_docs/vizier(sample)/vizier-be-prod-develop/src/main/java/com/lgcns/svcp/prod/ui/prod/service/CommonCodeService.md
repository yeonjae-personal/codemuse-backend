# 📄 CommonCodeService.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/service/CommonCodeService.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`CommonCodeService`](#class-commoncodeservice) - 복잡도: 53 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.Collections` • `java.util.HashMap` • `java.util.List` • `java.util.Map` • `java.util.function.Function` • `java.util.stream.Collectors` 외 10개 | ⚡ **총 복잡도**: 53 |
| 📊 **총 토큰 수**: 421 |  |



## 🏗️ 클래스

### <a id="class-commoncodeservice"></a>🎯 `CommonCodeService`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 53 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 53 || 📍 **라인 범위** | 23-23 |
| 🏷️ **태그** | `class, java, autowired, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class CommonCodeService {

	private static final String ITEM_CODE = "itemCode";
	private static final String PROPERTIES = "properties";

	@Autowired
	private CommonDao commonDao;

	public Attribute getGeneralCommonCodeDescription(String codeType, String codeValue) {
		RequestCommonCodeDto request = new RequestCommonCodeDto();
		request.setColumnName(codeType); request.setColumnValue(codeValue);
		
        String description = commonDao.select("UI.getGeneralCommonCodeDescription", request);
      
        return new Attribute(codeValue, description);
    }
	
	public String getAdditionalCommonCodeDescription(String attrUuid, String prodUuid) {
		RequestCommonCodeDto request = new RequestCommonCodeDto();
		request.setAttrUuid(attrUuid); request.setProdUuid(prodUuid);
        String des...
```

**Chunk 정보**
- 🆔 **ID**: `422943a776a5`
- 📍 **라인**: 23-23
- 📊 **토큰**: 193
- 🏷️ **태그**: `class, java, autowired, spring-boot`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **421개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 228 | 54.2% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 53.0 | 193 | 45.8% |

