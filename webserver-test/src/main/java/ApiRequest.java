import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class ApiRequest {

    private final static int RESPONSE_BUFFER_SIZE = 128;

    static String request(String fileName, String fileExt, String data) throws FileNotFoundException {
        SocketAddress appEndPoint;

        /*
            In the future this would be converted to a configuration file to
             specify a list of all app servers, like how Django has a 'url' file.
             Alternatively, could use sqlite to maintain a table of routing rules
             Again... would use asynchronous sockets with thread pools to handle multiple clients
             and wait for a response from the API since if the app server doesn't respond or takes a while
             to respond, it'll lag the entire server.
         */
        System.out.println(fileName);
        switch(fileName) {
            case "api/games/tictactoe":
                appEndPoint = new InetSocketAddress("127.0.0.1", 5050);break;
            default:
                throw new FileNotFoundException("Application server not found...");
        }

        Socket socket = new Socket();
        try {

            //Connect to the application server
            socket.connect(appEndPoint);

            //Get input/output streams
            InputStream inStream = socket.getInputStream();
            OutputStream outStream = socket.getOutputStream();

            //Send the data
            outStream.write(data.getBytes());

            //Wait & read the response
            byte[] responseBytes = new byte[RESPONSE_BUFFER_SIZE];
            inStream.read(responseBytes);

            //Convert the response to a String & return it
            String response = new String(responseBytes, "UTF-8").trim();
            System.out.println(response);
            return response;

        }
        catch(IOException ie) {
            ie.printStackTrace();
            //Unable to connect to the application server
            return null;
        }
    }
}
