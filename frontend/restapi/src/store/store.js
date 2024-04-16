import { defineStore } from "pinia";

export const useMainStore = defineStore(
  "main",
  {
    state: () => ({
      loginState: false,
      nickname: "",
      token: 0,
      coupon: 0,
      memberRoles: [],
      isDailyCouponGenerate: false,
      dailyCouponQuantity: 0,
    }),
    getters: {
      getToken: (state) => state.token,
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
      },
      updateUserInfo(payload) {
        this.coupon = payload.coupon;
        this.nickname = payload.nickname;
        this.token = payload.token;
        this.memberRoles = payload.memberRoles;
      },
      updateToken(payload) {
        this.token = payload;
      },
      decrementToken() {
        if (this.token > 0) {
          this.token -= 1;
        }
      },
      incrementToken() {
        this.token += 1;
      },
      decrementCoupon() {
        if (this.coupon > 0) {
          this.coupon -= 1;
        }
      },
      updateMemberRole(payload) {
        this.memberRoles = payload;
      },
    },
  },
  {
    persist: true,
  }
);
