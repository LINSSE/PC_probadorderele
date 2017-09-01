import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


public class chartHandler {

	XYSeries[][] seriesArray;
	XYSeriesCollection[] datasetArray;//aqui estan todos los datos
	JFreeChart[] chartArray;
	ArrayList<String> Cabeceras_ui;
	private int puertos;
	private int sensores;
	private int graph_limit = 10;
	
	private int getPuertos() {
		return puertos;
	}

	private void setPuertos(int puertos) {
		this.puertos = puertos;
	}

	private int getSensores() {
		return sensores;
	}

	private void setSensores(int sensores) {
		this.sensores = sensores;
	}

	public chartHandler(int puertos, int sensores)
	{
		this.setSensores(sensores);
		this.setPuertos(puertos);
		this.setSeriesArray(new XYSeries[puertos][sensores]);
		this.setDatasetArray( new XYSeriesCollection[sensores]);
		this.setChartArray(new JFreeChart[sensores]);
		
		
	    Cabeceras_ui = new ArrayList<String>();
		Cabeceras_ui.add("Hora Medicion");
		for(int i = 1; i<=6; i++)
		{
			Cabeceras_ui.add("Contacto "+i+" Normal Cerrado");
			Cabeceras_ui.add("Contacto "+i+" Normal Abierto");
		}
		
		this.dataBinding();
	}

	public XYSeries[][] getSeriesArray() {
		return seriesArray;
	}

	public void setSeriesArray(XYSeries[][] seriesArray) {
		this.seriesArray = seriesArray;
	}

	public XYSeriesCollection[] getDatasetArray() {
		return datasetArray;
	}

	public void setDatasetArray(XYSeriesCollection[] datasetArray) {
		this.datasetArray = datasetArray;
	}

	public JFreeChart[] getChartArray() {
		return chartArray;
	}

	public void setChartArray(JFreeChart[] chartArray) {
		this.chartArray = chartArray;
	}
	
	public void dataBinding()
	{
		//VINCULO LAS SERIE DE DATOS CON LOS DATASET PARA GRAFICAR
				for (int i = 0; i < datasetArray.length; i++) {//13
					
					//creo un data ser por cada sensor
					datasetArray[i] = new XYSeriesCollection();
					
					for (int j = 0; j < seriesArray.length; j++) {//3
						//creo una serie por cada puerto			
						seriesArray[j][i] = new XYSeries("Ciaa_"+(j+1));//creo la serie con el nombre CIAA_n
						seriesArray[j][i].setMaximumItemCount(this.graph_limit);//limito a 10 la informacion ah graficar
						
						datasetArray[i].addSeries(seriesArray[j][i]);//Vinculo la serie a cada dataset
					}
					//Creo un grafico de lineas por cada sensor
					chartArray[i] = ChartFactory.createXYLineChart(Cabeceras_ui.get(i+1), null, null, datasetArray[i]);
					chartArray[i].getXYPlot().getRangeAxis().setRange(0, 300);;
					//agrego al panel cada grafico
					
				}
	}
	
	public JPanel getPanel()
	{
		JPanel centerPanel = new JPanel(new GridLayout(3, 5));
		for(int i = 0; i < chartArray.length; i++)
		{
			centerPanel.add(new ChartPanel(chartArray[i]));
		}
		
		return centerPanel;
	}
	
	public void addValues(int x,int index,ArrayList<String>values)
	{
		System.out.println("ChartHandler: addValues("+x+","+index+")");
		for (int j = 0; j < chartArray.length; j++) 
		{
			
			//CONVIERTO LOS VALORES EN NUMEROS Y LOS CARGO A LA SERIE DE DATOS
			double number = Double.parseDouble(values.get(j));
			this.getSeriesArray()[index][j].add(x,number);
		}
	}
	
	public void clean()
	{
		//LIMPIO LOS GRAFICOS
		for (int i = 0; i < seriesArray.length; i++) {
			for (int j = 0; j < seriesArray[i].length; j++) {
				seriesArray[i][j].clear();
			}
		}
	}
	
	
	
	
	
	
}
