#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)

#프로필 쉘스크립트 임포트
source ${ABSDIR}/profile.sh

#프로필 쉘스크립트에 정의된 함수실행
IDLE_PORT=$(find_idle_port)

echo "> $IDLE_PORT 에서 구동중인 애플리케이션 pid 확인"

# lsof    : 시스템에서 열린 파일 목록을 알려주고 상세정보를 보여준다.
#      -t : pid정보만 출력
#      -i : 프로토콜, 포트를 같이쓴다. 특정포를 쓰고있는 프로세스의 정보를 출력
IDLE_PID=$(lsof -ti tcp:${IDLE_PORT})

if [ -z ${IDLE_PID} ]
then
  echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다."
else
  echo "> kill -15 $IDLE_PID"
  kill -15 ${IDLE_PID}
  sleep 5
fi