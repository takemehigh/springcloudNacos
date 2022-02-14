package netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;
import util.LoggerUtil;

public class EmbeddedDemo {

    @Test
    public void testInBound() {
        InBoundHandlerDemo inBoundHandlerDemo = new InBoundHandlerDemo();
        InBoundHandlerDemoA inBoundHandlerDemoA = new InBoundHandlerDemoA();
        InBoundHandlerDemoB inBoundHandlerDemoB = new InBoundHandlerDemoB();
        InBoundHandlerDemoC inBoundHandlerDemoC = new InBoundHandlerDemoC();

        LoggerUtil.info("子通道处理器初始化");

        ChannelInitializer i = new ChannelInitializer<EmbeddedChannel>() {
            @Override
            protected void initChannel(EmbeddedChannel ch) throws Exception {
                LoggerUtil.info("子通道处理器初始化");
                ch.pipeline().addLast(inBoundHandlerDemo);
                ch.pipeline().addLast(inBoundHandlerDemoA);
                ch.pipeline().addLast(inBoundHandlerDemoB);
                ch.pipeline().addLast(inBoundHandlerDemoC);

            }
        };
        EmbeddedChannel channel = new EmbeddedChannel(i);

        //缓冲区
        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(1234);
        //模拟入站 向通道写入缓冲区数据
        channel.writeInbound(buf);
        channel.flush();
        channel.close();
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
