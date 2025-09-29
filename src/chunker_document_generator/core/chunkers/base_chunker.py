"""
언어별 Chunker의 공통 인터페이스
"""

from abc import ABC, abstractmethod
from typing import List, Dict, Any, Optional
from dataclasses import dataclass


@dataclass
class CodeChunk:
    """코드 chunk를 나타내는 데이터 클래스 (공통)"""
    
    # 기본 식별 정보
    chunk_id: str
    file_path: str
    chunk_type: str  # function, method, class, overview, component, etc.
    name: str
    
    # 코드 위치 정보
    line_range: str  # "12-87"
    content: str     # 실제 코드 내용
    
    # 메타데이터
    parent: Optional[str] = None  # 클래스 내 메서드의 경우 클래스명
    base_classes: List[str] = None
    decorators: List[str] = None
    tags: List[str] = None
    complexity: Optional[int] = None
    docstring: Optional[str] = None
    dependencies: List[str] = None
    is_async: bool = False
    is_generator: bool = False
    token_count: Optional[int] = None
    
    # 언어별 특화 메타데이터
    language: str = "unknown"  # python, javascript, java, vue
    framework: Optional[str] = None  # vue, react, spring, etc.
    
    # 추가 메타 (시그니처/입출력/예외/호출/데이터플로우)
    signature: Optional[str] = None
    parameters: Optional[List[str]] = None
    returns: Optional[str] = None
    raises: Optional[List[str]] = None
    calls: Optional[List[str]] = None
    data_flow_summary: Optional[List[Dict[str, str]]] = None  # [{op, src, dst, line}]
    data_flow_stats: Optional[Dict[str, int]] = None
    code_hash: Optional[str] = None
    
    def __post_init__(self):
        if self.base_classes is None:
            self.base_classes = []
        if self.decorators is None:
            self.decorators = []
        if self.tags is None:
            self.tags = []
        if self.dependencies is None:
            self.dependencies = []
        if self.parameters is None:
            self.parameters = []
        if self.raises is None:
            self.raises = []
        if self.calls is None:
            self.calls = []
        if self.data_flow_summary is None:
            self.data_flow_summary = []
        if self.data_flow_stats is None:
            self.data_flow_stats = {}


class BaseChunker(ABC):
    """언어별 Chunker의 공통 인터페이스"""
    
    def __init__(self, max_tokens: int = 600):
        self.max_tokens = max_tokens
        self.supported_extensions = self._get_supported_extensions()
    
    @abstractmethod
    def _get_supported_extensions(self) -> List[str]:
        """지원하는 파일 확장자 목록 반환"""
        pass
    
    @abstractmethod
    def chunk_file(self, file_path: str) -> List[CodeChunk]:
        """파일을 처리하여 코드 청크들을 생성"""
        pass
    
    def can_process(self, file_path: str) -> bool:
        """파일을 처리할 수 있는지 확인"""
        return any(file_path.endswith(ext) for ext in self.supported_extensions)
    
    def _generate_chunk_id(self, file_path: str, chunk_type: str, name: str) -> str:
        """고유한 chunk ID 생성"""
        import hashlib
        content = f"{file_path}:{chunk_type}:{name}"
        return hashlib.md5(content.encode()).hexdigest()[:12]
    
    def _extract_docstring(self, content: str, start_line: int = 0) -> str:
        """언어별 독스트링 추출 (기본 구현)"""
        # 각 언어별로 오버라이드
        return ""
    
    def _calculate_complexity(self, content: str) -> int:
        """복잡도 계산 (기본 구현)"""
        # 간단한 라인 수 기반 복잡도
        lines = content.split('\n')
        return max(1, len([line for line in lines if line.strip() and not line.strip().startswith('//')]))
