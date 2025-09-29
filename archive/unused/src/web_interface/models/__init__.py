"""
Web Interface Models
"""

from .workflow_request import WorkflowRequest, WorkflowResponse
from .integration_request import IntegrationRequest, IntegrationResponse

__all__ = [
    "WorkflowRequest",
    "WorkflowResponse",
    "IntegrationRequest", 
    "IntegrationResponse"
]
