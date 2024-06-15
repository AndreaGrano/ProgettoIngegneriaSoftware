package dominioAmministratore;

import dominioOperatore.Operatore;

public class Amministratore extends Operatore {
	
	public Amministratore(String username, String nome, String cognome, String telefono) {
		super(username, nome, cognome, telefono);
	}
	
	public Amministratore(int id, String username, String nome, String cognome, String telefono) {
		super(id, username, nome, cognome, telefono);
	}
}
