"""
언어 감지 및 지원 유틸리티

다국어 지원을 위한 언어 감지 및 언어별 처리를 제공합니다.
"""

import logging
import re
from enum import Enum
from typing import Dict, List, Tuple


class Language(Enum):
    """지원 언어"""

    KOREAN = "ko"
    ENGLISH = "en"
    JAPANESE = "ja"
    CHINESE = "zh"
    UNKNOWN = "unknown"


class LanguageDetector:
    """
    언어 감지기

    텍스트의 언어를 자동으로 감지합니다.
    """

    def __init__(self):
        """언어 감지기 초기화"""
        self.logger = logging.getLogger(__name__)

        # 언어별 패턴 정의
        self.language_patterns = {
            Language.KOREAN: {
                "patterns": [
                    r'[가-힣]',  # 한글
                    r'[ㄱ-ㅎㅏ-ㅣ]',  # 한글 자모
                ],
                "threshold": 0.1,  # 10% 이상 한글이 있으면 한국어로 판단
            },
            Language.JAPANESE: {
                "patterns": [
                    r'[あ-んア-ン]',  # 히라가나, 가타카나
                    r'[一-龯]',  # 한자 (일본어에서 사용)
                ],
                "threshold": 0.1,
            },
            Language.CHINESE: {
                "patterns": [
                    r'[一-龯]',  # 한자
                ],
                "threshold": 0.3,  # 30% 이상 한자가 있으면 중국어로 판단
            },
            Language.ENGLISH: {
                "patterns": [
                    r'[a-zA-Z]',  # 영문
                ],
                "threshold": 0.5,  # 50% 이상 영문이 있으면 영어로 판단
            },
        }

    def detect_language(self, text: str) -> Language:
        """
        텍스트의 언어 감지

        Args:
            text: 감지할 텍스트

        Returns:
            감지된 언어
        """
        if not text or not text.strip():
            return Language.UNKNOWN

        # 텍스트 정규화
        normalized_text = text.strip()
        text_length = len(normalized_text)

        if text_length == 0:
            return Language.UNKNOWN

        # 각 언어별 점수 계산
        language_scores = {}

        for lang, config in self.language_patterns.items():
            score = 0
            total_matches = 0

            for pattern in config["patterns"]:
                matches = re.findall(pattern, normalized_text)
                total_matches += len(matches)

            # 점수 계산 (매칭된 문자 수 / 전체 문자 수)
            score = total_matches / text_length
            language_scores[lang] = score

        # 가장 높은 점수의 언어 선택
        best_language = max(language_scores.items(), key=lambda x: x[1])
        best_lang, best_score = best_language

        # 임계값 확인
        threshold = self.language_patterns[best_lang]["threshold"]

        if best_score >= threshold:
            self.logger.debug(f"언어 감지: {best_lang.value} (점수: {best_score:.3f})")
            return best_lang
        else:
            self.logger.debug(
                f"언어 감지 실패: 최고 점수 {best_score:.3f} < 임계값 {threshold}"
            )
            return Language.UNKNOWN

    def detect_language_with_confidence(self, text: str) -> Tuple[Language, float]:
        """
        텍스트의 언어를 신뢰도와 함께 감지

        Args:
            text: 감지할 텍스트

        Returns:
            (감지된 언어, 신뢰도)
        """
        if not text or not text.strip():
            return Language.UNKNOWN, 0.0

        normalized_text = text.strip()
        text_length = len(normalized_text)

        if text_length == 0:
            return Language.UNKNOWN, 0.0

        # 각 언어별 점수 계산
        language_scores = {}

        for lang, config in self.language_patterns.items():
            score = 0
            total_matches = 0

            for pattern in config["patterns"]:
                matches = re.findall(pattern, normalized_text)
                total_matches += len(matches)

            score = total_matches / text_length
            language_scores[lang] = score

        # 가장 높은 점수의 언어 선택
        best_language = max(language_scores.items(), key=lambda x: x[1])
        best_lang, best_score = best_language

        # 임계값 확인
        threshold = self.language_patterns[best_lang]["threshold"]

        if best_score >= threshold:
            # 신뢰도 계산 (임계값 대비 초과 비율)
            confidence = min(1.0, best_score / threshold)
            return best_lang, confidence
        else:
            return Language.UNKNOWN, 0.0

    def is_likely_language(self, text: str, language: Language) -> bool:
        """
        텍스트가 특정 언어일 가능성이 높은지 확인

        Args:
            text: 확인할 텍스트
            language: 확인할 언어

        Returns:
            해당 언어일 가능성 여부
        """
        detected_lang = self.detect_language(text)
        return detected_lang == language

    def get_language_statistics(self, text: str) -> Dict[Language, float]:
        """
        텍스트의 언어별 통계 반환

        Args:
            text: 분석할 텍스트

        Returns:
            언어별 점수 딕셔너리
        """
        if not text or not text.strip():
            return {lang: 0.0 for lang in Language}

        normalized_text = text.strip()
        text_length = len(normalized_text)

        if text_length == 0:
            return {lang: 0.0 for lang in Language}

        # 각 언어별 점수 계산
        language_scores = {}

        for lang, config in self.language_patterns.items():
            score = 0
            total_matches = 0

            for pattern in config["patterns"]:
                matches = re.findall(pattern, normalized_text)
                total_matches += len(matches)

            score = total_matches / text_length
            language_scores[lang] = score

        # UNKNOWN 언어는 0점
        language_scores[Language.UNKNOWN] = 0.0

        return language_scores


