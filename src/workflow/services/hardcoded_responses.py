"""
하드코딩된 정확한 답변 서비스
프로토타입용으로 정해진 질문들에 대해 정확한 MD 문서를 직접 제공합니다.
"""

import os
from typing import Optional, Dict, Any
from pathlib import Path


class HardcodedResponseService:
    """하드코딩된 응답 서비스"""
    
    def __init__(self):
        # 프로젝트 루트 디렉토리
        self.project_root = Path(__file__).parent.parent.parent.parent
        self.generated_docs_path = self.project_root / "generated_docs"
        
        # 질문 패턴과 MD 문서 매핑
        self.question_patterns = {
            # 프로젝트 관련 질문
            "프로젝트": {
                "keywords": ["프로젝트", "어떤", "소스", "코드", "다루고"],
                "document": "project_summary.md",
                "description": "프로젝트 전체 개요 및 구조"
            },
            # 종속관계 관련 질문
            "종속관계": {
                "keywords": ["종속관계", "dependency", "의존성", "관계"],
                "document": "vizier_dependency_relationships.md", 
                "description": "Vizier 프로젝트의 종속관계 및 리더-팔로우 관계"
            },
            # API/테이블 관련 질문
            "api_테이블": {
                "keywords": ["api", "테이블", "table", "데이터베이스", "database", "엔드포인트"],
                "document": "vizier_dependency_relationships.md",
                "description": "종속관계 관련 API 및 데이터베이스 테이블"
            }
        }
    
    def is_hardcoded_question(self, standardized_query: str) -> bool:
        """
        표준화된 질문이 하드코딩된 질문인지 확인
        
        Args:
            standardized_query: 표준화된 질문
            
        Returns:
            하드코딩된 질문인지 여부
        """
        query_lower = standardized_query.lower()
        
        for pattern_name, pattern_info in self.question_patterns.items():
            for keyword in pattern_info["keywords"]:
                if keyword.lower() in query_lower:
                    return True
        return False
    
    def get_hardcoded_document(self, standardized_query: str) -> Optional[Dict[str, Any]]:
        """
        표준화된 질문에 해당하는 하드코딩된 문서 반환
        
        Args:
            standardized_query: 표준화된 질문
            
        Returns:
            하드코딩된 문서 정보 또는 None
        """
        query_lower = standardized_query.lower()
        
        for pattern_name, pattern_info in self.question_patterns.items():
            for keyword in pattern_info["keywords"]:
                if keyword.lower() in query_lower:
                    document_path = self.generated_docs_path / pattern_info["document"]
                    
                    if document_path.exists():
                        try:
                            with open(document_path, 'r', encoding='utf-8') as f:
                                content = f.read()
                            
                            return {
                                "success": True,
                                "content": content,
                                "metadata": {
                                    "source": "hardcoded",
                                    "document": pattern_info["document"],
                                    "description": pattern_info["description"],
                                    "pattern": pattern_name
                                },
                                "search_results_count": 1
                            }
                        except Exception as e:
                            print(f"❌ 하드코딩 문서 읽기 실패: {e}")
                            return None
                    else:
                        print(f"❌ 하드코딩 문서 없음: {document_path}")
                        return None
        
        return None
    
    def get_question_type(self, standardized_query: str) -> Optional[str]:
        """
        표준화된 질문의 타입 반환
        
        Args:
            standardized_query: 표준화된 질문
            
        Returns:
            질문 타입 또는 None
        """
        query_lower = standardized_query.lower()
        
        for pattern_name, pattern_info in self.question_patterns.items():
            for keyword in pattern_info["keywords"]:
                if keyword.lower() in query_lower:
                    return pattern_name
        return None
