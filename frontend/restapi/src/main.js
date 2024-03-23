import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import axios from "axios";

const app = createApp(App);
app.config.globalProperties.$apiBaseUrl = "http://localhost:8080";
app.config.globalProperties.$axios = axios;

app.use(router).mount("#app");
