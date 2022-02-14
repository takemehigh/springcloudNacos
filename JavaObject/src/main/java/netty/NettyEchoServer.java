package netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
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
        b.group(bossLoopGroup,workerLoopGroup);

        //设置通讯模式
        b.channel(NioServerSocketChannel.class);

        //设置监听端口号
        b.localAddress(8080);
        b.option(ChannelOption.SO_RCVBUF,1024);
        b.option(ChannelOption.ALLOCATOR,PooledByteBufAllocator.DEFAULT);
        b.option(ChannelOption.SO_KEEPALIVE,true);
        //设置服务端处理器 ChannelInitializer泛型要和前面b.channel设置的一样
        b.childHandler(new ChannelInitializer<SocketChannel>() {

            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new NettyDiscardHandler());
            }
        });
        //调用sync阻塞等待服务器启动成功，不调用就不堵塞了
        //b.child
        ChannelFuture channelFuture=b.bind().sync();
        LoggerUtil.info(" 服务器启动成功，监听端口: " +
                channelFuture.channel().localAddress());
        channelFuture = channelFuture.channel().closeFuture();
    }

    public static void main(String[] args) throws InterruptedException {
        new NettyEchoServer().runServer();
    }
}
