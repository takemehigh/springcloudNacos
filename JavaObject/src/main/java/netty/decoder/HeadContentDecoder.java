package netty.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.ReplayingDecoder;
import util.LoggerUtil;

import java.nio.charset.StandardCharsets;
import java.util.List;
//bYTETOMESSAGEDECODER 与其自雷不能sharable
//public class Byte2Integer extends ByteToMessageDecoder {
public class HeadContentDecoder extends ByteToMessageDecoder {

        //public  static final HeadContentDecoder Byte2Integer = new HeadContentDecoder();
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {


        LoggerUtil.info("读取到的偏移量"+in.readerIndex());

        if(in.readableBytes()<4){
            return;
        }

        //消息头完整
        in.markReaderIndex();
        int length = in.readInt();
        LoggerUtil.info("读取到的长度"+length);
        if(in.readableBytes()<length){
            //消息还没收全，重置
            LoggerUtil.info("消息还没收全，把index重置到标记的位置");
            in.resetReaderIndex();
            return;
        }
        LoggerUtil.info("消息收全了，读取完整数据");

        byte[] inBytes = new byte[length];

        in.readBytes(inBytes,0,length);

        out.add(new String(inBytes, StandardCharsets.UTF_8));
        //in.clear();
    }
}
