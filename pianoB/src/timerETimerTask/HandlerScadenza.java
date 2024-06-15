package timerETimerTask;

import java.util.TimerTask;

import dominioCredito.CreditoNonRiconciliato;
import gestioneAutomaticaCredito.GestioneRitardoController;
import daoCredito.DAOFactoryCredito;

public class HandlerScadenza extends TimerTask{
	private CreditoNonRiconciliato creditoNonRiconciliato;
	
	public HandlerScadenza(CreditoNonRiconciliato creditoNonRiconciliato) {
		super();
		
		this.creditoNonRiconciliato = creditoNonRiconciliato;
	}

	@Override
	public void run() {
		DAOFactoryCredito factory = DAOFactoryCredito.getDAOFactoryCredito(0);
		GestioneRitardoController gestioneRitardoController = new GestioneRitardoController(factory);
		
		gestioneRitardoController.gestioneRitardo(creditoNonRiconciliato);
	}

	public CreditoNonRiconciliato getCreditoNonRiconciliato() {
		return creditoNonRiconciliato;
	}

	public void setCredito(CreditoNonRiconciliato creditoNonRiconciliato) {
		this.creditoNonRiconciliato = creditoNonRiconciliato;
	}
}
