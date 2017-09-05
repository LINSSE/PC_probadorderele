import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import javax.mail.MessagingException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;



//client id 571325534848-5gv2trfk3tm7dd1bs8980uok5pd9rhgp.apps.googleusercontent.com
//client secrete aYtej_QeEYei42ygruIvxjpS



import com.fazecast.jSerialComm.SerialPort;

public class Quickstart {
	
	static int cantidad_puertos = 3;
	static int cantidad_sensores = 12;
	static int x = 0;
	final static chartHandler Graficos = new chartHandler(cantidad_puertos, cantidad_sensores);
	final static PortHandler Puertos = new PortHandler(cantidad_puertos);
	static String Dir = "C:/Users/Sambrana Ivan/Google Drive/Registro de Reles/Local/";
	final static ArrayList<JComboBox<String>> portList = new ArrayList<JComboBox<String>>();

	
	//static String Dir = "C:/exceltest/";
	public chartHandler getGraficos()
	{
		return Quickstart.Graficos;
	}
	
	public static void main(String[] args) throws Exception {
		
		///definicion de alarmar
		final GoogleServices GoogleApi = new GoogleServices();
		//iObserver smsObserver = new SmsSender();
		//iObserver mailObserver = new MailSender(GoogleApi);
		//iObserver graphObserver = new GraphHandler();
		ISubject Alarma = new Alarma();
		//Alarma.attach(smsObserver);
		//Alarma.attach(mailObserver);
		//Alarma.attach(graphObserver);
		
		
		final ArrayList<String> Hojas = new ArrayList<>(Arrays.asList(("CIAA 1,CIAA 2,CIAA 3").split(",")));
			
		final ExcelFile Excel = new ExcelFile(Hojas);
		
		//objetos que gestionan las variables y sus anomalias
		final GestionVariable gestionUno = new GestionVariable();
		final GestionVariable gestionDos = new GestionVariable();
		final GestionVariable gestionTres = new GestionVariable();
			
		final JFrame window = new ventana();
		//crear un menu de seleccion de puertos y el boton para empezar
		final String ACTION_1 = "Conectar";
		final JButton connectButton = new JButton(ACTION_1);
		
		
		JPanel topPanel = new JPanel();
		SerialPort[] portNames = SerialPort.getCommPorts();
		for (int i = 0; i < 3; i++) 
		 {
						JComboBox<String> temp = new JComboBox<String>();
						portList.add(temp);
						
						for (int j = 0; j < portNames.length; j++) {
							temp.addItem(portNames[j].getSystemPortName().toString());
							
						}
						
						topPanel.add(portList.get(i));
										
		 }
						
				topPanel.add(connectButton);
				
				
				
				//agrego el panel al panel la botonera
						window.add(topPanel,BorderLayout.NORTH);
		
		
		
		//agrego al panel los graficos
		window.add(Graficos.getPanel(), BorderLayout.CENTER);
		//configura boton conectar
		connectButton.addActionListener(new ActionListener() {
		
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(connectButton.getText().equals("Conectar"))
				{
					System.out.println("Click on Conectar");
					//listo para conectar al pueto
					Puertos.ConfigurePort(portList);			
					
					
					if (Puertos.openPorts())
					{
						connectButton.setText("Desconectar");
						//APAGO LOS COMBOBOX
						SetComboBox(false);
					}
					
					//crear un nuevo hilo que escuche el puerto y lo lleve al grafico
					Thread hilo = new Thread(){
						@Override public void run(){
							System.out.println("Dentro del Hilo");
						Puertos.scannerInit();
						

							while (Puertos.HasNextLine()) {
							try {
								String line = Puertos.getNextLine(0);
								String line2 = Puertos.getNextLine(1);
								String line3 = Puertos.getNextLine(2);
								//divido el string
								//Convierto la cadena de datos en un Arraylist
								System.out.println("[#1]////LEER ENTRADAS");
								ArrayList<ArrayList<String>> dataset = new ArrayList<ArrayList<String>>();
								
								
								ArrayList<String> aList= new ArrayList<String>(Arrays.asList(line.split(",")));
								ArrayList<String> aList2= new ArrayList<String>(Arrays.asList(line2.split(",")));
								ArrayList<String> aList3= new ArrayList<String>(Arrays.asList(line3.split(",")));
							
								dataset.add(aList);
								dataset.add(aList2);
								dataset.add(aList3);
								
								
								///campo de sincronizacion para graficos
								//list.get(0) en lugar de x
								//Graficos.addValues(Integer.parseInt(aList.get(0)),0,aList);
								//Graficos.addValues(Integer.parseInt(aList2.get(0)),1,aList2);
								//Graficos.addValues(Integer.parseInt(aList3.get(0)),2,aList3);
								System.out.println("[#2]////GRAFICAR");
								Graficos.addValues(x,0,aList);
								Graficos.addValues(x,1,aList2);
								Graficos.addValues(x,2,aList3);
								//INCREMENTO CONTADOR
								System.out.println("Incremento"+x+"++");
								x++;
								
								
								
								
								///ENVIO LOS DATOS A GSHETTS
								System.out.println("[#3]////ENVIO LOS DATOS A DRIVE");
								updateFile(GoogleApi);///Actualiza el Target del Sheets
								String horaDeLectura = getHora();
								  aList.add(0,horaDeLectura);
								  aList2.add(0,horaDeLectura);
								  aList3.add(0,horaDeLectura);
								GoogleApi.insert(aList,Hojas.get(0));
								GoogleApi.insert(aList2,Hojas.get(1));
								GoogleApi.insert(aList3,Hojas.get(2));
								
								///
								///Grabar en EXcel Local
								System.out.println("[#4]////Grabar en EXcel Local");
								Excel.CargarExcel(line, Dir+getTitulo(), Hojas.get(0).toString());
								Excel.CargarExcel(line2, Dir+getTitulo(), Hojas.get(1).toString());
								Excel.CargarExcel(line3, Dir+getTitulo(), Hojas.get(2).toString());
								
								///
							/////Registrar Anomalias
								System.out.println("[#5]/////Registrar Anomalias");
								
								
								gestionUno.checkVariables(line);
								gestionUno.cargarEnExcel(Dir+getTitulo()+"_Anomalias_CIAA_1");
								gestionDos.checkVariables(line2);
								gestionDos.cargarEnExcel(Dir+getTitulo()+"_Anomalias_CIAA_2");
								gestionTres.checkVariables(line3);
								gestionTres.cargarEnExcel(Dir+getTitulo()+"_Anomalias_CIAA_3");
								
								
								//LIMPIO LA PANTALLA
								System.out.println("[#6]/////Limpio Pantalla");
								window.repaint();
							} catch (Exception e2) {
								// TODO: handle exception
								System.out.println("!!!e2= "+ e2.toString());
							}
						}
							//CIERRO LOS SCANNER DE PUERTOS
							Puertos.ScannerClose();
						}
					};
					
					hilo.start();
					
				}
				else
				{
					
					//desconetar del puerto
					Puertos.ScannerClose();
					Puertos.cerrarPuertos();
					
					//HABILITO LOS COMBOBOX
					SetComboBox(true);
					connectButton.setText("Conectar");
					Graficos.clean();
					//REINICIO EL CONTADOR DE GRAFICO
					x = 0;
				}
			}
		});
		
