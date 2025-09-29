"""
JavaScript/Vue.js/TypeScript 파일 Chunker
"""

import re
import os
from typing import List, Dict, Any, Optional
from .base_chunker import BaseChunker, CodeChunk


class JavaScriptChunker(BaseChunker):
    """JavaScript/Vue.js/TypeScript 파일 처리"""
    
    def _get_supported_extensions(self) -> List[str]:
        return ['.js', '.vue', '.ts', '.jsx', '.tsx']
    
    def chunk_file(self, file_path: str) -> List[CodeChunk]:
        """JavaScript/Vue 파일을 chunk로 변환"""
        try:
            with open(file_path, 'r', encoding='utf-8') as f:
                content = f.read()
            
            chunks = []
            
            # 1. Overview chunk 생성
            overview_chunk = self._create_overview_chunk(file_path, content)
            chunks.append(overview_chunk)
            
            # 2. Vue.js 컴포넌트 처리
            if file_path.endswith('.vue'):
                vue_chunks = self._parse_vue_component(file_path, content)
                chunks.extend(vue_chunks)
            else:
                # 3. 일반 JavaScript/TypeScript 처리
                js_chunks = self._parse_javascript_file(file_path, content)
                chunks.extend(js_chunks)
            
            return chunks
            
        except Exception as e:
            print(f"❌ 파일 처리 실패 {file_path}: {e}")
            return []
    
    def _create_overview_chunk(self, file_path: str, content: str) -> CodeChunk:
        """파일 overview chunk 생성"""
        file_name = os.path.basename(file_path)
        
        # 파일 타입별 설명
        if file_path.endswith('.vue'):
            file_type = "Vue.js 컴포넌트"
            framework = "vue"
        elif file_path.endswith('.ts'):
            file_type = "TypeScript 파일"
            framework = "typescript"
        elif file_path.endswith('.jsx'):
            file_type = "React JSX 파일"
            framework = "react"
        else:
            file_type = "JavaScript 파일"
            framework = "javascript"
        
        # import/require 문 추출
        imports = self._extract_imports(content)
        
        # 함수/클래스/컴포넌트 목록 추출
        functions = self._extract_function_names(content)
        classes = self._extract_class_names(content)
        components = self._extract_component_names(content)
        
        # overview 내용 생성
        overview_content = f"# {file_name}\n\n"
        overview_content += f"**파일 타입**: {file_type}\n\n"
        
        if imports:
            overview_content += f"**의존성**: {', '.join(imports[:10])}\n\n"
        
        if components:
            overview_content += f"**컴포넌트**: {', '.join(components)}\n\n"
        
        if classes:
            overview_content += f"**클래스**: {', '.join(classes)}\n\n"
        
        if functions:
            overview_content += f"**함수**: {', '.join(functions[:10])}\n\n"
        
        # 태그 생성
        tags = [file_type.lower(), framework]
        if 'vue' in content.lower():
            tags.append('vue')
        if 'react' in content.lower():
            tags.append('react')
        if 'typescript' in content.lower() or file_path.endswith('.ts'):
            tags.append('typescript')
        
        chunk_id = self._generate_chunk_id(file_path, "overview", "file_overview")
        
        return CodeChunk(
            chunk_id=chunk_id,
            file_path=file_path,
            chunk_type="overview",
            name="file_overview",
            line_range="1-end",
            content=overview_content,
            tags=tags,
            dependencies=imports,
            language="javascript",
            framework=framework,
            token_count=len(content.split())
        )
    
    def _parse_vue_component(self, file_path: str, content: str) -> List[CodeChunk]:
        """Vue.js 컴포넌트 파싱"""
        chunks = []
        
        # <template> 섹션
        template_match = re.search(r'<template[^>]*>(.*?)</template>', content, re.DOTALL)
        if template_match:
            template_content = template_match.group(1).strip()
            if template_content:
                chunk = CodeChunk(
                    chunk_id=self._generate_chunk_id(file_path, "template", "template"),
                    file_path=file_path,
                    chunk_type="template",
                    name="template",
                    line_range="template",
                    content=f"```vue\n<template>\n{template_content}\n</template>\n```",
                    tags=["vue", "template", "html"],
                    language="vue",
                    framework="vue"
                )
                chunks.append(chunk)
        
        # <script> 섹션
        script_match = re.search(r'<script[^>]*>(.*?)</script>', content, re.DOTALL)
        if script_match:
            script_content = script_match.group(1).strip()
            if script_content:
                # script 내부의 함수/클래스 추출
                script_chunks = self._parse_javascript_content(file_path, script_content, "script")
                chunks.extend(script_chunks)
        
        # <style> 섹션
        style_match = re.search(r'<style[^>]*>(.*?)</style>', content, re.DOTALL)
        if style_match:
            style_content = style_match.group(1).strip()
            if style_content:
                chunk = CodeChunk(
                    chunk_id=self._generate_chunk_id(file_path, "style", "style"),
                    file_path=file_path,
                    chunk_type="style",
                    name="style",
                    line_range="style",
                    content=f"```css\n{style_content}\n```",
                    tags=["vue", "style", "css"],
                    language="css",
                    framework="vue"
                )
                chunks.append(chunk)
        
        return chunks
    
    def _parse_javascript_file(self, file_path: str, content: str) -> List[CodeChunk]:
        """일반 JavaScript/TypeScript 파일 파싱"""
        return self._parse_javascript_content(file_path, content, "file")
    
    def _parse_javascript_content(self, file_path: str, content: str, context: str) -> List[CodeChunk]:
        """JavaScript 내용 파싱"""
        chunks = []
        lines = content.split('\n')
        
        # 함수 추출
        function_pattern = r'^(?:export\s+)?(?:async\s+)?function\s+(\w+)\s*\([^)]*\)\s*{'
        for i, line in enumerate(lines):
            match = re.match(function_pattern, line.strip())
            if match:
                func_name = match.group(1)
                func_chunk = self._extract_function_chunk(file_path, content, lines, i, func_name, context)
                if func_chunk:
                    chunks.append(func_chunk)
        
        # 화살표 함수 추출
        arrow_pattern = r'^(?:export\s+)?(?:const|let|var)\s+(\w+)\s*=\s*(?:async\s+)?\([^)]*\)\s*=>'
        for i, line in enumerate(lines):
            match = re.match(arrow_pattern, line.strip())
            if match:
                func_name = match.group(1)
                func_chunk = self._extract_arrow_function_chunk(file_path, content, lines, i, func_name, context)
                if func_chunk:
                    chunks.append(func_chunk)
        
        # 클래스 추출
        class_pattern = r'^(?:export\s+)?class\s+(\w+)'
        for i, line in enumerate(lines):
            match = re.match(class_pattern, line.strip())
            if match:
                class_name = match.group(1)
                class_chunk = self._extract_class_chunk(file_path, content, lines, i, class_name, context)
                if class_chunk:
                    chunks.append(class_chunk)
        
        return chunks
    
    def _extract_function_chunk(self, file_path: str, content: str, lines: List[str], 
                               start_line: int, func_name: str, context: str) -> Optional[CodeChunk]:
        """함수 chunk 추출"""
        # 함수 시작부터 끝까지 찾기
        brace_count = 0
        end_line = start_line
        
        for i in range(start_line, len(lines)):
            line = lines[i]
            brace_count += line.count('{') - line.count('}')
            if brace_count == 0 and i > start_line:
                end_line = i
                break
        
        func_content = '\n'.join(lines[start_line:end_line + 1])
        
        # JSDoc 추출
        docstring = self._extract_jsdoc(lines, start_line)
        
        # 복잡도 계산
        complexity = self._calculate_complexity(func_content)
        
        chunk_id = self._generate_chunk_id(file_path, "function", f"{context}_{func_name}")
        
        return CodeChunk(
            chunk_id=chunk_id,
            file_path=file_path,
            chunk_type="function",
            name=func_name,
            line_range=f"{start_line + 1}-{end_line + 1}",
            content=func_content,
            docstring=docstring,
            complexity=complexity,
            tags=["function", "javascript"],
            language="javascript",
            token_count=len(func_content.split())
        )
    
    def _extract_arrow_function_chunk(self, file_path: str, content: str, lines: List[str], 
                                    start_line: int, func_name: str, context: str) -> Optional[CodeChunk]:
        """화살표 함수 chunk 추출"""
        # 화살표 함수의 끝 찾기
        end_line = start_line
        brace_count = 0
        in_function = False
        
        for i in range(start_line, len(lines)):
            line = lines[i]
            if '=>' in line:
                in_function = True
                if '{' in line:
                    brace_count = 1
                else:
                    # 한 줄 화살표 함수
                    end_line = i
                    break
            elif in_function and brace_count > 0:
                brace_count += line.count('{') - line.count('}')
                if brace_count == 0:
                    end_line = i
                    break
        
        func_content = '\n'.join(lines[start_line:end_line + 1])
        
        # JSDoc 추출
        docstring = self._extract_jsdoc(lines, start_line)
        
        # 복잡도 계산
        complexity = self._calculate_complexity(func_content)
        
        chunk_id = self._generate_chunk_id(file_path, "arrow_function", f"{context}_{func_name}")
        
        return CodeChunk(
            chunk_id=chunk_id,
            file_path=file_path,
            chunk_type="arrow_function",
            name=func_name,
            line_range=f"{start_line + 1}-{end_line + 1}",
            content=func_content,
            docstring=docstring,
            complexity=complexity,
            tags=["arrow_function", "javascript"],
            language="javascript",
            token_count=len(func_content.split())
        )
    
    def _extract_class_chunk(self, file_path: str, content: str, lines: List[str], 
                           start_line: int, class_name: str, context: str) -> Optional[CodeChunk]:
        """클래스 chunk 추출"""
        # 클래스 시작부터 끝까지 찾기
        brace_count = 0
        end_line = start_line
        
        for i in range(start_line, len(lines)):
            line = lines[i]
            brace_count += line.count('{') - line.count('}')
            if brace_count == 0 and i > start_line:
                end_line = i
                break
        
        class_content = '\n'.join(lines[start_line:end_line + 1])
        
        # JSDoc 추출
        docstring = self._extract_jsdoc(lines, start_line)
        
        # 메서드 추출
        methods = self._extract_class_methods(class_content)
        
        # 복잡도 계산
        complexity = self._calculate_complexity(class_content)
        
        chunk_id = self._generate_chunk_id(file_path, "class", f"{context}_{class_name}")
        
        return CodeChunk(
            chunk_id=chunk_id,
            file_path=file_path,
            chunk_type="class",
            name=class_name,
            line_range=f"{start_line + 1}-{end_line + 1}",
            content=class_content,
            docstring=docstring,
            complexity=complexity,
            tags=["class", "javascript"],
            language="javascript",
            token_count=len(class_content.split())
        )
    
    def _extract_jsdoc(self, lines: List[str], start_line: int) -> str:
        """JSDoc 주석 추출"""
        doc_lines = []
        
        # 함수/클래스 정의 이전의 주석 찾기
        for i in range(start_line - 1, max(0, start_line - 10), -1):
            line = lines[i].strip()
            if line.startswith('/**'):
                doc_lines.insert(0, line)
                break
            elif line.startswith('*') and doc_lines:
                doc_lines.insert(0, line)
            elif line.startswith('//') and not doc_lines:
                doc_lines.insert(0, line)
            elif line and not line.startswith('*') and not line.startswith('//'):
                break
        
        return '\n'.join(doc_lines).strip()
    
    def _extract_imports(self, content: str) -> List[str]:
        """import/require 문에서 의존성 추출"""
        imports = []
        
        # ES6 import
        import_pattern = r'import\s+(?:.*?\s+from\s+)?[\'"]([^\'"]+)[\'"]'
        for match in re.finditer(import_pattern, content):
            imports.append(match.group(1))
        
        # CommonJS require
        require_pattern = r'require\s*\(\s*[\'"]([^\'"]+)[\'"]\s*\)'
        for match in re.finditer(require_pattern, content):
            imports.append(match.group(1))
        
        return list(set(imports))
    
    def _extract_function_names(self, content: str) -> List[str]:
        """함수명 추출"""
        functions = []
        
        # function 선언
        func_pattern = r'function\s+(\w+)\s*\('
        for match in re.finditer(func_pattern, content):
            functions.append(match.group(1))
        
        # 화살표 함수
        arrow_pattern = r'(?:const|let|var)\s+(\w+)\s*=\s*(?:async\s+)?\([^)]*\)\s*=>'
        for match in re.finditer(arrow_pattern, content):
            functions.append(match.group(1))
        
        return functions
    
    def _extract_class_names(self, content: str) -> List[str]:
        """클래스명 추출"""
        classes = []
        class_pattern = r'class\s+(\w+)'
        for match in re.finditer(class_pattern, content):
            classes.append(match.group(1))
        return classes
    
    def _extract_component_names(self, content: str) -> List[str]:
        """Vue 컴포넌트명 추출"""
        components = []
        
        # Vue 컴포넌트 정의
        vue_pattern = r'export\s+default\s*{\s*name:\s*[\'"]([^\'"]+)[\'"]'
        for match in re.finditer(vue_pattern, content):
            components.append(match.group(1))
        
        # React 컴포넌트
        react_pattern = r'(?:export\s+)?(?:default\s+)?(?:function|const)\s+(\w+)\s*(?:\([^)]*\)\s*{|=)'
        for match in re.finditer(react_pattern, content):
            if match.group(1)[0].isupper():  # 대문자로 시작하는 컴포넌트
                components.append(match.group(1))
        
        return components
    
    def _extract_class_methods(self, class_content: str) -> List[str]:
        """클래스 메서드명 추출"""
        methods = []
        method_pattern = r'(?:async\s+)?(\w+)\s*\([^)]*\)\s*{'
        for match in re.finditer(method_pattern, class_content):
            methods.append(match.group(1))
        return methods
