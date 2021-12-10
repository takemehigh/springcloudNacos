package nioTest;


import util.LoggerUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * NIO socket服务端测试
 */
public class ServerSocketNIO {
    //使用Map保存每个客户端传输，当OP_READ通道可读时，根据channel找到对应的对象
    Map<SelectableChannel, Client> clientMap = new HashMap<SelectableChannel, Client>();

    public static void main(String[] args) {

        ServerSocketNIO serverSocketNIO = new ServerSocketNIO();
        serverSocketNIO.startServer();
    }

    private  void startServer() {
        //
        ServerSocketChannel serverSocketChannel = null;
        try {
            Selector selector = Selector.open();

            //开一个serversocket通道
            serverSocketChannel = ServerSocketChannel.open();
            //设置成非阻塞
            serverSocketChannel.configureBlocking(false);

            serverSocketChannel.bind(new InetSocketAddress("127.0.0.1",8080));
           // while()
        /*while(!socketChannel.finishConnect()){
                System.out.println("尝试连接.....");
            }*/
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            int i=0;
            while ((selector.select()) > 0) {
                LoggerUtil.info("查询事件"+i);


                // 7、获取选择键集合
                Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                while (it.hasNext()) {
                    // 8、获取单个的选择键，并处理
                    SelectionKey key = it.next();
                    System.out.println(key);

                    if (null == key) continue;

                    // 9、判断key是具体的什么事件，是否为新连接事件
                    if (key.isAcceptable()) {
                        // 10、若接受的事件是“新连接”事件,就获取客户端新连接
                        ServerSocketChannel server = (ServerSocketChannel) key.channel();
                        SocketChannel socketChannel = server.accept();
                        if (socketChannel == null) continue;
                        // 11、客户端新连接，切换为非阻塞模式
                        socketChannel.configureBlocking(false);
                        // 12、将客户端新连接通道注册到selector选择器上
                        SelectionKey selectionKey =
                                socketChannel.register(selector, SelectionKey.OP_READ);
                        // 余下为业务处理
                        Client client = new Client();
                        client.remoteAddress
                                = (InetSocketAddress) socketChannel.getRemoteAddress();
                        clientMap.put(socketChannel, client);
                        //Logger.debug(socketChannel.getRemoteAddress() + "连接成功...");

                    } else if (key.isReadable()) {
                        try{
                            LoggerUtil.info("可读事件就绪");
                            processData(key);

                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    // NIO的特点只会累加，已选择的键的集合不会删除
                    // 如果不删除，下一次又会被select函数选中
                    it.remove();
                }
            }

            //åLoggerUtil.info("服务端关闭");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private ByteBuffer buffer
            = ByteBuffer.allocate(10240);
    /**
     * 处理客户端传输过来的数据
     */
    private void processData(SelectionKey key) throws UnsupportedEncodingException {
        Client client = clientMap.get(key.channel());

        SocketChannel socketChannel = (SocketChannel) key.channel();
        int num = 0;
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        try {

            while ((num = socketChannel.read(buffer)) > 0) {
                //刚进来都要转换成读模式。
                buffer.flip();

                LoggerUtil.info(new String(buffer.array(), 0, num));

                buffer.clear();
            }

            //key.cancel();
        } catch (IOException e) {
            key.cancel();
            e.printStackTrace();
            return;
        }
        finally {


        }
        // 调用close为-1 到达末尾
        if (num == -1) {
            finished(key, client);
        }
    }
    private void finished(SelectionKey key, Client client) {
        try {
            client.outChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        key.cancel();
        long endTime = System.currentTimeMillis();
    }

    static class Client {
        //文件名称
        String fileName;
        //长度
        long fileLength;

        //开始传输的时间
        long startTime;

        //客户端的地址
        InetSocketAddress remoteAddress;

        //输出的文件通道
        FileChannel outChannel;

        //接收长度
        long receiveLength;

        public boolean isFinished() {
            return receiveLength >= fileLength;
        }
    }
}
