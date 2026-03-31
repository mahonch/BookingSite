# 🎯 Краткая инструкция по деплою на Render

## Быстрый старт (5 минут)

### 1. Создайте PostgreSQL базу
- Render Dashboard → New → PostgreSQL
- Name: `forum-db`
- Сохраните **Internal Database URL**

### 2. Задеплойте Backend
- New → Web Service
- Подключите GitHub с вашим репозиторием
- Settings:
  - **Root Directory:** `backend`
  - **Build Command:** `mvn clean package -DskipTests`
  - **Start Command:** `java -jar target/booking-backend-1.0.0.jar`
  - **Region:** Frankfurt

- Environment Variables:
  ```
  SPRING_DATASOURCE_URL=<Internal Database URL>
  SPRING_DATASOURCE_USERNAME=forum_user
  SPRING_DATASOURCE_PASSWORD=<пароль от БД>
  SPRING_JPA_HIBERNATE_DDL_AUTO=update
  SPRING_SQL_INIT_MODE=never
  APP_JWT_SECRET=MySecretKey2026ChangeMe!
  APP_CORS_ALLOWED_ORIGINS=*
  ```

### 3. Задеплойте Frontend
- New → Static Site
- Подключите тот же репозиторий
- Settings:
  - **Root Directory:** `frontend`
  - **Build Command:** `npm install && npm run build`
  - **Publish Directory:** `dist`

- Environment Variables:
  ```
  VITE_API_BASE_URL=https://<ваш-backend-url>.onrender.com/api
  ```

### 4. Готово!
- Frontend URL: `https://<имя>.onrender.com`
- Admin: `https://<имя>.onrender.com/admin/register`

---

## 📁 Файлы для деплоя

Проект содержит все необходимые файлы:

```
BookingSite/
├── render.yaml              # Конфигурация для Render (backend + DB)
├── DEPLOY.md                # Подробная инструкция
├── backend/
│   └── src/main/resources/
│       ├── application.properties        # Dev настройки
│       └── application-production.properties  # Production настройки
└── frontend/
    ├── .env                   # Dev настройки
    ├── .env.production        # Production настройки
    └── render-frontend.yaml   # Конфигурация frontend
```

---

## 🔗 Ссылки

- **Render Dashboard:** https://dashboard.render.com
- **Документация:** https://render.com/docs
