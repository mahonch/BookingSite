import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('@/views/HomeView.vue')
  },
  {
    path: '/booking/:companyId',
    name: 'Booking',
    component: () => import('@/views/BookingView.vue')
  },
  {
    path: '/booking-success/:bookingId',
    name: 'BookingSuccess',
    component: () => import('@/views/BookingSuccessView.vue')
  },
  {
    path: '/admin/login',
    name: 'AdminLogin',
    component: () => import('@/views/admin/LoginView.vue')
  },
  {
    path: '/admin/register',
    name: 'AdminRegister',
    component: () => import('@/views/admin/RegisterView.vue')
  },
  {
    path: '/admin/dashboard',
    name: 'AdminDashboard',
    component: () => import('@/views/admin/DashboardView.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/admin/companies',
    name: 'AdminCompanies',
    component: () => import('@/views/admin/CompaniesView.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/admin/bookings',
    name: 'AdminBookings',
    component: () => import('@/views/admin/BookingsView.vue'),
    meta: { requiresAuth: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const authStore = useAuthStore()

  if (to.meta.requiresAuth && !authStore.isAuthenticated) {
    next('/admin/login')
  } else {
    next()
  }
})

export default router
