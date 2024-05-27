import { createRouter, createWebHistory } from "vue-router";
import LoginView from "../views/LoginView.vue";
import RegisterView from "../views/RegisterView.vue";
import ChatView from "../views/ChatView.vue";
import MainView from "../views/MainView.vue";
import NotFoundComponent from "../components/NotFoundComponent.vue";
import SuccessView from "../views/SuccessView.vue";
import Failure from "../views/Failure.vue";
import AdminView from "../views/AdminView.vue";
import { useMainStore } from "../store/store.js";
import RequestLogView from "@/views/RequestLogView.vue";
import UserBanComponent from "@/components/UserBanComponent.vue";
import SettingsComponent from "../components/SettingsComponent.vue";
import MyInfoView from "../views/MyInfoView.vue";
import WithdrawComponent from "@/components/WithdrawComponent.vue";
import MyInfoComponent from "@/components/MyInfoComponent.vue";
import InquiryComponent from "@/components/InquiryComponent.vue";

const routes = [
  {
    path: "/login",
    name: "Login",
    component: LoginView,
    meta: { showNavbar: false },
  },
  {
    path: "/register",
    name: "Register",
    component: RegisterView,
    meta: { showNavbar: false },
  },
  {
    path: "/chat",
    name: "Chat",
    component: ChatView,
    meta: { requiresAuth: true, showNavbar: true },
  },
  {
    path: "/",
    name: "Main",
    component: MainView,
    meta: { showNavbar: false },
  },
  {
    path: "/my",
    name: "My",
    component: MyInfoView,
    meta: { requiresAuth: true, showNavbar: true },
    children: [
      {
        path: "my-info",
        name: "MyInfo",
        component: MyInfoComponent,
      },
      {
        path: "withdraw",
        name: "Withdraw",
        component: WithdrawComponent,
      },
      {
        path: "inquiry",
        name: "Inquiry",
        component: InquiryComponent,
      },
    ],
  },
  {
    path: "/success",
    name: "SuccessView",
    component: SuccessView,
    meta: { showNavbar: false },
  },
  {
    path: "/failure",
    name: "Failure",
    component: Failure,
    meta: { showNavbar: false },
  },
  {
    path: "/:catchAll(.*)",
    name: "NotFoundComponent",
    component: NotFoundComponent,
    meta: { showNavbar: false },
  },
  {
    path: "/admin",
    name: "Admin",
    component: AdminView,
    meta: { requiresAdmin: true, requiresAuth: true, showNavbar: true },
    children: [
      {
        path: "settings",
        name: "SettingsComponent",
        component: SettingsComponent,
        meta: { showNavbar: true },
      },
      {
        path: "requests/:page",
        name: "RequestLog",
        component: RequestLogView,
        meta: { showNavbar: true },
      },
      {
        path: "ban",
        name: "UserBan",
        component: UserBanComponent,
        meta: { showNavbar: true },
      },
    ],
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.beforeEach((to, from, next) => {
  const store = useMainStore();
  const isLoggedIn = store.getLoginState;
  const isAdmin = store.getAdminRole; // 관리자 권한 확인

  // 로그인이 필요한 페이지인 경우
  if (to.matched.some((record) => record.meta.requiresAuth && !isLoggedIn)) {
    alert("로그인이 필요합니다.");
    next({ name: "Login" });
  } else if (
    to.matched.some((record) => record.meta.requiresAdmin && !isAdmin)
  ) {
    alert("관리자 권한이 필요합니다.");
    next({ name: "Chat" });
  } else {
    next();
  }
});

export default router;
