<template>
  <div class="container">
    <div>
      <div class="api-container">
        <h1>REST API 생성하기</h1>
        <div v-if="isLoading" class="overlay">REST API 생성중...</div>
        <div class="form-wrapper">
          <div class="form-group">
            <label for="model">GPT 모델 선택</label>
            <select v-model="formData.model" id="model">
              <option disabled value="">모델 선택</option>
              <option value="gpt-3.5-turbo">GPT-3.5</option>
              <option value="gpt-4">GPT-4</option>
            </select>
          </div>
          <div class="form-group">
            <label for="methodType">메서드 타입</label>
            <select v-model="formData.methodType" id="methodType">
              <option disabled value="">메서드 타입 선택</option>
              <option value="GET">GET</option>
              <option value="POST">POST</option>
              <option value="PUT">PUT</option>
              <option value="PATCH">PATCH</option>
              <option value="DELETE">DELETE</option>
            </select>
          </div>
          <div class="form-group">
            <label for="resource">자원(ex user, hotel, room)</label>
            <p2></p2>
            <input
              type="text"
              v-model="formData.resource"
              id="resource"
              required
            />
          </div>
          <div class="form-group">
            <label for="content">설명(상세할수록 좋습니다.)</label>
            <textarea
              v-model="formData.content"
              id="content"
              required
            ></textarea>
          </div>
          <div class="form-actions">
            <button :disabled="!isFormValid" @click="submitForm">제출</button>
            <button type="button" @click="resetForm">초기화</button>
          </div>
        </div>
        <div v-if="contentItems.length > 0">
          <h2>추천 결과:</h2>
          <ul>
            <li v-for="(item, index) in contentItems" :key="index">
              {{ item }}
            </li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useMainStore } from "@/store/store.js";
import { ref, computed } from "vue";
import { useRouter } from "vue-router";
import { useGlobalProperties } from "@/composables/useGlobalProperties";

const { $axios, $apiBaseUrl } = useGlobalProperties();
const store = useMainStore();
const router = useRouter();

const formData = ref({ model: "", methodType: "", resource: "", content: "" });
const isLoading = ref(false);
const contentItems = ref([]);
</script>

<style scoped>
body,
html {
  margin: 0;
  padding: 0;
  width: 100%;
  height: 100%;
}
.container {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 0;
  margin: 0;
  width: 100%;
  height: 100%;
  background-color: #fafafa;
}

.api-container {
  box-sizing: border-box;
  margin: auto;
  padding: 100px;
  max-width: 500px;
  width: 100%;
  background-color: #ffffff;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  position: relative;
}

.overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  color: white;
  font-size: 24px;
  z-index: 10;
}

.form-wrapper {
  width: 100%;
  margin: 0 auto;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 10px;
  font-size: 16px;
  color: #333;
  font-weight: 500;
}

.form-group input,
.form-group textarea,
.form-group select {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
  background-color: #f8f9fa;
}

button {
  padding: 10px 20px;
  margin-top: 10px;
  width: auto;
  background-color: #3498db;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
  font-weight: bold;
  transition: all 0.3s ease;
  display: inline-block;
  margin-right: 10px;
}

button:last-child {
  margin-right: 0;
}

button:disabled {
  background-color: #95a5a6;
  cursor: not-allowed;
}

button:hover:not(:disabled) {
  background-color: #2980b9;
}

h1 {
  margin-bottom: 20px;
  color: #2c3e50;
  font-weight: 400;
}

h2 {
  margin-top: 40px;
  color: #2c3e50;
}

ul {
  list-style: none;
  padding-left: 0;
}

li {
  background-color: #e9ecef;
  padding: 10px;
  border-radius: 4px;
  margin-bottom: 10px;
  text-align: left;
}

@media (max-width: 768px) {
  .api-container {
    padding: 20px;
  }

  .form-wrapper,
  .form-actions button {
    width: 100%;
  }
}
</style>
