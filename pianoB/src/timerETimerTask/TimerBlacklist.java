package timerETimerTask;

import java.util.Timer;

import dominioCredito.Credito;

public class TimerBlacklist extends Timer{
private Credito credito;
	
	public TimerBlacklist(Credito credito) {
		super();
		
		this.credito = credito;
	}

	public Credito getCredito() {
		return credito;
	}

	public void setCredito(Credito credito) {
		this.credito = credito;
	}
}
