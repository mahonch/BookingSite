<template>
  <v-container class="py-8">
    <v-row>
      <v-col cols="12">
        <h1 class="text-h4 mb-6">Панель администратора</h1>
      </v-col>

      <v-col cols="12" md="4">
        <v-card
          to="/admin/companies"
          hover
          class="text-center py-8"
        >
          <v-card-text>
            <v-icon
              color="primary"
              size="64"
              class="mb-4"
            >
              mdi-office-building
            </v-icon>
            <h3 class="text-h6 mb-2">Компании</h3>
            <p class="text-body-2 text-grey">
              Управление компаниями-участниками
            </p>
          </v-card-text>
        </v-card>
      </v-col>

      <v-col cols="12" md="4">
        <v-card
          to="/admin/bookings"
          hover
          class="text-center py-8"
        >
          <v-card-text>
            <v-icon
              color="success"
              size="64"
              class="mb-4"
            >
              mdi-calendar-check
            </v-icon>
            <h3 class="text-h6 mb-2">Бронирования</h3>
            <p class="text-body-2 text-grey">
              Просмотр всех забронированных встреч
            </p>
          </v-card-text>
        </v-card>
      </v-col>

      <v-col cols="12" md="4">
        <v-card
          to="/"
          hover
          class="text-center py-8"
        >
          <v-card-text>
            <v-icon
              color="grey"
              size="64"
              class="mb-4"
            >
              mdi-eye
            </v-icon>
            <h3 class="text-h6 mb-2">Публичная страница</h3>
            <p class="text-body-2 text-grey">
              Перейти на страницу бронирования
            </p>
          </v-card-text>
        </v-card>
      </v-col>

      <v-col cols="12" class="mt-8">
        <v-card>
          <v-card-title>
            <v-icon start color="primary">mdi-information</v-icon>
            Информация о форуме
          </v-card-title>
          <v-card-text>
            <v-list density="compact">
              <v-list-item>
                <v-list-item-title>
                  <strong>Дата форума:</strong> 24 апреля 2026
                </v-list-item-title>
              </v-list-item>
              <v-list-item>
                <v-list-item-title>
                  <strong>Время работы:</strong> 10:00 - 18:00
                </v-list-item-title>
              </v-list-item>
              <v-list-item>
                <v-list-item-title>
                  <strong>Длительность слота:</strong> 30 минут
                </v-list-item-title>
              </v-list-item>
              <v-list-item>
                <v-list-item-title>
                  <strong>Всего слотов на компанию:</strong> 16
                </v-list-item-title>
              </v-list-item>
            </v-list>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
// Dashboard компонент
</script>
