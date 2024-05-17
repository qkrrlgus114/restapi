<template>
  <div class="success-page">로그인 성공! 정보를 처리 중입니다...</div>
</template>

<script setup>
import { useMainStore } from "@/store/store.js";
import { onMounted } from "vue";
import { useRouter } from "vue-router";
import { useGlobalProperties } from "@/composables/useGlobalProperties";

const { $axios, $apiBaseUrl } = useGlobalProperties();
const store = useMainStore();
const router = useRouter();

onMounted(() => {
  socialLogin();
});

const socialLogin = async () => {
  try {
    await $axios.post(
      `${$apiBaseUrl}/api/social-login`,
      { social: true },
      { withCredentials: true }
    );
    store.loginState = true;
    router.push("/chat");
  } catch (error) {
    alert(error.response.data.message);
  }
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
