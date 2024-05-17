<template>
  <div class="container">
    <div class="from-title">데일리 쿠폰 발급 설정</div>
    <div class="form-control">
      <label for="status"
        >쿠폰 발급 상태 <br />(매일 00:00시에 쿠폰이 발급됩니다.)</label
      >
      <select id="status" v-model="isDailyCouponGenerate">
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
        min="1"
        max="100"
        v-model="dailyCouponQuantity"
      />
    </div>
    <div class="buttons">
      <button @click="resetSettings">초기화</button>
      <button>즉시 발급</button>
      <button @click="applySettings">적용</button>
    </div>
  </div>
</template>

<script setup>
import { useMainStore } from "@/store/store.js";
import { onMounted, ref } from "vue";
import { useRouter } from "vue-router";
import { useGlobalProperties } from "@/composables/useGlobalProperties";

const { $axios, $apiBaseUrl } = useGlobalProperties();
const store = useMainStore();
const router = useRouter();

const dailyCouponQuantity = ref(0);
const isDailyCouponGenerate = ref(false);
const originDailyCouponQuantity = ref(0);
const originIsDailyCouponGenerate = ref(false);

onMounted(() => {
  getCouponSetting();
});

// 쿠폰 설정 가져오기
const getCouponSetting = async () => {
  try {
    const response = await $axios.get(
      `${$apiBaseUrl}/api/admin/coupons/setting`,
      {
        withCredentials: true,
      }
    );
    dailyCouponQuantity.value = response.data.data.dailyCouponQuantity;
    isDailyCouponGenerate.value = response.data.data.isDailyCouponGenerate;
    originDailyCouponQuantity.value = dailyCouponQuantity.value;
    originIsDailyCouponGenerate.value = isDailyCouponGenerate.value;
  } catch (error) {
    alert(error.response.data.message);
  }
};

// 초기화 로직
const resetSettings = () => {
  dailyCouponQuantity.value = originDailyCouponQuantity.value;
  isDailyCouponGenerate.value = originIsDailyCouponGenerate.value;
};

// 쿠폰 설정 변경
const applySettings = async () => {
  try {
    // 쿠폰 개수 사전 체크
    if (!checkCouponData()) return;

    const response = await $axios.patch(
      `${$apiBaseUrl}/api/admin/coupons/setting`,
      {
        isDailyCouponGenerate: isDailyCouponGenerate.value,
        dailyCouponQuantity: dailyCouponQuantity.value,
      },
      {
        withCredentials: true,
      }
    );
    alert(response.data.message);
    originDailyCouponQuantity.value = dailyCouponQuantity.value;
    originIsDailyCouponGenerate.value = isDailyCouponGenerate.value;
  } catch (error) {
    alert(error.response.data.message);
  }
};

// 쿠폰 상태 체크
const checkCouponData = () => {
  if (dailyCouponQuantity.value <= 0 || dailyCouponQuantity.value > 100) {
    alert("쿠폰은 1~100개 설정이 가능합니다.");
    return false;
  }
  return true;
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
