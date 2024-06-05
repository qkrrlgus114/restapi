<template>
  <div class="content">
    <div class="content-title">API 공유 게시판</div>
    <div class="content-board">
      <div class="board-header">
        <div class="board-cell no">No</div>
        <div class="board-cell method_type">메서드</div>
        <div class="board-cell title">제목</div>
        <div class="board-cell nickname">닉네임</div>
        <div class="board-cell like_count">좋아요 수</div>
        <div class="board-cell view_count">조회 수</div>
      </div>
      <router-link
        v-for="item in boardItems"
        :key="item.postId"
        class="board-item"
        :to="{ name: 'BoardDetail', params: { id: item.postId } }"
      >
        <div class="board-cell no">
          {{ item.postId }}
        </div>
        <div class="board-cell method_type">{{ item.methodType }}</div>
        <div class="board-cell title">{{ item.title }}</div>
        <div class="board-cell nickname">{{ item.nickname }}</div>
        <div class="board-cell like_count">{{ item.likeCount }}</div>
        <div class="board-cell view_count">{{ item.viewCount }}</div>
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
  </div>
</template>

<script setup>
import { ref, onMounted, watch, computed } from "vue";
import { apiGet } from "@/utils/api";
import { useRoute, useRouter } from "vue-router";

const route = useRoute();
const router = useRouter();

const boardItems = ref([]);
const currentPage = ref(1);
const totalPages = ref(0);
const currentPageGroup = ref(1);
const itemsPerPageGroup = 5;

onMounted(() => {
  watch(
    () => route.params.page,
    (newPage) => {
      const page = parseInt(newPage) || 1;
      currentPage.value = page;
      currentPageGroup.value = Math.ceil(page / itemsPerPageGroup);
      getSharePosts(page);
    },
    { immediate: true }
  );
});

// 문의 내역 가져오기
const getSharePosts = async (page) => {
  try {
    const data = await apiGet(`api/post/shard-api?page=${page}`);

    console.log(data);
    boardItems.value = data.data.apiRecommendPostsResponseDTOS;
    totalPages.value = data.data.totalPages;
    currentPage.value = data.data.currentPage + 1;
  } catch (error) {}
};

// 페이지 변경
const changePage = (page) => {
  if (page >= 1 && page <= totalPages.value) {
    router.push({ name: "Board", params: { page } });
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

.board-cell.no {
  flex: 0 0 5%;
}

.board-cell.method_type {
  flex: 0 0 10%;
}

.board-cell.title {
  flex: 1 0 50%;
  text-align: left;
  padding-left: 10px;
}

.board-cell.nickname {
  flex: 0 0 15%;
}

.board-cell.like_count,
.board-cell.view_count {
  flex: 0 0 10%;
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
button:hover {
  background-color: #e7e7e7;
  color: black;
}
</style>
