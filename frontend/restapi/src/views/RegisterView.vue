<template>
  <div class="register-container">
    <div v-if="isLoading" class="overlay">인증번호 전송중...</div>
    <h1>회원가입</h1>
    <div>
      <label for="email">이메일</label>
      <input
        type="email"
        id="email"
        v-model="email"
        :disabled="isDisabled"
        required
      />
      <button @click="sendAuthenticationNumber" :disabled="isDisabled">
        인증번호 전송
      </button>
    </div>
    <div v-if="showAuthenticationNumberInput">
      <div>
        <label for="authenticationNumber">인증번호</label>
        <input
          type="text"
          id="authenticationNumber"
          v-model="authenticationNumber"
          :disabled="isDisabled"
          required
        />
        <button
          class="auth-btn"
          @click="checkAuthenticationNumber"
          :disabled="isDisabled"
        >
          인증
        </button>
        <button
          class="auth-btn"
          @click="sendAuthenticationNumber"
          :disabled="isDisabled"
        >
          재전송
        </button>
      </div>
    </div>
    <div>
      <label for="password">비밀번호</label>
      <input type="password" id="password" v-model="password" required />
    </div>
    <div>
      <label for="passwordConfirm">비밀번호 확인</label>
      <input
        type="password"
        id="passwordConfirm"
        v-model="passwordConfirm"
        required
      />
    </div>
    <div>
      <label for="nickname">닉네임</label>
      <input type="text" id="nickname" v-model="nickname" required />
    </div>
    <button @click="register">회원가입</button>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";
import { apiPost } from "@/utils/api";

const router = useRouter();

const email = ref("");
const authenticationNumber = ref("");
const password = ref("");
const passwordConfirm = ref("");
const nickname = ref("");
const showAuthenticationNumberInput = ref(false);
const isLoading = ref(false);
// 이메일 인증 끝나면 버튼 비활성화 목적
const isDisabled = ref(false);
// 이메일 인증이 되었는지 확인
const isCheckAuthentication = ref(false);

// 입력값 체크
const checkInputValue = () => {
  if (
    email.value.length === 0 ||
    password.value.length === 0 ||
    passwordConfirm.value.length === 0 ||
    nickname.value.length === 0
  ) {
    alert("모든 값을 입력해주세요.");
    return false;
  }

  if (!/\S+@\S+\.\S+/.test(email.value)) {
    alert("유효한 이메일 주소를 입력해주세요.");
    return false;
  } else if (!/^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,15}$/.test(password.value)) {
    alert("비밀번호는 8~15자리의 영문과 숫자를 포함해야 합니다.");
    return false;
  } else if (password.value !== passwordConfirm.value) {
    alert("비밀번호가 일치하지 않습니다.");
    return false;
  } else if (!/^[가-힣a-zA-Z0-9]{1,10}$/.test(nickname.value)) {
    alert("닉네임은 1~10자리의 한글, 영문, 숫자만 포함해야 합니다.");
    return false;
  } else if (isCheckAuthentication.value === false) {
    alert("이메일 인증이 필요합니다.");
    return false;
  }

  return true;
};

// 인증번호 전송
const sendAuthenticationNumber = async () => {
  if (!/\S+@\S+\.\S+/.test(email.value)) {
    alert("유효한 이메일 주소를 입력해주세요.");
    return;
  }
  isLoading.value = true;
  try {
    await apiPost("/api/email/authentication/send", { email: email.value });
    showAuthenticationNumberInput.value = true;
    alert("인증번호가 전송되었습니다.");
  } catch (error) {
  } finally {
    isLoading.value = false;
  }
};

// 인증번호 확인
const checkAuthenticationNumber = async () => {
  try {
    const data = await apiPost("/api/email/authentication/verify", {
      authenticationNumber: authenticationNumber.value,
    });
    alert("인증이 완료되었습니다.");
    isDisabled.value = true;
    isCheckAuthentication.value = true;
  } catch (error) {}
};

// 회원가입
const register = async () => {
  if (!checkInputValue()) return;

  try {
    const data = await apiPost("/api/members", {
      email: email.value,
      password: password.value,
      nickname: nickname.value,
    });
    alert("회원가입이 완료되었습니다.");
    router.push("/login");
  } catch (error) {}
};
</script>

<style scoped>
.overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.6);
  color: white;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 1.5rem;
  z-index: 1000;
}

.register-container {
  max-width: 400px;
  margin: 50px auto;
  background-color: #ffffff;
  padding: 2rem;
  border-radius: 30px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
}

h1 {
  color: #333;
  margin-bottom: 2rem;
  font-weight: 400;
  text-align: center;
}

input[type="email"],
input[type="password"],
input[type="text"] {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 8px;
  box-sizing: border-box;
  margin-bottom: 1rem;
  transition: border 0.3s ease;
}

input[type="email"]:focus,
input[type="password"]:focus,
input[type="text"]:focus {
  border-color: #3498db;
  outline: none;
}

button {
  padding: 0.65rem 1.5rem;
  border-radius: 8px;
  background-color: #3498db;
  color: #ffffff;
  font-size: 0.9rem;
  cursor: pointer;
  transition: background-color 0.2s;
  border: none;
  margin-top: 1rem;
  margin-bottom: 0.5rem;
  width: auto;
  display: inline-block;
  margin-right: 10px;
}

button:disabled {
  background-color: #95a5a6;
  cursor: default;
}

button:hover:not(:disabled) {
  background-color: #2980b9;
}

.auth-btn {
  margin-right: 10px;
}

p {
  color: #e74c3c;
  margin-top: -10px;
  margin-bottom: 10px;
}

.label {
  display: block;
  margin-bottom: 5px;
  color: #34495e;
  font-weight: 500;
}
</style>
