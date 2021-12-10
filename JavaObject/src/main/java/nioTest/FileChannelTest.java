package nioTest;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import config.NIOConfig;
import util.IOUtil;
import util.LoggerUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.InterfaceAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class FileChannelTest {

    public static void main(String[] args) {
    }

    public static void copyFile(String src,String dest){


        File srcFile = new File(src);
        File destFile = new File(dest);

        try{


            //如果目标文件不存在，则新建
            if (!destFile.exists()) {
                destFile.createNewFile();
            }

            FileInputStream fis = null;
            FileOutputStream fos = null;
            FileChannel inChannel = null;
            FileChannel outchannel = null;

            try{
                fis = new FileInputStream(srcFile);
                fos = new FileOutputStream(destFile);
                inChannel = fis.getChannel();
                outchannel = fos.getChannel();
                int length = -1;
                ByteBuffer bb = ByteBuffer.allocate(1024);

                while((length=inChannel.read(bb))!=-1 ){
                    //buffer转入读模式写入字节流
                    bb.flip();
                    int outlength = 0;

                    while ((outlength = outchannel.write(bb)) != 0) {
                        System.out.println("写入字节数：" + outlength);
                    }
                    //清除buf,变成写入模式
                    System.out.println("-------------------");

                    bb.clear();
                }
                outchannel.force(true);
            }
            catch (Exception e){
                e.printStackTrace();

            }
            finally {

                IOUtil.closeQuietly(fis);
                IOUtil.closeQuietly(fos);
                IOUtil.closeQuietly(inChannel);
                IOUtil.closeQuietly(outchannel);

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }


}

class FileChannelSender {

    private Charset charset = Charset.forName("UTF-8");

    public static void main(String[] args) {

        FileChannelSender fileChannelSender = new FileChannelSender();

        fileChannelSender.startSender();

    }

    private void startSender() {

        FileInputStream fis = null;
        FileChannel fc = null;
        SocketChannel sc = null;

        try{

            String srcPath = NIOConfig.SRC_PATH;
            srcPath = IOUtil.getResourcePath(srcPath);
            String destPath = NIOConfig.DEST_PATH;
            //destPath = IOUtil.builderResourcePath(destPath);

            LoggerUtil.info("源文件:"+srcPath);
            LoggerUtil.info("目标文件名:"+destPath);

            //从源文件获取
            File file= new File(srcPath);
            if(!file.exists()){
                throw new Exception("文件不存在");
            }
            fis = new FileInputStream(file);
            FileChannel fileChannel = fis.getChannel();

            sc = SocketChannel.open();
            sc.configureBlocking(false);
            sc.connect(new InetSocketAddress("127.0.0.1",8081));
            while(!sc.finishConnect()){
                LoggerUtil.info("尝试连接中");
            }
            LoggerUtil.info("成功连接服务端");
            //先发送文件名称/文件长度等信息，最后发文件
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            //byteBuffer.clear();
            ByteBuffer fileNameByteBuffer = charset.encode(file.getName());
            long fileNameLength = fileNameByteBuffer.remaining();
            LoggerUtil.info("发送文件名长度"+fileNameLength);
            byteBuffer.putInt((int)fileNameLength);
            //切换到读模式
            byteBuffer.flip();
            sc.write(byteBuffer);

            byteBuffer.clear();
            LoggerUtil.info("发送文件名"+file.getName());
            sc.write(fileNameByteBuffer);
            //发送文件长度
            long fileLength = file.length();
            byteBuffer.putLong((int)fileLength);
            byteBuffer.flip();
            sc.write(byteBuffer);
            LoggerUtil.info("发送文件长度"+file.getName());

            byteBuffer.clear();

            LoggerUtil.info("开始发送文件内容");
            int length = 0;
            long progress = 0;
            while((length=fileChannel.read(byteBuffer))>0){
                byteBuffer.flip();
                sc.write(byteBuffer);
                byteBuffer.clear();
                progress += length;
                LoggerUtil.info("| " + (100 * progress / file.length()) + "% |");
            }
            if (length == -1) {
                IOUtil.closeQuietly(fileChannel);
                sc.shutdownOutput();
                IOUtil.closeQuietly(sc);
            }

        }
        catch (Exception e){
            e.printStackTrace();

        }


    }

}

class FileChannelReceiver {

    Map<SelectableChannel,Client> clientMap = new HashMap<>();

    ByteBuffer byteBuffer = ByteBuffer.allocate(10240);

    private Charset charset = Charset.forName("UTF-8");


    public static void main(String[] args) {

        FileChannelReceiver fileChannelReceiver = new FileChannelReceiver();

        fileChannelReceiver.startReceive();


    }

    private void startReceive() {

        ServerSocketChannel serverSocketChannel = null;
        FileChannel fileChannel = null;
        Selector selector = null;
        try{
            //创建server
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress("127.0.0.1",8081));
            // 1、获取Selector选择器
            selector = Selector.open();
            //监听accept事件
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            while(selector.select()>0){
                Iterator<SelectionKey> keyIterator =selector.selectedKeys().iterator();
                while(keyIterator.hasNext()){
                    SelectionKey sk = keyIterator.next();
                    if(sk.isAcceptable()){
                        //LoggerUtil.info("");
                        //获取socket通道
                        ServerSocketChannel server = (ServerSocketChannel) sk.channel();

                        SocketChannel socket = server.accept();

                        if(socket==null) continue;
                        socket.configureBlocking(false);
                        // 12、将客户端新连接通道注册到 selector 选择器上
                        SelectionKey selectionKey =
                                socket.register(selector, SelectionKey.OP_READ);
                        // 余下为业务处理
                        Client client = new Client();
                        client.inetAddress =
                                (InetSocketAddress) socket.getRemoteAddress();
                        clientMap.put(socket, client);
                        LoggerUtil.info(socket.getRemoteAddress() + "连接成功...");
                    }
                    else if(sk.isReadable()){
                        processData(sk);
                    }
                    keyIterator.remove();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理客户端传输过来的数据
     */
    private void processData(SelectionKey key) throws IOException {
        Client client = clientMap.get(key.channel());

        SocketChannel socketChannel = (SocketChannel) key.channel();
        int num = 0;
        try {
            byteBuffer.clear();
            while ((num = socketChannel.read(byteBuffer)) > 0) {
                byteBuffer.flip();
                //客户端发送过来的，首先处理文件名
                if (null == client.fileNamel) {

                    //0 <= position <= limit <= capacity
                    //这里之前用的是buffer.capacity()
                    // capacity 是初始化话的内部数组元素数量 ，如Byte 数量
                    // 实际上这里应该是  buffer.limit()
                    //错误代码，如下
//                    if (buffer.capacity() < 4) {
//                        continue;
//                    }
                    //修改后的代码,这个bug，是社群小伙伴  nicole 发现的，小伙伴潜力无限呀
                    if (byteBuffer.limit() < 4) {
                        continue;
                    }
                    int fileNameLen = byteBuffer.getInt();
                    byte[] fileNameBytes = new byte[fileNameLen];
                    byteBuffer.get(fileNameBytes);

                    // 文件名
                    String fileName = new String(fileNameBytes, charset);

                    File directory = new File("/Users/gw/Documents/upload");
                    if (!directory.exists()) {
                        directory.mkdir();
                    }
                    LoggerUtil.info("NIO  传输目标dir：", directory);

                    client.fileNamel = fileName;
                    String fullName = directory.getAbsolutePath() + File.separatorChar + fileName;
                    LoggerUtil.info("NIO  传输目标文件：", fullName);

                    File file = new File(fullName.trim());

                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    FileChannel fileChannel = new FileOutputStream(file).getChannel();
                    client.outChannel = fileChannel;

                    if (byteBuffer.limit() < 8) {
                        continue;
                    }
                    // 文件长度
                    long fileLength = byteBuffer.getLong();
                    client.fileLength = fileLength;
                    client.startTime = System.currentTimeMillis();
                    LoggerUtil.info("NIO  传输开始：");

                    client.receiveLength += byteBuffer.capacity();
                    if (byteBuffer.limit() > 0) {
                        // 写入文件
                        client.outChannel.write(byteBuffer);
                    }
                    if (client.isFinished()) {
                        finished(key, client);
                    }
                }
                //客户端发送过来的，最后是文件内容
                else {
                    client.receiveLength += byteBuffer.capacity();
                    // 写入文件
                    client.outChannel.write(byteBuffer);
                    if (client.isFinished()) {
                        finished(key, client);
                    }
                }
                byteBuffer.clear();
            }
            key.cancel();
        } catch (IOException e) {
            key.cancel();
            e.printStackTrace();
            return;
        }
        // 调用close为-1 到达末尾
        if (num == -1) {
            finished(key, client);
        }
    }

    private void finished(SelectionKey key, Client client) {
        IOUtil.closeQuietly(client.outChannel);
        LoggerUtil.info("上传完毕");
        key.cancel();
        LoggerUtil.info("文件接收成功,File Name：" + client.fileNamel);
        LoggerUtil.info(" Size：" + IOUtil.getFormatFileSize(client.fileLength));
        long endTime = System.currentTimeMillis();
        LoggerUtil.info("NIO IO 传输毫秒数：" + (endTime - client.startTime));
    }

    static class Client{

        int step = 1; //1.文件长度2.文件名称3.文件内容长度4 读取文件内容

        public FileChannel channel;

        public String fileNamel;

        long fileLength;
        int fileNameLength;

        public long startTime;

        public InetSocketAddress inetAddress;
        //输出的文件通道
        FileChannel outChannel;

        public long receiveLength;

        public boolean isFinished() {
            return receiveLength >= fileLength;
        }
    }
}