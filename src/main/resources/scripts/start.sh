BASEDIR=$(dirname "$PWD")
logPath=${BASEDIR}/log
confPath=${BASEDIR}/conf
if [ ! -d ${logPath} ]; then
  mkdir ${logPath}
fi
MAINCLASS="info.shenc.aliyunddnsrefresher.AliyunDdnsRefresherApplication"
CLASSPATH=.:$BASEDIR/conf:$(ls $BASEDIR/lib/*.jar |tr '\n' :)
SERVERPARAMS="-server -cp $CLASSPATH"
echo "start refresher..."
nohup java -server -cp ".:$BASEDIR/conf:$BASEDIR/lib/*" -DlogPath=$logPath $MAINCLASS >> ${logPath}/consoleOut.log