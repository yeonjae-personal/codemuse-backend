# 📄 UiLabelService.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/ui/prod/service/UiLabelService.java`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🔌 인터페이스
- [`UiLabelService`](#interface-uilabelservice)


## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.io.IOException` • `java.util.List` • `com.lgcns.svcp.prod.ui.prod.dto.label.LabelItemDto` • `com.lgcns.svcp.prod.ui.prod.dto.label.LabelSearchPagingDto` • `com.lgcns.svcp.prod.ui.prod.dto.label.MultiLangLabelDto` • `com.lgcns.svcp.prod.util.paging.PageResult` 외 3개 | ⚡ **총 복잡도**: 9 |
| 📊 **총 토큰 수**: 86 |  |




## 🔌 인터페이스

### <a id="interface-uilabelservice"></a>🔌 `UiLabelService`


#### 📋 인터페이스 정보

| 속성 | 값 |
|------|----|
| 📍 **라인 범위** | 15-15 |
| 🏷️ **태그** | `interface, java` |
<details>
<summary>🔍 코드 미리보기</summary>

```java
public interface UiLabelService {
	PageResult<MultiLangLabelDto> findAll(LabelSearchPagingDto params);

	void save(MultiLangLabelDto input);

	void delete(String labelId);

	void exportExcel(String type, String value, String language, HttpServletResponse response);

	void importExcel(HttpServletRequest request) throws IOException, ServletException;

	List<MultiLangLabelDto> findLanguageI18n();

	List<LabelItemDto> getAllLanguage();
}...
```

**Chunk 정보**
- 🆔 **ID**: `0df1f0ad1f85`
- 📊 **토큰**: 33

</details>

---




## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **86개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 53 | 61.6% |
| package | 1 | 0.0 | 0 | 0.0% |
| interface | 1 | 9.0 | 33 | 38.4% |

