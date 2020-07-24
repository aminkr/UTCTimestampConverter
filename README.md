# Debezium MYSQL UTCTimestamp Converter  
Currently debezium converts all of timestamp fields into UTC and sometimes we need local timestamp in upstream tools (Apache Hive in our cases). 

## Make Package
```
mvn package -Dmaven.test.skip=true
```

## Usage
in docker file, just copy library into mysql path  
```
COPY UTCTimestampConverter-1.0.0-SNAPSHOT.jar /kafka/connect/debezium-connector-mysql/
```
### Configuration
```json
"converters": "utcTimestampConverter",
"timestampConverter.type": "snapp.kafka.connect.util.UTCTimestampConverter"
```
