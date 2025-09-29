"""
워크플로우 오케스트레이터
5개 모듈을 조율하여 최적화된 워크플로우를 실행합니다.
"""

import time
from typing import Dict, Any
from .modules import (
    QuestionStandardizer,
    KeywordExtractor, 
    RAGSearcher,
    QualityReviewer,
    ResponseGenerator
)
from .shared import LLMClient, RAGClient
from .shared.models import WorkflowRequest, WorkflowResponse


class WorkflowOrchestrator:
    """워크플로우 오케스트레이터"""
    
    def __init__(self, llm_base_url: str = None, rag_base_url: str = None):
        # 클라이언트 초기화
        self.llm_client = LLMClient(llm_base_url)
        self.rag_client = RAGClient(rag_base_url)
        
        # 모듈 초기화
        self.standardizer = QuestionStandardizer(self.llm_client)
        self.keyword_extractor = KeywordExtractor(self.llm_client)
        self.rag_searcher = RAGSearcher(self.rag_client)
        self.quality_reviewer = QualityReviewer(self.llm_client)
        self.response_generator = ResponseGenerator(self.llm_client)
    
    async def process_workflow(self, request: WorkflowRequest, status_callback=None) -> WorkflowResponse:
        """
        최적화된 5단계 워크플로우 실행
        
        1. 사용자 질문을 LLM이 용어집 기반으로 표준화
        2. 표준화된 질문에서 키워드 추출
        3. 표준화된 질문+키워드로 RAG 검색 (소스검색+독스트링검색+MD검색)
        4. 검색된 RAG 결과가 잘된건지 LLM 검토 및 품질 개선
        5. 개선된 RAG 데이터로 최종 답변 생성
        
        Args:
            request: 워크플로우 요청
            
        Returns:
            워크플로우 응답
        """
        start_time = time.time()
        
        try:
            print(f"🚀 최적화된 워크플로우 시작")
            print(f"   원본 질문: {request.query}")
            print(f"   모델: {request.model}")
            print(f"   RAG 사용: {request.use_rag}")
            
            # 1단계: 질문 표준화
            if status_callback:
                await status_callback("Step 1: Question standardization in progress...", "1")
            step1_start = time.time()
            standardization_result = await self.standardizer.standardize(
                request.query, request.model
            )
            step1_time = time.time() - step1_start
            
            if not standardization_result["success"]:
                return WorkflowResponse(
                    success=False,
                    response="질문 표준화에 실패했습니다.",
                    original_query=request.query,
                    model_used=request.model,
                    processing_time=time.time() - start_time,
                    error=standardization_result.get("error")
                )
            
            standardized_query = standardization_result["standardized_query"]
            print(f"✅ 1단계 완료 [⏱️ {step1_time:.2f}초]")
            
            # 2단계: 키워드 추출
            if status_callback:
                await status_callback("Step 2: Keyword extraction in progress...", "2")
            step2_start = time.time()
            keyword_result = await self.keyword_extractor.extract(
                standardized_query, request.model
            )
            step2_time = time.time() - step2_start
            
            if not keyword_result["success"]:
                keywords = []
            else:
                keywords = keyword_result["keywords"]
            
            print(f"✅ 2단계 완료 [⏱️ {step2_time:.2f}초]")
            
            # 3단계: RAG 검색
            if status_callback:
                await status_callback("Step 3: RAG search in progress...", "3")
            step3_start = time.time()
            if request.use_rag:
                search_result = await self.rag_searcher.search(
                    standardized_query, keywords, limit=10
                )
                search_results = search_result.get("results", [])
            else:
                search_results = []
            step3_time = time.time() - step3_start
            
            print(f"✅ 3단계 완료 [⏱️ {step3_time:.2f}초] - {len(search_results)}개 결과")
            
            # 4단계: 품질 검토
            if status_callback:
                await status_callback("Step 4: Quality review in progress...", "4")
            step4_start = time.time()
            if search_results:
                quality_result = await self.quality_reviewer.review(
                    search_results, request.query, request.model
                )
                reviewed_results = quality_result.get("reviewed_results", search_results)
                quality_score = quality_result.get("quality_score", 0.0)
            else:
                reviewed_results = []
                quality_score = 0.0
            step4_time = time.time() - step4_start
            
            print(f"✅ 4단계 완료 [⏱️ {step4_time:.2f}초] - 품질 점수: {quality_score:.2f}")
            
            # 5단계: 응답 생성
            if status_callback:
                await status_callback("Step 5: Final response generation in progress...", "5")
            step5_start = time.time()
            response_result = await self.response_generator.generate(
                request.query, reviewed_results, request.model
            )
            step5_time = time.time() - step5_start
            
            if not response_result["success"]:
                return WorkflowResponse(
                    success=False,
                    response="응답 생성에 실패했습니다.",
                    original_query=request.query,
                    standardized_query=standardized_query,
                    keywords=keywords,
                    search_results_count=len(search_results),
                    quality_score=quality_score,
                    model_used=request.model,
                    processing_time=time.time() - start_time,
                    error=response_result.get("error")
                )
            
            final_response = response_result["response"]
            print(f"✅ 5단계 완료 [⏱️ {step5_time:.2f}초]")
            
            # 전체 처리 시간
            total_time = time.time() - start_time
            
            print(f"🎯 워크플로우 완료 [⏱️ 총 {total_time:.2f}초]")
            print(f"   단계별 시간:")
            print(f"     1. 표준화: {step1_time:.2f}초 ({step1_time/total_time*100:.1f}%)")
            print(f"     2. 키워드: {step2_time:.2f}초 ({step2_time/total_time*100:.1f}%)")
            print(f"     3. 검색: {step3_time:.2f}초 ({step3_time/total_time*100:.1f}%)")
            print(f"     4. 검토: {step4_time:.2f}초 ({step4_time/total_time*100:.1f}%)")
            print(f"     5. 응답: {step5_time:.2f}초 ({step5_time/total_time*100:.1f}%)")
            
            return WorkflowResponse(
                success=True,
                response=final_response,
                original_query=request.query,
                standardized_query=standardized_query,
                keywords=keywords,
                search_results_count=len(search_results),
                quality_score=quality_score,
                model_used=request.model,
                processing_time=total_time
            )
            
        except Exception as e:
            print(f"❌ 워크플로우 실행 실패: {e}")
            return WorkflowResponse(
                success=False,
                response="워크플로우 실행 중 오류가 발생했습니다.",
                original_query=request.query,
                model_used=request.model,
                processing_time=time.time() - start_time,
                error=str(e)
            )
