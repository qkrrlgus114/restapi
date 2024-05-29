<template>
  <div class="content">
    <div class="content-header">문의 상세 정보</div>
    <div class="content-body">
      <div class="inquiry">
        <div class="inquiry-header">
          <div class="inquiry-category">
            <strong>카테고리 : </strong>
            {{ inquiry.inquiryCategory }}
          </div>
          <div class="inquiry-title">
            <strong>제목 : </strong>{{ inquiry.inquiryTitle }}
          </div>
          <div class="inquiry-create_date">
            <strong>작성일 : </strong
            >{{ formatDate(inquiry.inquiryCreateDate) }}
          </div>
          <div class="inquiry-email">
            <strong>작성자 : </strong>{{ inquiry.email }}
          </div>
        </div>
        <div class="inquiry-body">
          <div class="inquiry-content">{{ inquiry.inquiryContent }}</div>
        </div>
      </div>
      <div class="answer">
        <div v-if="inquiry.answered">
          <div class="answer-content">{{ inquiry.answeredContent }}</div>
          <div class="answer-create_date">
            <strong>답변일 : </strong
            >{{ formatDate(inquiry.answeredCreateDate) }}
          </div>
        </div>
        <div v-else>답변이 없습니다.</div>
      </div>
      <div class="write-form">
        <router-link
          v-if="isAdmin"
          class="custom-btn"
          :to="{
            name: 'AnswerForm',
            params: {
              id: inquiry.id,
            },
            query: {
              inquiryContent: inquiry.inquiryContent,
            },
          }"
          >답변하기</router-link
        >
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useMainStore } from "@/store/store.js";
import { apiGet } from "@/utils/api";
import { useRoute, useRouter } from "vue-router";

const route = useRoute();
const store = useMainStore();

const inquiry = ref({
  id: 0,
  inquiryTitle: "",
  inquiryCategory: "",
  inquiryCreateDate: "",
  inquiryContent: "",
  answered: false,
  answeredContent: "",
  answeredCreateDate: "",
});
const isAdmin = ref(false);

onMounted(() => {
  getInquiryInfo(route.params.id);

  isAdmin.value = store.getAdminRole;
});

// 상세 문의 내역 가져오기
const getInquiryInfo = async (inquiryId) => {
  try {
    const data = await apiGet(`api/inquiries/${inquiryId}`);
    inquiry.value = data.data;
  } catch (error) {}
};

// 날짜 형식 변환 함수
const formatDate = (dateString) => {
  const date = new Date(dateString);

  // 날짜 부분
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const day = String(date.getDate()).padStart(2, "0");

  // 시간 부분
  const hours = String(date.getHours()).padStart(2, "0");
  const minutes = String(date.getMinutes()).padStart(2, "0");

  return `${year}-${month}-${day} ${hours}:${minutes}`;
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

.content-header {
  font-size: 2rem;
  padding: 3rem 0;
}

.content-body {
  width: 80%;
  font-size: 1.2rem;
}

.inquiry {
  border: 1px solid rgb(240, 237, 255);
  border-radius: 5px;
  padding: 15px;
  background-color: #f9f9f9;
}

.inquiry-header > div {
  padding: 10px 0;
  border-bottom: 1px solid #ddd;
}

.inquiry-body {
  padding: 10px 0;
}

.answer {
  padding: 10px;
  margin-top: 50px;
  border: 1px solid rgb(240, 237, 255);
  background-color: #f9f9f9;
  border-radius: 5px;
}

.answer-content {
  margin-bottom: 100px;
}

.write-form {
  display: flex;
  justify-content: flex-end;
  width: 100%;
  margin-right: 20%;
  margin-top: 20px;
}

.custom-btn {
  padding: 0.5rem 1rem;
  margin-left: 10px;
  background-color: #ffffff;
  color: rgb(0, 0, 0);
  border: 1px solid rgb(109, 109, 109);
  border-radius: 0.375rem;
  transition: background-color 0.2s, border-color 0.2s, color 0.2s;
  text-decoration: none;
}

.custom-btn:hover {
  background-color: #e7e7e7;
}
</style>
