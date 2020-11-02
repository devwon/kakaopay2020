## KAKAOPAY RAIN MONEY API

![money rain](https://previews.123rf.com/images/vectorfusionart/vectorfusionart1707/vectorfusionart170701295/82323183-digital-composite-of-happy-business-man-looking-at-money-rain-against-white-background.jpg)
돈벼락을 맞다는 의미로 **RainMoney** 라고 프로젝트 이름을 정하였음.

개발 환경
---
분야| stack |
--|--|
 |언어 | Java (openjdk 11.0.9) |
 |프레임워크 | springBoot 2.3.4|
 |DB | mysql Ver 8.0.22(도커 이미지 사용, 만든 docker-compose deploy/ 폴더안에 있음)|
 |빌드 툴 | Gradle
 |Persistence 프레임워크 | JPA/Hibernate |
 |API 테스트 툴 | Postman |
 |ERD 작성 툴 | StarUML |
 | IDE | IntelliJ

ER 다이어그램
---
<img width="924" alt="kakaopay_server_ERD" src="https://user-images.githubusercontent.com/34532192/97914961-06b7b480-1d94-11eb-84df-ac7ddc6ea094.png">


API 명세
---
### 요청 공통

`헤더`

| 항목         | 값 (예)          | 설명           | 필수 여부|
| ------------ | ---------------- | --------------- |--
| Content-Type | application/json | `JSON` 으로 요청 |
| X-ROOM-ID    | AAA              | 채팅방 식별값   | O
| X-USER-ID    | 1               | 사용자 식별값   | O

### 응답 공통
---

`HTTP 응답코드`

| 응답코드 | 설명                  |
| -------- | --------------------- |
| `200` | **정상 응답**         |
| `201` | **정상적으로 생성**         |
| `400`    | 잘못된 요청           |
| `404`    | 리소스를 찾을 수 없음 |
| `500`    | 시스템 에러           |

`에러코드 및 메시지`


| 에러코드 | 메시지                  |
| -------- | --------------------- |
| `E0000` | 예상치 못한 오류가 발생하였습니다.         |
| `E0001` | TOKEN 발급에 실패하였습니다.        |
| `E0101`    | 이미 해당 뿌리기 금액을 받으셨네요! 받기는 한번씩만 가능합니다.           |
| `E0102`    | 본인이 뿌린 것은 본인이 받을 수 없습니다. |
| `E0103`    | 데이터가 존재하지 않습니다.           |
| `E0104`    | 본인이 뿌린 것은 본인이 받을 수 없습니다.           |
| `E0105`    | 뿌릴 금액은 뿌릴 인원 이상이어야 합니다.           |
| `E0106`    | 요청값 형식이 잘못되었습니다.           |
| `E0107`    | 마감되었습니다. 다음 기회에!           |
| `E0108`    | 뿌릴 인원은 채팅방내 인원수 이하이어야 합니다.           |
| `E0301`    | 내역 조회는 발급자만 가능합니다.          |
| `E0302`    | 해당 대화방에 소속되어있지 않습니다.        |

`헤더`

| 항목         | 값               | 설명             |
| ------------ | ---------------- | ---------------- |
| Content-Type | application/json | `JSON` 으로 응답 |

내용

| 이름    |  타입  | 필수 | 설명             |
| ------- | :----: | :---: | ---------------- |
| errCode    | string |  ○   | 응답 코드     |
| message | string |  ○   | API 별 응답 내용     |
응답 예

```json
{

"cause":  null,
"stackTrace":  [],
"errCode":  "301",
"message":  "내역 조회는 발급자만 가능합니다.",
"suppressed":  [],
"localizedMessage":  "내역 조회는 발급자만 가능합니다."

}
```

### 뿌리기 API
---

#### 요청

| 항목 | 값             |
| ---- | -------------- |
| URL  | `POST` /kakaopay/rainMoney |

`항목`

| 이름       |  타입  | 필수 | 설명                                                         |
| ---------- | :----: | :---: | ------------------------------------------------------------ |
| total_amount     | int |  ○   | 뿌릴 금액                                           |
| count      | int  |  ○   | 뿌릴 인원                                           |

요청 예)

```json
{
  "amount": 300,
  "count": 3
}
```

#### 응답

`응답 내용`

| 이름 |  타입  | 필수 | 설명        |
| ---- | :----: | :---: | ----------- |
| body   | string |  ○   | 뿌리기 요청건에 대한 고유 token |

응답 예시

```json
  fCz
```

### 뿌린 금액 받기 API
---

#### 요청

| 항목 | 값             | 설명 |
| ---- | -------------- | --- |
| URL  | `POST` /kakaopay/receive|



