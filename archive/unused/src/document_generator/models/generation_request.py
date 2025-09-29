"""
Generation Request/Response Models
"""

from typing import List, Optional, Dict, Any
from pydantic import BaseModel


class GenerationRequest(BaseModel):
    """문서 생성 요청"""
    project_path: str
    output_format: str = "markdown"
    include_metrics: bool = True
    include_dependencies: bool = True
    include_issues: bool = True
    template_type: str = "standard"
    generate_individual_files: bool = False


class GenerationResponse(BaseModel):
    """문서 생성 응답"""
    document_id: str
    status: str
    generated_content: str
    file_path: str
    generation_time: float
    metadata: Dict[str, Any]
