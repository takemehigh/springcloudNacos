package nioTest;

import util.DateUtil;
import util.IOUtil;
import util.LoggerUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Scanner;

public class UDPSeverNIO {

    public void receive(){

        //获取channel
        DatagramChannel datagramChannel = null;
        Selector selector = null;
        try {
            datagramChannel = DatagramChannel.open();
            datagramChannel.configureBlocking(false);
            datagramChannel.bind(new InetSocketAddress("127.0.0.1",8099));
            LoggerUtil.info("UDP服务器启动成功");
            selector = Selector.open();
            //将通道注册到选择器
            datagramChannel.register(selector, SelectionKey.OP_READ);
            //通过选择器，查询 IO 事件
            LoggerUtil.info("查询选择键");
            while (selector.select() > 0) {
                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                ByteBuffer byteBuffer = ByteBuffer.allocate(512);

                while(keyIterator.hasNext()){

                    SelectionKey selectionKey = keyIterator.next();

                    if(selectionKey.isConnectable()){
                        LoggerUtil.info("成功连接");
                    }
                    else if(selectionKey.isReadable()){
                        LoggerUtil.info("通道可读");
                        //SocketChannel channel = (SocketChannel) selectionKey.channel();

                        datagramChannel.receive(byteBuffer);

                        byteBuffer.flip();

                        LoggerUtil.info(new String(byteBuffer.array(),0, byteBuffer.limit()));
                        byteBuffer.clear();
                    }

                }
                keyIterator.remove();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            IOUtil.closeQuietly(datagramChannel);
            IOUtil.closeQuietly(selector);
        }

    }

    public static void main(String[] args) {
        new UDPSeverNIO().receive();
    }
}
