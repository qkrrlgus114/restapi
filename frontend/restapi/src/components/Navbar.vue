<template>
  <div>
    <nav class="navbar">
      <a @click="goHome" img>RESTAPI</a>
      <div class="user-info">
        <button v-if="isAdmin" @click="goAdmin">관리자 설정</button>
        <h2>{{ nickname }}</h2>
        <h2>남은 토큰 : {{ token }} 개</h2>
        <div class="buttons">
          <button @click="renewToken" class="refresh-button">토큰 갱신</button>
          <button @click="logout" class="logout-button">로그아웃</button>
        </div>
      </div>
    </nav>
    <div class="coupon-info">
      <h2>오늘의 선착순 쿠폰(토큰) : {{ coupon }} 개</h2>
      <button class="receive-button" @click="acquiredToken">토큰 받기</button>
    </div>
  </div>
</template>

<script setup>
import { useMainStore } from "@/store/store.js";
import { computed, onMounted, ref } from "vue";
import { useRouter } from "vue-router";
import { useGlobalProperties } from "@/composables/useGlobalProperties";

const { $axios, $apiBaseUrl } = useGlobalProperties();
const store = useMainStore();
const router = useRouter();

const nickname = computed(() => store.nickname);
const coupon = computed(() => store.coupon);
const token = computed(() => store.token);
const isAdmin = ref(false);

onMounted(() => {
  loadUserInfo();
  loadCouponInfo();
  const admin = store.getAdminRole;
  if (admin) isAdmin.value = true;
});

// 유저 정보 호출
const loadUserInfo = async () => {
  try {
    const response = await $axios.get(`${$apiBaseUrl}/api/users`, {
      withCredentials: true,
    });
    store.updateUserInfo(response.data.data);
  } catch (error) {
    alert(error.response.data.message);
  }
};

// 쿠폰 정보 호출
const loadCouponInfo = async () => {
  try {
    const response = await $axios.get(`${$apiBaseUrl}/api/coupons`, {
      withCredentials: true,
    });
    store.updateCouponInfo(response.data.data);
  } catch (error) {
    alert(error.response.data.message);
  }
};

// 로그아웃 함수
const logout = async () => {
  try {
    await $axios.get(`${$apiBaseUrl}/api/logout`, {
      withCredentials: true,
    });
    store.logout();
    router.push("/");
  } catch (error) {
    alert(error.response.data.message);
  }
};

// 토큰 갱신
const renewToken = async () => {
  try {
    const response = await $axios.get(`${$apiBaseUrl}/api/tokens`, {
      withCredentials: true,
    });
    store.updateToken(response.data.data);
    alert(response.data.message);
  } catch (error) {
    alert(error.response.data.message);
  }
};

// 데일리 쿠폰 사용하기(토큰 획득)
const acquiredToken = async () => {
  try {
    // pinia에서 쿠폰 데이터 먼저 확인
    if (store.coupon <= 0) {
      alert("쿠폰이 전부 소진되었습니다.");
      return;
    }

    const response = await $axios.post(
      `${$apiBaseUrl}/api/coupons`,
      {},
      {
        withCredentials: true,
      }
    );
    store.incrementToken();
    store.decrementCoupon();
    alert("토큰 1개를 획득하셨습니다.");
  } catch (error) {
    alert(error.response.data.message);
  }
};

// 홈으로 이동
const goHome = () => router.push("/");
// 어드민 페이지로 이동
const goAdmin = () => router.push("/admin/settings");
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
