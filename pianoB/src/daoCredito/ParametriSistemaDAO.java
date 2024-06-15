package daoCredito;

public interface ParametriSistemaDAO {
	public double readPercentualeMulta();
	public int readIntervalloRitardoCredito();
	public int readIntervalloRitardoBlacklist();
	public String readFormatoCausale();
	
	public boolean updatePercentualeMulta(double valore);
	public boolean updateIntervalloRitardoCredito(int valore);
	public boolean updateIntervalloRitardoBlacklist(int valore);
	public boolean updateFormatoCausale(String nuovoFormato);
	
	public void createTable();
	public void dropTable();	
}
