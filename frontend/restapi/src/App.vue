<template>
  <navbar v-if="loginState"></navbar>
  <router-view></router-view>
</template>

<script>
import Navbar from "./components/Navbar.vue";
import { mapState } from "vuex";

export default {
  name: "App",
  components: { Navbar },
  computed: {
    ...mapState(["loginState"]),
  },
};
</script>

<style scoped>
.navbar {
  position: fixed; /* 네비게이션 바를 상단에 고정 */
  top: 0; /* 상단에 맞춤 */
  left: 0; /* 왼쪽에 맞춤 */
  width: 100%; /* 전체 너비 */
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #333;
  color: white;
  padding: 1rem;
  z-index: 1000; /* 다른 요소들 위에 나타나도록 z-index 설정 */
}

.navbar-brand {
  font-weight: bold;
  color: white;
  text-decoration: none;
}

.user-info h2 {
  display: inline;
  margin-right: 20px;
  color: white;
}

button {
  padding: 8px 20px;
  margin-left: 10px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button:hover {
  background-color: #0056b3;
}

.logout-button {
  margin-right: 50px;
}
</style>
