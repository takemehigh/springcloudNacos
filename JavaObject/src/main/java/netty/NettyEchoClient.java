package netty;

import config.NIOConfig;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import netty.handler.NettyEchoClientHandler;
import netty.handler.NettyEchoClientHandler2;
import netty.handler.NettyEchoHandler;
import util.DateUtil;
import util.LoggerUtil;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.Scanner;

public class NettyEchoClient {



    public void runClient(String ip, int port) throws InterruptedException {
        //1.创建一个客户端的引导类、 EventLoopGroup workerLoopGroup = new NioEventLoopGroup();
        Bootstrap b = null;
        EventLoopGroup eventLoopGroup = null;
        try{
            b = new Bootstrap();
             eventLoopGroup = new NioEventLoopGroup();

            b.group(eventLoopGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.ALLOCATOR,PooledByteBufAllocator.DEFAULT);
            b.handler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel ch) throws Exception {


                    ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024,0,4,0,4));
                    //ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024, 0, 4,0,4));
                    ch.pipeline().addLast(new StringDecoder());
                    ch.pipeline().addLast(NettyEchoClientHandler2.NettyEchoHandler);

                    //添加长度编码器，配合对端长度解码器，防止半包问题
                    ch.pipeline().addLast(new LengthFieldPrepender(4));
                    ch.pipeline().addLast(new StringEncoder(Charset.defaultCharset()));

                }
            });

            startup(ip, port, b);


        }
        catch (Exception e){
            e.printStackTrace();
            startup(ip, port, b);
        }finally {
            // 优雅关闭EventLoopGroup，
            // 释放掉所有资源包括创建的线程
            if (eventLoopGroup!=null){
                eventLoopGroup.shutdownGracefully();

            }
        }
    }

    private void startup(String ip, int port, Bootstrap b) throws InterruptedException {
        try{
            ChannelFuture cf = b.connect(ip, port);
            cf.addListener(future->{
                if (!future.isSuccess()){
                    LoggerUtil.info("连接失败，重试");
                    startup(ip,port,b);
                }
            });
            cf.sync();

            LoggerUtil.info("客户端连接成功");
            LoggerUtil.info("请输入想发送的内容");

            Scanner scanner = new Scanner(System.in);
            while(scanner.hasNext()){
                try{
                    String str = scanner.next();
                    String now = DateUtil.getNow();
                    //ByteBuf buffer = PooledByteBufAllocator.DEFAULT.buffer();
                    ByteBuf buffer = Unpooled.buffer();
                    //byte[] toSendStr =(now+"-"+str).getBytes(StandardCharsets.UTF_8);
                    //byte[] toSendStr =(str).getBytes(StandardCharsets.UTF_8);
            /*for(Byte c:toSendStr){
                System.out.println(c);
            }*/
                    for (int j = 0; j < 1000; j++) { //发送 100 个包
                        //每个包随机 1-3 个 "疯狂创客圈：高性能学习社群!"
                        int random = new Random().nextInt(3);
                        ByteBuf buf = Unpooled.buffer();
                        //LoggerUtil.info("随机"+random+"次");
                        //用了长度编码器就不需要再自己手动写长度了
                        //buf.writeInt(1*(str.getBytes(StandardCharsets.UTF_8)).length);

                        for (int k = 0; k < 1; k++) {
                            buf.writeBytes(str.getBytes(StandardCharsets.UTF_8));
                        }
//发送"\r\n"回车换行符作为包结束符
                        //buf.writeBytes("\r\n".getBytes("UTF-8"));
                        cf.channel().writeAndFlush(buf);
                    }



                   // cf.channel().writeAndFlush(buffer);
                    //buffer.writeBytes(toSendStr);
            /*cf.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    LoggerUtil.info((now+"-"+str)+"发送完成");
                }
            });*/
                    LoggerUtil.info("请输入想发送的内容");
                }
                catch (Exception e){
                    e.printStackTrace();
                    LoggerUtil.info("请输入想发送的内容");
                    scanner = new Scanner(System.in);
                }


            }
        }
        catch (Exception e){
            e.printStackTrace();
            startup(ip,port,b);
        }

    }

    public static void main(String[] args) throws InterruptedException {

        new NettyEchoClient().runClient(NIOConfig.ip,NIOConfig.port);
    }
}
