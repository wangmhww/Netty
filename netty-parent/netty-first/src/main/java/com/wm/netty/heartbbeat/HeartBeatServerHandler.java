package com.wm.netty.heartbbeat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @author wangm
 * @title: HeartBeatServerHandler
 * @projectName netty-parent
 * @description: TODO
 * @date 2021/6/2723:19
 */
public class HeartBeatServerHandler extends SimpleChannelInboundHandler<String> {

    private int readIdleTimes = 0;

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        IdleStateEvent event = (IdleStateEvent) evt;

        String eventType = null;
        switch (event.state()) {
            case READER_IDLE:
                eventType = "读空闲";
                readIdleTimes++; // 读空闲的计数器加1
                break;
            case WRITER_IDLE:
                eventType = "写空闲";
                break;
            case ALL_IDLE:
                eventType = "读写空闲";
                break;
        }
        System.out.println(ctx.channel().remoteAddress() + "超时事件：" + eventType);
        if (readIdleTimes > 3) {
            System.out.println("[Server] 读空闲超过3次，关闭连接，释放更多资源");
            ctx.channel().writeAndFlush("idle close");
            ctx.channel().close();
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {
        System.out.println("=========== > [server] message received:" + msg);

        if("Heartbeat Packet".equals(msg)) {
            channelHandlerContext.writeAndFlush("ok");

        } else {
            System.out.println("其他信息处理。。。。。。");
        }
    }

}
