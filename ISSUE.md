# TravisCI 빌드중 에러 
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
- TravisCI가 ./gradlew 를 실행할 권한이 없어서 발생한 문제로 추정 
