# 📄 CharacteristicService.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/online/prod/service/CharacteristicService.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`CharacteristicService`](#class-characteristicservice) - 복잡도: 95 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `org.springframework.beans.factory.annotation.Autowired` • `org.springframework.stereotype.Component` • `com.lgcns.svcp.prod.online.prod.dto.characteristic.BlngInfoMDto` • `com.lgcns.svcp.prod.online.prod.dto.characteristic.DcntCstcMDto` • `com.lgcns.svcp.prod.online.prod.dto.characteristic.DcntTrgtInfoDDto` 외 11개 | ⚡ **총 복잡도**: 95 |
| 📊 **총 토큰 수**: 627 |  |



## 🏗️ 클래스

### <a id="class-characteristicservice"></a>🎯 `CharacteristicService`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 95 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 95 || 📍 **라인 범위** | 25-25 |
| 🏷️ **태그** | `class, java, autowired, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class CharacteristicService {
	@Autowired
	private CommonDao commonDao;
	
	public List<BlngInfoMDto> retrieveBlngInfoMList(BlngInfoMDto blngInfoMDto) {
		List<BlngInfoMDto> resultList = commonDao.selectList("Characteristic.retrieveBlngInfoMList", blngInfoMDto);
		return resultList;
		}

		public BlngInfoMDto retrieveBlngInfoM(BlngInfoMDto blngInfoMDto) {
		return commonDao.select("Characteristic.retrieveBlngInfoM", blngInfoMDto);
		}

		public List<DcntCstcMDto> retrieveDcntCstcMList(DcntCstcMDto dcntCstcMDto) {
		List<DcntCstcMDto> resultList = commonDao.selectList("Characteristic.retrieveDcntCstcMList", dcntCstcMDto);
		return resultList;
		}

		public DcntCstcMDto retrieveDcntCstcM(DcntCstcMDto dcntCstcMDto) {
		return commonDao.select("Characteristic.retrieveDcntCstcM", dcntCstc...
```

**Chunk 정보**
- 🆔 **ID**: `fc0d107bcdb8`
- 📍 **라인**: 25-25
- 📊 **토큰**: 295
- 🏷️ **태그**: `class, java, autowired, spring-boot`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **627개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 332 | 53.0% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 95.0 | 295 | 47.0% |

