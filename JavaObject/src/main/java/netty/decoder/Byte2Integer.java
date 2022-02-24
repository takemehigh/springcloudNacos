package netty.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.ReplayingDecoder;
import util.LoggerUtil;

import java.util.List;

//public class Byte2Integer extends ByteToMessageDecoder {
public class Byte2Integer extends ReplayingDecoder {

        public  static final Byte2Integer Byte2Integer = new Byte2Integer();
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        //while(in.readableBytes()>=4){
            int i = in.readInt();
            LoggerUtil.info("读取到一个INT数字："+i);
            out.add(i);
        //}

    }
}
