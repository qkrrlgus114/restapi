<template>
  <div class="content">
    <div class="content-header">
      <div class="content-title">API 공유 게시판</div>
      <button class="sort-by-likes" @click="likeSort(1)">좋아요 순</button>
    </div>
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
    <div class="search-bar">
      <select v-model="searchType">
        <option value="none" disabled>검색타입</option>
        <option value="nickname">닉네임</option>
        <option value="title">제목</option>
        <option value="methodType">메서드</option>
      </select>
      <input
        v-model="searchKey"
        type="text"
        placeholder="검색어를 입력해주세요"
        @keyup.enter="searchSharedPost(1, searchType, searchKey, sortBy)"
      />
      <button @click="searchSharedPost(1, searchType, searchKey, sortBy)">
        검색
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
const searchType = ref(route.query.searchType || "none");
const searchKey = ref(route.query.searchKey || "");
const sortBy = ref(route.query.sortBy || "");

onMounted(() => {
  watch(
    [
      () => route.params.page,
      () => route.query.searchType,
      () => route.query.searchKey,
      () => route.query.sortBy,
    ],
    ([newPage, newSearchType, newSearchKey, newSortBy]) => {
      const page = parseInt(newPage) || 1;
      currentPage.value = page;
      currentPageGroup.value = Math.ceil(page / itemsPerPageGroup);
      searchType.value = newSearchType || "none";
      searchKey.value = newSearchKey || "";
      sortBy.value = newSortBy || "";
      getSharePosts(page, searchType.value, searchKey.value, sortBy.value);
    },
    { immediate: true }
  );
});

// 공유게시판 게시글 가져오기
const getSharePosts = async (
  page,
  searchType = "",
  searchKey = "",
  sortBy = ""
) => {
  try {
    let url = `/api/post/share-api?page=${page}`;
    if (searchType && searchKey) {
      url += `&searchType=${searchType}&searchKey=${searchKey}`;
    } else if (sortBy) {
      url += `&sortBy=${sortBy}`;
    }
    const data = await apiGet(url);

    console.log(data);
    boardItems.value = data.data.apiRecommendPostsResponseDTOS;
    totalPages.value = data.data.totalPages;
    currentPage.value = data.data.currentPage + 1;
  } catch (error) {}
};

// 페이지 변경
const changePage = (page) => {
  if (page >= 1 && page <= totalPages.value) {
    router.push({
      name: "Board",
      params: { page },
      query: {
        searchType: route.query.searchType,
        searchKey: route.query.searchKey,
        sortBy: route.query.sortBy,
      },
    });
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

// 검색 요청 처리
const searchSharedPost = (page, searchType, searchKey) => {
  if (searchType == "none") {
    alert("검색 타입을 선택해주세요.");
    return;
  } else if (searchKey == "") {
    alert("검색어를 입력해주세요.");
    return;
  }
  router.push({
    name: "Board",
    params: { page: 1 },
    query: { searchType: searchType, searchKey: searchKey, sortBy: sortBy },
  });
};

// 좋아요 순 검색
const likeSort = (page) => {
  router.push({
    name: "Board",
    params: { page: 1 },
    query: { sortBy: "like" },
  });
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
  margin-bottom: 100px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.content-header {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  max-width: 1200px;
  position: relative;
}

.content-title {
  font-size: 2rem;
  padding: 3rem 0;
  margin: 0 auto;
}

.sort-by-likes {
  position: absolute;
  right: 0;
  padding: 10px;
  font-size: 1rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  background-color: #f4f4f4;
  cursor: pointer;
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
  flex: 0 0 10%;
}

.board-cell.method_type {
  flex: 0 0 10%;
}

.board-cell.title {
  flex: 1 0 45%;
  text-align: left;
  padding-left: 10px;
}

.board-cell.nickname {
  flex: 0 0 15%;
}

.board-cell.like_count {
  flex: 0 0 10%;
  display: flex;
  flex-direction: column;
  align-items: center;
}

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

.search-bar {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 20px;
}

.search-bar select,
.search-bar input,
.search-bar button {
  margin: 0 5px;
  padding: 10px;
  font-size: 1rem;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.search-bar input {
  flex: 1;
}
</style>
