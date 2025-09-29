#!/bin/bash

# CodeMuse Backend Services Stop Script

echo "🛑 CodeMuse Backend Services 중지 중..."

# Docker Compose로 모든 서비스 중지
docker-compose down

echo "✅ CodeMuse Backend Services 중지 완료!"
