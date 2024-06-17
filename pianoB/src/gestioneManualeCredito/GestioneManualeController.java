package gestioneManualeCredito;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import daoCredito.BonificoDAO;
import daoCredito.ClienteDAO;
import daoCredito.CreditoDAO;
import daoCredito.DAOFactoryCredito;
import dominioBonifico.*;
import dominioCredito.*;
import dominioParametriSistema.ParametriSistema;
import log.LogController;

public class GestioneManualeController implements IRiconciliazioneManuale, IGestioneCredito, IVisualizzazioneBonifici{
	private DAOFactoryCredito factory;
	
	public GestioneManualeController(DAOFactoryCredito factory) {
		this.factory = factory;
	}
	
	@Override
	public Bonifici visualizzaTuttiBonifici() {
		//si scrive nel log l'operazione di lettura di tutti i bonifici
		LogController logController = new LogController();
		String[] operazione = {LocalDateTime.now().toString(), " - visualizza tutti bonifici - ", this.getClass().getSimpleName()};
		logController.printLogOperazione(operazione);
		String[] messaggio = {LocalDateTime.now().toString(), " - VisualizzaTuttiBonifici - ", "ricezione - ", this.getClass().getSimpleName()};
		logController.printLogMessaggio(messaggio);
		
		//si esegue l'operazione
		BonificoDAO bonificoDAO = factory.getBonificoDAO();
		Bonifici bonifici = bonificoDAO.readAll();
		
		return bonifici;
	}

	@Override
	public BonificiRiconciliati visualizzaBonificiRiconciliati() {
		//si scrive nel log l'operazione di lettura dei bonifici riconciliati
		LogController logController = new LogController();
		String[] operazione = {LocalDateTime.now().toString(), " - visualizza bonifici riconciliati - ", this.getClass().getSimpleName()};
		logController.printLogOperazione(operazione);
		String[] messaggio = {LocalDateTime.now().toString(), " - VisualizzaBonificiRiconciliati - ", "ricezione - ", this.getClass().getSimpleName()};
		logController.printLogMessaggio(messaggio);
		
		//si esegue l'operazione
		BonificoDAO bonificoDAO = factory.getBonificoDAO();
		BonificiRiconciliati bonificiRiconciliati = new BonificiRiconciliati();
		Bonifici bonifici = bonificoDAO.readAll();
		BonificiNonRiconciliati bonificiNonRiconciliati = bonificoDAO.readBonificiNonRiconciliati();
		
		boolean trovato;
		for(Bonifico bonifico : bonifici) {
			trovato = false;
			for(BonificoNonRiconciliato bonificoNonRiconciliato : bonificiNonRiconciliati) {
				if(bonifico.equals(bonificoNonRiconciliato)) {
					trovato = true;
				}
			}
			
			if(!trovato) {
				bonificiRiconciliati.add(new BonificoRiconciliato(bonifico.getId(), bonifico.getIBAN(), bonifico.getDataValuta(), bonifico.getCausale(), bonifico.getImporto()));
			}
		}
		
		return bonificiRiconciliati;
	}

	@Override
	public BonificiNonRiconciliati visualizzaBonificiNonRiconciliati() {
		//si scrive nel log l'operazione di lettura dei bonifici non riconciliati
		LogController logController = new LogController();
		String[] operazione = {LocalDateTime.now().toString(), " - visualizza bonifici non riconciliati - ", this.getClass().getSimpleName()};
		logController.printLogOperazione(operazione);
		String[] messaggio = {LocalDateTime.now().toString(), " - VisualizzaBonificiNonRiconciliati - ", "ricezione - ", this.getClass().getSimpleName()};
		logController.printLogMessaggio(messaggio);
		
		//si esegue l'operazione
		BonificoDAO bonificoDAO = factory.getBonificoDAO();
		BonificiNonRiconciliati bonificiNonRiconciliati = bonificoDAO.readBonificiNonRiconciliati();
		
		return bonificiNonRiconciliati;
	}

