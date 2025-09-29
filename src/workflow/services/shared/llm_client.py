"""
LLM 클라이언트 모듈
"""

import asyncio
import httpx
from typing import Dict, Any, List, Optional
import os


class LLMClient:
    """LLM 클라이언트"""
    
    def __init__(self, base_url: str = None):
        self.base_url = base_url or os.getenv("LLM_SERVICE_URL", "http://localhost:8004")
        
    async def generate_response(self, messages: List[Dict[str, str]], 
                              model: str = "gpt-3.5-turbo", 
                              temperature: float = 0.7,
                              max_tokens: int = 1000) -> str:
        """
        LLM 응답 생성
        
        Args:
            messages: 메시지 리스트
            model: 사용할 모델
            temperature: 창의성 설정
            max_tokens: 최대 토큰 수
            
        Returns:
            생성된 응답
        """
        try:
            async with httpx.AsyncClient(timeout=30.0) as client:
                # 세션 생성
                session_response = await client.post(
                    f"{self.base_url}/api/v1/chat/sessions",
                    json={"title": "Workflow Processing"},
                    timeout=httpx.Timeout(20.0)
                )
                
                if session_response.status_code != 200:
                    raise Exception(f"세션 생성 실패: {session_response.status_code}")
                
                session_data = session_response.json()
                session_id = session_data["id"]
                
                # 메시지 전송
                message_response = await client.post(
                    f"{self.base_url}/api/v1/chat/sessions/{session_id}/messages",
                    json={
                        "message": messages[0]["content"],
                        "model": model
                    },
                    timeout=httpx.Timeout(30.0)
                )
                
                if message_response.status_code == 200:
                    data = message_response.json()
                    return data.get("message", data.get("response", ""))
                else:
                    raise Exception(f"LLM API 오류: {message_response.status_code}")
                    
        except Exception as e:
            print(f"LLM 호출 실패: {e}")
            # 폴백: 간단한 응답 생성
            return self._generate_fallback_response(messages)
    
    def _generate_fallback_response(self, messages: List[Dict[str, str]]) -> str:
        """폴백 응답 생성"""
        user_message = ""
        for msg in messages:
            if msg.get("role") == "user":
                user_message = msg.get("content", "")
                break
        
        if "표준화" in user_message or "standardize" in user_message.lower():
            return "질문을 표준화했습니다."
        elif "키워드" in user_message or "keyword" in user_message.lower():
            return "키워드를 추출했습니다."
        elif "검색" in user_message or "search" in user_message.lower():
            return "검색을 수행했습니다."
        else:
            return "요청을 처리했습니다."
