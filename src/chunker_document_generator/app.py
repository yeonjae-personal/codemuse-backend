from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware

from .api.routes import router as generator_router

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


