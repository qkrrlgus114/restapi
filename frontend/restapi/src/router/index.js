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
import ApiRequestLogComponent from "@/components/ApiRequestLogComponent.vue";
import UserBanComponent from "@/components/UserBanComponent.vue";
import SettingsComponent from "../components/SettingsComponent.vue";
import MyInfoView from "../views/MyInfoView.vue";
import WithdrawComponent from "@/components/WithdrawComponent.vue";
import MyInfoComponent from "@/components/MyInfoComponent.vue";
import InquiryBoardComponent from "@/components/InquiryBoardComponent.vue";
import InquiryDetailComponent from "@/components/InquiryDetailComponent.vue";
import InquiryFormComponent from "@/components/InquiryFormComponent.vue";
import AnswerFormComponent from "@/components/AnswerFormComponent.vue";

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
        path: "inquiry/:page",
        name: "InquiryBoard",
        component: InquiryBoardComponent,
      },
      {
        path: "inquiry/view/:id",
        name: "InquiryDetail",
        component: InquiryDetailComponent,
      },
      {
        path: "inquiry/write",
        name: "InquiryForm",
        component: InquiryFormComponent,
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
      },
      {
        path: "log/:page",
        name: "ApiRequestLog",
        component: ApiRequestLogComponent,
      },
      {
        path: "ban",
        name: "UserBan",
        component: UserBanComponent,
      },
      {
        path: "answer/:id/write",
        name: "AnswerForm",
        component: AnswerFormComponent,
        props: true,
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
