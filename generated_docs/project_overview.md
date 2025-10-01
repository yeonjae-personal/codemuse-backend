# 📋 분석 대상 프로젝트 개요

> **작성일**: 2025-10-01
> **목적**: CodeMuse로 분석 중인 프로젝트 정보
> **분석 대상**: 2개 프로젝트

---

## 🎯 현재 분석 대상 프로젝트 (2개)

CodeMuse는 현재 **2개의 프로젝트**를 분석하고 있습니다:

### **1. Rule Analyzer (규칙 분석기)** 📋

#### **프로젝트 정보**
- **프로젝트명**: Rule Analyzer
- **언어**: Python
- **유형**: 코드 분석 및 규칙 검증 도구
- **경로**: `sample_code/rule_analyzer/`

#### **기술 스택**
- **언어**: Python 3.x
- **주요 라이브러리**: 
  - 코드 분석 및 파싱
  - 스트리밍 처리
  - 포매팅 및 템플릿

#### **주요 기능**
1. **이슈/오류 검출**
   - 타입 불일치 검출
   - 중복 조건 감지
   - 복잡도 분석

2. **규칙 분석**
   - 조건 파싱
   - 로직 흐름 분석
   - 메트릭 생성

3. **모듈 구조**
   - `analyzers/`: 분석기 모듈
   - `formatters/`: 포매터 모듈
   - `streaming/`: 스트리밍 모듈
   - `shared/`: 공통 유틸리티

---

### **2. Vizier (비지어)** 🎯

#### **프로젝트 정보**
- **정식 명칭**: Vizier
- **한글명**: 비지어
- **유형**: 제품/서비스 관리 플랫폼
- **목적**: 통신 서비스 제품의 구조, 관계, 종속성 관리
- **경로**: `sample_code/vizier(sample)/`

#### **기술 스택**
- **백엔드**: Java, Spring Boot
  - 경로: `vizier-be-prod-develop/`
  - REST API, JPA/Hibernate
- **프론트엔드**: Vue.js, TypeScript
  - 경로: `vizier-fe-all-develop/`
  - Vue 3, Composition API

#### **주요 기능**
1. **제품 관계 관리**
   - 부모-자식 관계 (계층 구조)
   - 형제 관계
   - 제품 그룹 관계

2. **종속관계 (Dependency) 관리**
   - Leader-Follower 관계
   - 영향 분석 (Impact Analysis)
   - 의존성 추적

3. **데이터베이스 테이블**
   - `tb_item_mpng_d`: 제품 매핑 테이블
   - `tb_offer_dpdc_rel_d`: 제품 종속관계 테이블
   - `tb_offer_strc_d`: 제품 구조 테이블
   - `tb_offer_group_rel_d`: 제품 그룹 관계 테이블

---

## 📁 폴더 구조

```
sample_code/                    ← 테스트용 폴더명 (프로젝트명 아님!)
├── rule_analyzer/              ← 프로젝트 1: Rule Analyzer (Python)
│   ├── analyzers/              ← 분석기 모듈
│   ├── formatters/             ← 포매터 모듈
│   ├── streaming/              ← 스트리밍 모듈
│   └── shared/                 ← 공통 유틸리티
└── vizier(sample)/             ← 프로젝트 2: Vizier (Java + Vue.js)
    ├── vizier-be-prod-develop/ ← 백엔드 (Java, Spring Boot)
    └── vizier-fe-all-develop/  ← 프론트엔드 (Vue.js, TypeScript)
```

**⚠️ 주의사항:**
- `sample_code`는 테스트를 위한 **폴더명**이며, **프로젝트명이 아닙니다**.
- 실제 분석 대상 프로젝트는 **2개**입니다:
  1. **Rule Analyzer** - Python 기반 규칙 분석 도구
  2. **Vizier** - Java/Vue.js 기반 제품 관리 플랫폼

---

## 🔑 핵심 용어

### Rule Analyzer
- **프로젝트명**: Rule Analyzer (규칙 분석기)
- **도메인**: 코드 분석 및 규칙 검증
- **주요 기능**: 이슈 검출, 조건 분석, 메트릭 생성

### Vizier
- **프로젝트명**: Vizier (비지어)
- **도메인**: 통신 서비스 제품 관리
- **종속관계**: Leader-Follower 관계만 해당
- **제품 관계**: 부모-자식, 형제, 그룹 등 포함

---

## 📝 CodeMuse 사용 컨텍스트

이 **2개 프로젝트**는 **CodeMuse**를 통해 분석되고 있으며, CI/CD 파이프라인에서 레거시 시스템 분석 목적으로 사용될 예정입니다.

현재는 테스트 단계로, 다음 프로젝트들을 `sample_code/` 폴더에 배치하여 CodeMuse의 분석 기능을 검증하고 있습니다:

1. **Rule Analyzer** - Python 기반 분석 도구 검증
2. **Vizier** - Java/Vue.js 풀스택 프로젝트 검증

## 📊 프로젝트 비교

| 항목 | Rule Analyzer | Vizier |
|-----|--------------|--------|
| **언어** | Python | Java + Vue.js |
| **유형** | 분석 도구 | 비즈니스 플랫폼 |
| **규모** | 중소형 | 대형 |
| **도메인** | 코드 분석 | 통신 서비스 관리 |
| **아키텍처** | 모듈형 라이브러리 | 풀스택 웹 애플리케이션 |
