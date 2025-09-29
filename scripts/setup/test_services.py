#!/usr/bin/env python3
"""
CodeMuse Backend Services Test Script

모든 서비스의 기본 기능을 테스트합니다.
"""

import asyncio
import httpx
import json
from typing import Dict, Any


class ServiceTester:
    """서비스 테스터"""
    
    def __init__(self):
        self.base_urls = {
            "document_generator": "http://localhost:8002",
            "rag_engine": "http://localhost:8003",
            "llm_chat": "http://localhost:8004", 
            "web_interface": "http://localhost:8005",
            "workflow_orchestrator": "http://localhost:8006"
        }
    
    async def test_all_services(self):
        """모든 서비스 테스트"""
        print("🧪 CodeMuse Backend Services 테스트 시작...\n")
        
        # 헬스 체크 테스트
        await self.test_health_checks()
        
        # 각 서비스별 기능 테스트
        await self.test_document_generator()
        await self.test_rag_engine()
        await self.test_llm_chat()
        await self.test_web_interface()
        await self.test_workflow_orchestrator()
        
        print("✅ 모든 서비스 테스트 완료!")
    
    async def test_health_checks(self):
        """헬스 체크 테스트"""
        print("🏥 헬스 체크 테스트...")
        
        async with httpx.AsyncClient() as client:
            for service_name, base_url in self.base_urls.items():
                try:
                    response = await client.get(f"{base_url}/health", timeout=5.0)
                    if response.status_code == 200:
                        print(f"  ✅ {service_name}: 건강")
                    else:
                        print(f"  ❌ {service_name}: 비정상 ({response.status_code})")
                except Exception as e:
                    print(f"  ❌ {service_name}: 연결 실패 ({str(e)})")
        print()
    
    async def test_document_generator(self):
        """Document Generator 테스트"""
        print("📄 Document Generator 테스트...")
        
        async with httpx.AsyncClient() as client:
            try:
                # 문서 생성 테스트 (현재 디렉터리 분석)
                response = await client.post(
                    f"{self.base_urls['document_generator']}/api/v1/documents/generate",
                    json={
                        "project_path": ".",
                        "output_format": "markdown",
                        "include_metrics": True
                    },
                    timeout=30.0
                )
                
                if response.status_code == 200:
                    data = response.json()
                    print(f"  ✅ 문서 생성 성공: {data['document_id']}")
                else:
                    print(f"  ❌ 문서 생성 실패: {response.status_code}")
                    
            except Exception as e:
                print(f"  ❌ Document Generator 테스트 실패: {str(e)}")
        print()
    
    async def test_rag_engine(self):
        """RAG Engine 테스트"""
        print("🔍 RAG Engine 테스트...")
        
        async with httpx.AsyncClient() as client:
            try:
                # 임베딩 생성 테스트
                response = await client.post(
                    f"{self.base_urls['rag_engine']}/api/v1/embeddings/generate",
                    json={
                        "documents": ["테스트 문서입니다.", "코드 분석에 관한 내용입니다."],
                        "metadata": [{"type": "test"}, {"type": "test"}]
                    },
                    timeout=30.0
                )
                
                if response.status_code == 200:
                    data = response.json()
                    print(f"  ✅ 임베딩 생성 성공: {len(data['embeddings'])}개 문서")
                    
                    # 검색 테스트
                    search_response = await client.post(
                        f"{self.base_urls['rag_engine']}/api/v1/search",
                        json={
                            "query": "코드 분석",
                            "limit": 5
                        },
                        timeout=10.0
                    )
                    
                    if search_response.status_code == 200:
                        search_data = search_response.json()
                        print(f"  ✅ 검색 성공: {search_data['total_results']}개 결과")
                    else:
                        print(f"  ❌ 검색 실패: {search_response.status_code}")
                        
                else:
                    print(f"  ❌ 임베딩 생성 실패: {response.status_code}")
                    
            except Exception as e:
                print(f"  ❌ RAG Engine 테스트 실패: {str(e)}")
        print()
    
    async def test_llm_chat(self):
        """LLM Chat 테스트"""
        print("💬 LLM Chat 테스트...")
        
        async with httpx.AsyncClient() as client:
            try:
                # 세션 생성 테스트
                session_response = await client.post(
                    f"{self.base_urls['llm_chat']}/api/v1/chat/sessions",
                    json={"title": "테스트 세션"},
                    timeout=10.0
                )
                
                if session_response.status_code == 200:
                    session_data = session_response.json()
                    session_id = session_data['id']
                    print(f"  ✅ 세션 생성 성공: {session_id}")
                    
                    # 메시지 전송 테스트 (API 키가 없는 경우 에러 예상)
                    message_response = await client.post(
                        f"{self.base_urls['llm_chat']}/api/v1/chat/sessions/{session_id}/messages",
                        json={
                            "message": "안녕하세요!",
                            "model": "gpt-3.5-turbo"
                        },
                        timeout=30.0
                    )
                    
                    if message_response.status_code == 200:
                        print("  ✅ 메시지 전송 성공")
                    else:
                        print(f"  ⚠️  메시지 전송 실패 (API 키 필요): {message_response.status_code}")
                        
                else:
                    print(f"  ❌ 세션 생성 실패: {session_response.status_code}")
                    
            except Exception as e:
                print(f"  ❌ LLM Chat 테스트 실패: {str(e)}")
        print()
    
    async def test_web_interface(self):
        """Web Interface 테스트"""
        print("🌐 Web Interface 테스트...")
        
        async with httpx.AsyncClient() as client:
            try:
                # 설정 조회 테스트
                config_response = await client.get(
                    f"{self.base_urls['web_interface']}/api/v1/config",
                    timeout=10.0
                )
                
                if config_response.status_code == 200:
                    config_data = config_response.json()
                    print(f"  ✅ 설정 조회 성공: {len(config_data['available_workflows'])}개 워크플로우")
                else:
                    print(f"  ❌ 설정 조회 실패: {config_response.status_code}")
                    
            except Exception as e:
                print(f"  ❌ Web Interface 테스트 실패: {str(e)}")
        print()
    
    async def test_workflow_orchestrator(self):
        """Workflow Orchestrator 테스트"""
        print("🎭 Workflow Orchestrator 테스트...")
        
        async with httpx.AsyncClient() as client:
            try:
                # 워크플로우 타입 조회 테스트
                types_response = await client.get(
                    f"{self.base_urls['workflow_orchestrator']}/api/v1/orchestrator/workflow-types",
                    timeout=10.0
                )
                
                if types_response.status_code == 200:
                    types_data = types_response.json()
                    print(f"  ✅ 워크플로우 타입 조회 성공: {len(types_data['workflow_types'])}개 타입")
                else:
                    print(f"  ❌ 워크플로우 타입 조회 실패: {types_response.status_code}")
                    
            except Exception as e:
                print(f"  ❌ Workflow Orchestrator 테스트 실패: {str(e)}")
        print()


async def main():
    """메인 함수"""
    tester = ServiceTester()
    await tester.test_all_services()


if __name__ == "__main__":
    asyncio.run(main())
