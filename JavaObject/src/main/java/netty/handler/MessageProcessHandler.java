package netty.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import util.LoggerUtil;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

@ChannelHandler.Sharable
public class MessageProcessHandler extends ChannelInboundHandlerAdapter {

    public static final MessageProcessHandler MessageProcessHandler = new MessageProcessHandler();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException {

        LoggerUtil.info("decoder的mesg:"+msg);


        ChannelFuture f = ctx.writeAndFlush(Unpooled.buffer().writeBytes(new String(msg.toString()).getBytes(StandardCharsets.UTF_8)));
        f.addListener((ChannelFuture cf)->{
            LoggerUtil.info("------------------");
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
