package gestioneDati;

import dominioBonifico.*;
import dominioCredito.*;

public interface IAccessoDirettoAiDati {
	public void modificaDati(Bonifico bonifico);
	
	public void modificaDati(Credito credito);
	
	public void modificaDati(Cliente cliente);
	
	public void rimuoviDati(Bonifico bonifico);
	
	public void rimuoviDati(Credito credito);
	
	public void rimuoviDati(Cliente cliente);
	
	public void inserisciInBlacklist(Cliente cliente);
	
	public void rimuoviDaBlacklist(Cliente cliente);
}
