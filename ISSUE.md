# TravisCI 빌드중 에러 
## 로그
```bash
Worker information

system_info
Build system information

docker_mtu
resolvconf
install_jdk
Installing openjdk8
s git.checkout
$ git clone --depth=50 --branch=master https://github.com/ssw207/SpringBootWebServiceExam.git ssw207/SpringBootWebServiceExam
$ export TERM=dumb
cache.1
Setting up build cache
$ java -Xmx32m -version
openjdk version "1.8.0_252"
OpenJDK Runtime Environment (build 1.8.0_252-8u252-b09-1~16.04-b09)
OpenJDK 64-Bit Server VM (build 25.252-b09, mixed mode)
$ javac -J-Xmx32m -version
javac 1.8.0_252
$ ./gradlew assemble
/home/travis/.travis/functions: line 351: ./gradlew: Permission denied
The command "eval ./gradlew assemble " failed. Retrying, 2 of 3.
/home/travis/.travis/functions: line 351: ./gradlew: Permission denied
The command "eval ./gradlew assemble " failed. Retrying, 3 of 3.
/home/travis/.travis/functions: line 351: ./gradlew: Permission denied
The command "eval ./gradlew assemble " failed 3 times.
The command "./gradlew assemble" failed and exited with 126 during .
Your build has been stopped.
```
## 해결
- TravisCI가 ./gradlew 를 실행할 권한이 없어서 발생한 문제로 발생 `travis.yml`파일에 아래 코드 추가
```mariadb
...

before_install: // master브랜치에 push할 경우 travisCI에서 ./gradlew를 실행할 권한이 없는경우 에러가 발생하므로 추가
  - chmod +x gradlew
```

# InteliJ, TradviCI build시 에러 
## TradvisCI 빌드 에러로그
```java
org.springframework.transaction.CannotCreateTransactionException: Could not open JPA EntityManager for transaction; nested exception is java.lang.IllegalStateException: EntityManagerFactory is closed
	at org.springframework.orm.jpa.JpaTransactionManager.doBegin(JpaTransactionManager.java:446) ~[spring-orm-5.1.9.RELEASE.jar:5.1.9.RELEASE]
	at org.springframework.transaction.support.AbstractPlatformTransactionManager.getTransaction(AbstractPlatformTransactionManager.java:378) ~[spring-tx-5.1.9.RELEASE.jar:5.1.9.RELEASE]
	at org.springframework.transaction.support.TransactionTemplate.execute(TransactionTemplate.java:137) ~[spring-tx-5.1.9.RELEASE.jar:5.1.9.RELEASE]
	at org.springframework.session.jdbc.JdbcOperationsSessionRepository.cleanUpExpiredSessions(JdbcOperationsSessionRepository.java:619) ~[spring-session-jdbc-2.1.8.RELEASE.jar:2.1.8.RELEASE]
	at 
```

- `java.lang.IllegalStateException: EntityManagerFactory is closed`
## 해결
- build.gralde 문법 오류 수정 
  - 현재 gradle 4.x 버전사용중이나, 의존성 문법을 4.x+ 버전에서 사용하는 방식으로 사용
  - maven repository의 gradle 의존성은 코드는 4.x+ 버전 문법으로 되어있음
```
//testCompile group: 'org.springframework.security', name: 'spring-security-test'
testCompile('org.springframework.security:spring-security-test')
```

## 문제파악
1. TravisCI 빌드시 에러발생
1. JPA관련 설정 잘못되었는지 확인
   - 테스트코드 실행시 이상없음
1. IntelliJ의 터미널에서 ./gradlew build 명령어 실행 
   - `EntityManagerFactory`동일에러 확인
1. EC2에서 ./gradlew build 명령어 실행
   - 정상 빌드확인. IntelliJ 빌드 문제로 추정
1. 터미널 ./gradlew test실행
   - 정상실행 확인. build쪽 문제로 추정. build.grdle확인
1. build.gradle jcentral() 추가
   - 동일에러 발생, 빌드시 라이브러리는 정상적으로 들어오므로 저장소를 추가하는 것은 관계가 없음
1. build.gradle 의존성 부분 문법 변경
   - 터미널 ./gradlew build 시 성공 확인
1. git commit후 TravisCI 빌드 성공 확인 

# CodeDeploy 배포 성공후 deploy.sh 실행되지 않는 이슈 
## 문제파악
1. CodeDeploy에서 EC2로 파일을 정상적으로 생성됨
   - 생성된 파일들의 권한이 `root` `appspec.yml`에작성한 대로면 `ec2-user` 권한으로 파일이 생성되어야함.
   - `appspec.yml` 작성오류 추정
1. `appspec.yml` permission -> permissions 오타수정