package netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.ByteToMessageDecoder;
import util.LoggerUtil;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@ChannelHandler.Sharable
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    public static final NettyServerHandler NettyEchoHandler = new NettyServerHandler();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        String in = (String) msg;

        LoggerUtil.info("server received: " + new String(in));


        //写回数据，异步任务
        //LoggerUtil.info("写回前，msg.refCnt:" + ((ByteBuf) msg).refCnt());
        /*ByteBuf buf =   UnpooledByteBufAllocator.DEFAULT.buffer();

        buf.writeBytes(in.getBytes(StandardCharsets.UTF_8));*/

        ChannelFuture f = ctx.writeAndFlush(in);
        f.addListener((ChannelFuture futureListener) -> {
            LoggerUtil.info( "--------------------"+(in));
        });
    }

    /*@Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        String in = (String) in;

        LoggerUtil.info("server received: " + new String(in));


        //写回数据，异步任务
        //LoggerUtil.info("写回前，msg.refCnt:" + ((ByteBuf) msg).refCnt());
        ByteBuf buf =   UnpooledByteBufAllocator.DEFAULT.buffer();

        buf.writeBytes(in.getBytes(StandardCharsets.UTF_8));

        ChannelFuture f = ctx.writeAndFlush(buf);
        f.addListener((ChannelFuture futureListener) -> {
            LoggerUtil.info( "--------------------"+(in));
        });
    }*/

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
