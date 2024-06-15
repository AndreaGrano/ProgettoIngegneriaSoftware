package daoCredito;

import dominioCredito.Blacklist;
import dominioCredito.Cliente;
import java.util.*;
public interface ClienteDAO {
	public void create(Cliente cliente);
	
	public Cliente read(int id);
	public Cliente readByCF(String CF);
	public Blacklist readClientiInBlacklist();
	public Set<Cliente> readAll();
	
	public boolean update(Cliente cliente);
	
	public boolean inserisciInBlacklist(Cliente cliente);
	
	public boolean rimuoviDaBlacklist(Cliente cliente);
	
	public boolean delete(int id);
	
	public boolean createTable();
	
	public boolean dropTable();
}
