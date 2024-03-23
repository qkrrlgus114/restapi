<template>
  <div class="api-container">
    <h1>REST API 생성하기</h1>
    <div v-if="isLoading" class="overlay">restapi 생성중...</div>
    <div class="form-wrapper">
      <div class="form-group">
        <label for="model">GPT 모델 선택:</label>
        <select v-model="formData.model" id="model">
          <option disabled value="">모델 선택</option>
          <option value="gpt-3.5-turbo">GPT-3.5</option>
          <option value="gpt-4">GPT-4</option>
        </select>
      </div>
      <div class="form-group">
        <label for="operation">작업 선택:</label>
        <select v-model="formData.operation" id="operation">
          <option disabled value="">작업 선택</option>
          <option value="create">생성</option>
          <option value="modify">수정</option>
          <option value="delete">삭제</option>
          <option value="check">조회</option>
        </select>
      </div>
      <div class="form-group">
        <label for="methodType">메서드 타입:</label>
        <select v-model="formData.methodType" id="methodType">
          <option disabled value="">메서드 타입 선택</option>
          <option value="GET">GET</option>
          <option value="POST">POST</option>
          <option value="PUT">PUT</option>
          <option value="PATCH">PATCH</option>
          <option value="DELETE">DELETE</option>
        </select>
      </div>
      <div class="form-group">
        <label for="resource">자원:</label>
        <input type="text" v-model="formData.resource" id="resource" required />
      </div>
      <div class="form-group">
        <label for="content">설명:</label>
        <textarea v-model="formData.content" id="content" required></textarea>
      </div>
      <div class="form-actions">
        <button :disabled="!isFormValid" @click="submitForm">제출</button>
        <button type="button" @click="resetForm">초기화</button>
      </div>
    </div>
    <div v-if="contentItems.length > 0">
      <h2>추천 결과:</h2>
      <ul>
        <li v-for="(item, index) in contentItems" :key="index">{{ item }}</li>
      </ul>
    </div>
  </div>
</template>

<script>
export default {
  name: "ChatView",
  data() {
    return {
      formData: {
        model: "",
        operation: "",
        methodType: "",
        resource: "",
        content: "",
      },
      isLoading: false,
      contentItems: [],
    };
  },
  computed: {
    isFormValid() {
      return (
        this.formData.model &&
        this.formData.operation &&
        this.formData.methodType &&
        this.formData.resource &&
        this.formData.content
      );
    },
  },
  methods: {
    // 제출
    submitForm() {
      const currentToken = parseInt(localStorage.getItem("token"), 10);

      if (currentToken > 0) {
        this.isLoading = true;
        this.$axios
          .post(`${this.$apiBaseUrl}/api/write`, this.formData, {
            withCredentials: true, // 쿠키 포함하기 위해
          })
          .then((response) => {
            alert("데이터 추천 성공");

            const newTokenCount = currentToken - 1;
            localStorage.setItem("token", newTokenCount.toString());
            this.token = newTokenCount; // 컴포넌트의 데이터를 업데이트
            if (
              response.data &&
              response.data.data &&
              response.data.data.choices
            ) {
              const choice = response.data.data.choices[0];
              if (choice && choice.message && choice.message.content) {
                // \n을 기준으로 content를 분리하여 배열로 저장
                this.contentItems = choice.message.content.split("\n");
              }
            }
          })
          .catch((error) => {
            console.log(error);
            if (error.response.data.status == "1004") {
              alert("토큰이 부족합니다.");
            } else {
              alert("문제 발생");
            }
          })
          .finally(() => {
            this.isLoading = false;
          });
      } else {
        alert("토큰이 부족합니다. 토큰 갱신 눌러보세요(혹시?)");
      }
    },
    resetForm() {
      this.formData = {
        gptModel: "",
        task: "",
        methodType: "",
        resource: "",
        description: "",
      };
    },
  },
};
</script>

<style scoped>
.api-container {
  max-width: 500px;
  margin: 100px auto;
  padding: 20px;
  background-color: #f9f9f9;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  color: white;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 24px;
  z-index: 100;
}

.form-wrapper {
  display: flex;
  flex-direction: column;
}

.form-group {
  margin-bottom: 1rem;
}

label {
  display: block;
  margin-bottom: 0.5rem;
}

input[type="text"],
textarea,
select {
  width: 100%;
  padding: 0.5rem;
  margin-bottom: 0.5rem;
  border: 1px solid #ccc;
  border-radius: 4px;
}

textarea {
  resize: vertical; /* 사용자가 세로 크기 조절 가능 */
}

.form-actions {
  display: flex;
  justify-content: flex-start;
}

button {
  margin-right: 1rem;
  padding: 0.5rem 1rem;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

button:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}

button:hover:not(:disabled) {
  background-color: #0056b3;
}

h1 {
  text-align: center;
  color: #333;
  margin-bottom: 2rem;
}

h2 {
  color: #333;
  margin-top: 2rem;
}

ul {
  list-style-type: none;
  padding: 0;
}

li {
  background-color: #e9ecef;
  margin-bottom: 0.5rem;
  padding: 0.5rem;
  border-radius: 4px;
}

.overlay {
  font-size: 20px; /* 조금 더 작은 텍스트 크기로 조정 */
}

/* 반응형 디자인 추가 */
@media (max-width: 768px) {
  .api-container {
    margin: 10px;
    padding: 10px;
  }

  .form-actions {
    flex-direction: column;
  }

  button {
    width: 100%;
    margin-bottom: 1rem; /* 버튼 사이의 간격 조정 */
  }
}
</style>
