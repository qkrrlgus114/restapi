<template>
  <div class="content">
    <div class="content-title">API 요청 기록</div>
    <div class="content-board">
      <div class="board-header">
        <div class="board-cell email">이메일</div>
        <div class="board-cell date">요청 날짜</div>
        <div class="board-cell method">메소드 타입</div>
        <div class="board-cell request">요청 내용</div>
        <div class="board-cell response">응답 내용</div>
      </div>
      <div v-for="item in boardItems" :key="item.id" class="board-item">
        <div class="board-cell email">{{ item.email }}</div>
        <div class="board-cell date">{{ formatDate(item.requestDate) }}</div>
        <div class="board-cell method">{{ item.methodType }}</div>
        <div class="board-cell request">{{ item.requestContent }}</div>
        <div class="board-cell response">{{ item.responseContent }}</div>
      </div>
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

onMounted(() => {
  watch(
    () => route.params.page,
    (newPage) => {
      const page = parseInt(newPage) || 1;
      currentPage.value = page;
      currentPageGroup.value = Math.ceil(page / itemsPerPageGroup);
      getRequestHistory(page);
    },
    { immediate: true }
  );
});

// 요청 기록 가져오기
const getRequestHistory = async (page) => {
  try {
    const data = await apiGet(`api/admin/requests?page=${page}`);
    boardItems.value = data.data.apiRequestHistoryResponseDTOS;
    totalPages.value = data.data.totalPages;
    currentPage.value = data.data.currentPage + 1;
  } catch (error) {}
};

// 페이지 변경
const changePage = (page) => {
  if (page >= 1 && page <= totalPages.value) {
    router.push({ name: "ApiRequestLog", params: { page } });
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

.email {
  flex: 2;
}

.date {
  flex: 2;
}

.method {
  flex: 1;
}

.request {
  flex: 3;
}

.response {
  flex: 3;
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
</style>
