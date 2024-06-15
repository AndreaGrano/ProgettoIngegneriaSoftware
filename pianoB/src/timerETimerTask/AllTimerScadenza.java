package timerETimerTask;

import java.util.HashSet;

import dominioCredito.Credito;

public class AllTimerScadenza extends HashSet<TimerScadenza>{
	private static final long serialVersionUID = 1L;
	
	//implementazione pattern Singleton
	private static AllTimerScadenza instance = null;
	
	protected AllTimerScadenza() {
		super();
	}
	
	public static AllTimerScadenza getInstance() {
		if(instance == null) {
			instance = new AllTimerScadenza();
		}
		
		return instance;
	}
	
	//metodi per aggiungere e ottenere un TimerScadenza dato un Credito
	public void addTimerScadenza(TimerScadenza timerScadenza) {
		this.add(timerScadenza); //il metodo add ritorna un boolean di conferma, ma in questo caso è ignorabile
	}
	
	public TimerScadenza getTimerScadenza(Credito credito) {
		for(TimerScadenza timerScadenza : this) {
			if(timerScadenza.getCredito().equals(credito)) {
				return timerScadenza;
			}
		}
		
		return null;
	}
}
