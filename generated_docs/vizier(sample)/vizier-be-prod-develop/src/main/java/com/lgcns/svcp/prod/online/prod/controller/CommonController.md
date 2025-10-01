# 📄 CommonController.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/online/prod/controller/CommonController.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`CommonController`](#class-commoncontroller) - 복잡도: 11 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `org.springframework.beans.factory.annotation.Autowired` • `org.springframework.http.ResponseEntity` • `org.springframework.web.bind.annotation.CrossOrigin` • `org.springframework.web.bind.annotation.GetMapping` • `org.springframework.web.bind.annotation.ModelAttribute` 외 11개 | ⚡ **총 복잡도**: 11 |
| 📊 **총 토큰 수**: 314 |  |



## 🏗️ 클래스

### <a id="class-commoncontroller"></a>🎯 `CommonController`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 11 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 11 || 📍 **라인 범위** | 29-29 |
| 🏷️ **태그** | `class, java, autowired, getmapping, pathvariable, requestparam, param, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class CommonController {
	@Autowired
	private CommonService commonService;
//	@GetMapping(value = "/mapping/uuidcodes")
	@Operation(summary = "UUID - 코드 매핑 리스트 조회 API", description = "데이터에 UUID정보만 있어 코드를 알아야할 때 사용")
	public ResponseEntity<List<ProdItemMapgMDto>> retrieveProdItemMapgMList(@ModelAttribute ProdItemMapgMDto prodItemMapgMDto) {
		ProdItemMapgMDto request = new ProdItemMapgMDto();
		request.setProdItemCd(prodItemMapgMDto.getProdItemCd());
		List<ProdItemMapgMDto> response = commonService.retrieveProdItemMapgMList(request);
		return ResponseEntity.ok(response);
	}
	//uuid로 단건 조회는 보류
//	@GetMapping(value = "/mappings/{prodUuid}")
//	@Operation(summary = "", description = "")
//	public ResponseEntity<ProdItemMapgMDto> retrieveProdItemMapgM(@Parameter(description ="", require...
```

**Chunk 정보**
- 🆔 **ID**: `511aaac6ba28`
- 📍 **라인**: 29-29
- 📊 **토큰**: 133
- 🏷️ **태그**: `class, java, autowired, getmapping, pathvariable...`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **314개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 181 | 57.6% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 11.0 | 133 | 42.4% |

