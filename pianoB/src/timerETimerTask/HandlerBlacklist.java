package timerETimerTask;

import java.util.TimerTask;

import dominioCredito.Cliente;
import dominioCredito.CreditoNonRiconciliato;
import gestioneAutomaticaCredito.InserimentoBlacklistController;
import daoCredito.DAOFactoryCredito;

public class HandlerBlacklist extends TimerTask{
	private CreditoNonRiconciliato creditoNonRiconciliato;
	
	public HandlerBlacklist(CreditoNonRiconciliato creditoNonRiconciliato) {
		super();
		
		this.creditoNonRiconciliato = creditoNonRiconciliato;
	}

	@Override
	public void run() {
		DAOFactoryCredito factory = DAOFactoryCredito.getDAOFactoryCredito(0);
		InserimentoBlacklistController inserimentoBlacklistController = new InserimentoBlacklistController(factory);
		
		Cliente cliente = creditoNonRiconciliato.getCliente();
		inserimentoBlacklistController.inserisciInBlacklist(cliente);
	}
	
	public CreditoNonRiconciliato getCredito() {
		return creditoNonRiconciliato;
	}

	public void setCredito(CreditoNonRiconciliato creditoNonRiconciliato) {
		this.creditoNonRiconciliato = creditoNonRiconciliato;
	}

}
