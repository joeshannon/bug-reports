* Import projects
* Set target platform (my.target)
* Run the producer product
* Run the consumer product
* Note the errors from the consumer:
  ```
  19:36:22,231 |-INFO in ch.qos.logback.classic.net.SocketReceiver@51e5b755 - receiver localhost:6750: unknown event class: java.lang.ClassNotFoundException: ch.qos.logback.classic.spi.LoggingEventVO cannot be found by ch.qos.logback.core_1.2.3.v20200428-2012
  ```
  and the producer:
  ```
  19:36:24,222 |-INFO in ch.qos.logback.core.net.server.RemoteReceiverStreamClient@12cb6166 - client 127.0.0.1:52756: java.net.SocketException: Broken pipe (Write failed)
  ```