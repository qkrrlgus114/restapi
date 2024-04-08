# RESTFUL API 경로 추천 서비스

## 🌐 사이트 주소
[https://restapi.store/](https://restapi.store/)  
<br>
**주의**: API 사용량이 많아 가격 부담이 크므로 하루 1회 제한을 두었습니다.

# 1️⃣ **프로젝트 개요**

### 개발 기간
- **2024.03.21 ~ 2024.03.27** (V1 - RESTFUL API 추천 기능)
- **2024.03.28 ~ 2024.03.31** (V2 - 데일리 선착순 쿠폰 도입)
- **2024.04.01 ~ 2024.04.05** (V3 - 관리자 페이지 도입 완료(데일리 선착순 쿠폰 설정 기능 추가))
- **2024.04.06 ~ 2024.04.08** (V3 - 관리자 요청 기록 확인 기능 추가)
<br>

### 역할 분담
- **박기현**: 백엔드, 프론트엔드(30%), 인프라
- **Chat-GPT**: 프론트엔드(70%)
<br>

### 기획 배경
매번 RESTFUL API 경로를 생각하는 것이 머리 아파서, 간단하게 초안을 받을 수 있도록 서비스를 제작하였습니다.
- 정해진 토큰으로 일일 사용량을 제한합니다.
- 매일 12시마다 사용 토큰을 충전합니다.
- 이벤트로 선착순 토큰을 발행합니다.
<br>

### 목표
- 최대한 빠르게 RESTFUL API의 초안을 받아 적용하는 것을 목표로 합니다.

<hr>

# 2️⃣ **서비스 소개**

## RESTFUL하게 API를 만들어보자 - RESFUL API 경로 추천 서비스

### 1. 회원가입
- **자체 회원가입**: 이메일 인증번호 전송을 통한 자체 서비스 회원가입 진행
- **소셜 회원가입**: 카카오 로그인을 통한 빠른 서비스 회원가입 진행

### 2. RESTFUL API 추천
- **다양한 모델 선택**: GPT 3.5, GPT4 등 다양한 모델 선택 가능 (모델 확장 예정)
- **토큰 시스템 도입**: 제작자(박기현)의 지갑을 보호하고 과도한 요금 부과를 방지하기 위한 사용 토큰 도입
- **프롬프트를 통한 추천**: 모델, 메서드, 자원, 설명을 조합하여 최대 3개의 RESTFUL API를 추천

### 3. 관리자 페이지
- **데일리 쿠폰 설정 기능**: 데일리 쿠폰 발급 여부, 발급 개수 조절이 가능합니다.
- **유저 요청 기록 확인**: 유저들의 최근 요청 기록을 확인합니다.
- **유저 추방**: 악성 유저를 추방할 수 있는 기능입니다.(추가 예정)

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
  - 멀티 스레드 환경에서 GPT의 과도한 동시 요청을 제어
  - 최대 5개의 세마포어 설정을 통해, 동시에 처리할 수 있는 요청의 수를 제한하여 시스템을 안정적으로 운용.

- ## 비관적락
  - 선착순 쿠폰 데이터의 정합성을 관리하기 위해 비관적락 도입.
  - 00시에 갱신되는 쿠폰의 특성상, 많은 충돌을 예상하여 낙관적락 대신 비관적락을 선택
 
- ## 프롬프트
  - RESTFUL API의 필수 원칙을 정의하여 프롬프트로 제작.
  ![image](https://github.com/qkrrlgus114/restapi/assets/121294224/1845f065-9811-4a46-b6ea-5d198cccf460)

- ## Vuex
  - data로 관리하기에 번거로움을 느껴 Vuex를 도입하여 전역으로 데이터 관리.


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

# 5️⃣ **사진 및 사용 영상**
## 메인 화면
![image](https://github.com/qkrrlgus114/restapi/assets/121294224/b5b1437c-8e21-4ede-9865-a77a48bc8986)
## 로그인 화면
![image](https://github.com/qkrrlgus114/restapi/assets/121294224/016cfe75-ae44-457e-8d1a-c2e41fbc6603)
## 메인 서비스 화면
![image](https://github.com/qkrrlgus114/restapi/assets/121294224/737368db-0d4a-4ab3-ae19-65ff6ff3e16d)
## RESTAPI 추천 결과
![image](https://github.com/qkrrlgus114/restapi/assets/121294224/87f18a8f-5ca2-47cd-8e05-9d540cd8eaf3)
## 관리자 페이지(데일리 쿠폰 설정)
![image](https://github.com/qkrrlgus114/restapi/assets/121294224/86e562f8-f42f-4a70-9dd4-0a6506075aec)
## 관리자 페이지(요청 기록 확인)
![image](https://github.com/qkrrlgus114/restapi/assets/121294224/c3580365-c786-4d28-845a-4512ff646b9b)



![녹화_2024_03_27_23_06_42_125](https://github.com/qkrrlgus114/restapi/assets/121294224/1793b28d-1691-4482-b7c7-01930a6d2406)


![녹화_2024_03_27_23_07_00_651](https://github.com/qkrrlgus114/restapi/assets/121294224/95bb2f77-0ff2-4cb6-8908-40514be2e12f)


<hr>
<br><br>

<br><br>
