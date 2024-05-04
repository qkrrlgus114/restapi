import { createApp } from "vue";
import { createPinia } from "pinia";
import piniaPluginPersistedstate from "pinia-plugin-persistedstate";
import App from "./App.vue";
import router from "./router";
import axios from "axios";

const app = createApp(App);
const pinia = createPinia();
pinia.use(piniaPluginPersistedstate);
app.ues(axios);
app.use(pinia);
app.use(router).mount("#app");

// app.config.globalProperties.$apiBaseUrl = "https://restapi.store";
app.config.globalProperties.$apiBaseUrl = "http://localhost:8080";
app.config.globalProperties.axios = axios;
