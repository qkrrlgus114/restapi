<template>
  <div class="main bg-dark">
    <div class="header">
      <img src="/public/logo.png" alt="" class="logo" />
      <p>REST API 어떻게 지어야할까?</p>
    </div>

    <div class="footer">
      <button @click="goToLogin" class="btn btn-primary btn-lg custom-button">
        시작하기
      </button>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from "vue-router";

const router = useRouter();

const goToLogin = () => {
  router.push("/login");
};
</script>

<style scoped>
.main {
  height: 100vh;
  width: auto;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 50px 0px;
}

.header {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  padding: 50px 0px;
}

.btn {
  background-color: #3498db;
  border: none;
}

.btn:hover {
  background-color: #2980b9;
  border: none;
}

.header img {
  width: 600px;
}

.header p {
  font-size: 40px;
  padding: 50px 0px;
  color: white;
}

.custom-button {
  background: linear-gradient(135deg, #6a11cb 0%, #2575fc 100%);
  border: none;
  color: white;
  font-size: 1.25rem;
  padding: 10px 20px;
  border-radius: 50px;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.custom-button:hover {
  background: linear-gradient(135deg, #2575fc 0%, #6a11cb 100%);
  transform: translateY(-2px);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.2);
}

.custom-button:focus {
  outline: none;
  box-shadow: 0 0 0 4px rgba(98, 182, 239, 0.5);
}
</style>
