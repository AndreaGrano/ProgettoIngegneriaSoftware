package memorizzazione;

import java.time.LocalDateTime;

import dominioBonifico.BonificoNonRiconciliato;
import dominioBonifico.FlussoBancario;
import gestioneAutomaticaCredito.RiconciliazioneController;
import log.LogController;
import daoCredito.BonificoDAO;
import daoCredito.DAOFactoryCredito;

public class MemorizzazioneController implements IMemorizzazione{
	private DAOFactoryCredito factory;
	
	public MemorizzazioneController(DAOFactoryCredito factory) {
		this.factory = factory;
	}
	
	public void memorizzaFlusso(FlussoBancario flussoBancario) {
		//si scrive nel log l'operazione che si sta eseguendo
		LogController logController = new LogController();
		String[] operazionePrincipale = {LocalDateTime.now().toString(), " - memorizzazione di un flusso bancario - ", this.getClass().getSimpleName()};
		logController.printLogOperazione(operazionePrincipale);
				
		//si portano i vari bonifici (necessariamente non riconciliati) presenti nel flusso sul DB
		BonificoDAO bonificoDAO = factory.getBonificoDAO();
		for(BonificoNonRiconciliato bonificoNonRiconciliato : flussoBancario) {
			//si scrive nel log l'operazione di creazione di un bonifico
			String[] operazioniInnestate = {LocalDateTime.now().toString(), " - aggiunta di un nuovo bonifico - ", this.getClass().getSimpleName()};
			logController.printLogOperazione(operazioniInnestate);
			String[] messaggio = {LocalDateTime.now().toString(), " - MemorizzaFlusso, creazione bonifico: " + bonificoNonRiconciliato.getCausale(), " - invio - ", this.getClass().getSimpleName()};
			logController.printLogMessaggio(messaggio);
			
			//si crea il bonifico sul DB
			bonificoDAO.create(bonificoNonRiconciliato);
		}
		
		//si avvia la riconciliazione automatica
		RiconciliazioneController riconciliazioneController = new RiconciliazioneController(factory);
		riconciliazioneController.riconciliazione(flussoBancario);
	}

}
