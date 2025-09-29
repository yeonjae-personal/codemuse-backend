"""
AST Parser Service

코드 파일을 AST로 파싱하여 구조 분석
"""

import ast
import os
from typing import List, Dict, Any, Optional
from pathlib import Path


class ASTParser:
    """AST 파서 클래스"""
    
    def __init__(self):
        self.supported_extensions = {'.py', '.js', '.ts', '.java', '.cpp', '.c'}
    
    def parse_file(self, file_path: str) -> Optional[Dict[str, Any]]:
        """파일을 파싱하여 AST 정보 반환"""
        try:
            if not self._is_supported_file(file_path):
                return None
                
            with open(file_path, 'r', encoding='utf-8') as f:
                content = f.read()
            
            if file_path.endswith('.py'):
                return self._parse_python(content, file_path)
            else:
                return self._parse_generic(content, file_path)
                
        except Exception as e:
            print(f"파싱 오류 {file_path}: {e}")
            return None
    
    def _is_supported_file(self, file_path: str) -> bool:
        """지원되는 파일 확장자인지 확인"""
        return Path(file_path).suffix.lower() in self.supported_extensions
    
    def _parse_python(self, content: str, file_path: str) -> Dict[str, Any]:
        """Python 파일 파싱"""
        tree = ast.parse(content)
        
        functions = []
        classes = []
        imports = []
        
        for node in ast.walk(tree):
            if isinstance(node, ast.FunctionDef) or isinstance(node, ast.AsyncFunctionDef):
                functions.append({
                    'name': node.name,
                    'line_number': node.lineno,
                    'args': [arg.arg for arg in node.args.args],
                    'docstring': ast.get_docstring(node),
                    'is_async': isinstance(node, ast.AsyncFunctionDef)
                })
            elif isinstance(node, ast.ClassDef):
                classes.append({
                    'name': node.name,
                    'line_number': node.lineno,
                    'bases': [base.id if isinstance(base, ast.Name) else str(base) for base in node.bases],
                    'docstring': ast.get_docstring(node)
                })
            elif isinstance(node, (ast.Import, ast.ImportFrom)):
                if isinstance(node, ast.Import):
                    imports.extend([alias.name for alias in node.names])
                else:
                    imports.extend([alias.name for alias in node.names])
        
        return {
            'file_path': file_path,
            'language': 'python',
            'functions': functions,
            'classes': classes,
            'imports': imports,
            'exports': [],  # Python에서는 exports 개념이 없음
            'lines_of_code': len(content.splitlines()),
            'complexity_score': self._calculate_complexity(tree),
            'dependencies': imports  # imports를 dependencies로도 사용
        }
    
    def _parse_generic(self, content: str, file_path: str) -> Dict[str, Any]:
        """일반적인 파일 파싱 (간단한 분석)"""
        lines = content.splitlines()
        
        # 간단한 함수/클래스 패턴 매칭
        functions = []
        classes = []
        
        for i, line in enumerate(lines):
            line = line.strip()
            if line.startswith('function ') or line.startswith('def '):
                functions.append({
                    'name': line.split()[1].split('(')[0],
                    'line_number': i + 1
                })
            elif line.startswith('class '):
                classes.append({
                    'name': line.split()[1].split(':')[0],
                    'line_number': i + 1
                })
        
        return {
            'file_path': file_path,
            'language': Path(file_path).suffix[1:],
            'functions': functions,
            'classes': classes,
            'imports': [],
            'exports': [],
            'lines_of_code': len(lines),
            'complexity_score': 1.0,
            'dependencies': []
        }
    
    def _calculate_complexity(self, tree: ast.AST) -> float:
        """순환 복잡도 계산"""
        complexity = 1
        
        for node in ast.walk(tree):
            if (isinstance(node, ast.If) or isinstance(node, ast.While) or 
                isinstance(node, ast.For) or isinstance(node, ast.AsyncFor)):
                complexity += 1
            elif isinstance(node, ast.ExceptHandler):
                complexity += 1
            elif isinstance(node, ast.With) or isinstance(node, ast.AsyncWith):
                complexity += 1
        
        return complexity
