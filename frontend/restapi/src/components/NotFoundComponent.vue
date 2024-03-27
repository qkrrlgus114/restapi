<template>
  <div class="main">
    <h1>404 Not Found</h1>
    <p>요청하신 페이지를 찾을 수 없습니다.</p>
    <button @click="goHome">메인으로 이동하기</button>
  </div>
</template>

<script>
export default {
  methods: {
    goHome() {
      this.$router.push("/");
    },
  },
};
</script>

<style scoped>
.main {
  height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  text-align: center; /* 텍스트 중앙 정렬 */
  padding: 0; /* 패딩 초기화 */
  margin: 0; /* 마진 초기화 */
  box-sizing: border-box; /* 박스 사이징 규칙 설정 */
}

button {
  margin-top: 20px; /* 버튼과 텍스트 간 간격 추가 */
  padding: 10px 20px;
  background-color: #2e2f30;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 1em;
}

button:hover {
  background-color: #7d8187;
}
</style>
