package gestioneOperatori;

import java.time.LocalDateTime;

import daoOperatori.DAOFactoryOperatori;
import daoOperatori.OperatoreDAO;
import dominioOperatore.Operatore;
import log.LogController;

public class GestioneOperatoriController implements IGestioneOperatori, IGestioneBloccoOperatori{
	private DAOFactoryOperatori factory;
	
	public GestioneOperatoriController(DAOFactoryOperatori factory) {
		this.factory = factory;
	}
	
	public void aggiungiOperatore(Operatore operatore, String hash) {
		//preliminarmente si scrive nel log
		LogController logController = new LogController();
		String[] operazione = {LocalDateTime.now().toString(), " - aggiunta di un nuovo operatore - ", this.getClass().getSimpleName()};
		logController.printLogOperazione(operazione);
		String[] messaggio = {LocalDateTime.now().toString(), "AggiungiOperatore: " + operatore.getUsername(), "invio", this.getClass().getSimpleName()};
		logController.printLogMessaggio(messaggio);
		
		//poi si aggiorna il DB
		OperatoreDAO operatoreDAO = factory.getOperatoreDAO();
		operatoreDAO.create(operatore, hash);
	}
	
	public void rimuoviOperatore(String username) {
		//preliminarmente si scrive nel log
		LogController logController = new LogController();
		String[] operazione = {LocalDateTime.now().toString(), " - rimozione di un operatore - ", this.getClass().getSimpleName()};
		logController.printLogOperazione(operazione);
		String[] messaggio = {LocalDateTime.now().toString(), "RimuoviOperatore: " + username, "invio", this.getClass().getSimpleName()};
		logController.printLogMessaggio(messaggio);
				
		//poi si aggiorna il DB
		OperatoreDAO operatoreDAO = factory.getOperatoreDAO();
		operatoreDAO.delete(username);
	}
	
	public void sbloccaOperatore(String username) {
		//preliminarmente si scrive nel log
		LogController logController = new LogController();
		String[] operazione = {LocalDateTime.now().toString(), " - sblocco di un operatore - ", this.getClass().getSimpleName()};
		logController.printLogOperazione(operazione);
		String[] messaggio = {LocalDateTime.now().toString(), "SbloccaOperatore: " + username, "invio", this.getClass().getSimpleName()};
		logController.printLogMessaggio(messaggio);
						
		//poi si aggiorna il DB
		OperatoreDAO operatoreDAO = factory.getOperatoreDAO();
		Operatore operatore = operatoreDAO.readByUsername(username);
		operatoreDAO.sblocca(operatore);
	}
	
	public void bloccaOperatore(String username) {
		//preliminarmente si scrive nel log
		LogController logController = new LogController();
		String[] operazione = {LocalDateTime.now().toString(), " - blocco di un operatore - ", this.getClass().getSimpleName()};
		logController.printLogOperazione(operazione);
		String[] messaggio = {LocalDateTime.now().toString(), "BloccaOperatore: " + username, "invio", this.getClass().getSimpleName()};
		logController.printLogMessaggio(messaggio);
								
		//poi si aggiorna il DB
		OperatoreDAO operatoreDAO = factory.getOperatoreDAO();
		Operatore operatore = operatoreDAO.readByUsername(username);
		operatoreDAO.blocca(operatore);
	}
}
