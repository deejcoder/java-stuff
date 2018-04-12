
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetRequestHandler {

    //Must include "/" at end
    private static String RESOURCE_PATH = "./www/";

    //keeps track of any errors
    private int error = 200;

    private String fileExt;
    private String fileName;

    /**
     * Takes a GET request line, looks for and sends the requested resource
     * @param line the GET request line
     * @return bytes of data to send, or null if the GET request couldn't be processed.
     */
    protected byte[] processRequest(String line) {

        //Get the file path
        String filePath = getResourcePath(line);

        //Get the resource from the server
        byte[] bytes;

        try {
            bytes = getResource(filePath);
        }
        //If the requested resource isn't available, return 404.html
        catch(IOException ie) {
            try {
                error = 404;
                bytes = getResource("404.html");
            }
            catch(IOException ioe) {
                System.out.println("The server could not locate " + RESOURCE_PATH + "404.html");
                return null;
            }
        }

        String header = "";
        switch(error) {
            default:
            case 400: header = "HTTP/2.0 400 Bad Request\r\n"; break;
            case 404: header = "HTTP/2.0 404 Not Found\r\n"; break;
            case 200: header = "HTTP/2.0 200 OK\r\n"; break;
        }

        switch(fileExt) {
            default:
            case ".html": header += "Content-Type: text/html\r\n"; break;
            case ".png": header += "Content-Type: text/png\r\n"; break;
            case ".gif": header += "Content-Type: text/gif\r\n"; break;
            case ".jpg": header += "Content-Type: text/jpg\r\n"; break;
            case ".css": header += "Content-Type: text/css\r\n"; break;
        }
        header += "\r\n";

        System.out.println(header);
        System.out.println(filePath);

        //Add the header onto the bytes from the file
        byte[] headerBytes = header.getBytes();
        byte[] data = new byte[headerBytes.length+bytes.length];
        System.arraycopy(headerBytes, 0, data, 0, headerBytes.length);
        System.arraycopy(bytes, 0, data, headerBytes.length, bytes.length);
        return data;
    }

    /**
     * Find the file name/ext of the requested resource
     * @param line the GET request line
     * @return String, containing the requested resource path
     */
    private String getResourcePath(String line) {
        //Regex is used to validate GET request & extract file name/ext
        String regex = "^(GET) \\/([\\w\\d\\/]{0,})([.\\w]{0,6})";
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(line);

        //Get the file name & extension the client is requesting
        if(m.find()) {
            fileName = m.group(2);
            fileExt = m.group(3);

            //Route resources to /www/X folder
            switch(fileExt) {
                case ".css": fileName = "/styles/" + fileName; break;
                case ".jpg":
                case ".gif":
                case ".png":
                    fileName = "/images/" + fileName; break;
            }
        }
        //Bad Request (the regex did not match)
        else {
            error = 400;
            fileName = "400";
            fileExt = "html";
        }

        //If the filename is simply just "/"
        if(fileName.equals("")) {
            fileName = "index";
            fileExt = "html";
        }

        return fileName + fileExt;
    }

    /**
     * Reads from a file
     * @param filePath the path to the file
     * @return the bytes of the file
     * @throws IOException
     */
    protected byte[] getResource(String filePath) throws IOException {

        File file = new File((RESOURCE_PATH + filePath));
        byte[] bytes = new byte[(int)file.length()];
        FileInputStream fin = new FileInputStream(file);
        fin.read(bytes, 0, bytes.length);
        return bytes;
    }
}
