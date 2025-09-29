"""
2단계: 키워드 추출 모듈
표준화된 질문에서 검색에 필요한 키워드를 추출합니다.
"""

from typing import Dict, Any, List
from ..shared.llm_client import LLMClient


class KeywordExtractor:
    """키워드 추출 모듈"""
    
    def __init__(self, llm_client: LLMClient):
        self.llm_client = llm_client
        
    async def extract(self, standardized_query: str, model: str = "gpt-3.5-turbo") -> Dict[str, Any]:
        """
        표준화된 질문에서 키워드 추출
        
        Args:
            standardized_query: 표준화된 질문
            model: 사용할 LLM 모델
            
        Returns:
            키워드 추출 결과
        """
        try:
            print(f"🔍 2단계: 키워드 추출 시작...")
            print(f"   표준화된 질문: {standardized_query}")
            
            # 키워드 추출 프롬프트 구성
            prompt = self._build_keyword_extraction_prompt(standardized_query)
            
            # LLM 호출
            response = await self.llm_client.generate_response(
                messages=[{"role": "user", "content": prompt}],
                model=model,
                temperature=0.2
            )
            
            # 키워드 파싱
            keywords = self._parse_keywords(response)
            print(f"   추출된 키워드: {keywords}")
            
            return {
                "success": True,
                "keywords": keywords,
                "query": standardized_query,
                "model_used": model
            }
            
        except Exception as e:
            print(f"❌ 키워드 추출 실패: {e}")
            return {
                "success": False,
                "error": str(e),
                "keywords": [],
                "query": standardized_query
            }
    
    def _build_keyword_extraction_prompt(self, query: str) -> str:
        """키워드 추출 프롬프트 구성"""
        
        prompt = f"""
다음 질문에서 코드 검색에 필요한 핵심 키워드를 추출하세요.

질문: "{query}"

추출 우선순위:
1. **클래스명** (대문자로 시작하는 클래스명)
2. **메서드명** (언더스코어가 포함된 함수/메서드명)  
3. **파일명** (확장자가 포함된 파일명)
4. **기술 용어** (프레임워크, 라이브러리, 도구명)
5. **일반 용어** (기능, 동작, 개념 관련 용어)

추출 규칙:
- 질문의 핵심 의도와 관련된 키워드 우선 추출
- 기술스택 질문의 경우: 언어, 프레임워크, 도구명 우선
- 코드 수정 질문의 경우: 클래스명, 메서드명, 파일명 우선
- 구체적 질문의 경우: 관련 기술 용어 우선
- 최대 8개 키워드 추출

JSON 형태로 출력:
{{"keywords": ["키워드1", "키워드2", "키워드3"]}}
"""
        return prompt.strip()
    
    def _parse_keywords(self, response: str) -> List[str]:
        """LLM 응답에서 키워드 파싱"""
        try:
            import json
            import re
            
            # JSON 부분만 추출
            json_match = re.search(r'\{.*\}', response, re.DOTALL)
            if json_match:
                data = json.loads(json_match.group())
                keywords = data.get("keywords", [])
                
                # 키워드 정리
                cleaned_keywords = []
                for keyword in keywords:
                    if isinstance(keyword, str):
                        # 특수문자 제거 및 정리
                        cleaned = keyword.strip().replace('"', '').replace("'", "")
                        if cleaned and len(cleaned) > 1:
                            cleaned_keywords.append(cleaned)
                
                return cleaned_keywords[:10]  # 최대 10개
            
            # JSON 파싱 실패시 단순 텍스트 파싱
            lines = response.split('\n')
            keywords = []
            for line in lines:
                line = line.strip()
                if line and not line.startswith('#') and not line.startswith('//'):
                    # 키워드로 보이는 단어들 추출
                    words = line.split()
                    for word in words:
                        word = word.strip('.,!?;:"()[]{}')
                        if word and len(word) > 2:
                            keywords.append(word)
            
            return keywords[:10]
            
        except Exception as e:
            print(f"키워드 파싱 오류: {e}")
            return []
