# 🚀 Деплой на Render.com

## Инструкция по шагам

### 1. Подготовка

1. Зарегистрируйтесь на [Render.com](https://render.com)
2. Установите CLI (опционально): `npm install -g @render-cloud/cli`

### 2. Создание базы данных PostgreSQL

1. В дашборде Render нажмите **New** → **PostgreSQL**
2. Заполните:
   - **Name:** `forum-db`
   - **Database:** `forum_booking`
   - **User:** `forum_user`
   - **Region:** Frankfurt (Europe)
   - **Plan:** Free (до 90 дней)
3. Нажмите **Create Database**
4. Сохраните **Internal Database URL** (понадобится позже)

### 3. Деплой Backend (Spring Boot)

1. В дашборде нажмите **New** → **Web Service**
2. Подключите ваш GitHub репозиторий с проектом
3. Заполните настройки:

   ```
   Name: forum-booking-backend
   Region: Frankfurt
   Branch: main
   Root Directory: backend
   Runtime: Java
   Build Command: mvn clean package -DskipTests
   Start Command: java -jar target/booking-backend-1.0.0.jar
   ```

4. **Environment Variables** (добавьте вручную):

   ```
   SPRING_JPA_HIBERNATE_DDL_AUTO=update
   SPRING_SQL_INIT_MODE=never
   APP_JWT_SECRET=YourSecretKey2026ChangeThis!
   APP_CORS_ALLOWED_ORIGINS=https://forum-booking-frontend.onrender.com
   ```

5. **Database** (добавьте переменные из шага 2):

   ```
   SPRING_DATASOURCE_URL=<Internal Database URL из шага 2>
   SPRING_DATASOURCE_USERNAME=forum_user
   SPRING_DATASOURCE_PASSWORD=<пароль из шага 2>
   ```

6. Нажмите **Create Web Service**
7. Дождитесь деплоя (~5 минут)

### 4. Деплой Frontend (Vue.js)

#### Вариант A: Static Site (рекомендуется)

1. В дашборде нажмите **New** → **Static Site**
2. Подключите ваш GitHub репозиторий
3. Заполните настройки:

   ```
   Name: forum-booking-frontend
   Branch: main
   Root Directory: frontend
   Build Command: npm install && npm run build
   Publish Directory: dist
   ```

4. **Environment Variables**:

   ```
   VITE_API_BASE_URL=https://forum-booking-backend.onrender.com/api
   ```

5. Нажмите **Create Static Site**
6. Дождитесь деплоя (~2 минуты)

#### Вариант B: Web Service (альтернатива)

1. **New** → **Web Service**
2. Настройки:

   ```
   Name: forum-booking-frontend
   Root Directory: frontend
   Build Command: npm install && npm run build
   Start Command: npx serve dist -p $PORT
   ```

### 5. Настройка CORS

После деплоя backend обновите переменную окружения:

```
APP_CORS_ALLOWED_ORIGINS=https://forum-booking-frontend.onrender.com,https://yourdomain.com
```

Перезапустите backend в дашборде.

### 6. Проверка

1. Откройте URL frontend (из дашборда)
2. Проверьте, что компании загружаются
3. Зарегистрируйте администратора: `/admin/register`
4. Попробуйте создать бронирование

---

## 🔧 Переменные окружения

### Backend (.env production)

```env
PORT=10000
SPRING_DATASOURCE_URL=postgresql://user:pass@host:5432/forum_booking
SPRING_DATASOURCE_USERNAME=forum_user
SPRING_DATASOURCE_PASSWORD=your_password
SPRING_JPA_HIBERNATE_DDL_AUTO=update
SPRING_SQL_INIT_MODE=never
APP_JWT_SECRET=YourSecureSecretKey2026!
APP_JWT_EXPIRATION_MS=86400000
APP_CORS_ALLOWED_ORIGINS=https://your-frontend.onrender.com
```

### Frontend (.env.production)

```env
VITE_API_BASE_URL=https://forum-booking-backend.onrender.com/api
```

---

## 📝 Примечания

1. **Free план** на Render:
   - Backend засыпает через 15 минут бездействия
   - Первый запрос после "сна" может занимать 30-50 секунд
   - База данных работает 90 дней бесплатно

2. **Для production** рекомендуется:
   - Включить **Auto-Deploy** из GitHub
   - Настроить домен (Custom Domain)
   - Включить HTTPS (автоматически на Render)

3. **Инициализация БД**:
   - На production `spring.sql.init.mode=never`
   - Компании добавляются через админ-панель
   - Или выполните SQL вручную через Render Dashboard

---

## 🐛 Решение проблем

### Ошибка CORS
```
Access to fetch at '...' from origin '...' has been blocked by CORS policy
```
**Решение:** Обновите `APP_CORS_ALLOWED_ORIGINS` в backend

### Ошибка подключения к БД
**Решение:** Проверьте `SPRING_DATASOURCE_URL` и доступность БД

### Frontend не видит API
**Решение:** Проверьте `VITE_API_BASE_URL` и что backend запущен

### Backend долго загружается
**Решение:** Это нормально для Free плана. Для ускорения используйте платный план ($7/мес)

---

## 📊 Ссылки

- [Render Docs](https://render.com/docs)
- [Spring Boot на Render](https://render.com/docs/deploy-spring-boot)
- [Vue.js на Render](https://render.com/docs/deploy-vue)
- [PostgreSQL на Render](https://render.com/docs/databases)
