"""
1단계: 질문 표준화 모듈
용어집 기반으로 사용자 질문을 표준화합니다.
"""

import asyncio
import httpx
import json
from typing import Dict, Any, Optional
from ..shared.llm_client import LLMClient
from ..vocabulary_extractor import vocabulary_extractor


class QuestionStandardizer:
    """질문 표준화 모듈"""
    
    def __init__(self, llm_client: LLMClient):
        self.llm_client = llm_client
        
    async def standardize(self, user_query: str, model: str = "gpt-3.5-turbo") -> Dict[str, Any]:
        """
        사용자 질문을 용어집 기반으로 표준화
        
        Args:
            user_query: 원본 사용자 질문
            model: 사용할 LLM 모델
            
        Returns:
            표준화 결과
        """
        try:
            print(f"🧠 1단계: 질문 표준화 시작...")
            print(f"   원본 질문: {user_query}")
            
            # 표준화 프롬프트 구성
            prompt = self._build_standardization_prompt(user_query)
            
            # LLM 호출
            response = await self.llm_client.generate_response(
                messages=[{"role": "user", "content": prompt}],
                model=model,
                temperature=0.3
            )
            
            standardized_query = response.strip()
            print(f"   표준화된 질문: {standardized_query}")
            
            return {
                "success": True,
                "original_query": user_query,
                "standardized_query": standardized_query,
                "model_used": model
            }
            
        except Exception as e:
            print(f"❌ 질문 표준화 실패: {e}")
            return {
                "success": False,
                "error": str(e),
                "original_query": user_query,
                "standardized_query": user_query  # 실패시 원본 반환
            }
    
    def _build_standardization_prompt(self, user_query: str) -> str:
        """표준화 프롬프트 구성 (용어집 기반)"""
        
        try:
            # MD 용어집 로드
            vocabulary = vocabulary_extractor.load_vocabulary()
            
            # 모든 용어를 포함 (필터링 제거)
            key_vocabulary = {
                "class_names": vocabulary.get("class_names", [])[:30],
                "method_names": vocabulary.get("method_names", [])[:40], 
                "domain_concepts": vocabulary.get("domain_concepts", [])[:30],
                "korean_terms": vocabulary.get("korean_terms", [])[:20],
                "file_names": vocabulary.get("file_names", [])[:20],
                "framework_terms": vocabulary.get("framework_terms", [])[:15]
            }
            
            prompt = f"""당신은 CodeMuse 코드베이스 전문가입니다. 
사용자 질문을 분석하여 적절한 검색 키워드로 변환하세요.

📌 원본 질문:
{user_query}

📌 사용 가능한 용어집:
{json.dumps(key_vocabulary, ensure_ascii=False, indent=2)}

🎯 변환 규칙:
1. **질문 유형 파악**:
   - "기술스택" → "프로젝트 구조, 언어, 프레임워크"
   - "어디서 수정" → "구체적 파일명과 메서드명"
   - "어떻게 작동" → "관련 클래스와 메서드"
   - "구조는" → "모듈과 아키텍처"

2. **키워드 추출**:
   - 원본 질문의 핵심 키워드 유지
   - 용어집에서 관련 용어 추가
   - 불필요한 변환 금지

3. **의도 보존**: 원래 질문의 핵심 의도와 목적을 그대로 유지

📌 출력 지침:
- 원본 질문의 의도를 그대로 유지
- 관련 키워드만 추가
- 1-2문장으로 간결하게 작성
- 설명 없이 질문만 출력

📌 표준화된 질문:"""
            
            return prompt.strip()
            
        except Exception as e:
            print(f"❌ 용어집 로드 실패: {e}")
            # 폴백: 기본 프롬프트 사용
            return f"""
다음 사용자 질문을 개발/코딩 용어집을 사용하여 표준화된 형태로 변환해주세요.

원본 질문: "{user_query}"

표준화 규칙:
1. 기술적 용어는 정확한 영어 용어로 변환
2. 모호한 표현은 구체적인 기술 용어로 명확화
3. 질문의 의도를 명확하게 표현
4. 한국어와 영어가 섞인 표현은 일관성 있게 정리
5. 코드 관련 질문은 구체적인 기술 스택과 패턴을 명시

표준화된 질문만 출력하세요 (설명 없이):
"""
