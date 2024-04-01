// Vuex 4.x를 사용하여 Vue 3와 호환되도록 설정
import { createStore } from "vuex";
import createPersistedState from "vuex-persistedstate";

export default createStore({
  state: {
    loginState: false,
    nickname: "",
    token: 0,
  },
  // state를 변경하는 메서드
  mutations: {
    // 로그인
    setLoginState(state, payload) {
      state.loginState = payload.loginState;
      state.nickname = payload.nickname;
      state.token = payload.token;
    },
    //로그아웃
    logout(state) {
      state.loginState = false;
      state.nickname = "";
      state.token = 0;
    },
  },
  actions: {
    login({ commit }, payload) {
      commit("setLoginState", payload);
      localStorage.setItem("loginState", payload.loginState);
    },
    logout({ commit }) {
      commit("setLoginState");
      localStorage.removeItem("loginState");
    },
  },
  plugins: [
    createPersistedState({
      paths: ["loginState", "nickname"],
    }),
  ],
});
