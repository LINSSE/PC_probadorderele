import java.util.ArrayList;


public class Variable {

	//attributes
	private String nombreVariable;
	private int max;
	private ArrayList<Double> valores;
	private ArrayList<Anomalia> anomalias = new ArrayList<Anomalia>(); 

	//setter and getters
	public String getNombreVariable(){
		return this.nombreVariable;
	}
	
	public double getValor(){
		//retorna el ultimo valor almacenado, el mas actual
		return this.valores.get(this.getValores().size()-1);
	}
	
	public ArrayList<Double> getValores(){
		return this.valores;
	}
	
	public ArrayList<Anomalia> getAnomalias(){
		return this.anomalias;
	}
	
	public int getMax(){
		return this.max;
	}
	
	public void setValor(double p_valor){
		this.valores.add(p_valor);
		
		//solo guarda hasta 100 valores, arquitectura FIFO
		if(this.getValores().size() > this.getMax()){
			this.getValores().remove(0);
		}
	}
	
	public void setAnomalias(ArrayList<Anomalia> p_anomalias){
		this.anomalias = p_anomalias;
	}
	
	public void setMax(int p_max){
		this.max = p_max;
	}
	
	///NULL POINT EXCEPTION
	public void setAnomaliaMin(AnomaliaMin p_anomMin){
		this.getAnomalias().add(p_anomMin);
	}
	
	public void setAnomaliaMax(AnomaliaMax p_anomMax){
		this.getAnomalias().add(p_anomMax);
	}
	
	//constructor
	public Variable(String p_nombreVariable, int p_max) {
		this.nombreVariable = p_nombreVariable;
		this.valores = new ArrayList<Double>();
		this.max = p_max;
	}

	//check general
	public void check(){
		for (Anomalia a : this.getAnomalias()) {
	    	  a.check(this.getValores(), this.getNombreVariable());
		}
	}
	
	
}