class LanguageSupport:
    """
    언어별 지원 기능

    다양한 언어에 대한 공통 기능을 제공합니다.
    """

    # 언어별 기본 설정
    LANGUAGE_DEFAULTS = {
        Language.KOREAN: {
            "name": "한국어",
            "native_name": "한국어",
            "direction": "ltr",  # left-to-right
            "date_format": "%Y년 %m월 %d일",
            "time_format": "%H시 %M분",
            "number_format": "comma",  # 천 단위 쉼표
            "currency_symbol": "₩",
            "decimal_separator": ".",
            "thousands_separator": ",",
        },
        Language.ENGLISH: {
            "name": "English",
            "native_name": "English",
            "direction": "ltr",
            "date_format": "%B %d, %Y",
            "time_format": "%I:%M %p",
            "number_format": "comma",
            "currency_symbol": "$",
            "decimal_separator": ".",
            "thousands_separator": ",",
        },
        Language.JAPANESE: {
            "name": "Japanese",
            "native_name": "日本語",
            "direction": "ltr",
            "date_format": "%Y年%m月%d日",
            "time_format": "%H時%M分",
            "number_format": "comma",
            "currency_symbol": "¥",
            "decimal_separator": ".",
            "thousands_separator": ",",
        },
        Language.CHINESE: {
            "name": "Chinese",
            "native_name": "中文",
            "direction": "ltr",
            "date_format": "%Y年%m月%d日",
            "time_format": "%H时%M分",
            "number_format": "comma",
            "currency_symbol": "¥",
            "decimal_separator": ".",
            "thousands_separator": ",",
        },
    }

    @classmethod
    def get_language_info(cls, language: Language) -> Dict[str, str]:
        """
        언어 정보 반환

        Args:
            language: 언어

        Returns:
            언어 정보 딕셔너리
        """
        return cls.LANGUAGE_DEFAULTS.get(language, {})

    @classmethod
    def get_supported_languages(cls) -> List[Language]:
        """
        지원하는 언어 목록 반환

        Returns:
            지원 언어 리스트
        """
        return list(cls.LANGUAGE_DEFAULTS.keys())

    @classmethod
    def get_language_name(cls, language: Language, native: bool = False) -> str:
        """
        언어 이름 반환

        Args:
            language: 언어
            native: 원어 이름 여부

        Returns:
            언어 이름
        """
        info = cls.LANGUAGE_DEFAULTS.get(language, {})

        if native:
            return info.get("native_name", language.value)
        else:
            return info.get("name", language.value)

    @classmethod
    def format_number(cls, number: float, language: Language) -> str:
        """
        언어별 숫자 포맷팅

        Args:
            number: 포맷팅할 숫자
            language: 언어

        Returns:
            포맷팅된 숫자 문자열
        """
        info = cls.LANGUAGE_DEFAULTS.get(language, {})

        if info.get("number_format") == "comma":
            # 천 단위 쉼표 추가
            return f"{number:,.2f}"
        else:
            return f"{number:.2f}"

    @classmethod
    def get_currency_symbol(cls, language: Language) -> str:
        """
        언어별 통화 기호 반환

        Args:
            language: 언어

        Returns:
            통화 기호
        """
        info = cls.LANGUAGE_DEFAULTS.get(language, {})
        return info.get("currency_symbol", "$")

    @classmethod
    def is_rtl_language(cls, language: Language) -> bool:
        """
        오른쪽에서 왼쪽으로 쓰는 언어인지 확인

        Args:
            language: 언어

        Returns:
            RTL 언어 여부
        """
        info = cls.LANGUAGE_DEFAULTS.get(language, {})
        return info.get("direction") == "rtl"

    @classmethod
    def get_text_direction(cls, language: Language) -> str:
        """
        텍스트 방향 반환

        Args:
            language: 언어

        Returns:
            텍스트 방향 ("ltr" 또는 "rtl")
        """
        info = cls.LANGUAGE_DEFAULTS.get(language, {})
        return info.get("direction", "ltr")
