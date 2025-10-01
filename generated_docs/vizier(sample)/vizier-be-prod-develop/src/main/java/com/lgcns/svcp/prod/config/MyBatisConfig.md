# 📄 MyBatisConfig.java

> **파일 경로**: `vizier(sample)/vizier-be-prod-develop/src/main/java/com/lgcns/svcp/prod/config/MyBatisConfig.java`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 3개  
> **언어**: Java
---

## 📑 목차

### 🏗️ 클래스
- [`MyBatisConfig`](#class-mybatisconfig) - 복잡도: 18 (spring-boot)

## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `java.util.List` • `org.apache.ibatis.plugin.Interceptor` • `org.apache.ibatis.session.SqlSessionFactory` • `org.mybatis.spring.SqlSessionTemplate` • `org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer` • `org.springframework.context.annotation.Bean` 외 2개 | ⚡ **총 복잡도**: 18 |
| 📊 **총 토큰 수**: 107 |  |



## 🏗️ 클래스

### <a id="class-mybatisconfig"></a>🎯 `MyBatisConfig`

| 속성 | 값 |
|------|----|
| ⚡ 복잡도 | 18 |



#### 📋 클래스 정보

| 속성 | 값 |
|------|----|
| ⚡ **복잡도** | 18 || 📍 **라인 범위** | 15-15 |
| 🏷️ **태그** | `class, java, bean, spring-boot` || 🏗️ **프레임워크** | `spring-boot` |

<details>
<summary>🔍 코드 미리보기</summary>

```java
public class MyBatisConfig {

	@Bean
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}

	@Bean
	public Interceptor auditInterceptor() {
		return new AuditInterceptor();
	}

	@Bean
	public ConfigurationCustomizer configurationCustomizer(List<Interceptor> interceptors) {
		return configuration -> {
			for (Interceptor interceptor : interceptors) {
				configuration.addInterceptor(interceptor);
			}
		};
	}
}...
```

**Chunk 정보**
- 🆔 **ID**: `e35837333942`
- 📍 **라인**: 15-15
- 📊 **토큰**: 44
- 🏷️ **태그**: `class, java, bean, spring-boot`

</details>

---





## 🧩 Chunk 요약

이 파일은 총 **3개의 chunk**로 구성되어 있으며, **107개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 63 | 58.9% |
| package | 1 | 0.0 | 0 | 0.0% |
| 🏗️ 클래스 | 1 | 18.0 | 44 | 41.1% |

