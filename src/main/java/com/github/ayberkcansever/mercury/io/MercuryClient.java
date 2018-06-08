package com.github.ayberkcansever.mercury.io;

import com.github.ayberkcansever.mercury.Mercury;
import com.github.ayberkcansever.mercury.io.event.IOEvent;
import com.github.ayberkcansever.mercury.io.event.IOEventType;
import com.github.ayberkcansever.mercury.utils.StringUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import lombok.Getter;

public abstract class MercuryClient extends ChannelInboundHandlerAdapter {

    @Getter private ChannelHandlerContext ctx;
    @Getter private String id;

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelRegistered();
        this.ctx = ctx;
        this.id = StringUtil.generateId();
        Mercury.instance().getEventBus().postEvent(new IOEvent(this, IOEventType.CLIENT_CONNECTED));
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelUnregistered();
        Mercury.instance().getEventBus().postEvent(new IOEvent(this, IOEventType.CLIENT_DISCONNECTED));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        handleMessage(((ByteBuf) msg).toString(CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    public void send(String message) {
        ctx.writeAndFlush(Unpooled.wrappedBuffer(message.getBytes(CharsetUtil.UTF_8)));
    }

    protected abstract void handleMessage(String message);

}
