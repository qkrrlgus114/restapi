<template class="container">
  <div v-if="isLoading" class="overlay">요청 기록 불러오는중...</div>

  <table>
    <thead>
      <tr>
        <th>회원 ID</th>
        <th>이메일</th>
        <th>메소드 유형</th>
        <th>요청 날짜</th>
        <th>요청 내용</th>
        <th>응답 내용</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="log in requestLogs" :key="log.id">
        <td>{{ log.memberId }}</td>
        <td>{{ log.email }}</td>
        <td>{{ log.methodType }}</td>
        <td>{{ log.requestDate }}</td>
        <td>{{ log.requestContent }}</td>
        <td>{{ log.responseContent }}</td>
      </tr>
    </tbody>
  </table>
  <div class="search-container">
    <div class="form-group">
      <select v-model="searchType" id="searchType">
        <option disabled value="">검색 조건</option>
        <option value="id">ID</option>
        <option value="email">이메일</option>
      </select>
    </div>
    <input type="text" v-model="searchKey" placeholder="검색어 입력" />
    <button @click="getRequestLogs(0)" :disabled="!canSearch">검색</button>
  </div>
  <nav aria-label="Page navigation">
    <ul class="pagination">
      <li class="page-item" :class="{ disabled: currentPage <= 1 }">
        <button
          class="page-link"
          @click="changePage(Math.floor((currentPage - 2) / 5) * 5)"
        >
          이전
        </button>
      </li>
      <li
        class="page-item"
        v-for="page in paginatedPages"
        :key="page"
        :class="{ active: currentPage === page }"
      >
        <button class="page-link" @click="changePage(page)">{{ page }}</button>
      </li>
      <li class="page-item" :class="{ disabled: currentPage >= totalPages }">
        <button
          class="page-link"
          @click="changePage(Math.ceil(currentPage / 5) * 5 + 1)"
        >
          다음
        </button>
      </li>
    </ul>
  </nav>
</template>

<script>
export default {
  name: "RequestLogView",
  computed: {
    paginatedPages() {
      const startPage = Math.floor((this.currentPage - 1) / 5) * 5 + 1;
      const pages = Array.from({ length: 5 }, (v, k) => startPage + k).filter(
        (page) => page <= this.totalPages
      );
      return pages;
    },
    canSearch() {
      return this.searchType && this.searchKey.trim();
    },
  },
  data() {
    return {
      requestLogs: [],
      isLoading: false,
      currentPage: 0,
      totalPages: 0, // 총 페이지 수
      totalElements: 0, // 총 요소 수
      searchKey: "",
      searchType: "",
    };
  },
  watch: {
    "$route.params.page": function (newVal, oldVal) {
      this.getRequestLogs(newVal);
    },
  },
  mounted() {
    this.getRequestLogs(this.currentPage);
  },
  methods: {
    getRequestLogs(page) {
      this.isLoading = true;
      const params = {
        page: page,
      };
      // 검색 조건이 있는 경우 쿼리 파라미터에 추가
      if (this.canSearch) {
        params.searchType = this.searchType;
        params.searchKey = this.searchKey;
      }
      this.$axios
        .get(`${this.$apiBaseUrl}/api/admin/requests`, {
          params: params,
          withCredentials: true,
        })
        .then((response) => {
          this.requestLogs = response.data.data.content;
          this.totalPages = response.data.data.totalPages; // 총 페이지 수 업데이트
          this.totalElements = response.data.data.totalElements;
          this.currentPage = Number(this.$route.params.page) || 1;
        })
        .catch((error) => {
          console.error("요청기록 조회 중 오류 발생:", error);
        })
        .finally(() => {
          this.isLoading = false;
        });
    },
    getNextPage() {
      if (this.currentPage < this.totalPages - 1) {
        this.getRequestLogs(this.currentPage + 1);
      }
    },
    getPrePage() {
      if (this.currentPage > 0) {
        this.getRequestLogs(this.currentPage - 1);
      }
    },
    changePage(page) {
      this.$router
        .push({ name: "RequestLog", params: { page } })
        .catch((err) => {});
      this.getRequestLogs(page);
    },
  },
};
</script>

<style scoped>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
  line-height: 1.6;
  background: #f4f4f4;
  padding: 20px;
  color: #333;
}

.container {
  overflow: hidden; /* 컨텐츠가 컨테이너를 넘치지 않도록 설정 */
  margin: 20px auto;
  padding: 20px;
  background: #fff;
}

/* 로딩 오버레이 스타일 */
.overlay {
  display: none; /* 로딩 시만 보여야 함 */
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.7);
  color: #fff;
  z-index: 9999;
  text-align: center;
  line-height: 100vh;
}

table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 20px; /* 테이블 아래 간격 추가 */
}

th,
td {
  border: 1px solid #ddd;
  padding: 10px;
  text-align: left;
}

/* 테이블 헤더 스타일 */
th {
  background-color: #f9f9f9;
}

/* 줄 간격 색상을 조금 더 진하게 조정 */
tr:nth-child(odd) {
  background-color: #eee;
}

/* 호버 효과 강조 */
tr:hover {
  background-color: #d1d1d1;
}

/* 페이지네이션 스타일 */
.pagination {
  text-align: center;
  padding: 10px 0;
}

.page-item {
  display: inline-block;
  margin-right: 5px;
}

.page-link {
  display: block;
  padding: 8px 16px;
  background-color: #fff;
  border: 1px solid #ddd;
  color: #333;
  text-decoration: none;
}

.page-item.active .page-link,
.page-link:hover {
  background-color: #ddd;
  color: #fff;
}

.page-item.disabled .page-link {
  color: #aaa;
  cursor: not-allowed;
}

/* 반응형 웹 디자인 */
@media (max-width: 768px) {
  .container {
    width: 95%;
    padding: 10px;
  }

  table {
    display: block;
    overflow-x: auto;
  }
}
</style>
