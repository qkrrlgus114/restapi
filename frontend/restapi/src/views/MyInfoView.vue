<template>
  <div class="main">
    <div class="side-menu">
      <div class="menu">
        <button class="btn menu-category">개인 정보</button>
      </div>
      <div class="menu">
        <button class="btn menu-category">회원 탈퇴</button>
      </div>
      <div class="menu">
        <button class="btn menu-category">1:1 문의</button>
      </div>
    </div>
    <div class="content">컨텐츠</div>
  </div>
</template>

<script setup>
import { useMainStore } from "@/store/store.js";
import { ref } from "vue";
import { useRouter } from "vue-router";
import { apiPost } from "@/utils/api";

const store = useMainStore();
const router = useRouter();

const formData = ref({ model: "", methodType: "", resource: "", content: "" });
const isLoading = ref(false);
const contentItems = ref([]);

// rest api 제출
const submitForm = async () => {
  if (store.getToken <= 0) {
    alert("토큰이 부족합니다.");
    return;
  }
  isLoading.value = true;

  try {
    const data = await apiPost("/api/gpt/recommendations", formData.value);
    alert(data.message);
    const choices = data.data.choices;
    const recommendations = choices[0].message.content.split(", ");
    contentItems.value = recommendations;
    store.decrementToken();
  } catch (error) {
  } finally {
    isLoading.value = false;
  }
};
</script>

<style scoped>
.main {
  display: flex;
  width: 100%;
  height: 100vh;
}

.side-menu {
  flex: 2;
  background-color: #f5f5f5;
  margin: 20px;
  padding-top: 150px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.content {
  flex: 8;
  background-color: rgb(113, 122, 119);
  padding: 10px;
  margin: 20px;
}

.btn {
  font-size: 2.5rem;
}

.menu {
  width: 100%;
  display: flex;
  justify-content: center;
}

.menu-category {
  margin: 40px 0px;
  width: 100%;
}

.menu-category:hover {
  background-color: #e0e0e0;
}
</style>
