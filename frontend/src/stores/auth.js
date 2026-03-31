import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useAuthStore = defineStore('auth', () => {
  const token = ref(localStorage.getItem('adminToken') || null)
  const username = ref(localStorage.getItem('adminUsername') || null)

  const isAuthenticated = computed(() => !!token.value)

  function setAuth(newToken, newUsername) {
    token.value = newToken
    username.value = newUsername
    localStorage.setItem('adminToken', newToken)
    localStorage.setItem('adminUsername', newUsername)
  }

  function logout() {
    token.value = null
    username.value = null
    localStorage.removeItem('adminToken')
    localStorage.removeItem('adminUsername')
  }

  return {
    token,
    username,
    isAuthenticated,
    setAuth,
    logout
  }
})
