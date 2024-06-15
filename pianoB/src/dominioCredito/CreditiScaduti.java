package dominioCredito;

import java.util.HashSet;

public class CreditiScaduti extends HashSet<CreditoScaduto>{
	private static final long serialVersionUID = 1L;

	public String stampaCreditiScaduti() {
		String creditiScaduti = "";
		
		for (CreditoScaduto creditoScaduto : this) {
			creditiScaduti += creditoScaduto.stampaCredito();
			creditiScaduti += "\n";
		}
		
		return creditiScaduti;
	}
}
