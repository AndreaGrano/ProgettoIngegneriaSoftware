package daoOperatori;

import dominioOperatore.Operatore;

public interface OperatoreDAO {
	public void create(Operatore operatore, String hash);
	
	public Operatore read(int id);
	
	public Operatore readByUsername(String username);
	
	public String readHash(String username);
	
	public void update(Operatore operatore, String hash, boolean isBloccato);
	
	public void update(Operatore operatore);
	
	public void update(Operatore operatore, String hash);
	
	public void blocca(Operatore operatore);
	
	public void sblocca(Operatore operatore);
	
	public boolean isBloccato(Operatore operatore);
	
	public void delete(int id);
	
	public void delete(String username);
	
	public void createTable();
	
	public void dropTable();
}
