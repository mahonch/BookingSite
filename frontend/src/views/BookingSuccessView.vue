<template>
  <v-container class="py-8">
    <v-row justify="center">
      <v-col cols="12" md="6">
        <v-card class="text-center">
          <v-card-text class="py-8">
            <v-icon
              color="success"
              size="100"
              class="mb-4"
            >
              mdi-check-circle
            </v-icon>

            <h2 class="text-h4 font-weight-bold mb-2">
              Бронирование подтверждено!
            </h2>

            <p class="text-body-1 text-grey mb-6">
              Ваша встреча успешно забронирована
            </p>

            <v-alert
              type="success"
              variant="tonal"
              class="mb-6"
            >
              <strong>Номер бронирования:</strong> #{{ bookingId }}
            </v-alert>

            <div class="text-body-2 text-grey mb-6">
              <p>
                Мы ожидаем вас <strong>24 апреля 2026</strong>.
              </p>
              <p>
                Пожалуйста, будьте вовремя. Длительность встречи — 30 минут.
              </p>
            </div>

            <v-btn
              to="/"
              color="primary"
              size="large"
              prepend-icon="mdi-home"
            >
              На главную
            </v-btn>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const bookingId = computed(() => route.params.bookingId)
</script>
