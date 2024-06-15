package gestioneManualeCredito;

import dominioBonifico.BonificoNonRiconciliato;
import dominioCredito.CreditiNonRiconciliati;

public interface IRiconciliazioneManuale {
	public boolean riconciliazioneManuale(BonificoNonRiconciliato bonificoNonRiconciliato, CreditiNonRiconciliati creditiNonRiconciliati);
}
