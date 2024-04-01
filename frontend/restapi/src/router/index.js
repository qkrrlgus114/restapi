import { createRouter, createWebHistory } from "vue-router";
import LoginView from "../views/LoginView.vue";
import RegisterView from "../views/RegisterView.vue";
import ChatView from "../views/ChatView.vue";
import MainView from "../views/MainView.vue";
import NotFoundComponent from "../components/NotFoundComponent.vue";
import Success from "../views/Success.vue";

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
];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
});

router.beforeEach((to, from, next) => {
  if (to.matched.some((record) => record.meta.requiresAuth)) {
    const loginState = localStorage.getItem("loginState") === "true";
    if (!loginState) {
      alert("로그인이 필요합니다.");
      next({ name: "Login" });
    } else {
      next();
    }
  } else {
    next();
  }
});

export default router;
