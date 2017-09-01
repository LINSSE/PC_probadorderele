import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AnomaliaMin extends Anomalia {

	//Constructor
	public AnomaliaMin(double p_data){
		super(p_data);
	}
	
	
	//check MIN anomaly
	public ResultadoAnomalia check(ArrayList<Double> p_data, String p_nombreVariable){
		String msg;
		double diff;
		ResultadoAnomalia r = new ResultadoAnomalia();
		
		//get current date
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String strDate = sdf.format(cal.getTime());
		        
		if(super.refValue > p_data.get(p_data.size()-1)){
			diff = super.refValue - (p_data.get(p_data.size()-1));
			msg = "<log date="+ strDate +"><type>MIN</type><variable>"+p_nombreVariable+"</variable><ref_value>"+super.refValue +"</ref_value><actual_value>"+p_data.get(p_data.size()-1)+"</actual_value><diff>"+diff+"<diff></log>";
			r.add(true, msg);
		}
		this.setFecha(strDate);
		this.setResultadoAnomalia(r);
		return r;
	}
	

	
}
