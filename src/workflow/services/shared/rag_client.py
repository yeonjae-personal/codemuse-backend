"""
RAG 클라이언트 모듈
"""

import asyncio
import httpx
from typing import Dict, Any, List, Optional
import os


class RAGClient:
    """RAG 클라이언트"""
    
    def __init__(self, base_url: str = None):
        self.base_url = base_url or os.getenv("RAG_SERVICE_URL", "http://localhost:8003")
        
    async def search_code(self, query: str, limit: int = 10) -> List[Dict[str, Any]]:
        """소스코드 검색"""
        try:
            async with httpx.AsyncClient(timeout=15.0) as client:
                response = await client.get(
                    f"{self.base_url}/api/v1/code/search",
                    params={"query": query, "limit": limit}
                )
                
                if response.status_code == 200:
                    data = response.json()
                    return data.get("results", [])
                else:
                    print(f"소스코드 검색 실패: {response.status_code}")
                    return []
                    
        except Exception as e:
            print(f"소스코드 검색 오류: {e}")
            return []
    
    async def search_docstrings(self, query: str, limit: int = 10) -> List[Dict[str, Any]]:
        """독스트링 검색"""
        try:
            async with httpx.AsyncClient(timeout=15.0) as client:
                response = await client.post(
                    f"{self.base_url}/api/v1/search/docstrings",
                    json={"query": query, "limit": limit}
                )
                
                if response.status_code == 200:
                    data = response.json()
                    return data.get("results", [])
                else:
                    print(f"독스트링 검색 실패: {response.status_code}")
                    return []
                    
        except Exception as e:
            print(f"독스트링 검색 오류: {e}")
            return []
    
    async def search_documents(self, query: str, limit: int = 10) -> List[Dict[str, Any]]:
        """MD 문서 검색"""
        try:
            async with httpx.AsyncClient(timeout=15.0) as client:
                response = await client.post(
                    f"{self.base_url}/api/v1/search",
                    json={
                        "query": query,
                        "limit": limit,
                        "chunk_type_filter": "overview|function|class"
                    }
                )
                
                if response.status_code == 200:
                    data = response.json()
                    return data.get("results", [])
                else:
                    print(f"MD 문서 검색 실패: {response.status_code}")
                    return []
                    
        except Exception as e:
            print(f"MD 문서 검색 오류: {e}")
            return []
    
    async def search_general(self, query: str, limit: int = 10) -> List[Dict[str, Any]]:
        """일반 RAG 검색"""
        try:
            async with httpx.AsyncClient(timeout=15.0) as client:
                response = await client.post(
                    f"{self.base_url}/api/v1/search",
                    json={"query": query, "limit": limit}
                )
                
                if response.status_code == 200:
                    data = response.json()
                    return data.get("results", [])
                else:
                    print(f"일반 검색 실패: {response.status_code}")
                    return []
                    
        except Exception as e:
            print(f"일반 검색 오류: {e}")
            return []
