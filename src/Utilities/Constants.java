package Utilities;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Constants {
    public static final int port = 8302;
    public static final int delay = 1000; // for server message
    public static InetAddress host;

    static {
        try {
            host = Constants();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public static InetAddress Constants() throws UnknownHostException {
        //host = InetAddress.getByName("192.168.56.1");
        host = InetAddress.getByName("localhost");
        return host;
    }
}
