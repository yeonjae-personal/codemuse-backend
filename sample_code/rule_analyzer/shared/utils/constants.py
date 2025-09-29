"""
공통 상수 정의

애플리케이션에서 사용하는 공통 상수들을 정의합니다.
"""

# 버전 정보
VERSION = "1.0.0"
VERSION_INFO = (1, 0, 0)

# 애플리케이션 정보
APP_NAME = "RaaS Rule Analyzer"
APP_DESCRIPTION = "AI 기반 룰 분석 및 검증 시스템"
APP_AUTHOR = "RaaS Team"

# 기본 설정값
DEFAULT_CONFIG_DIR = ".raas-rule-analyzer"
DEFAULT_CONFIG_FILE = "config.json"
DEFAULT_LOG_LEVEL = "INFO"
DEFAULT_LOG_FORMAT = "%(asctime)s - %(name)s - %(levelname)s - %(message)s"

# 로깅 관련
LOG_LEVELS = ["DEBUG", "INFO", "WARNING", "ERROR", "CRITICAL"]
LOG_SIZE_LIMITS = ["1MB", "5MB", "10MB", "50MB", "100MB"]
LOG_BACKUP_COUNTS = [1, 3, 5, 10, 20]

# 포맷터 관련
SUPPORTED_LANGUAGES = ["ko", "en", "ja", "zh"]
DETAIL_LEVELS = ["simple", "normal", "detailed"]
BULLET_STYLES = ["•", "-", "*", "→", "▶"]
SEPARATOR_STYLES = ["---", "===", "***", "###", "___"]

# 스트리밍 관련
MIN_CHUNK_DELAY = 0.01
MAX_CHUNK_DELAY = 2.0
MIN_CHUNK_SIZE = 100
MAX_CHUNK_SIZE = 10000
MIN_HEARTBEAT_INTERVAL = 5.0
MAX_HEARTBEAT_INTERVAL = 300.0
MIN_RETRY_COUNT = 0
MAX_RETRY_COUNT = 10
MIN_BUFFER_SIZE = 10
MAX_BUFFER_SIZE = 1000

# 성능 관련
DEFAULT_CACHE_TTL = 3600  # 1시간
MAX_MEMORY_USAGE = "512MB"
ENABLE_COMPRESSION = False

# 보안 관련
DEFAULT_RATE_LIMIT = 100  # 분당 요청 수
DEFAULT_ALLOWED_ORIGINS = ["*"]
ENABLE_SSL = False

# 파일 관련
MAX_FILE_SIZE = "100MB"
SUPPORTED_FILE_TYPES = [".json", ".yaml", ".yml", ".xml", ".txt"]
TEMP_DIR = "temp"
BACKUP_DIR = "backup"

# 에러 코드
ERROR_CODES = {
    "VALIDATION_ERROR": "VALIDATION_ERROR",
    "STREAMING_ERROR": "STREAMING_ERROR",
    "FORMATTING_ERROR": "FORMATTING_ERROR",
    "CONFIG_ERROR": "CONFIG_ERROR",
    "LANGUAGE_ERROR": "LANGUAGE_ERROR",
    "NETWORK_ERROR": "NETWORK_ERROR",
    "PERMISSION_ERROR": "PERMISSION_ERROR",
    "RESOURCE_ERROR": "RESOURCE_ERROR",
    "TIMEOUT_ERROR": "TIMEOUT_ERROR",
    "UNKNOWN_ERROR": "UNKNOWN_ERROR",
}

# HTTP 상태 코드
HTTP_STATUS_CODES = {
    200: "OK",
    201: "Created",
    400: "Bad Request",
    401: "Unauthorized",
    403: "Forbidden",
    404: "Not Found",
    500: "Internal Server Error",
    503: "Service Unavailable",
}

# 시간 관련
DEFAULT_TIMEOUT = 30  # 초
DEFAULT_RETRY_DELAY = 1  # 초
MAX_TIMEOUT = 300  # 5분
MIN_TIMEOUT = 1  # 1초

# 데이터베이스 관련
DEFAULT_DB_TIMEOUT = 10  # 초
MAX_DB_CONNECTIONS = 20
DEFAULT_DB_POOL_SIZE = 5

# API 관련
DEFAULT_API_VERSION = "v1"
DEFAULT_PAGE_SIZE = 20
MAX_PAGE_SIZE = 100
DEFAULT_SORT_ORDER = "desc"

# 캐시 관련
DEFAULT_CACHE_SIZE = 1000
MAX_CACHE_SIZE = 10000
CACHE_CLEANUP_INTERVAL = 3600  # 1시간

# 모니터링 관련
HEALTH_CHECK_INTERVAL = 30  # 초
METRICS_COLLECTION_INTERVAL = 60  # 초
ALERT_THRESHOLD = 0.8  # 80%

# 개발/테스트 관련
DEBUG_MODE = False
TEST_MODE = False
MOCK_MODE = False
LOG_SQL_QUERIES = False

# 국제화 관련
DEFAULT_LOCALE = "ko_KR"
SUPPORTED_LOCALES = ["ko_KR", "en_US", "ja_JP", "zh_CN"]
DEFAULT_TIMEZONE = "Asia/Seoul"

# 성능 임계값
PERFORMANCE_THRESHOLDS = {
    "response_time": 1000,  # ms
    "memory_usage": "100MB",
    "cpu_usage": 80,  # %
    "disk_usage": 90,  # %
    "error_rate": 0.01,  # 1%
}

# 품질 임계값
QUALITY_THRESHOLDS = {
    "maintainability": 70,  # 점수
    "readability": 80,
    "completeness": 90,
    "consistency": 85,
    "overall": 80,
}

# 복잡도 임계값
COMPLEXITY_THRESHOLDS = {"low": 30, "medium": 60, "high": 80, "very_high": 100}

# 이슈 심각도
ISSUE_SEVERITY = {"low": "낮음", "medium": "보통", "high": "높음", "critical": "치명적"}

# 이슈 타입
ISSUE_TYPES = {
    "syntax": "구문 오류",
    "semantic": "의미 오류",
    "performance": "성능 문제",
    "security": "보안 문제",
    "maintainability": "유지보수성 문제",
    "compatibility": "호환성 문제",
}
