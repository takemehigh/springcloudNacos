package reactor;

import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

public class MultiThreadReactorServer {
    //一个选择器用来查询accept事件
    public   Selector bossSelector ;
    //一个长度为2 的选择器数组查询READ事件
    public  Selector[] childSelectors;
    public  Reactor bossReactor;
    public  Reactor[] childReactors;
    public MultiThreadReactorServer() throws IOException {
        bossSelector = Selector.open();
        Selector s1=Selector.open();
        Selector s2=Selector.open();
        childSelectors = new Selector[]{s1,s2};
        // udp协议 DatagramChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress("127.0.0.1",8080));

        SelectionKey sk = serverSocketChannel.register(bossSelector, SelectionKey.OP_ACCEPT);

    }

    //反应器类
    class Reactor implements Runnable{

        @Override
        public void run() {

        }
    }

    public static void main(String[] args) throws IOException {
        new MultiThreadReactorServer();
    }
}
