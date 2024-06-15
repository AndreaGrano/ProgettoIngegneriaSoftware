package gestioneAutomaticaCredito;

import java.time.LocalDateTime;

import dominioCredito.Credito;
import dominioParametriSistema.ParametriSistema;
import log.LogController;
import daoCredito.CreditoDAO;
import daoCredito.DAOFactoryCredito;

public class GestioneRitardoController implements IGestioneRitardo{
	private DAOFactoryCredito factory;

	public GestioneRitardoController(DAOFactoryCredito factory) {
		this.factory = factory;
	}

	@Override
	public void gestioneRitardo(Credito credito) {
		//si preleva la percentuale di multa
		ParametriSistema parametriSistema = ParametriSistema.getInstance();
		double percentualeMulta = parametriSistema.getPercentualeMulta();
		
		//si aggiorna l'importo
		credito.setImporto(credito.getImporto() + credito.getImporto() * percentualeMulta / 100);
		
		
		//si scrive nel log l'operazione di aggiornamento del credito
		LogController logController = new LogController();
		String[] operazione = {LocalDateTime.now().toString(), " - aggiornamento importo credito - ", this.getClass().getSimpleName()};
		logController.printLogOperazione(operazione);
		String[] messaggio = {LocalDateTime.now().toString(), " - GestioneRitardo del credito: " + credito.getCausale() , " - invio - ", this.getClass().getSimpleName()};
		logController.printLogMessaggio(messaggio);
		
		//si aggiorna il credito sul DB
		CreditoDAO creditoDAO = factory.getCreditoDAO();
		creditoDAO.update(credito);
	}
}
