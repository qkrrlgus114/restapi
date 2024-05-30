<template>
  <div class="content">
    <div class="content-title">1:1 문의 게시판</div>
    <div class="content-board">
      <div class="board-header">
        <div class="board-cell category">카테고리</div>
        <div class="board-cell title">제목</div>
        <div class="board-cell date">문의 날짜</div>
        <div class="board-cell answered">답변 등록 여부</div>
      </div>
      <router-link
        v-for="item in boardItems"
        :key="item.id"
        class="board-item"
        :to="{ name: 'InquiryDetail', params: { id: item.id } }"
      >
        <div class="board-cell category">
          {{ translateCategory(item.inquiryCategory) }}
        </div>
        <div class="board-cell title">{{ item.title }}</div>
        <div class="board-cell date">{{ formatDate(item.createDate) }}</div>
        <div
          class="board-cell answered"
          :class="{
            'answered-true': item.answered,
            'answered-false': !item.answered,
          }"
        >
          {{ item.answered ? "등록됨" : "등록안됨" }}
        </div>
      </router-link>
    </div>
    <div class="pagination">
      <button
        @click="changePageGroup('prev')"
        :disabled="currentPageGroup === 1"
      >
        이전
      </button>
      <button
        v-for="page in pageNumbers"
        :key="page"
        @click="changePage(page)"
        :disabled="page === currentPage"
        :class="{ active: page === currentPage }"
      >
        {{ page }}
      </button>
      <button
        @click="changePageGroup('next')"
        :disabled="currentPageGroup === totalPageGroups"
      >
        다음
      </button>
    </div>
    <div class="write-form">
      <router-link class="custom-btn" :to="{ name: 'InquiryForm' }"
        >작성하기</router-link
      >
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, computed } from "vue";
import { apiGet } from "@/utils/api";
import { useRoute, useRouter } from "vue-router";

const route = useRoute();
const router = useRouter();

const boardItems = ref([]);
const currentPage = ref(1); // 현재 페이지 번호
const totalPages = ref(0); // 총 페이지 수
const currentPageGroup = ref(1); // 현재 페이지 그룹
const itemsPerPageGroup = 5; // 한 번에 표시할 페이지 번호 수

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
  watch(
    () => route.params.page,
    (newPage) => {
      const page = parseInt(newPage) || 1;
      currentPage.value = page;
      currentPageGroup.value = Math.ceil(page / itemsPerPageGroup);
      getInquiries(page);
    },
    { immediate: true }
  );
});

// 문의 내역 가져오기
const getInquiries = async (page) => {
  try {
    const data = await apiGet(`api/inquiries?page=${page}`);
    boardItems.value = data.data.inquiryResponseDTOS;
    totalPages.value = data.data.totalPages;
    currentPage.value = data.data.currentPage + 1;
  } catch (error) {}
};

// 페이지 변경
const changePage = (page) => {
  if (page >= 1 && page <= totalPages.value) {
    router.push({ name: "InquiryBoard", params: { page } });
  }
};

// 페이지 그룹 변경
const changePageGroup = (direction) => {
  if (direction === "prev" && currentPageGroup.value > 1) {
    currentPageGroup.value--;
    changePage((currentPageGroup.value - 1) * itemsPerPageGroup + 1);
  } else if (
    direction === "next" &&
    currentPageGroup.value < totalPageGroups.value
  ) {
    currentPageGroup.value++;
    changePage((currentPageGroup.value - 1) * itemsPerPageGroup + 1);
  }
};

const pageNumbers = computed(() => {
  const startPage = (currentPageGroup.value - 1) * itemsPerPageGroup + 1;
  const endPage = Math.min(startPage + itemsPerPageGroup - 1, totalPages.value);
  const pages = [];
  for (let i = startPage; i <= endPage; i++) {
    pages.push(i);
  }
  return pages;
});

const totalPageGroups = computed(() => {
  return Math.ceil(totalPages.value / itemsPerPageGroup);
});

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
  padding: 10px;
  margin: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.content-title {
  font-size: 2rem;
  padding: 3rem 0;
}

.content-board {
  width: 100%;
  max-width: 1200px;
  border-collapse: collapse;
}

.board-header,
.board-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #ddd;
  padding: 10px;
  background-color: #fff;
  text-decoration: none;
  color: black;
}

.board-header {
  background-color: #f4f4f4;
  font-weight: bold;
}

.board-cell {
  padding: 10px;
  text-align: center;
}

.board-item:hover {
  background-color: #e6e6e6;
  cursor: pointer;
}

.category {
  flex: 1;
}

.title {
  flex: 4;
  text-align: left;
  padding-left: 10px;
}

.date {
  flex: 2;
}

.answered {
  flex: 1;
}

.answered-true {
  color: green;
}

.answered-false {
  color: red;
}

.pagination {
  margin-top: 20px;
  text-align: center;
}

.pagination button {
  margin: 0 5px;
  padding: 10px;
  background-color: transparent;
  color: rgb(172, 172, 172);
  cursor: pointer;
  border: none;
}

.pagination button:disabled {
  cursor: not-allowed;
}

.pagination button.active {
  color: black;
  background-color: rgb(209, 209, 209);
}

.write-form {
  display: flex;
  justify-content: flex-end;
  width: 100%;
  margin-right: 20%;
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

button:hover {
  background-color: #e7e7e7;
  color: black;
}
</style>
