# JVM GC参数设置

## 是否需要修改JVM参数

是否需要修改jvm参数，取决于是否遇到如下情况
* gc的时间过长
* 内存溢出（OutOfMemory, OOM)

```shell
# 1. 用jstat -gcutil查看年轻代、老年代、Metaspace的使用率，FullGC和年轻代GC的次数和时间
jstat -gcutil 1205

S0     S1     E      O      M     CCS    YGC     YGCT    FGC    FGCT     GCT   
0.00   3.51   5.85  10.59  98.03  94.57     25    1.167     2    0.212    1.379

E是Eden区的使用率
O是老年区的使用率
M是Metaspace的使用率
YGC年轻代GC的次数
YGCT年轻代GC的时间
FGC FullGC的次数，这里统计的是Stop the World（STW）的时间，每次CMS会有两次STW——初始标记和重新标记。
FGCT FullGC的时间

# 2. 用jstat -gccapacity查看年轻代、老年代、Metaspace的容量
NGCMN    NGCMX     NGC     S0C   S1C       EC      OGCMN      OGCMX       OGC         OC       MCMN     MCMX      MC     CCSMN    CCSMX     CCSC    YGC    FGC 
1048576.0 1048576.0 1048576.0 104832.0 104832.0 838912.0  2097152.0  2097152.0  2097152.0  2097152.0      0.0 1099776.0  55888.0      0.0 1048576.0   6256.0     25     2

NGC 当前年轻代的容量
OGC 当前老年代的容量。OGC是old generation capacity, OC是old space capacity，可能一般情况下两者数值一样。
MC Metaspace的容量

# 3. 也可以用jmap -heap来查看年轻代、老年代、Metaspace的容量
```

## jdk8 JVM参数举例

* 使用CMS，设置每次FullGC和remark之前先进行一次年轻代的回收
* 打印GC日志，避免重启程序后覆盖gc日志，日志名中加上进程id(%p)
* 堆内存大小，老年代设置为年轻代的2倍

```shell
JAVA_OPTS="-Duser.timezone=GMT+8 \
-Djava.net.preferIPv4Stack=true \
-Dfile.encoding=utf-8 \
-XX:+UseParNewGC \
-XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled \
-XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 \
-XX:+ScavengeBeforeFullGC -XX:+CMSScavengeBeforeRemark \
-XX:+CMSClassUnloadingEnabled \
-XX:MaxTenuringThreshold=6 \
-XX:+PrintGCDetails -XX:+PrintGC -XX:+PrintGCDateStamps \
-Xloggc:/my/log/gc/gc-%p.log \
-XX:+UseGCLogFileRotation -XX:GCLogFileSize=100M -XX:NumberOfGCLogFiles=10 \
-XX:+PrintGCApplicationStoppedTime -XX:+PrintGCApplicationConcurrentTime \
-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/my/log/gc/%p.hprof \
-XX:MaxMetaspaceSize=256m -Xms3g -Xmx3g -Xmn1g "
```

## 参考资料

建议的jdk参数
https://blog.sokolenko.me/2014/11/javavm-options-production.html

```txt
Java >= 8
    -server
    -Xms<heap size>[g|m|k] -Xmx<heap size>[g|m|k]
    -XX:MaxMetaspaceSize=<metaspace size>[g|m|k]
    -Xmn<young size>[g|m|k]
    -XX:SurvivorRatio=<ratio>
    -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled
    -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=<percent>
    -XX:+ScavengeBeforeFullGC -XX:+CMSScavengeBeforeRemark
    -XX:+PrintGCDateStamps -verbose:gc -XX:+PrintGCDetails -Xloggc:"<path to log>"
    -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=10 -XX:GCLogFileSize=100M
    -Dsun.net.inetaddr.ttl=<TTL in seconds>
    -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=<path to dump>`date`.hprof
    -Djava.rmi.server.hostname=<external IP>
    -Dcom.sun.management.jmxremote.port=<port> 
    -Dcom.sun.management.jmxremote.authenticate=false 
    -Dcom.sun.management.jmxremote.ssl=false
```

java8各个参数介绍
https://docs.oracle.com/javase/8/docs/technotes/tools/windows/java.html

java8 jstat
https://docs.oracle.com/javase/8/docs/technotes/tools/unix/jstat.html

java8 jstack
https://docs.oracle.com/javase/8/docs/technotes/tools/unix/jstack.html

java8 GC调优指导
https://docs.oracle.com/javase/8/docs/technotes/guides/vm/gctuning/index.html
