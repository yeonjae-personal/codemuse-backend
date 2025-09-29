"""
RaaS Rule Analyzer - Shared Package

공통 설정, 유틸리티, 로깅 등 공통 컴포넌트를 제공합니다.
"""

from .config import ConfigManager, get_config, set_config
from .logging import get_logger, setup_logging
from .utils.constants import (
    VERSION,
    VERSION_INFO,
    APP_NAME,
    APP_DESCRIPTION,
    APP_AUTHOR,
    DEFAULT_CONFIG_DIR,
    DEFAULT_CONFIG_FILE,
    DEFAULT_LOG_LEVEL,
    DEFAULT_LOG_FORMAT,
    SUPPORTED_LANGUAGES,
    DETAIL_LEVELS,
    BULLET_STYLES,
    SEPARATOR_STYLES,
    MIN_CHUNK_DELAY,
    MAX_CHUNK_DELAY,
    MIN_CHUNK_SIZE,
    MAX_CHUNK_SIZE,
    DEFAULT_CACHE_TTL,
    DEFAULT_RATE_LIMIT,
    DEFAULT_TIMEOUT,
    ERROR_CODES,
    HTTP_STATUS_CODES,
)
from .utils.language import LanguageDetector, LanguageSupport
from .utils.validation import ValidationHelper


__all__ = [
    "get_config",
    "set_config",
    "ConfigManager",
    "setup_logging",
    "get_logger",
    "LanguageDetector",
    "LanguageSupport",
    "ValidationHelper",
    # Constants
    "VERSION",
    "VERSION_INFO",
    "APP_NAME",
    "APP_DESCRIPTION",
    "APP_AUTHOR",
    "DEFAULT_CONFIG_DIR",
    "DEFAULT_CONFIG_FILE",
    "DEFAULT_LOG_LEVEL",
    "DEFAULT_LOG_FORMAT",
    "SUPPORTED_LANGUAGES",
    "DETAIL_LEVELS",
    "BULLET_STYLES",
    "SEPARATOR_STYLES",
    "MIN_CHUNK_DELAY",
    "MAX_CHUNK_DELAY",
    "MIN_CHUNK_SIZE",
    "MAX_CHUNK_SIZE",
    "DEFAULT_CACHE_TTL",
    "DEFAULT_RATE_LIMIT",
    "DEFAULT_TIMEOUT",
    "ERROR_CODES",
    "HTTP_STATUS_CODES",
]
