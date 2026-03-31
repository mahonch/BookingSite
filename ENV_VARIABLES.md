# 📋 Переменные окружения для Render

## Frontend (Static Site)

**File:** `frontend/.env` (уже создан)

| Key | Value |
|-----|-------|
| `VITE_API_BASE_URL` | `https://forum-booking-backend.onrender.com/api` |

---

## Backend (Web Service)

Добавьте в Render Dashboard → Backend → Environment:

| Key | Value | Примечание |
|-----|-------|------------|
| `SPRING_DATASOURCE_URL` | `postgresql://user:pass@host:5432/forum_booking` | Из PostgreSQL |
| `SPRING_DATASOURCE_USERNAME` | `forum_user` | |
| `SPRING_DATASOURCE_PASSWORD` | `ваш_пароль` | Из PostgreSQL |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | `update` | |
| `SPRING_SQL_INIT_MODE` | `never` | |
| `APP_JWT_SECRET` | `ForumBookingSecretKey2026Production!` | |
| `APP_CORS_ALLOWED_ORIGINS` | `*` | Потом замените на URL frontend |

---

## 🚀 Быстрое копирование

### Frontend (1 переменная):
```
VITE_API_BASE_URL=https://forum-booking-backend.onrender.com/api
```

### Backend (7 переменных):
```
SPRING_DATASOURCE_URL=<Internal Database URL>
SPRING_DATASOURCE_USERNAME=forum_user
SPRING_DATASOURCE_PASSWORD=<ваш пароль>
SPRING_JPA_HIBERNATE_DDL_AUTO=update
SPRING_SQL_INIT_MODE=never
APP_JWT_SECRET=ForumBookingSecretKey2026Production!
APP_CORS_ALLOWED_ORIGINS=*
```

---

## ✅ Проверка

Файлы созданы:
- ✅ `frontend/.env` - для локальной разработки
- ✅ `frontend/.env.production` - для production
- ✅ `backend/.env.example` - шаблон для backend
- ✅ `ENV_VARIABLES.md` - эта инструкция

**Теперь не нужно писать переменные вручную!** 🎉
