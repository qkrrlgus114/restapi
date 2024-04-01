<template>
  <nav class="navbar" v-if="!shouldHideNavbar">
    <div class="user-info">
      <h2>{{ nickname }}</h2>
      <h2>토큰 개수 : {{ token }}</h2>
      <div class="buttons">
        <button @click="refreshTokenCount" class="refresh-button">
          토큰 갱신
        </button>
        <button @click="logout" class="logout-button">로그아웃</button>
      </div>
    </div>
  </nav>
  <div class="coupon-info">
    <h2>오늘의 선착순 쿠폰(토큰) : {{ coupon }}개</h2>
    <button class="receive-button" @click="acquiredToken">토큰 받기</button>
  </div>
</template>

<script>
import { mapState } from "vuex";

export default {
  name: "Navbar",
  methods: {
    // 로그아웃
    logout() {
      this.$axios
        .get(`${this.$apiBaseUrl}/api/logout`, {
          withCredentials: true,
        })
        .then((response) => {
          this.$store.dispatch("logout");
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
          this.$store.dispatch("updateToken", response.data.data);

          alert("갱신 성공");
        })
        .catch((error) => {
          console.error("토큰 개수 갱신 중 오류 발생:", error);
        });
    },
    // 토큰 획득하기
    acquiredToken() {
      if (this.coupon <= 0) {
        alert("쿠폰이 전부 소진되었습니다.");
      } else {
        this.$axios
          .post(
            `${this.$apiBaseUrl}/api/coupons`,
            {},
            {
              withCredentials: true,
            }
          )
          .then((response) => {
            alert(response.data.message);
            this.$store.commit("incrementToken");
            this.$store.commit("decrementCoupon");
          })
          .catch((error) => {
            alert(error.response.data.message);
          });
      }
    },
  },
  computed: {
    shouldHideNavbar() {
      return this.$route.meta.hideNavbar;
    },
    ...mapState(["nickname", "token", "coupon"]),
  },
};
</script>

<style scoped>
.navbar {
  position: fixed; /* 네비게이션 바를 상단에 고정 */
  top: 0; /* 상단에 맞춤 */
  left: 0; /* 왼쪽에 맞춤 */
  width: 100%; /* 전체 너비 */
  height: 5%;
  display: flex;
  justify-content: space-between;
  background-color: #333;
  padding: 1rem;
}

.user-info {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  width: 100%;
}

.user-info h2 {
  margin-right: 20px;
  float: right;
  color: white;
}

.coupon-info {
  padding-top: 100px;
  display: flex;
  justify-content: center;
}

.buttons {
  display: flex;
  margin-right: 50px;
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

.receive-button {
  padding: 10px 20px; /* 버튼 내부의 상하좌우 여백 */
  background-color: #4caf50; /* 버튼 배경 색상 */
  color: white; /* 버튼 텍스트 색상 */
  border: none; /* 테두리 제거 */
  cursor: pointer; /* 마우스 오버 시 커서 변경 */
  border-radius: 5px; /* 버튼 모서리 둥글게 */
}

.receive-button:hover {
  background-color: #45a049; /* 버튼 마우스 오버 시 배경 색상 변경 */
}
</style>
