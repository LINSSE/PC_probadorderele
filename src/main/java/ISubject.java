

public interface ISubject {
public void attach(iObserver observador);
public void dettach(iObserver observador);
public void notifyObservers();
}
