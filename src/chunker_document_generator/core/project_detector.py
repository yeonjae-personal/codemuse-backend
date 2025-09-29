"""
프로젝트 타입 및 설정 파일 기반 언어/프레임워크 자동 감지
"""

import os
import json
import yaml
from typing import Dict, List, Optional, Tuple
from pathlib import Path


class ProjectDetector:
    """프로젝트 타입 및 설정 자동 감지"""
    
    def __init__(self, project_root: str):
        self.project_root = project_root
        self.detected_configs = {}
        self._detect_project_configs()
    
    def _detect_project_configs(self):
        """프로젝트 설정 파일들 감지"""
        config_files = {
            # Python 프로젝트
            'requirements.txt': 'python',
            'pyproject.toml': 'python',
            'setup.py': 'python',
            'Pipfile': 'python',
            'poetry.lock': 'python',
            
            # Node.js/JavaScript 프로젝트
            'package.json': 'javascript',
            'yarn.lock': 'javascript',
            'pnpm-lock.yaml': 'javascript',
            'bun.lockb': 'javascript',
            
            # Vue.js 프로젝트
            'vue.config.js': 'vue',
            'vite.config.js': 'vue',
            'nuxt.config.js': 'nuxt',
            'nuxt.config.ts': 'nuxt',
            
            # React 프로젝트
            'next.config.js': 'nextjs',
            'next.config.ts': 'nextjs',
            'craco.config.js': 'react',
            
            # Java 프로젝트
            'pom.xml': 'maven',
            'build.gradle': 'gradle',
            'gradle.properties': 'gradle',
            'settings.gradle': 'gradle',
            
            # Spring Boot 프로젝트
            'application.yml': 'spring-boot',
            'application.yaml': 'spring-boot',
            'application.properties': 'spring-boot',
            'bootstrap.yml': 'spring-boot',
            'bootstrap.yaml': 'spring-boot',
            'bootstrap.properties': 'spring-boot',
        }
        
        for config_file, project_type in config_files.items():
            config_path = os.path.join(self.project_root, config_file)
            if os.path.exists(config_path):
                self.detected_configs[config_file] = {
                    'type': project_type,
                    'path': config_path,
                    'content': self._read_config_file(config_path)
                }
    
    def _read_config_file(self, file_path: str) -> Optional[Dict]:
        """설정 파일 내용 읽기"""
        try:
            with open(file_path, 'r', encoding='utf-8') as f:
                if file_path.endswith('.json'):
                    return json.load(f)
                elif file_path.endswith(('.yml', '.yaml')):
                    return yaml.safe_load(f)
                elif file_path.endswith('.toml'):
                    import toml
                    return toml.load(f)
                else:
                    # 텍스트 파일인 경우
                    return {'raw_content': f.read()}
        except Exception as e:
            print(f"⚠️ 설정 파일 읽기 실패 {file_path}: {e}")
            return None
    
    def get_project_type(self) -> str:
        """주요 프로젝트 타입 반환"""
        if 'pom.xml' in self.detected_configs or 'build.gradle' in self.detected_configs:
            return 'java'
        elif 'package.json' in self.detected_configs:
            return 'javascript'
        elif 'requirements.txt' in self.detected_configs or 'pyproject.toml' in self.detected_configs:
            return 'python'
        else:
            return 'unknown'
    
    def get_framework_info(self) -> Dict[str, str]:
        """프레임워크 정보 반환"""
        framework_info = {}
        
        # Spring Boot 감지
        if any(config in self.detected_configs for config in 
               ['application.yml', 'application.yaml', 'application.properties']):
            framework_info['java'] = 'spring-boot'
        
        # Vue.js 감지
        if 'vue.config.js' in self.detected_configs or 'vite.config.js' in self.detected_configs:
            framework_info['javascript'] = 'vue'
        elif 'next.config.js' in self.detected_configs or 'next.config.ts' in self.detected_configs:
            framework_info['javascript'] = 'nextjs'
        elif 'package.json' in self.detected_configs:
            package_info = self.detected_configs['package.json']['content']
            if package_info and 'dependencies' in package_info:
                deps = package_info['dependencies']
                if 'react' in deps:
                    framework_info['javascript'] = 'react'
                elif 'vue' in deps:
                    framework_info['javascript'] = 'vue'
                elif 'angular' in deps:
                    framework_info['javascript'] = 'angular'
        
        # Python 프레임워크 감지
        if 'requirements.txt' in self.detected_configs:
            req_content = self.detected_configs['requirements.txt']['content']
            if req_content and 'raw_content' in req_content:
                content = req_content['raw_content']
                if 'fastapi' in content:
                    framework_info['python'] = 'fastapi'
                elif 'django' in content:
                    framework_info['python'] = 'django'
                elif 'flask' in content:
                    framework_info['python'] = 'flask'
        
        return framework_info
    
    def get_spring_boot_info(self) -> Optional[Dict]:
        """Spring Boot 프로젝트 상세 정보"""
        if 'application.yml' in self.detected_configs:
            config = self.detected_configs['application.yml']['content']
            return {
                'config_type': 'yaml',
                'server_port': config.get('server', {}).get('port', 8080),
                'spring_profiles': config.get('spring', {}).get('profiles', {}).get('active', 'default'),
                'datasource': config.get('spring', {}).get('datasource', {}),
                'jpa': config.get('spring', {}).get('jpa', {})
            }
        elif 'application.properties' in self.detected_configs:
            config = self.detected_configs['application.properties']['content']
            return {
                'config_type': 'properties',
                'raw_config': config.get('raw_content', '')
            }
        return None
    
    def get_vue_info(self) -> Optional[Dict]:
        """Vue.js 프로젝트 상세 정보"""
        if 'package.json' in self.detected_configs:
            package_info = self.detected_configs['package.json']['content']
            if package_info and 'dependencies' in package_info:
                deps = package_info['dependencies']
                return {
                    'vue_version': deps.get('vue', 'unknown'),
                    'typescript': 'typescript' in deps,
                    'router': 'vue-router' in deps,
                    'state_management': 'vuex' in deps or 'pinia' in deps,
                    'ui_framework': self._detect_ui_framework(deps)
                }
        return None
    
    def _detect_ui_framework(self, dependencies: Dict[str, str]) -> Optional[str]:
        """UI 프레임워크 감지"""
        ui_frameworks = {
            'element-plus': 'element-plus',
            'ant-design-vue': 'ant-design-vue',
            'vuetify': 'vuetify',
            'quasar': 'quasar',
            'bootstrap-vue': 'bootstrap-vue',
            'primevue': 'primevue'
        }
        
        for dep, framework in ui_frameworks.items():
            if dep in dependencies:
                return framework
        return None
    
    def get_java_package_structure(self) -> List[str]:
        """Java 패키지 구조 추출"""
        java_dirs = []
        for root, dirs, files in os.walk(self.project_root):
            # src/main/java, src/test/java 등 Java 소스 디렉토리 찾기
            if 'src' in root and any(f.endswith('.java') for f in files):
                java_dirs.append(root)
        return java_dirs
    
    def get_javascript_source_dirs(self) -> List[str]:
        """JavaScript 소스 디렉토리 추출"""
        js_dirs = []
        for root, dirs, files in os.walk(self.project_root):
            # node_modules 제외
            if 'node_modules' not in root:
                if any(f.endswith(('.js', '.vue', '.ts', '.jsx', '.tsx')) for f in files):
                    js_dirs.append(root)
        return js_dirs
    
    def should_process_file(self, file_path: str) -> Tuple[bool, str, Optional[str]]:
        """파일 처리 여부 및 언어/프레임워크 결정"""
        file_ext = os.path.splitext(file_path)[1].lower()
        
        # 확장자 기반 기본 판단
        if file_ext == '.java':
            return True, 'java', self.get_framework_info().get('java')
        elif file_ext in ['.js', '.vue', '.ts', '.jsx', '.tsx']:
            return True, 'javascript', self.get_framework_info().get('javascript')
        elif file_ext == '.py':
            return True, 'python', self.get_framework_info().get('python')
        
        return False, 'unknown', None
    
    def get_processing_hints(self) -> Dict[str, any]:
        """처리 힌트 정보 반환"""
        hints = {
            'project_type': self.get_project_type(),
            'framework_info': self.get_framework_info(),
            'detected_configs': list(self.detected_configs.keys())
        }
        
        # Spring Boot 특화 힌트
        if 'java' in hints['framework_info'] and hints['framework_info']['java'] == 'spring-boot':
            hints['spring_boot_info'] = self.get_spring_boot_info()
            hints['java_package_dirs'] = self.get_java_package_structure()
        
        # Vue.js 특화 힌트
        if 'javascript' in hints['framework_info'] and hints['framework_info']['javascript'] == 'vue':
            hints['vue_info'] = self.get_vue_info()
            hints['javascript_source_dirs'] = self.get_javascript_source_dirs()
        
        return hints
