# Mercury
Mercury is highly-scalable and distributed socket messaging platform based on Netty as socket framework, Apache Ignite as caching platform and Google Protocol Buffers as remote procedure calls.

# When to use Mercury?
While developing any kind of messaging applications (IoT device-to-device or device-to-server messaging, online gaming server, instant messaging mobile/web etc.), we build a socket based server application as an orientation platform. When load increases we have to add new server nodes to the system, then we have a new problem to deal with: **Which client is connected to which server?**

To overcome this problem, we implement a distributed cache solution. After this implementation, we know the presence of the clients anymore but this time a new problem occurs: **The client-to-client messages must be transferred from one server node to other if the clients are connected to different nodes.**  Then we choose a server-to-server communication platform to deal with this problem and implement it.

Finally, if you do not want to solve these problems again and again, you can add **Mercury** to your project and have a prepared, highly-scalable and distributed messaging platform for your new application. 

When a new Mercury node starts to run, it automatically connects to the Mercury cluster, starts to accept client connections, gets ready to accept messages from clients connected and forward to the clients connected to other Mercury nodes.

The figures below show the single vs multi node socket messaging systems.

### Easy:
<img src="https://preview.ibb.co/mCT3Ud/Screen_Shot_2018_06_12_at_16_14_22.png" width="500" height="300">

### Complex (Mercury Cluster):
<img src="https://preview.ibb.co/euucqJ/Screen_Shot_2018_06_13_at_14_04_11.png" width="800" height="500">

# How to use Mercury?

Add dependency to your pom.xml:
```xml
<dependency>
  <groupId>com.github.ayberkcansever</groupId>
  <artifactId>mercury</artifactId>
  <version>1.0.0</version>
</dependency>
```

It is enough to examine the demo project to have knowledge about Mercury but let's deep dive to the code a little bit:

At startup of your main application it is enough to initialize Mercury with the appropriate configuration.

```java
  MercuryConfig mercuryConfig = new MercuryConfig.MercuryConfigBuilder()
				.setGRpcIp("127.0.0.1")
				.setGRpcPort(6666)
				.setServerPort(5555)
				.setClientClass(Client.class)
				.setMessageThreadPoolTaskExecutor(executor)
				.build();
  Mercury mercury = new Mercury().init(mercuryConfig);
```

Configure Mercury node's gRPC host and port, configure socket server port, set Client implementation class which is your application's socket client and set ThreadPoolTaskExecutor for message sending threads.

There are some events posted by Mercury to the main application. Main application may want to catch these events, if so the appropriate listeners must be registered. 

Available listeners are:

1. IOEventListener
2. ClientEventListener
3. MessageEventListener

These listeners can be registered after Mercury initialization:

```java
    mercury.getEventBus().register(new SomeIOEventListener());
    mercury.getEventBus().register(new SomeClientEventListener());
    mercury.getEventBus().register(new SomeMessageEventListener());
```

Now we have a Mercury node which is ready to be a piece of a Mercury cluster. A client can connect to this node through the server socket. Mercury **does not** care the messaging protocol you implement, it only delivers the string messages. **So you are free to implement your own messaging protocol based on JSON or XML or use XMPP, LEMP etc.**

After a client connects to a node, it must be identified for receiving messages. After the connection a random unique id is assigned to this client but it is not really identified yet. It must prove its identity through the main application. The main application can apply a login mechanism or some other mechanisms to identify the client. It is up to the main application and the messaging protocol it implements. But the important thing is to call **identify** method of the Client class after successful identification. 

For example, let's say that the client sends a message **id:Alice** for identifying itself after connecting to the socket, our Client code should be:

```java

public class Client extends MercuryClient {
    @Override
    protected void handleMessage(String message) {
        if(message.startsWith("id:")) {
            identify(message.split("id:")[1].trim());
        } 
    }
}
```

You see that **identify** method is called if message starts with **id:** Now this client is Alice and any other clients can send messages to Alice. 

**MercuryClient** has a **route(String to, String message)** method to send messages to the target clients. It is straightforward to send messages after deciding details of the messaging protocol that main application implements. **demo** project includes the example codes.

## Important
If your application is a Spring boot application, you do not need to annotate your main class **(@SpringBootApplication)** and do not need to run Spring application **(SpringApplication.run)** if you initialize Mercury because Mercury is a Spring boot application and initialize the application context for you :P

