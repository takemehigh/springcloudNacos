package reactor;

import util.LoggerUtil;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class EchoHandler implements Runnable{

    final SocketChannel channel;
    final SelectionKey sk;
    final ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

    //处理器实例的状态：发送和接收，一个连接对应一个处理器实例
    static final int RECIEVING = 0, SENDING = 1;
    int state = RECIEVING;



    public EchoHandler(Selector selector, SocketChannel p1) throws IOException {

        //回显处理器初始化，接收选择器和客户端socket
        channel = p1;
        p1.configureBlocking(false);
        sk=p1.register(selector,0);
        //处理器注册到该选择键
        sk.attach(this);
        //注册读事件
        sk.interestOps(SelectionKey.OP_READ);
        selector.wakeup();
        
    }

    @Override
    public void run() {
        try {

            if (state == SENDING) {
                //写入通道
                channel.write(byteBuffer);
                //写完后,准备开始从通道读,byteBuffer切换成写模式
                byteBuffer.clear();
                //写完后,注册read就绪事件
                sk.interestOps(SelectionKey.OP_READ);
                //写完后,进入接收的状态
                state = RECIEVING;
            } else if (state == RECIEVING) {
                //从通道读
                int length = 0;
                while ((length = channel.read(byteBuffer)) > 0) {
                    LoggerUtil.info(new String(byteBuffer.array(), 0, length));
                }
                //读完后，准备开始写入通道,byteBuffer切换成读模式
                byteBuffer.flip();
                //读完后，注册write就绪事件
                sk.interestOps(SelectionKey.OP_WRITE);
                //读完后,进入发送的状态
                state = SENDING;
            }
            //处理结束了, 这里不能关闭select key，需要重复使用
            //sk.cancel();
        } catch (IOException ex) {
            ex.printStackTrace();
            sk.cancel();
            try {
                channel.finishConnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
