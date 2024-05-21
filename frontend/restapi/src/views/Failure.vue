<template></template>

<script setup>
import { onMounted } from "vue";
import { useRouter } from "vue-router";

const router = useRouter();

onMounted(() => {
  alert("이용이 불가능한 계정입니다.");
  router.push("/login");
});
</script>

<style scoped></style>
