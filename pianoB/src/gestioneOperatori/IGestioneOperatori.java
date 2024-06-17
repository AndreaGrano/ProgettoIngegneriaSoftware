package gestioneOperatori;

import dominioOperatore.Operatore;

public interface IGestioneOperatori {
	public void aggiungiOperatore(Operatore operatore, String hash);
	
	public void rimuoviOperatore(String username);
}
