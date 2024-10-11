
### 解决方案

1. 查询WLS2 Ubuntu的IP

`ifconfig eth0 | grep inet | grep -v inet6 | awk '{print $2}'`

输出：172.20.50.88

2. 配置Kafka config/server.properties

```shell
listeners = PLAINTEXT://172.20.50.88:9092
advertised.listeners=PLAINTEXT://172.20.50.88:9092
```


3. 重启Kafka Server

`bin/kafka-server-start.sh config/server.properties`
