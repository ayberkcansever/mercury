package com.github.ayberkcansever.mercury.cache;

import com.github.ayberkcansever.mercury.grpc.client.ClientHolder;
import com.github.ayberkcansever.mercury.grpc.server.GRpcServer;
import com.github.ayberkcansever.mercury.util.StringUtil;
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

import javax.annotation.PostConstruct;


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
        ignite = Ignition.start("ignite-config.xml");
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

}
