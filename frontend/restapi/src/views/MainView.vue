<template>
  <div class="overlay">
    <div><h1 class="title">RESTFUL API 생성 서비스</h1></div>
    <div>
      <p class="subtitle">
        아...이건 어떻게 해야 RESTFUL하지? <br />아하! 이 서비스를 이용해볼까?
      </p>
    </div>
    <button @click="goToLogin" class="admission-button">사용해주마</button>
  </div>
</template>

<script>
export default {
  methods: {
    goToLogin() {
      this.$router.push("/login");
    },
  },
};
</script>

<style scoped>
.overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background-image: url("/back.png");
  background-size: cover;
  background-position: center;
  color: white;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.title {
  font-size: 4.5em;
  margin-bottom: 0.5em;
}

.subtitle {
  font-size: 1.5em;
  margin-bottom: 2em; /* 버튼과의 간격을 조정 */
}

.admission-button {
  padding: 20px 100px;
  background-color: #3e4853;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 2em;
  font-weight: bold;
  text-transform: uppercase;
  transition: background-color 0.3s;
}

.admission-button:hover {
  background-color: #000000;
}
</style>
