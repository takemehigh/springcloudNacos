package netty.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import lombok.extern.java.Log;
import util.LoggerUtil;

import java.util.List;

//public class Byte2Integer extends ByteToMessageDecoder {
public class IntegerAddDecoder extends ReplayingDecoder<IntegerAddDecoder.PHASE> {

    public final static IntegerAddDecoder IntegerAddDecoder = new IntegerAddDecoder(PHASE.P1);

    public IntegerAddDecoder(PHASE p1) {
        super(p1);
    }

    public enum PHASE{
        P1,
        P2
    }

    int first;
    int second;


    public  static final IntegerAddDecoder Byte2Integer = new IntegerAddDecoder(PHASE.P1);
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        LoggerUtil.info("decoder的in:"+in);
        switch(state())
        {
            case P1:
                first=in.readInt();
                LoggerUtil.info("接收第一个参数:"+first);
                checkpoint(PHASE.P2);
                break;
            case P2:
                second=in.readInt();
                LoggerUtil.info("接收第2个参数:"+second);

                Integer sum = first + second;
                LoggerUtil.info("计算和:"+sum);

                out.add(sum);
// 进入下一轮解码的第一步，设置“state”为第一阶段
                checkpoint(PHASE.P1);
                break;
            default:
                break;
        }


    }
}
