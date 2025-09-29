"""
텍스트 포맷터

분석 결과를 읽기 쉬운 텍스트로 변환합니다.
"""

import logging
from typing import Dict, Optional

from .options.formatting_options import DetailLevel, FormattingOptions, Language
from .templates.custom_templates import CustomTemplateManager
from .templates.english_templates import EnglishTemplates
from .templates.korean_templates import KoreanTemplates


class TextFormatter:
    """
    텍스트 포맷터

    ValidationResult를 읽기 쉬운 텍스트로 변환합니다.
    """

    def __init__(self, options: Optional[FormattingOptions] = None):
        """
        텍스트 포맷터 초기화

        Args:
            options: 포맷팅 옵션
        """
        self.logger = logging.getLogger(__name__)
        self.options = options or FormattingOptions()

        # 템플릿 매니저들
        self.korean_templates = KoreanTemplates()
        self.english_templates = EnglishTemplates()
        self.custom_templates = CustomTemplateManager()

    def format(self, validation_result) -> str:
        """
        분석 결과를 텍스트로 포맷팅

        Args:
            validation_result: ValidationResult 객체

        Returns:
            포맷팅된 텍스트
        """
        try:
            # 언어별 템플릿 선택
            if self.options.language == Language.KOREAN:
                templates = self.korean_templates
            elif self.options.language == Language.ENGLISH:
                templates = self.english_templates
            else:
                templates = self.korean_templates  # 기본값

            # 상세도별 템플릿 선택
            if self.options.detail_level == DetailLevel.SIMPLE:
                template = templates._get_simple_template()
            elif self.options.detail_level == DetailLevel.DETAILED:
                template = templates._get_detailed_template()
            else:
                template = templates._get_normal_template()

            # 커스텀 템플릿이 있는 경우 사용
            if self.options.custom_template:
                custom_template = self.custom_templates.get_template(
                    self.options.custom_template
                )
                if custom_template:
                    template = custom_template

            # 템플릿으로 포맷팅
            formatted_text = self._apply_template(template, validation_result)

            self.logger.debug(f"텍스트 포맷팅 완료: {len(formatted_text)} 문자")
            return formatted_text

        except Exception as e:
            self.logger.error(f"텍스트 포맷팅 오류: {str(e)}")
            return f"포맷팅 오류: {str(e)}"

    def _apply_template(self, template: Dict[str, str], validation_result) -> str:
        """
        템플릿을 적용하여 텍스트 생성

        Args:
            template: 템플릿 딕셔너리
            validation_result: ValidationResult 객체

        Returns:
            포맷팅된 텍스트
        """
        try:
            # 기본 텍스트 생성
            result_lines = []

            # 헤더
            if "header" in template:
                header = template["header"].format(emoji="📊")
                result_lines.append(header)
                result_lines.append("=" * 50)

            # 유효성
            if "validity" in template:
                validity_emoji = "✅" if validation_result.is_valid else "❌"
                validity_text = (
                    "유효함" if validation_result.is_valid else "유효하지 않음"
                )
                validity = template["validity"].format(
                    emoji=validity_emoji, result=validity_text
                )
                result_lines.append(validity)

            # 요약
            if "summary" in template:
                summary = template["summary"].format(emoji="📝")
                result_lines.append(summary)
                if hasattr(validation_result, 'summary') and validation_result.summary:
                    result_lines.append(f"  {validation_result.summary}")

            # 복잡도
            if "complexity" in template:
                complexity = template["complexity"].format(
                    emoji="📈", score=validation_result.complexity_score
                )
                result_lines.append(complexity)

            # 이슈 개수
            if "issue_count" in template:
                issue_count = template["issue_count"].format(
                    emoji="🚨", count=validation_result.get_total_issues()
                )
                result_lines.append(issue_count)

            # 구조 정보 (있는 경우)
            if hasattr(validation_result, 'structure') and validation_result.structure:
                if "structure" in template:
                    structure = template["structure"].format(emoji="🏗️")
                    result_lines.append(structure)

                structure_obj = validation_result.structure
                if "structure_depth" in template:
                    depth = template["structure_depth"].format(
                        emoji="•", depth=getattr(structure_obj, 'depth', 'N/A')
                    )
                    result_lines.append(depth)

                if "structure_conditions" in template:
                    conditions = template["structure_conditions"].format(
                        emoji="•",
                        count=getattr(structure_obj, 'condition_count', 'N/A'),
                    )
                    result_lines.append(conditions)

            # 이슈 상세 (있는 경우)
            if hasattr(validation_result, 'issues') and validation_result.issues:
                if "issues_header" in template:
                    issues_header = template["issues_header"].format(emoji="🔍")
                    result_lines.append(issues_header)

                for i, issue in enumerate(validation_result.issues):
                    if "issue_item" in template:
                        issue_text = template["issue_item"].format(
                            emoji="⚠️",
                            index=i + 1,
                            type=getattr(issue, 'issue_type', 'unknown'),
                            explanation=getattr(issue, 'explanation', '설명 없음'),
                        )
                        result_lines.append(issue_text)

            # 타임스탬프
            if "timestamp" in template:
                timestamp = getattr(
                    validation_result.report_metadata, 'analysis_timestamp', 'N/A'
                )
                timestamp_text = template["timestamp"].format(
                    emoji="🕒", time=timestamp
                )
                result_lines.append(timestamp_text)

            # 이모지 처리
            if self.options.include_emojis:
                result_lines = self._add_emojis_to_lines(result_lines)
            else:
                # 이모지 제거
                result_lines = self._remove_emojis_from_lines(result_lines)

            return "\n".join(result_lines)

        except Exception as e:
            self.logger.error(f"템플릿 적용 오류: {str(e)}")
            return f"템플릿 적용 오류: {str(e)}"

    def _add_emojis_to_lines(self, lines: list) -> list:
        """
        라인들에 이모지 추가

        Args:
            lines: 원본 라인들

        Returns:
            이모지가 추가된 라인들
        """
        emoji_map = {
            "✅": "성공",
            "❌": "실패",
            "⚠️": "경고",
            "ℹ️": "정보",
            "🔍": "분석",
            "📊": "통계",
            "⚡": "성능",
            "🎯": "목표",
        }

        result_lines = []
        for line in lines:
            for emoji, keyword in emoji_map.items():
                if keyword in line:
                    line = line.replace(keyword, f"{emoji} {keyword}")
            result_lines.append(line)

        return result_lines

    def _remove_emojis_from_lines(self, lines: list) -> list:
        """
        라인들에서 이모지 제거

        Args:
            lines: 원본 라인들

        Returns:
            이모지가 제거된 라인들
        """
        import re

        result_lines = []
        for line in lines:
            # 이모지 패턴 제거 (유니코드 이모지 범위)
            cleaned_line = re.sub(
                r'[\U0001F600-\U0001F64F\U0001F300-\U0001F5FF\U0001F680-\U0001F6FF\U0001F1E0-\U0001F1FF\U00002600-\U000027BF]',
                '',
                line,
            )
            # 공백 정리
            cleaned_line = re.sub(r'\s+', ' ', cleaned_line).strip()
            if cleaned_line:
                result_lines.append(cleaned_line)

        return result_lines

    def format_summary(self, validation_result) -> str:
        """
        분석 결과 요약 포맷팅

        Args:
            validation_result: ValidationResult 객체

        Returns:
            요약 텍스트
        """
        try:
            summary = validation_result.to_summary()

            if self.options.language == Language.KOREAN:
                return f"""
📊 분석 결과 요약
================
✅ 유효성: {'유효함' if summary['is_valid'] else '유효하지 않음'}
📝 요약: {summary['summary']}
🚨 총 이슈: {summary['total_issues']}개
⚠️ 치명적 이슈: {summary['critical_issues']}개
📈 복잡도 점수: {summary['complexity_score']}/100
🏆 전체 점수: {summary['overall_score']}/100
📊 품질 등급: {summary['quality_grade']}
⏱️ 분석 시간: {summary['analysis_timestamp']}
"""
            else:
                return f"""
📊 Analysis Summary
==================
✅ Validity: {'Valid' if summary['is_valid'] else 'Invalid'}
📝 Summary: {summary['summary']}
🚨 Total Issues: {summary['total_issues']}
⚠️ Critical Issues: {summary['critical_issues']}
📈 Complexity Score: {summary['complexity_score']}/100
🏆 Overall Score: {summary['overall_score']}/100
📊 Quality Grade: {summary['quality_grade']}
⏱️ Analysis Time: {summary['analysis_timestamp']}
"""

        except Exception as e:
            self.logger.error(f"요약 포맷팅 오류: {str(e)}")
            return f"요약 포맷팅 오류: {str(e)}"
