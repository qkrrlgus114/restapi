<template>
  <div class="success-page">로그인 성공! 정보를 처리 중입니다...</div>
</template>

<script setup>
import { useMainStore } from "@/store/store.js";
import { onMounted } from "vue";
import { useRouter } from "vue-router";
import { apiPost, apiGet } from "@/utils/api";

const store = useMainStore();
const router = useRouter();

onMounted(() => {
  socialLogin();
});

const socialLogin = async () => {
  try {
    await apiPost("/api/social-login", {
      social: true,
    });
    store.loginState = true;
    router.push("/chat");
  } catch (error) {}
};
</script>

<style scoped>
.success-page {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  text-align: center;
}
</style>
