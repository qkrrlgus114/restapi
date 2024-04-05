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
      this.$router.push("/loginview");
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
  background: linear-gradient(rgba(0, 0, 0, 0.5), rgba(0, 0, 0, 0.5)),
    url("/back.png") no-repeat center center;
  background-size: cover;
  color: #f7f7f7;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  text-align: center;
}

.title {
  font-size: 4em;
  font-weight: 300;
  margin-bottom: 0.25em;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5);
  font-family: "Montserrat", sans-serif;
}

.subtitle {
  font-size: 1.25em;
  max-width: 800px;
  margin-bottom: 1.5em;
  line-height: 1.6;
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.5);
  font-family: "Open Sans", sans-serif;
  opacity: 0.85;
  max-width: 600px;
}

.admission-button {
  padding: 15px 80px;
  background-color: rgba(255, 255, 255, 0.2);
  color: white;
  border-radius: 50px;
  cursor: pointer;
  font-size: 1.75em;
  font-weight: 600;
  letter-spacing: 1px;
  text-transform: uppercase;
  transition: all 0.3s ease-in-out;
  border: 2px solid rgba(255, 255, 255, 0.5);
  background: none;
  box-shadow: none;
}

.admission-button:hover {
  background-color: rgba(255, 255, 255, 0.2);
  border-color: rgba(255, 255, 255, 0.75);
}
@keyframes fadeInUp {
  from {
    transform: translate3d(0, 20px, 0);
    opacity: 0;
  }

  to {
    transform: translate3d(0, 0, 0);
    opacity: 1;
  }
}

.overlay > * {
  animation: fadeInUp 0.8s ease-out forwards;
  animation-delay: 0.2s;
}

.title {
  animation-delay: 0.2s;
}
.subtitle {
  animation-delay: 0.4s;
}
.admission-button {
  animation-delay: 0.6s;
}
</style>
