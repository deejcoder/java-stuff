import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Server extends Thread {

    private final SocketAddress localEndpoint;
    private static int BUFFER_SIZE = 512;

    private ServerSocket socket;

    /**
     * Creates a new Server instance, and binds to the specified endpoint.
     * @param addr the IP address of the local endpoint
     * @param port the port of the local endpoint
     * @throws IOException on port in use, or security has prevented binding
     */
    public Server(String addr, int port) throws IOException {
        localEndpoint = new InetSocketAddress(addr, port);
        socket = new ServerSocket();

        socket.setReceiveBufferSize(BUFFER_SIZE);
        socket.bind(localEndpoint);
    }

    @Override
    public void run() {
        listen();
    }


    private void listen() {
        while(true) {

            //Wait & accept a client
            Socket client;

            try {
                client = socket.accept();
            }
            catch(IOException ie) {
                ie.printStackTrace();
                return;
            }
            catch(SecurityException se) {
                se.printStackTrace();
                continue;
            }

            //Get the input & output streams from the client socket
            DataInputStream inStream;
            BufferedOutputStream outStream;

            try {
                inStream = new DataInputStream(client.getInputStream());
                outStream = new BufferedOutputStream(client.getOutputStream());
            }
            catch(IOException ie) {
                ie.printStackTrace();
                continue;
            }

            //Read data from the client
            read(client, inStream, outStream);

            try {
                outStream.flush();
                client.close();
            }
            //Client connection already closed
            catch(IOException ie) {
                ie.printStackTrace();
            }
        }
    }

    /**
     * Reads incoming data from a client, then processes it based on what request it is.
     * @param client the client
     * @param in the input stream
     * @param out the output stream
     */
    private void read(Socket client, DataInputStream in, BufferedOutputStream out) {
        /*
            The below regex will extract the request type i.e GET, followed by
            the filename and finally the file extension (3 groups)
         */
        String requestRegex = "^(GET|POST)";
        Pattern pattern = Pattern.compile(requestRegex);

        try {
            byte[] bytes = new byte[BUFFER_SIZE];
            in.read(bytes, 0, BUFFER_SIZE);
            String data = new String(bytes, "utf-8");

            Matcher m = pattern.matcher(data);
            if (m.find()) {
                switch (m.group(0)) {
                    case "GET": {
                        GetRequestHandler request = new GetRequestHandler();
                        byte[] returnData = request.processRequest(data);
                        send(out, returnData);
                        break;
                    }
                    case "POST": {
                        PostRequestHandler request = new PostRequestHandler();
                        byte[] returnData = request.processRequest(data);
                        send(out, returnData);
                        break;
                    }
                    default:
                        client.close();
                        break;
                }
            }
        }
        /*
            This will be invoked if:
            1. client closed connection & there is no data to read.
            2. character encoding is incorrect
            3. can't close client connection, because it's  already closed
         */
        catch(IOException ie) {
            ie.printStackTrace();
            return;
        }
    }

    /**
     * Sends a message to some client connected to the provided stream.
     * @param out the BufferedOutputStream to write to
     * @param data the data to write.
     */
    private void send(BufferedOutputStream out, byte[] data) {
        try {
            out.write(data);
        }
        catch(IOException ie) {
            ie.printStackTrace();
        }
    }
}
