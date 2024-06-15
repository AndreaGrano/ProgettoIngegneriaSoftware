package dominioCredito;

import java.time.LocalDate;

import dominioBonifico.Bonifico;

public class CreditoRiconciliato extends Credito {
	private Bonifico bonifico; 

	//un CreditoRiconciliato ha necessariamente un id perché lo leggiamo sempre dal DB
	//o lo generiamo in conseguenza di una riconciliazione con un credito non riconciliato letto da DB
	public CreditoRiconciliato(int id, double importo, LocalDate dataStipula, String causale, Cliente cliente, Bonifico bonifico) {
		super(id, importo, dataStipula, causale, cliente);
		
		this.bonifico = bonifico;
	}
	
	//costruttore vuoto
	public CreditoRiconciliato() {
		super();
	}
	
	public Bonifico getBonifico() {
		return bonifico;
	}

	public void setBonifico(Bonifico bonifico) {
		this.bonifico = bonifico;
	}
}
