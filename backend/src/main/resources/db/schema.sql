-- Схема базы данных для системы бронирования встреч форума
-- Дата форума: 24 апреля 2026

-- Таблица администраторов
CREATE TABLE IF NOT EXISTS admins (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Таблица компаний
CREATE TABLE IF NOT EXISTS companies (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    logo_url VARCHAR(500),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Таблица бронирований
CREATE TABLE IF NOT EXISTS bookings (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL REFERENCES companies(id) ON DELETE CASCADE,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(255) NOT NULL,
    booking_date DATE NOT NULL DEFAULT '2026-04-24',
    time_slot VARCHAR(5) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT unique_booking UNIQUE(company_id, booking_date, time_slot)
);

-- Индексы для оптимизации
CREATE INDEX IF NOT EXISTS idx_bookings_company_date ON bookings(company_id, booking_date);
CREATE INDEX IF NOT EXISTS idx_bookings_date_slot ON bookings(booking_date, time_slot);
CREATE INDEX IF NOT EXISTS idx_companies_active ON companies(is_active);
