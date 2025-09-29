"""
설정 관리자

애플리케이션의 설정을 중앙에서 관리하고 접근할 수 있도록 합니다.
"""

import json
import logging
# os 모듈은 현재 사용되지 않지만 향후 확장을 위해 유지
from pathlib import Path
from typing import Any, Dict, Optional


class ConfigManager:
    """
    설정 관리자 클래스

    애플리케이션의 설정을 중앙에서 관리하고 접근할 수 있도록 합니다.
    """

    def __init__(self, config_file: Optional[str] = None):
        """
        설정 관리자 초기화

        Args:
            config_file: 설정 파일 경로 (None이면 기본값 사용)
        """
        self.logger = logging.getLogger(__name__)

        # 설정 파일 경로
        if config_file:
            self.config_file = Path(config_file)
        else:
            # 기본 설정 파일 경로
            default_config_dir = Path.home() / ".raas-rule-analyzer"
            default_config_dir.mkdir(exist_ok=True)
            self.config_file = default_config_dir / "config.json"

        # 기본 설정
        self.default_config = {
            "logging": {
                "level": "INFO",
                "format": "%(asctime)s - %(name)s - %(levelname)s - %(message)s",
                "file": None,
                "max_size": "10MB",
                "backup_count": 5,
            },
            "formatter": {
                "default_language": "ko",
                "default_detail_level": "normal",
                "include_emojis": True,
                "max_line_length": 80,
            },
            "streaming": {
                "default_chunk_delay": 0.1,
                "max_chunk_size": 1000,
                "enable_heartbeat": True,
                "heartbeat_interval": 30.0,
                "max_concurrent_sessions": 10,
            },
            "performance": {
                "enable_caching": True,
                "cache_ttl": 3600,
                "max_memory_usage": "512MB",
                "enable_compression": False,
            },
            "security": {
                "enable_rate_limiting": True,
                "max_requests_per_minute": 100,
                "allowed_origins": ["*"],
                "enable_ssl": False,
            },
        }

        # 현재 설정
        self.config = self.default_config.copy()

        # 설정 파일 로드
        self.load_config()

    def load_config(self) -> bool:
        """
        설정 파일 로드

        Returns:
            로드 성공 여부
        """
        try:
            if self.config_file.exists():
                with open(self.config_file, 'r', encoding='utf-8') as f:
                    file_config = json.load(f)
                    self._merge_config(file_config)
                    self.logger.info(f"설정 파일 로드 완료: {self.config_file}")
                    return True
            else:
                # 설정 파일이 없으면 기본 설정으로 생성
                self.save_config()
                self.logger.info(f"기본 설정 파일 생성: {self.config_file}")
                return True

        except Exception as e:
            self.logger.error(f"설정 파일 로드 실패: {str(e)}")
            return False

    def save_config(self) -> bool:
        """
        설정 파일 저장

        Returns:
            저장 성공 여부
        """
        try:
            # 설정 디렉토리 생성
            self.config_file.parent.mkdir(parents=True, exist_ok=True)

            with open(self.config_file, 'w', encoding='utf-8') as f:
                json.dump(self.config, f, indent=2, ensure_ascii=False)

            self.logger.info(f"설정 파일 저장 완료: {self.config_file}")
            return True

        except Exception as e:
            self.logger.error(f"설정 파일 저장 실패: {str(e)}")
            return False

    def get(self, key: str, default: Any = None) -> Any:
        """
        설정 값 조회

        Args:
            key: 설정 키 (점 표기법 지원: "logging.level")
            default: 기본값

        Returns:
            설정 값 또는 기본값
        """
        try:
            value = self.config
            for k in key.split('.'):
                value = value[k]
            return value
        except (KeyError, TypeError):
            return default

    def set(self, key: str, value: Any) -> bool:
        """
        설정 값 설정

        Args:
            key: 설정 키 (점 표기법 지원: "logging.level")
            value: 설정 값

        Returns:
            설정 성공 여부
        """
        try:
            keys = key.split('.')
            config = self.config

            # 중간 키들 처리
            for k in keys[:-1]:
                if k not in config:
                    config[k] = {}
                config = config[k]

            # 마지막 키에 값 설정
            config[keys[-1]] = value

            self.logger.debug(f"설정 업데이트: {key} = {value}")
            return True

        except Exception as e:
            self.logger.error(f"설정 업데이트 실패: {key} = {value}, 오류: {str(e)}")
            return False

    def update(self, config_dict: Dict[str, Any]) -> bool:
        """
        설정 딕셔너리로 일괄 업데이트

        Args:
            config_dict: 업데이트할 설정 딕셔너리

        Returns:
            업데이트 성공 여부
        """
        try:
            self._merge_config(config_dict)
            self.logger.info("설정 일괄 업데이트 완료")
            return True

        except Exception as e:
            self.logger.error(f"설정 일괄 업데이트 실패: {str(e)}")
            return False

    def reset_to_default(self) -> bool:
        """
        기본 설정으로 초기화

        Returns:
            초기화 성공 여부
        """
        try:
            self.config = self.default_config.copy()
            self.save_config()
            self.logger.info("설정을 기본값으로 초기화 완료")
            return True

        except Exception as e:
            self.logger.error(f"설정 초기화 실패: {str(e)}")
            return False

    def get_all(self) -> Dict[str, Any]:
        """
        모든 설정 반환

        Returns:
            전체 설정 딕셔너리
        """
        return self.config.copy()

    def get_section(self, section: str) -> Dict[str, Any]:
        """
        특정 섹션의 설정 반환

        Args:
            section: 섹션명

        Returns:
            섹션 설정 딕셔너리
        """
        return self.config.get(section, {})

    def has_section(self, section: str) -> bool:
        """
        특정 섹션이 존재하는지 확인

        Args:
            section: 섹션명

        Returns:
            섹션 존재 여부
        """
        return section in self.config

    def has_key(self, key: str) -> bool:
        """
        특정 키가 존재하는지 확인

        Args:
            key: 설정 키 (점 표기법 지원)

        Returns:
            키 존재 여부
        """
        try:
            value = self.config
            for k in key.split('.'):
                value = value[k]
            return True
        except (KeyError, TypeError):
            return False

    def validate_config(self) -> tuple[bool, list[str]]:
        """
        설정 유효성 검증

        Returns:
            (유효성 여부, 에러 메시지 리스트)
        """
        errors = []

        # 필수 섹션 확인
        required_sections = ["logging", "formatter", "streaming"]
        for section in required_sections:
            if not self.has_section(section):
                errors.append(f"필수 섹션 '{section}'가 누락되었습니다.")

        # 로깅 레벨 검증
        logging_level = self.get("logging.level")
        valid_logging_levels = ["DEBUG", "INFO", "WARNING", "ERROR", "CRITICAL"]
        if logging_level not in valid_logging_levels:
            errors.append(f"잘못된 로깅 레벨: {logging_level}")

        # 스트리밍 딜레이 검증
        chunk_delay = self.get("streaming.default_chunk_delay")
        if (
            not isinstance(chunk_delay, (int, float))
            or chunk_delay < 0.01
            or chunk_delay > 2.0
        ):
            errors.append(f"잘못된 청크 딜레이: {chunk_delay}")

        # 최대 청크 크기 검증
        max_chunk_size = self.get("streaming.max_chunk_size")
        if (
            not isinstance(max_chunk_size, int)
            or max_chunk_size < 100
            or max_chunk_size > 10000
        ):
            errors.append(f"잘못된 최대 청크 크기: {max_chunk_size}")

        return len(errors) == 0, errors

    def _merge_config(self, new_config: Dict[str, Any]) -> None:
        """
        설정 병합 (재귀적)

        Args:
            new_config: 병합할 새 설정
        """
        for key, value in new_config.items():
            if (
                key in self.config
                and isinstance(self.config[key], dict)
                and isinstance(value, dict)
            ):
                self._merge_config(value)
            else:
                self.config[key] = value

    def reload(self) -> bool:
        """
        설정 파일 재로드

        Returns:
            재로드 성공 여부
        """
        return self.load_config()

    def export_config(self, export_file: str) -> bool:
        """
        설정을 파일로 내보내기

        Args:
            export_file: 내보낼 파일 경로

        Returns:
            내보내기 성공 여부
        """
        try:
            export_path = Path(export_file)
            export_path.parent.mkdir(parents=True, exist_ok=True)

            with open(export_path, 'w', encoding='utf-8') as f:
                json.dump(self.config, f, indent=2, ensure_ascii=False)

            self.logger.info(f"설정 내보내기 완료: {export_file}")
            return True

        except Exception as e:
            self.logger.error(f"설정 내보내기 실패: {str(e)}")
            return False


# 전역 설정 관리자 인스턴스
_config_manager = ConfigManager()


def get_config(key: str, default: Any = None) -> Any:
    """
    전역 설정 값 조회

    Args:
        key: 설정 키
        default: 기본값

    Returns:
        설정 값 또는 기본값
    """
    return _config_manager.get(key, default)


def set_config(key: str, value: Any) -> bool:
    """
    전역 설정 값 설정

    Args:
        key: 설정 키
        value: 설정 값

    Returns:
        설정 성공 여부
    """
    return _config_manager.set(key, value)
