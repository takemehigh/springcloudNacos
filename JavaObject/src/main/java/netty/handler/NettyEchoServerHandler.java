package netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import util.LoggerUtil;

import java.io.UnsupportedEncodingException;

@ChannelHandler.Sharable
public class NettyEchoServerHandler extends ChannelInboundHandlerAdapter {

    public static final NettyEchoServerHandler NettyEchoHandler = new NettyEchoServerHandler();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException {


        LoggerUtil.info(this);

        ByteBuf in = (ByteBuf) msg;
        LoggerUtil.info("msg type: " + (in.hasArray()?"堆内存":"直接内存"));

        int len = in.readableBytes();
        byte[] arr = new byte[len];
        in.getBytes(0, arr);
        LoggerUtil.info("server received: " + new String(arr, "UTF-8"));

        //写回数据，异步任务
        LoggerUtil.info("写回前，msg.refCnt:" + ((ByteBuf) msg).refCnt());
        ChannelFuture f = ctx.writeAndFlush(msg);
        f.addListener((ChannelFuture futureListener) -> {
            LoggerUtil.info("写回后，msg.refCnt:" + ((ByteBuf) msg).refCnt());
        });
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
