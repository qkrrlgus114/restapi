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

<style scoped></style>
