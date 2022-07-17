# MUSINSA_Assignment

## Category 과제

### 환경셋팅 및 빌드/실행 방법

```java
//Java 11 Install 과정
sudo apt-get update && sudo apt-get upgrade;
sudo apt-get install openjdk-11-jdk

//Git Install 과정
sudo apt install git

//Git Clone 과정
git clone https://github.com/ffinn92/MUSINSA_Assignment.git

//프로젝트 경로에서 Build 과정 진행
gradlew build
cd build/libs
java -jar musinsa-0.0.1-SNAPSHOT.jar
```

### REST API

전체 카테고리 조회 
> GET /api/categories

상위 카테고리 Id로 하위 카테고리 조회 
> GET /api/categories/{id}

카테고리 등록
> POST /api/categories

카테고리 수정
> PATCH /api/categories/{id}?name={name}

카테고리 삭제
> DELETE /api/categories/{id}?name={name}
>> 상위 카테고리 삭제를 해도 하위 카테고리 데이터는 남아있을 수 있도록 제약조건을 수정하게끔 schema.sql을 추가하였습니다.
>> ALTER TABLE category DROP CONSTRAINT FKS2RIDE9GVILXY2TCUV7WITNXC;

## 테스트 결과
![image](https://user-images.githubusercontent.com/92678171/179404967-fa3dd5f6-d277-48c9-a651-54c5034bc2e5.png)
![image](https://user-images.githubusercontent.com/92678171/179405008-5585f785-9f9c-479c-a5c4-c7f367442caa.png)
![image](https://user-images.githubusercontent.com/92678171/179405258-ac55f4f5-b3f3-4038-a2aa-a08cab5baa21.png)





