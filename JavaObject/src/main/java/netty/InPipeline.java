package netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;
import util.LoggerUtil;

public class InPipeline {

    static class InHandlerA extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            LoggerUtil.info("入站处理器 A: 被回调 ");
            super.channelRead(ctx, msg);
        }
    }

    static class InHandlerB extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            LoggerUtil.info("入站处理器 B: 被回调 ");
            //不调用super会中断
            super.channelRead(ctx, msg);
        }
    }

    static class InHandlerC extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            LoggerUtil.info("入站处理器 C: 被回调 ");

            super.channelRead(ctx, msg);
        }
    }

    static class OutHandlerA extends ChannelOutboundHandlerAdapter {
        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            LoggerUtil.info("出站处理器 A: 被回调 ");
            super.write(ctx, msg,promise);
        }

    }

    static class OutHandlerB extends ChannelOutboundHandlerAdapter {
        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            LoggerUtil.info("出站处理器 B: 被回调 ");
            LoggerUtil.info(ctx.pipeline());
            LoggerUtil.info(ctx.channel().pipeline());

            super.write(ctx, msg,promise);
        }
    }

    static class OutHandlerC extends ChannelOutboundHandlerAdapter {
        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            LoggerUtil.info("出站处理器 C: 被回调 ");
            LoggerUtil.info(ctx.pipeline());
            LoggerUtil.info(ctx.channel().pipeline());

            super.write(ctx, msg,promise);
        }
    }

    @Test
    public void testPipelineInBound(){
        ChannelInitializer i = new ChannelInitializer() {
            @Override
            protected void initChannel(Channel ch) throws Exception {

                ch.pipeline().addLast(new InPipeline.InHandlerA());
                ch.pipeline().addLast(new InPipeline.OutHandlerA());

                ch.pipeline().addLast(new InPipeline.InHandlerB());
                ch.pipeline().addLast(new InPipeline.InHandlerC());
                ch.pipeline().addLast(new InPipeline.OutHandlerB());
                ch.pipeline().addLast(new InPipeline.OutHandlerC());

            }
        };
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(i);
        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(1);
        embeddedChannel.writeInbound(buf);
        embeddedChannel.writeOutbound(buf);
    }

}
