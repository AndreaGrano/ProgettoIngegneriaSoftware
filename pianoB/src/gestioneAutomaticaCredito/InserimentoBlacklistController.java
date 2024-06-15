package gestioneAutomaticaCredito;

import java.time.LocalDateTime;

import dominioCredito.Cliente;
import log.LogController;
import daoCredito.ClienteDAO;
import daoCredito.DAOFactoryCredito;

public class InserimentoBlacklistController implements IInserimentoBlacklist{
	private DAOFactoryCredito factory;
	
	public InserimentoBlacklistController(DAOFactoryCredito factory) {
		this.factory = factory;
	}
	
	@Override
	public void inserisciInBlacklist(Cliente cliente) {
		//si scrive sul log l'operazione di inserimento in blacklist
		LogController logController = new LogController();
		String[] operazione = {LocalDateTime.now().toString(), " - inserimento in blacklist - ", this.getClass().getSimpleName()};
		logController.printLogOperazione(operazione);
		String[] messaggio = {LocalDateTime.now().toString(), " - InserimentoBlacklist, cliente: " + cliente.getCodiceFiscale() , " - invio - ", this.getClass().getSimpleName()};
		logController.printLogMessaggio(messaggio);
		
		//si esegue l'operazione
		ClienteDAO clienteDAO = factory.getClienteDAO();
		clienteDAO.inserisciInBlacklist(cliente);
	}

}
