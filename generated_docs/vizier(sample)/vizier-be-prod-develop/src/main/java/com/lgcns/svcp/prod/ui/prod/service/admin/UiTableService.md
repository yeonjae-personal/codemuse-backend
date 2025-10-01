# 📄 UiTableService.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/service/admin/UiTableService.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`UiTableService`](#class-uitableservice) - 복잡도: 251

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.math.BigDecimal` • `java.util.ArrayList` • `java.util.Collections` • `java.util.HashMap` • `java.util.LinkedHashMap` • `java.util.LinkedHashSet` 외 32개 | ⚡ **총 복잡도**: 251 |
| 📊 **총 토큰 수**: 2,378 |  |



## 🏗️ 클래스

### <a id="class-uitableservice"></a>🎯 `UiTableService`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 251 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 251 || 📍 **라인 범위** | 47-47 |
| 🏷️ **태그** | `class, java` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class UiTableService {

	private final CommonDao commonDao;
	private final MessageSource messageSource;
	private final TableExcelHelper excelHelper;

	public PageResult<?> searchTableType(SearchTableTypeReqDto reqDto) {
		return commonDao.selectPagedList("Ui-table.searchTableType", reqDto);
	}

	public TableStrcTypeDto retrieveTableType(SearcTableStrcReqDto reqDto) {
		SearchTableTypeReqDto typeReqDto = new SearchTableTypeReqDto();
		typeReqDto.setTableTypeCode(reqDto.getTableTypeCode());

		TableStrcTypeDto tableStrcTypeDto = commonDao.select("Ui-table.searchTableType", typeReqDto);

		if (tableStrcTypeDto != null) {
			PageResult<?> tableStrcDtos = commonDao.selectPagedList("Ui-table.searchTableStrc", reqDto);
			tableStrcTypeDto.setTableStrcDtos(tableStrcDtos);
		}
		return table...
```

**Chunk 정보**
- 🆔 **ID**: `d5c4ae6ef39e`
- 📍 **라인**: 47-47
- 📊 **토큰**: 1149
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **2,378개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 1,229 | 51.7% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 251.0 | 1,149 | 48.3% |

