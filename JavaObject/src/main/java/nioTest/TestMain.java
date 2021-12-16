package nioTest;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class TestMain {

    public static void main(String[] args) throws IOException {
        Selector selector=Selector.open();
        ServerSocketChannel socketChannel = ServerSocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.bind(new InetSocketAddress(8090));
        SelectionKey selectionKey=socketChannel.register(selector, SelectionKey.OP_ACCEPT);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                    selector.wakeup();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                    SocketChannel socketChannel1 = SocketChannel.open();
                    socketChannel1.configureBlocking(false);
                    socketChannel1.connect(new InetSocketAddress(8090));
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            }
        });
        //t.start();
        //t2.start();
        while(!Thread.interrupted()){
            System.out.println("------------");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(selector.select());

        }

    }
}
