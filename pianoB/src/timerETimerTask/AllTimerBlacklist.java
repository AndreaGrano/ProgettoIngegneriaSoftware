package timerETimerTask;

import java.util.HashSet;

import dominioCredito.Credito;

public class AllTimerBlacklist extends HashSet<TimerBlacklist>{
	private static final long serialVersionUID = 1L;
	
	//implementazione pattern Singleton
	private static AllTimerBlacklist instance = null;
	
	protected AllTimerBlacklist() {
		super();
	}
	
	public static AllTimerBlacklist getInstance() {
		if(instance == null) {
			instance = new AllTimerBlacklist();
		}
		
		return instance;
	}
	
	//metodi per aggiungere e ottenere un TimerBlacklist dato un Credito
	public void addTimerBlacklist(TimerBlacklist timerBlacklist) {
		this.add(timerBlacklist); //il metodo add ritorna un boolean di conferma, ma in questo caso è ignorabile
	}
	
	public TimerBlacklist getTimerBlacklist(Credito credito) {
		for(TimerBlacklist timerBlacklist : this) {
			if(timerBlacklist.getCredito().equals(credito)) {
				return timerBlacklist;
			}
		}
		
		return null;
	}
}
