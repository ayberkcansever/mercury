# Mercury
Mercury is highly-scalable, distributed socket messaging platform based on Netty as socket framework, Apache Ignite as caching platform and Google Protocol Buffers as remote procedure calls.

# When to use Mercury?
While developing any messaging applications (IoT device-to-device/device-to-server messaging, instant messaging mobile/web etc.), we build a socket based server application as an orientation platform. When system load increases we have to add new server nodes, then we have a new problem to deal with: **Which client is connected to which server?**

To overcome this problem, we implement a distributed cache solution for knowing which client is connected which node. After implementing the distributed cache solution we know the presence of the clients but this time a new problem occurs: **The client-to-client messages must be transferred from server node to server node if the clients are connected to different nodes.**  We choose a server-to-server communication platform to deal with this problem and implement it.

If you do not want to solve these problems again and again, you can add **Mercury** to your project and have a prepared highly-scalable, distributed messaging platform for your new application. When a new Mercury node starts to run, it automatically connects to the Mercury cluster and starts to accept client connections.

The figures below show the single vs multi node architecture.

### Easy:
<img src="https://preview.ibb.co/mCT3Ud/Screen_Shot_2018_06_12_at_16_14_22.png" width="500" height="300">

### Complex:
<img src="https://preview.ibb.co/eZp7Ny/Screen_Shot_2018_06_12_at_16_14_14.png" width="800" height="500">
