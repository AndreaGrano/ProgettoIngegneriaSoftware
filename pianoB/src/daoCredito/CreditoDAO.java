package daoCredito;

import dominioCredito.*;

public interface CreditoDAO {
	
	public void create(Credito credito);
	
	public Credito read(int id);
	
	public CreditiScaduti readCreditiScaduti();
	
	public Crediti readCreditiNonRiconciliati();
	
	public Crediti readAll();
	
	public void update(Credito credito);
	
	public void update(Credito credito, int idBonifico);
	
	public void delete(int id);
	
	public void createTable();
	
	public void dropTable();
}
