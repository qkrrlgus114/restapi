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
      <button @click="withdraw">탈퇴하기</button>
    </div>
  </div>
</template>

<script setup>
import { useMainStore } from "@/store/store.js";
import { ref, computed } from "vue";
import { apiPost } from "@/utils/api";
import { useRouter } from "vue-router";

const store = useMainStore();
const router = useRouter();

const password = ref("");
const passwordConfirm = ref("");
const social = computed(() => store.social);

// 비밀번호 입력 확인
const checkInput = () => {
  if (social.value) {
    return true;
  }
  if (password.value.length === 0) {
    alert("비밀번호를 입력해주세요.");
    return false;
  }
  if (passwordConfirm.value.length === 0) {
    alert("비밀번호 확인을 입력해주세요.");
    return false;
  }
  if (password.value != passwordConfirm.value) {
    alert("비밀번호 확인이 일치하지 않습니다.");
    return false;
  }
  return true;
};

// 탈퇴하기 진행
const withdraw = async () => {
  if (checkInput()) {
    var isPassword = await passwordConfirmAPI();
    if (social.value) isPassword = true;
    if (isPassword) {
      alert("탈퇴 진행");
    } else {
      alert("비밀번호가 일치하지 않습니다.");
    }
  }
};

// 비밀번호 확인
const passwordConfirmAPI = async () => {
  try {
    const data = await apiPost("api/members/password-check", {
      password: password.value,
    });

    return data.data;
  } catch (error) {}
};
</script>

<style scoped>
.content {
  flex: 8;
  background-color: rgb(223, 223, 223);
  padding: 10px;
  margin: 20px;
  display: flex;
  flex-direction: column;
}

.input-container {
  display: flex;
  flex-direction: column;
  margin-top: 100px;
}

.input {
  padding: 20px 0;

  display: flex;
  width: 80%;
  justify-content: flex-start;
  align-items: center;
  font-size: 1.5rem;
  flex-wrap: wrap;
}

.label {
  flex: 1;
  min-width: 100px;
  margin-right: 10px;
  font-weight: bold;
  display: flex;
  justify-content: flex-end;
}

.input input {
  flex: 2;
  min-width: 200px;
  padding: 10px;
  font-size: 1.5rem;
}

.withdraw-btn {
  display: flex;
  justify-content: center;
}

.withdraw-btn button {
  padding: 10px 20px;
  font-size: 1.5rem;
  font-weight: 800;
  margin-top: 150px;
  cursor: pointer;
  border-radius: 4px;
  color: #5c5c5c;
  outline: none;
  border: none;
  background-color: #ffe8de;
  color: #db0700;
}

.withdraw-btn button:hover {
  background-color: #ffcdb8;
  border-color: #ffcdb8;
}

@media (max-width: 768px) {
  .input {
    font-size: 1.2rem;
  }

  .label {
    justify-content: flex-start;
    margin-right: 5px;
  }

  .input input {
    font-size: 1.2rem;
  }

  .withdraw-btn button {
    font-size: 1.2rem;
  }
}

@media (max-width: 480px) {
  .input {
    font-size: 1rem;
    padding: 10px 0;
  }

  .label {
    width: 100%;
    margin-bottom: 5px;
  }

  .input input {
    width: 100%;
    font-size: 1rem;
  }

  .withdraw-btn button {
    font-size: 1rem;
  }
}
</style>
