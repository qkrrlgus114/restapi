<template class="container">
  <div v-if="isLoading" class="overlay">요청 기록 불러오는중...</div>
  <table>
    <thead>
      <tr>
        <th>No</th>
        <th>회원 ID</th>
        <th>메소드 유형</th>
        <th>요청 날짜</th>
        <th>요청 내용</th>
        <th>응답 내용</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="log in requestLogs" :key="log.id">
        <td>{{ log.no }}</td>
        <td>{{ log.memberId }}</td>
        <td>{{ log.methodType }}</td>
        <td>{{ log.requestDate }}</td>
        <td>{{ log.requestContent }}</td>
        <td>{{ log.responseContent }}</td>
      </tr>
    </tbody>
  </table>
  <button @click="getPrePage">이전 페이지</button>
  <button @click="getNextPage">다음 페이지</button>
</template>

<script>
export default {
  name: "RequestLogView",
  data() {
    return {
      requestLogs: [],
      isLoading: false,
      currentPage: 0,
    };
  },
  mounted() {
    this.getRequestLogs(this.currentPage);
  },
  methods: {
    getRequestLogs(page) {
      this.isLoading = true;
      this.$axios
        .get(`${this.$apiBaseUrl}/api/admin/requests`, {
          params: { page },
          withCredentials: true,
        })
        .then((response) => {
          console.log(response);
          this.requestLogs = response.data.data;
          this.currentPage = page;
        })
        .catch((error) => {
          console.error("요청기록 조회 중 오류 발생:", error);
        })
        .finally(() => {
          this.isLoading = false;
        });
    },
    // 다음 페이지 데이터 로드
    getNextPage() {
      this.getRequestLogs(this.currentPage + 1);
    },
    // 이전 페이지 데이터 로드
    getPrePage() {
      this.getRequestLogs(this.currentPage - 1);
    },
  },
};
</script>

<style scoped>
.container {
  margin-top: 20px;
}

.overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  color: white;
  font-size: 24px;
  z-index: 10;
}

table {
  width: 100%;
  border-collapse: collapse;
}

th,
td {
  border: 1px solid #ddd;
  padding: 8px;
  text-align: center;
}

th {
  background-color: #f2f2f2;
}

tr:nth-child(even) {
  background-color: #f9f9f9;
}

tr:hover {
  background-color: #ddd;
}
</style>
