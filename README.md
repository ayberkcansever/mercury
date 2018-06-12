# Mercury
Mercury is highly-scalable and distributed socket messaging platform based on Netty as socket framework, Apache Ignite as caching platform and Google Protocol Buffers as remote procedure calls.

# When to use Mercury?
While developing any kind of messaging applications (IoT device-to-device or device-to-server messaging, online game server, instant messaging mobile/web etc.), we build a socket based server application as an orientation platform. When system load increases we have to add new server nodes, then we have a new problem to deal with: **Which client is connected to which server?**

To overcome this problem, we implement a distributed cache solution for knowing which client is connected which node. After implementing the cache solution, we know the presence of the clients but this time a new problem occurs: **The client-to-client messages must be transferred from server node to server node if the clients are connected to different server nodes.**  Then we choose a server-to-server communication platform to deal with this problem and implement it.

Finally, if you do not want to solve these problems again and again, you can add **Mercury** to your project and have a prepared highly-scalable and distributed messaging platform for your new application. When a new Mercury node starts to run, it automatically connects to the Mercury cluster and starts to accept client connections and gets ready to accept messages from clients connected and forward to the clients connected to other Mercury nodes.

The figures below show the single vs multi node architecture.

### Easy:
<img src="https://preview.ibb.co/mCT3Ud/Screen_Shot_2018_06_12_at_16_14_22.png" width="500" height="300">

### Complex:
<img src="https://preview.ibb.co/eZp7Ny/Screen_Shot_2018_06_12_at_16_14_14.png" width="800" height="500">

# How to use Mercury?

It is enough to examine the demo project to have the knowledge but if it is so necessary let's deep dive to the code:

At the startup of the application it is enough to initialize Mercury with the appropriate configuration.

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

Configure node's gRPC host and port, configure socket server port, set Client implementation class which is the socket client and ThreadPoolTaskExecutor for message sending threads.

There are some events posted by Mercury to the main application. Main application may catch these events if the listeners are registered. Available listeners are:

1. IOEventListener
2. ClientEventListener
3. MessageEventListener

These listeners can be registered after the Mercury initialization:

```java
    mercury.getEventBus().register(new SomeIOEventListener());
    mercury.getEventBus().register(new SomeClientEventListener());
    mercury.getEventBus().register(new SomeMessageEventListener());
```

Now we have a Mercury node which is ready to be a piece of a Mercury cluster and a client can connect to the node through the server socket. Mercury **does not** care the protocol you implement, it only delivers the messages. So you are free to implement your own messaging protocol.

The connected clients must be identified for receiving messages. When a client connects to a Mercury node, a random unique id is assigned to this client but it is not identified yet. It must prove its identity through the main application. The main application can apply a login mechanism or some other mechanisms to identify the client. It is up to the main application and the messaging protocol it implements. But the important thing is to call **identify** method of the Client class after the successful identification. For example, let's say that the client sends a message **id:Alice** for identifying itself after connecting to the socket, our Client will be:

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

Now this client is Alice and any other clients can send messages to Alice. 
**MercuryClient** has a **route(String to, String message)** method to send messages to the target clients. It is straightforward to send messages after deciding details of the messaging protocol that main application implements.

## Important
If your application is a Spring boot application, you do not need to annotate your main class **(@SpringBootApplication)** and do not need to run Spring application **(SpringApplication.run)** if you initialized Mercury, because Mercury is a Spring boot application and initialize the application context for you :P

