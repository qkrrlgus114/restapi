<template>
  <div class="content">
    <div class="form-container">
      <div class="form-header">
        <div class="form-type">
          <label for="inquiryCategory">문의 타입</label>
          <select id="inquiryCategory" v-model="inquiryCategory">
            <option disabled value="">문의 타입 선택</option>
            <option value="ACCOUNT">계정 관련 문의</option>
            <option value="TECHNICAL">기술 관련 문의</option>
            <option value="FEEDBACK">피드백 및 제안</option>
            <option value="OTHER">기타 문의 사항</option>
          </select>
        </div>
        <div class="form-title">
          <label for="title">제목</label>
          <input type="text" id="title" placeholder="제목" v-model="title" />
        </div>
      </div>
      <div class="form-body">
        <label for="content">문의 내용</label>
        <textarea
          id="content"
          placeholder="문의 내용"
          v-model="content"
        ></textarea>
      </div>
      <div class="form-checkbox">
        <input type="checkbox" id="emailCheck" v-model="emailSendCheck" />
        <label for="emailCheck">답변이 달리면 이메일로 알림 받기.</label>
      </div>
      <div class="form-footer">
        <button @click="submitForm">제출하기</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";
import { apiPost } from "@/utils/api";

const router = useRouter();
const inquiryCategory = ref("");
const title = ref("");
const content = ref("");
const emailSendCheck = ref(false);

// 내용 체크
const checkContent = () => {
  if (inquiryCategory.value === "") {
    alert("문의 타입을 선택해주세요.");
    return false;
  } else if (title.value === "") {
    alert("제목을 입력해주세요.");
    return false;
  } else if (content.value === "") {
    alert("내용을 입력해주세요.");
    return false;
  }

  return true;
};

const submitForm = async () => {
  if (checkContent()) {
    try {
      const data = await apiPost(`/api/inquiries`, {
        title: title.value,
        content: content.value,
        inquiryCategory: inquiryCategory.value,
        emailSendCheck: emailSendCheck.value,
      });

      alert("문의가 등록되었습니다.");
      router.push("/my/inquiry/1");
    } catch (error) {}
  }
};
</script>

<style scoped>
.content {
  flex: 8;
  background-color: rgb(255, 255, 255);
  padding: 20px;
  margin: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.form-container {
  width: 100%;
  max-width: 600px;
  background-color: #f9f9f9;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

label {
  padding: 10px 0px;
}

.form-header,
.form-body,
.form-footer {
  margin-bottom: 20px;
}

.form-header {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.form-type,
.form-title {
  width: 100%;
}

.form-type select,
.form-title input,
.form-body textarea {
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 5px;
  width: 100%;
}

.form-body label {
  display: block;
  margin-bottom: 10px;
}

.form-body textarea {
  height: 150px;
  resize: none;
}

.form-footer {
  text-align: right;
}

.form-footer button {
  padding: 0.5rem 1rem;
  margin-left: 10px;
  background-color: #ffffff;
  color: rgb(0, 0, 0);
  border: 1px solid rgb(109, 109, 109);
  border-radius: 0.375rem;
  transition: background-color 0.2s, border-color 0.2s, color 0.2s;
  text-decoration: none;
}

.form-checkbox {
  display: flex;
  align-items: center;
}

.form-checkbox input {
  margin-right: 10px;
}

.form-footer button:hover {
  background-color: #eeeeee;
}
</style>
