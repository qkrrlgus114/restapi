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
      // 유저 정보 갱신
      updateUserInfo(payload) {
        this.coupon = payload.coupon;
        this.nickname = payload.nickname;
        this.token = payload.token;
        this.memberRoles = payload.memberRoles;
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
  },
  {
    persist: true,
  }
);
