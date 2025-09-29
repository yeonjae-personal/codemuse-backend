"""
한국어 포맷팅 템플릿

한국어로 분석 결과를 포맷팅하기 위한 템플릿들을 정의합니다.
"""

from typing import Dict

from ..options.formatting_options import DetailLevel


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
    def get_template(cls, detail_level: DetailLevel) -> Dict[str, str]:
        """
        상세도 레벨에 따른 템플릿 반환

        Args:
            detail_level: 상세도 레벨

        Returns:
            템플릿 딕셔너리
        """
        if detail_level == DetailLevel.SIMPLE:
            return cls._get_simple_template()
        elif detail_level == DetailLevel.DETAILED:
            return cls._get_detailed_template()
        else:
            return cls._get_normal_template()

    @classmethod
    def _get_simple_template(cls) -> Dict[str, str]:
        """간단 레벨 템플릿"""
        return {
            "header": "{emoji} 룰 분석 결과",
            "validity": "{emoji} 검증 결과: {result}",
            "complexity": "복잡도: {score}/100",
            "issue_count": "문제점: {count}개",
            "summary": "{emoji} 요약",
        }

    @classmethod
    def _get_normal_template(cls) -> Dict[str, str]:
        """일반 레벨 템플릿"""
        return {
            "header": "{emoji} 룰 분석 결과",
            "separator": "{emoji}",
            "validity": "{emoji} 룰 검증 결과: {result}",
            "summary": "{emoji} 요약 정보",
            "summary_text": "{text}",
            "complexity": "{emoji} 복잡도 점수: {score}/100",
            "issue_count": "{emoji} 발견된 문제: {count}개",
            "structure": "{emoji} 구조 분석",
            "structure_depth": "  • 최대 깊이: {depth}",
            "structure_conditions": "  • 조건 수: {count}",
            "structure_fields": "  • 고유 필드: {fields}",
            "issues_header": "{emoji} 문제점 상세",
            "issue_item": "  {emoji} 문제 {index}: {type} - {explanation}",
            "performance_header": "{emoji} 성능 메트릭",
            "quality_header": "{emoji} 품질 메트릭",
            "timestamp": "{emoji} 분석 시간: {time}",
        }

    @classmethod
    def _get_detailed_template(cls) -> Dict[str, str]:
        """상세 레벨 템플릿"""
        base = cls._get_normal_template()
        detailed = {
            "field_analysis_header": "{emoji} 필드별 상세 분석",
            "field_item": "  {emoji} {field}: {type} 타입, {conditions}개 조건, {issues}개 이슈",
            "logic_flow_header": "{emoji} 로직 플로우 분석",
            "logic_operators": "  • 논리 연산자: {operators}",
            "nesting_levels": "  • 중첩 레벨: {levels}",
            "branch_coverage": "  • 분기 커버리지: {coverage}",
            "performance_details": "  • 예상 실행 시간: {time}ms",
            "performance_memory": "  • 예상 메모리 사용량: {memory}KB",
            "performance_rating": "  • 복잡도 등급: {rating}",
            "performance_suggestions": "  • 최적화 제안: {suggestions}",
            "quality_details": "  • 유지보수성: {maintainability}/100",
            "quality_readability": "  • 가독성: {readability}/100",
            "quality_completeness": "  • 완성도: {completeness}/100",
            "quality_consistency": "  • 일관성: {consistency}/100",
            "quality_overall": "  • 전체 품질: {overall}/100",
            "metadata_header": "{emoji} 메타데이터",
            "metadata_version": "  • 분석 버전: {version}",
            "metadata_model": "  • 검증 모델: {model}",
            "metadata_generator": "  • 생성자: {generator}",
            "metadata_time": "  • 생성 시간: {time}",
        }
        base.update(detailed)
        return base

    @classmethod
    def get_emoji(cls, key: str, include_emojis: bool = True) -> str:
        """
        이모지 반환

        Args:
            key: 이모지 키
            include_emojis: 이모지 포함 여부

        Returns:
            이모지 문자열
        """
        if not include_emojis:
            return ""

        return cls.EMOJIS.get(key, "")

    @classmethod
    def get_severity_emoji(cls, severity: str, include_emojis: bool = True) -> str:
        """
        심각도에 따른 이모지 반환

        Args:
            severity: 심각도
            include_emojis: 이모지 포함 여부

        Returns:
            이모지 문자열
        """
        if not include_emojis:
            return ""

        severity_emojis = {
            "error": cls.EMOJIS["error"],
            "warning": cls.EMOJIS["warning"],
            "info": cls.EMOJIS["info"],
        }

        return severity_emojis.get(severity.lower(), "")

    @classmethod
    def get_complexity_emoji(cls, score: int, include_emojis: bool = True) -> str:
        """
        복잡도 점수에 따른 이모지 반환

        Args:
            score: 복잡도 점수
            include_emojis: 이모지 포함 여부

        Returns:
            이모지 문자열
        """
        if not include_emojis:
            return ""

        if score < 20:
            return "🟢"  # 낮음
        elif score < 50:
            return "🟡"  # 보통
        elif score < 80:
            return "🟠"  # 높음
        else:
            return "🔴"  # 매우 높음
