
import java.util.ArrayList;

public class Alarma implements ISubject {
private static ArrayList<iObserver> observadores = new ArrayList<iObserver>();

	@Override
	public void attach(iObserver observador) {
		// TODO Auto-generated method stub
		observadores.add(observador);
		
	}

	@Override
	public void dettach(iObserver observador) {
		// TODO Auto-generated method stub
		observadores.remove(observador);
		
	}

	@Override
	public void notifyObservers() {
		System.out.println("                               NOTIFCANDO OBSERVADORES");
		// TODO Auto-generated method stub
		for (iObserver obs : observadores) {
			obs.update();
		}
		
	}
	
	public Alarma()
	{	//observer por defecto

	}

}
