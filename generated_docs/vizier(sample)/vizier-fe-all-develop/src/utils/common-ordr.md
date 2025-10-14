# 📄 common-ordr.ts

> **파일 경로**: `vizier(sample)/vizier-fe-all-develop/src/utils/common-ordr.ts`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 2개  
> **언어**: Javascript / Typescript
---

## 📑 목차

### 🏗️ 클래스
- [`CommonOrdrUtil`](#class-commonordrutil) - 복잡도: 94


## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `@/store` • `./http-common` • `moment-timezone` | ⚡ **총 복잡도**: 94 |
| 📊 **총 토큰 수**: 618 |  |



## 🏗️ 클래스

### <a id="class-commonordrutil"></a>🎯 `CommonOrdrUtil`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 94 |



<details>
<summary>🔍 코드 미리보기</summary>

```javascript
export class CommonOrdrUtil {
  public static getCurrentTime = () => {
    return moment(new Date()).tz("Asia/Seoul").format("YYYY-MM-DDTHH:mm:ss");
  };

  public static execute = async (
    mthd: string, // ex) GET
    uri: string, // ex) /api/ordr/ordrevetitem/v1/evetitemevet
    reqData: any
  ) => {
    let retVal;

    if (mthd === "GET") {
      let queryParam = "?";
      queryParam += CommonOrdrUtil.jsonToQueryString(reqData);
      retVal = await httpClient.get(uri + queryParam);
    } else if (mthd === "POST") {
      retVal = await httpClient.post(uri, reqData);
    } else if (mthd === "PUT") {
      retVal = await httpClient.put(uri, reqData);
    }

    if (retVal?.status === 200) {
      if (retVal?.data?.errorCode == "400") {
        await CommonOrdrUtil.showErrorAlert(ret...
```

**Chunk 정보**
- 🆔 **ID**: `3f6ac7977286`
- 📍 **라인**: 5-105
- 📊 **토큰**: 301
- 🏷️ **태그**: `class, javascript`

</details>

---




## 🧩 Chunk 요약

이 파일은 총 **2개의 chunk**로 구성되어 있으며, **618개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 317 | 51.3% |
| 🏗️ 클래스 | 1 | 94.0 | 301 | 48.7% |

