import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
    Reads a POST request, extracting the url
    requested and then sending the data.
 */
public class PostRequest {

    private final String request;

    //Post request is valid?
    private boolean validRequest;
    //URL info
    private String fileName;
    private String fileExt;


    public PostRequest(String request) {

        this.request = request;

        //Regex validates it's a valid POST request & gets URL info
        String regex = "^(POST) \\/([\\w\\d\\/]{0,})([.\\w]{0,6})";
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(request);

        validRequest = false;

        //If match
        if(m.find()) {
            //Request is valid
            validRequest = true;

            //Get file name & extension of the requested file
            fileName = m.group(2);
            fileExt = m.group(3);
        }
    }

    /**
     * Sends out an API request provided the post data after initializing the PostRequest object.
     * @return byte[] the data to be sent back to the client
     */
    public byte[] processRequest() {
        if(isValidPostRequest()) {
            //Try get the data the client has sent in the POST request
            String data = getPostData();

            if(data != null) {

                String response;

                /*
                    MAKE API REQUEST
                 */
                try {
                    response = ApiRequest.request(fileName, fileExt, data);
                    if(response != null) {
                        return ("HTTP/2.0 200 OK\r\ncontent-type: application/json;charset=UTF-8\r\n\r\n" + response).getBytes();
                    }
                    else {
                        //Not Found: Couldn't find matching App Server or can't connect
                        return "404".getBytes();
                    }
                }
                //No such API for this type of request
                catch(FileNotFoundException fnfe) {
                    fnfe.printStackTrace();
                    return "".getBytes();
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


    public boolean isValidPostRequest() {
        return validRequest;
    }


    /**
     * Extracts the body content/data of a POST request
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
