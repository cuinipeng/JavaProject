package com.github.cuinipeng.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloClientHandler extends SimpleChannelInboundHandler {
    private static final Logger logger = LoggerFactory.getLogger(HelloClientHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty Rock!", CharsetUtil.UTF_8));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf inBuffer = (ByteBuf)msg;

        String received = inBuffer.toString(CharsetUtil.UTF_8);
        logger.info("Client received: " + received);

        ReferenceCountUtil.release(inBuffer);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
