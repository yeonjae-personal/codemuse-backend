# 📄 FieldService.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ruleengine/service/FieldService.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`FieldService`](#class-fieldservice) - 복잡도: 35 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `org.apache.commons.lang3.StringUtils` • `org.springframework.beans.factory.annotation.Autowired` • `org.springframework.context.MessageSource` • `org.springframework.context.i18n.LocaleContextHolder` • `org.springframework.stereotype.Service` 외 9개 | ⚡ **총 복잡도**: 35 |
| 📊 **총 토큰 수**: 255 |  |



## 🏗️ 클래스

### <a id="class-fieldservice"></a>🎯 `FieldService`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 35 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 35 || 📍 **라인 범위** | 25-25 |
| 🏷️ **태그** | `class, java, autowired, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class FieldService {
	
	private final CommonDao commonDao;
	private final FieldMapper fieldMapper;
	
	@Autowired
	public MessageSource messageSource;
	
	public PageResult<FieldDto> retrieveAllFields(FieldSearchPagingDto params) {
		FieldSearchPagingDto properties = buildProperties(params);
		PageResult<FieldEntity> pageResult = commonDao.selectPagedList("Rule-Field.retrieveAllFields", properties);
		List<FieldDto> dtos = fieldMapper.convertListEntityToDto(pageResult.getElements());
		return new PageResult<FieldDto>(dtos, pageResult.getPage(), pageResult.getSize(), pageResult.getTotalElements());
	}

	private FieldSearchPagingDto buildProperties(FieldSearchPagingDto params) {
		if (StringUtils.isNotBlank(params.getType()) && StringUtils.isNotBlank(params.getValue())) {
			switch (par...
```

**Chunk 정보**
- 🆔 **ID**: `1e7b0369c778`
- 📍 **라인**: 25-25
- 📊 **토큰**: 110
- 🏷️ **태그**: `class, java, autowired, spring-boot`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **255개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 145 | 56.9% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 35.0 | 110 | 43.1% |

