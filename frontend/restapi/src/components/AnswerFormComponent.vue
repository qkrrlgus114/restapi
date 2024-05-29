<template>
  <div class="content">
    <div class="form-container">
      <div class="form-header">
        <div class="form-title">
          <label for="inquiryContent">문의 내용</label>
          <input
            type="text"
            id="inquiryContent"
            v-model="inquiryContent"
            readonly
          />
        </div>
      </div>
      <div class="form-body">
        <label for="answerContent">답변 내용</label>
        <textarea
          id="answerContent"
          placeholder="답변 내용"
          v-model="answerContent"
        ></textarea>
      </div>
      <div class="form-footer">
        <button @click="submitForm">답변하기</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useRouter, useRoute } from "vue-router";
import { apiPost } from "@/utils/api";

const router = useRouter();
const route = useRoute();

onMounted(() => {
  console.log(inquiryContent.value);
});

const inquiryId = ref(route.params.id);
const inquiryContent = ref(route.query.inquiryContent);
const answerContent = ref("");

// 내용 체크
const checkContent = () => {
  if (answerContent.value === "") {
    alert("답변을 입력해주세요.");
    return false;
  }

  return true;
};

const submitForm = async () => {
  if (checkContent()) {
    try {
      const data = await apiPost(`api/answers`, {
        content: answerContent.value,
        inquiryId: inquiryId.value,
      });

      alert("답변이 등록되었습니다.");
      router.push(`/my/inquiry/view/${inquiryId.value}`);
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

.form-title {
  width: 100%;
}

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

.form-footer button:hover {
  background-color: #eeeeee;
}
</style>
