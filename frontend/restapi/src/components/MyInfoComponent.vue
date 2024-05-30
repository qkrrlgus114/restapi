<template>
  <div class="content">
    <div class="social-type" v-if="social">
      <img src="/kakao_small_logo.png" alt="Kakao" class="social-img" />
      <span class="social-label">Kakao</span>
    </div>
    <div class="info-container">
      <div class="info">
        <span class="label">닉네임 :</span>
        <span class="value">{{ nickname }}</span>
      </div>
      <div class="info">
        <span class="label">누적 사용 토큰 :</span>
        <span class="value">{{ totalUseToken }} 개</span>
      </div>
      <div class="info">
        <span class="label">누적 획득 토큰(쿠폰) :</span>
        <span class="value">{{ totalAcquisitionToken }} 개</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useMainStore } from "@/store/store.js";
import { onMounted, ref, computed } from "vue";
import { apiGet } from "@/utils/api";
import { useRouter } from "vue-router";

const store = useMainStore();
const router = useRouter();

const social = computed(() => store.social);
const nickname = computed(() => store.nickname);
const totalUseToken = ref(0);
const totalAcquisitionToken = ref(0);

onMounted(() => {
  loadUserInfo();
});

// 유저 개인정보 호출
const loadUserInfo = async () => {
  try {
    const data = await apiGet("api/members/info");

    social.value = data.data.socialMember;
    nickname.value = data.data.nickname;
    totalUseToken.value = data.data.totalUseToken;
    totalAcquisitionToken.value = data.data.totalAcquisitionToken;
  } catch (error) {}
};
</script>

<style scoped>
.content {
  flex: 8;
  background-color: #ffffff;
  padding: 20px;
  margin: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  border-radius: 10px;
}

.social-type {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  padding: 10px 20px;
  background-color: #f9f9f9;
  border: 1px solid #ddd;
  border-radius: 5px;
}

.social-img {
  width: 40px;
  margin-right: 10px;
}

.social-label {
  font-size: 1.5rem;
  font-weight: bold;
  color: #ffcc00;
}

.info-container {
  display: flex;
  flex-direction: column;
  width: 100%;
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 10px;
}

.info {
  display: flex;
  width: 100%;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px solid #ddd;
  font-size: 1.2rem;
}

.info:last-child {
  border-bottom: none;
}

.label {
  font-weight: bold;
  color: #333;
}

.value {
  text-align: right;
  color: #666;
}
</style>