	@Override
	public int registraCredito(Cliente cliente, double importo, LocalDate dataStipula, String causale) {
		//si scrive nel log l'operazione di lettura della blacklist
		LogController logController = new LogController();
		String[] operazioneBlacklist = {LocalDateTime.now().toString(), " - lettura blacklist - ", this.getClass().getSimpleName()};
		logController.printLogOperazione(operazioneBlacklist);
		String[] messaggioBlacklist = {LocalDateTime.now().toString(), "RegistraCredito, lettura blacklist", "ricezione - ", this.getClass().getSimpleName()};
		logController.printLogMessaggio(messaggioBlacklist);
				
		//si verifica che il cliente non sia in blacklist, altrimenti si torna -1
		ClienteDAO clienteDAO = factory.getClienteDAO();
		Blacklist blacklist = clienteDAO.readClientiInBlacklist();
		if(blacklist.contains(cliente))
			return -1; //codice che identifica l'errore UTENTE IN BLACKLIST
		
		//si verifica che la causale sia stata scritta nel formato giusto altrimenti si torna -2
		ParametriSistema parametriSistema = ParametriSistema.getInstance();
		if(!causale.matches(parametriSistema.getFormatoCausale()))
			return -2; //codice che identifica l'errore FORMATO CAUSALE ERRATO
		
		
		//si scrive nel log l'operazione di lettura ti tutti i clienti
		String[] operazioneClienti = {LocalDateTime.now().toString(), " - lettura tutti clienti - ", this.getClass().getSimpleName()};
		logController.printLogOperazione(operazioneClienti);
		String[] messaggioClienti = {LocalDateTime.now().toString(), " - RegistrazioneCredito, lettura clienti", "ricezione - ", this.getClass().getSimpleName()};
		logController.printLogMessaggio(messaggioClienti);
		
		//si verifica se cliente sia o meno presente sul DB; se necessario, lo si inserisce
		Set<Cliente> clienti = clienteDAO.readAll();
		
		boolean presente = false;
		for(Cliente c : clienti) {
			if(c.getCodiceFiscale().equals(cliente.getCodiceFiscale())) {
				presente = true;
				break;
			}
		}
		
		if(!presente) {
			//si scrive nel log l'operazione di creazione del cliente
			String[] operazioneCliente = {LocalDateTime.now().toString(), " - creazione cliente - ", this.getClass().getSimpleName()};
			logController.printLogOperazione(operazioneCliente);
			String[] messaggioCliente = {LocalDateTime.now().toString(), " - RegistrazioneCredito, creazione cliente - " + causale, " - invio - ", this.getClass().getSimpleName()};
			logController.printLogMessaggio(messaggioCliente);
			
			//si inserisce il cliente nel DB
			clienteDAO.create(cliente);
		}
		
		//per riassegnare l'id
		cliente = clienteDAO.readByCF(cliente.getCodiceFiscale());
		
		//si scrive nel log l'operazione di registrazione del credito
		String[] operazioneCredito = {LocalDateTime.now().toString(), " - creazione credito - ", this.getClass().getSimpleName()};
		logController.printLogOperazione(operazioneCredito);
		String[] messaggioCredito = {LocalDateTime.now().toString(), " - RegistrazioneCredito, creazione credito - " + causale, " - invio - ", this.getClass().getSimpleName()};
		logController.printLogMessaggio(messaggioCredito);
		
		//si inserisce il credito nel DB
		CreditoNonRiconciliato creditoNonRiconciliato = new CreditoNonRiconciliato(importo, dataStipula, causale, cliente);
		CreditoDAO creditoDAO = factory.getCreditoDAO();
		creditoDAO.create(creditoNonRiconciliato);
		return 0;
	}

