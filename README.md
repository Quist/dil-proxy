# DIL Proxy

A prototype proxy to facilitate communication in DIL environments, developed as a part of a master thesis spring 2016.

The proxy currently supports GZIP compression and three configurable protocols for inter-proxy communication:
* HTTP/TCP
* CoAP
* AMQP

## Requirements
* Maven
* Java 8

## Build
Run the following from the project root directory:
`mvn clean install`


## Run instructions
The proxy is packaged as a JAR file and can be launched from the terminal:

``` $ java -jar target/proxy-1.0-SNAPSHOT.jar configs/configfile.conf ```

A valid configration file must be passed as an argument.

### Configuration file
The configuration fields specifies the configuration of the proxy.

#### Sample configuration file
``` 
proxy {
  targetProxyHostname = "192.168.10.10:4001"
  useCompression = false
  protocol = "amqp"
  hostname = "0.0.0.0"
  port = 3001
  timeout = 40000
}

amqp {
  brokerConnectionUri = "amqp://vetur:5672"
  produceQueue = 4001
  consumeQueue = 3001
}

```

