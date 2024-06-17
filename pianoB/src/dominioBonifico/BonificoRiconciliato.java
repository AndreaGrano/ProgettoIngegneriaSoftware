package dominioBonifico;

import java.time.LocalDate;

public class BonificoRiconciliato extends Bonifico{
	
	//un BonificoRiconciliato avrà sempre un id, perché lo leggeremo sempre da DB
	//o sarà creato da una riconciliazione manuale, dopo averne estratto uno non riconciliato con id dal DB
	public BonificoRiconciliato(int id, String IBAN, LocalDate dataValuta, String causale, double importo) {
		super(id, IBAN, dataValuta, causale, importo);
	}
	
	//costruttore vuoto
	public BonificoRiconciliato() {
		super();
	}
	
	@Override
	public String toString() {
		return super.stampaBonifico();
	}
}
