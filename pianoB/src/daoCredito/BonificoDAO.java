package daoCredito;

import dominioBonifico.*;

public interface BonificoDAO {
	
	public void create(Bonifico bonifico);
	
	public Bonifico read(int id);
	
	public BonificiNonRiconciliati readBonificiNonRiconciliati();
	
	public Bonifici readAll();
	
	public int readId(Bonifico bonifico);
	
	public boolean update(Bonifico bonifico, int nuoveRiconciliazioni);
	
	public boolean delete(int id);
	
	public boolean createTable();
	
	public boolean dropTable();
}
