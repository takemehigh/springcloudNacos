package reactor;

import util.LoggerUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.SocketChannel;

public class ReactorClient {

    public static void main(String[] args) {
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
    }
    //用户处理socket数据
    class Processor implements Runnable{

        SocketChannel sc ;

        public Processor(SocketChannel sc) {
            this.sc = sc;
        }

        @Override
        public void run() {
            
        }
    }
}
