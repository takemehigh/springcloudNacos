package reactor;

import com.sun.org.apache.bcel.internal.generic.Select;
import com.sun.security.ntlm.Server;
import util.LoggerUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

public class ReactorServer implements Runnable{

    //反应器模式（单线程版）服务端
    public static void main(String[] args) {
       ReactorServer reactorServer = new ReactorServer();
       new Thread(reactorServer).start();
    }
    //选择器
    Selector selector;

    //服务端socketChannel;
    ServerSocketChannel serverSocketChannel;

    public static final  int PORT = 8099;

    ReactorServer(){
        try {
            selector = Selector.open();

            serverSocketChannel = ServerSocketChannel.open();

            serverSocketChannel.configureBlocking(false);

            serverSocketChannel.bind(new InetSocketAddress(PORT));
            //
            SelectionKey selectionKey = serverSocketChannel.register(selector,SelectionKey.OP_ACCEPT);
            //将handler放入选择键的附件
            selectionKey.attach(new AcceptHandler());

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void run() {
        //死循环监听
        try {
            while(!Thread.interrupted()){

                selector.select();

                Set<SelectionKey> keySet= selector.selectedKeys();

                Iterator<SelectionKey> it = keySet.iterator();

                while(it.hasNext()){
                    SelectionKey sk = it.next();
                    dispatcher(sk);
                }
                keySet.clear();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void dispatcher(SelectionKey sk){

            Runnable run=(Runnable)sk.attachment();
            if(run!=null){
                run.run();
            }

    }

    //专门用来处理接收就绪事件的handler
    class AcceptHandler implements Runnable{

        @Override
        public void run() {
            //获取选择键上的channel
            //ServerSocketChannel serverSocketChannel = (ServerSocketChannel)selectionKey.channel();
            //拿到客户端socket
            try {
                SocketChannel socketChannel=serverSocketChannel.accept();

                LoggerUtil.info("监听到了客户连接:"+socketChannel.getLocalAddress());

                //SelectionKey selectionKey = socketChannel.register(selector,SelectionKey.OP_READ);
                //将显示HANDLER加到选择键中
                //传入selector\channel是为了方便注册事件，
                selectionKey.attach(new EchoHandler(selector,));


            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }
}
