package netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import netty.handler.MessageProcessHandler;
import netty.handler.NettyEchoServerHandler;
import netty.handler.NettyServerHandler;
import util.LoggerUtil;

import java.nio.charset.Charset;

public class TestDecoderEchoServer {

    public void runServer() throws InterruptedException {
        //1.创建一个服务器端的引导类
        ServerBootstrap b = new ServerBootstrap();


        //2.创建反应器轮询组
        //boss 轮询组
        EventLoopGroup bossLoopGroup = new NioEventLoopGroup(1);
        //worker 轮询组
        EventLoopGroup workerLoopGroup = new NioEventLoopGroup();
        try{
            b.group(bossLoopGroup,workerLoopGroup);

            //设置通讯模式
            b.channel(NioServerSocketChannel.class);

            //设置监听端口号
            b.localAddress(8080);
            b.option(ChannelOption.SO_RCVBUF,1024);
            b.option(ChannelOption.ALLOCATOR,PooledByteBufAllocator.DEFAULT);
            b.option(ChannelOption.SO_KEEPALIVE,true);
            //设置服务端处理器 ChannelInitializer泛型要和前面b.channel设置的对应
            b.childHandler(new ChannelInitializer<NioSocketChannel>() {

                @Override
                protected void initChannel(NioSocketChannel ch) throws Exception {
                    //固定长度数据包解码器——FixedLengthFrameDecoder
                    //ch.pipeline().addLast(new FixedLengthFrameDecoder(10));
                    //行分割数据包解码器——LineBasedFrameDecoder
                    //参数代表着一行最多读取这个数量的字节后如果没发现换行符，抛异常
                    //ch.pipeline().addLast(new LineBasedFrameDecoder(100));
                    //自定义分隔符数据包解码器——DelimiterBasedFrameDecoder
                    //ch.pipeline().addLast(new DelimiterBasedFrameDecoder(10));
                    //自定义长度数据包解码器——LengthFieldBasedFrameDecoder
                    //ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(10));
                    /*int maxFrameLength, //发送的数据包最大长度
                    int lengthFieldOffset, //长度字段偏移量
                    int lengthFieldLength, //长度字段自己占用的字节数
                    int lengthAdjustment, //长度字段的偏移量矫正
                    int initialBytesToStrip) //丢弃的起始字节数*/
                    ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024,0,4,0,4));

                    //ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024, 0, 4,0,4));
                    ch.pipeline().addLast(new StringDecoder());

                   // ch.pipeline().addLast(NettyEchoServerHandler.NettyEchoHandler);
                    ch.pipeline().addLast(new LengthFieldPrepender(4));
                    ch.pipeline().addLast(new StringEncoder(Charset.defaultCharset()));
                    ch.pipeline().addLast(NettyServerHandler.NettyEchoHandler);


                }
            });

            //调用sync阻塞等待服务器启动成功，不调用就不堵塞了
            //b.child
            ChannelFuture channelFuture=b.bind().sync();
            LoggerUtil.info(" 服务器启动成功，监听端口: " +
                    channelFuture.channel().localAddress());
            //channelFuture = channelFuture.channel().closeFuture();
            ChannelFuture closeFuture =
                    channelFuture.channel().closeFuture();
            closeFuture.sync();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            workerLoopGroup.shutdownGracefully();
            bossLoopGroup.shutdownGracefully();
        }



    }

    public static void main(String[] args) throws InterruptedException {
        new TestDecoderEchoServer().runServer();
    }
}
