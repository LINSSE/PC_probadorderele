import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;

import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.*;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.drive.Drive;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DriveApi {
    /** Application name. */
    private static final String APPLICATION_NAME =
    		  "MonitorReles";

    /** Directory to store user credentials for this application. */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(
        System.getProperty("user.home"), ".credentials/MonitorReles");

    /** Global instance of the {@link FileDataStoreFactory}. */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY =
        JacksonFactory.getDefaultInstance();

    /** Global instance of the HTTP transport. */
    private static HttpTransport HTTP_TRANSPORT;

    /** Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/drive-java-quickstart
     */
    private static final List<String> SCOPES =
    		 Arrays.asList(SheetsScopes.SPREADSHEETS,DriveScopes.DRIVE);

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Creates an authorized Credential object.
     * @return an authorized Credential object.
     * @throws Exception 
     */
    public static Credential authorize() throws Exception {
        // Load client secrets.
        InputStream in =
            Quickstart.class.getResourceAsStream("/client_secret.json");
        GoogleClientSecrets clientSecrets =
            GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(DATA_STORE_FACTORY)
                .setAccessType("offline")
                .build();
        Credential credential = new AuthorizationCodeInstalledApp(
            flow, new LocalServerReceiver()).authorize("user");
        System.out.println(
                "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }

    /**
     * Build and return an authorized Drive client service.
     * @return an authorized Drive client service
     * @throws Exception 
     */
    public static Drive getDriveService() throws Exception {
        Credential credential = authorize();
        return new Drive.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    /**
     * Build and return an authorized Sheets API client service.
     * @return an authorized Sheets API client service
     * @throws Exception 
     */
    public static Sheets getSheetsService() throws Exception {
        Credential credential = authorize();
        return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    //private static Drive service;
    public static String actualFolder = "0B2SU9lm9vfKiQkc0WEdrQTNVOFE";
    public static ArrayList<String> Hojas = null;
    Drive service;
    
    public void init(ArrayList<String> hojas) throws Exception {
        // Build a new authorized API client service.
    	service = getDriveService();
    	Hojas = hojas;
        // Print the names and IDs for up to 10 files.
       // FileList result = service.files().list()
        //     .setPageSize(10)
        //     .setFields("nextPageToken, files(id, name)")
        //     .execute();
        //List<File> files = result.getFiles();
        //if (files == null || files.size() == 0) {
        //    System.out.println("No files found.");
        //} else {
         //   System.out.println("Files:");
          //  for (File file : files) {
           //     System.out.printf("%s (%s)\n", file.getName(), file.getId());
            //}
        //}
    }
    
    public void moveToFolder(String FileId) throws IOException
    {
    	System.out.println("Moviendo Archivo");
        //Drive driveService = getDriveService();
    	String fileId = FileId;
    	String folderId = actualFolder;
    	// Retrieve the existing parents to remove
    	File file = service.files().get(fileId)
    	        .setFields("parents")
    	        .execute();
    	StringBuilder previousParents = new StringBuilder();
    	for(String parent: file.getParents()) {
    	    previousParents.append(parent);
    	    previousParents.append(',');
    	}
    	// Move the file to the new folder
    	file = service.files().update(fileId, null)
    	        .setAddParents(folderId)
    	        .setRemoveParents(previousParents.toString())
    	        .setFields("id, parents")
    	        .execute();
    	
    }
    
    public String createFolder(String nombre) throws IOException
    {  
    	//Drive driveService = getDriveService();
    	File fileMetadata = new File();
    	fileMetadata.setName(nombre);
    	fileMetadata.setMimeType("application/vnd.google-apps.folder");

    	File file = service.files().create(fileMetadata)
    	        .setFields("id")
    	        .execute();
    	System.out.println("Folder ID: " + file.getId());
    	return file.getId();
    }
    
    public String fileExist(String Filename) throws IOException
    {
    	//Drive driveService = getDriveService();
    	String pageToken = null;
    	do {
    	    FileList result = service.files().list()
    	            .setQ("name='"+Filename+"' and '"+actualFolder+"' in parents")
    	            .setSpaces("drive")
    	            .setFields("nextPageToken, files(id, name)")
    	            .setPageToken(pageToken)
    	            .execute();
    	    for(File file: result.getFiles()) {
    	    	System.out.println(file.getId());
    	        return file.getId();
    	   
    	    }
    	    pageToken = result.getNextPageToken();
    	} while (pageToken != null);
    	return null;
    }

    public String CreateFile(String Filename) throws IOException
    {
    	String templateId = "1uSiIF6MJLjHZvRf9EBgM7BXuo9vKYB7UbtMaDaJwRTI";
    	File file = new File();
    	file.setName(Filename);
    	ArrayList<String> parents = new ArrayList<String>();
    	parents.add("0B2SU9lm9vfKiQkc0WEdrQTNVOFE");
    	file.setParents(parents);
    	File response = service.files().copy(templateId, file).execute();
    	return (response.getId());
    	
    }
}