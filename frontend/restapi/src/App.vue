<template>
  <navbar v-if="showNavbar"></navbar>
  <router-view></router-view>
</template>

<script setup>
import Navbar from "./components/Navbar.vue";
import { useRoute } from "vue-router";
import { ref, watch } from "vue";

const route = useRoute();
const showNavbar = ref(route.meta.showNavbar === false);

watch(route, (newRoute) => {
  showNavbar.value = newRoute.meta.showNavbar !== false;
});
</script>

<style scoped></style>
