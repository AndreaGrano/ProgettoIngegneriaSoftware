package dominioBonifico;

import java.time.LocalDate;

public class BonificoNonRiconciliato extends Bonifico{

	public BonificoNonRiconciliato(int id, String IBAN, LocalDate dataValuta, String causale, double importo) {
		super(id, IBAN, dataValuta, causale, importo);
	}
	
	//cotruttore vuoto
	public BonificoNonRiconciliato() {
		super();
	}
}
