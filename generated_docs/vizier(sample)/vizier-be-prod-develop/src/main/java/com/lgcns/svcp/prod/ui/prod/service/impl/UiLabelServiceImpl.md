# 📄 UiLabelServiceImpl.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/service/impl/UiLabelServiceImpl.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`UiLabelServiceImpl`](#class-uilabelserviceimpl) - 복잡도: 277 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.io.IOException` • `java.io.InputStream` • `java.util.ArrayList` • `java.util.HashMap` • `java.util.List` • `java.util.Map` 외 28개 | ⚡ **총 복잡도**: 277 |
| 📊 **총 토큰 수**: 2,150 |  |



## 🏗️ 클래스

### <a id="class-uilabelserviceimpl"></a>🎯 `UiLabelServiceImpl`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 277 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 277 || 📍 **라인 범위** | 43-43 |
| 🏷️ **태그** | `class, java, transactional, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class UiLabelServiceImpl implements UiLabelService {

	private final CommonDao commonDao;
	private final LabelMapper labelMapper;
	private final ExcelHelper excelHelper;

	@Override
	public PageResult<MultiLangLabelDto> findAll(LabelSearchPagingDto params) {
		LabelSearchPagingDto properties = buildProperties(params);
		PageResult<MultiLangLabelEntity> pageResult = commonDao.selectPagedList("ui-multi-lang-label-m.findByProperties", properties);
		List<MultiLangLabelDto> dtos = convertListEntityToDto(pageResult.getElements());
		return new PageResult<MultiLangLabelDto>(dtos, pageResult.getPage(), pageResult.getSize(), pageResult.getTotalElements());
	}

	private List<MultiLangLabelDto> convertListEntityToDto(List<MultiLangLabelEntity> entities) {
		return entities.stream().map(item -...
```

**Chunk 정보**
- 🆔 **ID**: `9a0925add439`
- 📍 **라인**: 43-43
- 📊 **토큰**: 1039
- 🏷️ **태그**: `class, java, transactional, spring-boot`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **2,150개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 1,111 | 51.7% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 277.0 | 1,039 | 48.3% |

