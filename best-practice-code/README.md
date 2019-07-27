
[Using the Maven Assembly Plugin to build a ZIP distribution](https://medium.com/@kasunpdh/using-the-maven-assembly-plugin-to-build-a-zip-distribution-5cbca2a3b052)

## Maven and Test
```bash
$ mvn clean package
$ scp target/best-practice-code-1.0.zip root@192.168.100.131:/root
```

## Kafka Operation
1. Start zookeeper and kafka or stop or clean data
```bash
$ ./bin/zookeeper-server-start.sh -daemon ./config/zookeeper.properties
$ ./bin/kafka-server-start.sh -daemon ./config/server.properties
$ kill -9 $(jps -l | grep -E "(kafka.Kafka|org.apache.zookeeper.server.quorum.QuorumPeerMain)" | awk '{print $1}')
$ rm -rf /tmp/kafka-logs/ /tmp/zookeeper/
```
2. Create topic
```bash
$ kafka-topics.sh --zookeeper localhost:2181 --create --topic test --partitions 3 --replication-factor 1
```
3. Describe topic
```bash
$ kafka-topics.sh --zookeeper localhost:2181 --describe --topic test
Topic:test	PartitionCount:3	ReplicationFactor:1	Configs:
	Topic: test	Partition: 0	Leader: 0	Replicas: 0	Isr: 0
	Topic: test	Partition: 1	Leader: 0	Replicas: 0	Isr: 0
	Topic: test	Partition: 2	Leader: 0	Replicas: 0	Isr: 0
```

4. List topics
```bash
$ kafka-topics.sh --zookeeper localhost:2181 --list
```

5. Check topic data
```bash
$ kafka-console-consumer.sh --bootstrap-server localhost:9092 --from-beginning --topic test
```


## Netty
http://tutorials.jenkov.com/netty/netty-channelpipeline.html
https://www.baeldung.com/netty

Bootstrap
EventLoopGroup
EventLoop
SocketChannel(ChannelInitializer)
ChannelPipeline
ChannelHandler

ByteBuf
ByteBufAllocator



pip install face_recognition