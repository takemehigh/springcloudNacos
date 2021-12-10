package config;

public class NIOConfig extends ConfigProperties{

    static final NIOConfig singeton = new NIOConfig("/system.properties");

    private NIOConfig(String fileName){
        super(fileName);
        loadFromFile();
    }

    public static final String SRC_PATH = singeton.getValue("socket.send.file");
    public static final String DEST_PATH = singeton.getValue("socket.receive.file");

}
