# RESTFUL하게 API를 만들어보자 - RESFUL API 추천 서비스
<br>

# 1️⃣ **프로젝트 개요**

### **개발 기간**

| 개발기간 | 2024.03.21 ~ 개선중 |
| -------- | ----------------------------- |

### 역할 분담

- 박기현 : 백엔드, 인프라
  
- Chat-GPT : 프론트엔드

### **기획 배경**

```
매번 프로젝트를 할 때마다 RESTFUL API 경로를 짓는 것에 진절머리가 나서 제작을 결심.

- 간단한 요청으로 빠르게 RESTFUL한 API를 추천받음.
- 정해진 토큰으로 일일 사용량 제한.
- 매일 12시마다 사용 토큰 충전
- 선착순 토큰 쿠폰 발행(진행 예정)
```

### **목표**

```
최대한 빠르게 RESTFUL API의 초안을 받아서 적용하는 것이 목표입니다.
```

<hr>

# 2️⃣ **서비스 소개**

## RESTFUL하게 API를 만들어보자 - RESFUL API 추천 서비스

### 1. 회원가입
- **자체 회원가입**: 이메일 인증번호 전송을 통해 자체 서비스 회원가입을 진행합니다.
- **소셜 회원가입**: 카카오 로그인을 통해 빠르게 서비스 회원가입을 진행합니다.

### 2. RESTFUL API 추천
- **다양한 모델 선택** : GPT 3.5, GPT4를 통해 다양한 모델 선택이 가능합니다.(모델 확장 가능)
- **사용 토큰** : 사용 토큰을 도입하여 제작자(박기현)의 지갑을 안전하게 지킬 수 있습니다.(과도한 요금 부과 문제 방지)
- **프롬프트를 통한 추천** : 모델, 메서드, 자원, 설명을 조합하여 RESTFUL API를 3개까지 추천해줍니다.(빠른 초안 작성 가능)


<hr>

# 3️⃣**주요 기술**

- ## KAKAO API
  - 카카오 로그인(Security Oauth2를 사용하여 BackEnd에서 로그인 로직 구현 / 클라이언트 <-> 백엔드 <-> 카카오서버 )
    

- ## Chat-GPT API
  - API에 필요한 model, message를 생성하여 GPT로부터 응답 데이터 반환.
  - 자체 제작 프롬프트의 RESTFUL API 정의를 바탕으로 데이터 생성.

- ## JWT
  - 토큰 인증 방식을 사용하여 서버의 부담을 줄이고 확장성을 챙김.
  - Cookie의 secure(), httpOnly() 옵션을 통해 보안성 향상.
  - RTR(RefreshToken Rotation) 기법을 사용하여 리프레시 토큰의 탈취 피해를 최소화.
 
- ## 동시성 제어(세마포어)
  - 멀티 스레드 환경에서 과도한 GPT의 동시 요청을 제어하기 위해 세마포어를 사용.
  - 최대 5개의 세마포어 설정을 통해, 동시에 처리할 수 있는 요청의 수를 제한하여 시스템을 안정적으로 운용.

<hr>

# 4️⃣ **개발 환경**

## ⚙ Management Tool

- 형상 관리 : GitHub

<br>

## 💻 IDE

- Visual Studio Code
- Intellij CE 2023.1.3

<br>

## 📁 Backend

- Springboot `3.2.4`
- Lombok
- Spring Data JPA
- Spring Web
- Spring Security
- JWT
- Spring-oauth2-client

## 💾 Database
- MySQL

## 🌁 Infra
- docker
- SSL
- CertBot
- Nginx

## 외부 API
- Kakao Developer API

<hr>

# 5️⃣ **사용 영상**

![녹화_2024_03_27_22_35_42_710](https://github.com/qkrrlgus114/restapi/assets/121294224/bbd9877f-f701-4197-adb5-b104ed323220)

![녹화_2024_03_27_22_36_10_416](https://github.com/qkrrlgus114/restapi/assets/121294224/74b54c25-f2b7-4ea5-beba-7b667b404fce)


<hr>
<br><br>

<br><br>
