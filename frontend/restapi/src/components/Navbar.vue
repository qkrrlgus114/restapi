<template>
  <div class="main">
    <nav class="navbar">
      <a @click="goHome" class="navbar-brand">RESTAPI</a>
      <div class="menu">
        <router-link class="btn menu-api" :to="{ name: 'Chat' }">
          API 서비스
        </router-link>
        <router-link
          class="btn menu-category"
          :to="{ name: 'Board', params: { page: 1 } }"
        >
          공유 게시판
        </router-link>
      </div>
      <div class="user-info" v-if="loginStatus">
        <button class="custom-btn" v-if="isAdmin" @click="goAdmin">
          관리자 설정
        </button>
        <h2>{{ nickname }}</h2>
        <h2>남은 토큰 : {{ token }} 개</h2>
        <div class="reset-img" @click="renewToken">
          <img src="/reset.png" />
        </div>
        <div class="btn-group">
          <button
            class="btn btn-secondary dropdown-toggle custom-btn"
            data-bs-toggle="dropdown"
            aria-expanded="false"
          >
            메뉴
          </button>
          <ul class="dropdown-menu dropdown-menu-end">
            <li>
              <button class="dropdown-item" @click="goMyInfo">내정보</button>
            </li>
            <li>
              <button class="dropdown-item" @click="logout">로그아웃</button>
            </li>
          </ul>
        </div>
      </div>
      <div class="user-info" v-if="!loginStatus">
        <button
          class="btn btn-secondary custom-btn not-login-btn"
          @click="goLogin"
        >
          로그인
        </button>
      </div>
    </nav>
    <div class="coupon-info" v-if="loginState">
      <h2>오늘의 선착순 쿠폰(토큰) : {{ coupon }} 개</h2>
      <button class="receive-button custom-btn" @click="acquiredToken">
        토큰 받기
      </button>
    </div>
  </div>
</template>

<script setup>
import { useMainStore } from "@/store/store.js";
import { computed, onMounted, ref } from "vue";
import { apiGet, apiPost } from "@/utils/api";
import { useRouter } from "vue-router";

const store = useMainStore();
const router = useRouter();

const nickname = computed(() => store.nickname);
const coupon = computed(() => store.coupon);
const token = computed(() => store.token);
const isAdmin = ref(false);
const loginStatus = computed(() => store.loginState);

onMounted(async () => {
  if (loginStatus.value) {
    await loadUserInfo();
    await loadCouponInfo();
    const admin = store.getAdminRole;
    if (admin) isAdmin.value = true;
  }
});

// 유저 정보 호출
const loadUserInfo = async () => {
  try {
    const data = await apiGet("api/members");
    store.updateUserInfo(data.data);
  } catch (error) {}
};

// 쿠폰 정보 호출
const loadCouponInfo = async () => {
  try {
    const data = await apiGet("/api/coupons");
    store.updateCouponInfo(data.data);
  } catch (error) {}
};

// 로그아웃 함수
const logout = async () => {
  try {
    await apiGet("/api/logout");
    store.logout();
    router.push("/");
  } catch (error) {}
};

// 토큰 갱신
const renewToken = async () => {
  try {
    const data = await apiGet("/api/tokens");
    store.updateToken(data.data);
    alert(data.message);
  } catch (error) {}
};

// 데일리 쿠폰 사용하기(토큰 획득)
const acquiredToken = async () => {
  try {
    // pinia에서 쿠폰 데이터 먼저 확인
    if (store.coupon <= 0) {
      alert("쿠폰이 전부 소진되었습니다.");
      return;
    }

    await apiPost("/api/coupons");
    store.incrementToken();
    store.decrementCoupon();
    alert("토큰 1개를 획득하셨습니다.");
  } catch (error) {}
};

// 홈으로 이동
const goHome = () => router.push("/chat");
// 로그인으로 이동
const goLogin = () => router.push("/login");
// 공유게시판으로 이동
const goBoard = (page) => router.push(`/board/${page}`);
// 어드민 페이지로 이동
const goAdmin = () => router.push("/admin/settings");
// 내 정보 페이지로 이동
const goMyInfo = () => {
  router.push("/my/my-info");
};
</script>

<style scoped>
.main {
  display: flex;
  flex-direction: column;
}

.navbar {
  display: flex;
  justify-content: space-between;
  padding: 0.75rem 1.5rem;
  background-color: #2c3e50;
  color: white;
}

.navbar-brand {
  flex: 1;
  display: flex;
  justify-content: flex-start;
}

.menu {
  flex: 6;
  display: flex;
  justify-content: center;
  margin-left: 300px;
}

.menu-api,
.menu-board {
  cursor: pointer;
}

.menu-api:hover,
.menu-board:hover {
  transition: transform 0.3s ease-in-out;
  transform: scale(1.1);
}

.menu-api {
  font-size: 1.5rem;
  margin-right: 50px;
}

.menu-board {
  font-size: 1.5rem;
}

.user-info {
  flex: 3;
  display: flex;
  justify-content: flex-end;
  align-items: center;
  width: 80%;
}

.user-info h2 {
  margin: 0 1rem;
  color: white;
  font-size: 1rem;
}

.coupon-info {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 0.75rem 1.5rem;
  color: white;
  background-color: #22252b;
}

.coupon-info h2 {
  font-size: 1.5rem;
  margin: 0;
}

.custom-btn {
  padding: 0.5rem 1rem;
  margin-left: 10px;
  background-color: transparent;
  color: white;
  border: 1px solid white;
  border-radius: 0.375rem;
  cursor: pointer;
  transition: background-color 0.2s, border-color 0.2s, color 0.2s;
}

.custom-btn:hover {
  background-color: white;
  color: #5a5a5a;
}

.receive-button:hover {
  border-color: #e0e0e0;
}

.dropdown-item:hover {
  background-color: #dadada;
}
.dropdown-item:active {
  background-color: #dadada;
  color: black;
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
  text-decoration: none;
  color: white;
  font-size: 1.5rem;
  font-weight: bold;
  text-align: center;
  transition: transform 0.3s ease-in-out;
}

a:hover {
  transform: scale(1.1);
  cursor: pointer;
}

.reset-img > img {
  width: 20px;
  height: 20px;
  cursor: pointer;
}

.not-login-btn {
  width: 100px;
}
</style>
