"""
LLM Chat 서비스 실행 스크립트 (문서 스펙에 맞게 수정)
"""

import sys
import os
# 루트 src 경로를 path에 추가하여 타 모듈 절대 import 지원
_BASE_DIR = os.path.dirname(__file__)
_SRC_DIR = os.path.dirname(_BASE_DIR)
if _SRC_DIR not in sys.path:
    sys.path.append(_SRC_DIR)

from fastapi import FastAPI, HTTPException
from fastapi.responses import StreamingResponse
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
from typing import Optional, List, Dict, Any
import openai
import time
import uuid
import asyncio
from datetime import datetime

app = FastAPI(title="LLM Chat Service", version="1.0.0")

# CORS 설정
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# OpenAI 클라이언트 설정
openai.api_key = os.getenv("OPENAI_API_KEY")
print(f"🔑 OpenAI API Key 설정됨: {openai.api_key[:20]}..." if openai.api_key else "❌ OpenAI API Key가 설정되지 않음")

# 환경변수 확인
print(f"🔍 환경변수 OPENAI_API_KEY: {os.getenv('OPENAI_API_KEY', 'NOT_SET')[:20]}...")

# 강제로 올바른 API 키 설정
correct_api_key = "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"
openai.api_key = correct_api_key
print(f"🔧 강제 API 키 설정: {openai.api_key[:20]}...")

# API 호출 통계
api_call_stats = {
    "total_calls": 0,
    "successful_calls": 0,
    "failed_calls": 0,
    "last_call_time": None,
    "calls_per_minute": 0,
    "last_minute_calls": []
}

# 메모리 내 세션 저장소 (실제로는 데이터베이스 사용)
sessions = {}
messages = {}

async def call_openai_with_retry(messages: List[Dict], model: str, max_retries: int = 3, base_delay: float = 1.0):
    """
    OpenAI API 호출을 retry 로직과 함께 수행합니다.
    
    Args:
        messages: 채팅 메시지 리스트
        model: 사용할 모델명
        max_retries: 최대 재시도 횟수
        base_delay: 기본 대기 시간 (초)
    
    Returns:
        OpenAI API 응답
        
    Raises:
        HTTPException: 최대 재시도 후에도 실패한 경우
    """
    for attempt in range(max_retries + 1):
        try:
            print(f"🔄 [{attempt + 1}/{max_retries + 1}] OpenAI API 호출 시도")
            response = openai.chat.completions.create(
                model=model,
                messages=messages
            )
            print(f"✅ OpenAI API 호출 성공 (시도 {attempt + 1})")
            return response
            
        except Exception as e:
            error_str = str(e)
            print(f"❌ OpenAI API 호출 실패 (시도 {attempt + 1}): {error_str}")
            
            # 429 오류인 경우에만 재시도
            if "429" in error_str and attempt < max_retries:
                delay = base_delay * (2 ** attempt)  # exponential backoff
                print(f"⏳ {delay}초 대기 후 재시도...")
                await asyncio.sleep(delay)
                continue
            else:
                # 429가 아니거나 최대 재시도에 도달한 경우
                raise HTTPException(status_code=502, detail=f"LLM 호출 실패: {error_str}")
    
    # 이 지점에 도달하면 안 되지만, 안전을 위해
    raise HTTPException(status_code=502, detail="최대 재시도 횟수 초과")

