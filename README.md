# 스프링부트와 AWS로 혼자 구현하는 웹서비스(프리렉, 이동욱) 책 실습
## 웹프로젝트 개발
SpringBoot gradle security oauth2를 활용하여 기본 게시판 등록,수정,삭제, 소셜로그인, 회원가입기능 개발

### 사용 라이브러리
- springboot 2.1.7
- open jdk java 1.8
- gradle 4.10
- lombok
- OAuth2 (구글,네이버)
- Jpa
- spring-session-jdbc
- h2

### AWS 세팅
#### AWS 인스턴스 생성, 로컬 개발환경 구축
- AWS 프리티어 계정생성
- EC2 인스턴스 생성
- 인스턴스에 탄력적IP 등록
- 로컬에서 AWS 인스턴스 접속 
  - windows 
    - putty 사용
      - puttygen.exe으로 인스턴스 pem ppk 변경
      - putty.exe으로 접속
        - connection > ssh > auth > private파일 선택
        - session ec2-user@아이피, port:22, save 클릭
    - xshell 사용

#### AWS EC2 서버 세팅
- 기본 java 7로 설정되어있으므로 8설치후 변경 
  - `sudo yum install -y java-1.8.0-openjdk-devel.x86_64`
  - `sudo /usr/sbin/alternatives --config java` 
    - java 1.8 선택
    - java -version 
- 타임존 변경
  - EC2서버의 기본타임존은 UTC로(세계표준시간) 한국시간대가 아님(9시간차이) java 에서 생성시간도 서버기준을 따라가므로 변경처리
  ```bash
  sudo rm /etc/localtime
  sudo ln -s /usr/share/zoneinfo/Asia/Seoul /etc/localtime
  date
  ```
- 호스트네임 변경
  - 변경한 HOSTNAME으로 호스트명이 변경
    - `sudo vim /etc/sysconfig/network`
      ```bash
      #변경전
      NETWORKING=yes
      HOSTNAME=localhost.localdomain
      NOZEROCONF=yes
      ```
      ```bash
      #변경후
      NETWORKING=yes
      HOSTNAME=springboot-webservice
      NOZEROCONF=yes
      ```
  - 재시작
      - sudo reboot
  - 호스트 파일에 변경한 HOSTNAME등록
    - `sudo vim /etc/hosts`
        ```bash
        ...
        
        127.0.0.1   springboot-webservice
        ```
   - 테스트
     - `curl springboot-webservice`
     - 성공시 80포트로 접속이 되지않는다는 메시지 출력
       - `curl: (7) Failed to connect to springboot-webservice port 80: Connection refused`

#### AWS RDS세팅
- RDS > 파라미터그룹 생성
  - 타임존
    - `time_zone` 
  - Character Set
    - `char` 검색후 검색결과 `utf8mb4`선택
    - `col` 검색후 겸색결과 `utf8mb4_general_ci` 선택 
  - Max Connection
    - `150`으로 변경
- 파라미터 RDS 연결
- 보안그룹 세팅
  - EC2 보안그룹 인바운드 추가
  - 내 IP ssh추가
- ide에서 db 인코딩 변경
    ```mariadb
     show databases ;
     use springboot_webservice;
     show variables like 'c%';
     
     alter database springboot_webservice
         character set ='utf8mb4'
         collate = 'utf8mb4_general_ci';
     commit;
     
     select @@time_zone, now();
     
     create table test (
       id bigint(20) not null auto_increment,
       content varchar(20) default null,
       primary key (id)
     ) engine = InnoDB;
     
     insert into test (content)
     values ('테스트');
     
     select *
     from test;
    ```
- ec2에서 db접속 테스트
  - `sudo yum install mysql`
  - `sudo mysql -u 계정 -p -h 호스트`
  - `show databases`
  
### EC2 서버 배포
- 프로젝트 clone
    ```bash
    sudo yum -y install git &&
    mkdir ~/app && mkdir ~/app/step1 &&
    cd ~/app/step1 &&
    git clone https://github.com/ssw207/SpringBootWebServiceExam.git &&
    cd SpringBootWebServiceExam &&
    chmod +x ./gradlew &&
    ./gradlew test
    ```
  
- 배포 스크립트 작성
  - 프로젝트내 `script/deploy.sh` 참조
  - `vim deploy.sh`
  - gitinore에 제외된 application-oauth.properties 파일생성  
      ```bash
      cd /home/ec2-user/app/step1 &&
      mkdir properties &&
      cd properties &&
      vim ./application-oauth.properties;
      ```  
  - jar 실행시 추가한 properties 참조하도록 `deply.sh` 스크립트 수정
      ```bash
      nohup java -jar \
          -Dspring.config.location=classpath:/application.properties,/home/ec2-user/app/application-oauth.properties \
           $REPOSITORY/$JAR_NAME 2>&1 &
      ```
- DB변경 (h2 -> mariadb) 
  - 테이블 생성
    - h2에서 자동생성됐던 테이블 직접 생성
    - posts, users 
      - 로컬에서 application run후 create sql 복사
    - spring-session
      - `ctrl + shift + n` > `schema-mysql` 검색
    - SQL 
        ```mariadb
        use springboot_webservice;
        create table posts (id bigint not null auto_increment, created_date datetime, modified_date datetime, author varchar(255), content TEXT not null, title varchar(500) not null, primary key (id)) engine=InnoDB;
        create table user (id bigint not null auto_increment, created_date datetime, modified_date datetime, email varchar(255) not null, name varchar(255) not null, picture varchar(255), role varchar(255) not null, primary key (id)) engine=InnoDB;
        
        CREATE TABLE SPRING_SESSION (
                                        PRIMARY_ID CHAR(36) NOT NULL,
                                        SESSION_ID CHAR(36) NOT NULL,
                                        CREATION_TIME BIGINT NOT NULL,
                                        LAST_ACCESS_TIME BIGINT NOT NULL,
                                        MAX_INACTIVE_INTERVAL INT NOT NULL,
                                        EXPIRY_TIME BIGINT NOT NULL,
                                        PRINCIPAL_NAME VARCHAR(100),
                                        CONSTRAINT SPRING_SESSION_PK PRIMARY KEY (PRIMARY_ID)
        ) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;
        
        CREATE UNIQUE INDEX SPRING_SESSION_IX1 ON SPRING_SESSION (SESSION_ID);
        CREATE INDEX SPRING_SESSION_IX2 ON SPRING_SESSION (EXPIRY_TIME);
        CREATE INDEX SPRING_SESSION_IX3 ON SPRING_SESSION (PRINCIPAL_NAME);
        
        CREATE TABLE SPRING_SESSION_ATTRIBUTES (
                                                   SESSION_PRIMARY_ID CHAR(36) NOT NULL,
                                                   ATTRIBUTE_NAME VARCHAR(200) NOT NULL,
                                                   ATTRIBUTE_BYTES BLOB NOT NULL,
                                                   CONSTRAINT SPRING_SESSION_ATTRIBUTES_PK PRIMARY KEY (SESSION_PRIMARY_ID, ATTRIBUTE_NAME),
                                                   CONSTRAINT SPRING_SESSION_ATTRIBUTES_FK FOREIGN KEY (SESSION_PRIMARY_ID) REFERENCES SPRING_SESSION(PRIMARY_ID) ON DELETE CASCADE
        ) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;
        commit ;
        ```
  - 프로젝트 설정
    - mariadb 드라이버 추가
  - EC2설정
    - DB설정정보파일 추가