package netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import util.LoggerUtil;

import java.io.UnsupportedEncodingException;
@ChannelHandler.Sharable
public class NettyEchoHandler extends ChannelInboundHandlerAdapter {

    public static final NettyEchoHandler NettyEchoHandler = new NettyEchoHandler();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException {


        //LoggerUtil.info(this);

        ByteBuf in = (ByteBuf) msg;
        LoggerUtil.info("msg type: " + (in.hasArray()?"堆内存":"直接内存"));

        int len = in.readableBytes();
        byte[] arr = new byte[len];
        in.getBytes(0, arr);
        LoggerUtil.info("client received: " + new String(arr, "UTF-8"));


    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        LoggerUtil.info("channelRegistered被调用");
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        LoggerUtil.info("channelRegistered被调用");
        super.channelUnregistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LoggerUtil.info("channelActive被调用");

        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        LoggerUtil.info("channelInactive被调用");

        super.channelInactive(ctx);
    }



    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        LoggerUtil.info("channelReadComplete被调用");

        super.channelReadComplete(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        LoggerUtil.info("userEventTriggered被调用");

        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {

        LoggerUtil.info("channelWritabilityChanged被调用");

        super.channelWritabilityChanged(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LoggerUtil.info("exceptionCaught被调用");

        super.exceptionCaught(ctx, cause);
    }
}
