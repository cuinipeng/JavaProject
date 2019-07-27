package com.github.cuinipeng.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

public class NettyTCPClient {
    private String host;
    private int port;

    public NettyTCPClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() {
        /**
         * To create a Netty TCP client:
         *  1. Create an EventLoopGroup
         *  2. Create and configure a Bootstrap
         *  3. Create a ChannelInitializer
         *  4. Start the client
         */
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .remoteAddress(new InetSocketAddress(host, port))
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new HelloClientHandler());
                    }
                });

        try {
            ChannelFuture channelFuture = bootstrap.connect().sync();
            channelFuture.channel().closeFuture().sync();
            group.shutdownGracefully().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
