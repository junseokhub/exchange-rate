# 과제 개요

주요 4개 통화(USD,JPY,CNY,EUR) 에 대해 실시간으로 변동하는 환율 정보를 데이터베이스에 이력으로 관리하고, 사용자가 요청한 외화 금액을 현재 환율에 맞춰 원화로 환산하여 주문을 처리하는 백엔드 시스템을 구현합니다.

## 실행 방법
- ./gradlew bootRun
- 앱 실행 시 즉시 환율 수집 시작 (이후 1분마다 반복)
- API: http://localhost:8080
- H2 콘솔: http://localhost:8080/h2-console (활성화 시)
  - url: dbc:h2:mem:forexdb
  - user: sa

## 기술 스택

- Java 17, Spring Boot 3.5.14
- H2 In-Memory DB, Spring Data JPA
- Spring Batch, Spring Scheduler
- Resilience4j CircuitBreaker, Retry
- RestClient

## 테스트 코드
- 단위 테스트 (java 순수코드 Order, ExchangeRate)
- 통합 테스트 (ExchangeRateIntegrationTest, OrderIntegrationTest)

## API (과제 요구사항과 동일하게 처리했습니다.)

### 환율 조회

GET /exchange-rate/latest
- 전체 통화(USD, JPY, CNY, EUR) 최신 환율 조회
- currency, buyRate, tradeStanRate, sellRate, dateTime 반환

GET /exchange-rate/latest/{currency}
- 특정 통화 최신 환율 조회
- 지원하지 않는 통화 요청 시 400 반환

### 주문

POST /order
- forexAmount, fromCurrency, toCurrency 요청
- 모든 주문은 KRW 기준 (KRW->외화 또는 외화->KRW)
- KRW->외화 : buyRate 적용
- 외화->KRW : sellRate 적용
- 동일 통화 주문, 이중 통화 주문(USD->JPY 등) 400 반환

GET /order/list
- 전체 주문 내역 조회

## 에러 응답

```json
{
  "code": "BAD_REQUEST",
  "message": "지원하지 않는 통화입니다.",
  "returnObject": null
}
```

## 에러 코드
- BAD_REQUEST : 지원하지 않는 통화
- BAD_REQUEST : 잘못된 주문 요청
- NOT_FOUND : 환율 정보를 찾을 수 없음
- SERVICE_UNAVAILABLE : 외부 연동 오류 발생

## 동작 방식

**환율 수집**
- 앱이 시작되면 스케줄러가 즉시 실행되고 이후 1분마다 반복됩니다. open.er-api.com에서 KRW 기준 환율을 가져와서 역수를 취해 원화 환율로 변환합니다. JPY는 1엔 기준으로 내려오기 때문에 100엔 기준으로 변환해서 저장합니다.
- 외부 API 호출 실패 시 최대 3번 재시도하고 그래도 실패하면 Mock 데이터로 대체합니다. 실패율이 50%를 넘으면 60초간 API 호출을 차단합니다.


**주문 처리**
- 모든 주문은 KRW 기준입니다. KRW에서 외화를 살 때는 buyRate, 외화를 팔 때는 sellRate가 적용됩니다.
  - buyRate = 매매기준율 x 1.05
  - sellRate = 매매기준율 x 0.95
  - KRW 환산 금액은 소수점 버림 처리
  - JPY는 100엔 기준 환율로 저장되므로 계산 시 100으로 나눕니다

## 결정 기록
**Spring Batch**
- 처음에는 @Scheduled + @Transactional 조합으로 충분하다고 생각했습니다.
- 근데 위 방식은 실패했을 때 어디서 실패했는지 추적이 안 되고 재시도 정책도 직접 구현해야 합니다.
- Spring Batch는 Job 실행 이력을 DB에 자동으로 남기기 때문에 언제 실패했는지 바로 확인할 수 있고 Skip/Retry 정책도 설정만으로 처리할 수 있습니다. 
- 지금은 통화가 4개라 체감이 없지만 나중에 수십 개로 늘어나면 chunk 단위 처리가 의미있어집니다.

**Resilience4j**
- 외부 API가 느려지거나 죽었을 때 단순 try-catch로 처리하면 타임아웃이 날 때까지 스레드가 잡혀있게 됩니다. 
- 요청이 몰리면 스레드 풀이 다 차서 다른 API까지 응답이 느려질 수 있습니다. 
- CircuitBreaker는 장애를 감지하면 차단하고 Mock 데이터로 전환하기 때문에 외부 API 장애가 시스템 전체로 번지지 않습니다.
- Retry도 같은 라이브러리에서 관리할 수 있어서 설정을 한 곳에서 관리할 수 있다는 것도 이유 중 하나입니다.

**Test**
- 요구 사항엔 없었지만 기본적인 테스트 코드는 필요하다 생각하여 작성했습니다.
- 현재는 인메모리 기반이라 인메모리 안에서 테스트가 동작하지만 추후 TestContainer등을 사용하면 실무와 같은 환경에서 테스트 가능하다고 생각합니다.