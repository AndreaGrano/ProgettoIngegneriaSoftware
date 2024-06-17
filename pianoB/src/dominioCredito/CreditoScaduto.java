package dominioCredito;

import java.time.LocalDate;

public class CreditoScaduto extends Credito{

	//un CreditoScaduto ha necessariamente un id perché lo leggiamo sempre dal DB
	public CreditoScaduto(int id, double importo, LocalDate dataStipula, String causale, Cliente cliente) {
		super(id, importo, dataStipula, causale, cliente);
	}

	public CreditoScaduto() {
		
	}
	
	@Override
	public String toString() {
		return super.stampaCredito();
	}
}
