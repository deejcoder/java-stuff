import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
            Server server = new Server("127.0.0.1", 5001);
            server.start();
        }
        catch(IOException ie) {
            ie.printStackTrace();
        }

    }
}
