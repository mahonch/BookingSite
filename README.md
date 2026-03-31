# Система бронирования встреч для форума

Система позволяет пользователям бронировать 30-минутные встречи с компаниями-участниками форума.

## 🌐 Деплой на Render.com

- 📖 Полная инструкция: [DEPLOY.md](DEPLOY.md)
- ⚡ Краткая версия: [DEPLOY-QUICK.md](DEPLOY-QUICK.md)

---

## 🚀 Быстрый старт

### Требования
- Java 21
- PostgreSQL
- Node.js 18+

### 1. База данных

```sql
CREATE DATABASE forum_booking;
```

### 2. Настройка

Откройте `backend/src/main/resources/application.properties` и укажите ваш пароль PostgreSQL:

```properties
spring.datasource.password=ВАШ_ПАРОЛЬ
```

### 3. Запуск

**Backend (порт 8080):**
```bash
cd backend
apache-maven-3.9.6\bin\mvn.cmd spring-boot:run
```

**Frontend (порт 5173):**
```bash
cd frontend
npm install
npm run dev
```

Или используйте скрипт `start.bat` в корне проекта для одновременного запуска.

## 📋 Функционал

### Для пользователей
- ✅ Просмотр 12 компаний-участников
- ✅ Отображение доступных временных слотов (10:00 - 18:00, шаг 30 мин)
- ✅ Бронирование встречи (имя, фамилия, телефон, email)
- ✅ Забронированные слоты автоматически скрываются
- ✅ Пользователи не видят данные других бронирующих

### Для администраторов
- ✅ Регистрация/авторизация
- ✅ CRUD операции с компаниями
- ✅ Загрузка логотипов компаний
- ✅ Просмотр всех бронирований
- ✅ Статистика по бронированиям

## 🌐 Доступ

- **Главная страница:** http://localhost:5173
- **Админ-панель (вход):** http://localhost:5173/admin/login
- **Админ-панель (регистрация):** http://localhost:5173/admin/register

## 📅 Дата форума

**24 апреля 2026 года**

Время работы: 10:00 - 18:00  
Длительность слота: 30 минут  
Всего слотов на компанию: 16

## 🏢 Компании

В систему предустановлены 12 компаний:
1. TechSoft - Software development and IT consulting
2. InnovatePlus - Implementing innovative business solutions
3. DataCenter - Cloud storage and data processing services
4. CyberSec - Information security and audit
5. AnalyticsPro - Business analytics and consulting
6. Robotics - Robotics systems development
7. NeuroNet - Artificial intelligence and machine learning
8. WebMaster - Website creation and promotion
9. MobileApps - Mobile application development
10. Quantum - Quantum computing and research
11. BlockChain - Distributed ledger technologies
12. GreenTech - Eco-friendly technologies for business

## 🔧 Технологии

**Backend:**
- Java 21
- Spring Boot 3.2.4
- Spring Security + JWT
- Spring Data JPA
- PostgreSQL

**Frontend:**
- Vue.js 3 (Composition API)
- Vuetify 3
- Pinia
- Vue Router
- Axios

## 📁 Структура проекта

```
BookingSite/
├── backend/              # Spring Boot приложение
│   ├── pom.xml
│   ├── apache-maven-3.9.6/  # Maven (локальная копия)
│   └── src/main/
│       ├── java/com/forum/booking/
│       │   ├── config/
│       │   ├── controller/
│       │   ├── dto/
│       │   ├── entity/
│       │   ├── exception/
│       │   ├── filter/
│       │   ├── repository/
│       │   └── service/
│       └── resources/
│           ├── application.properties
│           └── db/
│               ├── schema.sql
│               └── data.sql
├── frontend/             # Vue.js приложение
│   ├── package.json
│   └── src/
│       ├── services/
│       ├── stores/
│       ├── router/
│       └── views/
├── start.bat            # Скрипт запуска
└── README.md
```

## 🔐 Безопасность

- JWT токены для авторизации администраторов
- Хеширование паролей (BCrypt)
- CORS настроен для localhost:5173
- Валидация входных данных

## 📝 Примечания

1. При первом запуске зарегистрируйте администратора через `/admin/register`
2. Компании уже добавлены (12 шт.)
3. Бронирования доступны только на 24 апреля 2026
4. При повторном запуске данные сохраняются в БД

## 🛠️ Решение проблем

**Ошибка подключения к БД:**
- Проверьте, запущен ли PostgreSQL
- Убедитесь, что пароль в `application.properties` верный

**Backend не запускается:**
- Очистите Maven: `mvn clean`
- Проверьте логи в консоли

**Frontend не запускается:**
- Удалите `node_modules` и выполните `npm install` снова
