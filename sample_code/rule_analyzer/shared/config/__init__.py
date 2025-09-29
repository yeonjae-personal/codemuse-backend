"""
RaaS Rule Analyzer - Shared Config Package

공통 설정 관리 기능을 제공합니다.
"""

from .config_manager import ConfigManager, get_config, set_config


__all__ = [
    "ConfigManager",
    "get_config",
    "set_config",
]
