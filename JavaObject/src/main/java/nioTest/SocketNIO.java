package nioTest;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import sun.misc.IOUtils;
import sun.nio.ch.IOUtil;
import util.DateUtil;
import util.LoggerUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Scanner;

public class SocketNIO {

    public static void main(String[] args) throws IOException {

        SocketChannel socketChannel = null;

        try {
            //开一个socket通道
            socketChannel = SocketChannel.open();
            //设置成非阻塞
            socketChannel.configureBlocking(false);

            socketChannel.connect(new InetSocketAddress("127.0.0.1",8080));
            while(!socketChannel.finishConnect()){
                System.out.println("尝试连接.....");
            }
            LoggerUtil.info("客户端连接成功");
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

            //要传输的文本
            Scanner scanner = new Scanner(System.in);
            LoggerUtil.info("请输入内容,输入exit退出:");
            String instr="exit";
            while(scanner.hasNext()&&(!"exit".equals(instr = scanner.next()))){
                byteBuffer.put(instr.getBytes());
                byteBuffer.flip();
                socketChannel.write(byteBuffer);
                byteBuffer.clear();
            }
            socketChannel.shutdownOutput();

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            socketChannel.close();
        }

    }
}
