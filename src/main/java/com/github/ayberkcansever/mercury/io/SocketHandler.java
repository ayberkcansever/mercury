package com.github.ayberkcansever.mercury.io;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class SocketHandler extends ChannelInboundHandlerAdapter {

    private ChannelHandlerContext ctx;

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelRegistered();
        this.ctx = ctx;
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelUnregistered();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf inBuffer = (ByteBuf) msg;
        String received = inBuffer.toString(CharsetUtil.UTF_8);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    public void send(String message) {
        ctx.write(Unpooled.wrappedBuffer(message.getBytes(CharsetUtil.UTF_8)));
        ctx.flush();
    }

}
