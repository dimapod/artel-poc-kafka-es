# POC of Artel new Architecture

- Kafka
- ElasticSearch
- Micro-services


## Run

### Kafka
- Start ZooKeeper : `bin/zookeeper-server-start.sh config/zookeeper.properties`
- Start Kafka broker(s): `bin/kafka-server-start.sh config/server.properties`

### ElasticSearch
Start ES: `docker run --name elasticsearch -p 9200:9200 -p 9300:9300 -d elasticsearch`

### Start Apps
TBD




