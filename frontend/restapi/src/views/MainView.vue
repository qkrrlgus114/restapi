<template>
  <div class="container">
    <div class="header">
      <img src="/public/logo.png" alt="" class="logo" />
      <p>REST API 어떻게 지어야할까?</p>
    </div>
    <div class="step" v-for="guide in guides" :key="guide.id">
      <div class="description">
        <h3>{{ guide.title }}</h3>
        <p>{{ guide.text }}</p>
      </div>
      <img :src="guide.imageUrl" :alt="`이미지 ${guide.id}`" class="de-img" />
    </div>

    <div class="footer">
      <button @click="goToLogin">시작하기</button>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      guides: [
        {
          id: 1,
          title: "1. 원하는 GPT 모델 선택하기",
          text: "원하는 GPT 모델을 선택하세요.",
          imageUrl: "/guide1.png",
        },
        {
          id: 2,
          title: "2. 원하는 메서드 선택하기",
          text: "사용하고 싶은 메서드를 선택하세요.",
          imageUrl: "/guide2.png",
        },
        {
          id: 3,
          title: "3. 사용하는 자원 입력하기",
          text: "사용되는 자원을 입력하세요.(ex user, post, comment)",
          imageUrl: "/guide3.png",
        },
        {
          id: 4,
          title: "4. 간단한 설명 작성하기",
          text: "어떤 api를 만들고 싶은지 설명을 적어주세요(상세할 수록 좋습니다.)",
          imageUrl: "/guide4.png",
        },
        {
          id: 5,
          title: "5. 추천 결과 확인",
          text: "3개의 추천 결과를 받아보세요.",
          imageUrl: "/guide5.png",
        },
      ],
    };
  },
  methods: {
    goToLogin() {
      this.$router.push("/loginview");
    },
  },
};
</script>

<style scoped>
.container {
  width: 100%;
  top: 0;
  left: 0;
  position: absolute;
  padding: 100px 0px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  overflow-y: auto;
  background-color: #000;
  background-size: cover;
}

.header {
  text-align: center;
  margin: 200px 0px;
}

.header h1 {
  margin: 0;
  font-size: 4em;
  font-weight: 300;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5);
  font-family: "Montserrat", sans-serif;
}

.header p {
  font-size: 1.25em;
  max-width: 800px;
  margin-bottom: 1.5em;
  line-height: 1.6;
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.5);
  color: white;
  font-family: "Open Sans", sans-serif;
  opacity: 0.85;
}

.step {
  display: flex;
  align-items: center;
  justify-content: flex-start;
  margin: 50px 0px;
  width: 80%;
  height: 300px;
}

.description {
  background-color: #ffffff;
  color: #333333;
  width: 25%;
  padding: 20px;
  text-align: left;
  margin: 0 15%;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  border-radius: 4px;
  border: none;
}
.description h3 {
  font-size: 1.5em;
  color: #000000;
  margin-bottom: 0.5em;
}

.description p {
  font-size: 1em;
  line-height: 1.4;
  color: #555555;
}

.logo {
  width: 500px;
  height: 200px;
}

.step .de-img {
  width: 35%; /* Adjust image width to match description */
  height: auto;
  border: 1px solid #ccc;
}

.footer {
  margin-top: 20px;
  width: 100%; /* 폭을 100%로 설정하여 화면 가득 차게 함 */
  display: flex;
  justify-content: center;
}

button {
  padding: 20px 40px; /* 버튼의 크기를 증가시킴 */
  font-size: 24px; /* 글꼴 크기 증가 */
  font-weight: bold; /* 글꼴 두께를 더욱 진하게 함 */
  border-radius: 30px; /* 모서리를 더욱 둥글게 함 */
  border: 2px solid #fff; /* 버튼의 테두리를 흰색으로 설정 */
  background: #000; /* 배경색을 검은색으로 */
  color: #fff; /* 글꼴 색상을 흰색으로 */
  text-transform: uppercase; /* 대문자로 변환 */
  cursor: pointer;
  outline: none;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2); /* 그림자 효과 추가 */
  transition: all 0.3s ease; /* 부드러운 전환 효과 추가 */
}

button:hover {
  background: #444; /* 마우스 오버 시 버튼 색 변경 */
  color: #ddd; /* 마우스 오버 시 글꼴 색 변경 */
  transform: translateY(-2px); /* 마우스 오버 시 버튼이 약간 위로 움직임 */
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.3); /* 마우스 오버 시 그림자 효과 강조 */
}

@keyframes fadeInUp {
  from {
    transform: translate3d(0, 20px, 0);
    opacity: 0;
  }

  to {
    transform: translate3d(0, 0, 0);
    opacity: 1;
  }
}

.header > * {
  animation: fadeInUp 0.8s ease-out forwards;
  animation-delay: 0.2s;
}
</style>
