"""
LLM Service

LLM API 연동 서비스
"""

import os
import time
from typing import List, Dict, Any, Optional
import openai
import anthropic
from datetime import datetime

from ..models.chat_request import ChatRequest, ChatResponse
from ..models.chat_session import ChatSession, Message


class LLMService:
    """LLM 서비스"""
    
    def __init__(self):
        # OpenAI 설정
        self.openai_client = openai.OpenAI(
            api_key=os.getenv("OPENAI_API_KEY")
        )
        
        # Anthropic 설정
        self.anthropic_client = anthropic.Anthropic(
            api_key=os.getenv("ANTHROPIC_API_KEY")
        )
    
    async def chat(self, request: ChatRequest) -> ChatResponse:
        """채팅 요청 처리"""
        start_time = time.time()
        
        try:
            if request.model.startswith("gpt"):
                response = await self._chat_openai(request)
            elif request.model.startswith("claude"):
                response = await self._chat_anthropic(request)
            else:
                # 기본값으로 OpenAI 사용
                response = await self._chat_openai(request)
            
            response_time = time.time() - start_time
            
            return ChatResponse(
                message=response["message"],
                session_id=request.session_id or "default",
                model=request.model,
                usage=response.get("usage", {}),
                response_time=response_time,
                metadata={
                    "provider": "openai" if request.model.startswith("gpt") else "anthropic",
                    "timestamp": datetime.now().isoformat()
                }
            )
            
        except Exception as e:
            # 에러 발생 시 기본 응답
            return ChatResponse(
                message=f"죄송합니다. 오류가 발생했습니다: {str(e)}",
                session_id=request.session_id or "default",
                model=request.model,
                usage={},
                response_time=time.time() - start_time,
                metadata={"error": str(e)}
            )
    
    async def _chat_openai(self, request: ChatRequest) -> Dict[str, Any]:
        """OpenAI API 호출"""
        try:
            messages = self._prepare_messages(request)
            
            response = self.openai_client.chat.completions.create(
                model=request.model,
                messages=messages,
                temperature=request.temperature,
                max_tokens=request.max_tokens
            )
            
            return {
                "message": response.choices[0].message.content,
                "usage": {
                    "prompt_tokens": response.usage.prompt_tokens,
                    "completion_tokens": response.usage.completion_tokens,
                    "total_tokens": response.usage.total_tokens
                }
            }
            
        except Exception as e:
            raise Exception(f"OpenAI API 오류: {str(e)}")
    
    async def _chat_anthropic(self, request: ChatRequest) -> Dict[str, Any]:
        """Anthropic API 호출"""
        try:
            # Anthropic은 다른 메시지 형식 사용
            system_message = ""
            user_message = request.message
            
            if request.context:
                system_message = f"컨텍스트: {request.context.get('summary', '')}"
            
            response = self.anthropic_client.messages.create(
                model=request.model,
                max_tokens=request.max_tokens or 1000,
                temperature=request.temperature,
                system=system_message,
                messages=[{"role": "user", "content": user_message}]
            )
            
            return {
                "message": response.content[0].text,
                "usage": {
                    "input_tokens": response.usage.input_tokens,
                    "output_tokens": response.usage.output_tokens,
                    "total_tokens": response.usage.input_tokens + response.usage.output_tokens
                }
            }
            
        except Exception as e:
            raise Exception(f"Anthropic API 오류: {str(e)}")
    
    def _prepare_messages(self, request: ChatRequest) -> List[Dict[str, str]]:
        """메시지 준비"""
        messages = []
        
        # 시스템 메시지 (컨텍스트가 있는 경우)
        if request.context:
            ctx = request.context or {}
            system_context = ""
            
            # 1) 우선: 최종 컨텍스트 문자열이 있으면 그대로 사용
            final_ctx = ctx.get("final_context")
            raw_ctx = ctx.get("raw")
            if isinstance(final_ctx, str) and final_ctx.strip():
                system_context = final_ctx.strip()
            elif isinstance(raw_ctx, str) and raw_ctx.strip():
                system_context = raw_ctx.strip()
            else:
                # 2) RAG 검색 결과 요약 구성
                search_results = ctx.get("search_results")
                if isinstance(search_results, list) and search_results:
                    lines: List[str] = []
                    for i, r in enumerate(search_results[:5]):
                        meta = r.get("metadata", {}) if isinstance(r, dict) else {}
                        title = (
                            meta.get("section_title")
                            or meta.get("name")
                            or meta.get("filename")
                            or "section"
                        )
                        filename = meta.get("filename") or meta.get("source_file") or "unknown"
                        chunk_type = meta.get("chunk_type") or "unknown"
                        content = (r.get("content") if isinstance(r, dict) else "") or ""
                        content = content[:800]
                        lines.append(f"- [{chunk_type}] {title} ({filename})\n{content}")
                    if lines:
                        system_context = "참고 문서 섹션:\n" + "\n\n".join(lines)
                
                # 3) 하위 호환: summary가 있으면 사용
                if not system_context:
                    context_summary = ctx.get('summary', '')
                    if isinstance(context_summary, str) and context_summary.strip():
                        system_context = context_summary.strip()
            
            if system_context:
                messages.append({
                    "role": "system",
                    "content": f"다음 컨텍스트를 정확히 활용해 답변하세요:\n{system_context}"
                })
        
        # 사용자 메시지
        messages.append({
            "role": "user",
            "content": request.message
        })
        
        return messages
    
    def get_available_models(self) -> Dict[str, List[str]]:
        """사용 가능한 모델 목록"""
        return {
            "openai": [
                "gpt-3.5-turbo",
                "gpt-4",
                "gpt-4-turbo-preview"
            ],
            "anthropic": [
                "claude-3-sonnet-20240229",
                "claude-3-opus-20240229",
                "claude-3-haiku-20240307"
            ]
        }
