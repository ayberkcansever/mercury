package com.github.ayberkcansever.mercury.client;

import com.github.ayberkcansever.mercury.Mercury;
import com.github.ayberkcansever.mercury.client.event.ClientEvent;
import com.github.ayberkcansever.mercury.client.event.ClientEventType;
import com.github.ayberkcansever.mercury.io.event.IOEvent;
import com.github.ayberkcansever.mercury.io.event.IOEventType;
import com.github.ayberkcansever.mercury.utils.StringUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.Getter;

public abstract class MercuryClient extends ChannelInboundHandlerAdapter {

    @Getter private ChannelHandlerContext ctx;
    @Getter private String tempId;
    @Getter private String id;

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelRegistered();
        this.ctx = ctx;
        this.tempId = StringUtil.generateId();
        Mercury.instance().getEventBus().postEvent(new IOEvent(this, IOEventType.CLIENT_CONNECTED));
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelUnregistered();
        if(id != null) {
            MercuryClientHolder.removeClient(id);
            Mercury.instance().getCacheHolder().getPresenceCache().remove(id);
        }
        Mercury.instance().getEventBus().postEvent(new IOEvent(this, IOEventType.CLIENT_DISCONNECTED));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        handleMessage((String) msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    public void send(String message) {
        ctx.writeAndFlush(message);
    }

    public void identify(String id) {
        this.id = id;
        MercuryClientHolder.putClient(id, this);
        Mercury.instance().getCacheHolder().getPresenceCache().put(id, Mercury.instance().getGRpcServer().getLocalGRpcServerUrl());
        Mercury.instance().getEventBus().postEvent(new ClientEvent(this, ClientEventType.IDENTIFIED));
    }

    public void route(String to, String message) {
        Mercury.instance().routeMessage(this.id, to, message);
    }

    protected abstract void handleMessage(String message);

}
