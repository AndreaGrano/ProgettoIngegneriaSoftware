package dominioCredito;

import java.util.HashSet;

public class Blacklist extends HashSet<Cliente>{
	private static final long serialVersionUID = 1L;
	
	public boolean verifica(Cliente cliente) {
		return this.contains(cliente);
	}
}
