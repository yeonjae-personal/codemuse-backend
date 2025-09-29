"""
프로젝트 구조 및 기술 스택 분석기
"""

import os
import re
from typing import Dict, List, Any, Optional, Tuple
from dataclasses import dataclass
from pathlib import Path


@dataclass
class TechStack:
    """기술 스택 정보"""
    category: str
    technology: str
    version: Optional[str] = None
    note: Optional[str] = None


@dataclass
class Component:
    """주요 컴포넌트 정보"""
    name: str
    type: str
    file_path: str
    description: str


class ProjectAnalyzer:
    """프로젝트 구조 및 기술 스택 분석기"""
    
    def __init__(self, project_root: str):
        self.project_root = project_root
        self.config_files = {
            'python': ['requirements.txt', 'pyproject.toml', 'setup.py', 'Pipfile'],
            'javascript': ['package.json', 'yarn.lock', 'package-lock.json'],
            'java': ['pom.xml', 'build.gradle', 'gradle.properties'],
            'vue': ['vue.config.js', 'vite.config.js', 'nuxt.config.js']
        }
        
        self.framework_patterns = {
            'python': {
                'fastapi': [r'from fastapi import', r'@app\.', r'FastAPI\('],
                'django': [r'from django import', r'Django\(', r'INSTALLED_APPS'],
                'flask': [r'from flask import', r'Flask\(', r'@app\.route']
            },
            'javascript': {
                'vue': [r'<template>', r'export default', r'Vue\.'],
                'react': [r'import React', r'JSX', r'useState', r'useEffect'],
                'node': [r'require\(', r'module\.exports', r'process\.env']
            },
            'java': {
                'spring-boot': [r'@SpringBootApplication', r'@RestController', r'@Service', r'@Repository'],
                'spring': [r'@Controller', r'@Service', r'@Component'],
                'jpa': [r'@Entity', r'@Table', r'@Id', r'@Column']
            }
        }
    
    def analyze_project(self) -> Dict[str, Any]:
        """프로젝트 전체 분석"""
        return {
            'project_type': self._detect_project_type(),
            'detected_stacks': self._detect_tech_stacks(),
            'main_components': self._find_main_components(),
            'directory_structure': self._analyze_directory_structure(),
            'dependencies': self._extract_dependencies(),
            'run_commands': self._generate_run_commands(),
            'dev_setup': self._generate_dev_setup(),
            'language_stats': self._analyze_language_distribution(),
            'architecture_patterns': self._detect_architecture_patterns(),
            'framework_features': self._analyze_framework_features(),
            'dev_tools': self._detect_dev_tools()
        }
    
    def _detect_project_type(self) -> str:
        """프로젝트 타입 감지"""
        # 설정 파일 기반 감지
        for lang, files in self.config_files.items():
            for file in files:
                if self._find_file(file):
                    if lang == 'python':
                        return 'python'
                    elif lang == 'javascript':
                        return 'javascript'
                    elif lang == 'java':
                        return 'java'
        
        # 디렉토리 구조 기반 감지
        if os.path.exists(os.path.join(self.project_root, 'src', 'main', 'java')):
            return 'java'
        elif os.path.exists(os.path.join(self.project_root, 'src', 'components')):
            return 'javascript'
        elif os.path.exists(os.path.join(self.project_root, 'src', 'app')):
            return 'python'
        
        return 'mixed'
    
    def _detect_tech_stacks(self) -> List[TechStack]:
        """기술 스택 감지"""
        stacks = []
        
        # Python 스택
        if self._has_python_files():
            stacks.append(TechStack('Backend', 'Python', note='주요 언어'))
            
            # 프레임워크 감지
            if self._detect_framework('python', 'fastapi'):
                stacks.append(TechStack('Web Framework', 'FastAPI'))
            elif self._detect_framework('python', 'django'):
                stacks.append(TechStack('Web Framework', 'Django'))
            elif self._detect_framework('python', 'flask'):
                stacks.append(TechStack('Web Framework', 'Flask'))
        
        # JavaScript 스택
        if self._has_javascript_files():
            stacks.append(TechStack('Frontend', 'JavaScript', note='클라이언트 언어'))
            
            if self._detect_framework('javascript', 'vue'):
                stacks.append(TechStack('Frontend Framework', 'Vue.js'))
            elif self._detect_framework('javascript', 'react'):
                stacks.append(TechStack('Frontend Framework', 'React'))
        
        # Java 스택
        if self._has_java_files():
            stacks.append(TechStack('Backend', 'Java', note='엔터프라이즈 언어'))
            
            if self._detect_framework('java', 'spring-boot'):
                stacks.append(TechStack('Framework', 'Spring Boot'))
            elif self._detect_framework('java', 'spring'):
                stacks.append(TechStack('Framework', 'Spring'))
        
        return stacks
    
    def _find_main_components(self) -> List[Component]:
        """주요 컴포넌트 찾기"""
        components = []
        
        for root, dirs, files in os.walk(self.project_root):
            for file in files:
                if file.endswith('.py'):
                    if 'service' in file or 'controller' in file or 'manager' in file:
                        components.append(Component(
                            name=file.replace('.py', ''),
                            type='Python Service',
                            file_path=os.path.join(root, file),
                            description=f'Python 서비스 컴포넌트: {file}'
                        ))
                elif file.endswith('.java'):
                    if 'Controller' in file or 'Service' in file:
                        components.append(Component(
                            name=file.replace('.java', ''),
                            type='Java Component',
                            file_path=os.path.join(root, file),
                            description=f'Java 컴포넌트: {file}'
                        ))
                elif file.endswith('.vue'):
                    components.append(Component(
                        name=file.replace('.vue', ''),
                        type='Vue Component',
                        file_path=os.path.join(root, file),
                        description=f'Vue.js 컴포넌트: {file}'
                    ))
        
        return components[:10]  # 상위 10개만
    
    def _analyze_directory_structure(self) -> str:
        """디렉토리 구조 분석"""
        structure = []
        for root, dirs, files in os.walk(self.project_root):
            level = root.replace(self.project_root, '').count(os.sep)
            indent = ' ' * 2 * level
            structure.append(f"{indent}{os.path.basename(root)}/")
            
            # 파일은 최대 3개까지만 표시
            for file in files[:3]:
                structure.append(f"{indent}  {file}")
            if len(files) > 3:
                structure.append(f"{indent}  ... ({len(files) - 3} more files)")
        
        return '\n'.join(structure[:20])  # 최대 20줄
    
    def _extract_dependencies(self) -> Dict[str, List[Dict[str, str]]]:
        """의존성 정보 추출"""
        dependencies = {}
        
        # Python 의존성
        req_file = self._find_file('requirements.txt')
        if req_file:
            deps = []
            with open(req_file, 'r') as f:
                for line in f:
                    line = line.strip()
                    if line and not line.startswith('#'):
                        name = line.split('==')[0].split('>=')[0].split('<=')[0]
                        version = line.split('==')[1] if '==' in line else None
                        deps.append({'name': name, 'version': version})
            dependencies['Python'] = deps
        
        return dependencies
    
    def _generate_run_commands(self) -> List[str]:
        """실행 명령어 생성"""
        commands = []
        
        if self._has_python_files():
            commands.append("python -m uvicorn main:app --reload  # FastAPI")
            commands.append("python manage.py runserver  # Django")
        
        if self._has_javascript_files():
            commands.append("npm run dev  # Vue.js/React")
            commands.append("npm start  # Node.js")
        
        if self._has_java_files():
            commands.append("mvn spring-boot:run  # Spring Boot")
            commands.append("./gradlew bootRun  # Gradle")
        
        return commands
    
    def _generate_dev_setup(self) -> List[str]:
        """개발 환경 설정 가이드"""
        setup = []
        
        if self._has_python_files():
            setup.extend([
                "Python 3.8+ 설치",
                "가상환경 생성: python -m venv venv",
                "가상환경 활성화: source venv/bin/activate (Linux/Mac) 또는 venv\\Scripts\\activate (Windows)",
                "의존성 설치: pip install -r requirements.txt"
            ])
        
        if self._has_javascript_files():
            setup.extend([
                "Node.js 16+ 설치",
                "의존성 설치: npm install",
                "개발 서버 실행: npm run dev"
            ])
        
        if self._has_java_files():
            setup.extend([
                "Java 11+ 설치",
                "Maven 또는 Gradle 설치",
                "의존성 설치: mvn install 또는 ./gradlew build"
            ])
        
        return setup
    
    def _analyze_language_distribution(self) -> List[Dict[str, Any]]:
        """언어별 분포 분석"""
        stats = {}
        total_files = 0
        
        for root, dirs, files in os.walk(self.project_root):
            for file in files:
                if file.endswith('.py'):
                    stats['python'] = stats.get('python', 0) + 1
                    total_files += 1
                elif file.endswith(('.js', '.jsx', '.ts', '.tsx', '.vue')):
                    stats['javascript'] = stats.get('javascript', 0) + 1
                    total_files += 1
                elif file.endswith('.java'):
                    stats['java'] = stats.get('java', 0) + 1
                    total_files += 1
        
        result = []
        for lang, count in stats.items():
            frameworks = self._get_frameworks_for_language(lang)
            result.append({
                'language': lang,
                'file_count': count,
                'percentage': round((count / total_files) * 100, 1) if total_files > 0 else 0,
                'frameworks': frameworks
            })
        
        return result
    
    def _detect_architecture_patterns(self) -> List[Dict[str, Any]]:
        """아키텍처 패턴 감지"""
        patterns = []
        
        # MVC 패턴
        if self._has_controller_files() and self._has_service_files():
            patterns.append({
                'name': 'MVC Pattern',
                'description': 'Model-View-Controller 아키텍처',
                'files': self._find_files_by_pattern(['controller', 'service', 'model']),
                'features': ['Controller Layer', 'Service Layer', 'Model Layer']
            })
        
        # 모듈 패턴
        if self._has_module_structure():
            patterns.append({
                'name': 'Module Pattern',
                'description': '모듈 기반 구조',
                'files': self._find_module_files(),
                'features': ['Modular Design', 'Separation of Concerns']
            })
        
        return patterns
    
    def _analyze_framework_features(self) -> Dict[str, List[Dict[str, str]]]:
        """프레임워크별 특화 기능 분석"""
        features = {}
        
        if self._detect_framework('python', 'fastapi'):
            features['FastAPI'] = [
                {'name': 'REST API', 'description': '자동 API 문서화 및 타입 검증'},
                {'name': 'Async Support', 'description': '비동기 처리 지원'},
                {'name': 'Pydantic', 'description': '데이터 검증 및 직렬화'}
            ]
        
        if self._detect_framework('javascript', 'vue'):
            features['Vue.js'] = [
                {'name': 'Component System', 'description': '재사용 가능한 컴포넌트'},
                {'name': 'Reactive Data', 'description': '반응형 데이터 바인딩'},
                {'name': 'Single File Component', 'description': '템플릿, 스크립트, 스타일 통합'}
            ]
        
        if self._detect_framework('java', 'spring-boot'):
            features['Spring Boot'] = [
                {'name': 'Auto Configuration', 'description': '자동 설정 및 의존성 주입'},
                {'name': 'REST Controller', 'description': 'RESTful API 개발'},
                {'name': 'Data JPA', 'description': '데이터베이스 연동'}
            ]
        
        return features
    
    def _detect_dev_tools(self) -> List[Dict[str, Any]]:
        """개발 도구 감지"""
        tools = []
        
        # Git
        if os.path.exists(os.path.join(self.project_root, '.git')):
            tools.append({
                'name': 'Git',
                'description': '버전 관리 시스템',
                'config_files': ['.gitignore', '.gitattributes'],
                'purpose': '소스 코드 버전 관리'
            })
        
        # Docker
        if self._find_file('Dockerfile') or self._find_file('docker-compose.yml'):
            tools.append({
                'name': 'Docker',
                'description': '컨테이너화 플랫폼',
                'config_files': ['Dockerfile', 'docker-compose.yml'],
                'purpose': '애플리케이션 컨테이너화'
            })
        
        return tools
    
    # 헬퍼 메서드들
    def _find_file(self, filename: str) -> Optional[str]:
        """파일 찾기"""
        for root, dirs, files in os.walk(self.project_root):
            if filename in files:
                return os.path.join(root, filename)
        return None
    
    def _has_python_files(self) -> bool:
        """Python 파일 존재 여부"""
        for root, dirs, files in os.walk(self.project_root):
            if any(f.endswith('.py') for f in files):
                return True
        return False
    
    def _has_javascript_files(self) -> bool:
        """JavaScript 파일 존재 여부"""
        for root, dirs, files in os.walk(self.project_root):
            if any(f.endswith(('.js', '.jsx', '.ts', '.tsx', '.vue')) for f in files):
                return True
        return False
    
    def _has_java_files(self) -> bool:
        """Java 파일 존재 여부"""
        for root, dirs, files in os.walk(self.project_root):
            if any(f.endswith('.java') for f in files):
                return True
        return False
    
    def _detect_framework(self, language: str, framework: str) -> bool:
        """프레임워크 감지"""
        patterns = self.framework_patterns.get(language, {}).get(framework, [])
        
        for root, dirs, files in os.walk(self.project_root):
            for file in files:
                if (language == 'python' and file.endswith('.py')) or \
                   (language == 'javascript' and file.endswith(('.js', '.jsx', '.ts', '.tsx', '.vue'))) or \
                   (language == 'java' and file.endswith('.java')):
                    
                    file_path = os.path.join(root, file)
                    try:
                        with open(file_path, 'r', encoding='utf-8', errors='ignore') as f:
                            content = f.read()
                            if any(re.search(pattern, content) for pattern in patterns):
                                return True
                    except:
                        continue
        return False
    
    def _get_frameworks_for_language(self, language: str) -> List[str]:
        """언어별 프레임워크 목록"""
        frameworks = []
        lang_frameworks = self.framework_patterns.get(language, {})
        
        for framework in lang_frameworks:
            if self._detect_framework(language, framework):
                frameworks.append(framework)
        
        return frameworks
    
    def _has_controller_files(self) -> bool:
        """Controller 파일 존재 여부"""
        for root, dirs, files in os.walk(self.project_root):
            if any('controller' in f.lower() for f in files):
                return True
        return False
    
    def _has_service_files(self) -> bool:
        """Service 파일 존재 여부"""
        for root, dirs, files in os.walk(self.project_root):
            if any('service' in f.lower() for f in files):
                return True
        return False
    
    def _has_module_structure(self) -> bool:
        """모듈 구조 존재 여부"""
        return os.path.exists(os.path.join(self.project_root, 'src'))
    
    def _find_files_by_pattern(self, patterns: List[str]) -> List[str]:
        """패턴에 맞는 파일 찾기"""
        files = []
        for root, dirs, filenames in os.walk(self.project_root):
            for filename in filenames:
                if any(pattern in filename.lower() for pattern in patterns):
                    files.append(os.path.join(root, filename))
        return files
    
    def _find_module_files(self) -> List[str]:
        """모듈 파일 찾기"""
        files = []
        for root, dirs, filenames in os.walk(self.project_root):
            if 'src' in root:
                for filename in filenames:
                    if filename.endswith('.py'):
                        files.append(os.path.join(root, filename))
        return files
