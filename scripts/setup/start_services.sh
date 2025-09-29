#!/bin/bash

# CodeMuse Backend Services Startup Script

echo "🚀 CodeMuse Backend Services 시작 중..."

# 환경 변수 파일 확인
if [ ! -f .env ]; then
    echo "⚠️  .env 파일이 없습니다. env.example을 복사합니다..."
    cp env.example .env
    echo "📝 .env 파일을 편집하여 필요한 설정을 완료하세요."
fi

# Docker Compose로 모든 서비스 시작
echo "🐳 Docker Compose로 서비스들을 시작합니다..."
docker-compose up -d

# 서비스 시작 대기
echo "⏳ 서비스들이 시작될 때까지 대기 중..."
sleep 30

# 서비스 상태 확인
echo "🔍 서비스 상태 확인 중..."
docker-compose ps

# 헬스 체크
echo "🏥 헬스 체크 실행 중..."
curl -f http://localhost:8005/health || echo "❌ Web Interface 서비스가 아직 준비되지 않았습니다."

echo "✅ CodeMuse Backend Services 시작 완료!"
echo ""
echo "📋 서비스 정보:"
echo "  - Document Generator: http://localhost:8002"
echo "  - RAG Engine: http://localhost:8003" 
echo "  - LLM Chat: http://localhost:8004"
echo "  - Web Interface: http://localhost:8005"
echo "  - Workflow Orchestrator: http://localhost:8006"
echo "  - Nginx (통합): http://localhost:80"
echo ""
echo "📚 API 문서:"
echo "  - Swagger UI: http://localhost:8005/docs"
echo ""
echo "🛑 서비스 중지: docker-compose down"
