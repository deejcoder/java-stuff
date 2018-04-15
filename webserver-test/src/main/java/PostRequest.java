import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
    Merge POST & GET into class, RequestHandler
 */
public class PostRequest {


    private final String request;

    private final static int RESPONSE_BUFFER_SIZE = 128;

    private boolean validRequest;
    private String fileName;
    private String fileExt;

    public PostRequest(String request) {
        /*
            This regex is used for validating a post request
            as well as getting file name and ext.
         */
        this.request = request;
        String regex = "^(POST) \\/([\\w\\d\\/]{0,})([.\\w]{0,6})";
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(request);

        //Check if the POST request is valid & get the file name & extension
        validRequest = false;
        if(m.find()) {
            validRequest = true;
            fileName = m.group(2);
            fileExt = m.group(3);
        }
    }

    public byte[] processRequest() {
        if(isValidPostRequest()) {
            //Try get the data the client has sent in the POST request
            String data = getPostData();

            if(data != null) {
                //Try send data to ideal application server
                String response = routeRequest(data);
                if(response != null) {
                    return ("HTTP/2.0 200 OK\r\ncontent-type: application/json;charset=UTF-8\r\n\r\n" + response).getBytes();
                }
                else {
                    //Not Found: Couldn't find matching App Server or can't connect
                    return "404".getBytes();
                }
            }
            else {
                //Bad Request: provided no data
                return "400".getBytes();
            }
        }
        else {
            //Bad Request: not valid POST request
            return "400".getBytes();
        }
    }

    public String routeRequest(String data) {
        String fileName = getFileName();
        String fileExt = getFileExt();
        SocketAddress appEndPoint;

        switch(fileName) {
            case "game":
                appEndPoint = new InetSocketAddress("127.0.0.1", 5051);break;
            default:
                return null;
        }

        Socket socket = new Socket();
        try {
            System.out.println(data);
            socket.connect(appEndPoint);
            InputStream inStream = socket.getInputStream();
            OutputStream outStream = socket.getOutputStream();

            outStream.write(data.getBytes());

            byte[] responseBytes = new byte[RESPONSE_BUFFER_SIZE];
            inStream.read(responseBytes);

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

    public boolean isValidPostRequest() {
        return validRequest;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileExt() {
        return fileExt;
    }


    /**
     * Extracts the body content of a POST request (the data sent by the client)
     * @return a String containing the POST data
     */
    public String getPostData() {
        /*
            This regex will extract the body content of the request, which contains the POST data
            that the user has provided. It must be in the form: {VARIABLE}={SOME+CONTENT}
         */
        String regex = "\\r\\n(\\{[{\"\\w\\d:,}]*)";
        Pattern pattern = Pattern.compile(regex);
        Matcher pm = pattern.matcher(request);

        //If result found, return results.
        if(pm.find()) {
            return pm.group(1);
        }

        //Else return null
        return null;
    }
}
