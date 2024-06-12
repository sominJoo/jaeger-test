# jaeger
- 로그 데이터를 중앙에 집중해 관리하기 위한 프로젝트 샘플 코드


# Jaeger 실행 방법
- Docker 파일 제공

❕ All-in-one 이미지를 제공하지만, 운영 시 컨테이너가 죽게되면 단일장애 원천이 되므로 위험하다.
⇒ 개별 컴포넌트로 배포를 해야한다.


1. Docker로 All-in-one 컨테이너 실행

    ```
    docker run -d -p6831:6831/udp -p16686:16686 jaegertracing/all-in-one:latest
    ```

2. Jaeger에서 제공하는 web 접속 → Tracing 정보를 확인 할 수 있음
    - [http://localhost:16686](http://localhost:16686/)


# 레파지토리 설명

## api-gateway
- Spring Cloud Gateway를 사용한 API gateway

## service
- MSA 구현을 위한 테스트 서비스
