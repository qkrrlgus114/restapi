![image](https://github.com/qkrrlgus114/restapi/assets/121294224/67b51fe3-1f48-4874-b1f9-b5769406dbc9)

# RESTFUL API 경로 추천 서비스

## 🌐 사이트 주소
[https://restapi.store/](https://restapi.store/)  


# 1️⃣ **프로젝트 개요**

### 개발 기간
- **2024.03.21 ~ 2024.03.27** (V1 - RESTFUL API 추천 기능)
- **2024.03.28 ~ 2024.03.31** (V2 - 데일리 선착순 쿠폰 도입)
- **2024.04.01 ~ 2024.04.05** (V3 - 관리자 페이지 도입 완료(데일리 선착순 쿠폰 설정 기능 추가))
- **2024.04.10 ~ 2024.05.24** (V4 - 전체적인 css 디자인 변경, 버그 수정, GPT-4o 변경)
- **2024.05.25 ~ 2024.05.30** (V5 - 1:1 문의 게시판 추가, 관리자 답변 기능 추가, 답변 달리면 이메일 체크 추가)
- **2024.05.30 ~ 2024.05.31** (V6 - 관리자 요청 기록 확인 기능 추가)
- **2024.06.01 ~ 2024.06.05** (V7 - API 공유 게시판 추가)
<br>

### 역할 분담
- **박기현**: 백엔드, 프론트엔드, 인프라
<br>

### 기획 배경
매번 RESTFUL하게 API 경로를 작성하는 것에 어려움이 많았던 팀원, 저 자신을 보고 초안을 얻을 수 있도록 서비스를 제작하였습니다.
<br>

### 목표
- 최대한 빠르게 RESTFUL API의 초안을 받아 적용하는 것을 목표로 합니다.

<hr>

# 2️⃣ **서비스 소개**

## RESTFUL하게 API를 만들어보자 - RESFUL API 경로 추천 서비스

### 1. 회원가입
- **자체 회원가입**: 이메일 인증번호 전송을 통한 자체 서비스 회원가입 진행.
- **소셜 회원가입**: 카카오 로그인을 통한 빠른 서비스 회원가입 진행.

### 2. RESTFUL API 추천
- **GPT 최신 모델 사용**: GPT-4o를 사용하여 빠른 응답.
- **토큰 시스템 도입**: 제작자(박기현)의 지갑을 보호하고 과도한 요금 부과를 방지하기 위한 토큰 시스템 도입.
- **프롬프트를 통한 추천**: 모델, 메서드, 자원, 설명을 조합하여 최대 3개의 RESTFUL API를 추천.
- **API 공유**: 추천 받은 API를 공유 게시판에 게시할 수 있습니다.

### 3. 관리자 페이지
- **데일리 쿠폰 설정 기능**: 데일리 쿠폰 발급 여부, 발급 개수 조절 가능.
- **유저 요청 기록 확인**: 유저들의 최근 요청 기록을 확인.
- **유저 추방**: 악성 유저를 추방할 수 있는 기능.(추가 예정)

### 4. 개인정보 페이지
- **개인정보 확인**: 누적 토큰 사용 개수, 누적 토큰 획득 개수 파악
- **탈퇴하기**: 30일 이후 개인정보 완전 삭제 진행.
- **1:1 문의하기**: 관리자에게 1:1 문의 메시지 작성. 이메일로 답변이 달리면 안내 메일을 받음.

### 5. API 공유 게시판
- **게시판**: 유저들이 응답으로 만든 RESTFUL API를 확인할 수 있습니다.

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
 
- ## 스레드 동시성 제어(세마포어)
  - 멀티 스레드 환경에서 GPT의 과도한 동시 요청을 제어
  - 최대 5개의 세마포어 설정을 통해, 동시에 처리할 수 있는 요청의 수를 제한하여 시스템을 안정적으로 운영.

- ## DB 락
  - 선착순 쿠폰 데이터, 좋아요, 조회수의 데이터 정합성을 보장하기 위해 DB 락 도입.
  - 00시에 갱신되는 쿠폰의 특성상, 많은 충돌을 예상하여 낙관적락 대신 비관적락을 선택
 
- ## 비동기 스레드 풀
  - 1:1 관리자 답변 시 이메일 전송 로직을 비동기로 처리하여 빠른 응답 제공.

<hr>

# 4️⃣ **개발 환경**

![사진](https://github.com/qkrrlgus114/restapi/assets/121294224/a8fd57d4-e749-425e-b00e-c2bb9087c194)

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

- ## 📁 Frontend

- Vue3
- Vuex

## 💾 Database
- MySQL

## 🌁 Infra
- docker
- SSL
- CertBot
- Nginx
- EC2

## 외부 API
- Kakao Developer API

<hr>

# 5️⃣ **사진**
## 메인 화면
![image](https://github.com/qkrrlgus114/restapi/assets/121294224/2702b632-13ca-4c02-981f-4d86eb2ffca1)

## 로그인 화면
![image](https://github.com/qkrrlgus114/restapi/assets/121294224/c8522933-3450-41f2-b2e8-390739ca8260)

## 메인 서비스 화면(게스트 모드)
![image](https://github.com/qkrrlgus114/restapi/assets/121294224/520958eb-ec01-4493-935a-639cf190b427)

## RESTAPI 추천 결과
![image](https://github.com/qkrrlgus114/restapi/assets/121294224/736f903a-7bc3-4689-81e5-a4130900ac11)

## 관리자 화면(데일리 쿠폰 설정)
- 데일리 쿠폰 발급 조건 설정 가능.
- 쿠폰 즉시 발급 가능.

![image](https://github.com/qkrrlgus114/restapi/assets/121294224/922fe263-9644-4e1a-9530-236b19983a2e)
![image](https://github.com/qkrrlgus114/restapi/assets/121294224/2696eaf6-0e52-4870-a7cc-a9c42c3680f8)

## 개인정보 화면
![image](https://github.com/qkrrlgus114/restapi/assets/121294224/cb519745-e7ad-43fe-9481-7f5c23aa31e8)

## 회원탈퇴 화면
![image](https://github.com/qkrrlgus114/restapi/assets/121294224/e25ac4ff-643f-4015-9eca-61c19b1a4549)

## 1:1 문의하기 서비스
![image](https://github.com/qkrrlgus114/restapi/assets/121294224/e73b8e39-5ef1-46fb-a4ec-d8c61eaa23bc)
![image](https://github.com/qkrrlgus114/restapi/assets/121294224/d0705b97-dd88-4dd0-b902-9616557e55f9)
![image](https://github.com/qkrrlgus114/restapi/assets/121294224/ebca21e0-5f22-4629-94e6-3b64d4a7cd50)

## API 공유 게시판
![image](https://github.com/qkrrlgus114/restapi/assets/121294224/8f6f1bf2-444c-44de-b86f-e746b70ff3b4)
![image](https://github.com/qkrrlgus114/restapi/assets/121294224/30df4b8d-aaa1-44c8-83ca-2a815d1d39f8)



<hr>
<br><br>

<br><br>