def format_response_with_paragraphs(response: str) -> str:
    """
    AI 응답에 문단 구분을 추가하여 가독성을 향상시킵니다.
    
    Args:
        response: 원본 응답 텍스트
        
    Returns:
        문단 구분이 추가된 응답 텍스트
    """
    if not response:
        return response
        
    import re
    
    # 문단 구분 패턴들
    paragraph_patterns = [
        # 제목/헤더 패턴
        (r'(\*\*[^*]+\*\*:)', r'\n\n\1\n'),  # **제목:** 형태
        (r'(### [^#\n]+)', r'\n\n\1\n'),     # ### 제목 형태
        (r'(## [^#\n]+)', r'\n\n\1\n'),      # ## 제목 형태
        (r'(# [^#\n]+)', r'\n\n\1\n'),       # # 제목 형태
        
        # 번호 목록 패턴
        (r'(\n\d+\.\s)', r'\n\n\1'),         # 1. 2. 3. 형태
        (r'(\n-\s)', r'\n\n\1'),             # - 목록 형태
        (r'(\n\*\s)', r'\n\n\1'),            # * 목록 형태
        
        # 특별한 키워드 패턴
        (r'(\n핵심 요지)', r'\n\n\1'),
        (r'(\n근거 섹션)', r'\n\n\1'),
        (r'(\n조치/확장)', r'\n\n\1'),
        (r'(\n추가 설명)', r'\n\n\1'),
        (r'(\n결론)', r'\n\n\1'),
        (r'(\n요약)', r'\n\n\1'),
        
        # 문장 끝 패턴 (마침표 후)
        (r'(\.\s)([A-Z가-힣])', r'\1\n\n\2'),  # 마침표 후 대문자/한글
    ]
    
    formatted_response = response
    
    # 패턴 적용
    for pattern, replacement in paragraph_patterns:
        formatted_response = re.sub(pattern, replacement, formatted_response)
    
    # 연속된 줄바꿈 정리 (3개 이상을 2개로)
    formatted_response = re.sub(r'\n{3,}', '\n\n', formatted_response)
    
    # 시작과 끝의 불필요한 줄바꿈 제거
    formatted_response = formatted_response.strip()
    
    return formatted_response

class CreateSessionRequest(BaseModel):
    title: str = "새 채팅"

class SessionResponse(BaseModel):
    id: str
    title: str
    created_at: str
    messages: List[Dict[str, Any]]

class MessageRequest(BaseModel):
    message: str
    model: str = "gpt-3.5-turbo"
    context: Optional[Dict[str, Any]] = None

class MessageResponse(BaseModel):
    message: str
    session_id: str
    model: str
    usage: Dict[str, int]
    response_time: float

@app.get("/")
async def root():
    return {"service": "LLM Chat Service", "status": "running", "port": 8004}

@app.get("/health")
async def health():
    return {"status": "healthy", "service": "LLM Chat Service"}

@app.get("/api/v1/stats")
async def get_api_stats():
    """API 호출 통계 확인"""
    return {
        "api_call_stats": api_call_stats,
        "timestamp": datetime.now().isoformat()
    }

@app.get("/api/v1/debug")
async def debug_info():
    """디버그 정보 확인"""
    return {
        "openai_api_key": openai.api_key[:20] + "..." if openai.api_key else "NOT_SET",
        "env_openai_key": os.getenv("OPENAI_API_KEY", "NOT_SET")[:20] + "..." if os.getenv("OPENAI_API_KEY") else "NOT_SET",
        "key_length": len(openai.api_key) if openai.api_key else 0,
        "env_key_length": len(os.getenv("OPENAI_API_KEY", "")),
        "keys_match": openai.api_key == os.getenv("OPENAI_API_KEY"),
        "timestamp": datetime.now().isoformat()
    }

@app.post("/api/v1/chat/sessions", response_model=SessionResponse)
async def create_session(request: CreateSessionRequest):
    """채팅 세션 생성"""
    try:
        session_id = str(uuid.uuid4())
        now = datetime.now().isoformat()
        
        session = {
            "id": session_id,
            "title": request.title,
            "created_at": now,
            "messages": []
        }
        
        sessions[session_id] = session
        messages[session_id] = []
        
        return SessionResponse(**session)
        
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"세션 생성 중 오류 발생: {str(e)}")

@app.get("/api/v1/chat/sessions", response_model=List[SessionResponse])
async def list_sessions():
    """세션 목록 조회"""
    try:
        return [SessionResponse(**session) for session in sessions.values()]
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"세션 목록 조회 중 오류 발생: {str(e)}")

