# POC of Artel new Architecture

- Kafka
- ElasticSearch
- Micro-services


## Run

### Kafka
- Start ZooKeeper : `bin/zookeeper-server-start.sh config/zookeeper.properties`
- Start Kafka broker(s): `bin/kafka-server-start.sh config/server.properties`

### ElasticSearch
Start ES: `docker run --name es -v /tmp/es-arval:/usr/share/elasticsearch/data -d -p 9200:9200 -p 9300:9300  -e ES_JAVA_OPTS="-Xms1g -Xmx1g" elasticsearch:2.4.1`
           
### Start Apps
- Acq (9901): `spring-boot:run -pl artel-acq`
- Trip #0 (9902): `spring-boot:run -pl artel-trip -Drun.arguments=--kafka.partitionId=0,--server.port=9902`
- Trip #1 (9903): `spring-boot:run -pl artel-trip -Drun.arguments=--kafka.partitionId=1,--server.port=9903`
- ES (9905): `spring-boot:run -pl artel-es`




