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
        @input="validateEmail"
        required
      />
      <button @click="sendCertificationCode" :disabled="!EmailValid">
        인증번호 전송
      </button>
    </div>
    <div v-if="showCertificationInput">
      <div>
        <label for="certificationCode">인증번호</label>
        <input
          type="text"
          id="certificationCode"
          v-model="certificationCode"
          :disabled="isCertificationVerified"
          required
        />
        <button class="auth-btn" @click="checkCertificationCode">인증</button>
        <button @click="sendCertificationCode">재전송</button>
      </div>
    </div>
    <div>
      <label for="password">비밀번호</label>
      <input
        type="password"
        id="password"
        v-model="password"
        @input="validatePassword"
        required
      />
      <p v-if="!passwordValid && password.length > 0">
        비밀번호는 8~15자 영문+숫자여야 합니다.
      </p>
    </div>
    <div>
      <label for="passwordConfirm">비밀번호 확인</label>
      <input
        type="password"
        id="passwordConfirm"
        v-model="passwordConfirm"
        @input="validatePassword"
        required
      />
      <p v-if="!passwordsMatch && passwordConfirm.length > 0">
        비밀번호가 일치하지 않습니다.
      </p>
    </div>
    <div>
      <label for="nickname">닉네임</label>
      <input
        type="text"
        id="nickname"
        v-model="nickname"
        @input="validateNickname"
        required
      />
    </div>

    <button @click="register">회원가입</button>
  </div>
</template>

<script>
export default {
  name: "RegisterView",
  data() {
    return {
      email: "",
      certificationCode: "",
      password: "",
      passwordConfirm: "",
      nickname: "",
      showCertificationInput: false, // 인증번호 입력칸
      isLoading: false, // 인증번호 전송중
      isCertificationVerified: false, // 인증상태
      passwordValid: false, // 비밀번호 검증
      passwordsMatch: false, // 비밀번호 일치여부
      EmailValid: false, // 이메일 입력여부
      nicknameValid: false, // 닉네임 입력여부
    };
  },
  methods: {
    // 이메일로 인증번호 전송
    sendCertificationCode() {
      this.isLoading = true;
      this.$axios
        .post(`${this.$apiBaseUrl}/api/authentications/send`, {
          email: this.email,
        })
        .then((response) => {
          this.showCertificationInput = true;
        })
        .catch((error) => {
          if (error.response.data.status === "2003") {
            alert("이미 가입된 이메일입니다.");
          }
        })
        .finally(() => {
          this.isLoading = false;
        });
    },
    // 인증번호 인증
    checkCertificationCode() {
      this.$axios
        .post(`${this.$apiBaseUrl}/api/authentications/verify`, {
          certificationCode: this.certificationCode,
        })
        .then((response) => {
          alert("인증되었습니다.");
          this.isCertificationVerified = true;
        })
        .catch((error) => {
          if (error.response.data.status === "2001") {
            alert(error.response.data.message);
          } else if (error.response.data.status === "2002") {
            alert(error.response.data.message);
          }
        });
    },
    // 비밀번호 입력 확인
    validatePassword() {
      // 영문, 숫자를 포함하며, 8자 이상 15자 이하인 경우에 유효
      const regex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,15}$/;
      this.passwordValid = regex.test(this.password);
      this.passwordsMatch = this.password === this.passwordConfirm;
    },
    // 이메일 입력 확인
    validateEmail() {
      const regex = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
      this.EmailValid = regex.test(this.email);
    },
    // 닉네임 입력 확인
    validateNickname() {
      // 한글, 영문, 숫자만 허용
      const regex = /^[a-zA-Z가-힣0-9]*$/;
      this.nicknameValid = regex.test(this.nickname);
    },
    // 회원가입
    register() {
      if (
        this.EmailValid &&
        this.passwordValid &&
        this.passwordsMatch &&
        this.isCertificationVerified &&
        this.nicknameValid
      ) {
        // 입력값이 모두 유효한 경우에만 요청을 보냄
        const userData = {
          email: this.email,
          password: this.password,
          nickname: this.nickname,
        };

        this.$axios
          .post(`${this.$apiBaseUrl}/api/signup`, userData)
          .then((response) => {
            // 회원가입 성공 처리
            alert("회원가입이 성공적으로 완료되었습니다.");
            this.$router.push("/login");
          })
          .catch((error) => {
            // 회원가입 실패 처리
            alert("회원가입 중 문제가 발생했습니다.");
            console.error("회원가입 오류:", error);
          });
      } else {
        // 입력값 중 하나라도 유효하지 않은 경우
        alert("입력하신 정보를 다시 확인해주세요.");
      }
    },
  },
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
