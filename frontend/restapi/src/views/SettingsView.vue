<template>
  <div class="container">
    <div class="from-title">데일리 쿠폰 발급 설정</div>
    <div class="form-control">
      <label for="status"
        >쿠폰 발급 상태 <br />(매일 00:00시에 쿠폰이 발급됩니다.)</label
      >
      <select id="status" v-model="dailyCouponSetting.isDailyCouponGenerate">
        <option value="true">On</option>
        <option value="false">Off</option>
      </select>
    </div>
    <div class="form-control">
      <label for="quantity"
        >발급 쿠폰 개수<br />
        (1~100개 설정 가능합니다.)</label
      >
      <input
        id="quantity"
        type="number"
        v-model="dailyCouponSetting.dailyCouponQuantity"
        min="0"
      />
    </div>
    <div class="buttons">
      <button @click="resetSettings">초기화</button>
      <button @click="applySettings">적용</button>
    </div>
  </div>
</template>

<script>
export default {
  name: "AdminView",
  data() {
    return {
      dailyCouponSetting: {
        isDailyCouponGenerate: false,
        dailyCouponQuantity: 0,
      },
      originalSetting: {
        isDailyCouponGenerate: false,
        dailyCouponQuantity: 0,
      },
    };
  },
  mounted() {
    this.getCouponSetting();
  },
  methods: {
    // 쿠폰 설정 가져오기
    getCouponSetting() {
      this.$axios
        .get(`${this.$apiBaseUrl}/api/admin/coupons/setting`, {
          withCredentials: true,
        })
        .then((response) => {
          this.dailyCouponSetting = response.data.data;
          this.originalSetting = response.data.data;
        })
        .catch((error) => {
          console.error("토큰 개수 갱신 중 오류 발생:", error);
          alert(error.message);
        });
    },
    // 쿠폰 설정 적용하기
    applySettings() {
      if (this.dailyCouponSetting.dailyCouponQuantity === 0) {
        alert("데일리 쿠폰은 최소 1개부터 가능합니다.");
      } else if (this.dailyCouponSetting.dailyCouponQuantity > 100) {
        alert("데일리 쿠폰은 최대 100개까지 가능합니다.");
      } else {
        this.$axios
          .patch(
            `${this.$apiBaseUrl}/api/admin/coupons/setting`,
            this.dailyCouponSetting,
            {
              withCredentials: true,
            }
          )
          .then((response) => {
            alert(response.data.message);
            this.$router.go();
          })
          .catch((error) => {
            console.error("토큰 개수 갱신 중 오류 발생:", error);
          });
      }
    },
    resetSettings() {
      this.dailyCouponSetting = { ...this.originalSetting };
    },
  },
};
</script>

<style scoped>
.container {
  max-width: 300px; /* 폼의 최대 너비 설정 */
  margin: 150px auto; /* 상하 여백과 가운데 정렬 */
  padding: 50px;
  background-color: #fff; /* 배경색 */
  border: 1px solid #e0e0e0; /* 경계선 */
  border-radius: 4px; /* 경계 둥글기 */
}

.from-title {
  font-size: 20px;
  font-weight: 1000;
  display: flex;
  justify-content: center;
  margin-bottom: 30px;
}

.form-control {
  margin-bottom: 20px; /* 하단 여백 */
}

label {
  display: block;
  font-size: 16px; /* 글꼴 크기 조정 */
  color: #333; /* 글꼴색 */
  margin-bottom: 10px; /* 라벨 하단 여백 */
}

select {
  width: 50%;
}

select,
input {
  padding: 10px;
  border: 1px solid #e0e0e0; /* 경계선 스타일 */
  border-radius: 4px; /* 입력 필드 경계 둥글기 */
  font-size: 16px; /* 글꼴 크기 조정 */
}

.buttons {
  display: flex;
  justify-content: space-between; /* 버튼 사이의 간격을 균등하게 분배 */
  margin-top: 20px; /* 버튼 상단 여백 */
}

button {
  padding: 10px 20px; /* 패딩 */
  font-size: 16px; /* 글꼴 크기 */
  cursor: pointer; /* 포인터 모양 변경 */
  border: 1px solid #3498db; /* 버튼 경계선 */
  border-radius: 4px; /* 버튼 경계 둥글기 */
  background-color: #3498db; /* 버튼 배경색 */
  color: white; /* 글꼴색 */
  outline: none; /* 윤곽선 제거 */
  transition: background-color 0.2s, border-color 0.2s; /* 색상 전환 효과 */
}

button:hover {
  background-color: #2980b9; /* 호버 시 배경색 변경 */
  border-color: #2980b9; /* 호버 시 경계선 색상 변경 */
}

button:active {
  background-color: #2471a3; /* 클릭 시 배경색 */
  border-color: #2471a3; /* 클릭 시 경계선 색상 */
}

/* 선택된 버튼 스타일을 다르게 하고 싶다면 추가 클래스를 사용 */
button.apply {
  background-color: #28a745; /* 적용 버튼의 배경색 */
  border-color: #28a745; /* 적용 버튼의 경계선 색상 */
}

button.apply:hover {
  background-color: #218838; /* 적용 버튼 호버 시 배경색 */
  border-color: #218838; /* 적용 버튼 호버 시 경계선 색상 */
}

button.apply:active {
  background-color: #1e7e34; /* 적용 버튼 클릭 시 배경색 */
  border-color: #1e7e34; /* 적용 버튼 클릭 시 경계선 색상 */
}
</style>
