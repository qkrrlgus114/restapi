<template>
  <div class="login-container">
    <h1>REST API 서비스</h1>
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

<script>
export default {
  name: "LoginView",
  data() {
    return {
      email: "",
      password: "",
    };
  },
  methods: {
    kakaoLogin() {
      window.location.href = `${this.$apiBaseUrl}/oauth2/authorization/kakao`;
    },
    login() {
      this.$axios
        .post(
          `${this.$apiBaseUrl}/api/login`,
          { email: this.email, password: this.password },
          {
            withCredentials: true, // 쿠키 포함하기 위해
          }
        )
        .then((response) => {
          // 스토어의 로그인 액션 호출
          this.$store.dispatch("login", { loginState: true });
          this.$router.push("/chat");
        })
        .catch((error) => {
          if (error.response.data.status === "1002") {
            alert(error.response.data.message);
          }
        });
    },
    register() {
      // 회원가입 페이지로 리다이렉션
      this.$router.push({ name: "Register" });
    },
  },
};
</script>

<style scoped>
.login-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;
  background-color: #f7f7f7;
  font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
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
  color: #34495e;
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
}

@media (max-width: 768px) {
  .form-group {
    max-width: 280px;
  }
}
</style>
