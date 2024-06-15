package dominioParametriSistema;

import daoCredito.DAOFactoryCredito;
import daoCredito.ParametriSistemaDAO;

public class ParametriSistema {
	private long intervalloRitardoCredito;
	private long intervalloRitardoBlacklist;
	private double percentualeMulta;
	private String formatoCausale;
	
	//implementazione pattern Singleton
	private static ParametriSistema instance = null;
	
	protected ParametriSistema(long intervalloRitardoCredito, long intervalloRitardoBlacklist, double percentualeMulta, String formatoCausale) {
		this.intervalloRitardoCredito = intervalloRitardoCredito;
		this.intervalloRitardoBlacklist = intervalloRitardoBlacklist;
		this.percentualeMulta = percentualeMulta;
		this.formatoCausale = formatoCausale;
	}
	
	public static ParametriSistema getInstance() {
		if(instance == null) {
			DAOFactoryCredito factoryCredito = DAOFactoryCredito.getDAOFactoryCredito(0);
			ParametriSistemaDAO psDAO = factoryCredito.getParametriSistemaDAO();
			int intervalloRitardoCredito = psDAO.readIntervalloRitardoCredito();
			int intervalloRitardoBlacklist = psDAO.readIntervalloRitardoBlacklist();
			double percentualeMulta = psDAO.readPercentualeMulta();
			String formatoCausale = psDAO.readFormatoCausale();
			
			//si convertono i ritardi in giorni in millisecondi, usando un long per il dato
			long intervalloCreditoInMillisecondi = intervalloRitardoCredito * 24 * 60 * 60 * 1000;
			long intervalloBlacklistInMillisecondi = intervalloRitardoBlacklist * 24 * 60 * 60 * 1000;
			
			instance = new ParametriSistema(intervalloCreditoInMillisecondi, intervalloBlacklistInMillisecondi, percentualeMulta, formatoCausale);
		}
		
			return instance;
	}
	
	//convertitori
	private long convertiGiorniInMillisecondi(int giorni) {
		return Integer.toUnsignedLong(giorni * 24 * 60 * 60 * 1000);
	}
	
	private int convertiMillisecondiInGiorni(long millisecondi) {
		return (int) ( millisecondi / (24 * 60 * 60 * 1000) );
	}

	//getters e setters
	public long getIntervalloRitardoCredito() {
		DAOFactoryCredito factoryCredito = DAOFactoryCredito.getDAOFactoryCredito(0);
		ParametriSistemaDAO psDAO = factoryCredito.getParametriSistemaDAO();
		
		this.intervalloRitardoCredito = this.convertiGiorniInMillisecondi(psDAO.readIntervalloRitardoCredito());
		
		return intervalloRitardoCredito;
	}

	public void setIntervalloRitardoCredito(long intervalloRitardoCredito) {
		DAOFactoryCredito factoryCredito = DAOFactoryCredito.getDAOFactoryCredito(0);
		ParametriSistemaDAO psDAO = factoryCredito.getParametriSistemaDAO();
		
		psDAO.updateIntervalloRitardoCredito(this.convertiMillisecondiInGiorni(intervalloRitardoCredito));
		
		this.intervalloRitardoCredito = intervalloRitardoCredito;
	}

	public long getIntervalloRitardoBlacklist() {
		DAOFactoryCredito factoryCredito = DAOFactoryCredito.getDAOFactoryCredito(0);
		ParametriSistemaDAO psDAO = factoryCredito.getParametriSistemaDAO();
		
		this.intervalloRitardoBlacklist = this.convertiGiorniInMillisecondi(psDAO.readIntervalloRitardoBlacklist());
		
		return intervalloRitardoBlacklist;
	}

	public void setIntervalloRitardoBlacklist(long intervalloRitardoBlacklist) {
		DAOFactoryCredito factoryCredito = DAOFactoryCredito.getDAOFactoryCredito(0);
		ParametriSistemaDAO psDAO = factoryCredito.getParametriSistemaDAO();
		
		psDAO.updateIntervalloRitardoBlacklist(this.convertiMillisecondiInGiorni(intervalloRitardoBlacklist));
		
		this.intervalloRitardoBlacklist = intervalloRitardoBlacklist;
	}

	public double getPercentualeMulta() {
		DAOFactoryCredito factoryCredito = DAOFactoryCredito.getDAOFactoryCredito(0);
		ParametriSistemaDAO psDAO = factoryCredito.getParametriSistemaDAO();
		
		this.percentualeMulta = psDAO.readPercentualeMulta();
		
		return percentualeMulta;
	}

	public void setPercentualeMulta(double percentualeMulta) {
		DAOFactoryCredito factoryCredito = DAOFactoryCredito.getDAOFactoryCredito(0);
		ParametriSistemaDAO psDAO = factoryCredito.getParametriSistemaDAO();
		
		psDAO.updatePercentualeMulta(percentualeMulta);
		
		this.percentualeMulta = percentualeMulta;
	}

	public String getFormatoCausale() {
		DAOFactoryCredito factoryCredito = DAOFactoryCredito.getDAOFactoryCredito(0);
		ParametriSistemaDAO psDAO = factoryCredito.getParametriSistemaDAO();
		
		this.formatoCausale = psDAO.readFormatoCausale();
		
		return formatoCausale;
	}

	public void setFormatoCausale(String formatoCausale) {
		DAOFactoryCredito factoryCredito = DAOFactoryCredito.getDAOFactoryCredito(0);
		ParametriSistemaDAO psDAO = factoryCredito.getParametriSistemaDAO();
		
		psDAO.updateFormatoCausale(formatoCausale);
		
		this.formatoCausale = formatoCausale;
	}
}
