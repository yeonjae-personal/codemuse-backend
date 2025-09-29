"""
애플리케이션 설정
"""

import os
from typing import Optional
from pydantic import BaseSettings, Field


class Settings(BaseSettings):
    """애플리케이션 설정"""
    
    # 기본 설정
    app_name: str = Field(default="CodeMuse", description="애플리케이션 이름")
    app_version: str = Field(default="1.0.0", description="애플리케이션 버전")
    debug: bool = Field(default=False, description="디버그 모드")
    environment: str = Field(default="development", description="환경")
    
    # 서버 설정
    host: str = Field(default="0.0.0.0", description="서버 호스트")
    port: int = Field(default=8000, description="서버 포트")
    
    # 데이터베이스 설정
    database_url: str = Field(..., description="데이터베이스 URL")
    database_pool_size: int = Field(default=10, description="데이터베이스 풀 크기")
    
    # Redis 설정
    redis_url: str = Field(default="redis://localhost:6379", description="Redis URL")
    redis_db: int = Field(default=0, description="Redis 데이터베이스")
    
    # JWT 설정
    secret_key: str = Field(..., description="JWT 시크릿 키")
    algorithm: str = Field(default="HS256", description="JWT 알고리즘")
    access_token_expire_minutes: int = Field(default=30, description="액세스 토큰 만료 시간")
    
    # LLM 설정
    openai_api_key: Optional[str] = Field(None, description="OpenAI API 키")
    anthropic_api_key: Optional[str] = Field(None, description="Anthropic API 키")
    default_llm_provider: str = Field(default="openai", description="기본 LLM 제공업체")
    
    # 벡터 DB 설정
    vector_db_type: str = Field(default="chroma", description="벡터 DB 타입")
    vector_db_url: str = Field(default="http://localhost:8001", description="벡터 DB URL")
    
    # 파일 업로드 설정
    max_file_size: int = Field(default=10 * 1024 * 1024, description="최대 파일 크기 (10MB)")
    allowed_file_types: list = Field(default=[".py", ".js", ".ts", ".java", ".cpp", ".c"], description="허용된 파일 타입")
    
    # 로깅 설정
    log_level: str = Field(default="INFO", description="로그 레벨")
    log_file: Optional[str] = Field(None, description="로그 파일 경로")
    
    class Config:
        env_file = ".env"
        env_file_encoding = "utf-8"


# 전역 설정 인스턴스
_settings: Optional[Settings] = None


def get_settings() -> Settings:
    """설정 인스턴스 가져오기"""
    global _settings
    if _settings is None:
        _settings = Settings()
    return _settings
