
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*

    This file contains Utils for generating a response back to the
    client. It also handles incoming GET requests.

 */
public class GetRequest {

    //Must include "/" at end
    private static String RESOURCE_PATH = "./www/";

    private boolean validRequest = false;
    private String fileName;
    private String fileExt;

    public GetRequest(String request) {

        //Regex is used to validate GET request & extract file name/ext
        String regex = "^(GET) \\/([\\w\\d\\/]{0,})([.\\w]{0,6})";
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(request);

        /*
            Extract the requested file name & extension while
            validating it's a valid HTTP GET request
         */
        if (m.find()) {
            validRequest = true;
            fileName = m.group(2);
            fileExt = m.group(3);

            //If the file name is null, the client is requesting "/"
            if(fileName.equals("")) {
                fileName = "index";
                fileExt = ".html";
            }
        }
    }

    public byte[] processRequest() {

        if(isValidGetRequest()) {
            String fileName = getFileName();
            String fileExt = getFileExt();


            byte[] data;
            //Does the requested resource exist?
            try {
                data = getResource(fileName + fileExt);
            }
            //If it doesn't, return a 404 Not Found error...
            catch(IOException ie) {
                ie.printStackTrace();
                return generateError(404);
            }

            //Generate the header & return the response
            String header = generateHeader(fileExt);

            byte[] out = combineStringBytes(header, data);
            return out;

        }
        else {
            //Bad Request: not a valid GET request
            return generateError(400);
        }
    }

    /**
     * Generates a header for the response
     * @param fileExt the file extension of the requested resource
     * @return the generated header as a String
     */
    public String generateHeader(String fileExt) {
        String header = "HTTP/2.0 200 OK\r\n";

        switch(fileExt) {
            default:
            case ".html": header += "Content-Type: text/html\r\n"; break;
            case ".png": header += "Content-Type: text/png\r\n"; break;
            case ".gif": header += "Content-Type: text/gif\r\n"; break;
            case ".jpg": header += "Content-Type: text/jpg\r\n"; break;
            case ".css": header += "Content-Type: text/css\r\n"; break;
        }
        header += "\r\n";
        return header;
    }

    /**
     * Generates a new error response to send back
     * @param error the HTTP error code
     * @return the byte[] of the error response
     */
    public byte[] generateError(int error) {
        //Generate the error's header
        String header = "HTTP/2.0 ";

        switch(error) {
            case 400:
                header += "400 Bad Request\r\n";
                break;
            default:
            case 404:
                header += "404 Not Found\r\n";
                break;
        }

        header += "Content-Type: text/html\r\n";
        header += "\r\n";

        //Read the error HTML file
        try {
            byte[] fileData = getResource(error + ".html");
            return combineStringBytes(header, fileData);
        }
        //If error HTML file not found
        catch(IOException ie) {
            return (header + "<h2>An unexpected error occurred</h2><br>" +
                    "We are unsure what error has occurred. Please contact us.").getBytes();
        }
    }

    /**
     * Combines a String and a byte array (byte[])
     * @param string
     * @param bytes
     * @return
     */
    public byte[] combineStringBytes(String string, byte[] bytes) {
        //Convert string into bytes
        byte[] stringBytes = string.getBytes();

        //Create a new byte array with new length
        byte[] out = new byte[stringBytes.length+bytes.length];

        //copy contents to new array
        System.arraycopy(stringBytes, 0, out, 0, stringBytes.length);
        System.arraycopy(bytes, 0, out, stringBytes.length, bytes.length);
        return out;
    }

    /**
     * Checks if the request is valid
     * @return true or false (boolean)
     */
    public boolean isValidGetRequest() {
        if(validRequest) {
            return true;
        }
        return false;
    }

    /**
     * Gets the file name of the requested resource
     * @return the file name as a String
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Gets the file extension of the requested resource
     * @return the file extension as a String
     */
    public String getFileExt() {
        return fileExt;
    }

    /**
     * Reads a file
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
