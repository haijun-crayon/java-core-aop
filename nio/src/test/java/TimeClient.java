import com.ccy.IO.nio.client.TimeClientHandler;

import java.io.IOException;

public class TimeClient {

    public static void main(String[] args) throws IOException {

        TimeClientHandler client = new TimeClientHandler("127.0.0.1", 8020);
        new Thread(client).start();
    }
}
