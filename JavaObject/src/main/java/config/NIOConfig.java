package config;

public class NIOConfig extends ConfigProperties{

    static final NIOConfig singeton = new NIOConfig("/system.properties");

    private NIOConfig(String fileName){
        super(fileName);
        loadFromFile();
    }
    /*socket.server.ip=127.0.0.1
    socket.server.port=18899*/
    public static final String SRC_PATH = singeton.getValue("socket.send.file");
    public static final String DEST_PATH = singeton.getValue("socket.receive.file");
    public static final String ip = singeton.getValue("socket.server.ip");
    public static final int port = Integer.valueOf(singeton.getValue("socket.server.port"));
}
