<template>
  <v-container class="py-4 py-md-8">
    <v-row justify="center">
      <v-col cols="12" md="10">
        <div class="text-center mb-6 mb-md-8">
          <h1 class="text-h4 text-md-h3 font-weight-bold mb-2">Форум 2026</h1>
          <p class="text-body-1 text-md-subtitle-1 text-grey">
            Забронируйте встречу с компанией
          </p>
          <v-chip color="primary" class="mt-2" size="small" size-md="large">
            <v-icon start>mdi-calendar</v-icon>
            24 апреля 2026
          </v-chip>
        </div>

        <v-alert
          v-if="loading"
          type="info"
          variant="tonal"
          class="mb-4"
        >
          Загрузка компаний...
        </v-alert>

        <v-alert
          v-if="error"
          type="error"
          variant="tonal"
          class="mb-4"
        >
          {{ error }}
        </v-alert>

        <v-row v-if="!loading && companies.length > 0">
          <v-col
            v-for="company in companies"
            :key="company.id"
            cols="12"
            sm="6"
            md="6"
            lg="4"
          >
            <v-card
              class="h-100"
              :class="company.isBooked ? 'border-success' : ''"
              border
              hover
            >
              <v-card-item>
                <div class="d-flex align-center mb-2">
                  <v-avatar
                    v-if="company.logoUrl"
                    size="50"
                    class="mr-3"
                  >
                    <v-img :src="company.logoUrl" :alt="company.name" />
                  </v-avatar>
                  <v-avatar
                    v-else
                    size="50"
                    color="primary"
                    class="mr-3"
                  >
                    <span class="text-h6 text-white">
                      {{ company.name.charAt(0) }}
                    </span>
                  </v-avatar>

                  <div class="ml-3" style="flex: 1; min-width: 0;">
                    <v-card-title class="text-subtitle-1 pa-0 text-truncate">
                      {{ company.name }}
                    </v-card-title>
                    <v-chip
                      size="x-small"
                      :color="company.isBooked ? 'success' : 'grey'"
                      class="mt-1"
                    >
                      {{ company.isBooked ? 'Забронировано' : 'Свободно' }}
                    </v-chip>
                  </div>
                </div>

                <v-card-text class="pa-0 mt-3">
                  <p class="text-body-2 text-grey mb-2">
                    {{ company.description || 'Описание отсутствует' }}
                  </p>
                  
                  <div v-if="company.isBooked" class="mt-2">
                    <v-alert type="success" variant="tonal" density="compact" class="mb-0">
                      <div class="text-body-2">
                        <strong>Время:</strong> {{ company.bookedTime }}
                      </div>
                    </v-alert>
                  </div>
                </v-card-text>
              </v-card-item>

              <v-card-actions v-if="!company.isBooked" class="px-2 pb-2">
                <v-btn
                  :to="`/booking/${company.id}`"
                  color="primary"
                  variant="elevated"
                  size="small"
                  block
                >
                  Забронировать
                </v-btn>
              </v-card-actions>
            </v-card>
          </v-col>
        </v-row>

        <v-alert
          v-if="!loading && companies.length === 0"
          type="warning"
          variant="tonal"
        >
          Компании пока не добавлены. Попробуйте позже.
        </v-alert>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { bookingApi } from '@/services/api'

const companies = ref([])
const loading = ref(true)
const error = ref(null)

const loadCompanies = async () => {
  try {
    loading.value = true
    const response = await bookingApi.getCompanies()
    companies.value = response.data
  } catch (err) {
    error.value = 'Ошибка загрузки компаний: ' + (err.response?.data?.message || err.message)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadCompanies()
})
</script>

<style scoped>
.v-card {
  transition: transform 0.2s;
}
.v-card:hover {
  transform: translateY(-2px);
}
</style>
