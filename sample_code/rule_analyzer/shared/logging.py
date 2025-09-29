"""
로깅 설정 관리

애플리케이션의 로깅을 중앙에서 설정하고 관리합니다.
"""

import logging
import logging.handlers
import sys
from pathlib import Path
from typing import Any, Dict, Optional

from .config import get_config


def setup_logging(
    name: str = "raas_rule_analyzer",
    level: Optional[str] = None,
    log_file: Optional[str] = None,
    format_string: Optional[str] = None,
) -> logging.Logger:
    """
    로깅 설정

    Args:
        name: 로거 이름
        level: 로깅 레벨
        log_file: 로그 파일 경로
        format_string: 로그 포맷 문자열

    Returns:
        설정된 로거
    """
    # 설정에서 기본값 가져오기
    if level is None:
        level = get_config("logging.level", "INFO")

    if format_string is None:
        format_string = get_config(
            "logging.format", "%(asctime)s - %(name)s - %(levelname)s - %(message)s"
        )

    if log_file is None:
        log_file = get_config("logging.file")

    # 로깅 레벨 변환
    numeric_level = getattr(logging, level.upper(), logging.INFO)

    # 로거 생성
    logger = logging.getLogger(name)
    logger.setLevel(numeric_level)

    # 기존 핸들러 제거 (중복 방지)
    for handler in logger.handlers[:]:
        logger.removeHandler(handler)

    # 포맷터 생성
    formatter = logging.Formatter(format_string)

    # 콘솔 핸들러
    console_handler = logging.StreamHandler(sys.stdout)
    console_handler.setLevel(numeric_level)
    console_handler.setFormatter(formatter)
    logger.addHandler(console_handler)

    # 파일 핸들러 (지정된 경우)
    if log_file:
        try:
            log_path = Path(log_file)
            log_path.parent.mkdir(parents=True, exist_ok=True)

            # 로테이팅 파일 핸들러
            max_size = get_config("logging.max_size", "10MB")
            backup_count = get_config("logging.backup_count", 5)

            # 크기 제한을 바이트로 변환
            max_bytes = _parse_size_string(max_size)

            file_handler = logging.handlers.RotatingFileHandler(
                log_path, maxBytes=max_bytes, backupCount=backup_count, encoding='utf-8'
            )
            file_handler.setLevel(numeric_level)
            file_handler.setFormatter(formatter)
            logger.addHandler(file_handler)

        except Exception as e:
            logger.warning(f"로그 파일 핸들러 생성 실패: {str(e)}")

    # 로거 설정 완료 로그
    logger.info(f"로깅 설정 완료: 레벨={level}, 파일={log_file or '없음'}")

    return logger


def get_logger(name: str = "raas_rule_analyzer") -> logging.Logger:
    """
    로거 반환

    Args:
        name: 로거 이름

    Returns:
        로거 인스턴스
    """
    return logging.getLogger(name)


def set_log_level(name: str, level: str) -> bool:
    """
    특정 로거의 레벨 설정

    Args:
        name: 로거 이름
        level: 로깅 레벨

    Returns:
        설정 성공 여부
    """
    try:
        numeric_level = getattr(logging, level.upper(), logging.INFO)
        logger = logging.getLogger(name)
        logger.setLevel(numeric_level)

        # 모든 핸들러의 레벨도 업데이트
        for handler in logger.handlers:
            handler.setLevel(numeric_level)

        return True

    except Exception:
        return False


def add_file_handler(
    logger: logging.Logger,
    log_file: str,
    level: Optional[str] = None,
    format_string: Optional[str] = None,
) -> bool:
    """
    로거에 파일 핸들러 추가

    Args:
        logger: 대상 로거
        log_file: 로그 파일 경로
        level: 로깅 레벨
        format_string: 로그 포맷 문자열

    Returns:
        추가 성공 여부
    """
    try:
        if level is None:
            level = get_config("logging.level", "INFO")

        if format_string is None:
            format_string = get_config(
                "logging.format", "%(asctime)s - %(name)s - %(levelname)s - %(message)s"
            )

        # 로그 파일 경로 생성
        log_path = Path(log_file)
        log_path.parent.mkdir(parents=True, exist_ok=True)

        # 포맷터 생성
        formatter = logging.Formatter(format_string)

        # 파일 핸들러 생성
        file_handler = logging.FileHandler(log_path, encoding='utf-8')
        file_handler.setLevel(getattr(logging, level.upper(), logging.INFO))
        file_handler.setFormatter(formatter)

        # 로거에 핸들러 추가
        logger.addHandler(file_handler)

        return True

    except Exception:
        return False


def remove_file_handler(logger: logging.Logger, log_file: str) -> bool:
    """
    로거에서 특정 파일 핸들러 제거

    Args:
        logger: 대상 로거
        log_file: 제거할 로그 파일 경로

    Returns:
        제거 성공 여부
    """
    try:
        handlers_to_remove = []

        for handler in logger.handlers:
            if isinstance(handler, logging.FileHandler) and handler.baseFilename == str(
                Path(log_file).absolute()
            ):
                handlers_to_remove.append(handler)

        for handler in handlers_to_remove:
            logger.removeHandler(handler)
            handler.close()

        return len(handlers_to_remove) > 0

    except Exception:
        return False


def get_logging_config() -> Dict[str, Any]:
    """
    현재 로깅 설정 반환

    Returns:
        로깅 설정 딕셔너리
    """
    return {
        "level": get_config("logging.level", "INFO"),
        "format": get_config(
            "logging.format", "%(asctime)s - %(name)s - %(levelname)s - %(message)s"
        ),
        "file": get_config("logging.file"),
        "max_size": get_config("logging.max_size", "10MB"),
        "backup_count": get_config("logging.backup_count", 5),
    }


def _parse_size_string(size_string: str) -> int:
    """
    크기 문자열을 바이트로 변환

    Args:
        size_string: 크기 문자열 (예: "10MB", "1GB")

    Returns:
        바이트 수
    """
    size_string = size_string.upper().strip()

    if size_string.endswith('KB'):
        return int(float(size_string[:-2]) * 1024)
    elif size_string.endswith('MB'):
        return int(float(size_string[:-2]) * 1024 * 1024)
    elif size_string.endswith('GB'):
        return int(float(size_string[:-2]) * 1024 * 1024 * 1024)
    elif size_string.endswith('B'):
        return int(float(size_string[:-1]))
    else:
        # 숫자만 있는 경우 바이트로 간주
        try:
            return int(size_string)
        except ValueError:
            return 10 * 1024 * 1024  # 기본값: 10MB


# 기본 로거 설정
_default_logger = setup_logging()


def get_default_logger() -> logging.Logger:
    """
    기본 로거 반환

    Returns:
        기본 로거 인스턴스
    """
    return _default_logger
