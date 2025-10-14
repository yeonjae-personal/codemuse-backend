# 📄 mockServiceWorker.js

> **파일 경로**: `vizier(sample)/vizier-fe-all-develop/public/mockServiceWorker.js`  
> **생성일**: 2025-10-13  
> **Chunk 수**: 8개  
> **언어**: Javascript / Javascript
---

## 📑 목차

### ⚙️ 함수
- [`handleRequest`](#function-handlerequest) - 복잡도: 31
- [`resolveMainClient`](#function-resolvemainclient) - 복잡도: 19
- [`getResponse`](#function-getresponse) - 복잡도: 46
- [`passthrough`](#function-passthrough) - 복잡도: 16
- [`sendToClient`](#function-sendtoclient) - 복잡도: 15
- [`respondWithMock`](#function-respondwithmock) - 복잡도: 11
- [`serializeRequest`](#function-serializerequest) - 복잡도: 17


## 📋 파일 개요

| | |
|--|--|
| ⚡ **총 복잡도**: 155 | 📊 **총 토큰 수**: 1,659 |




## ⚙️ 함수

### <a id="function-handlerequest"></a>🔧 `handleRequest`

![복잡도](https://img.shields.io/badge/복잡도-31-red)

> 📖 **함수 설명**  
> /**

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 31 |
| 📊 토큰 수 | 121 |
| 📍 라인 범위 | 126-164 |





<details>
<summary>💻 코드 미리보기</summary>

```javascript
async function handleRequest(event, requestId) {
  const client = await resolveMainClient(event)
  const requestCloneForEvents = event.request.clone()
  const response = await getResponse(event, client, requestId)

  // Send back the response clone for the "response:*" life-cycle events.
  // Ensure MSW is active and ready to handle the message, otherwise
  // this message will pend indefinitely.
  if (client && activeClientIds.has(client.id)) {
    const serializedRequest = await serializeRequest(requestCloneForEvents)

    // Clone the response so both the client and the library could consume it.
    const responseClone = response.clone()

    sendToClient(
      client,
      {
        type: 'RESPONSE',
        payload: {
          isMockedResponse: IS_MOCKED_RESPONSE in response,
     ...
```

**Chunk 메타데이터**
- 🆔 **ID**: `02d1d2cab042`
- 🏷️ **태그**: `function, javascript`

</details>

---

### <a id="function-resolvemainclient"></a>🔧 `resolveMainClient`

![복잡도](https://img.shields.io/badge/복잡도-19-red)

> 📖 **함수 설명**  
> /**

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 19 |
| 📊 토큰 수 | 75 |
| 📍 라인 범위 | 174-199 |





<details>
<summary>💻 코드 미리보기</summary>

```javascript
async function resolveMainClient(event) {
  const client = await self.clients.get(event.clientId)

  if (activeClientIds.has(event.clientId)) {
    return client
  }

  if (client?.frameType === 'top-level') {
    return client
  }

  const allClients = await self.clients.matchAll({
    type: 'window',
  })

  return allClients
    .filter((client) => {
      // Get only those clients that are currently visible.
      return client.visibilityState === 'visible'
    })
    .find((client) => {
      // Find the client ID that's recorded in the
      // set of clients that have registered the worker.
      return activeClientIds.has(client.id)
    })
}...
```

**Chunk 메타데이터**
- 🆔 **ID**: `71e0c5a73a90`
- 🏷️ **태그**: `function, javascript`

</details>

---

### <a id="function-getresponse"></a>🔧 `getResponse`

![복잡도](https://img.shields.io/badge/복잡도-46-red)

> 📖 **함수 설명**  
> /**

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 46 |
| 📊 토큰 수 | 243 |
| 📍 라인 범위 | 207-275 |





<details>
<summary>💻 코드 미리보기</summary>

```javascript
async function getResponse(event, client, requestId) {
  // Clone the request because it might've been already used
  // (i.e. its body has been read and sent to the client).
  const requestClone = event.request.clone()

  function passthrough() {
    // Cast the request headers to a new Headers instance
    // so the headers can be manipulated with.
    const headers = new Headers(requestClone.headers)

    // Remove the "accept" header value that marked this request as passthrough.
    // This prevents request alteration and also keeps it compliant with the
    // user-defined CORS policies.
    const acceptHeader = headers.get('accept')
    if (acceptHeader) {
      const values = acceptHeader.split(',').map((value) => value.trim())
      const filteredValues = values.filter(
        (v...
```

**Chunk 메타데이터**
- 🆔 **ID**: `75c9486b0d00`
- 🏷️ **태그**: `function, javascript`

</details>

---

### <a id="function-passthrough"></a>🔧 `passthrough`

![복잡도](https://img.shields.io/badge/복잡도-16-red)

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 16 |
| 📊 토큰 수 | 97 |
| 📍 라인 범위 | 212-235 |





<details>
<summary>💻 코드 미리보기</summary>

```javascript
  function passthrough() {
    // Cast the request headers to a new Headers instance
    // so the headers can be manipulated with.
    const headers = new Headers(requestClone.headers)

    // Remove the "accept" header value that marked this request as passthrough.
    // This prevents request alteration and also keeps it compliant with the
    // user-defined CORS policies.
    const acceptHeader = headers.get('accept')
    if (acceptHeader) {
      const values = acceptHeader.split(',').map((value) => value.trim())
      const filteredValues = values.filter(
        (value) => value !== 'msw/passthrough',
      )

      if (filteredValues.length > 0) {
        headers.set('accept', filteredValues.join(', '))
      } else {
        headers.delete('accept')
      }
    }

    return fetc...
```

**Chunk 메타데이터**
- 🆔 **ID**: `3ef225620482`
- 🏷️ **태그**: `function, javascript`

</details>

---

### <a id="function-sendtoclient"></a>🔧 `sendToClient`

![복잡도](https://img.shields.io/badge/복잡도-15-red)

> 📖 **함수 설명**  
> /**

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 15 |
| 📊 토큰 수 | 40 |
| 📍 라인 범위 | 283-300 |





<details>
<summary>💻 코드 미리보기</summary>

```javascript
function sendToClient(client, message, transferrables = []) {
  return new Promise((resolve, reject) => {
    const channel = new MessageChannel()

    channel.port1.onmessage = (event) => {
      if (event.data && event.data.error) {
        return reject(event.data.error)
      }

      resolve(event.data)
    }

    client.postMessage(message, [
      channel.port2,
      ...transferrables.filter(Boolean),
    ])
  })
}...
```

**Chunk 메타데이터**
- 🆔 **ID**: `80cb85630468`
- 🏷️ **태그**: `function, javascript`

</details>

---

### <a id="function-respondwithmock"></a>🔧 `respondWithMock`

![복잡도](https://img.shields.io/badge/복잡도-11-red)

> 📖 **함수 설명**  
> /**

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 11 |
| 📊 토큰 수 | 75 |
| 📍 라인 범위 | 306-323 |





<details>
<summary>💻 코드 미리보기</summary>

```javascript
function respondWithMock(response) {
  // Setting response status code to 0 is a no-op.
  // However, when responding with a "Response.error()", the produced Response
  // instance will have status code set to 0. Since it's not possible to create
  // a Response instance with status code 0, handle that use-case separately.
  if (response.status === 0) {
    return Response.error()
  }

  const mockedResponse = new Response(response.body, response)

  Reflect.defineProperty(mockedResponse, IS_MOCKED_RESPONSE, {
    value: true,
    enumerable: true,
  })

  return mockedResponse
}...
```

**Chunk 메타데이터**
- 🆔 **ID**: `c2c8fbd9bea9`
- 🏷️ **태그**: `function, javascript`

</details>

---

### <a id="function-serializerequest"></a>🔧 `serializeRequest`

![복잡도](https://img.shields.io/badge/복잡도-17-red)

> 📖 **함수 설명**  
> /**

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 17 |
| 📊 토큰 수 | 35 |
| 📍 라인 범위 | 328-344 |





<details>
<summary>💻 코드 미리보기</summary>

```javascript
async function serializeRequest(request) {
  return {
    url: request.url,
    mode: request.mode,
    method: request.method,
    headers: Object.fromEntries(request.headers.entries()),
    cache: request.cache,
    credentials: request.credentials,
    destination: request.destination,
    integrity: request.integrity,
    redirect: request.redirect,
    referrer: request.referrer,
    referrerPolicy: request.referrerPolicy,
    body: await request.arrayBuffer(),
    keepalive: request.keepalive,
  }
}...
```

**Chunk 메타데이터**
- 🆔 **ID**: `b0474d3ca502`
- 🏷️ **태그**: `function, javascript`

</details>

---



## 🧩 Chunk 요약

이 파일은 총 **8개의 chunk**로 구성되어 있으며, **1,659개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 973 | 58.6% |
| ⚙️ 함수 | 7 | 22.1 | 686 | 41.4% |

