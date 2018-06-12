package com.github.ayberkcansever.mercury.demo.test;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import lombok.Getter;

import java.util.concurrent.atomic.AtomicInteger;

public class LoadClient {

    @Getter private int id;
    @Getter private String host;
    @Getter private int port;
    private Channel channel;

    public LoadClient(int id, String host, int port) {
        this.id = id;
        this.host = host;
        this.port = port;
        new Thread(() -> {
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            try {
                Bootstrap b = new Bootstrap();
                b.group(workerGroup);
                b.channel(NioSocketChannel.class);
                b.option(ChannelOption.SO_KEEPALIVE, true);
                b.handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.nulDelimiter()));
                        ch.pipeline().addLast(new StringDecoder(CharsetUtil.UTF_8));
                        ch.pipeline().addLast(new StringEncoder(CharsetUtil.UTF_8));
                        ch.pipeline().addLast(new LoadClientHandler(id));
                    }
                });

                // Start the client.
                ChannelFuture f = b.connect(host, port).sync(); // (5)
                this.channel = f.channel();

                // Wait until the connection is closed.
                f.channel().closeFuture().sync();

            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                workerGroup.shutdownGracefully();
            }
        }).start();

    }

    static class LoadClientHandler extends ChannelDuplexHandler {

        private AtomicInteger msgCount = new AtomicInteger(0);

        @Getter private int id;

        public LoadClientHandler(int id) {
            this.id = id;
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            int count = msgCount.incrementAndGet();
            if(count % 1000 == 0) {
                System.out.println(id + " " + count);
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            cause.printStackTrace();
            ctx.close();
        }

    }

    public void send(String message) {
        this.channel.writeAndFlush(message.concat("\r\n"));
    }

}
