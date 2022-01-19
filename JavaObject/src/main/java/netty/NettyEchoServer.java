package netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import util.LoggerUtil;

import java.net.InetSocketAddress;

public class NettyEchoServer {

    public void runServer() throws InterruptedException {
        //1.创建一个服务器端的引导类
        ServerBootstrap b = new ServerBootstrap();

        //2.创建反应器轮询组
        //boss 轮询组
        EventLoopGroup bossLoopGroup = new NioEventLoopGroup(1);
        //worker 轮询组
        EventLoopGroup workerLoopGroup = new NioEventLoopGroup();
        b.group(bossLoopGroup, workerLoopGroup);
        //3.设置通道类型
        b.channel(NioServerSocketChannel.class);
        b.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        b.localAddress(new InetSocketAddress(8084));
        //step4：设置通道的参数
        b.option(ChannelOption.SO_KEEPALIVE, true);
        b.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        //设置子通道的缓冲区分配器
        b.childOption(ChannelOption.ALLOCATOR,PooledByteBufAllocator.DEFAULT);

        //step5：装配子通道流水线
        b.childHandler(new ChannelInitializer<SocketChannel>() {
            //有连接到达时会创建一个通道的子通道，并初始化
            protected void initChannel(SocketChannel ch){
                // 这里可以管理子通道中的 Handler 业务处理器
                // 向子通道流水线添加一个 Handler 业务处理器
                ch.pipeline().addLast(new NettyDiscardHandler());

            }
        });
        b.handler(new ChannelInitializer<ServerSocketChannel>() {

            @Override
            protected void initChannel(ServerSocketChannel ch) throws Exception {
                LoggerUtil.info("创建父通道");
            }
        });
        //step6：开始绑定端口，通过调用 sync 同步方法阻塞直到绑定成功
        ChannelFuture channelFuture = b.bind().sync();
        LoggerUtil.info(" 服务器启动成功，监听端口: " +
                channelFuture.channel().localAddress());
        //step7：自我阻塞，直到通道关闭的异步任务结束
        ChannelFuture closeFuture = channelFuture.channel().closeFuture();
        closeFuture.sync();
        //step8：释放掉所有资源，包括创建的反应器线程
        workerLoopGroup.shutdownGracefully();
        bossLoopGroup.shutdownGracefully();
    }

    public static void main(String[] args) throws InterruptedException {
        new NettyEchoServer().runServer();
    }
}
