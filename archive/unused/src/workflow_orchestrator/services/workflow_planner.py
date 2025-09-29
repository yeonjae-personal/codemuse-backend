"""
Workflow Planner

워크플로우 계획 수립 서비스
"""

from typing import List, Dict, Any, Optional
from ..models.workflow_step import WorkflowStep


class WorkflowPlanner:
    """워크플로우 계획자"""
    
    def __init__(self):
        self.workflow_templates = {
            "code_analysis": self._get_code_analysis_template(),
            "code_generation": self._get_code_generation_template(),
            "bug_fix": self._get_bug_fix_template(),
            "general": self._get_general_template()
        }
    
    def plan_workflow(self, workflow_type: str, user_input: str, context: Optional[Dict[str, Any]] = None) -> List[WorkflowStep]:
        """워크플로우 계획 수립"""
        template = self.workflow_templates.get(workflow_type, self.workflow_templates["general"])
        
        steps = []
        for i, step_config in enumerate(template):
            step = WorkflowStep(
                step_id=f"step_{i+1}",
                step_name=step_config["name"],
                service_name=step_config["service"],
                action=step_config["action"],
                parameters=self._prepare_parameters(step_config["parameters"], user_input, context),
                status="pending"
            )
            steps.append(step)
        
        return steps
    
    def _prepare_parameters(self, template_params: Dict[str, Any], user_input: str, context: Optional[Dict[str, Any]]) -> Dict[str, Any]:
        """매개변수 준비"""
        parameters = template_params.copy()
        
        # 사용자 입력으로 매개변수 치환
        for key, value in parameters.items():
            if isinstance(value, str) and "{user_input}" in value:
                parameters[key] = value.replace("{user_input}", user_input)
        
        # 컨텍스트 정보 추가
        if context:
            parameters.update(context)
        
        return parameters
    
    def _get_code_analysis_template(self) -> List[Dict[str, Any]]:
        """코드 분석 워크플로우 템플릿"""
        return [
            {
                "name": "코드 분석",
                "service": "document_generator",
                "action": "analyze_code",
                "parameters": {
                    "project_path": ".",
                    "output_format": "markdown",
                    "include_metrics": True,
                    "include_dependencies": True,
                    "include_issues": True
                }
            },
            {
                "name": "문서 생성",
                "service": "document_generator", 
                "action": "generate_document",
                "parameters": {
                    "project_path": ".",
                    "output_format": "markdown"
                }
            }
        ]
    
    def _get_code_generation_template(self) -> List[Dict[str, Any]]:
        """코드 생성 워크플로우 템플릿"""
        return [
            {
                "name": "관련 문서 검색",
                "service": "rag_engine",
                "action": "search_documents",
                "parameters": {
                    "query": "{user_input}",
                    "limit": 5,
                    "threshold": 0.7
                }
            },
            {
                "name": "코드 생성",
                "service": "llm_chat",
                "action": "generate_code",
                "parameters": {
                    "message": "{user_input}",
                    "model": "gpt-4",
                    "context": "search_results"
                }
            }
        ]
    
    def _get_bug_fix_template(self) -> List[Dict[str, Any]]:
        """버그 수정 워크플로우 템플릿"""
        return [
            {
                "name": "코드 분석",
                "service": "document_generator",
                "action": "analyze_code",
                "parameters": {
                    "project_path": ".",
                    "include_issues": True
                }
            },
            {
                "name": "버그 관련 문서 검색",
                "service": "rag_engine",
                "action": "search_documents",
                "parameters": {
                    "query": "bug fix {user_input}",
                    "limit": 5
                }
            },
            {
                "name": "버그 수정 제안",
                "service": "llm_chat",
                "action": "suggest_fix",
                "parameters": {
                    "message": "다음 버그를 수정하는 방법을 제안해주세요: {user_input}",
                    "model": "gpt-4",
                    "context": "analysis_and_search_results"
                }
            }
        ]
    
    def _get_general_template(self) -> List[Dict[str, Any]]:
        """일반 워크플로우 템플릿"""
        return [
            {
                "name": "문서 검색",
                "service": "rag_engine",
                "action": "search_documents",
                "parameters": {
                    "query": "{user_input}",
                    "limit": 3
                }
            },
            {
                "name": "LLM 대화",
                "service": "llm_chat",
                "action": "chat",
                "parameters": {
                    "message": "{user_input}",
                    "model": "gpt-3.5-turbo",
                    "context": "search_results"
                }
            }
        ]
