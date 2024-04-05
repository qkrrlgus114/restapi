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
  text-align: center;
  padding: 0;
  margin: 0;
  box-sizing: border-box;
}

button {
  margin-top: 20px;
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
