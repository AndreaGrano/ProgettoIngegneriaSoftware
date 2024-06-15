package dominioCredito;

import java.util.HashSet;

public class Crediti extends HashSet<Credito>{
	private static final long serialVersionUID = 1L;
	
	public String visualizzaTuttiCrediti() {
		String crediti = "";
		
		for (Credito credito: this) {
			crediti += credito.stampaCredito();
			crediti += "\n";
		}
		
		return crediti;
	}
}
