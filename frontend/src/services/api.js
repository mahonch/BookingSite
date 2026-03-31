import axios from 'axios'

// Базовый URL API (из переменной окружения или localhost)
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || '/api'

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json'
  }
})

// Интерцептор для добавления токена
api.interceptors.request.use(config => {
  const token = localStorage.getItem('adminToken')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

// Публичные эндпоинты
export const bookingApi = {
  getCompanies: () => api.get('/companies'),
  getCompany: (id) => api.get(`/companies/${id}`),
  getForumDate: () => api.get('/forum-date'),
  createBooking: (data) => api.post('/bookings', data)
}

// Админские эндпоинты
export const adminApi = {
  login: (credentials) => api.post('/admin/auth/login', credentials),
  register: (credentials) => api.post('/admin/auth/register', credentials),
  
  // Компании
  getCompanies: () => api.get('/admin/companies'),
  getCompany: (id) => api.get(`/admin/companies/${id}`),
  createCompany: (data) => api.post('/admin/companies', data),
  updateCompany: (id, data) => api.put(`/admin/companies/${id}`, data),
  deleteCompany: (id) => api.delete(`/admin/companies/${id}`),
  
  // Бронирования
  getBookings: () => api.get('/admin/bookings'),
  getBookingsByCompany: (companyId) => api.get(`/admin/bookings/company/${companyId}`)
}

export default api
