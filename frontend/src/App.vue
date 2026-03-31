<template>
  <v-app>
    <v-app-bar color="primary" density="comfortable">
      <v-app-bar-title>
        <router-link to="/" style="text-decoration: none; color: inherit;">
          Форум 2026 - Бронирование встреч
        </router-link>
      </v-app-bar-title>
      
      <v-spacer></v-spacer>
      
      <v-btn
        v-if="!isAdmin"
        to="/admin/login"
        variant="text"
        color="white"
      >
        Админ
      </v-btn>
      
    </v-app-bar>

    <v-main>
      <v-container fluid class="pa-0">
        <router-view />
      </v-container>
    </v-main>

    <v-footer color="grey-lighten-3" class="mt-auto">
      <v-container>
        <v-row align="center" no-gutters>
          <v-col class="text-center">
            <span class="text-grey">© 2026 Форум - Система бронирования встреч</span>
          </v-col>
        </v-row>
      </v-container>
    </v-footer>
  </v-app>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from './stores/auth'

const router = useRouter()
const authStore = useAuthStore()

const isAdmin = computed(() => authStore.isAuthenticated)

const logout = () => {
  authStore.logout()
  router.push('/')
}
</script>