--


| 이름       |  타입  | 필수 | 설명
| ---------- | :----: | :---: | ------------------------------------ |
| token     | String |  ○   | 뿌릴 금액                                           |

#### 응답

`응답 내용`

| 이름 |  타입  | 필수 | 설명        |
| ---- | :----: | :---: | ----------- |
| body   | long |  ○   | 받은 금액 |

`응답 예시`

```
269
```

### 뿌리기 정보 조회 API

#### 요청

| 항목 | 값             | 설명 |
| ---- | -------------- | --- |
| URL  | `GET` /kakaopay/rainMoney |

#### 응답

`응답 내용`

| 이름 |  타입  | 필수 | 설명        |
| ---- | :----: | :---: | ----------- |
| createdTime | String | ○ | 뿌린 시각 |
| totalAmount | int | ○ | 뿌린 금액 |
| receivedAmount | int | ○ | 받기 완료된 금액 |
| receivedList[].amount | int | ○ | 받은 금액 |
| receivedList[].userId | long | ○ | 받은 사용자 ID |

응답 예시

```json
{
"createdTime":  {
	"date":  {
		"year":  2020,
		"month":  11,
		"day":  3
		},
	"time":  {
		"hour":  5,
		"minute":  32,
		"second":  37,
		"nano":  0
	}
},
"totalAmount":  30000,
"receivedAmount":  22496,
"receivedList":  [
	{
	"amount":  2671,
	"userId":  2
	},
	{
	"amount":  19825,
	"userId":  3
	}
]
}
```
핵심 문제 해결 전략
---
### 뿌리기 기능
- 요구사항에는 없는 내용이지만 현재 상용 뿌리기 서비스에는 있는 뿌릴 인원수 최대값을 채팅방 사용자 수 - 1(본인) 보다 크지 않도록 구현. 불필요하게 뿌릴 인원수를 많이 설정하여 요청자 지갑으로 환불하는 로직을 호출해야하기 때문에 서비스 과부하 가능성 있기 때문(프론트에서 막아도 됨)
- 뿌리기 토큰을 랜덤 3자리로 생성할 때 random() 대신randomAlphanumeric() 으로 생성하여 인코딩 오류 방지, 또한 DB 데이터와 중복 체크 필수
- 중복될 경우나 내부 오류시 지정해놓은 횟수만큼 반복 생성하는데, 현재는 3회로 지정하였지만 추후 뿌릴 인원(count)에 맞춰서 유동적으로 가져가는 것도 고민중.

### 뿌린 금액 받기 기능
- 돈이 오고가는 거래이므로 로직 중에 오류나면 생성한 레코드 롤백하는 @Transaction 어노테이션 사용
- 뿌린 금액이 DB에 오름차순으로 저장되어 있기 때문에 뿌린 금액 받기 기능 호출시 한번 더 랜덤 정렬 시행하여 '랜덤'기능 충실

### 데이터 모델링 관련
- KP_TB_RAIN_MONEY의 FK를 KP_TB_ROOM_USER의 복합키로 할지, 각각의 PK로 할지 고민하다가 복합키로 결정하였으나 조회문이 복잡해지는 단점 생.
- TOKEN 필드의 데이터크기를 3자리로 잡았는데, 추후 확장성을 고려해 더 넉넉하게 잡았어도 좋았겠다는 생각. 서비스가 확장되면 인원이 늘어나고 3자리수로 유니크한 문자가 한계가 있기 때문
- PK Java에서는 Long인데 데이터 타입 Unsigned int로 할지, bigint로 할지 고민했다가 Java에서는 기본적으로 unsigned type을 지원하지 않아서 방법은 있겠으나 시간을 뺏길 것 같아서 bigint 사용

### 총평
원래 자바가 비효율적인 것 같아서 조금 싫어했는데, 이번 프로젝트를 계기로 자바도 효율적으로 쓸 수 있는 것들이 엄청 많구나라고 알게됨. boot, JPA/Hibernate, loombok, gradle, IntelliJ 등 을 통해서 이렇게 느꼈는데, 특히 JPA는 너무 좋았지만 파이썬의 ORM과 비교하면 그래도 해줘야할 게 많다. 이전에 썼던 Mybatis가 금융, SI기업에서 많이 쓰는 이유가 안정적이고 원래 쓰던거여서 라고 하던데, 안정화되면 빨리 도입했으면 좋겠다. 이번에 IntelliJ + gradle 조합을 쓰면서 너무 fancy 하다고 생각.
언어에 대한 인식을 바꾸게 해준 너무 좋은 경험.

