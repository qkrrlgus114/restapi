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

.header img {
  width: 600px;
}

.header p {
  font-size: 40px;
  padding: 50px 0px;
  color: white;
}

.btn {
  background-color: #3498db;
  border: none;
  padding: 15px 30px;
  font-size: 1.5rem;
  font-weight: bold;
  color: white;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s, box-shadow 0.3s;
}

.btn:hover {
  background-color: #2980b9;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.btn:active {
  background-color: #1c6694;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}
</style>
