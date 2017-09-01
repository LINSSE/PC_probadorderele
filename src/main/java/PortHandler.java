import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import javax.swing.JComboBox;

import com.fazecast.jSerialComm.SerialPort;


public class PortHandler {
	private static ArrayList<SerialPort> PortArray = new ArrayList<SerialPort>();
	private static ArrayList<Scanner> ScannerArray = new ArrayList<Scanner>();
	//Constructor
	public PortHandler(int cantidad_puertos)
	{
		
		for (int i = 0; i < cantidad_puertos; i++) 
		{
			SerialPort port = null;
			PortArray.add(port);
		}
	}
	
	

	public void ConfigurePort(ArrayList<JComboBox<String>> portlist)
	{
		
		for (int j = 0; j < portlist.size(); j++) 
		{
			PortArray.set(j, SerialPort.getCommPort(portlist.get(j).getSelectedItem().toString()));
			this.getPort(j).setBaudRate(115200);
			this.getPort(j).setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
		}
		
		
		
	}
	
	public SerialPort getPort(int index)
	{
		return PortArray.get(index);
		
	}
		
	public void cerrarPuertos(){
	 for (SerialPort item : PortArray) 
	 {
		item.closePort();
	 }
	}
	
	public Boolean openPorts()
	{
		for (SerialPort serialPort : PortArray) {
			if (!serialPort.openPort()) 
			{
				return false;
			}
		}
		return true;
		
	}
	
	public void scannerInit()
	{
		for (SerialPort serialPort : PortArray) {
			ScannerArray.add(new Scanner(serialPort.getInputStream()));
		}
	}
	
	public boolean HasNextLine()
	{
		boolean ret = true;
		for (Scanner scanner : ScannerArray) {
			if (!scanner.hasNext()) 
			{
				ret = false;
			}
		}
		return ret;
	}
	
	public String getNextLine(int index)
	{
		return ScannerArray.get(index).nextLine();
	}
	
	public void ScannerClose()
	{
		for (Scanner scanner : ScannerArray) {
			scanner.close();
		}
	}
	
}
