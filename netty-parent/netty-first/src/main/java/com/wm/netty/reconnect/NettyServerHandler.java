package com.wm.netty.reconnect;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;

/**
 * @author wangm
 * @title: NettyServerHandler
 * @projectName netty-parent
 * @description: TODO
 * @date 2021/6/2220:05
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<String> {

    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("[客户端]："+ channel.remoteAddress() + " 上线了");
        channelGroup.add(channel);
        System.out.println(channel.remoteAddress() + " 上线了！");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("[客户端]："+ channel.remoteAddress() + " 下线了");
        channelGroup.remove(channel);
        System.out.println(channel.remoteAddress() + " 下线了！");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {
        Channel ch = channelHandlerContext.channel();
        channelGroup.forEach(channel -> {
            if (channel != ch) {
                channel.writeAndFlush("[客户端]:" +  ch.remoteAddress() + "发送的消息" + msg + "_");
            } else {
                channel.writeAndFlush("[自己]:发送的消息" + msg + "_");
            }
        });

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

    }
}
