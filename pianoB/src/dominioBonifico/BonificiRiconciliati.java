package dominioBonifico;

import java.util.HashSet;

public class BonificiRiconciliati extends HashSet<BonificoRiconciliato>{
	private static final long serialVersionUID = 1L;

	public String stampaBonificiRiconciliati() {
		String bonificiRiconciliati = "";
		
		for(BonificoRiconciliato bonificoRiconciliato : this) {
			bonificiRiconciliati += bonificoRiconciliato.stampaBonifico();
			bonificiRiconciliati += "\n";
		}
		
		return bonificiRiconciliati;
	}
}
