"""
Document Analysis Models
"""

from typing import List, Dict, Any, Optional
from pydantic import BaseModel
from datetime import datetime


class CodeStructure(BaseModel):
    """코드 구조 정보"""
    file_path: str
    language: str
    functions: List[Dict[str, Any]]
    classes: List[Dict[str, Any]]
    imports: List[str]
    exports: List[str]
    complexity_score: float
    lines_of_code: int
    dependencies: List[str]


class DependencyGraph(BaseModel):
    """의존성 그래프"""
    nodes: List[str]  # 파일 경로들
    edges: List[Dict[str, str]]  # 의존성 관계
    cycles: List[List[str]]  # 순환 의존성


class DocumentAnalysis(BaseModel):
    """문서 분석 결과"""
    id: str
    project_name: str
    analysis_timestamp: datetime
    total_files: int
    total_lines: int
    languages: Dict[str, int]
    code_structures: List[CodeStructure]
    dependency_graph: DependencyGraph
    issues: List[Dict[str, Any]]
    metrics: Dict[str, Any]
