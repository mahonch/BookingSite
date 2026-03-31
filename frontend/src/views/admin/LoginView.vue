<template>
  <v-container class="py-8">
    <v-row justify="center">
      <v-col cols="12" md="6">
        <v-card>
          <v-card-title class="text-h5 text-center">
            Вход в админ-панель
          </v-card-title>

          <v-card-text>
            <v-form
              ref="formRef"
              v-model="formValid"
              @submit.prevent="login"
            >
              <v-text-field
                v-model="credentials.username"
                label="Имя пользователя"
                :rules="[v => !!v || 'Введите имя пользователя']"
                variant="outlined"
                density="comfortable"
                prepend-inner-icon="mdi-account"
                class="mb-4"
              />

              <v-text-field
                v-model="credentials.password"
                label="Пароль"
                :rules="[v => !!v || 'Введите пароль']"
                variant="outlined"
                density="comfortable"
                prepend-inner-icon="mdi-lock"
                :type="showPassword ? 'text' : 'password'"
                :append-inner-icon="showPassword ? 'mdi-eye-off' : 'mdi-eye'"
                @click:append-inner="showPassword = !showPassword"
                class="mb-4"
              />

              <v-alert
                v-if="error"
                type="error"
                variant="tonal"
                class="mb-4"
              >
                {{ error }}
              </v-alert>

              <v-btn
                type="submit"
                color="primary"
                size="large"
                block
                :disabled="!formValid || loading"
                :loading="loading"
              >
                Войти
              </v-btn>
            </v-form>

            <div class="text-center mt-4">
              <router-link to="/admin/register" class="text-body-2">
                Нет аккаунта? Зарегистрироваться
              </router-link>
            </div>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { adminApi } from '@/services/api'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const authStore = useAuthStore()

const formRef = ref(null)
const formValid = ref(false)
const loading = ref(false)
const error = ref(null)
const showPassword = ref(false)

const credentials = reactive({
  username: '',
  password: ''
})

const login = async () => {
  try {
    loading.value = true
    error.value = null

    const response = await adminApi.login(credentials)
    
    authStore.setAuth(response.data.token, response.data.username)
    router.push('/admin/dashboard')
  } catch (err) {
    error.value = err.response?.data?.message || 'Ошибка входа. Проверьте логин и пароль.'
  } finally {
    loading.value = false
  }
}
</script>
