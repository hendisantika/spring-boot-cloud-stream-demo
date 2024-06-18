# spring-boot-cloud-stream-demo

### Sample Application:

To demo this real time stream processing, Lets consider a simple application which contains 3 microservices.

**Producer**: This Microservice produces some data

* In the real world, Producer could be you browser/some user action sending movie surf history / credit card
  transactions etc.
* In this demo, I would be generating numbers sequentially from 1 to N, every second just to keep things simple to
  understand.

**Processor**: This Microservice consumes the data, does some processing on the data and writes back to another topic

* In the real world, this could be the movie recommendation engine for Netflix.
* In this demo, I would be skipping all the odd numbers and finding the square of the even numbers.

**Consumer**: This Microservice consumes the processed data.

* In the real world, this could be your browser again to get the latest recommendations based on your movie browsing.
* In this demo, I would consume the data and print it on the console.

Producer, Processor and Consumer are 3 different applications connected via 2 different Kafka topics as shown below.

