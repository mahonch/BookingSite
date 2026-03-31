<template>
  <v-container class="py-8">
    <v-row>
      <v-col cols="12">
        <h1 class="text-h4 mb-6">Все бронирования</h1>

        <v-btn
          to="/admin/dashboard"
          variant="text"
          prepend-icon="mdi-arrow-left"
          class="mb-4"
        >
          Назад к панели
        </v-btn>

        <v-alert
          v-if="loading"
          type="info"
          variant="tonal"
        >
          Загрузка бронирований...
        </v-alert>

        <v-alert
          v-if="error"
          type="error"
          variant="tonal"
        >
          {{ error }}
        </v-alert>

        <v-card v-if="!loading && bookings.length > 0">
          <v-table>
            <thead>
              <tr>
                <th>ID</th>
                <th>Компания</th>
                <th>Имя</th>
                <th>Фамилия</th>
                <th>Телефон</th>
                <th>Email</th>
                <th>Время</th>
                <th>Дата создания</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="booking in bookings" :key="booking.id">
                <td>{{ booking.id }}</td>
                <td>{{ booking.companyName }}</td>
                <td>{{ booking.firstName }}</td>
                <td>{{ booking.lastName }}</td>
                <td>{{ booking.phone }}</td>
                <td>{{ booking.email }}</td>
                <td>
                  <v-chip color="primary" size="small">
                    {{ booking.timeSlot }}
                  </v-chip>
                </td>
                <td class="text-grey">
                  {{ formatDate(booking.createdAt) }}
                </td>
              </tr>
            </tbody>
          </v-table>
        </v-card>

        <v-alert
          v-if="!loading && bookings.length === 0"
          type="info"
          variant="tonal"
        >
          Бронирований пока нет.
        </v-alert>

        <!-- Статистика -->
        <v-card class="mt-6" v-if="bookings.length > 0">
          <v-card-title>
            <v-icon start color="primary">mdi-chart-bar</v-icon>
            Статистика
          </v-card-title>
          <v-card-text>
            <v-row>
              <v-col cols="6" md="3">
                <div class="text-center">
                  <div class="text-h4 text-primary">{{ bookings.length }}</div>
                  <div class="text-body-2 text-grey">Всего бронирований</div>
                </div>
              </v-col>
              <v-col cols="6" md="3">
                <div class="text-center">
                  <div class="text-h4 text-success">{{ uniqueCompanies }}</div>
                  <div class="text-body-2 text-grey">Компаний с бронированиями</div>
                </div>
              </v-col>
              <v-col cols="6" md="3">
                <div class="text-center">
                  <div class="text-h4 text-info">{{ bookedSlots }}</div>
                  <div class="text-body-2 text-grey">Занято слотов</div>
                </div>
              </v-col>
              <v-col cols="6" md="3">
                <div class="text-center">
                  <div class="text-h4 text-warning">{{ totalSlots }}</div>
                  <div class="text-body-2 text-grey">Всего слотов доступно</div>
                </div>
              </v-col>
            </v-row>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { adminApi } from '@/services/api'

const bookings = ref([])
const loading = ref(true)
const error = ref(null)

const uniqueCompanies = computed(() => {
  const companyIds = new Set(bookings.value.map(b => b.companyId))
  return companyIds.size
})

const bookedSlots = computed(() => {
  return bookings.value.length
})

const totalSlots = computed(() => {
  return uniqueCompanies.value * 16 // 16 слотов на компанию (с 10 до 18, по 30 мин)
})

const formatDate = (dateString) => {
  if (!dateString) return '—'
  const date = new Date(dateString)
  return date.toLocaleString('ru-RU', {
    day: '2-digit',
    month: '2-digit',
    year: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

const loadBookings = async () => {
  try {
    loading.value = true
    const response = await adminApi.getBookings()
    bookings.value = response.data
  } catch (err) {
    error.value = 'Ошибка загрузки: ' + (err.response?.data?.message || err.message)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadBookings()
})
</script>
