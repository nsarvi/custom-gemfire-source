# Spring cloud stream - Gemfire Source

This is a Spring cloud stream application to listen for the region events such as create and update, publishes the data changes to configured topic

# To build

maven clean package

# To Run

java -jar spring-cloud-starter-stream-source-gemfire-0.0.1-SNAPSHOT.jar --gemfire.pool.connect-type=locator --gemfire.pool.host-addresses=localhost:10334 --gemfire.pool.subscription-enabled=true --gemfire.region.region-name=employee  --server.port=8081 --debug --spring.cloud.stream.bindings.output.destination=employeetopic

