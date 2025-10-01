"""
1단계: 질문 표준화 모듈
용어집 기반으로 사용자 질문을 표준화합니다.
"""

import asyncio
import httpx
import json
import logging
from typing import Dict, Any, Optional
from ..shared.llm_client import LLMClient
from ..vocabulary_extractor import vocabulary_extractor

logger = logging.getLogger("workflow.question_standardizer")


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
            logger.info(f"🧠 1단계: 질문 표준화 시작")
            logger.info(f"   원본 질문: {user_query}")
            
            # 표준화 프롬프트 구성
            prompt = self._build_standardization_prompt(user_query)
            
            # LLM 호출
            response = await self.llm_client.generate_response(
                messages=[{"role": "user", "content": prompt}],
                model=model,
                temperature=0.3
            )
            
            standardized_query = response.strip()
            logger.info(f"   표준화된 질문: {standardized_query}")
            
            return {
                "success": True,
                "original_query": user_query,
                "standardized_query": standardized_query,
                "model_used": model
            }
            
        except Exception as e:
            logger.error(f"❌ 질문 표준화 실패: {e}", exc_info=True)
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
            
            prompt = f"""당신은 RAG 검색을 위한 질문 표준화 전문가입니다.
사용자 질문을 검색에 최적화된 형태로 변환하세요.

📌 원본 질문:
{user_query}

📌 프로젝트 용어집:
{json.dumps(key_vocabulary, ensure_ascii=False, indent=2)}

🎯 표준화 규칙:

1. **핵심 의도 유지**:
   - 원본 질문의 요청 의도를 그대로 유지
   - "알려줘/설명해줘" → "정보" 또는 "설명"으로 간결하게 변환
   - "어디서 수정" → "수정 위치" 또는 "파일 위치"
   - "어떻게 작동" → "동작 원리" 또는 "구현 방식"

2. **키워드 정제**:
   - 용어집에 있는 정확한 용어로 매핑
   - 검색에 방해되는 조사/어미 제거
   - 중복 의미 제거 (예: "관련 api" → "API")

3. **검색 최적화**:
   - 명사형 키워드 중심으로 구성
   - 불필요한 문장 구조 제거
   - 핵심 개념을 명확히 표현

4. **범위 보존**:
   - 원본의 검색 범위를 축소하지 않음
   - "관련"이라는 표현은 유지

📌 출력 형식:
- 검색 키워드로 적합한 간결한 문장
- 원본 의도를 정확히 반영
- 1문장으로 작성
- 설명 없이 표준화된 질문만 출력

📌 예시:
- "종속관계 관련 api와 테이블 알려줘" → "종속관계 관련 API와 테이블 정보"
- "이슈 탐지 어떻게 작동해?" → "이슈 탐지 동작 원리"
- "어디서 수정해야 해?" → "수정 위치 및 파일"
- "어떤 프로젝트에 대한 소스인가요?" → "분석 대상 프로젝트명과 목적"
- "이 코드는 어떤 프로젝트야?" → "프로젝트 이름과 도메인"

📌 표준화된 질문:"""
            
            return prompt.strip()
            
        except Exception as e:
            logger.error(f"❌ 용어집 로드 실패: {e}", exc_info=True)
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
