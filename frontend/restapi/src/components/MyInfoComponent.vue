<template>
  <div class="content">
    <div class="social-type">
      <img
        src="/kakao_small_logo.png"
        alt=""
        v-if="social"
        class="social-img"
      />
    </div>
    <div class="info-container">
      <div class="info">
        <span class="label">닉네임 :</span>
        <span class="value"> {{ nickname }}</span>
      </div>
      <div class="info">
        <span class="label">누적 사용 토큰 :</span>
        <span class="value"> {{ totalUseToken }}개</span>
      </div>
      <div class="info">
        <span class="label">누적 획득 토큰 :</span>
        <span class="value"> {{ totalAcquisitionToken }}개</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useMainStore } from "@/store/store.js";
import { onMounted, ref } from "vue";
import { apiGet } from "@/utils/api";
import { useRouter } from "vue-router";

const store = useMainStore();
const router = useRouter();

const social = ref(false);
const nickname = ref("");
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
  background-color: rgb(113, 122, 119);
  padding: 10px;
  margin: 20px;
  display: flex;
  flex-direction: column;
}

.social-img {
  width: 50px;
}

.info-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
}

.info {
  padding: 50px 0px;
  font-size: 3rem;
}

.info {
  display: flex;
  width: 100%;
  justify-content: flex-start;
  margin: 10px 0;
  font-size: 1.5rem;
}

.label {
  width: 200px;
  font-weight: bold;
  display: flex;
  justify-content: flex-end;
}

.value {
  flex: 1;
  text-align: left;
}
</style>
