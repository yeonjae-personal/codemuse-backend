"""
LLM Chat API Routes
"""

from fastapi import APIRouter, HTTPException
from typing import List, Optional

from ..models.chat_request import ChatRequest, ChatResponse
from ..models.chat_session import ChatSession, Message
from ..services.chat_service import ChatService

router = APIRouter(prefix="/api/v1/chat", tags=["LLM Chat"])

# 서비스 인스턴스
chat_service = ChatService()


@router.post("/sessions", response_model=ChatSession)
async def create_chat_session(user_id: Optional[str] = None, title: str = "New Chat"):
    """채팅 세션 생성"""
    try:
        session = chat_service.create_session(user_id, title)
        return session
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"세션 생성 중 오류 발생: {str(e)}")


@router.get("/sessions/{session_id}", response_model=ChatSession)
async def get_chat_session(session_id: str):
    """채팅 세션 조회"""
    try:
        session = chat_service.get_session(session_id)
        if not session:
            raise HTTPException(status_code=404, detail="세션을 찾을 수 없습니다")
        return session
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"세션 조회 중 오류 발생: {str(e)}")


@router.post("/sessions/{session_id}/messages", response_model=ChatResponse)
async def send_message(session_id: str, request: ChatRequest):
    """메시지 전송"""
    try:
        # 세션 존재 확인
        session = chat_service.get_session(session_id)
        if not session:
            raise HTTPException(status_code=404, detail="세션을 찾을 수 없습니다")
        
        # 세션 ID 설정
        request.session_id = session_id
        
        # 메시지 전송
        response = await chat_service.send_message(request)
        return response
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"메시지 전송 중 오류 발생: {str(e)}")


@router.get("/sessions/{session_id}/messages", response_model=List[Message])
async def get_messages(session_id: str):
    """세션의 메시지 목록 조회"""
    try:
        messages = chat_service.get_messages(session_id)
        return messages
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"메시지 조회 중 오류 발생: {str(e)}")


@router.get("/sessions", response_model=List[ChatSession])
async def list_sessions(user_id: Optional[str] = None):
    """세션 목록 조회"""
    try:
        sessions = chat_service.list_sessions(user_id)
        return sessions
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"세션 목록 조회 중 오류 발생: {str(e)}")


@router.delete("/sessions/{session_id}")
async def delete_session(session_id: str):
    """세션 삭제"""
    try:
        success = chat_service.delete_session(session_id)
        if success:
            return {"message": "세션이 삭제되었습니다"}
        else:
            raise HTTPException(status_code=404, detail="세션을 찾을 수 없습니다")
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"세션 삭제 중 오류 발생: {str(e)}")


@router.get("/models")
async def get_available_models():
    """사용 가능한 모델 목록 조회"""
    try:
        models = chat_service.get_available_models()
        return models
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"모델 목록 조회 중 오류 발생: {str(e)}")


@router.get("/health")
async def health_check():
    """헬스 체크"""
    return {"status": "healthy", "service": "llm-chat"}
