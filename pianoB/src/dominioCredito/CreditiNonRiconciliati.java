package dominioCredito;

import java.util.HashSet;

public class CreditiNonRiconciliati extends HashSet<CreditoNonRiconciliato>{
	private static final long serialVersionUID = 1L;
	
	public String stampaCreditiNonRiconciliati() {
		String creditiNonRiconciliati = "";
		
		for (CreditoNonRiconciliato creditoNonRiconciliato : this) {
			creditiNonRiconciliati += creditoNonRiconciliato.stampaCredito();
			creditiNonRiconciliati += "\n";
		}
		
		return creditiNonRiconciliati;
	}

}
