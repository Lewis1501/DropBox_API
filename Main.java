/**
 * Created by Sony VAIO on 08/01/2016.
 */

import com.dropbox.core.*;
import java.io.*;
import java.util.Locale;


public class Main {
    static public void main(String[] args) throws IOException, DbxException{

        final String upFile = "File/HomeFile.txt";
        final String APP_KEY = "dw456oj6brncyd3";
        final String APP_SECRET = "dw456oj6brncyd3";

        DbxAppInfo appInfo = new DbxAppInfo(APP_KEY,APP_SECRET);

        DbxRequestConfig config = new DbxRequestConfig(
                "JavaTutorial/1.0",Locale.UK.toString());
        DbxWebAuthNoRedirect webAuthNoRedirect = new DbxWebAuthNoRedirect(config,appInfo);


        String authorizeUrl = webAuthNoRedirect.start();
        System.out.println("1. Go to  " + authorizeUrl);
        System.out.println("2. Click \"Allow\" (you might have to log in first)");
        System.out.println("3. Copy the authorization code.");
        String code = new BufferedReader(new InputStreamReader(System.in)).readLine().trim();


        DbxAuthFinish authFinish = webAuthNoRedirect.finish(code);
        String acessToken = authFinish.accessToken;


        DbxClient client = new DbxClient(config,acessToken);
        System.out.println("Linked account " + client.getAccountInfo().displayName);

        /**
         * Create below into a second method.
         */

        File inputFile = new File(upFile);
        FileInputStream inputStream = new FileInputStream(inputFile);
        try{
            DbxEntry.File uploadedFile = client.uploadFile("File/uploadedFile.txt",
                    DbxWriteMode.add(),inputFile.length(),inputStream);
            System.out.println("Uploaded: " + uploadedFile.toString());
        } catch (IOException io){
            io.printStackTrace();
        } finally {
            inputStream.close();
        }

        DbxEntry.WithChildren listing = client.getMetadataWithChildren("/");
        System.out.println("Files in the root path:");
        for (DbxEntry child : listing.children) {
            System.out.println("	" + child.name + ": " + child.toString());
        }

        FileOutputStream outputStream = new FileOutputStream("File/uploadedFile.txt");
        try {
            DbxEntry.File downloadedFile = client.getFile("File/uploadedFile.txt", null,
                    outputStream);
            System.out.println("Metadata: " + downloadedFile.toString());
        } finally {
            outputStream.close();
        }
    }
}

