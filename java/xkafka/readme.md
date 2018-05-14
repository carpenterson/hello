
# kafka demo

## 运行kafka

```bat
E:\tools\kafka_2.11-1.1.0>bin\windows\zookeeper-server-start.bat config/zookeepe
r.properties
E:\tools\kafka_2.11-1.1.0>bin\windows\kafka-server-start.bat config\server.prope
rties
```

## consumer控制
1. 默认自动维护offset
2. 自己维护offset，调用poll和commit。精确控制什么情况下一条消息才算被消费
3. 在体外存储offset

## 参考链接

* [consumerapi](https://kafka.apache.org/documentation/#consumerapi)
* [consumerapi](https://kafka.apache.org/11/javadoc/index.html?org/apache/kafka/clients/consumer/KafkaConsumer.html)

