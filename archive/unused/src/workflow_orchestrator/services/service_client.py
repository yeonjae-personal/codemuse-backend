"""
Service Client

다른 서비스들과의 통신 클라이언트
"""

import httpx
import time
from typing import Dict, Any, Optional
from datetime import datetime


class ServiceClient:
    """서비스 클라이언트"""
    
    def __init__(self):
        self.service_urls = {
            "document_generator": "http://localhost:8002",
            "rag_engine": "http://localhost:8003",
            "llm_chat": "http://localhost:8004",
            "web_interface": "http://localhost:8005"
        }
        self.timeout = 30.0
    
    async def call_service(self, service_name: str, endpoint: str, method: str = "POST", 
                          data: Optional[Dict[str, Any]] = None) -> Dict[str, Any]:
        """서비스 호출"""
        if service_name not in self.service_urls:
            raise ValueError(f"지원하지 않는 서비스: {service_name}")
        
        url = f"{self.service_urls[service_name]}{endpoint}"
        
        try:
            async with httpx.AsyncClient(timeout=self.timeout) as client:
                if method.upper() == "GET":
                    response = await client.get(url)
                elif method.upper() == "POST":
                    response = await client.post(url, json=data)
                elif method.upper() == "PUT":
                    response = await client.put(url, json=data)
                elif method.upper() == "DELETE":
                    response = await client.delete(url)
                else:
                    raise ValueError(f"지원하지 않는 HTTP 메서드: {method}")
                
                if response.status_code == 200:
                    return response.json()
                else:
                    raise Exception(f"서비스 응답 오류: {response.status_code} - {response.text}")
                    
        except httpx.TimeoutException:
            raise Exception(f"서비스 호출 타임아웃: {service_name}")
        except httpx.ConnectError:
            raise Exception(f"서비스 연결 실패: {service_name}")
        except Exception as e:
            raise Exception(f"서비스 호출 오류: {str(e)}")
    
    async def health_check(self, service_name: str) -> bool:
        """서비스 헬스 체크"""
        try:
            await self.call_service(service_name, "/health", "GET")
            return True
        except:
            return False
    
    async def health_check_all(self) -> Dict[str, bool]:
        """모든 서비스 헬스 체크"""
        health_status = {}
        for service_name in self.service_urls.keys():
            health_status[service_name] = await self.health_check(service_name)
        return health_status
