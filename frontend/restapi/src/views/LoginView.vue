<template>
  <div class="login-container bg-dark">
    <img src="/public/logo.png" alt="" class="logo" />
    <div class="form-group">
      <label for="email">이메일</label>
      <input
        type="email"
        @keyup.enter="login"
        id="email"
        v-model="email"
        required
      />
    </div>
    <div class="form-group">
      <label for="password">비밀번호</label>
      <input
        type="password"
        @keyup.enter="login"
        id="password"
        v-model="password"
        required
      />
    </div>
    <div class="button-group">
      <a @click="kakaoLogin"><img src="/kakao_login_medium_narrow.png" /></a>
      <button @click="login" class="group">로그인</button>
      <button @click="register" class="group">회원가입</button>
    </div>
  </div>
</template>

<script setup>
import { useMainStore } from "@/store/store.js";
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import { apiPost, getBaseURL } from "@/utils/api";

const store = useMainStore();
const router = useRouter();

const email = ref("");
const password = ref("");
const loginState = store.getLoginState;

onMounted(() => {
  checkLogin();
});

// 로그인 상태 검사
const checkLogin = () => {
  if (loginState) router.push("/chat");
};

// 이메일 검사
const isValidEmail = (email) => {
  return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
};

// 카카오 로그인 함수
const kakaoLogin = () => {
  const baseUrl = getBaseURL();
  window.location.href = `${baseUrl}/oauth2/authorization/kakao`;
};

// 로그인 함수
const login = async () => {
  if (!email.value) {
    alert("이메일을 입력해주세요.");
    return;
  } else if (!isValidEmail(email.value)) {
    alert("유효한 이메일 주소를 입력해주세요.");
    return;
  }

  if (!password.value) {
    alert("비밀번호를 입력해주세요.");
    return;
  } else if (password.value.length > 15 || password.value.length < 8) {
    alert("비밀번호는 8자 이상 15자 이하로 입력해주세요.");
    return;
  }

  try {
    await apiPost("/api/members/login", {
      email: email.value,
      password: password.value,
    });
    store.loginState = true;
    router.push("/chat");
  } catch (error) {}
};

// 회원가입 이동
const register = () => {
  router.push("/register");
};
</script>

<style scoped>
.login-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;
  font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
  top: 0;
  left: 0;
  position: absolute;
  width: 100%;
}

.logo {
  width: 500px;
  height: 200px;
  margin-bottom: 100px;
}

h1 {
  color: #2c3e50;
  margin-bottom: 1.5rem;
  font-weight: 300;
}

.form-group {
  margin-bottom: 1.5rem;
  width: 100%;
  max-width: 340px;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  font-size: 16px;
  color: #ffffff;
  font-weight: 500;
}

.form-group input {
  width: 100%;
  padding: 12px;
  font-size: 14px;
  border: 2px solid #bdc3c7;
  border-radius: 8px;
  box-sizing: border-box;
  transition: border-color 0.3s;
}

.form-group input:focus {
  border-color: #3498db;
  outline: none;
}

.group {
  padding: 12px 18px;
  border: none;
  border-radius: 8px;
  background-color: #3498db;
  color: white;
  font-size: 16px;
  text-decoration: none;
  cursor: pointer;
  transition: background-color 0.2s, transform 0.2s;
}

.button-group {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  margin-top: 1rem;
}

button:hover,
.button-group a:hover {
  transform: translateY(-2px);
  cursor: pointer;
}

@media (max-width: 768px) {
  .form-group {
    max-width: 280px;
  }
}
</style>
