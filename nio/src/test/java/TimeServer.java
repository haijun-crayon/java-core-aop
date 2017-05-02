import com.ccy.IO.nio.server.MultTimeServer;

import java.io.IOException;

public class TimeServer {
    public static void main(String[] args) throws IOException {
        int port = 8020;
        if (args != null && args.length > 0) {
            port = Integer.valueOf(args[0]);
        }

        MultTimeServer server = new MultTimeServer(port);
        new Thread(server).start();
    }
}
