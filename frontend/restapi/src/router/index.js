import { createRouter, createWebHistory } from "vue-router";
import LoginView from "../views/LoginView.vue";
import RegisterView from "../views/RegisterView.vue";
import ChatView from "../views/ChatView.vue";
import MainView from "../views/MainView.vue";
import NotFoundComponent from "../components/NotFoundComponent.vue";
import Success from "../views/Success.vue";
import AdminView from "../views/AdminView.vue";
import Store from "../store/store.js";
import RequestLogView from "@/views/RequestLogView.vue";
import UserBanView from "@/views/UserBanView.vue";
import SettingsView from "../views/SettingsView.vue";

const routes = [
  {
    path: "/loginview",
    name: "Login",
    component: LoginView,
    meta: { hideNavbar: true },
  },
  {
    path: "/register",
    name: "Register",
    component: RegisterView,
    meta: { hideNavbar: true },
  },
  {
    path: "/chat",
    name: "Chat",
    component: ChatView,
    meta: { requiresAuth: true },
  },
  {
    path: "/",
    name: "Main",
    component: MainView,
    meta: { hideNavbar: true },
  },
  {
    path: "/success",
    name: "Success",
    component: Success,
    meta: { hideNavbar: true },
  },
  {
    path: "/:catchAll(.*)",
    name: "NotFoundComponent",
    component: NotFoundComponent,
    meta: { hideNavbar: true },
  },
  {
    path: "/admin",
    name: "Admin",
    component: AdminView,
    meta: { requiresAdmin: true, requiresAuth: true },
    children: [
      {
        path: "settings",
        name: "Settings",
        component: SettingsView,
      },
      { path: "requests", name: "RequestLog", component: RequestLogView },
      { path: "ban", name: "UserBan", component: UserBanView },
    ],
  },
];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
});

router.beforeEach((to, from, next) => {
  const isLoggedIn = Store.state.loginState; // 로그인 상태
  const isAdmin = Store.state.memberRoles.includes("ADMIN"); // 관리자 권한 확인

  if (to.matched.some((record) => record.meta.requiresAuth && !isLoggedIn)) {
    // 로그인이 필요한 페이지에 로그인하지 않은 경우
    alert("로그인이 필요합니다.");
    next({ name: "Login" });
  } else if (
    to.matched.some((record) => record.meta.requiresAdmin && !isAdmin)
  ) {
    // 관리자 권한이 필요한 페이지에 관리자 권한이 없는 경우
    alert("관리자 권한이 필요합니다.");
    next({ name: "Chat" });
  } else {
    next();
  }
});

export default router;
