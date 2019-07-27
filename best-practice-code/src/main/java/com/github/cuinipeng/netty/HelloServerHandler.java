package com.github.cuinipeng.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloServerHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(HelloServerHandler.class);
    private ByteBuf buf;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("Channel Active");
        buf = ctx.alloc().buffer(8192);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("Channel Active");
        buf.release();
        buf = null;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // called whenever data is received from the SocketChannel
        ByteBuf inBuffer = (ByteBuf)msg;
        buf.writeBytes(inBuffer);

        // inBuffer.release();
        ReferenceCountUtil.release(msg);

        String received = buf.toString(CharsetUtil.UTF_8);
        logger.info("Server received: " + received);

        ChannelFuture channelFuture = ctx.writeAndFlush(
                Unpooled.copiedBuffer("Hello " + received, CharsetUtil.UTF_8));
        channelFuture.addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // called when there is no more data for read from the SocketChannel
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
