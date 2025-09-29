"""
3단계: RAG 검색 모듈
표준화된 질문과 키워드로 RAG 검색을 수행합니다.
"""

import asyncio
import httpx
from typing import Dict, Any, List, Optional
from ..shared.rag_client import RAGClient


class RAGSearcher:
    """RAG 검색 모듈"""
    
    def __init__(self, rag_client: RAGClient):
        self.rag_client = rag_client
        
    async def search(self, standardized_query: str, keywords: List[str], 
                    limit: int = 10) -> Dict[str, Any]:
        """
        표준화된 질문과 키워드로 RAG 검색 수행
        
        Args:
            standardized_query: 표준화된 질문
            keywords: 추출된 키워드
            limit: 검색 결과 제한
            
        Returns:
            검색 결과
        """
        try:
            print(f"🔍 3단계: RAG 검색 시작...")
            print(f"   검색 쿼리: {standardized_query}")
            print(f"   키워드: {keywords}")
            
            # 검색 전략 결정
            search_strategy = self._determine_search_strategy(standardized_query, keywords)
            print(f"   검색 전략: {search_strategy}")
            
            # 다단계 검색 수행
            search_results = await self._perform_multi_stage_search(
                standardized_query, keywords, search_strategy, limit
            )
            
            print(f"   검색 결과: {len(search_results)}개 문서")
            
            return {
                "success": True,
                "results": search_results,
                "query": standardized_query,
                "keywords": keywords,
                "strategy": search_strategy,
                "total_found": len(search_results)
            }
            
        except Exception as e:
            print(f"❌ RAG 검색 실패: {e}")
            return {
                "success": False,
                "error": str(e),
                "results": [],
                "query": standardized_query
            }
    
    def _determine_search_strategy(self, query: str, keywords: List[str]) -> str:
        """검색 전략 결정"""
        
        query_lower = query.lower()
        
        # 구체적 질문인지 판단
        if any(word in query_lower for word in ['어떻게', 'how', '방법', '구현', '코드']):
            return "specific_code"
        elif any(word in query_lower for word in ['무엇', 'what', '뭐', '이게', '이것', '기술', '스택', '프로젝트', '구조']):
            return "overview"
        elif any(word in query_lower for word in ['왜', 'why', '이유', '원인']):
            return "explanation"
        else:
            return "general"
    
    async def _perform_multi_stage_search(self, query: str, keywords: List[str], 
                                        strategy: str, limit: int) -> List[Dict]:
        """다단계 검색 수행"""
        
        all_results = []
        
        # 1단계: 구체적 클래스/메서드명으로 직접 검색 (최우선)
        # 기술스택 질문의 경우 전체 프로젝트 검색을 위해 조건 완화
        if any(keyword in query.lower() for keyword in ['detect', 'analyze', 'check', '수정', '검출', '기술', '스택', '프로젝트', '구조']):
            specific_results = await self._search_specific_methods(keywords, limit//2)
            all_results.extend(specific_results)
        
        # 2단계: 소스코드 직접 검색 (구체적 질문인 경우 + 기술스택 질문)
        if strategy == "specific_code" or any(word in query.lower() for word in ['기술', '스택', '프로젝트', '구조']):
            source_results = await self._search_source_code(query, keywords, limit//3)
            all_results.extend(source_results)
        
        # 3단계: 독스트링 검색
        docstring_results = await self._search_docstrings(query, keywords, limit//4)
        all_results.extend(docstring_results)
        
        # 4단계: MD 문서 검색 (보조적)
        md_results = await self._search_md_documents(query, keywords, limit//5)
        all_results.extend(md_results)
        
        # 5단계: 기술스택 특별 검색 (기술스택 질문인 경우)
        if any(word in query.lower() for word in ['기술', '스택', '프로젝트', '구조']):
            tech_stack_results = await self._search_tech_stack(query, keywords, limit//3)
            all_results.extend(tech_stack_results)
        
        # 6단계: 일반 RAG 검색 (최후)
        if len(all_results) < limit//2:  # 결과가 부족한 경우에만
            general_results = await self._search_general(query, keywords, limit//2)
            all_results.extend(general_results)
        
        # 중복 제거 및 점수 기반 정렬
        unique_results = self._deduplicate_and_rank(all_results)
        
        return unique_results[:limit]
    
    async def _search_specific_methods(self, keywords: List[str], limit: int) -> List[Dict]:
        """구체적 메서드명으로 직접 검색"""
        try:
            # 실제 코드베이스의 정확한 정보를 찾기 위한 검색 전략
            search_terms = []
            for keyword in keywords:
                # 대문자로 시작하는 클래스명
                if keyword[0].isupper():
                    search_terms.append(keyword)
                # 언더스코어가 포함된 메서드명/파일명
                elif '_' in keyword:
                    search_terms.append(keyword)
                # 확장자가 포함된 파일명
                elif any(ext in keyword for ext in ['.py', '.js', '.vue', '.java', '.css']):
                    search_terms.append(keyword)
                # 기술 용어 (프레임워크, 라이브러리, 도구명)
                elif any(term in keyword.lower() for term in ['framework', 'library', 'tool', 'service', 'api']):
                    search_terms.append(keyword)
                # 일반적인 기술 용어
                else:
                    search_terms.append(keyword)
            
            # 다중 검색 전략 적용
            all_results = []
            
            # 1. 정확한 키워드 조합으로 검색
            if search_terms:
                search_query = ' '.join(search_terms[:3])
                print(f"   구체적 메서드 검색: {search_query}")
                results = await self.rag_client.search_code(search_query, limit)
                all_results.extend(results)
            
            # 2. 개별 키워드로도 검색 (더 넓은 범위)
            for term in search_terms[:2]:  # 상위 2개만
                individual_results = await self.rag_client.search_code(term, limit//2)
                all_results.extend(individual_results)
            
            # 3. 실제 파일명 우선 검색
            file_keywords = [kw for kw in keywords if '.py' in kw]
            if file_keywords:
                for file_kw in file_keywords:
                    file_results = await self.rag_client.search_code(file_kw, limit//3)
                    all_results.extend(file_results)
            
            # 중복 제거 및 점수 기반 정렬
            unique_results = self._deduplicate_and_rank(all_results)
            return unique_results[:limit]
            
        except Exception as e:
            print(f"구체적 메서드 검색 오류: {e}")
            return []
    
    async def _search_source_code(self, query: str, keywords: List[str], limit: int) -> List[Dict]:
        """소스코드 직접 검색"""
        try:
            search_query = f"{query} {' '.join(keywords[:3])}"
            return await self.rag_client.search_code(search_query, limit)
        except Exception as e:
            print(f"소스코드 검색 오류: {e}")
            return []
    
    async def _search_docstrings(self, query: str, keywords: List[str], limit: int) -> List[Dict]:
        """독스트링 검색"""
        try:
            search_query = f"{query} {' '.join(keywords[:3])}"
            return await self.rag_client.search_docstrings(search_query, limit)
        except Exception as e:
            print(f"독스트링 검색 오류: {e}")
            return []
    
    async def _search_md_documents(self, query: str, keywords: List[str], limit: int) -> List[Dict]:
        """MD 문서 검색"""
        try:
            search_query = f"{query} {' '.join(keywords[:3])}"
            return await self.rag_client.search_documents(search_query, limit)
        except Exception as e:
            print(f"MD 문서 검색 오류: {e}")
            return []
    
    async def _search_tech_stack(self, query: str, keywords: List[str], limit: int) -> List[Dict]:
        """기술스택 특별 검색"""
        try:
            # 기술스택 관련 검색어 조합
            tech_terms = ['python', 'fastapi', 'vue', 'javascript', 'docker', 'rag', 'llm', 'framework', 'library']
            search_terms = keywords + tech_terms[:3]  # 키워드 + 기술 용어
            
            search_query = f"{query} {' '.join(search_terms)}"
            print(f"   기술스택 검색: {search_query}")
            
            # 다양한 검색 전략 시도
            all_results = []
            
            # 1. 일반 검색
            general_results = await self.rag_client.search_general(search_query, limit//2)
            all_results.extend(general_results)
            
            # 2. 소스코드 검색 (프레임워크 관련)
            for term in ['fastapi', 'vue', 'python', 'docker']:
                code_results = await self.rag_client.search_code(term, limit//4)
                all_results.extend(code_results)
            
            return all_results
            
        except Exception as e:
            print(f"기술스택 검색 오류: {e}")
            return []
    
    async def _search_general(self, query: str, keywords: List[str], limit: int) -> List[Dict]:
        """일반 RAG 검색"""
        try:
            search_query = f"{query} {' '.join(keywords[:5])}"
            return await self.rag_client.search_general(search_query, limit)
        except Exception as e:
            print(f"일반 검색 오류: {e}")
            return []
    
    def _deduplicate_and_rank(self, results: List[Dict]) -> List[Dict]:
        """중복 제거 및 점수 기반 정렬"""
        
        # 중복 제거 (content 기준)
        seen_contents = set()
        unique_results = []
        
        for result in results:
            content = result.get('content', '')
            if content not in seen_contents:
                seen_contents.add(content)
                unique_results.append(result)
        
        # 점수 기반 정렬
        unique_results.sort(key=lambda x: x.get('score', 0), reverse=True)
        
        return unique_results
