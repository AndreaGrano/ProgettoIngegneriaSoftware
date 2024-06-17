package dominioCredito;

import java.time.LocalDate;

public class CreditoNonRiconciliato extends Credito{

	public CreditoNonRiconciliato(double importo, LocalDate dataStipula, String causale, Cliente cliente) {
		super(importo, dataStipula, causale, cliente);
	}
	
	public CreditoNonRiconciliato(int id, double importo, LocalDate dataStipula, String causale, Cliente cliente) {
		super(id, importo, dataStipula, causale, cliente);
	}
	
	//costruttore vuoto
	public CreditoNonRiconciliato() {
		super();
	}

	@Override
	public String toString() {
		return super.stampaCredito();
	}
}
