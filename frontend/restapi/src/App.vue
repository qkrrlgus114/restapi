<template>
  <navbar v-if="loginState"></navbar>
  <router-view></router-view>
</template>

<script setup>
import Navbar from "./components/Navbar.vue";
import { useMainStore } from "@/store/store.js";
import { computed } from "vue";

const store = useMainStore();
const loginState = computed(() => store.state.loginState);
</script>

<style scoped></style>