	@Override
	public Crediti visualizzaCrediti() {
		//si scrive nel log l'operazione di lettura di tutti i crediti
		LogController logController = new LogController();
		String[] operazione = {LocalDateTime.now().toString(), " - visualizza tutti i crediti - ", this.getClass().getSimpleName()};
		logController.printLogOperazione(operazione);
		String[] messaggio = {LocalDateTime.now().toString(), " - VisualizzaCrediti - ", "ricezione - ", this.getClass().getSimpleName()};
		logController.printLogMessaggio(messaggio);
		
		//si esegue l'operazione
		CreditoDAO creditoDAO = factory.getCreditoDAO();
		Crediti crediti = creditoDAO.readAll();
		
		return crediti;
	}

	@Override
	public CreditiScaduti visualizzaCreditiScaduti() {
		//si scrive nel log l'operazione di lettura dei crediti scaduti
		LogController logController = new LogController();
		String[] operazione = {LocalDateTime.now().toString(), " - visualizza crediti scaduti - ", this.getClass().getSimpleName()};
		logController.printLogOperazione(operazione);
		String[] messaggio = {LocalDateTime.now().toString(), " - VisualizzaCreditiScaduti - ", "ricezione - ", this.getClass().getSimpleName()};
		logController.printLogMessaggio(messaggio);
		
		//si esegue l'operazione		
		CreditoDAO creditoDAO = factory.getCreditoDAO();
		CreditiScaduti creditiScaduti = creditoDAO.readCreditiScaduti();
		
		return creditiScaduti;
	}

	@Override
	public boolean riconciliazioneManuale(BonificoNonRiconciliato bonificoNonRiconciliato, CreditiNonRiconciliati creditiNonRiconciliati) {
		//si scrive nel log l'operazione di aggiornamento del bonifico
		LogController logController = new LogController();
		String[] operazioneBonifico = {LocalDateTime.now().toString(), " - aggiornamento bonifico - ", this.getClass().getSimpleName()};
		logController.printLogOperazione(operazioneBonifico);
		String[] messaggioBonifico = {LocalDateTime.now().toString(), " - RiconciliazioneManuale, aggiornamento bonifico - " + bonificoNonRiconciliato.getCausale(), " - invio - ", this.getClass().getSimpleName()};
		logController.printLogMessaggio(messaggioBonifico);
		
		//si aggiorna il bonifico sul DB
		BonificoRiconciliato bonificoRiconciliato = new BonificoRiconciliato(bonificoNonRiconciliato.getId(), bonificoNonRiconciliato.getIBAN(), bonificoNonRiconciliato.getDataValuta(),
																				bonificoNonRiconciliato.getCausale(), bonificoNonRiconciliato.getImporto());
		BonificoDAO bonificoDAO = factory.getBonificoDAO();
		bonificoDAO.update(bonificoRiconciliato, creditiNonRiconciliati.size());
		
		
		//si aggiornano i crediti sul DB
		CreditoDAO creditoDAO = factory.getCreditoDAO();
		for(CreditoNonRiconciliato creditoNonRiconciliato : creditiNonRiconciliati) {
			//si scrive nel log l'operazione di aggiornamento del credito
			String[] operazioneCredito = {LocalDateTime.now().toString(), " - aggiornamento credito - ", this.getClass().getSimpleName()};
			logController.printLogOperazione(operazioneCredito);
			String[] messaggioCredito = {LocalDateTime.now().toString(), " - RiconciliazioneManuale, aggiornamento credito - " + creditoNonRiconciliato.getCausale(), " - invio - ", this.getClass().getSimpleName()};
			logController.printLogMessaggio(messaggioCredito);
			
			//si aggiorna il credito sul DB
			CreditoRiconciliato creditoRiconciliato = new CreditoRiconciliato(creditoNonRiconciliato.getId(), creditoNonRiconciliato.getImporto(), creditoNonRiconciliato.getDataStipula(),
																				creditoNonRiconciliato.getCausale(), creditoNonRiconciliato.getCliente(), bonificoRiconciliato);
			creditoDAO.update(creditoRiconciliato, bonificoRiconciliato.getId());
		}
		
		return true;
	}
	
}
