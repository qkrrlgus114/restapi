<template>
  <navbar v-if="showNavbar"></navbar>
  <router-view></router-view>
</template>

<script setup>
import Navbar from "./components/Navbar.vue";
import { useRoute } from "vue-router";
import { computed } from "vue";

const route = useRoute();
const showNavbar = computed(() => {
  return route.meta.showNavbar !== false;
});
</script>

<style scoped></style>
