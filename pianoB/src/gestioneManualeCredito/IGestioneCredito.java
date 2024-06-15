package gestioneManualeCredito;

import dominioCredito.*;
import java.time.*;

public interface IGestioneCredito {
	public int registraCredito(Cliente cliente, double importo, LocalDate dataStipula, String causale);
	
	public Crediti visualizzaCrediti();
	
	public CreditiScaduti visualizzaCreditiScaduti();
}
