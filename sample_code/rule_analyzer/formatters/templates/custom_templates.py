"""
사용자 정의 템플릿 관리

사용자가 커스터마이징한 포맷팅 템플릿을 관리하고 적용합니다.
"""

import json
import logging
from typing import Any, Dict, List, Optional

from ..options.formatting_options import DetailLevel, Language


class CustomTemplateManager:
    """
    사용자 정의 템플릿 관리 클래스

    JSON 형태의 사용자 정의 템플릿을 파싱하고 적용합니다.
    """

    def __init__(self):
        """템플릿 관리자 초기화"""
        self.logger = logging.getLogger(__name__)
        self.custom_templates: Dict[str, Dict[str, str]] = {}
        self.template_cache: Dict[str, Dict[str, str]] = {}

    def load_custom_template(
        self, template_json: str, template_name: str = "default"
    ) -> bool:
        """
        사용자 정의 템플릿 로드

        Args:
            template_json: JSON 형태의 템플릿 문자열
            template_name: 템플릿 이름

        Returns:
            로드 성공 여부
        """
        try:
            template_data = json.loads(template_json)

            # 템플릿 구조 검증
            if not self._validate_template_structure(template_data):
                return False

            # 템플릿 저장
            self.custom_templates[template_name] = template_data
            self.logger.info(f"사용자 정의 템플릿 '{template_name}' 로드 완료")
            return True

        except json.JSONDecodeError as e:
            self.logger.error(f"사용자 정의 템플릿 JSON 파싱 실패: {str(e)}")
            return False
        except Exception as e:
            self.logger.error(f"사용자 정의 템플릿 로드 실패: {str(e)}")
            return False

    def get_custom_template(
        self,
        template_name: str = "default",
        detail_level: DetailLevel = DetailLevel.NORMAL,
        language: Language = Language.KOREAN,
    ) -> Optional[Dict[str, str]]:
        """
        사용자 정의 템플릿 반환

        Args:
            template_name: 템플릿 이름
            detail_level: 상세도 레벨
            language: 언어

        Returns:
            템플릿 딕셔너리 또는 None
        """
        cache_key = f"{template_name}_{detail_level.value}_{language.value}"

        # 캐시에서 확인
        if cache_key in self.template_cache:
            return self.template_cache[cache_key]

        # 템플릿이 존재하는지 확인
        if template_name not in self.custom_templates:
            return None

        # 템플릿 적용
        template = self._apply_template_variations(
            self.custom_templates[template_name], detail_level, language
        )

        # 캐시에 저장
        self.template_cache[cache_key] = template
        return template

    def list_custom_templates(self) -> List[str]:
        """
        사용 가능한 사용자 정의 템플릿 목록 반환

        Returns:
            템플릿 이름 리스트
        """
        return list(self.custom_templates.keys())

    def remove_custom_template(self, template_name: str) -> bool:
        """
        사용자 정의 템플릿 제거

        Args:
            template_name: 제거할 템플릿 이름

        Returns:
            제거 성공 여부
        """
        if template_name in self.custom_templates:
            del self.custom_templates[template_name]

            # 관련 캐시도 제거
            cache_keys_to_remove = [
                key
                for key in self.template_cache.keys()
                if key.startswith(template_name)
            ]
            for key in cache_keys_to_remove:
                del self.template_cache[key]

            self.logger.info(f"사용자 정의 템플릿 '{template_name}' 제거 완료")
            return True

        return False

    def clear_custom_templates(self) -> None:
        """모든 사용자 정의 템플릿 제거"""
        self.custom_templates.clear()
        self.template_cache.clear()
        self.logger.info("모든 사용자 정의 템플릿 제거 완료")

    def _validate_template_structure(self, template_data: Dict[str, Any]) -> bool:
        """
        템플릿 구조 유효성 검증

        Args:
            template_data: 검증할 템플릿 데이터

        Returns:
            유효성 여부
        """
        # 필수 키 확인
        required_keys = ["header", "validity", "summary"]
        for key in required_keys:
            if key not in template_data:
                self.logger.error(f"필수 템플릿 키 '{key}'가 누락되었습니다.")
                return False

        # 값 타입 확인
        for key, value in template_data.items():
            if not isinstance(value, str):
                self.logger.error(
                    f"템플릿 값 '{key}'가 문자열이 아닙니다: {type(value)}"
                )
                return False

        # 플레이스홀더 검증
        for key, value in template_data.items():
            if not self._validate_placeholders(value):
                self.logger.error(
                    f"템플릿 '{key}'에 잘못된 플레이스홀더가 포함되어 있습니다: {value}"
                )
                return False

        return True

    def _validate_placeholders(self, template_string: str) -> bool:
        """
        플레이스홀더 유효성 검증

        Args:
            template_string: 검증할 템플릿 문자열

        Returns:
            유효성 여부
        """
        # 허용된 플레이스홀더 목록
        allowed_placeholders = {
            "emoji",
            "result",
            "score",
            "count",
            "text",
            "depth",
            "fields",
            "index",
            "type",
            "explanation",
            "time",
            "version",
            "model",
            "generator",
            "operators",
            "levels",
            "coverage",
            "memory",
            "rating",
            "suggestions",
            "maintainability",
            "readability",
            "completeness",
            "consistency",
            "overall",
        }

        # 중괄호로 감싸진 플레이스홀더 찾기
        import re

        placeholders = re.findall(r'\{(\w+)\}', template_string)

        # 모든 플레이스홀더가 허용된 것인지 확인
        for placeholder in placeholders:
            if placeholder not in allowed_placeholders:
                return False

        return True

    def _apply_template_variations(
        self,
        base_template: Dict[str, str],
        detail_level: DetailLevel,
        language: Language,
    ) -> Dict[str, str]:
        """
        템플릿에 상세도와 언어별 변형 적용

        Args:
            base_template: 기본 템플릿
            detail_level: 상세도 레벨
            language: 언어

        Returns:
            변형이 적용된 템플릿
        """
        template = base_template.copy()

        # 상세도에 따른 필드 필터링
        if detail_level == DetailLevel.SIMPLE:
            template = self._filter_simple_fields(template)
        elif detail_level == DetailLevel.DETAILED:
            template = self._add_detailed_fields(template)

        # 언어별 변형 적용
        if language == Language.ENGLISH:
            template = self._translate_to_english(template)

        return template

    def _filter_simple_fields(self, template: Dict[str, str]) -> Dict[str, str]:
        """
        간단 레벨에 맞게 필드 필터링

        Args:
            template: 원본 템플릿

        Returns:
            필터링된 템플릿
        """
        simple_keys = ["header", "validity", "complexity", "issue_count", "summary"]
        return {key: template[key] for key in simple_keys if key in template}

    def _add_detailed_fields(self, template: Dict[str, str]) -> Dict[str, str]:
        """
        상세 레벨에 맞게 필드 추가

        Args:
            template: 원본 템플릿

        Returns:
            확장된 템플릿
        """
        # 기본 필드가 없으면 추가
        detailed_fields = {
            "field_analysis_header": "{emoji} 필드별 상세 분석",
            "logic_flow_header": "{emoji} 로직 플로우 분석",
            "metadata_header": "{emoji} 메타데이터",
        }

        for key, value in detailed_fields.items():
            if key not in template:
                template[key] = value

        return template

    def _translate_to_english(self, template: Dict[str, str]) -> Dict[str, str]:
        """
        영어로 번역 (기본적인 번역만 지원)

        Args:
            template: 한국어 템플릿

        Returns:
            영어 템플릿
        """
        # 간단한 번역 매핑
        translations = {
            "룰 분석 결과": "Rule Analysis Result",
            "검증 결과": "Validation Result",
            "요약 정보": "Summary Information",
            "복잡도 점수": "Complexity Score",
            "발견된 문제": "Issues Found",
            "구조 분석": "Structure Analysis",
            "문제점 상세": "Issue Details",
            "성능 메트릭": "Performance Metrics",
            "품질 메트릭": "Quality Metrics",
            "분석 시간": "Analysis Time",
        }

        translated = {}
        for key, value in template.items():
            translated_value = value
            for korean, english in translations.items():
                translated_value = translated_value.replace(korean, english)
            translated[key] = translated_value

        return translated

    def get_template_info(self, template_name: str) -> Optional[Dict[str, Any]]:
        """
        템플릿 정보 반환

        Args:
            template_name: 템플릿 이름

        Returns:
            템플릿 정보 딕셔너리 또는 None
        """
        if template_name not in self.custom_templates:
            return None

        template = self.custom_templates[template_name]

        return {
            "name": template_name,
            "field_count": len(template),
            "fields": list(template.keys()),
            "has_placeholders": any("{" in value for value in template.values()),
            "estimated_size": len(json.dumps(template, ensure_ascii=False)),
        }
