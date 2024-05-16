import { createApp } from "vue";
import { createPinia } from "pinia";
import piniaPluginPersistedstate from "pinia-plugin-persistedstate";
import App from "./App.vue";
import router from "./router";
import axios from "axios"; // 전역으로 사용하기 위해 main.js에서 설정

// Axios 인스턴스 생성 및 기본 구성
const axiosInstance = axios.create();

const app = createApp(App);
const pinia = createPinia();
pinia.use(piniaPluginPersistedstate);
app.use(pinia);

// 라우터 설정
app.use(router);

app.config.globalProperties.$axios = axiosInstance;
// app.config.globalProperties.$apiBaseUrl = "https://restapi.store";
app.config.globalProperties.$apiBaseUrl = "http://localhost:8080";

app.mount("#app");
