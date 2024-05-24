<template>
  <div class="main">
    <div class="api-container">
      <h1>REST API 생성하기</h1>
      <div v-if="isLoading" class="overlay">REST API 생성중...</div>
      <div class="form-wrapper">
        <div class="form-group">
          <label for="model">GPT 모델 선택</label>
          <select v-model="formData.model" id="model">
            <option disabled value="">모델 선택</option>
            <option value="gpt-4o">GPT-4o</option>
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
          <textarea v-model="formData.content" id="content" required></textarea>
        </div>
        <div class="form-actions">
          <button class="btn" @click="checkFormData">제출</button>
        </div>
      </div>
      <div v-if="contentItems.length > 0">
        <h2>추천 결과</h2>
        <ul>
          <li v-for="(item, index) in contentItems" :key="index">
            {{ index + 1 }}. {{ item }}
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useMainStore } from "@/store/store.js";
import { ref } from "vue";
import { useRouter } from "vue-router";
import { apiPost } from "@/utils/api";

const store = useMainStore();
const router = useRouter();

const formData = ref({ model: "", methodType: "", resource: "", content: "" });
const isLoading = ref(false);
const contentItems = ref([]);

// form 데이터 검사
const checkFormData = () => {
  const { model, methodType, resource, content } = formData.value;

  if (!model || !methodType || !resource || !content) {
    alert("모든 필드를 채워주세요.");
    return;
  }

  submitForm();
};

// rest api 제출
const submitForm = async () => {
  if (store.getToken <= 0) {
    alert("토큰이 부족합니다.");
    return;
  }
  isLoading.value = true;

  try {
    const data = await apiPost("/api/gpt/recommendations", formData.value);
    alert(data.message);
    const choices = data.data.choices;
    const recommendations = choices[0].message.content.split(", ");
    contentItems.value = recommendations;
    store.decrementToken();
  } catch (error) {
  } finally {
    isLoading.value = false;
  }
};
</script>

<style scoped>
.main {
  display: flex;
  justify-content: center;
}

.api-container {
  margin-top: 50px;
  padding: 50px 100px 100px 100px;
  max-width: 40%;
  width: 100%;
  background-color: #ffffff;
  box-shadow: 0px 1px 4px 8px rgba(0, 0, 0, 0.1);
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

.form-actions {
  display: flex;
  justify-content: center;
  padding-top: 20px;
}

.btn {
  background-color: #eeeeee;
}
.btn {
  padding: 10px 50px;
  font-size: 16px;
  font-weight: 1000;
  cursor: pointer;
  border-radius: 4px;
  color: #5c5c5c;
  outline: none;
  border: none;
  transition: background-color 0.2s, border-color 0.2s;
}

.btn:hover {
  background-color: #c7c7c7;
  border-color: #c7c7c7;
}

.btn:active {
  background-color: #c7c7c7;
  border-color: #c7c7c7;
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
</style>
