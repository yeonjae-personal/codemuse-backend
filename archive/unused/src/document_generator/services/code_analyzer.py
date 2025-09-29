"""
Code Analyzer Service

프로젝트 전체 코드 분석
"""

import os
from typing import List, Dict, Any, Set
from pathlib import Path
from collections import defaultdict

from .ast_parser import ASTParser
from ..models.document_analysis import DocumentAnalysis, CodeStructure, DependencyGraph


class CodeAnalyzer:
    """코드 분석기"""
    
    def __init__(self):
        self.ast_parser = ASTParser()
        self.ignore_dirs = {'.git', '__pycache__', 'node_modules', '.venv', 'venv', 'env'}
        self.ignore_files = {'.gitignore', '.env', '.env.local'}
    
    def analyze_project(self, project_path: str) -> DocumentAnalysis:
        """프로젝트 전체 분석"""
        project_path = Path(project_path).resolve()
        
        # 파일 수집
        files = self._collect_files(project_path)
        
        # 각 파일 분석
        code_structures = []
        total_lines = 0
        languages = defaultdict(int)
        
        for file_path in files:
            analysis = self.ast_parser.parse_file(str(file_path))
            if analysis:
                code_structures.append(CodeStructure(**analysis))
                total_lines += analysis['lines_of_code']
                languages[analysis['language']] += 1
        
        # 의존성 그래프 생성
        dependency_graph = self._build_dependency_graph(code_structures)
        
        # 이슈 검출
        issues = self._detect_issues(code_structures)
        
        # 메트릭 계산
        metrics = self._calculate_metrics(code_structures)
        
        return DocumentAnalysis(
            id=f"analysis_{project_path.name}_{int(os.path.getctime(project_path))}",
            project_name=project_path.name,
            analysis_timestamp=os.path.getctime(project_path),
            total_files=len(files),
            total_lines=total_lines,
            languages=dict(languages),
            code_structures=code_structures,
            dependency_graph=dependency_graph,
            issues=issues,
            metrics=metrics
        )
    
    def _collect_files(self, project_path: Path) -> List[Path]:
        """프로젝트 내 모든 코드 파일 수집"""
        files = []
        
        for root, dirs, files_in_dir in os.walk(project_path):
            # 무시할 디렉터리 제거
            dirs[:] = [d for d in dirs if d not in self.ignore_dirs]
            
            for file in files_in_dir:
                if file not in self.ignore_files:
                    file_path = Path(root) / file
                    if file_path.suffix.lower() in self.ast_parser.supported_extensions:
                        files.append(file_path)
        
        return files
    
    def _build_dependency_graph(self, code_structures: List[CodeStructure]) -> DependencyGraph:
        """의존성 그래프 빌드"""
        nodes = []
        edges = []
        
        # 노드 생성 (파일 경로들)
        for structure in code_structures:
            nodes.append(structure.file_path)
        
        # 엣지 생성 (import 관계)
        for structure in code_structures:
            for import_name in structure.imports:
                # 간단한 의존성 매칭
                for other_structure in code_structures:
                    if import_name in other_structure.file_path or \
                       import_name in Path(other_structure.file_path).stem:
                        edges.append({
                            'from': structure.file_path,
                            'to': other_structure.file_path
                        })
        
        # 순환 의존성 검출
        cycles = self._detect_cycles(nodes, edges)
        
        return DependencyGraph(
            nodes=nodes,
            edges=edges,
            cycles=cycles
        )
    
    def _detect_cycles(self, nodes: List[str], edges: List[Dict[str, str]]) -> List[List[str]]:
        """순환 의존성 검출 (DFS 기반)"""
        graph = defaultdict(list)
        for edge in edges:
            graph[edge['from']].append(edge['to'])
        
        cycles = []
        visited = set()
        rec_stack = set()
        
        def dfs(node, path):
            if node in rec_stack:
                # 순환 발견
                cycle_start = path.index(node)
                cycles.append(path[cycle_start:] + [node])
                return
            
            if node in visited:
                return
            
            visited.add(node)
            rec_stack.add(node)
            
            for neighbor in graph[node]:
                dfs(neighbor, path + [node])
            
            rec_stack.remove(node)
        
        for node in nodes:
            if node not in visited:
                dfs(node, [])
        
        return cycles
    
    def _detect_issues(self, code_structures: List[CodeStructure]) -> List[Dict[str, Any]]:
        """코드 이슈 검출"""
        issues = []
        
        for structure in code_structures:
            # 복잡도 이슈
            if structure.complexity_score > 10:
                issues.append({
                    'type': 'high_complexity',
                    'file': structure.file_path,
                    'severity': 'warning',
                    'message': f'높은 순환 복잡도: {structure.complexity_score}'
                })
            
            # 긴 함수 이슈
            for func in structure.functions:
                if func.get('line_number', 0) > 50:
                    issues.append({
                        'type': 'long_function',
                        'file': structure.file_path,
                        'function': func['name'],
                        'severity': 'info',
                        'message': f'긴 함수: {func["name"]}'
                    })
        
        return issues
    
    def _calculate_metrics(self, code_structures: List[CodeStructure]) -> Dict[str, Any]:
        """프로젝트 메트릭 계산"""
        total_functions = sum(len(s.functions) for s in code_structures)
        total_classes = sum(len(s.classes) for s in code_structures)
        avg_complexity = sum(s.complexity_score for s in code_structures) / len(code_structures) if code_structures else 0
        
        return {
            'total_functions': total_functions,
            'total_classes': total_classes,
            'average_complexity': avg_complexity,
            'files_with_issues': len([s for s in code_structures if s.complexity_score > 5])
        }
