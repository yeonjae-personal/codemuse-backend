from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
import logging
import os
from pathlib import Path

from .api.routes import router as generator_router

# 로깅 설정
def setup_logging():
    """로깅 시스템 설정"""
    # 로그 디렉토리 생성
    log_dir = Path("logs")
    log_dir.mkdir(exist_ok=True)
    
    # 로그 파일 경로
    log_file = log_dir / "docgen.log"
    
    # 로거 생성
    logger = logging.getLogger("chunker_document_generator")
    logger.setLevel(logging.INFO)
    
    # 기존 핸들러 제거 (중복 방지)
    for handler in logger.handlers[:]:
        logger.removeHandler(handler)
    
    # 로그 포맷 설정
    formatter = logging.Formatter(
        "%(asctime)s - %(name)s - %(levelname)s - %(message)s",
        datefmt="%Y-%m-%d %H:%M:%S"
    )
    
    # 콘솔 핸들러
    console_handler = logging.StreamHandler()
    console_handler.setLevel(logging.INFO)
    console_handler.setFormatter(formatter)
    logger.addHandler(console_handler)
    
    # 파일 핸들러
    file_handler = logging.FileHandler(log_file, encoding='utf-8')
    file_handler.setLevel(logging.INFO)
    file_handler.setFormatter(formatter)
    logger.addHandler(file_handler)
    
    # Uvicorn 로거 설정
    uvicorn_logger = logging.getLogger("uvicorn")
    uvicorn_logger.addHandler(file_handler)
    
    return logger

# 로깅 시스템 초기화
logger = setup_logging()

app = FastAPI(title="CodeMuse Document Generator (Chunk)", version="1.0.0")

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

@app.get("/")
async def root():
    return {"service": "CodeMuse Document Generator (Chunk)", "status": "running"}

@app.get("/health")
async def health():
    return {"status": "ok"}

app.include_router(generator_router)

# 애플리케이션 시작 로그
@app.on_event("startup")
async def startup_event():
    logger.info("🚀 CodeMuse Document Generator (Chunk) 서비스 시작")
    logger.info(f"📁 로그 파일: {Path('logs/docgen.log').absolute()}")

@app.on_event("shutdown")
async def shutdown_event():
    logger.info("🛑 CodeMuse Document Generator (Chunk) 서비스 종료")


