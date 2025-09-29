"""
MD 문서에서 용어집을 추출하는 모듈
"""

import os
import json
import re
from typing import Dict, List, Set
from pathlib import Path


class VocabularyExtractor:
    """MD 문서에서 용어집을 추출하는 클래스"""
    
    def __init__(self, generated_docs_path: str = None):
        if generated_docs_path is None:
            # 기본 경로 설정
            current_dir = os.path.dirname(os.path.dirname(os.path.dirname(__file__)))
            self.generated_docs_path = os.path.join(current_dir, "..", "generated_docs")
        else:
            self.generated_docs_path = generated_docs_path
        
        # 용어집 파일 경로
        self.vocabulary_file = os.path.join(self.generated_docs_path, "vocabulary.json")
    
    def load_vocabulary(self) -> Dict[str, List[str]]:
        """생성된 용어집 파일을 로드"""
        
        try:
            # 먼저 생성된 용어집 파일이 있는지 확인
            if os.path.exists(self.vocabulary_file):
                with open(self.vocabulary_file, 'r', encoding='utf-8') as f:
                    vocabulary = json.load(f)
                print(f"📚 용어집 파일 로드 완료: {sum(len(v) for v in vocabulary.values())}개 용어")
                return vocabulary
            
            # 용어집 파일이 없으면 MD 파일에서 추출 (폴백)
            print("⚠️ 용어집 파일이 없습니다. MD 파일에서 추출합니다...")
            vocabulary = {
                "class_names": [],
                "method_names": [],
                "function_names": [],
                "domain_concepts": [],
                "korean_terms": [],
                "technical_terms": []
            }
            
            # MD 파일들 스캔
            for root, dirs, files in os.walk(self.generated_docs_path):
                for file in files:
                    if file.endswith('.md'):
                        file_path = os.path.join(root, file)
                        self._extract_from_md_file(file_path, vocabulary)
            
            # 중복 제거 및 정렬
            for key in vocabulary:
                vocabulary[key] = sorted(list(set(vocabulary[key])))
            
            print(f"📚 MD에서 용어집 추출 완료: {sum(len(v) for v in vocabulary.values())}개 용어")
            return vocabulary
            
        except Exception as e:
            print(f"❌ 용어집 로드 실패: {e}")
            return {
                "class_names": [],
                "method_names": [],
                "function_names": [],
                "domain_concepts": [],
                "korean_terms": [],
                "technical_terms": []
            }
    
    def _extract_from_md_file(self, file_path: str, vocabulary: Dict[str, List[str]]):
        """MD 파일에서 용어 추출"""
        
        try:
            with open(file_path, 'r', encoding='utf-8') as f:
                content = f.read()
            
            # 클래스명 추출 (### 클래스명 패턴)
            class_pattern = r'###\s+`([A-Z][a-zA-Z0-9_]*)`'
            classes = re.findall(class_pattern, content)
            vocabulary["class_names"].extend(classes)
            
            # 메서드명 추출 (#### 메서드명 패턴)
            method_pattern = r'####\s+`([a-zA-Z_][a-zA-Z0-9_]*)`'
            methods = re.findall(method_pattern, content)
            vocabulary["method_names"].extend(methods)
            
            # 함수명 추출 (### 함수명 패턴)
            function_pattern = r'###\s+`([a-zA-Z_][a-zA-Z0-9_]*)`'
            functions = re.findall(function_pattern, content)
            vocabulary["function_names"].extend(functions)
            
            # 기술 용어 추출 (코드 블록 내)
            code_pattern = r'```python\n(.*?)\n```'
            code_blocks = re.findall(code_pattern, content, re.DOTALL)
            for code in code_blocks:
                # 클래스/함수 정의 추출
                class_defs = re.findall(r'class\s+([A-Z][a-zA-Z0-9_]*)', code)
                method_defs = re.findall(r'def\s+([a-zA-Z_][a-zA-Z0-9_]*)', code)
                vocabulary["class_names"].extend(class_defs)
                vocabulary["method_names"].extend(method_defs)
            
            # 도메인 개념 추출 (한글 + 영어 혼합)
            domain_pattern = r'[가-힣]+(?:[A-Za-z]+|[가-힣]+)*'
            domains = re.findall(domain_pattern, content)
            vocabulary["domain_concepts"].extend(domains)
            
            # 한국어 기술 용어 추출
            korean_tech_terms = [
                '검출', '분석', '처리', '생성', '변환', '검증', '오류', '이슈',
                '조건', '규칙', '로직', '타입', '불일치', '중복', '복잡성',
                '파싱', '렌더링', '포맷팅', '스트리밍', '임베딩', '벡터',
                '검색', '인덱싱', '토큰화', '전처리', '후처리'
            ]
            
            for term in korean_tech_terms:
                if term in content:
                    vocabulary["korean_terms"].append(term)
            
            # 영어 기술 용어 추출
            tech_terms = [
                'detection', 'analysis', 'processing', 'generation', 'transformation',
                'validation', 'error', 'issue', 'condition', 'rule', 'logic', 'type',
                'mismatch', 'duplicate', 'complexity', 'parsing', 'rendering', 'formatting',
                'streaming', 'embedding', 'vector', 'search', 'indexing', 'tokenization',
                'preprocessing', 'postprocessing', 'chunking', 'rag', 'llm', 'ai'
            ]
            
            for term in tech_terms:
                if term.lower() in content.lower():
                    vocabulary["technical_terms"].append(term)
            
        except Exception as e:
            print(f"❌ MD 파일 처리 실패 {file_path}: {e}")
    
    def save_vocabulary(self, vocabulary: Dict[str, List[str]], output_path: str = None):
        """용어집을 파일로 저장"""
        
        if output_path is None:
            output_path = os.path.join(self.generated_docs_path, "vocabulary.json")
        
        try:
            with open(output_path, 'w', encoding='utf-8') as f:
                json.dump(vocabulary, f, ensure_ascii=False, indent=2)
            
            print(f"💾 용어집 저장 완료: {output_path}")
            
        except Exception as e:
            print(f"❌ 용어집 저장 실패: {e}")


# 전역 인스턴스
vocabulary_extractor = VocabularyExtractor()
