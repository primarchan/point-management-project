# Point Management Project
Spring Batch 기반 포인트 관리 프로젝트

## TECH-STACK
- Java 11
- Spring Data JPA
- QueryDSL
- Spring Boot 2.7.16
- Spring Batch
- Lombok
- H2
- MySQL
- Docker
- IntelliJ IDEA 2022.1.4 (Ultimate Edition)

## OUTLINE

## REQUIREMENT
- 유저와 포인트 지갑
  - 1명의 유저는 1개의 포인트 지갑을 가진다.
  - 1명의 유저는 N개의 포인트 를 적립한 내역을 가지고 있다.
  - 적립한 포인트들은 유효 기간이 모두 다르다.
- 포인트 만료
  - 유효기간이 만료되면 해당 포인트는 사용 불가한 상태가 된다.
  - 유효기간은 일 단위 까지만 고려하고 시간 단위는 무시한다.
  - 하루에 한 번 알림을 위해 유효기간이 만료된 금액에 대하여 알림 서버에 메시지 전송을 요청한다.
  - 하루에 한 번 알림을 위해 유효기간이 1주일 이내로 임박한 포인트 금액에 대하여 알림 서버에 메시지 전송을 요청한다.
- 포인트 예약 적립
  - 캐시백 보상이나 이벤트 와 같은 이유로 포인트 적립을 예약한다.
  - 예약된 포인트는 정해진 일시에 해당 유저 에게 적립한다.
- 상세 기획
  - 포인트 만료
    - 포인트의 사용 기한이 오늘 보다 이전인 경우 에는 포인트의 상태를 만료로 변경합니다.
    - 포인트를 만료 하면 포인트 지갑의 잔액도 차감 합니다.
    - 이미 사용한 포인트는 만료 하지 않습니다.
    - 만료된 포인트는 유저에게 알림 서비스를 통해 알려줍니다. (DB 저장으로 대체)
  - 포인트 만료 예약
    - 포인트 사용 기한이 7일 이내인 경우 (만료 일자 < 오늘 일자 + 7일) 에는 유저에게 만료 예정 알림을 보냅니다. (DB 저장으로 대체)
  - 포인트 예약 적립
    - 포인트 적립 예약에 저장된 내역 중에 적립일이 오늘이면 그 유저의 포인트 지갑에 포인트를 적립합니다.
    - 적립 예약 건은 완료되면 상태를 완료로 변경합니다.
    - 적립한 포인트 만큼 포인트 지갑의 잔액을 증액시킵니다.
- Batch 상세
  - expirePointJob
    - 포인트 만료 Job
    - 다음 경우에 해당 되면 포인트 만료
      - 사용 기한이 오늘보다 작음
      - 포인트의 상태가 만료가 아님
      - 포인트는 사용하지 않음
    - 포인트 만료하는 방법
      - 포인트의 만료 여부를 true 로 변경
      - 포인트 지갑의 금액을 차감
  - messageExpiredPointJob
    - 만료된 포인트 메시지 전송 Job
    - 다음 경우에 해당 되면 만료 포인트 메시지 전송 대상에 포함됨
      - 포인트의 만료 여부가 true
      - 포인트의 만료 일자가 어제임
    - 메시지 전송하는 방법
      - 유저의 포인트 지갑 1개 당 만료된 포인트 금액을 SUM 한다.
      - 메시지 테이블에 해당 메시지를 저장
      - `Ex) 2023-01-02 기준 3000 포인트가 만료되었습니다.`
  - executePointReservationJob
    - 포인트 예약 건 적립
    - 다음 경우에 해당 되면 포인트 적립
      - 적립 일자가 오늘보다 작거나 같은 경우
      - 적립 완료 상태가 false 인 경우
    - 포인트 예약 적립하는 방법
      - 포인트 예약 건의 상태를 완료로 변경
      - 포인트 적립 내역 내역에 추가
      - 포인트 지갑의 잔액을 증액
  - messageExpireSoonPointJob
    - 포인트 만료 예정 알림 Job
    - 다음 경우에 해당 되면 알림 대상
      - 만료 여부가 false
      - 사용 여부가 false
      - 사용 기한이 오늘 +7일 보다는 작음
    메시지 전송하는 방법
      - 유저의 포인트 지갑 1개 당 만료 예정된 포인트 금액을 SUM 한다.
      - 메시지 테이블에 해당 메시지를 저장
      - `Ex) 2023-01-02 기준 3000 포인트가 만료 예정입니다.`

## REFERENCE

## INFORMATION
### Docker
- `docker run` : Docker Container 실행
- `-p 33060:3306` : 포트 파인딩 Container 내부의 3306 포트를 외부의 33060 포트와 연결 한다.
- `--name` : Container 이름
- `-e` : Container 의 환경 변수 지정 (MYSQL_ROOT_PASSWORD=password 를 통해 비밀 번호 지정) 
- `-d` : Container 를  Background 에서 실행
- `docker run -p 33060:3306 --name point-mysql -e MYSQL_ROOT_PASSWORD=<password> --platform linux/amd64 -d mysql:8.0.26`
- `docker ps` : 실행 중인 Container 조회
- `docker exec -i -t <container id> /bin/bash` : Container Bash 실행
- `mysql -u root -p` : MySQL 실행
- `alter user 'root'@'%' identified with mysql_native_password by '<password>';` : root 의 password 초기화
- `docker stop <container id>` : Container 중지
- `docker rm <contatiner id>` : Container 삭제
- `docker pull <image 이름:버전>` : Image 가져 오기
- `docker images` : Image 조회
- `docker rmi <image id>` : Image 삭제

### Database
- `docker exec -i -t <container id> /bin/bash` -> `mysql -u root -p` : MySQL 실행
- `create database point;` : Database 생성
- `show databases;` : 생성된 Database 조회
- `use point;` : Database 사용
- `show tables`: 생성된 Table 조회

## RESULT