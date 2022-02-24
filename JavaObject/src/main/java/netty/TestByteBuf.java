package netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.junit.Test;
import util.LoggerUtil;

public class TestByteBuf {

    @Test
    public void test1(){
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(10,100);
        LoggerUtil.info("动作：分配 ByteBuf(9, 100)", buffer);
        buffer.writeBytes(new byte[]{1, 2, 3, 4});
        LoggerUtil.info("动作：写入 4 个字节 (1,2,3,4)", buffer);
        LoggerUtil.info("start==========:get==========");
        getByteBuf(buffer);
        LoggerUtil.info("动作：取数据 ByteBuf", buffer);
        LoggerUtil.info("start==========:read==========");
        readByteBuf(buffer);
        LoggerUtil.info("动作：读完 ByteBuf", buffer);
        LoggerUtil.info(buffer.readableBytes());
        LoggerUtil.info(buffer.maxWritableBytes());

    }

    private void readByteBuf(ByteBuf buffer) {
        while (buffer.isReadable()) {
            LoggerUtil.info("取一个字节:" + buffer.readByte());
        }
    }
    //读字节，不改变指针
    private void getByteBuf(ByteBuf buffer) {
        for (int i = 0; i<buffer.readableBytes(); i++) {
            System.out.println(i);
            LoggerUtil.info("读一个字节:" + buffer.getByte(i));
        }
    }


}
