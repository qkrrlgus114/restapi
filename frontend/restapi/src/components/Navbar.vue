<template>
  <div v-if="!shouldHideNavbar">
    <nav class="navbar">
      <a @click="goHome" img>RESTAPI</a>
      <div class="user-info">
        <button v-if="isAdmin" @click="goAdmin">관리자 설정</button>
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
    goAdmin() {
      this.$router.push("/admin/settings");
    },
    goHome() {
      this.$router.push("/");
    },
  },
  computed: {
    shouldHideNavbar() {
      return this.$route.meta.hideNavbar;
    },
    ...mapState(["nickname", "token", "coupon", "memberRoles"]),
    isAdmin() {
      return this.memberRoles.includes("ADMIN");
    },
  },
};
</script>

<style scoped>
.user-info {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  width: 100%;
}

.user-info h2 {
  margin: 0 1rem;
  color: white;
  font-size: 1rem;
}
.navbar,
.coupon-info {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 0.75rem 1.5rem;
  background-color: #2c3e50;
  color: white;
}

.coupon-info {
  margin-top: 4rem;
  background-color: #22252b;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  position: relative;
  z-index: 998;
}

.navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.75rem 1.5rem;
  background-color: #2c3e50;
  color: white;
  position: fixed;
  top: 0;
  width: 100%;
  z-index: 999;
}

.buttons {
  display: flex;
  margin-right: 50px;
}

button {
  padding: 0.5rem 1rem;
  margin-left: 10px;
  background-color: transparent;
  color: white;
  border: 1px solid white;
  border-radius: 0.375rem;
  cursor: pointer;
  transition: background-color 0.2s, border-color 0.2s, color 0.2s;
}

button:hover {
  background-color: white;
  color: #5a5a5a;
}

.refresh-button:hover,
.logout-button:hover,
.receive-button:hover {
  border-color: #e0e0e0;
}

@media (max-width: 768px) {
  .navbar {
    flex-direction: column;
    align-items: flex-start;
  }

  .coupon-info {
    flex-direction: column;
    align-items: center;
  }

  .receive-button {
    margin-top: 10px;
  }

  .buttons {
    flex-direction: column;
    align-items: flex-start;
  }
}

a {
  flex-grow: 1; /* 로고, restapi 추천, user-info 사이의 공간을 균등하게 나눔 */
  text-decoration: none;
  color: white;
  font-size: 1.5rem;
  font-weight: bold;
  text-align: center; /* 텍스트를 중앙 정렬 */
  transition: transform 0.3s ease-in-out;
}

a:hover {
  transform: scale(1.1);
  cursor: pointer;
}
</style>
