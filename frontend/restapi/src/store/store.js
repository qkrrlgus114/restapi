// Vuex 4.x를 사용하여 Vue 3와 호환되도록 설정
import { createStore } from "vuex";
import createPersistedState from "vuex-persistedstate";

export default createStore({
  state: {
    loginState: false,
    nickname: "",
    token: 0,
    coupon: 0,
    memberRoles: [],
    isDailyCouponGenerate: false,
    dailyCouponQuantity: 0,
  },
  getters: {
    getToken(state) {
      return state.token;
    },
  },
  // state를 변경하는 메서드
  mutations: {
    // 로그인
    setLoginState(state, payload) {
      state.loginState = payload.loginState;
    },
    // 로그아웃
    logout(state) {
      state.loginState = false;
      state.nickname = "";
      state.token = 0;
    },
    // 유저 정보 업데이트
    updateUserInfoState(state, payload) {
      state.coupon = payload.coupon;
      state.nickname = payload.nickname;
      state.token = payload.token;
      state.memberRoles = payload.memberRoles;
    },
    // 토큰 갱신
    updateTokenState(state, payload) {
      state.token = payload;
    },
    // 토큰 감소
    decrementToken(state) {
      if (state.token > 0) {
        state.token -= 1;
      }
    },
    // 토큰 감소
    incrementToken(state) {
      state.token += 1;
    },
    // 쿠폰 감소
    decrementCoupon(state) {
      if (state.coupon > 0) {
        state.coupon -= 1;
      }
    },
    // 유저 권한
    updateMemberRoleState(state, payload) {
      state.memberRoles = payload;
    },
  },
  actions: {
    login({ commit }, payload) {
      commit("setLoginState", payload);
    },
    logout({ commit }) {
      commit("logout");
    },
    updateUserInfo({ commit }, payload) {
      commit("updateUserInfoState", payload);
    },
    // 토큰 갱신
    updateToken({ commit }, payload) {
      commit("updateTokenState", payload);
    },
    // 설정 정보 ㄱ
  },
  plugins: [
    createPersistedState({
      paths: ["loginState", "memberRoles", "token", "coupon", "nickname"],
    }),
  ],
});
