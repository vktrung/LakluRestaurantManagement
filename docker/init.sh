#!/bin/sh

echo "🕒 Đợi ứng dụng Spring Boot khởi động..."
sleep 20  # Chờ ứng dụng Spring Boot khởi động hoàn tất

echo "🚀 Gọi API setup permissions..."
curl -X POST http://localhost:8080/api/v1/setup/permissions

echo "🚀 Gọi API setup super-admin..."
curl -X POST http://localhost:8080/api/v1/setup/super-admin

echo "✅ API đã được gọi thành công!"
