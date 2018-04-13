import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
    Merge POST & GET into class, RequestHandler
 */
public class PostRequestHandler {

    public byte[] processRequest(String request) {
        String regex = "^(POST) \\/([\\w\\d\\/]{0,})([.\\w]{0,6})";
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(request);

        String fileName;
        String fileExt;
        if(m.find()) {
            fileName = m.group(2);
            fileExt = m.group(3);

            String data = getPostData(request);
            if(data != null) {
                System.out.println("Send to: " + fileName + "'s application server.\nData:\n" + data);

            }
            else {
                System.out.println("WARNING: failed to get POST data from client.");
            }
            GetRequestHandler getrequest = new GetRequestHandler();
            byte[] returnData = getrequest.processRequest("GET /" + fileName + fileExt + " HTTP/2.0");
            return returnData;
        }

        else {
            return null; //change to return E400
        }

    }

    public String getPostData(String request) {
        /*
            This regex will extract the body content of the request, which contains the POST data
            that the user has provided. It must be in the form: {VARIABLE}={SOME+CONTENT}
         */
        String regex = "\\r\\n(([\\w\\d+]{0,}\\={1}[\\w\\d+]{0,}\\n{0,1}){1,})";
        Pattern pattern = Pattern.compile(regex);
        Matcher pm = pattern.matcher(request);

        //If result found, return results.
        if(pm.find()) return pm.group(2);

        //Else return null
        return null;
    }
}
