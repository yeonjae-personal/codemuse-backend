# 📄 BasePaginationDto.java

> **파일 경로**: `vizier(sample)/be/src/main/java/com/lgcns/svcp/prod/util/paging/BasePaginationDto.java`  
> **생성일**: 2025-09-26  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`BasePaginationDto`](#class-basepaginationdto) - 복잡도: 52

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.LinkedHashMap` • `java.util.Map` • `com.fasterxml.jackson.annotation.JsonIgnore` | ⚡ **총 복잡도**: 52 |
| 📊 **총 토큰 수**: 456 |  |



## 🏗️ 클래스

### <a id="class-basepaginationdto"></a>🎯 `BasePaginationDto`

> 📝 **클래스 설명**  
> /**

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 52 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 52 || 📍 **라인 범위** | 12-12 |
| 🏷️ **태그** | `class, java` |
#### 📚 Javadoc 상세

```
/**
```


<details>
<summary>🔍 코드 미리보기</summary>

```java
public class BasePaginationDto {
	
	private int page = 1; // Page starts at 1
	private int size = 10; // Default 10 records per page
	private String sort; // Sort field and direction, e.g., "col1 ASC,col2 DESC,col3"

	public int getPage() {
		return page;
	}

	/**
	 * Ensures page is at least 1.
	 */
	public void setPage(int page) {
		this.page = Math.max(page, 1);
	}

	public int getSize() {
		return size;
	}

	/**
	 * Ensures size is greater than 0, defaults to 10 if invalid.
	 */
	public void setSize(int size) {
		this.size = (size <= 0) ? 10 : size;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	/**
	 * Parses sort string (e.g., "col1 ASC,col2 DESC,col3") into a Map. Default
	 * direction is "ASC" if not specified. Preserv...
```

**Chunk 정보**
- 🆔 **ID**: `5e904e25f746`
- 📍 **라인**: 12-12
- 📊 **토큰**: 215
- 🏷️ **태그**: `class, java`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **456개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 241 | 52.9% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 52.0 | 215 | 47.1% |

