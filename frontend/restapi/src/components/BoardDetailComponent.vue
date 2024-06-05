<template>
  <div class="content">
    <div class="content-header">
      <div class="post-title">{{ post.title }}</div>
      <div class="post-info">
        <span class="nickname">{{ post.nickname }}</span>
        <span class="view-count">조회 수: {{ post.viewCount }}</span>
      </div>
    </div>
    <div class="content-body">
      <div class="inquiry">
        <table class="inquiry-table">
          <tr>
            <th>내용</th>
            <td>
              <div v-for="(content, index) in contentArray" :key="index">
                {{ index + 1 }}. {{ content }}
              </div>
            </td>
          </tr>
        </table>
        <div class="like-button">
          <button
            class="custom-btn"
            :class="{ liked: post.isLiked }"
            @click="postLike"
          >
            좋아요 {{ post.likeCount }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from "vue";
import { useMainStore } from "@/store/store.js";
import { apiGet, apiPost, apiDelete } from "@/utils/api";
import { useRoute, useRouter } from "vue-router";

const route = useRoute();
const router = useRouter();
const store = useMainStore();

const post = ref({
  postId: 0,
  title: "",
  nickname: "",
  content: "",
  createDate: "",
  likeCount: "",
  viewCount: "",
  isLiked: false,
});
const isAdmin = ref(false);
const loginStatus = computed(() => store.loginState);

onMounted(() => {
  getSharePostDetail(route.params.id);
  isAdmin.value = store.getAdminRole;
});

// 상세 문의 내역 가져오기
const getSharePostDetail = async (postId) => {
  try {
    const data = await apiGet(`api/post/share-api/${postId}`);
    console.log(data);
    post.value = data.data;
  } catch (error) {}
};

// 좋아요 누르기
const postLike = async () => {
  if (!loginStatus.value) {
    alert("로그인이 필요합니다.");
    return;
  }
  if (!post.value.isLiked) {
    try {
      await apiPost(`api/post/${post.value.postId}/like`, {});
      post.value.isLiked = true;
      post.value.likeCount += 1;
    } catch (error) {}
  } else {
    console.log("왜");
    try {
      await apiDelete(`api/post/${post.value.postId}/like`);
      post.value.isLiked = false;
      post.value.likeCount -= 1;
    } catch (error) {}
  }
};

const contentArray = computed(() => {
  return post.value.content.split(",");
});
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
  width: 100%;
  max-width: 800px;
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
}

.post-title {
  font-size: 1.5rem;
  font-weight: bold;
}

.post-info {
  display: flex;
  justify-content: space-between;
  margin-top: 10px;
  font-size: 0.9rem;
  color: #888;
}

.nickname {
  font-weight: bold;
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

.like-button {
  margin-top: 20px;
  text-align: center;
}

.custom-btn {
  padding: 0.5rem 1rem;
  background-color: #ffffff;
  color: #000000;
  border: 1px solid #6d6d6d;
  border-radius: 0.375rem;
  transition: background-color 0.2s, border-color 0.2s, color 0.2s;
  text-decoration: none;
  cursor: pointer;
}

.custom-btn.liked {
  background-color: #ffdcdc;
  border-color: #ffadad;
  color: #000000;
}

.custom-btn.liked:hover {
  background-color: #ffc6c6;
  border-color: #ffffff;
  color: #000000;
}

.custom-btn:hover {
  background-color: #e7e7e7;
}
</style>
