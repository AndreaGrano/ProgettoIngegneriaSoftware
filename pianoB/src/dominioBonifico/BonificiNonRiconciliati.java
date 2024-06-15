package dominioBonifico;

import java.util.HashSet;

public class BonificiNonRiconciliati extends HashSet<BonificoNonRiconciliato>{
	private static final long serialVersionUID = 1L;
	
	public String stampaBonificiNonRiconciliati() {
		String bonificiNonRiconciliati = "";
		
		for (BonificoNonRiconciliato bonificoNonRiconciliato : this) {
			bonificiNonRiconciliati += bonificoNonRiconciliato.stampaBonifico();
			bonificiNonRiconciliati += "\n";
		}
		
		return bonificiNonRiconciliati;
	}
}
