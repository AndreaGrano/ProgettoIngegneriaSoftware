package autenticazione;

import java.time.LocalDateTime;

import daoOperatori.DAOFactoryOperatori;
import daoOperatori.OperatoreDAO;
import dominioOperatore.Operatore;
import log.LogController;

public class AutenticazioneController implements IAutenticazione{
	private DAOFactoryOperatori factory;
	
	public AutenticazioneController(DAOFactoryOperatori factory) {
		this.factory = factory;
	}
	
	public boolean verificaCredenziali(String username, String hash) {
		//innanzitutto, si verifica se l'operatore esista
		//in caso contrario si lancia un'eccezione (allo scopo di non fare incrementare il counter dei tentativi nel client)
		
		//si scrive nel log l'operazione di lettura dell'operatore
		LogController logController = new LogController();
		String[] operazioneOperatore = {LocalDateTime.now().toString(), " - lettura di un operatore - ", this.getClass().getSimpleName()};
		logController.printLogOperazione(operazioneOperatore);
		String[] messaggioOperatore = {LocalDateTime.now().toString(), " - VerificaCredenziali, lettura di un operatore - ", "ricezione - ", this.getClass().getSimpleName()};
		logController.printLogMessaggio(messaggioOperatore);
		
		//si prova a leggere l'operatore e se non esiste si lancia eccezione
		OperatoreDAO operatoreDAO = factory.getOperatoreDAO();
		Operatore operatore = operatoreDAO.readByUsername(username);
		if(operatore == null) {
			throw new IllegalArgumentException("Operatore inesistente");
		}
		
	
		//si scrive nel log l'operazione di lettura dell'operatore
		String[] operazioneHash = {LocalDateTime.now().toString(), " - lettura di un hash - ", this.getClass().getSimpleName()};
		logController.printLogOperazione(operazioneHash);
		String[] messaggioHash = {LocalDateTime.now().toString(), " - VerificaCredenziali, lettura hash di: " + username , " - ricezione - ", this.getClass().getSimpleName()};
		logController.printLogMessaggio(messaggioHash);
		
		//si legge l'hash dal DB e si ritorna true se coincide con quello passato, false altrimenti
		String hashOperatore = operatoreDAO.readHash(username);

		if(hash.equals(hashOperatore) && (!operatoreDAO.isBloccato(operatore))) {
			return true;
		} else {
			return false;
		}
	}
}