@app.get("/api/v1/chat/sessions/{session_id}/messages", response_model=List[Dict[str, Any]])
async def get_messages(session_id: str):
    """세션의 메시지 목록 조회"""
    try:
        if session_id not in messages:
            raise HTTPException(status_code=404, detail="세션을 찾을 수 없습니다")
        
        return messages[session_id]
    except HTTPException:
        raise
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"메시지 조회 중 오류 발생: {str(e)}")

@app.post("/api/v1/chat/sessions/{session_id}/messages", response_model=MessageResponse)
async def send_message(session_id: str, request: MessageRequest):
    """메시지 전송"""
    try:
        if session_id not in sessions:
            raise HTTPException(status_code=404, detail="세션을 찾을 수 없습니다")
        
        start_time = time.time()
        
        # 컨텍스트가 있으면 프롬프트에 추가
        system_message = """
당신은 "CodeMuse" – 레거시 코드베이스 분석 어시스턴트입니다.

목표: 제공된 컨텍스트를 근거로, 질문 의도에 맞게 실용적이고 맥락 있는 답변을 생성합니다.

📌 답변 지침:
1. **🚨 숫자 보존 필수**: 모든 숫자(7가지, 80번 줄, 157번 줄 등)를 절대 생략하지 마세요.
2. **🚨 구체적 표현**: "몇번째줄" 대신 "80번 줄", "157번 줄" 등 정확한 숫자 사용
3. **🚨 목록 번호**: "7가지", "5개", "3단계" 등 모든 숫자를 반드시 포함
4. **🚨 경고**: 숫자를 생략하면 안 됩니다! 반드시 모든 숫자를 포함하세요!
5. 반드시 제공된 정보를 우선 활용합니다. (숫자/목록/함수명은 절대 생략하지 않습니다)  
6. **소스코드 정보가 포함된 경우**, 반드시 다음을 포함합니다:
   - 📁 **파일 경로**: 정확한 파일명과 경로
   - 📍 **줄 번호**: 해당 로직이 위치한 정확한 줄 번호 (예: 80번 줄, 157번 줄)
   - 🔧 **구체적 위치**: 함수명, 클래스명, 메서드명
7. **중요**: sample_code 디렉토리의 파일이 포함된 경우, 반드시 해당 파일의 정보를 우선적으로 사용하세요.
8. **줄 번호 필수**: 📍 **줄 번호** 정보가 제공된 경우, 반드시 "80번 줄", "157번 줄" 등으로 명시하세요.
9. **경고**: 줄 번호를 생략하면 안 됩니다. 반드시 구체적인 줄 번호를 포함해야 합니다.
10. **링크 형식**: 클래스명이나 파일명을 언급할 때는 다음 형식을 사용하세요:
   - 클래스명: [IssueDetector](class_a1b2c3_IssueDetector)
   - 파일명: [issue_detector.py](file_a1b2c3)
   - 함수명: [detect_issues()](func_a1b2c3_detect_issues_80)
11. 답변은 **3단 구성**으로 작성합니다:  
    - **핵심 요지**: (2~4줄 요약)  
    - **근거 섹션 요약**: (관련 함수/클래스/모듈, 필요 시 코드 시그니처 포함)  
    - **조치/확장 방법**: (파일 → 클래스 → 함수 흐름 단위로 단계적 설명)  
12. **문단 구분**: 각 섹션은 반드시 빈 줄로 구분하고, 섹션 제목은 **굵은 글씨**로 표시하세요.
13. 정보가 부족한 경우, "근거 부족"을 명시하고 필요한 추가 정보를 요청합니다.  
14. 추측은 하지 않고, 불확실하면 명확히 표시합니다.  
15. 답변은 친근하면서도 전문적으로, 레거시 코드 분석/리팩토링 맥락을 유지합니다.

---

📌 최종 답변:"""

        # RAG 컨텍스트 처리 (여러 형태 지원)
        context_text = ""
        if request.context:
            # 0) 최우선: final_context 문자열
            if isinstance(request.context.get("final_context"), str) and request.context.get("final_context").strip():
                context_text = request.context.get("final_context").strip()
            elif "rag_documents" in request.context:
                # 워크플로우에서 전달하는 형태
                rag_docs = request.context["rag_documents"]
                if rag_docs:
                    context_text = "\n\n".join(rag_docs)
            elif "search_results" in request.context:
                # 기존 형태
                search_results = request.context["search_results"]
                context_text = "\n".join([doc.get("content", "") for doc in search_results])
        
        # 일반 워크플로우와 동일한 프롬프트 구조 사용
        if context_text:
            # 완전한 프롬프트 구조 (일반 워크플로우와 동일)
            prompt = f"""당신은 "CodeMuse" – 레거시 코드베이스 분석 어시스턴트입니다.  
다음 코드 정보를 바탕으로 사용자 질문에 답변하세요.  

📌 참고 정보:
{context_text}

📌 질문:
{request.message}

---

📌 답변 지침:
1. **🚨 숫자 보존 필수**: 모든 숫자(7가지, 80번 줄, 157번 줄 등)를 절대 생략하지 마세요.
2. **🚨 구체적 표현**: "몇번째줄" 대신 "80번 줄", "157번 줄" 등 정확한 숫자 사용
3. **🚨 목록 번호**: "7가지", "5개", "3단계" 등 모든 숫자를 반드시 포함
4. **🚨 경고**: 숫자를 생략하면 안 됩니다! 반드시 모든 숫자를 포함하세요!
5. 반드시 제공된 정보를 우선 활용합니다. (숫자/목록/함수명은 절대 생략하지 않습니다)  
6. **소스코드 정보가 포함된 경우**, 반드시 다음을 포함합니다:
   - 📁 **파일 경로**: 정확한 파일명과 경로
   - 📍 **줄 번호**: 해당 로직이 위치한 정확한 줄 번호 (예: 80번 줄, 157번 줄)
   - 🔧 **구체적 위치**: 함수명, 클래스명, 메서드명
7. **중요**: sample_code 디렉토리의 파일이 포함된 경우, 반드시 해당 파일의 정보를 우선적으로 사용하세요.
8. **줄 번호 필수**: 📍 **줄 번호** 정보가 제공된 경우, 반드시 "80번 줄", "157번 줄" 등으로 명시하세요.
9. **경고**: 줄 번호를 생략하면 안 됩니다. 반드시 구체적인 줄 번호를 포함해야 합니다.
10. **링크 형식**: 클래스명이나 파일명을 언급할 때는 다음 형식을 사용하세요:
   - 클래스명: [IssueDetector](class_a1b2c3_IssueDetector)
   - 파일명: [issue_detector.py](file_a1b2c3)
   - 함수명: [detect_issues()](func_a1b2c3_detect_issues_80)
11. 답변은 **3단 구성**으로 작성합니다:  
    - **핵심 요지**: (2~4줄 요약)  
    - **근거 섹션 요약**: (관련 함수/클래스/모듈, 필요 시 코드 시그니처 포함)  
    - **조치/확장 방법**: (파일 → 클래스 → 함수 흐름 단위로 단계적 설명)  
12. **문단 구분**: 각 섹션은 반드시 빈 줄로 구분하고, 섹션 제목은 **굵은 글씨**로 표시하세요.
13. 정보가 부족한 경우, "근거 부족"을 명시하고 필요한 추가 정보를 요청합니다.  
14. 추측은 하지 않고, 불확실하면 명확히 표시합니다.  
15. 답변은 친근하면서도 전문적으로, 레거시 코드 분석/리팩토링 맥락을 유지합니다.

---

📌 최종 답변:"""
            
            # 시스템 메시지는 간단하게 설정
            enhanced_system_message = "당신은 CodeMuse - 레거시 코드베이스 분석 어시스턴트입니다."
            user_message = prompt
        else:
            # 컨텍스트가 없는 경우 기존 방식 사용
            enhanced_system_message = system_message
            user_message = request.message

        # 대화 히스토리 구성
        chat_messages = [{"role": "system", "content": enhanced_system_message}]
        
        # 기존 메시지들 추가 (컨텍스트가 있는 경우 히스토리 제한)
        if context_text:
            # 컨텍스트가 있으면 히스토리를 최소화하여 프롬프트에 집중
            for msg in messages[session_id][-3:]:  # 최근 3개 메시지만
                chat_messages.append({"role": msg["role"], "content": msg["content"]})
        else:
            # 컨텍스트가 없으면 기존 방식 유지
            for msg in messages[session_id][-10:]:  # 최근 10개 메시지
                chat_messages.append({"role": msg["role"], "content": msg["content"]})
        
        # 새 사용자 메시지 추가
        chat_messages.append({"role": "user", "content": user_message})
        
        # API 호출 통계 업데이트
        current_time = time.time()
        request_id = str(uuid.uuid4())[:8]
        
        # 분당 호출 수 계산
        api_call_stats["last_minute_calls"] = [
            call_time for call_time in api_call_stats["last_minute_calls"] 
            if current_time - call_time < 60
        ]
        api_call_stats["last_minute_calls"].append(current_time)
        api_call_stats["calls_per_minute"] = len(api_call_stats["last_minute_calls"])
        api_call_stats["total_calls"] += 1
        api_call_stats["last_call_time"] = current_time
        
        # OpenAI API 호출
        print(f"🔍 [{request_id}] OpenAI API 호출 시작: model={request.model}, messages={len(chat_messages)}개")
        print(f"🔑 [{request_id}] 사용 중인 API Key: {openai.api_key[:20]}..." if openai.api_key else f"❌ [{request_id}] API Key 없음")
        print(f"📊 [{request_id}] 통계 - 총 호출: {api_call_stats['total_calls']}, 분당: {api_call_stats['calls_per_minute']}")
        
        # API 호출 전에 API 키를 다시 설정
        correct_api_key = "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"
        openai.api_key = correct_api_key
        print(f"🔧 [{request_id}] API 호출 전 키 재설정: {openai.api_key[:20]}...")
        
        try:
            response = await call_openai_with_retry(chat_messages, request.model)
            api_call_stats["successful_calls"] += 1
            print(f"✅ [{request_id}] OpenAI API 호출 성공")
        except HTTPException as e:
            api_call_stats["failed_calls"] += 1
            print(f"❌ [{request_id}] OpenAI API 호출 최종 실패: {e.detail}")
            print(f"📊 [{request_id}] 실패 통계 - 성공: {api_call_stats['successful_calls']}, 실패: {api_call_stats['failed_calls']}")
            raise e
        
        response_time = time.time() - start_time
        
        # 응답 메시지 저장
        user_msg = {
            "role": "user",
            "content": request.message,
            "timestamp": datetime.now().isoformat()
        }
        ai_msg = {
            "role": "assistant", 
            "content": response.choices[0].message.content,
            "timestamp": datetime.now().isoformat()
        }
        
        messages[session_id].extend([user_msg, ai_msg])
        
        # 사용량 정보 (실제로는 OpenAI 응답에서 가져옴)
        usage = {
            "prompt_tokens": 150,  # 실제로는 response.usage.prompt_tokens
            "completion_tokens": 200,  # 실제로는 response.usage.completion_tokens
            "total_tokens": 350  # 실제로는 response.usage.total_tokens
        }
        
        # 응답 내용 확인
        if not response.choices or not response.choices[0].message.content:
            print(f"❌ OpenAI 응답이 비어있음: {response}")
            raise HTTPException(status_code=500, detail="OpenAI에서 빈 응답을 받았습니다")
        
        response_content = response.choices[0].message.content
        # 응답에 문단 구분 추가
        response_content = format_response_with_paragraphs(response_content)
        print(f"✅ 응답 생성 완료: {len(response_content)}자")
        
        return MessageResponse(
            message=response_content,
            session_id=session_id,
            model=request.model,
            usage=usage,
            response_time=response_time
        )
        
    except HTTPException:
        raise
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"메시지 전송 중 오류 발생: {str(e)}")
    
