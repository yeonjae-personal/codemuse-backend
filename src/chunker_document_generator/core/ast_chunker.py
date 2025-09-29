"""
AST 파싱 기반 Python 코드 Chunk 생성기

파일 단위 대신 함수/클래스/메서드 단위로 코드를 분할하여
더 정확한 RAG 검색과 LLM 처리를 가능하게 합니다.
"""

import ast
import os
import re
import hashlib
import tiktoken
from typing import List, Dict, Any, Optional, Tuple
from dataclasses import dataclass
from pathlib import Path


@dataclass
class CodeChunk:
    """코드 chunk를 나타내는 데이터 클래스"""
    
    # 기본 식별 정보
    chunk_id: str
    file_path: str
    chunk_type: str  # function, method, class, overview
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


class ASTChunker:
    """AST 파싱 기반 코드 chunker"""
    
    def __init__(self, max_tokens: int = 600):
        """
        Args:
            max_tokens: chunk당 최대 토큰 수 (sub-chunk 분리 기준)
        """
        self.max_tokens = max_tokens
        self.encoding = tiktoken.get_encoding("cl100k_base")  # GPT-4 기준
        
    def chunk_file(self, file_path: str) -> List[CodeChunk]:
        """
        Python 파일을 AST 파싱하여 chunk 리스트로 변환
        
        Args:
            file_path: 분석할 Python 파일 경로
            
        Returns:
            CodeChunk 리스트 (overview + functions/classes/methods)
        """
        try:
            with open(file_path, 'r', encoding='utf-8') as f:
                source_code = f.read()
                
            # AST 파싱
            tree = ast.parse(source_code, filename=file_path)
            
            chunks = []
            
            # 1. Overview chunk 생성 (파일 요약)
            overview_chunk = self._create_overview_chunk(file_path, source_code, tree)
            chunks.append(overview_chunk)
            
            # 2. 클래스 및 함수 chunk 생성 (중첩 방지)
            processed_nodes = set()
            
            for node in tree.body:  # 최상위 노드만 처리
                if isinstance(node, ast.FunctionDef):
                    chunk = self._create_function_chunk(file_path, source_code, node)
                    chunks.append(chunk)
                    processed_nodes.add(id(node))
                    
                elif isinstance(node, ast.AsyncFunctionDef):
                    chunk = self._create_function_chunk(file_path, source_code, node, is_async=True)
                    chunks.append(chunk)
                    processed_nodes.add(id(node))
                    
                elif isinstance(node, ast.ClassDef):
                    # 클래스 자체 chunk
                    class_chunk = self._create_class_chunk(file_path, source_code, node)
                    chunks.append(class_chunk)
                    processed_nodes.add(id(node))
                    
                    # 클래스 내 메서드들
                    for item in node.body:
                        if isinstance(item, ast.FunctionDef):
                            method_chunk = self._create_method_chunk(
                                file_path, source_code, item, node.name
                            )
                            chunks.append(method_chunk)
                            processed_nodes.add(id(item))
                        elif isinstance(item, ast.AsyncFunctionDef):
                            method_chunk = self._create_method_chunk(
                                file_path, source_code, item, node.name, is_async=True
                            )
                            chunks.append(method_chunk)
                            processed_nodes.add(id(item))
            
            # 3. 큰 chunk 분리 (sub-chunking)
            final_chunks = []
            for chunk in chunks:
                if chunk.token_count and chunk.token_count > self.max_tokens:
                    sub_chunks = self._split_large_chunk(chunk)
                    final_chunks.extend(sub_chunks)
                else:
                    final_chunks.append(chunk)
                    
            return final_chunks
            
        except Exception as e:
            print(f"❌ 파일 {file_path} chunk 생성 오류: {e}")
            return []
    
    def _create_overview_chunk(self, file_path: str, source_code: str, tree: ast.AST) -> CodeChunk:
        """파일 overview chunk 생성"""
        
        # 파일 기본 정보 추출
        file_name = os.path.basename(file_path)
        
        # 최상위 docstring 추출 (개선된 방식)
        source_lines = source_code.split('\n')
        docstring = self._extract_docstring(tree, source_lines, 0) or ""
        
        # 클래스와 함수 이름 목록 (최상위만)
        classes = []
        functions = []
        for node in tree.body:
            if isinstance(node, ast.ClassDef):
                classes.append(node.name)
            elif isinstance(node, (ast.FunctionDef, ast.AsyncFunctionDef)):
                functions.append(node.name)
        
        # import 정보 추출
        imports = self._extract_imports(tree)
        
        # overview 내용 구성
        overview_content = self._generate_overview_content(
            file_name, docstring, classes, functions, imports
        )
        
        # tags 생성
        tags = self._generate_tags_from_file(file_path, imports, classes, functions)
        
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
            token_count=len(self.encoding.encode(overview_content))
        )
    
    def _create_function_chunk(self, file_path: str, source_code: str, 
                             node: ast.FunctionDef, is_async: bool = False) -> CodeChunk:
        """함수 chunk 생성"""
        
        lines = source_code.split('\n')
        
        # 함수 코드 추출
        start_line = node.lineno - 1  # 0-based index
        end_line = node.end_lineno if hasattr(node, 'end_lineno') else start_line + 10
        
        function_code = '\n'.join(lines[start_line:end_line])
        
        # docstring 추출 (개선된 방식)
        docstring = self._extract_docstring(node, lines, start_line) or ""
        
        # 데코레이터 추출
        decorators = [self._get_decorator_name(dec) for dec in node.decorator_list]
        
        # generator 체크
        is_generator = self._is_generator_function(node)
        
        # 복잡도 계산 (간단한 사이클로매틱 복잡도)
        complexity = self._calculate_complexity(node)
        
        # 시그니처/파라미터/리턴/예외/호출/데이터플로우 추출
        signature = self._build_signature(node)
        parameters = self._extract_parameters(node)
        returns = self._annotation_to_str(node.returns) if hasattr(node, 'returns') else None
        raises = self._extract_raises(node)
        calls = self._extract_calls(node)
        dflow, dstats = self._extract_data_flow(node)
        
        # tags 생성
        tags = self._generate_tags_from_function(node, decorators, is_async, is_generator)
        
        chunk_id = self._generate_chunk_id(file_path, "function", node.name)
        
        # 코드 해시
        code_hash = hashlib.sha256(function_code.encode('utf-8')).hexdigest()[:16]

        return CodeChunk(
            chunk_id=chunk_id,
            file_path=file_path,
            chunk_type="async_function" if is_async else "function",
            name=node.name,
            line_range=f"{node.lineno}-{end_line}",
            content=function_code,
            decorators=decorators,
            tags=tags,
            complexity=complexity,
            docstring=docstring,
            is_async=is_async,
            is_generator=is_generator,
            token_count=len(self.encoding.encode(function_code)),
            signature=signature,
            parameters=parameters,
            returns=returns,
            raises=raises,
            calls=calls,
            data_flow_summary=dflow,
            data_flow_stats=dstats,
            code_hash=code_hash
        )
    
    def _create_method_chunk(self, file_path: str, source_code: str, 
                           node: ast.FunctionDef, class_name: str, 
                           is_async: bool = False) -> CodeChunk:
        """클래스 메서드 chunk 생성"""
        
        # 기본적으로 function과 동일하지만 parent 정보 추가
        chunk = self._create_function_chunk(file_path, source_code, node, is_async)
        
        # 메서드 특성 반영
        chunk.chunk_type = "method"
        chunk.parent = class_name
        
        # method tags 추가
        method_tags = []
        if node.name.startswith('__') and node.name.endswith('__'):
            method_tags.append("magic_method")
        elif node.name.startswith('_'):
            method_tags.append("private_method")
        else:
            method_tags.append("public_method")
        
        chunk.tags.extend(method_tags)
        
        # chunk_id 갱신
        chunk.chunk_id = self._generate_chunk_id(file_path, "method", f"{class_name}.{node.name}")
        
        return chunk
    
    def _create_class_chunk(self, file_path: str, source_code: str, node: ast.ClassDef) -> CodeChunk:
        """클래스 chunk 생성"""
        
        lines = source_code.split('\n')
        
        # 클래스 헤더 부분만 추출 (메서드 제외)
        start_line = node.lineno - 1
        
        # 클래스 docstring까지만 포함
        class_header_lines = []
        in_class_def = False
        
        for i, line in enumerate(lines[start_line:], start_line):
            class_header_lines.append(line)
            
            # 첫 번째 메서드나 다른 정의를 만나면 중단
            if in_class_def and (line.strip().startswith('def ') or 
                               line.strip().startswith('async def ') or
                               line.strip().startswith('class ')):
                break
                
            if line.strip().startswith(f'class {node.name}'):
                in_class_def = True
        
        class_code = '\n'.join(class_header_lines)
        
        # 상속 클래스 추출
        base_classes = [self._get_base_class_name(base) for base in node.bases]
        
        # docstring 추출 (개선된 방식)
        docstring = self._extract_docstring(node, lines, start_line) or ""
        
        # 데코레이터 추출
        decorators = [self._get_decorator_name(dec) for dec in node.decorator_list]
        
        # tags 생성
        tags = self._generate_tags_from_class(node, base_classes, decorators)
        
        chunk_id = self._generate_chunk_id(file_path, "class", node.name)
        
        return CodeChunk(
            chunk_id=chunk_id,
            file_path=file_path,
            chunk_type="class",
            name=node.name,
            line_range=f"{node.lineno}-{node.lineno + 10}",  # 클래스 헤더 부분만
            content=class_code,
            base_classes=base_classes,
            decorators=decorators,
            tags=tags,
            docstring=docstring,
            token_count=len(self.encoding.encode(class_code)),
            signature=f"class {node.name}({', '.join(base_classes)})" if base_classes else f"class {node.name}"
        )
    
    def _split_large_chunk(self, chunk: CodeChunk) -> List[CodeChunk]:
        """큰 chunk를 sub-chunk로 분리"""
        
        # 극단적으로 큰 경우(2000 토큰+)만 분리
        if chunk.token_count < 2000:
            return [chunk]
        
        # 코드를 논리적 블록 단위로 분리
        lines = chunk.content.split('\n')
        sub_chunks = []
        current_block = []
        current_tokens = 0
        block_count = 0
        
        for line in lines:
            line_tokens = len(self.encoding.encode(line))
            
            # 블록 경계 체크 (if, for, while, try, def 등)
            is_block_start = any(line.strip().startswith(keyword) for keyword in 
                               ['if ', 'for ', 'while ', 'try:', 'def ', 'class ', 'with '])
            
            # 현재 블록이 너무 크고 새 블록이 시작되면 분리
            if current_tokens + line_tokens > self.max_tokens and is_block_start and current_block:
                # 현재 블록을 sub-chunk로 저장
                block_content = '\n'.join(current_block)
                sub_chunk = self._create_sub_chunk(chunk, block_content, block_count)
                sub_chunks.append(sub_chunk)
                
                # 새 블록 시작
                current_block = [line]
                current_tokens = line_tokens
                block_count += 1
            else:
                current_block.append(line)
                current_tokens += line_tokens
        
        # 마지막 블록 처리
        if current_block:
            block_content = '\n'.join(current_block)
            sub_chunk = self._create_sub_chunk(chunk, block_content, block_count)
            sub_chunks.append(sub_chunk)
        
        return sub_chunks if len(sub_chunks) > 1 else [chunk]
    
    def _create_sub_chunk(self, original_chunk: CodeChunk, content: str, block_index: int) -> CodeChunk:
        """원본 chunk의 sub-chunk 생성"""
        
        sub_chunk_id = f"{original_chunk.chunk_id}_block_{block_index}"
        
        return CodeChunk(
            chunk_id=sub_chunk_id,
            file_path=original_chunk.file_path,
            chunk_type=f"{original_chunk.chunk_type}_block",
            name=f"{original_chunk.name}_block_{block_index}",
            line_range=original_chunk.line_range,  # 정확한 line 계산은 복잡하므로 원본 유지
            content=content,
            parent=original_chunk.parent,
            base_classes=original_chunk.base_classes,
            decorators=original_chunk.decorators,
            tags=original_chunk.tags + ["sub_chunk"],
            complexity=original_chunk.complexity,
            docstring=original_chunk.docstring,
            dependencies=original_chunk.dependencies,
            is_async=original_chunk.is_async,
            is_generator=original_chunk.is_generator,
            token_count=len(self.encoding.encode(content))
        )
    
    # 헬퍼 메서드들
    
    def _build_signature(self, node: ast.FunctionDef) -> str:
        try:
            args = []
            for a in node.args.args:
                ann = self._annotation_to_str(a.annotation) if hasattr(a, 'annotation') else None
                args.append(f"{a.arg}: {ann}" if ann else a.arg)
            if node.args.vararg:
                args.append(f"*{node.args.vararg.arg}")
            for a in node.args.kwonlyargs:
                ann = self._annotation_to_str(a.annotation) if hasattr(a, 'annotation') else None
                args.append(f"{a.arg}: {ann}" if ann else a.arg)
            if node.args.kwarg:
                args.append(f"**{node.args.kwarg.arg}")
            ret = self._annotation_to_str(node.returns) if hasattr(node, 'returns') else None
            sig = f"({', '.join(args)})"
            if ret:
                sig += f" -> {ret}"
            return sig
        except Exception:
            return "()"

    def _annotation_to_str(self, ann) -> Optional[str]:
        if ann is None:
            return None
        try:
            if isinstance(ann, ast.Name):
                return ann.id
            if isinstance(ann, ast.Subscript):
                base = self._annotation_to_str(ann.value)
                sub = self._annotation_to_str(ann.slice)
                return f"{base}[{sub}]" if base and sub else base
            if isinstance(ann, ast.Attribute):
                return ann.attr
            if isinstance(ann, ast.Tuple):
                return ", ".join(filter(None, [self._annotation_to_str(e) for e in ann.elts]))
            if isinstance(ann, ast.Constant):
                return str(ann.value)
            return None
        except Exception:
            return None

    def _extract_parameters(self, node: ast.FunctionDef) -> List[str]:
        try:
            params = []
            for a in node.args.args:
                ann = self._annotation_to_str(a.annotation) if hasattr(a, 'annotation') else None
                params.append(f"{a.arg}: {ann}" if ann else a.arg)
            if node.args.vararg:
                params.append(f"*{node.args.vararg.arg}")
            for a in node.args.kwonlyargs:
                ann = self._annotation_to_str(a.annotation) if hasattr(a, 'annotation') else None
                params.append(f"{a.arg}: {ann}" if ann else a.arg)
            if node.args.kwarg:
                params.append(f"**{node.args.kwarg.arg}")
            return params
        except Exception:
            return []

    def _extract_raises(self, node: ast.AST) -> List[str]:
        raises = []
        try:
            for n in ast.walk(node):
                if isinstance(n, ast.Raise) and n.exc is not None:
                    if isinstance(n.exc, ast.Call) and isinstance(n.exc.func, ast.Name):
                        raises.append(n.exc.func.id)
                    elif isinstance(n.exc, ast.Name):
                        raises.append(n.exc.id)
        except Exception:
            pass
        return list(dict.fromkeys(raises))

    def _extract_calls(self, node: ast.AST) -> List[str]:
        calls = []
        try:
            for n in ast.walk(node):
                if isinstance(n, ast.Call):
                    if isinstance(n.func, ast.Name):
                        calls.append(n.func.id)
                    elif isinstance(n.func, ast.Attribute):
                        calls.append(n.func.attr)
        except Exception:
            pass
        return list(dict.fromkeys(calls))

    def _extract_data_flow(self, node: ast.AST) -> Tuple[List[Dict[str, str]], Dict[str, int]]:
        edges = []
        stats = {"assign": 0, "call": 0, "return": 0, "raise": 0}
        try:
            for n in ast.walk(node):
                if isinstance(n, ast.Assign):
                    src = self._expr_to_str(n.value)
                    for t in n.targets:
                        dst = self._expr_to_str(t)
                        edges.append({"op": "assign", "src": src, "dst": dst, "line": str(n.lineno)})
                        stats["assign"] += 1
                elif isinstance(n, ast.Call):
                    fname = self._expr_to_str(n.func)
                    edges.append({"op": "call", "src": fname, "dst": "(result)", "line": str(n.lineno)})
                    stats["call"] += 1
                elif isinstance(n, ast.Return):
                    val = self._expr_to_str(n.value)
                    edges.append({"op": "return", "src": val, "dst": "return", "line": str(n.lineno)})
                    stats["return"] += 1
                elif isinstance(n, ast.Raise):
                    exc = self._expr_to_str(n.exc)
                    edges.append({"op": "raise", "src": exc, "dst": "exception", "line": str(n.lineno)})
                    stats["raise"] += 1
        except Exception:
            pass
        # 상위 N개만 요약(ENV 미사용 시 12개 기본)
        summary = edges[:12]
        stats["edges"] = len(edges)
        return summary, stats

    def _expr_to_str(self, expr: ast.AST) -> str:
        try:
            if isinstance(expr, ast.Name):
                return expr.id
            if isinstance(expr, ast.Attribute):
                base = self._expr_to_str(expr.value)
                return f"{base}.{expr.attr}" if base else expr.attr
            if isinstance(expr, ast.Constant):
                return str(expr.value)
            if isinstance(expr, ast.Call):
                if isinstance(expr.func, ast.Name):
                    return expr.func.id
                if isinstance(expr.func, ast.Attribute):
                    return expr.func.attr
            if isinstance(expr, ast.BinOp):
                return "binop"
            if expr is None:
                return "None"
            return expr.__class__.__name__
        except Exception:
            return "expr"
    def _extract_imports(self, tree: ast.AST) -> List[str]:
        """import 구문에서 라이브러리 목록 추출"""
        imports = []
        for node in ast.walk(tree):
            if isinstance(node, ast.Import):
                for alias in node.names:
                    imports.append(alias.name.split('.')[0])  # 최상위 패키지만
            elif isinstance(node, ast.ImportFrom):
                if node.module:
                    imports.append(node.module.split('.')[0])
        return list(set(imports))  # 중복 제거
    
    def _get_decorator_name(self, decorator: ast.expr) -> str:
        """데코레이터 이름 추출"""
        if isinstance(decorator, ast.Name):
            return decorator.id
        elif isinstance(decorator, ast.Attribute):
            return decorator.attr
        elif isinstance(decorator, ast.Call):
            if isinstance(decorator.func, ast.Name):
                return decorator.func.id
            elif isinstance(decorator.func, ast.Attribute):
                return decorator.func.attr
        return "unknown"
    
    def _get_base_class_name(self, base: ast.expr) -> str:
        """상속 클래스 이름 추출"""
        if isinstance(base, ast.Name):
            return base.id
        elif isinstance(base, ast.Attribute):
            return base.attr
        return "unknown"
    
    def _is_generator_function(self, node: ast.FunctionDef) -> bool:
        """함수가 generator인지 체크 (yield 사용 여부)"""
        for child in ast.walk(node):
            if isinstance(child, (ast.Yield, ast.YieldFrom)):
                return True
        return False
    
    def _calculate_complexity(self, node: ast.FunctionDef) -> int:
        """간단한 사이클로매틱 복잡도 계산"""
        complexity = 1  # 기본 복잡도
        for child in ast.walk(node):
            if isinstance(child, (ast.If, ast.While, ast.For, ast.AsyncFor)):
                complexity += 1
            elif isinstance(child, ast.ExceptHandler):
                complexity += 1
        return complexity
    
    def _generate_overview_content(self, file_name: str, docstring: str, 
                                 classes: List[str], functions: List[str], 
                                 imports: List[str]) -> str:
        """파일 overview 내용 생성"""
        content = f"# {file_name}\n\n"
        
        if docstring:
            content += f"## 설명\n{docstring}\n\n"
        
        if classes:
            content += f"## 클래스\n" + "\n".join(f"- {cls}" for cls in classes) + "\n\n"
        
        if functions:
            content += f"## 함수\n" + "\n".join(f"- {func}" for func in functions) + "\n\n"
        
        if imports:
            content += f"## 의존성\n" + "\n".join(f"- {imp}" for imp in imports) + "\n\n"
        
        return content
    
    def _generate_tags_from_file(self, file_path: str, imports: List[str], 
                               classes: List[str], functions: List[str]) -> List[str]:
        """파일 기반 tags 생성"""
        tags = []
        
        # 파일명 기반
        file_name = os.path.basename(file_path).replace('.py', '')
        tags.append(file_name)
        
        # 경로 기반
        path_parts = Path(file_path).parts
        for part in path_parts:
            if part not in ['.', '..', 'src', '__pycache__']:
                tags.append(part)
        
        # import 기반
        common_libs = {
            'fastapi': ['api', 'web'],
            'requests': ['http', 'api'],
            'pandas': ['data', 'analysis'],
            'numpy': ['math', 'calculation'],
            'asyncio': ['async', 'concurrency'],
            'logging': ['log', 'debug'],
            'json': ['serialization', 'data'],
            'os': ['system', 'file'],
            'sys': ['system'],
            're': ['regex', 'pattern']
        }
        
        for imp in imports:
            if imp in common_libs:
                tags.extend(common_libs[imp])
            tags.append(imp)
        
        return list(set(tags))
    
    def _generate_tags_from_function(self, node: ast.FunctionDef, decorators: List[str], 
                                   is_async: bool, is_generator: bool) -> List[str]:
        """함수 기반 tags 생성"""
        tags = []
        
        # 함수명 기반
        if 'test_' in node.name:
            tags.append('test')
        if 'validate' in node.name or 'check' in node.name:
            tags.append('validation')
        if 'process' in node.name or 'handle' in node.name:
            tags.append('processing')
        if 'get_' in node.name or 'fetch' in node.name:
            tags.append('getter')
        if 'set_' in node.name or 'update' in node.name:
            tags.append('setter')
        if 'create' in node.name or 'generate' in node.name:
            tags.append('creation')
        if 'delete' in node.name or 'remove' in node.name:
            tags.append('deletion')
        
        # 데코레이터 기반
        decorator_tags = {
            'property': 'property',
            'staticmethod': 'static',
            'classmethod': 'class_method',
            'abstractmethod': 'abstract',
            'cached_property': 'cached'
        }
        
        for dec in decorators:
            if dec in decorator_tags:
                tags.append(decorator_tags[dec])
        
        # 특성 기반
        if is_async:
            tags.append('async')
        if is_generator:
            tags.append('generator')
        
        return tags
    
    def _generate_tags_from_class(self, node: ast.ClassDef, base_classes: List[str], 
                                decorators: List[str]) -> List[str]:
        """클래스 기반 tags 생성"""
        tags = ['class']
        
        # 클래스명 패턴 기반
        if node.name.endswith('Error') or node.name.endswith('Exception'):
            tags.append('exception')
        if node.name.endswith('Manager') or node.name.endswith('Controller'):
            tags.append('manager')
        if node.name.endswith('Service'):
            tags.append('service')
        if node.name.endswith('Model') or node.name.endswith('Schema'):
            tags.append('model')
        if 'Test' in node.name:
            tags.append('test')
        
        # 상속 기반
        common_bases = {
            'BaseModel': 'pydantic',
            'FastAPI': 'fastapi',
            'HTTPException': 'exception',
            'Enum': 'enum',
            'ABC': 'abstract'
        }
        
        for base in base_classes:
            if base in common_bases:
                tags.append(common_bases[base])
        
        return tags
    
    def _extract_docstring(self, node: ast.AST, lines: List[str], start_line: int) -> str:
        """향상된 독스트링 추출 (AST + 텍스트 분석 결합)"""
        
        # 1. AST 방식으로 시도
        ast_docstring = ast.get_docstring(node)
        if ast_docstring and ast_docstring.strip():
            return ast_docstring.strip()
        
        # 2. 텍스트 파싱 방식으로 보완
        try:
            # 함수/클래스 정의 다음 줄부터 찾기
            search_start = start_line + 1
            if search_start >= len(lines):
                return ""
            
            # 첫 번째 문자열 리터럴 찾기
            for i in range(search_start, min(search_start + 10, len(lines))):
                line = lines[i].strip()
                
                # 빈 줄이나 주석 건너뛰기
                if not line or line.startswith('#'):
                    continue
                
                # 문자열 리터럴 패턴 매칭
                # """...""" 또는 '''...''' 또는 "..." 또는 '...'
                if (line.startswith('"""') or line.startswith("'''") or 
                    line.startswith('"') or line.startswith("'")):
                    
                    # 멀티라인 독스트링 처리
                    if line.startswith('"""') or line.startswith("'''"):
                        quote = '"""' if line.startswith('"""') else "'''"
                        docstring_lines = []
                        
                        # 같은 줄에 끝나는 경우
                        if line.count(quote) >= 2:
                            return line.replace(quote, '').strip()
                        
                        # 여러 줄에 걸친 경우
                        docstring_lines.append(line.replace(quote, ''))
                        for j in range(i + 1, min(i + 20, len(lines))):
                            next_line = lines[j]
                            if quote in next_line:
                                docstring_lines.append(next_line.split(quote)[0])
                                break
                            docstring_lines.append(next_line)
                        
                        return '\n'.join(docstring_lines).strip()
                    
                    # 단일 라인 독스트링
                    else:
                        quote = '"' if line.startswith('"') else "'"
                        if line.count(quote) >= 2:
                            return line.split(quote)[1].strip()
                
                # 다른 코드가 나오면 독스트링이 없는 것으로 판단
                elif not line.startswith('@'):  # 데코레이터는 제외
                    break
            
            return ""
        
        except Exception as e:
            print(f"독스트링 추출 오류: {e}")
            return ast_docstring or ""

    def _generate_chunk_id(self, file_path: str, chunk_type: str, name: str) -> str:
        """고유한 chunk ID 생성"""
        content = f"{file_path}:{chunk_type}:{name}"
        return hashlib.md5(content.encode()).hexdigest()[:12]


