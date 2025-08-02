#!/bin/bash

# 定义变量
APP_NAME="avalon-im.jar"
PORT="8093"
ACTIVE="pro,im-pro"
LOG_DIR="./logs"
LOG_FILE="$LOG_DIR/app.log"

# 创建日志目录
mkdir -p $LOG_DIR

# 获取进程ID
pid=`ps -ef|grep $APP_NAME|grep -v grep|awk '{print $2}'`

# 获取当前脚本相对路径
HOME=`cd $(dirname $0); pwd`

# 日志函数
log() {
    echo "[$(date '+%Y-%m-%d %H:%M:%S')] $1" | tee -a $LOG_FILE
}

start(){
   if [ -n "$pid" ]; then
      log "server already start,pid:$pid"
      return 0
   fi

   # 检查jar文件是否存在
   if [ ! -f "$HOME/$APP_NAME" ]; then
      log "ERROR: $APP_NAME not found in $HOME"
      return 1
   fi

   # 进入命令所在目录
   cd $HOME

   # 启动应用
   nohup java \
      --add-opens java.base/java.lang=ALL-UNNAMED \
      --add-opens java.base/java.io=ALL-UNNAMED \
      --add-opens java.base/java.util=ALL-UNNAMED \
      -Xms512m \
      -Xmx1024m \
      -jar $HOME/$APP_NAME \
      --server.port=$PORT \
      --spring.profiles.active=$ACTIVE \
      > $LOG_FILE 2>&1 &

   # 等待启动
   sleep 3

   # 检查是否启动成功
   new_pid=`ps -ef|grep $APP_NAME|grep -v grep|awk '{print $2}'`
   if [ -n "$new_pid" ]; then
      log "start success at port:$PORT, pid:$new_pid"
   else
      log "start failed, please check log: $LOG_FILE"
      return 1
   fi
}

stop(){
   if [ -z "$pid" ]; then
      log "not find program on port:$PORT"
      return 0
   fi

   # 先尝试优雅停止
   kill $pid
   sleep 5

   # 检查是否还在运行
   if ps -p $pid > /dev/null 2>&1; then
      log "force kill program,pid:$pid"
      kill -9 $pid
   else
      log "program stopped gracefully,pid:$pid"
   fi
}

status(){
   if [ -z "$pid" ]; then
      log "not find program on port:$PORT"
      return 1
   else
      log "program is running,pid:$pid"
      return 0
   fi
}

case $1 in
   start)
      start
      ;;
   stop)
      stop
      ;;
   restart)
      log "restarting application..."
      $0 stop
      sleep 2
      $0 start
      ;;
   status)
      status
      ;;
   logs)
      tail -f $LOG_FILE
      ;;
   *)
      echo "Usage: {start|stop|status|restart|logs}"
      ;;
esac

exit 0