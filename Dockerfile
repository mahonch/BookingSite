# Multi-stage Dockerfile для Forum Booking System
# Backend (Spring Boot) + Frontend (Vue.js)

# ============================================
# Stage 1: Build Frontend
# ============================================
FROM node:20-alpine AS frontend-build
WORKDIR /app/frontend

# Копируем package files
COPY frontend/package*.json ./
RUN npm install

# Копируем исходный код и билдим
COPY frontend/ ./
RUN npm run build

# ============================================
# Stage 2: Build Backend
# ============================================
FROM maven:3.9-eclipse-temurin-21 AS backend-build
WORKDIR /app/backend

# Копируем pom.xml и кэшируем зависимости
COPY backend/pom.xml ./
RUN mvn dependency:go-offline

# Копируем исходный код и билдим
COPY backend/src ./src
RUN mvn clean package -DskipTests

# ============================================
# Stage 3: Production Image
# ============================================
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Копируем JAR backend
COPY --from=backend-build /app/backend/target/*.jar app.jar

# Копируем статические файлы frontend в resources/static
COPY --from=frontend-build /app/frontend/dist ./static

# Переменные окружения
ENV SPRING_PROFILES_ACTIVE=production
ENV SERVER_PORT=8080

# Открываем порт
EXPOSE 8080

# Запускаем приложение с указанием профиля
ENTRYPOINT ["java", "-Dspring.profiles.active=production", "-jar", "app.jar"]
