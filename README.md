# Point Management Project
Spring Batch 기반 포인트 관리 Project

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