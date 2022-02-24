package netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import util.LoggerUtil;

import java.io.UnsupportedEncodingException;

@ChannelHandler.Sharable
public class NettyEchoClientHandler2 extends ChannelInboundHandlerAdapter {

    public static final NettyEchoClientHandler2 NettyEchoHandler = new NettyEchoClientHandler2();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException {



        String in = (String) msg;

        LoggerUtil.info("client received: " + in);


    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        LoggerUtil.info("通道注册完成:");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        LoggerUtil.info("通道激活完成:");

    }
}
