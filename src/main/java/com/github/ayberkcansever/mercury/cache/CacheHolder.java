package com.github.ayberkcansever.mercury.cache;

import com.github.ayberkcansever.mercury.Mercury;
import com.github.ayberkcansever.mercury.grpc.client.ClientHolder;
import com.github.ayberkcansever.mercury.grpc.client.GRpcClient;
import com.github.ayberkcansever.mercury.grpc.server.GRpcServer;
import com.github.ayberkcansever.mercury.utils.StringUtil;
import lombok.Getter;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteException;
import org.apache.ignite.Ignition;
import org.apache.ignite.events.DiscoveryEvent;
import org.apache.ignite.events.EventType;
import org.apache.ignite.lang.IgnitePredicate;
import org.apache.ignite.lifecycle.LifecycleBean;
import org.apache.ignite.lifecycle.LifecycleEventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

@Component
public class CacheHolder implements LifecycleBean {

    @Getter private Ignite ignite;
    @Getter private IgniteCache<String, String> presenceCache;

    @Autowired
    private ClientHolder clientHolder;

    @Autowired
    private GRpcServer gRpcServer;

    @PostConstruct
    public void init() {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("ignite-config.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(is);
            XPath xPath = XPathFactory.newInstance().newXPath();

            Node gRpcIpNode = (Node) xPath.compile("/beans/bean/property[@name='userAttributes']/map/entry[@key='grpcIp']")
                    .evaluate(doc, XPathConstants.NODE);
            gRpcIpNode.getAttributes().getNamedItem("value").setNodeValue(Mercury.getMercuryConfig().getGRpcIp());

            Node gRpcPortNode = (Node) xPath.compile("/beans/bean/property[@name='userAttributes']/map/entry[@key='grpcPort']")
                    .evaluate(doc, XPathConstants.NODE);
            gRpcPortNode.getAttributes().getNamedItem("value").setNodeValue(String.valueOf(Mercury.getMercuryConfig().getGRpcPort()));

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Source xmlSource = new DOMSource(doc);
            Result outputTarget = new StreamResult(outputStream);
            TransformerFactory.newInstance().newTransformer().transform(xmlSource, outputTarget);
            ignite = Ignition.start(new ByteArrayInputStream(outputStream.toByteArray()));

        } catch (Exception e) {
            e.printStackTrace();
        }

        //ignite = Ignition.start("ignite-config.xml");
        gRpcServer.init();

        IgnitePredicate<DiscoveryEvent> nodeJoinedListener = evt -> {
            String serverUrl = StringUtil.getServerUrl(evt.eventNode().attribute(GRpcServer.GRPC_IP_USER_ATTRIBUTE).toString(),
                                                       evt.eventNode().attribute(GRpcServer.GRPC_PORT_USER_ATTRIBUTE).toString());
            clientHolder.addNewNode(serverUrl);
            return true;
        };
        ignite.events().localListen(nodeJoinedListener, EventType.EVT_NODE_JOINED);

        IgnitePredicate<DiscoveryEvent> nodeLeftListener = evt -> {
            String serverUrl = StringUtil.getServerUrl(evt.eventNode().attribute(GRpcServer.GRPC_IP_USER_ATTRIBUTE).toString(),
                    evt.eventNode().attribute(GRpcServer.GRPC_PORT_USER_ATTRIBUTE).toString());
            clientHolder.removeNode(serverUrl);
            return true;
        };
        ignite.events().localListen(nodeLeftListener, EventType.EVT_NODE_LEFT);

        ignite.cluster().nodes().stream().forEach(node -> {
            String serverUrl = StringUtil.getServerUrl(node.attribute(GRpcServer.GRPC_IP_USER_ATTRIBUTE).toString(),
                    node.attribute(GRpcServer.GRPC_PORT_USER_ATTRIBUTE).toString());
            if(!gRpcServer.getLocalGRpcServerUrl().equals(serverUrl)) {
                clientHolder.addNewNode(serverUrl);
            }
        });

        presenceCache = ignite.getOrCreateCache("presenceCache");

    }

    @Override
    public void onLifecycleEvent(LifecycleEventType lifecycleEventType) throws IgniteException {
        if (lifecycleEventType == LifecycleEventType.BEFORE_NODE_START) {
            // todo:
        } else if (lifecycleEventType == LifecycleEventType.AFTER_NODE_START) {
            // todo:
        }
    }

    public String getIgniteConfigurationUserAttribute(String key) {
        return ignite.configuration().getUserAttributes().get(key).toString();
    }

    public GRpcClient getGRpcClient(String serverUrl) {
        return clientHolder.getNode(serverUrl);
    }

}
