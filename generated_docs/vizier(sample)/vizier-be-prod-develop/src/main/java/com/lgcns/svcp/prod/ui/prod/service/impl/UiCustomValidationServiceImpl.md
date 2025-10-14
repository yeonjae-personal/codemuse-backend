# 📄 UiCustomValidationServiceImpl.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/service/impl/UiCustomValidationServiceImpl.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`UiCustomValidationServiceImpl`](#class-uicustomvalidationserviceimpl) - 복잡도: 1158 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.ArrayList` • `java.util.Arrays` • `java.util.Calendar` • `java.util.Comparator` • `java.util.Date` • `java.util.HashMap` 외 54개 | ⚡ **총 복잡도**: 1158 |
| 📊 **총 토큰 수**: 7,616 |  |



## 🏗️ 클래스

### <a id="class-uicustomvalidationserviceimpl"></a>🎯 `UiCustomValidationServiceImpl`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 1158 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 1158 || 📍 **라인 범위** | 69-69 |
| 🏷️ **태그** | `class, java, autowired, value, valid, transactional, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class UiCustomValidationServiceImpl implements UiCustomValidationService {
	
	@Value("${external.api.comm}")
    private String commUrl;

	@Autowired
	private RestTemplate restTemplate;
	
	private final CommonDao commonDao;
	private final CustomValidationExcelMapper customValidationExcelMapper;
	private final CustomValidationExcelHelper excelHelper;
		
	@Override
	public List<CustomValidationMainRespone> getListMain(String item, String type, String subType, String attrUuid) {
		List<CustomValidationMainRespone> results = new ArrayList<>();
		Map<String, Object> paramsGetList = new HashMap<>();
		if (item.equals("O") || item.equals("R")) {
			paramsGetList.put("itemCode", type);
		} else if (item.equals("C")) {
			paramsGetList.put("itemCode", subType);
		}
		List<AttributeDto> entit...
```

**Chunk 정보**
- 🆔 **ID**: `32447552a61d`
- 📍 **라인**: 69-69
- 📊 **토큰**: 3746
- 🏷️ **태그**: `class, java, autowired, value, valid...`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **7,616개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 3,870 | 50.8% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 1158.0 | 3,746 | 49.2% |

