package gestioneAutomaticaCredito;

import java.time.LocalDateTime;

import dominioBonifico.BonificoNonRiconciliato;
import dominioBonifico.FlussoBancario;
import dominioCredito.*;
import log.LogController;
import daoCredito.BonificoDAO;
import daoCredito.CreditoDAO;
import daoCredito.DAOFactoryCredito;

public class RiconciliazioneController implements IRiconciliazione{
	private DAOFactoryCredito factory;
	
	public RiconciliazioneController (DAOFactoryCredito factory) {
		this.factory = factory;
	}

	@Override
	public void riconciliazione(FlussoBancario flussoBancario) {
		//si scrive nel log l'operazione che si sta eseguendo
		LogController logController = new LogController();
		String[] operazione = {LocalDateTime.now().toString(), " - riconciliazione automatica - ", this.getClass().getSimpleName()};
		logController.printLogOperazione(operazione);
		
		//si esegue la riconciliazione automatica
		BonificoDAO bonificoDAO = factory.getBonificoDAO();
		CreditoDAO creditoDAO = factory.getCreditoDAO();
		Crediti creditiNonRiconciliati = creditoDAO.readCreditiNonRiconciliati();
		for(BonificoNonRiconciliato bonificoNonRiconciliato : flussoBancario) {
			//scrivo nel log l'operazione di lettura dell'id del bonifico
			String[] operazioneId = {LocalDateTime.now().toString(), " - lettura id di un bonifico - ", this.getClass().getSimpleName()};
			logController.printLogOperazione(operazioneId);
			String[] messaggioId = {LocalDateTime.now().toString(), " - Riconciliazione, lettura id del bonifico: " + bonificoNonRiconciliato.getCausale() , " - ricezione - ", this.getClass().getSimpleName()};
			logController.printLogMessaggio(messaggioId);
			
			//si legge l'id del bonifico dal DB (precedentemente salvato da memorizzazione controller) e lo si "carica" nell'oggetto
			bonificoNonRiconciliato.setId(bonificoDAO.readId(bonificoNonRiconciliato));
			
			
			//con un bonifico si possono saldare più crediti; estraggo le varie "causali"
			String[] causaliBonifico = bonificoNonRiconciliato.getCausale().split(" ");
			
			
			//per ognuna delle "causali" di un bonifico provo a eseguire la riconciliazione
			//si tiene il conto di quante riconciliazioni vengano eseguite
			int countRiconciliazioni = 0;
			for(String causale : causaliBonifico) {
				for(Credito creditoNonRiconciliato : creditiNonRiconciliati) {
					if(causale.trim().equals(creditoNonRiconciliato.getCausale())) {
						//si scrive l'operazione di aggiornamento credito sul log
						String[] operazioneCredito = {LocalDateTime.now().toString(), " - riconciliazione di un credito - ", this.getClass().getSimpleName()};
						logController.printLogOperazione(operazioneCredito);
						String[] messaggioCredito = {LocalDateTime.now().toString(), " - Riconciliazione, aggiornamento credito: " + creditoNonRiconciliato.getCausale() , " - invio - ", this.getClass().getSimpleName()};
						logController.printLogMessaggio(messaggioCredito);
						
						//si aggiorna il credito riconciliato sul DB
						CreditoRiconciliato creditoRiconciliato = new CreditoRiconciliato(creditoNonRiconciliato.getId(), creditoNonRiconciliato.getImporto(), creditoNonRiconciliato.getDataStipula(),
																							creditoNonRiconciliato.getCausale(), creditoNonRiconciliato.getCliente(), bonificoNonRiconciliato);
						creditoDAO.update(creditoRiconciliato, bonificoNonRiconciliato.getId());
						
						//si aumenta il counter di riconciliazioni eseguite per questo bonifico
						countRiconciliazioni++;
					}
				}
			}
			
			//si scrive l'operazione di aggiornamento bonifico sul log
			String[] operazioneBonifico = {LocalDateTime.now().toString(), " - riconciliazione di un credito - ", this.getClass().getSimpleName()};
			logController.printLogOperazione(operazioneBonifico);
			String[] messaggioBonifico = {LocalDateTime.now().toString(), " - Riconciliazione, aggiornamento bonifico: " + bonificoNonRiconciliato.getCausale(), " - invio - ", this.getClass().getSimpleName()};
			logController.printLogMessaggio(messaggioBonifico);
			
			//si aggiorna il bonifico su DB
			bonificoDAO.update(bonificoNonRiconciliato, countRiconciliazioni);
		}
	}
}
