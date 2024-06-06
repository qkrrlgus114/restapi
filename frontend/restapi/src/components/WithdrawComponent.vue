<template>
  <div class="content">
    <div class="input-container" v-if="!social">
      <div class="input">
        <span class="label">비밀번호 :</span>
        <input type="password" placeholder="비밀번호" v-model="password" />
      </div>
      <div class="input">
        <span class="label">비밀번호 확인 :</span>
        <input
          type="password"
          placeholder="비밀번호 확인"
          v-model="passwordConfirm"
        />
      </div>
    </div>
    <div class="withdraw-btn">
      <button @click="checkInput">탈퇴하기</button>
    </div>
    <Modal
      :isVisible="showModal"
      @confirm="handleConfirm"
      @cancel="handleCancel"
    >
      <template #header> 정말로 탈퇴하시겠습니까? </template>
      <template #body>
        (탈퇴하면 해당 이메일로 재가입이 불가능합니다.)
      </template>
      <template #cancel-btn> 취소하기 </template>
      <template #confirm-btn> 탈퇴하기 </template>
    </Modal>
  </div>
</template>

<script setup>
import { useMainStore } from "@/store/store.js";
import { ref, computed } from "vue";
import { apiPatch } from "@/utils/api";
import { useRouter } from "vue-router";
import Modal from "./Modal.vue";

const store = useMainStore();
const router = useRouter();

const password = ref("");
const passwordConfirm = ref("");
const social = computed(() => store.social);
const showModal = ref(false);

// 탈퇴하기
const handleConfirm = () => {
  withdrawAPI();
  showModal.value = false;
};

// 취소하기
const handleCancel = () => {
  showModal.value = false;
};

// 비밀번호 입력 확인 -> 모달 띄우기
const checkInput = () => {
  if (social.value) {
    showModal.value = true;
    return;
  }
  if (password.value.length === 0) {
    alert("비밀번호를 입력해주세요.");
  } else if (passwordConfirm.value.length === 0) {
    alert("비밀번호 확인을 입력해주세요.");
  } else if (password.value != passwordConfirm.value) {
    alert("비밀번호 확인이 일치하지 않습니다.");
  } else {
    showModal.value = true;
  }
  return;
};

// 탈퇴하기 진행
const withdrawAPI = async () => {
  const socialType = social.value ? "KAKAO" : "GENERAL";

  try {
    const data = await apiPatch("/api/members/deactivate", {
      socialType: socialType,
      password: password.value,
    });

    alert(data.message);
    store.logout();
    router.push("/login");
  } catch (error) {}
};
</script>

<style scoped>
.content {
  flex: 8;
  background-color: #ffffff;
  padding: 20px;
  margin: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  border-radius: 10px;
}

.input-container {
  display: flex;
  flex-direction: column;
  margin-top: 50px;
  width: 100%;
  max-width: 500px;
}

.input {
  padding: 15px 0;
  display: flex;
  flex-direction: column;
  width: 100%;
}

.label {
  font-weight: bold;
  margin-bottom: 5px;
}

.input input {
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 5px;
  width: 100%;
  font-size: 1.2rem;
  box-sizing: border-box;
}

.withdraw-btn {
  display: flex;
  justify-content: center;
  width: 100%;
  margin-top: 30px;
}

.withdraw-btn button {
  padding: 10px 20px;
  font-size: 1.2rem;
  background-color: #ff7f7f;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.withdraw-btn button:hover {
  background-color: #ff4f4f;
}

@media (max-width: 768px) {
  .input input {
    font-size: 1rem;
  }

  .withdraw-btn button {
    font-size: 1rem;
  }
}

@media (max-width: 480px) {
  .input input {
    font-size: 0.9rem;
  }

  .withdraw-btn button {
    font-size: 0.9rem;
  }
}
</style>
