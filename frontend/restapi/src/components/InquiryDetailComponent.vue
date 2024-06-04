<template>
  <div class="content">
    <div class="content-header">문의 상세 정보</div>
    <div class="content-body">
      <div class="inquiry">
        <table class="inquiry-table">
          <tr>
            <th>카테고리</th>
            <td>{{ translateCategory(inquiry.inquiryCategory) }}</td>
          </tr>
          <tr>
            <th>작성자</th>
            <td>{{ inquiry.email }}</td>
          </tr>
          <tr>
            <th>제목</th>
            <td>{{ inquiry.inquiryTitle }}</td>
          </tr>
          <tr>
            <th>작성일</th>
            <td>{{ formatDate(inquiry.inquiryCreateDate) }}</td>
          </tr>
          <tr>
            <th>내용</th>
            <td>{{ inquiry.inquiryContent }}</td>
          </tr>
        </table>
      </div>
      <div class="answer">
        <div v-if="inquiry.isAnswered">
          <div class="answer-content">{{ inquiry.answeredContent }}</div>
          <div class="answer-create_date">
            <strong>답변일: </strong
            >{{ formatDate(inquiry.answeredCreateDate) }}
          </div>
        </div>
        <div v-else>답변이 없습니다.</div>
      </div>
      <div class="write-form">
        <button
          v-if="isAdmin && !inquiry.answered"
          class="custom-btn"
          @click="setInquiryData(false)"
        >
          답변하기
        </button>
        <button
          v-if="isAdmin && inquiry.answered"
          class="custom-btn"
          @click="setInquiryData(true)"
        >
          수정하기
        </button>
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
const router = useRouter();
const store = useMainStore();

const inquiry = ref({
  id: 0,
  inquiryTitle: "",
  inquiryCategory: "",
  inquiryCreateDate: "",
  inquiryContent: "",
  answeredContent: "",
  answeredCreateDate: "",
  isAnswered: false,
});
const isAdmin = ref(false);

const categoryMap = {
  ACCOUNT: "계정 관련 문의",
  TECHNICAL: "기술 관련 문의",
  FEEDBACK: "피드백 및 제안",
  OTHER: "기타 문의 사항",
};

// 카테고리 변환 함수
const translateCategory = (category) => {
  return categoryMap[category] || category;
};

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

const setInquiryData = (isAnswered) => {
  store.setInquiryData({
    inquiryContent: inquiry.value.inquiryContent,
    answeredContent: inquiry.value.answeredContent,
    isAnswered: isAnswered,
  });
  router.push({
    name: "AnswerForm",
    params: { id: inquiry.value.id },
  });
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
}

.content-header {
  font-size: 2rem;
  padding: 3rem 0;
}

.content-body {
  width: 100%;
  max-width: 800px;
  font-size: 1.2rem;
  background-color: #f9f9f9;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.inquiry {
  margin-bottom: 20px;
}

.inquiry-table {
  width: 100%;
  border-collapse: collapse;
}

.inquiry-table th,
.inquiry-table td {
  border: 1px solid #ddd;
  padding: 8px;
  text-align: left;
}

.inquiry-table th {
  background-color: #f2f2f2;
  width: 20%;
}

.answer {
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 10px;
}

.answer-content {
  margin-bottom: 20px;
}

.write-form {
  display: flex;
  justify-content: flex-end;
  width: 100%;
  margin-top: 20px;
}

.custom-btn {
  padding: 0.5rem 1rem;
  background-color: #ffffff;
  color: #000000;
  border: 1px solid #6d6d6d;
  border-radius: 0.375rem;
  transition: background-color 0.2s, border-color 0.2s, color 0.2s;
  text-decoration: none;
}

.custom-btn:hover {
  background-color: #e7e7e7;
}
</style>
