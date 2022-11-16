# 과제
1일차 과제 - 학생 정보를 등록, 조회, 수정할 수 있는 Controller를 기반으로

# View 변경
- JSP 대신 Thymeleaf View template engine 사용

# 다국어 지원
- 모든 메세지는 영어, 한국어 2개 언어 지원
- 언어 변경 가능

# 로그인/로그아웃 기능 추가
- 로그인하지 않고 다른 기능 사용 불가
- 로그인 체크하는 `LoginCheckInterceptor` 작성

# REST API 제공 - JSON 요청/응답
- 학생 정보 등록: `POST /students`
- 학생 정보 조회: `GET /students/{studentId}`
- 학생 정보 수정: `PUT /students/{studentId}`
