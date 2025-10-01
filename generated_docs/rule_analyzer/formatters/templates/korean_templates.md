# 📄 korean_templates.py

> **파일 경로**: `rule_analyzer/formatters/templates/korean_templates.py`  
> **생성일**: 2025-10-01  
> **Chunk 수**: 9개

---

## 📑 목차

### 🏗️ 클래스
- [`KoreanTemplates`](#class-koreantemplates) - 복잡도: 0


## 📋 파일 개요

| | |
|--|--|
| 📦 **의존성**: `typing` • `options` | ⚡ **총 복잡도**: 15 |
| 📊 **총 토큰 수**: 1,753 |  |



## 🏗️ 클래스

### <a id="class-koreantemplates"></a>🎯 `KoreanTemplates`

![메서드](https://img.shields.io/badge/메서드-7개-orange)

> 📝 **클래스 설명**  
> 한국어 포맷팅 템플릿 클래스

상세도 레벨별로 다른 템플릿을 제공합니다.

#### 📋 메서드 목록

| 메서드 | 타입 | 복잡도 | 설명 |
|--------|------|--------|------|
| `_get_detailed_template` | private | 1 | 상세 레벨 템플릿 |
| `_get_normal_template` | private | 1 | 일반 레벨 템플릿 |
| `_get_simple_template` | private | 1 | 간단 레벨 템플릿 |
| `get_complexity_emoji` | public | 5 | 복잡도 점수에 따른 이모지 반환 |
| `get_emoji` | public | 2 | 이모지 반환 |
| `get_severity_emoji` | public | 2 | 심각도에 따른 이모지 반환 |
| `get_template` | public | 3 | 상세도 레벨에 따른 템플릿 반환 |



#### 🔧 메서드 상세

##### `get_complexity_emoji`
| 속성 | 값 |
|------|----|
| 🎨 데코레이터 | `classmethod` |
| ⚡ 복잡도 | 5 |
| 📊 토큰 수 | 180 |
| 📍 라인 범위 | 160-181 |
- **Signature**: `get_complexity_emoji(cls, score: int, include_emojis: bool) -> str`- **Parameters**: `cls, score: int, include_emojis: bool`- **Returns**: `str`
---
##### `get_template`
| 속성 | 값 |
|------|----|
| 🎨 데코레이터 | `classmethod` |
| ⚡ 복잡도 | 3 |
| 📊 토큰 수 | 131 |
| 📍 라인 범위 | 40-55 |
- **Signature**: `get_template(cls, detail_level: DetailLevel) -> Dict[str, str]`- **Parameters**: `cls, detail_level: DetailLevel`- **Returns**: `Dict[str, str]`
- **Calls**: `_get_simple_template`, `_get_detailed_template`, `_get_normal_template`---
##### `get_emoji`
| 속성 | 값 |
|------|----|
| 🎨 데코레이터 | `classmethod` |
| ⚡ 복잡도 | 2 |
| 📊 토큰 수 | 96 |
| 📍 라인 범위 | 120-134 |
- **Signature**: `get_emoji(cls, key: str, include_emojis: bool) -> str`- **Parameters**: `cls, key: str, include_emojis: bool`- **Returns**: `str`
- **Calls**: `get`---
##### `get_severity_emoji`
| 속성 | 값 |
|------|----|
| 🎨 데코레이터 | `classmethod` |
| ⚡ 복잡도 | 2 |
| 📊 토큰 수 | 154 |
| 📍 라인 범위 | 137-157 |
- **Signature**: `get_severity_emoji(cls, severity: str, include_emojis: bool) -> str`- **Parameters**: `cls, severity: str, include_emojis: bool`- **Returns**: `str`
- **Calls**: `get`, `lower`---
##### `_get_simple_template`
| 속성 | 값 |
|------|----|
| 🎨 데코레이터 | `classmethod` |
| ⚡ 복잡도 | 1 |
| 📊 토큰 수 | 113 |
| 📍 라인 범위 | 58-66 |
- **Signature**: `_get_simple_template(cls) -> Dict[str, str]`- **Parameters**: `cls`- **Returns**: `Dict[str, str]`
---
##### `_get_normal_template`
| 속성 | 값 |
|------|----|
| 🎨 데코레이터 | `classmethod` |
| ⚡ 복잡도 | 1 |
| 📊 토큰 수 | 297 |
| 📍 라인 범위 | 69-88 |
- **Signature**: `_get_normal_template(cls) -> Dict[str, str]`- **Parameters**: `cls`- **Returns**: `Dict[str, str]`
---
##### `_get_detailed_template`
| 속성 | 값 |
|------|----|
| 🎨 데코레이터 | `classmethod` |
| ⚡ 복잡도 | 1 |
| 📊 토큰 수 | 463 |
| 📍 라인 범위 | 91-117 |
- **Signature**: `_get_detailed_template(cls) -> Dict[str, str]`- **Parameters**: `cls`- **Returns**: `Dict[str, str]`
- **Calls**: `_get_normal_template`, `update`---
<details>
<summary>🔍 코드 미리보기</summary>

```python
class KoreanTemplates:
    """
    한국어 포맷팅 템플릿 클래스

    상세도 레벨별로 다른 템플릿을 제공합니다.
    """

    # 이모지 매핑
    EMOJIS = {
        "valid": "✅",
        "invalid": "❌",
        "warning": "⚠️",
        "info": "ℹ️",
        "error": "🚨",
        "success": "🎯",
        "complexity": "📈",
        "performance": "⚡",
        "quality": "🏆",
        "structure": "🏗️",
        "issues": "🔍",
        "metrics": "📊",
        "summary": "📋",
        "timestamp": "🕒",
        "bullet": "•",
        "separator": "─",
    }

    @classmethod
    def get_template(cls, detail_level: DetailLevel) -> Dict[str, str]:...
```

**Chunk 정보**
- 🆔 **ID**: `c22bf0f03ace`
- 📍 **라인**: 12-22
- 📊 **토큰**: 238
- 🏷️ **태그**: `class`

</details>

---



## 📊 시각화 및 분석

### ⚡ 복잡도 분석

```mermaid
pie title 복잡도 분포
    "낮음 (1-3)" : 8
    "보통 (4-7)" : 1

```

### 🔧 함수 유형 분석

```mermaid
pie title 함수 유형 분포
    "메서드" : 4
    "프라이빗 메서드" : 3

```

### 🔗 호출 순서 (Sequence)

```mermaid
sequenceDiagram
  participant get as get
  participant lower as lower
  participant _get_normal_template as _get_normal_template
  participant update as update
  participant get_template as get_template
  participant _get_detailed_template as _get_detailed_template
  participant _get_simple_template as _get_simple_template
  participant get_severity_emoji as get_severity_emoji
  participant get_complexity_emoji as get_complexity_emoji
  participant get_emoji as get_emoji
  get_template->>_get_simple_template: call
  get_template->>_get_detailed_template: call
  get_template->>_get_normal_template: call
  _get_detailed_template->>_get_normal_template: call
  _get_detailed_template->>update: call
  get_emoji->>get: call
  get_severity_emoji->>get: call
  get_severity_emoji->>lower: call
```


## 📈 퍼포먼스 메트릭스

### 📊 핵심 지표

| 🎯 메트릭 | 📊 값 | 🚦 상태 |
|-----------|-------|--------|
| **총 라인 수** | 141 | 🟡 보통 |
| **평균 복잡도** | 2.1 | 🟢 양호 |
| **최대 복잡도** | 5 | 🟢 양호 |
| **함수 밀도** | 77.8% | 🔴 주의 |


### 🎯 품질 점수

```mermaid
pie title 코드 복잡도 분포
    "낮은 복잡도 (1-3)" : 6
    "중간 복잡도 (4-7)" : 1
    "높은 복잡도 (8+)" : 0

```


## 🧩 Chunk 요약

이 파일은 총 **9개의 chunk**로 구성되어 있으며, **1,753개의 토큰**을 포함합니다.

| 🧩 Chunk 타입 | 📊 개수 | ⚡ 평균 복잡도 | 📝 총 토큰 | 📈 비율 |
|---------------|--------|-------------|----------|--------|
| 📋 파일 개요 | 1 | 0.0 | 81 | 4.6% |
| 🏗️ 클래스 | 1 | 0.0 | 238 | 13.6% |
| 🔧 메서드 | 7 | 2.1 | 1,434 | 81.8% |

