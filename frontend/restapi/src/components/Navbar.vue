<template>
  <nav class="navbar" v-if="!shouldHideNavbar">
    <a @click="navigateHome" class="navbar-brand">REST API 추천 서비스</a>
    <div v-if="isLoggedIn" class="user-info">
      <h2>{{ nickname }}</h2>
      <h2>토큰 개수 : {{ token }}</h2>
      <button @click="refreshTokenCount" class="refresh-button">
        토큰 갱신
      </button>
      <button v-if="isLoggedIn" @click="logout" class="logout-button">
        로그아웃
      </button>
    </div>
  </nav>
</template>

<script>
export default {
  name: "Navbar",
  mounted() {
    this.checkLoginStatus();
  },
  data() {
    return {
      isLoggedIn: false,
      nickname: "",
      token: 0,
    };
  },
  methods: {
    // 로그아웃
    logout() {
      this.$axios
        .get(`${this.$apiBaseUrl}/api/logout`, {
          withCredentials: true,
        })
        .then((response) => {
          localStorage.removeItem("isLoggedIn");
          localStorage.removeItem("nickname");
          localStorage.removeItem("token");
          this.$router.push("/");
        })
        .catch((error) => {
          console.error("토큰 개수 갱신 중 오류 발생:", error);
        });
    },
    // 토큰 갱신
    refreshTokenCount() {
      this.$axios
        .get(`${this.$apiBaseUrl}/api/tokens`, {
          withCredentials: true,
        })
        .then((response) => {
          const newTokenCount = response.data.data;
          // 로컬 스토리지에도 토큰 개수 저장
          localStorage.setItem("token", newTokenCount);

          this.token = newTokenCount;

          alert("갱신 성공");
        })
        .catch((error) => {
          console.error("토큰 개수 갱신 중 오류 발생:", error);
        });
    },
    // 로고 클릭
    navigateHome() {
      const isLoggedIn = localStorage.getItem("isLoggedIn");
      if (isLoggedIn) {
        this.$router.push("/chat");
      } else {
        this.$router.push("/");
      }
    },
    // 정보 업데이트
    checkLoginStatus() {
      const isLoggedIn = localStorage.getItem("isLoggedIn") === "true";
      const nickname = localStorage.getItem("nickname");
      const token = localStorage.getItem("token");
      if (isLoggedIn) {
        this.isLoggedIn = true;
        this.nickname = nickname;
        this.token = token;
      }
    },
  },
  computed: {
    shouldHideNavbar() {
      return this.$route.meta.hideNavbar;
    },
  },
};
</script>

<style scoped>
.navbar {
  position: fixed; /* 네비게이션 바를 상단에 고정 */
  top: 0; /* 상단에 맞춤 */
  left: 0; /* 왼쪽에 맞춤 */
  width: 100%; /* 전체 너비 */
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #333;
  color: white;
  padding: 1rem;
  z-index: 1000; /* 다른 요소들 위에 나타나도록 z-index 설정 */
}

.navbar-brand {
  font-weight: bold;
  color: white;
  text-decoration: none;
}

.user-info h2 {
  display: inline;
  margin-right: 20px;
  color: white;
}

button {
  padding: 8px 20px;
  margin-left: 10px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button:hover {
  background-color: #0056b3;
}

.refresh-button {
  /* 추가적인 스타일링이 필요하다면 여기에 작성 */
}

.logout-button {
  margin-right: 50px;
}
</style>
