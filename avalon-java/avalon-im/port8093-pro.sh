#chmod a+x **.sh
#Description: 启动重启server服务
#端口号，根据此端口号确定PID
PORT=8093
APP_NAME='avalon-im.jar'
ACTIVE='pro,im-pro'
#查询出监听了PORT端口TCP协议的程序
pid=`ps -ef|grep $APP_NAME|grep -v grep|awk '{print $2}' `
#获取当前脚本相对路径
HOME=`cd $(dirname $0); pwd`

start(){
   if [ -n "$pid" ]; then
      echo "server already start,pid:$pid"
      return 0
   fi
   #进入命令所在目录
   cd $HOME
   nohup java -jar $HOME/$APP_NAME  --server.port=$PORT  --spring.profiles.active=$ACTIVE  > /dev/null 2>&1 &   #启动命令 最后一段是去掉nohup日志
   echo "start at port:$PORT"
}

stop(){
   if [ -z "$pid" ]; then
      echo "not find program on port:$PORT"
      return 0
   fi
   #结束程序，使用讯号2，如果不行可以尝试讯号9强制结束
   kill -9 $pid
   echo "kill program use signal 2,pid:$pid"
}
status(){
   if [ -z "$pid" ]; then
      echo "not find program on port:$PORT"
   else
      echo "program is running,pid:$pid"
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
      $0 stop
      sleep 2
      $0 start
    ;;
   status)
      status
   ;;
   *)
      echo "Usage: {start|stop|status|restart}"
   ;;
esac

exit 0