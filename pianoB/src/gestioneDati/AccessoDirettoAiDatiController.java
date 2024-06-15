package gestioneDati;

import java.time.LocalDateTime;

import daoCredito.BonificoDAO;
import daoCredito.ClienteDAO;
import daoCredito.CreditoDAO;
import daoCredito.DAOFactoryCredito;
import dominioBonifico.Bonifico;
import dominioCredito.Cliente;
import dominioCredito.Credito;
import log.LogController;

public class AccessoDirettoAiDatiController implements IAccessoDirettoAiDati{
	private DAOFactoryCredito factory;
	
	public AccessoDirettoAiDatiController(DAOFactoryCredito factory) {
		this.factory = factory;
	}
	
	@Override
	public void modificaDati(Bonifico bonifico) {
		//preliminarmente si scrive nel log
		LogController logController = new LogController();
		String[] operazione = {LocalDateTime.now().toString(), " - modifica dati (Bonifico) - ", this.getClass().getSimpleName()};
		logController.printLogOperazione(operazione);
		String[] messaggio = {LocalDateTime.now().toString(), "ModificaDati del bonifico: " + bonifico.getId(), "invio", this.getClass().getSimpleName()};
		logController.printLogMessaggio(messaggio);
		
		//poi si aggiorna il DB
		BonificoDAO bonificoDAO = factory.getBonificoDAO();
		bonificoDAO.update(bonifico, 0);
	}

	@Override
	public void modificaDati(Credito credito) {
		//preliminarmente si scrive nel log
		LogController logController = new LogController();
		String[] operazione = {LocalDateTime.now().toString(), " - modifica dati (Credito) - ", this.getClass().getSimpleName()};
		logController.printLogOperazione(operazione);
		String[] messaggio = {LocalDateTime.now().toString(), "MOdificaDati del credito: " + credito.getId(), "invio", this.getClass().getSimpleName()};
		logController.printLogMessaggio(messaggio);
		
		//poi si aggiorna il DB
		CreditoDAO creditoDAO = factory.getCreditoDAO();
		creditoDAO.update(credito);	
	}

	@Override
	public void modificaDati(Cliente cliente) {
		//preliminarmente si scrive nel log
		LogController logController = new LogController();
		String[] operazione = {LocalDateTime.now().toString(), " - modifica dati (Cliente) - ", this.getClass().getSimpleName()};
		logController.printLogOperazione(operazione);
		String[] messaggio = {LocalDateTime.now().toString(), "ModificaDati del cliente: " + cliente.getId(), "invio", this.getClass().getSimpleName()};
		logController.printLogMessaggio(messaggio);
		
		//poi si aggiorna il DB
		ClienteDAO clienteDAO = factory.getClienteDAO();
		clienteDAO.update(cliente);
	}

	@Override
	public void rimuoviDati(Bonifico bonifico) {
		//preliminarmente si scrive nel log
		LogController logController = new LogController();
		String[] operazione = {LocalDateTime.now().toString(), " - rimozione dati (Bonifico) - ", this.getClass().getSimpleName()};
		logController.printLogOperazione(operazione);
		String[] messaggio = {LocalDateTime.now().toString(), "RimuoviDati del bonifico: " + bonifico.stampaBonifico(), "invio", this.getClass().getSimpleName()};
		logController.printLogMessaggio(messaggio);
			
		//poi si aggiorna il DB
		BonificoDAO bonificoDAO = factory.getBonificoDAO();
		bonificoDAO.delete(bonifico.getId());
		
	}

	@Override
	public void rimuoviDati(Credito credito) {
		//preliminarmente si scrive nel log
		LogController logController = new LogController();
		String[] operazione = {LocalDateTime.now().toString(), " - rimozione dati (Credito) - ", this.getClass().getSimpleName()};
		logController.printLogOperazione(operazione);
		String[] messaggio = {LocalDateTime.now().toString(), "RimuoviDati del credito: " + credito.stampaCredito(), "invio", this.getClass().getSimpleName()};
		logController.printLogMessaggio(messaggio);
				
		//poi si aggiorna il DB
		CreditoDAO creditoDAO = factory.getCreditoDAO();
		creditoDAO.delete(credito.getId());
	}

	@Override
	public void rimuoviDati(Cliente cliente) {
		//preliminarmente si scrive nel log
		LogController logController = new LogController();
		String[] operazione = {LocalDateTime.now().toString(), " - rimozione dati (Cliente) - ", this.getClass().getSimpleName()};
		logController.printLogOperazione(operazione);
		String[] messaggio = {LocalDateTime.now().toString(), "RimuoviDati del cliente: " + cliente.stampaCliente(), "invio", this.getClass().getSimpleName()};
				logController.printLogMessaggio(messaggio);
			
		//poi si aggiorna il DB
		ClienteDAO clienteDAO = factory.getClienteDAO();
		clienteDAO.delete(cliente.getId());
	}

	@Override
	public void inserisciInBlacklist(Cliente cliente) {
		//preliminarmente si scrive nel log
		LogController logController = new LogController();
		String[] operazione = {LocalDateTime.now().toString(), " - inserimento in blacklist - ", this.getClass().getSimpleName()};
		logController.printLogOperazione(operazione);
		String[] messaggio = {LocalDateTime.now().toString(), "InserisciInBlacklist: id:" + cliente.getId() + ", CF: " + cliente.getCodiceFiscale(), "invio", this.getClass().getSimpleName()};
		logController.printLogMessaggio(messaggio);
			
		//poi si aggiorna il DB
		ClienteDAO clienteDAO = factory.getClienteDAO();
		clienteDAO.inserisciInBlacklist(cliente);
	}

	@Override
	public void rimuoviDaBlacklist(Cliente cliente) {
		//preliminarmente si scrive nel log
		LogController logController = new LogController();
		String[] operazione = {LocalDateTime.now().toString(), " - rimozione dalla blacklist - ", this.getClass().getSimpleName()};
		logController.printLogOperazione(operazione);
		String[] messaggio = {LocalDateTime.now().toString(), "RimuoviDaBlacklist: id:" + cliente.getId() + ", CF: " + cliente.getCodiceFiscale(), "invio", this.getClass().getSimpleName()};
		logController.printLogMessaggio(messaggio);
			
		//poi si aggiorna il DB
		ClienteDAO clienteDAO = factory.getClienteDAO();
		clienteDAO.rimuoviDaBlacklist(cliente);
	}

}
