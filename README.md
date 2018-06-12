# Mercury
Mercury is highly-scalable, distributed socket messaging platform based on Netty as socket framework, Apache Ignite as caching platform and Google Protocol Buffers as remote procedure calls.

# When to use Mercury?
While developing any messaging applications (instant messaging mobile/web, IoT device-to-device/device-to-server etc.), we build a socket based server application as an orientation platform. When traffic increases we have to increase the server node count and then we have a new problem: **Which client is connected to which server?**

To overcome this problem, we implement a distributed cache solution for knowing which client is on which node. After implementing the distributed cache solution we know the presence of the clients but this time a new problem occurs: **The client-to-client messages must be transferred from server node to server node if the clients are connected to different nodes.**  

### Easy:
<img src="https://preview.ibb.co/mCT3Ud/Screen_Shot_2018_06_12_at_16_14_22.png" width="500" height="300">

### Complex:
<img src="https://preview.ibb.co/eZp7Ny/Screen_Shot_2018_06_12_at_16_14_14.png" width="800" height="500">
