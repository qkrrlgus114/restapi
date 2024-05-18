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
        (0~100개 설정 가능합니다.)</label
      >
      <input
        id="quantity"
        type="number"
        min="0"
        max="100"
        v-model="dailyCouponQuantity"
      />
    </div>
    <div class="buttons">
      <button class="btn-immediately" @click="showModal = true">
        즉시 발급
      </button>
      <Modal
        :isVisible="showModal"
        @confirm="handleConfirm"
        @cancel="handleCancel"
      >
        <template #header> 쿠폰의 개수를 즉시 변경하시겠습니까? </template>
        <template #body>
          현재 설정하신 쿠폰의 개수로<br />
          즉시 발급 (개수 변경)을 진행합니다.
        </template>
        <template #cancel-btn> 취소하기 </template>
        <template #confirm-btn> 변경하기 </template>
      </Modal>
      <button class="btn" @click="resetSettings">초기화</button>
      <button class="btn" @click="applySettings">적용</button>
    </div>
  </div>
</template>

<script setup>
import { useMainStore } from "@/store/store.js";
import { onMounted, ref } from "vue";
import { useRouter } from "vue-router";
import { apiPost, apiGet, apiPatch } from "@/utils/api";
import Modal from "../components/Modal.vue";

const store = useMainStore();
const router = useRouter();

const dailyCouponQuantity = ref(0);
const isDailyCouponGenerate = ref(false);
const originDailyCouponQuantity = ref(0);
const originIsDailyCouponGenerate = ref(false);
const showModal = ref(false);

const handleConfirm = () => {
  immediatelyCoupon();
  showModal.value = false;
};

const handleCancel = () => {
  showModal.value = false;
};

onMounted(() => {
  getCouponSetting();
});

// 쿠폰 설정 가져오기
const getCouponSetting = async () => {
  try {
    const data = await apiGet("/api/admin/coupons/setting");
    dailyCouponQuantity.value = data.data.dailyCouponQuantity;
    isDailyCouponGenerate.value = data.data.isDailyCouponGenerate;
    originDailyCouponQuantity.value = dailyCouponQuantity.value;
    originIsDailyCouponGenerate.value = isDailyCouponGenerate.value;
  } catch (error) {}
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

    const data = await apiPatch("/api/admin/coupons/setting", {
      isDailyCouponGenerate: isDailyCouponGenerate.value,
      dailyCouponQuantity: dailyCouponQuantity.value,
    });
    alert(data.message);
    originDailyCouponQuantity.value = dailyCouponQuantity.value;
    originIsDailyCouponGenerate.value = isDailyCouponGenerate.value;
  } catch (error) {}
};

// 쿠폰 상태 체크
const checkCouponData = () => {
  if (dailyCouponQuantity.value < 0 || dailyCouponQuantity.value > 100) {
    alert("쿠폰은 0 ~ 100개 까지 설정이 가능합니다.");
    return false;
  }
  return true;
};

// 쿠폰 즉시 발급
const immediatelyCoupon = async () => {
  try {
    // 쿠폰 개수 사전 체크
    if (!checkCouponData()) return;

    const data = await apiPost("/api/admin/coupons", {
      dailyCouponQuantity: dailyCouponQuantity.value,
    });
    alert(data.message);
  } catch (error) {}
};
</script>

<style scoped>
.container {
  max-width: 300px;
  margin: 150px auto;
  padding: 50px;
  background-color: #fff;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
}

.from-title {
  font-size: 20px;
  font-weight: 1000;
  display: flex;
  justify-content: center;
  margin-bottom: 30px;
}

.form-control {
  margin-bottom: 20px;
}

label {
  display: block;
  font-size: 16px;
  color: #333;
  margin-bottom: 10px;
}

select {
  width: 50%;
}

select,
input {
  padding: 10px;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  font-size: 16px;
}

.buttons {
  display: flex;
  justify-content: space-between;
  margin-top: 50px;
}

.btn {
  background-color: #eeeeee;
}
.btn,
.btn-immediately {
  padding: 10px 20px;
  font-size: 16px;
  font-weight: 1000;
  cursor: pointer;
  border-radius: 4px;
  color: #5c5c5c;
  outline: none;
  border: none;
  transition: background-color 0.2s, border-color 0.2s;
}

.btn:hover {
  background-color: #c7c7c7;
  border-color: #c7c7c7;
}

.btn:active {
  background-color: #c7c7c7;
  border-color: #c7c7c7;
}

.btn-immediately:hover {
  background-color: #ffcdb8;
  border-color: #ffcdb8;
}
.btn-immediately {
  background-color: #ffe8de;
  color: #db0700;
}
</style>