## 폴백 함수 삭제됨 (도메인 하드코딩 제거)

# SSE 스트리밍 엔드포인트 (실시간 토큰 스트림)
@app.post("/api/v1/chat/stream")
async def stream_message(request: MessageRequest):
    """실시간 SSE로 토큰을 스트리밍 반환"""
    try:
        start_time = time.time()

        # 일반 워크플로우와 동일한 프롬프트 구조 사용
        context_text = ""
        if request.context:
            if isinstance(request.context.get("final_context"), str) and request.context.get("final_context").strip():
                context_text = request.context.get("final_context").strip()

        if context_text:
            # 완전한 프롬프트 구조 (일반 워크플로우와 동일)
            prompt = f"""당신은 "CodeMuse" – 레거시 코드베이스 분석 어시스턴트입니다.  
다음 코드 정보를 바탕으로 사용자 질문에 답변하세요.  

📌 참고 정보:
{context_text}

📌 질문:
{request.message}

---

📌 답변 지침:
1. **🚨 숫자 보존 필수**: 모든 숫자(7가지, 80번 줄, 157번 줄 등)를 절대 생략하지 마세요.
2. **🚨 구체적 표현**: "몇번째줄" 대신 "80번 줄", "157번 줄" 등 정확한 숫자 사용
3. **🚨 목록 번호**: "7가지", "5개", "3단계" 등 모든 숫자를 반드시 포함
4. **🚨 경고**: 숫자를 생략하면 안 됩니다! 반드시 모든 숫자를 포함하세요!
5. 반드시 제공된 정보를 우선 활용합니다. (숫자/목록/함수명은 절대 생략하지 않습니다)  
6. **소스코드 정보가 포함된 경우**, 반드시 다음을 포함합니다:
   - 📁 **파일 경로**: 정확한 파일명과 경로
   - 📍 **줄 번호**: 해당 로직이 위치한 정확한 줄 번호 (예: 80번 줄, 157번 줄)
   - 🔧 **구체적 위치**: 함수명, 클래스명, 메서드명
7. **중요**: sample_code 디렉토리의 파일이 포함된 경우, 반드시 해당 파일의 정보를 우선적으로 사용하세요.
8. **줄 번호 필수**: 📍 **줄 번호** 정보가 제공된 경우, 반드시 "80번 줄", "157번 줄" 등으로 명시하세요.
9. **경고**: 줄 번호를 생략하면 안 됩니다. 반드시 구체적인 줄 번호를 포함해야 합니다.
10. **링크 형식**: 클래스명이나 파일명을 언급할 때는 다음 형식을 사용하세요:
   - 클래스명: [IssueDetector](class_a1b2c3_IssueDetector)
   - 파일명: [issue_detector.py](file_a1b2c3)
   - 함수명: [detect_issues()](func_a1b2c3_detect_issues_80)
11. 답변은 **3단 구성**으로 작성합니다:  
    - **핵심 요지**: (2~4줄 요약)  
    - **근거 섹션 요약**: (관련 함수/클래스/모듈, 필요 시 코드 시그니처 포함)  
    - **조치/확장 방법**: (파일 → 클래스 → 함수 흐름 단위로 단계적 설명)  
12. **문단 구분**: 각 섹션은 반드시 빈 줄로 구분하고, 섹션 제목은 **굵은 글씨**로 표시하세요.
13. 정보가 부족한 경우, "근거 부족"을 명시하고 필요한 추가 정보를 요청합니다.  
14. 추측은 하지 않고, 불확실하면 명확히 표시합니다.  
15. 답변은 친근하면서도 전문적으로, 레거시 코드 분석/리팩토링 맥락을 유지합니다.

---

📌 최종 답변:"""
            
            # 시스템 메시지는 간단하게 설정
            system_message = "당신은 CodeMuse - 레거시 코드베이스 분석 어시스턴트입니다."
            user_message = prompt
        else:
            # 컨텍스트가 없는 경우 기존 방식 사용
            system_message = """
당신은 "CodeMuse" – 레거시 코드베이스 분석 어시스턴트입니다.

목표: 제공된 Markdown(MD) 섹션 컨텍스트를 근거로, 질문 의도에 맞게 실용적이고 맥락 있는 답변을 생성합니다.
"""
            user_message = request.message

        chat_messages = [{"role": "system", "content": system_message}, {"role": "user", "content": user_message}]

        model_name = request.model or "gpt-3.5-turbo"

        def format_sse(event: str, data: str) -> bytes:
            return f"event: {event}\ndata: {data}\n\n".encode("utf-8")

        async def token_generator():
            yield format_sse("status", "started")
            yield format_sse("model", model_name)

            # API 호출 통계 업데이트
            current_time = time.time()
            request_id = str(uuid.uuid4())[:8]
            
            # 분당 호출 수 계산
            api_call_stats["last_minute_calls"] = [
                call_time for call_time in api_call_stats["last_minute_calls"] 
                if current_time - call_time < 60
            ]
            api_call_stats["last_minute_calls"].append(current_time)
            api_call_stats["calls_per_minute"] = len(api_call_stats["last_minute_calls"])
            api_call_stats["total_calls"] += 1
            api_call_stats["last_call_time"] = current_time
            
            print(f"🔍 [{request_id}] STREAM OpenAI API 호출 시작: model={model_name}, messages={len(chat_messages)}개")
            print(f"🔑 [{request_id}] 사용 중인 API Key: {openai.api_key[:20]}..." if openai.api_key else f"❌ [{request_id}] API Key 없음")
            print(f"📊 [{request_id}] 통계 - 총 호출: {api_call_stats['total_calls']}, 분당: {api_call_stats['calls_per_minute']}")

            # 스트림 API용 retry 로직
            max_retries = 3
            for attempt in range(max_retries + 1):
                try:
                    stream = openai.chat.completions.create(
                        model=model_name,
                        messages=chat_messages,
                        stream=True,
                    )
                    api_call_stats["successful_calls"] += 1
                    print(f"✅ [{request_id}] STREAM OpenAI API 호출 성공 (시도 {attempt + 1})")
                    break  # 성공하면 루프 탈출
                    
                except Exception as openai_error:
                    error_str = str(openai_error)
                    print(f"❌ [{request_id}] STREAM OpenAI API 호출 실패 (시도 {attempt + 1}): {error_str}")
                    
                    # 429 오류인 경우에만 재시도
                    if "429" in error_str and attempt < max_retries:
                        delay = 1.0 * (2 ** attempt)  # exponential backoff
                        print(f"⏳ [{request_id}] {delay}초 대기 후 재시도...")
                        await asyncio.sleep(delay)
                        continue
                    else:
                        # 429가 아니거나 최대 재시도에 도달한 경우
                        api_call_stats["failed_calls"] += 1
                        print(f"📊 [{request_id}] 실패 통계 - 성공: {api_call_stats['successful_calls']}, 실패: {api_call_stats['failed_calls']}")
                        yield format_sse("error", f"LLM call failed: {error_str}")
                        yield format_sse("done", "error")
                        return
            
            # 최대 재시도 후에도 실패한 경우 (이론적으로 도달하지 않아야 함)
            if attempt == max_retries:
                yield format_sse("error", "최대 재시도 횟수 초과")
                yield format_sse("done", "error")
                return

            full_text = []
            try:
                for chunk in stream:
                    token = None
                    try:
                        delta = chunk.choices[0].delta  # type: ignore[attr-defined]
                        token = getattr(delta, "content", None)
                    except Exception:
                        if chunk.choices and getattr(chunk.choices[0], "delta", None):
                            token = chunk.choices[0].delta.get("content") if isinstance(chunk.choices[0].delta, dict) else None
                    if token:
                        full_text.append(token)
                        yield format_sse("token", token)
            except Exception as stream_error:
                yield format_sse("error", f"stream error: {str(stream_error)}")
            finally:
                duration = time.time() - start_time
                yield format_sse("metrics", str({"response_time": round(duration, 3)}))
                yield format_sse("done", "ok")

        headers = {
            "Cache-Control": "no-cache",
            "Content-Type": "text/event-stream",
            "Connection": "keep-alive",
        }
        return StreamingResponse(token_generator(), headers=headers, media_type="text/event-stream")
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"스트리밍 중 오류 발생: {str(e)}")

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8004)
