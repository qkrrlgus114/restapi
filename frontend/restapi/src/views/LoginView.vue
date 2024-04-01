<template>
  <div class="login-container">
    <h1>REST API 서비스</h1>
    <div class="form-group">
      <label for="email">이메일</label>
      <input type="email" id="email" v-model="email" required />
    </div>
    <div class="form-group">
      <label for="password">비밀번호</label>
      <input type="password" id="password" v-model="password" required />
    </div>
    <div class="button-group">
      <a @click="kakaoLogin"><img src="/kakao_login_medium_narrow.png" /></a>
      <button @click="login" class="mr-2">로그인</button>
      <button @click="register">회원가입</button>
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
  background-color: #f5f5f5;
}

h1 {
  color: #333;
  margin-bottom: 2rem;
}

.form-group {
  margin-bottom: 20px;
  width: 100%;
  max-width: 320px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-size: 16px;
  color: #666;
}

.form-group input {
  width: 100%;
  padding: 10px;
  font-size: 14px;
  border: 1px solid #ddd;
  border-radius: 4px;
  box-sizing: border-box; /* 패딩을 포함한 너비가 전체 너비가 되도록 */
}

button {
  padding: 12px 15px;
  border: none;
  border-radius: 4px;
  background-color: #007bff;
  color: white;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.button-group {
  display: flex; /* flexbox 레이아웃 적용 */
  align-items: center; /* 수직 중앙 정렬 */
  justify-content: center; /* 필요하다면 가운데 정렬 */
}

button:hover {
  background-color: #0056b3;
}

a {
  margin-top: 5px;
  margin-right: 10px;
}

button.mr-2 {
  margin-right: 8px;
}

/* 선택적: 화면 크기가 작을 때 스타일 조정 */
@media (max-width: 768px) {
  .form-group {
    max-width: 280px;
  }
}
</style>
