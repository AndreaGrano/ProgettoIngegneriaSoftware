package gestioneDati;

import java.time.LocalDateTime;

import daoCredito.DAOFactoryCredito;
import daoCredito.ParametriSistemaDAO;
import log.LogController;

public class ImpostazioneParametriController implements IImpostazioneParametri{
	private DAOFactoryCredito factory;
	
	public ImpostazioneParametriController(DAOFactoryCredito factory) {
		this.factory = factory;
	}
	
	@Override
	public void impostaIntervalloRitardoCredito(int intervallo) {
		//preliminarmente si scrive nel log
		LogController logController = new LogController();
		String[] operazione = {LocalDateTime.now().toString(), " - impostaIntervalloRitardoCredito - ", this.getClass().getSimpleName()};
		logController.printLogOperazione(operazione);
		String[] messaggio = {LocalDateTime.now().toString(), "ImpostaIntervalloRitardoCredito a: " + intervallo, "invio", this.getClass().getSimpleName()};
		logController.printLogMessaggio(messaggio);
		
		//si aggiorna il DB
		ParametriSistemaDAO parametriSistemaDAO = factory.getParametriSistemaDAO();
		parametriSistemaDAO.updateIntervalloRitardoCredito(intervallo);
	}

	@Override
	public void impostaIntervalloRitardoBlacklist(int intervallo) {
		//preliminarmente si scrive nel log
		LogController logController = new LogController();
		String[] operazione = {LocalDateTime.now().toString(), " - impostaIntervalloRitardoBlacklist - ", this.getClass().getSimpleName()};
		logController.printLogOperazione(operazione);
		String[] messaggio = {LocalDateTime.now().toString(), "ImpostaIntervalloRitardoBlacklist a: " + intervallo, "invio", this.getClass().getSimpleName()};
		logController.printLogMessaggio(messaggio);
				
		//si aggiorna il DB
		ParametriSistemaDAO parametriSistemaDAO = factory.getParametriSistemaDAO();
		parametriSistemaDAO.updateIntervalloRitardoBlacklist(intervallo);
	}

	@Override
	public void impostaPercentualeMulta(double percentualeMulta) {
		//preliminarmente si scrive nel log
		LogController logController = new LogController();
		String[] operazione = {LocalDateTime.now().toString(), " - impostaPercentualeMulta - ", this.getClass().getSimpleName()};
		logController.printLogOperazione(operazione);
		String[] messaggio = {LocalDateTime.now().toString(), "ImpostaPercentualeMulta a: " + percentualeMulta, "invio", this.getClass().getSimpleName()};
		logController.printLogMessaggio(messaggio);
						
		//si aggiorna il DB
		ParametriSistemaDAO parametriSistemaDAO = factory.getParametriSistemaDAO();
		parametriSistemaDAO.updatePercentualeMulta(percentualeMulta);		
	}

	@Override
	public void impostaFormatoCausale(String formatoCausale) {
		//preliminarmente si scrive nel log
		LogController logController = new LogController();
		String[] operazione = {LocalDateTime.now().toString(), " - impostaFormatoCausale - ", this.getClass().getSimpleName()};
		logController.printLogOperazione(operazione);
		String[] messaggio = {LocalDateTime.now().toString(), "ImpostaFormatoCausale a: " + formatoCausale, "invio", this.getClass().getSimpleName()};
		logController.printLogMessaggio(messaggio);
								
		//si aggiorna il DB
		ParametriSistemaDAO parametriSistemaDAO = factory.getParametriSistemaDAO();
		parametriSistemaDAO.updateFormatoCausale(formatoCausale);			
	}
}
