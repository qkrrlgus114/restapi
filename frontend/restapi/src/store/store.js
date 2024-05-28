import { defineStore } from "pinia";

export const useMainStore = defineStore("main", {
  state: () => ({
    loginState: false,
    nickname: "",
    token: 0,
    coupon: 0,
    memberRoles: [],
    social: false,
    isDailyCouponGenerate: false,
    dailyCouponQuantity: 0,
  }),
  getters: {
    getToken: (state) => state.token,
    getLoginState: (state) => state.loginState,
    getAdminRole: (state) => state.memberRoles.includes("ADMIN"),
  },
  actions: {
    setLoginState(payload) {
      this.loginState = payload.loginState;
    },
    logout() {
      this.loginState = false;
      this.nickname = "";
      this.token = 0;
      this.coupon = 0;
      this.memberRoles = [];
      this.social = false;
      this.isDailyCouponGenerate = false;
      this.dailyCouponQuantity = 0;
    },
    // 유저 정보 갱신
    updateUserInfo(payload) {
      this.nickname = payload.nickname;
      this.token = payload.token;
      this.memberRoles = payload.memberRoles;
      this.social = payload.social;
    },
    updateCouponInfo(payload) {
      this.coupon = payload;
    },
    // 토큰 갱신
    updateToken(payload) {
      this.token = payload;
    },
    // 토큰 감소(사용)
    decrementToken() {
      if (this.token > 0) {
        this.token -= 1;
      }
    },
    // 토큰 증가(획득)
    incrementToken() {
      this.token += 1;
    },
    // 쿠폰 감소(사용)
    decrementCoupon() {
      if (this.coupon > 0) {
        this.coupon -= 1;
      }
    },
    // 멤버 권한 갱신
    updateMemberRole(payload) {
      this.memberRoles = payload;
    },
  },
  persist: true, // persist 옵션을 스토어 정의 객체 내에 포함
});
