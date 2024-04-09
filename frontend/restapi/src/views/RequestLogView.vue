<template class="container">
  <div v-show="isLoading" class="overlay">요청 기록 불러오는중...</div>
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
    <button @click="getRequestLogs(1)" :disabled="!canSearch">검색</button>
  </div>
  <nav aria-label="Page navigation">
    <ul class="pagination">
      <li class="page-item" :class="{ disabled: currentPage <= 1 }">
        <button
          class="page-link"
          @click="changePage(currentPage - 5)"
          :disabled="currentPage <= 1"
        >
          이전
        </button>
      </li>
      <li
        class="page-item"
        v-for="page in paginatedPages"
        :key="page"
        :class="{
          active: currentPage === page,
          disabled: currentPage === page,
        }"
      >
        <button
          class="page-link"
          @click="currentPage !== page && changePage(page)"
          :disabled="currentPage === page"
        >
          {{ page }}
        </button>
      </li>
      <li class="page-item" :class="{ disabled: currentPage >= totalPages }">
        <button
          class="page-link"
          @click="changePage(currentPage + 5)"
          :disabled="currentPage >= totalPages"
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
      return Array.from({ length: 5 }, (v, k) => startPage + k).filter(
        (page) => page <= this.totalPages
      );
    },
    canSearch() {
      return this.searchType && this.searchKey.trim();
    },
  },
  data() {
    return {
      requestLogs: [],
      isLoading: false,
      currentPage: 1,
      totalPages: 0, // 총 페이지 수
      totalElements: 0, // 총 요소 수
      searchKey: "",
      searchType: "",
    };
  },
  watch: {
    "$route.params.page": function (newPage) {
      // URL의 페이지 번호가 변경될 때마다 새로운 페이지 데이터를 로드
      // URL은 1부터 시작하지만, 백엔드 요청을 위해 1을 빼줌 (백엔드는 0부터 시작)
      this.getRequestLogs(newPage ? Number(newPage) : 1);
    },
  },
  mounted() {
    // 컴포넌트가 마운트될 때 현재 페이지에 해당하는 데이터 로드
    // $route.params.page가 undefined일 수 있으므로 기본값 1 사용
    this.getRequestLogs(
      this.$route.params.page ? Number(this.$route.params.page) : 1
    );
  },
  methods: {
    getRequestLogs(page) {
      this.isLoading = true;
      // 백엔드에 맞춰 페이지 번호 조정 (1 빼기)
      const adjustedPage = page - 1;
      const params = { page: adjustedPage };

      if (this.canSearch) {
        params.searchType = this.searchType;
        params.searchKey = this.searchKey;
      }

      this.$axios
        .get(`${this.$apiBaseUrl}/api/admin/requests`, {
          params,
          withCredentials: true,
        })
        .then((response) => {
          this.requestLogs = response.data.data.content;
          this.totalPages = response.data.data.totalPages;
          this.totalElements = response.data.data.totalElements;
          this.currentPage = page; // URL과 일치하는 사용자 친화적 페이지 번호 설정
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
    changePage(newPage) {
      // 사용자 친화적 페이지 번호 사용 (1부터 시작)
      this.$router
        .push({ name: "RequestLog", params: { page: newPage } })
        .catch((err) => {});
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
