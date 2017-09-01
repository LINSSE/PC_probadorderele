import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.Sheets.Spreadsheets.BatchUpdate;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.BatchUpdateValuesRequest;
import com.google.api.services.sheets.v4.model.GridProperties;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.SheetProperties;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.SpreadsheetProperties;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.api.services.sheets.v4.model.*;




import java.security.GeneralSecurityException;

public class GoogleSheets {
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
     * at ~/.credentials/sheets.googleapis.com-java-quickstart
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
     * @throws IOException
     */
    public static Credential authorize() throws IOException {
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
        Credential credential = null;
		try {
			credential = new AuthorizationCodeInstalledApp(
			    flow, new LocalServerReceiver()).authorize("user");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println(
                "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }

    /**
     * Build and return an authorized Sheets API client service.
     * @return an authorized Sheets API client service
     * @throws IOException
     */
    public static Sheets getSheetsService() throws IOException {
        Credential credential = authorize();
        return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
    
    /////////////////////////////////////////////
    //Inicio de mi aplicacion
    
    
    //private static Sheets service;
    private static String spreadsheetId = "1Tc0H58Fl2HN5RF9_6CtdjuGQRnW-Mix2P_Gv3kNY85c";
    private static ArrayList<String> Hojas = null;
    Sheets service;
 
    
public void init(ArrayList<String> hojas) throws IOException {
{
	 // Build a new authorized API client service.
    service = getSheetsService();
    Hojas = hojas;
  
}
}

public void setActualFile(String SpreadsheetId)
{
	GoogleSheets.setSpreadsheetId(SpreadsheetId);
	}

public String getRange(String range) throws IOException
{
	// Sheets service = getSheetsService();
	  ValueRange response = service.spreadsheets().values()
	        .get(getSpreadsheetId(), range)
	        .execute();
	  
	  System.out.println("Rango ACtual: "+response.getRange());
	return response.getRange();
	}



public void insert(List<String> datos, String param) throws IOException
{

	 //Sheets service = getSheetsService();
	///write
    String rango = getRange(param+"!A1:N");//ultimo dato
    ///crear una lista de objetos a escribir
    List<List<Object>> valores = new ArrayList<>();
    
    ///creo el objeto
 
  
   List<Object> dato = new ArrayList<Object>(datos);
 
    
    valores.add(dato);
 
    
    //crear el valuerange y modificar la configuracion
    ValueRange rangodevalores = new ValueRange();
    //System.out.println(rango);
    rangodevalores.setMajorDimension("ROWS");
    rangodevalores.setRange(rango);
    rangodevalores.setValues(valores);
    
    ///ejecutar la peticion
    AppendValuesResponse response = service.spreadsheets()
    .values()
    .append(getSpreadsheetId(), rango, rangodevalores)
    .setValueInputOption("RAW")
    .execute();
    
    //System.out.println("INSERT RESPONSE: "+response);
    System.out.println(param+" Insert ok");
	}



public String CreateSheet(String titulo) throws IOException, GeneralSecurityException
{ //Sheets service = getSheetsService();
	System.out.println("Creando un Nuevo Archivo");
	
	//creo cuerpo de la peticion
	Spreadsheet requestBody = new Spreadsheet();
	//ajusto propiedades de la peticion
	SpreadsheetProperties propiedades = new SpreadsheetProperties();
	propiedades.setTitle(titulo);
	requestBody.setProperties(propiedades);
	requestBody.setSpreadsheetId(titulo);
	
	//Preparo las hojas de cada archivo
	ArrayList<Sheet> list = new ArrayList<Sheet>();
	for (String item : Hojas) {
		Sheet hojita = new Sheet();
		SheetProperties hojita_p = new SheetProperties();
		hojita_p.setTitle(item);
		hojita.setProperties(hojita_p);
		list.add(hojita);
	}
	
	requestBody.setSheets(list);
	///preparo l apeticion
	Sheets.Spreadsheets.Create request = service.spreadsheets().create(requestBody);
	//ejecuto la peticon
	Spreadsheet response = request.execute();
	System.out.println("----->"+response.getSpreadsheetId());
	return response.getSpreadsheetId();
			
}

public void setheaders(String fecha) throws IOException {
	// TODO Auto-generated method stubUSER_ENTERED
	System.out.println("Insertando Cabeceras");
	for (String item : Hojas) 
	{
		 //Sheets service = getSheetsService();
		///write
	    String rango = getRange(item+"!E1:N");//ultimo dato
	    ///crear una lista de objetos a escribir
	    List<List<Object>> valores = new ArrayList<>();
	    
	    ///creo el objeto
	 
	    ArrayList<String> cabeceras= new ArrayList<String>(Arrays.asList(fecha.split(",")));
	   List<Object> dato = new ArrayList<Object>(cabeceras);
	 
	    
	    valores.add(dato);
	    //crear el valuerange y modificar la configuracion
	    ValueRange rangodevalores = new ValueRange();
	    //System.out.println(rango);
	    rangodevalores.setMajorDimension("ROWS");
	    rangodevalores.setRange(rango);
	    rangodevalores.setValues(valores);
	    
	    
	    ///ejecutar la peticion
	    service.spreadsheets()
	    .values()
	    .update(getSpreadsheetId(), rango, rangodevalores)
	    .setValueInputOption("RAW")
	    .execute();
	    
	   
	    //System.out.println("INSERT RESPONSE: "+response);
	    System.out.println(item+" Insert ok");
		}
	}

public static String getSpreadsheetId() {
	return spreadsheetId;
}

public static void setSpreadsheetId(String spreadsheetId) {
	GoogleSheets.spreadsheetId = spreadsheetId;
}
}	