def chunk_directory(directory_path: str, max_tokens: int = 600) -> List[CodeChunk]:
    """
    디렉토리 내 모든 Python 파일을 chunk로 변환
    
    Args:
        directory_path: 분석할 디렉토리 경로
        max_tokens: chunk당 최대 토큰 수
        
    Returns:
        모든 파일의 CodeChunk 리스트
    """
    chunker = ASTChunker(max_tokens=max_tokens)
    all_chunks = []
    
    for root, dirs, files in os.walk(directory_path):
        # __pycache__ 등 제외
        dirs[:] = [d for d in dirs if not d.startswith('__')]
        
        for file in files:
            if file.endswith('.py'):
                file_path = os.path.join(root, file)
                print(f"📄 Processing: {file_path}")
                
                chunks = chunker.chunk_file(file_path)
                all_chunks.extend(chunks)
                
                print(f"   ✅ Generated {len(chunks)} chunks")
    
    print(f"\n🎯 총 {len(all_chunks)} 개의 chunk 생성 완료")
    return all_chunks


if __name__ == "__main__":
    # 테스트용
    sample_dir = "/Users/roseline/projects/codemuse-backend/sample_code"
    chunks = chunk_directory(sample_dir)
    
    # 결과 출력
    for chunk in chunks[:5]:  # 상위 5개만 출력
        print(f"\n--- {chunk.chunk_type}: {chunk.name} ---")
        print(f"파일: {chunk.file_path}")
        print(f"라인: {chunk.line_range}")
        print(f"토큰: {chunk.token_count}")
        print(f"태그: {chunk.tags}")
        print(f"내용 (처음 100자): {chunk.content[:100]}...")
