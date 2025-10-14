# 📄 SubMenuCommon.vue

> **파일 경로**: `vizier(sample)/vizier-fe-all-develop/src/components/prod/layout/SubMenuCommon.vue`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 4개  
> **언어**: Javascript / Vue
---




## 📑 목차

### ⚙️ 함수
- [`handleClick`](#function-handleclick) - 복잡도: 29


## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `@/store` • `lodash-es/clone` • `@/utils/config-path` • `vue-router` | ⚡ **총 복잡도**: 29 |
| 📊 **총 토큰 수**: 565 |  |




## ⚙️ 함수

### <a id="function-handleclick"></a>🔧 `handleClick`

![복잡도](https://img.shields.io/badge/복잡도-29-red)

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 29 |
| 📊 토큰 수 | 79 |
| 📍 라인 범위 | 41-69 |





<details>
<summary>💻 코드 미리보기</summary>

```javascript
function handleClick(item: any, parent?: any) {
  const instance = clone(item);
  instance.path = configPath(instance);
  instance.menuNm = props.nameParent;
  instance.rawName = props.nameParent;
  instance.tabName = "";
  if (parent) {
    instance.tabName +=
      item.menuLv === "2" ? item.menuNm : `${parent.menuNm} ${item.menuNm}`;
    instance.menuNm += ` ${parent.menuNm} ${item.menuNm}`;
    instance.rawName += `${parent.menuNm}${item.menuNm}`.replace(/\s+/g, "");
    menuStore.setOpenId([parent?.menuId]);
  } else {
    instance.menuNm += ` ${item.menuNm}`;
    instance.rawName += item.menuNm.replace(/\s+/g, "");
    instance.tabName += item.menuNm;
  }
  if (addTab) {
    addTab(instance);
  }
  if (menuList?.length < 5) {
    router.push(configPath(item));
    menuStore.setActive...
```

**Chunk 메타데이터**
- 🆔 **ID**: `1d5cd02b502c`
- 🏷️ **태그**: `function, javascript`

</details>

---



## 🧩 Chunk 요약

이 파일은 총 **4개의 chunk**로 구성되어 있으며, **565개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 486 | 86.0% |
| template | 1 | 0.0 | 0 | 0.0% |
| ⚙️ 함수 | 1 | 29.0 | 79 | 14.0% |
| style | 1 | 0.0 | 0 | 0.0% |

