package com.github.cuinipeng.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public class NettyTCPServer {
    private String host;
    private int port;

    public NettyTCPServer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() {
        /**
         * To create a Netty TCP server:
         *  1. Create an EventLoopGroup
         *  2. Create and configure a ServerBootstrap
         *  3. Create a ChannelInitializer
         *  4. Start the server
         */
        EventLoopGroup bossGroup = new NioEventLoopGroup();                 // EventLoopGroup
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();            // ServerBootstrap
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .localAddress(new InetSocketAddress(host, port))
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {     // ChannelInitializer
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new HelloServerHandler());
                    }
                });

        try {
            ChannelFuture channelFuture = serverBootstrap.bind().sync();
            channelFuture.channel().closeFuture().sync();
            workerGroup.shutdownGracefully().sync();
            bossGroup.shutdownGracefully().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
