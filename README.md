## run-it - Remove compiler
#### Supported languages:
- C
- Java

## How it works?
Client makes HTTP request to create "submission", server generates unique identifier for the submission, saves initial state in Redis by generated id and publishes message to Kafka topic.
There are a number of "workers" that listen to Kafka topic and execute code and save result in Redis.
Client can track current status using other endpoint.

#### Advantage of this approach is scalability, its quiet easy to add additional workers to system so that more requests can be processed

TODO:
1. Add option to wait until submission is processed
