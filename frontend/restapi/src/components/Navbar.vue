<template>
  <div v-if="!shouldHideNavbar">
    <nav class="navbar">
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
  justify-content: center; /* 내용을 양쪽으로 분배 */
  align-items: center;
  padding: 0.75rem 1.5rem; /* 여백 조정 */
  background-color: #2c3e50; /* 색상 조정 */
  color: white;
}

.coupon-info {
  margin-top: 4rem; /* 네비게이션 바의 고정 위치 아래로 간격 조정 */
  background-color: #22252b; /* 네비게이션 바와 비슷하지만 약간 다른 색상 */
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); /* 하단 그림자 효과 추가 */
  position: relative;
  z-index: 998; /* 네비게이션 바 바로 아래 */
}

.navbar {
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
  background-color: #3498db;
  color: white;
  border: none;
  border-radius: 0.375rem; /* 버튼 모서리를 약간 둥글게 */
  cursor: pointer;
  transition: background-color 0.2s;
}

button:hover {
  background-color: #2980b9;
}

.refresh-button {
  background-color: #109e28; /* 갱신 버튼에는 다른 색상 적용 */
}

.refresh-button:hover {
  background-color: #18773f;
}

.logout-button {
  background-color: #e74c3c; /* 로그아웃 버튼에는 경고 색상 적용 */
}

.logout-button:hover {
  background-color: #c0392b;
}

.receive-button {
  padding: 0.5rem 1rem;
  margin-left: 10px;
  background-color: #149900;
  border: none;
  border-radius: 0.375rem;
  cursor: pointer;
  transition: background-color 0.2s;
  font-size: 0.9rem;
  font-weight: 600;
}

.receive-button:hover {
  background-color: #42b72a;
}

@media (max-width: 768px) {
  .navbar {
    flex-direction: column;
    align-items: flex-start; /* 상단 바의 아이템을 왼쪽으로 정렬 */
  }

  .coupon-info {
    flex-direction: column;
    align-items: center; /* 쿠폰 정보를 중앙에 정렬 */
  }

  .receive-button {
    margin-top: 10px; /* 쿠폰 버튼 위에 마진 추가 */
  }

  .buttons {
    flex-direction: column;
    align-items: flex-start; /* 버튼들을 위로 쌓아 올림 */
  }
}
</style>
