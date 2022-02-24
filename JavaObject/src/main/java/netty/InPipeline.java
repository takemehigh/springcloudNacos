package netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;
import util.LoggerUtil;

import java.nio.Buffer;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class InPipeline {

    static class InHandlerA extends SimpleChannelInboundHandler {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            LoggerUtil.info("入站处理器 A: 被回调 ");
            super.channelRead(ctx,msg);
        }

        @Override
        public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
            LoggerUtil.info("入站处理器 A: handlerAdded被回调 ");

            super.handlerAdded(ctx);
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
            Channel channel = ctx.channel();
            final int hashCode = channel.hashCode();
            System.out.println("channel hashCode:" + hashCode + " msg:" + msg + " cache:");

            channel.closeFuture().addListener(future -> {
                System.out.println("channel close, remove key:" + hashCode);
            });
            System.out.println(((ByteBuf)msg).refCnt());

            ScheduledFuture scheduledFuture = ctx.executor().schedule(
                    () -> {

                        System.out.println("schedule runs, close channel:" + hashCode);
                        channel.close();
                    }, 10, TimeUnit.SECONDS);

            while(scheduledFuture.isDone()){
                System.out.println(hashCode+"业务结束");
            }
        }
    }

    static class InHandlerB extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            LoggerUtil.info("入站处理器 B: 被回调 ");
            //不调用super会中断
            int i = ((ByteBuf)msg).getInt(0);
            if(i==1){
                ctx.pipeline().remove(this);
            }
            System.out.println();

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
            ByteBuf bb=(ByteBuf) msg;
            //bb.set
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
                LoggerUtil.info("添加handler");
                //ch.pipeline().addLast(new InPipeline.InHandlerA());
                ch.pipeline().addLast(new InPipeline.OutHandlerA());

                ch.pipeline().addLast(new InPipeline.InHandlerB());
                ch.pipeline().addLast(new InPipeline.InHandlerC());
                ch.pipeline().addLast(new InPipeline.OutHandlerB());
                ch.pipeline().addLast(new InPipeline.OutHandlerC());

            }
        };

        int n =1;
        /*Runnable r = new Runnable() {
            @Override
            public void run() {
                EmbeddedChannel embeddedChannel = new EmbeddedChannel(i);

                ByteBuf buf =   UnpooledByteBufAllocator.DEFAULT.buffer();
                buf.writeInt(1);
                embeddedChannel.writeInbound(buf);
            }
        };*/
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(i);

        for (int a=0;a<n;a++){
            System.out.println("第"+(a+1)+"个连接");
            ByteBuf buf =   UnpooledByteBufAllocator.DEFAULT.buffer();

            buf.writeInt(1);
            embeddedChannel.writeAndFlush(buf);
            System.out.println(buf.refCnt());
            buf.clear();
            //embeddedChannel.writeOutbound(buf);
            /*buf.clear();
            buf.writeInt(2);
            embeddedChannel.writeInbound(buf);
            embeddedChannel.writeOutbound(buf);*/
        }
        while(true){

        }

    }

}
