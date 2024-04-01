<template>
  <div class="success-page">로그인 성공! 정보를 처리 중입니다...</div>
</template>

<script>
export default {
  name: "SuccessPage",
  mounted() {
    this.$axios
      .post(
        `${this.$apiBaseUrl}/api/social-login`,
        { social: true }, // 로그인에 필요한 데이터를 여기에 추가
        {
          withCredentials: true,
        }
      )
      .then((response) => {
        // 스토어의 로그인 액션 호출
        this.$store.dispatch("login", { loginState: true });
        this.$router.push("/chat");
      })
      .catch((error) => {
        console.error("인증 과정 중 오류 발생:", error);
      });
  },
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
