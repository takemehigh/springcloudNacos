package reactor;

import lombok.Data;
import org.springframework.util.StringUtils;
import util.DateUtil;
import util.LoggerUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class ReactorClient {

    public static void main(String[] args) throws IOException {
        new ReactorClient().start();
    }

    private void start() throws IOException {

        InetSocketAddress address =
                new InetSocketAddress("127.0.0.1",
                        8099);

        // 1、获取通道（channel）
        SocketChannel socketChannel = SocketChannel.open(address);
        LoggerUtil.info("客户端连接成功");
        // 2、切换成非阻塞模式
        socketChannel.configureBlocking(false);
        socketChannel.setOption(StandardSocketOptions.TCP_NODELAY, true);
        //不断的自旋、等待连接完成，或者做一些其他的事情
        while (!socketChannel.finishConnect()) {

        }
        LoggerUtil.info("客户端启动成功！");
        Processor p=new Processor(socketChannel);
        Commander c=new Commander(p);
        new Thread(p).start();
        new Thread(c).start();

    }
    //用户处理socket数据
    @Data
    class Processor implements Runnable{

        String data ="";

        SocketChannel sc ;

        ByteBuffer readBuffer = ByteBuffer.allocate(1024);

        ByteBuffer writeBuffer = ByteBuffer.allocate(1024);

        final Selector selector;

        public Processor(SocketChannel sc) throws IOException {
            this.sc = sc;
            selector = Selector.open();
            sc.register(selector,
                    SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        }

        @Override
        public void run() {
            try{
                while (!Thread.interrupted()){
                    selector.select();
                    Set<SelectionKey> selected = selector.selectedKeys();
                    Iterator<SelectionKey> itr = selected.iterator();
                    while(itr.hasNext()){
                        SelectionKey sk = itr.next();
                        if(sk.isReadable()){
                            LoggerUtil.info("检测到可读");

// 若选择键的IO事件是“可读”事件,读取数据
                            SocketChannel socketChannel = (SocketChannel) sk.channel();

                            int length = 0;
                            while ((length = socketChannel.read(readBuffer)) > 0) {
                                readBuffer.flip();
                                LoggerUtil.info("server echo:" + new String(readBuffer.array(), 0, length));
                                readBuffer.clear();
                            }
                        }
                        else if(sk.isWritable()){
                            //LoggerUtil.info("检测到可写");
                            SocketChannel socketChannel = (SocketChannel) sk.channel();
                            //writeBuffer.flip();
                            // 操作三：发送数据
                            if(!StringUtils.isEmpty(data)) {
                                socketChannel.write(ByteBuffer.wrap(data.getBytes()));

                                data="";
                            }

                            //writeBuffer.clear();
                            //hasData.set(false);
                        }
                    }
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Data
    static class Commander implements Runnable{

        String data = "";
        Processor processor;

        Commander(Processor processor){
            this.processor = processor;
        }

        @Override
        public void run() {
            while (!Thread.interrupted()){
                //死循环持续接收用户操作
                Scanner scanner = new Scanner(System.in);
                //while(processor.get)
                while(scanner.hasNext()){
                    //防止输入量过大，分段读取发送

                    /*String str = scanner.next();
                    processor.writeBuffer.put((DateUtil.getNow() + " >>" + str).getBytes());*/

                    String str = scanner.next();
                    byte[] bytes = (DateUtil.getNow() + " >>" + str).getBytes();
                   /* Charset charset=Charset.forName("UTF-8");
                    ByteBuffer inBuffer = charset.encode(str);*/
                    //ByteBuffer bb1=ByteBuffer.allocate(1024);
                    ByteBuffer bb=ByteBuffer.wrap(bytes);

                    //processor.writeBuffer=bb;
                    if(!StringUtils.isEmpty(str)){
                        processor.data=str;

                    }
                }
            }
        }
    }
}
