<template>
  <v-container class="py-8">
    <v-row>
      <v-col cols="12">
        <div class="d-flex justify-space-between align-center mb-6">
          <h1 class="text-h4">Управление компаниями</h1>
          <v-btn
            color="primary"
            prepend-icon="mdi-plus"
            @click="showCreateDialog = true"
          >
            Добавить компанию
          </v-btn>
        </div>

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
          Загрузка...
        </v-alert>

        <v-alert
          v-if="error"
          type="error"
          variant="tonal"
        >
          {{ error }}
        </v-alert>

        <v-table v-if="!loading && companies.length > 0">
          <thead>
            <tr>
              <th>ID</th>
              <th>Название</th>
              <th>Описание</th>
              <th>Статус</th>
              <th>Действия</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="company in companies" :key="company.id">
              <td>{{ company.id }}</td>
              <td>
                <div class="d-flex align-center">
                  <v-avatar
                    v-if="company.logoUrl"
                    size="40"
                    class="mr-2"
                  >
                    <v-img :src="company.logoUrl" />
                  </v-avatar>
                  {{ company.name }}
                </div>
              </td>
              <td class="text-truncate" style="max-width: 300px;">
                {{ company.description || '—' }}
              </td>
              <td>
                <v-chip
                  :color="company.isActive ? 'success' : 'grey'"
                  size="small"
                >
                  {{ company.isActive ? 'Активна' : 'Неактивна' }}
                </v-chip>
              </td>
              <td>
                <v-btn
                  size="small"
                  variant="text"
                  icon="mdi-pencil"
                  @click="editCompany(company)"
                  class="mr-2"
                />
                <v-btn
                  size="small"
                  variant="text"
                  icon="mdi-delete"
                  color="error"
                  @click="deleteCompany(company)"
                />
              </td>
            </tr>
          </tbody>
        </v-table>

        <v-alert
          v-if="!loading && companies.length === 0"
          type="warning"
          variant="tonal"
        >
          Компании ещё не добавлены. Нажмите "Добавить компанию".
        </v-alert>
      </v-col>
    </v-row>

    <!-- Диалог создания/редактирования -->
    <v-dialog v-model="showCreateDialog" max-width="600">
      <v-card>
        <v-card-title>
          {{ editingCompany ? 'Редактировать компанию' : 'Новая компания' }}
        </v-card-title>

        <v-card-text>
          <v-form ref="formRef" v-model="formValid">
            <v-text-field
              v-model="formData.name"
              label="Название компании *"
              :rules="[v => !!v || 'Введите название']"
              variant="outlined"
              class="mb-4"
            />

            <v-textarea
              v-model="formData.description"
              label="Описание"
              rows="3"
              variant="outlined"
              class="mb-4"
            />

            <v-text-field
              v-model="formData.logoUrl"
              label="URL логотипа"
              variant="outlined"
              class="mb-4"
              hint="Ссылка на изображение логотипа"
            />

            <v-switch
              v-if="editingCompany"
              v-model="formData.isActive"
              label="Активная компания"
              color="success"
            />
          </v-form>
        </v-card-text>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
            @click="closeDialog"
            variant="text"
          >
            Отмена
          </v-btn>
          <v-btn
            @click="saveCompany"
            color="primary"
            :disabled="!formValid || saving"
            :loading="saving"
          >
            Сохранить
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { adminApi } from '@/services/api'

const companies = ref([])
const loading = ref(true)
const error = ref(null)
const showCreateDialog = ref(false)
const editingCompany = ref(null)
const formValid = ref(false)
const saving = ref(false)
const formRef = ref(null)

const formData = reactive({
  name: '',
  description: '',
  logoUrl: '',
  isActive: true
})

const loadCompanies = async () => {
  try {
    loading.value = true
    const response = await adminApi.getCompanies()
    companies.value = response.data
  } catch (err) {
    error.value = 'Ошибка загрузки: ' + (err.response?.data?.message || err.message)
  } finally {
    loading.value = false
  }
}

const openDialog = () => {
  formData.name = ''
  formData.description = ''
  formData.logoUrl = ''
  formData.isActive = true
  editingCompany.value = null
}

const editCompany = (company) => {
  editingCompany.value = company
  formData.name = company.name
  formData.description = company.description || ''
  formData.logoUrl = company.logoUrl || ''
  formData.isActive = company.isActive
  showCreateDialog.value = true
}

const closeDialog = () => {
  showCreateDialog.value = false
  openDialog()
}

const saveCompany = async () => {
  try {
    saving.value = true
    
    if (editingCompany.value) {
      await adminApi.updateCompany(editingCompany.value.id, formData)
    } else {
      await adminApi.createCompany(formData)
    }
    
    await loadCompanies()
    closeDialog()
  } catch (err) {
    alert('Ошибка сохранения: ' + (err.response?.data?.message || err.message))
  } finally {
    saving.value = false
  }
}

const deleteCompany = async (company) => {
  if (!confirm(`Удалить компанию "${company.name}"?`)) return
  
  try {
    await adminApi.deleteCompany(company.id)
    await loadCompanies()
  } catch (err) {
    alert('Ошибка удаления: ' + (err.response?.data?.message || err.message))
  }
}

onMounted(() => {
  loadCompanies()
  openDialog()
})
</script>
