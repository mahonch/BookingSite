<template>
  <v-container class="py-4 py-md-8">
    <v-row justify="center">
      <v-col cols="12" md="8">
        <v-btn
          to="/"
          variant="text"
          prepend-icon="mdi-arrow-left"
          class="mb-4"
          size="small"
        >
          Назад к списку
        </v-btn>

        <v-card v-if="loading">
          <v-card-text class="text-center py-8">
            <v-progress-circular indeterminate color="primary" />
            <p class="mt-4">Загрузка информации...</p>
          </v-card-text>
        </v-card>

        <v-card v-else-if="error">
          <v-card-text class="text-center py-8">
            <v-icon color="error" size="64">mdi-alert-circle</v-icon>
            <p class="mt-4 text-error">{{ error }}</p>
            <v-btn to="/" color="primary" class="mt-4" size="small">На главную</v-btn>
          </v-card-text>
        </v-card>

        <template v-else-if="company">
          <v-card class="mb-6">
            <v-card-item>
              <div class="d-flex align-center">
                <v-avatar
                  v-if="company.logoUrl"
                  size="60"
                  class="mr-3"
                >
                  <v-img :src="company.logoUrl" :alt="company.name" />
                </v-avatar>
                <v-avatar
                  v-else
                  size="60"
                  color="primary"
                  class="mr-3"
                >
                  <span class="text-h5 text-white">
                    {{ company.name.charAt(0) }}
                  </span>
                </v-avatar>

                <div style="flex: 1; min-width: 0;">
                  <v-card-title class="text-h5 text-md-h4 pa-0 text-truncate">
                    {{ company.name }}
                  </v-card-title>
                  <v-card-subtitle class="pa-0 text-body-2">
                    {{ company.description || 'Описание отсутствует' }}
                  </v-card-subtitle>
                </div>
              </div>
            </v-card-item>
          </v-card>

          <v-card>
            <v-card-title class="text-subtitle-1 text-md-h5">
              <v-icon start color="primary" size="small">mdi-clock-outline</v-icon>
              Выберите время
            </v-card-title>

            <v-card-text>
              <v-alert
                type="info"
                variant="tonal"
                density="compact"
                class="mb-4"
              >
                <v-icon start size="small">mdi-information</v-icon>
                24 апреля 2026, слоты по 30 минут
              </v-alert>

              <div class="mb-6">
                <h4 class="text-subtitle-2 mb-3">Доступные слоты:</h4>
                <v-chip-group
                  v-model="selectedSlot"
                  column
                  mandatory
                  class="flex-fill"
                >
                  <v-chip
                    v-for="slot in company.availableSlots"
                    :key="slot.time"
                    :value="slot.time"
                    :disabled="!slot.isAvailable"
                    filter
                    variant="outlined"
                    class="ma-1"
                    size="small"
                  >
                    {{ slot.time }}
                    <v-icon
                      v-if="!slot.isAvailable"
                      end
                      size="small"
                      color="error"
                    >
                      mdi-close
                    </v-icon>
                  </v-chip>
                </v-chip-group>
              </div>

              <v-divider class="mb-6"></v-divider>

              <h4 class="text-subtitle-1 mb-3">Ваши данные:</h4>
              <v-form
                ref="formRef"
                v-model="formValid"
                @submit.prevent="submitBooking"
              >
                <v-row>
                  <v-col cols="12" sm="6">
                    <v-text-field
                      v-model="formData.firstName"
                      label="Имя *"
                      :rules="[v => !!v || 'Введите имя']"
                      variant="outlined"
                      density="compact"
                    />
                  </v-col>
                  <v-col cols="12" sm="6">
                    <v-text-field
                      v-model="formData.lastName"
                      label="Фамилия *"
                      :rules="[v => !!v || 'Введите фамилию']"
                      variant="outlined"
                      density="compact"
                    />
                  </v-col>
                  <v-col cols="12" sm="6">
                    <v-text-field
                      v-model="formData.phone"
                      label="Телефон *"
                      :rules="[
                        v => !!v || 'Введите телефон',
                        v => /^[\+]?[0-9\s\-\(\)]{7,20}$/.test(v) || 'Неверный формат телефона'
                      ]"
                      variant="outlined"
                      density="compact"
                      placeholder="+7 XXX XXX-XX-XX"
                    />
                  </v-col>
                  <v-col cols="12" sm="6">
                    <v-text-field
                      v-model="formData.email"
                      label="Email *"
                      :rules="[
                        v => !!v || 'Введите email',
                        v => /.+@.+\..+/.test(v) || 'Неверный формат email'
                      ]"
                      variant="outlined"
                      density="compact"
                      type="email"
                    />
                  </v-col>
                </v-row>

                <v-alert
                  v-if="submitError"
                  type="error"
                  variant="tonal"
                  density="compact"
                  class="mt-4"
                >
                  {{ submitError }}
                </v-alert>

                <v-alert
                  v-if="submitSuccess"
                  type="success"
                  variant="tonal"
                  density="compact"
                  class="mt-4"
                >
                  Бронирование успешно создано!
                </v-alert>

                <div class="mt-4">
                  <v-btn
                    type="submit"
                    color="primary"
                    size="large"
                    :disabled="!formValid || !selectedSlot || submitting"
                    :loading="submitting"
                    block
                  >
                    Подтвердить бронирование
                  </v-btn>
                </div>
              </v-form>
            </v-card-text>
          </v-card>
        </template>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { bookingApi } from '@/services/api'

const route = useRoute()
const router = useRouter()

const company = ref(null)
const loading = ref(true)
const error = ref(null)
const selectedSlot = ref(null)
const formValid = ref(false)
const submitting = ref(false)
const submitError = ref(null)
const submitSuccess = ref(false)
const formRef = ref(null)

const formData = reactive({
  firstName: '',
  lastName: '',
  phone: '',
  email: ''
})

const loadCompany = async () => {
  try {
    loading.value = true
    const response = await bookingApi.getCompany(route.params.companyId)
    company.value = response.data
  } catch (err) {
    error.value = 'Ошибка загрузки информации о компании: ' + (err.response?.data?.message || err.message)
  } finally {
    loading.value = false
  }
}

const submitBooking = async () => {
  try {
    submitting.value = true
    submitError.value = null
    submitSuccess.value = false

    const response = await bookingApi.createBooking({
      companyId: parseInt(route.params.companyId),
      firstName: formData.firstName,
      lastName: formData.lastName,
      phone: formData.phone,
      email: formData.email,
      bookingDate: '2026-04-24',
      timeSlot: selectedSlot.value
    })

    submitSuccess.value = true
    router.push(`/booking-success/${response.data.id}`)
  } catch (err) {
    submitError.value = err.response?.data?.message || 'Ошибка при создании бронирования. Попробуйте другой слот.'
  } finally {
    submitting.value = false
  }
}

onMounted(() => {
  loadCompany()
})
</script>
