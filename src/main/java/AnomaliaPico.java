import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AnomaliaPico extends Anomalia  {
	
	private int maximoValores;
	
	//Constructor
	public AnomaliaPico(double p_data){
		super(p_data);
		this.maximoValores = 100; // para que check se implemente despues de 100 valores leidos
	}
	
	//setters
	public int getMaximoValores(){
		return this.maximoValores;
	}
	
	//verifica si la anomalia supera un valor de referencia
	public ResultadoAnomalia check(ArrayList<Double> p_data, String p_nombreVariable){ 
		String msg;
		double desv = 0;
		ResultadoAnomalia r = new ResultadoAnomalia();
		
		//solo se implementa cuando existen más de un maximo de valores leidos
		if (p_data.size() > this.getMaximoValores()){
			
			//get current date
			Calendar cal = Calendar.getInstance();
	        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	        String strDate = sdf.format(cal.getTime());
	        
	        //for (double value : p_data){
	        for (int i = 0; i< p_data.size()-1;i++){	
	        	//acumulacion de valores
	        	desv = desv + p_data.get(i);
	        }
	        
	        //Saca promedio de los valores
	        desv = desv / p_data.size();
	        
	        //desvio en porcentaje positivo
	        desv = Math.abs(desv / p_data.get(p_data.size()-1) * 100);
	        
	        if(desv >= this.getRefValue()){
	            msg = "<log date="+ strDate +"><type>DESVIO</type><variable>"+p_nombreVariable+"</variable><ref_value>"+this.getRefValue() +"</ref_value><actual_value>"+p_data.get(p_data.size()-1)+"</actual_value><desv>"+desv+"<desv></log>";
	    		r.add(true, msg);
	        }
			
		}

		return r;
	}
}
