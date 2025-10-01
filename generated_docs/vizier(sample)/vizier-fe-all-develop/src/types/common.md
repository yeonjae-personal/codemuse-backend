# 📄 common.ts

> **파일 경로**: `vizier(sample)/vizier-fe-all-develop/src/types/common.ts`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Javascript / Typescript
---

## 📑 목차

### 🏗️ 클래스
- [`BaseSearchPaneParamClass`](#class-basesearchpaneparamclass) - 복잡도: 23
- [`BaseItemSearchPaneDto`](#class-baseitemsearchpanedto) - 복잡도: 44


## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `@/enums` • `@/interfaces/prod/menu` • `@/interfaces/admin/label-management` | ⚡ **총 복잡도**: 67 |
| 📊 **총 토큰 수**: 806 |  |



## 🏗️ 클래스

### <a id="class-basesearchpaneparamclass"></a>🎯 `BaseSearchPaneParamClass`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 23 |



<details>
<summary>🔍 코드 미리보기</summary>

```javascript
export class BaseSearchPaneParamClass implements BaseSearchPaneParam {
  type?: string;
  subType?: string;
  searchBy: SearchBy;
  searchKey?: string;
  page?: number;
  size?: number;

  constructor(
    type: string = "",
    subType: string | undefined = undefined,
    searchBy: SearchBy = SearchBy.Name,
    searchKey: string = "",
    page: number = 1,
    size: number = 10
  ) {
    this.type = type;
    this.subType = subType;
    this.searchBy = searchBy;
    this.searchKey = searchKey;
    this.page = page;
    this.size = size;
  }
}...
```

**Chunk 정보**
- 🆔 **ID**: `5617dc24d2b4`
- 📍 **라인**: 218-241
- 📊 **토큰**: 67
- 🏷️ **태그**: `class, javascript`

</details>

---

### <a id="class-baseitemsearchpanedto"></a>🎯 `BaseItemSearchPaneDto`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 44 |



<details>
<summary>🔍 코드 미리보기</summary>

```javascript
export class BaseItemSearchPaneDto {
  itemUnique: string;
  itemName: string;
  itemDescription?: string;
  itemType?: string;
  validEndDtm?: string;
  validStartDtm?: string;
  editable?: boolean;
  showAppendIcon?: boolean;
  isNew?: boolean;
  itemLargeType?: string;
  itemDetail?: any;
  expand?: boolean;
  useYn?: string;

  constructor(
    itemUnique: string,
    itemName: string,
    itemDescription?: string,
    itemType?: string,
    validEndDtm?: string,
    validStartDtm?: string,
    editable?: boolean,
    showAppendIcon?: boolean,
    isNew?: boolean,
    itemLargeType?: string,
    itemDetail?: any,
    expand?: boolean,
    useYn?: string
  ) {
    this.itemUnique = itemUnique;
    this.itemName = itemName;
    this.itemDescription = itemDescription;
    this.itemType = ...
```

**Chunk 정보**
- 🆔 **ID**: `5647989e2908`
- 📍 **라인**: 243-287
- 📊 **토큰**: 106
- 🏷️ **태그**: `class, javascript`

</details>

---




## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **806개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 633 | 78.5% |
| 🏗️ 클래스 | 2 | 33.5 | 173 | 21.5% |

