"""
Document Generator Models
"""

from .document_analysis import DocumentAnalysis, CodeStructure, DependencyGraph
from .generation_request import GenerationRequest, GenerationResponse

__all__ = [
    "DocumentAnalysis",
    "CodeStructure", 
    "DependencyGraph",
    "GenerationRequest",
    "GenerationResponse"
]
