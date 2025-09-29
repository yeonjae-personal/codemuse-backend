"""
포맷팅 옵션 모델

사용자가 포맷팅 방식을 커스터마이징할 수 있는 옵션들을 정의합니다.
"""

from enum import Enum
from typing import Any, Dict, Optional

from pydantic import BaseModel, Field


class DetailLevel(str, Enum):
    """상세도 레벨"""

    SIMPLE = "simple"  # 간단: 핵심 정보만
    NORMAL = "normal"  # 일반: 기본 정보 + 요약
    DETAILED = "detailed"  # 상세: 모든 정보 포함


class Language(str, Enum):
    """지원 언어"""

    KOREAN = "ko"  # 한국어 (기본)
    ENGLISH = "en"  # 영어 (향후 확장)


class FormattingOptions(BaseModel):
    """
    포맷팅 옵션 모델

    사용자가 포맷팅 방식을 커스터마이징할 수 있는 옵션들을 정의합니다.
    """

    # 기본 옵션
    include_emojis: bool = Field(default=True, description="이모지 포함 여부")

    language: Language = Field(default=Language.KOREAN, description="출력 언어")

    detail_level: DetailLevel = Field(
        default=DetailLevel.NORMAL, description="상세도 레벨"
    )

    # 고급 옵션
    custom_template: Optional[str] = Field(
        default=None, description="사용자 정의 템플릿 (JSON 형태)"
    )

    max_line_length: int = Field(default=80, ge=40, le=200, description="최대 줄 길이")

    include_timestamps: bool = Field(default=True, description="타임스탬프 포함 여부")

    include_metadata: bool = Field(default=True, description="메타데이터 포함 여부")

    # 스타일 옵션
    bullet_style: str = Field(default="•", description="글머리 기호 스타일")

    separator_style: str = Field(default="─", description="구분선 스타일")

    # 필터링 옵션
    include_issues: bool = Field(default=True, description="이슈 정보 포함 여부")

    include_performance: bool = Field(default=True, description="성능 메트릭 포함 여부")

    include_quality: bool = Field(default=True, description="품질 메트릭 포함 여부")

    # 사용자 정의 설정
    custom_settings: Dict[str, Any] = Field(
        default_factory=dict, description="사용자 정의 설정"
    )

    class Config:
        """Pydantic 설정"""

        use_enum_values = True
        validate_assignment = True

    @classmethod
    def get_available_options(cls) -> Dict[str, Any]:
        """
        사용 가능한 포맷팅 옵션 정보 반환

        Returns:
            옵션 정보 딕셔너리
        """
        return {
            "detail_levels": [level.value for level in DetailLevel],
            "languages": [lang.value for lang in Language],
            "defaults": {
                "include_emojis": True,
                "language": "ko",
                "detail_level": "normal",
                "max_line_length": 80,
                "include_timestamps": True,
                "include_metadata": True,
                "include_issues": True,
                "include_performance": True,
                "include_quality": True,
            },
            "constraints": {
                "max_line_length": {"min": 40, "max": 200},
            },
        }

    def get_template_key(self) -> str:
        """
        현재 옵션에 해당하는 템플릿 키 반환

        Returns:
            템플릿 키 문자열
        """
        return f"{self.language.value}_{self.detail_level.value}"

    def is_simple_level(self) -> bool:
        """간단 레벨인지 확인"""
        return self.detail_level == DetailLevel.SIMPLE

    def is_detailed_level(self) -> bool:
        """상세 레벨인지 확인"""
        return self.detail_level == DetailLevel.DETAILED

    def should_include_field(self, field_name: str) -> bool:
        """
        특정 필드를 포함해야 하는지 확인

        Args:
            field_name: 필드명

        Returns:
            포함 여부
        """
        field_mapping = {
            "issues": self.include_issues,
            "performance_metrics": self.include_performance,
            "quality_metrics": self.include_quality,
            "report_metadata": self.include_metadata,
        }

        return field_mapping.get(field_name, True)