		//MOSTRAR PANTALLA
		window.setVisible(true);
		
		
	}

	

	public static String getTitulo()
	{
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		// Get the date today using Calendar object.
		Date today = Calendar.getInstance().getTime();        
		// Using DateFormat format method we can create a string 
		// representation of a date with the defined format.
		String reportDate = df.format(today);
		return reportDate+"_Registro Rele";
	}
	
	public static String getFecha()
	{
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

		// Get the date today using Calendar object.
		Date today = Calendar.getInstance().getTime();        
		// Using DateFormat format method we can create a string 
		// representation of a date with the defined format.
		String reportDate = df.format(today);
		return reportDate;
	}
	
	public static String getHora()
	{
		DateFormat df = new SimpleDateFormat("hh:mm:ss");

		// Get the date today using Calendar object.
		Date today = Calendar.getInstance().getTime();        
		// Using DateFormat format method we can create a string 
		// representation of a date with the defined format.
		String reportDate = df.format(today);
		return reportDate;
	}
	
	public static void updateFile(GoogleServices GoogleApi) throws IOException, GeneralSecurityException
	{
		try {
			//DriveApi.createFolder("Registro de Reles");
			
			String actualFile = GoogleApi.fileExist(getTitulo()); 
			if( actualFile != null)
			{
				System.out.println("El Archivo<<"+getTitulo()+">>Ya existe");
				GoogleApi.setActualFile(actualFile);
				
			}
			else
			{
				actualFile = GoogleApi.CreateFile(getTitulo());
				//DriveApi.moveToFolder(actualFile);
				GoogleApi.setActualFile(actualFile);
				GoogleApi.setheaders(getFecha());
				
			}
			
		
			
			
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
	}

	public static String getPort(int index)
	{
		return portList.get(0).getSelectedItem().toString();
	}
	
	public static void SetComboBox(boolean v)
	{
		for (JComboBox<String> jComboBox : portList) {
			jComboBox.setEnabled(v);
		}
	}
	
}
