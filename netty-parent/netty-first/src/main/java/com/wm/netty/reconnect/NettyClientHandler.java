package com.wm.netty.reconnect;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author wangm
 * @title: NettyClientHandler
 * @projectName netty-parent
 * @description: TODO
 * @date 2021/6/2220:56
 */
public class NettyClientHandler extends SimpleChannelInboundHandler<String> {
    private NettyClient nettyClient = new NettyClient("localhost", 9000);
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {
        System.out.println(msg.trim());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        nettyClient.connect();
    }
}
