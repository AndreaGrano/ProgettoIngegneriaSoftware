package dominioBonifico;

import java.time.LocalDate;

public abstract class Bonifico {
	private int id;
	private String IBAN;
	private LocalDate dataValuta;
	private String causale;
	private double importo;
	
	//costruttore senza id utilizzato alla prima creazione dell'oggetto
	public Bonifico(String IBAN, LocalDate dataValuta, String causale, double importo) {
		this.IBAN = IBAN;
		this.dataValuta = dataValuta;
		this.causale = causale;
		this.importo = importo;
		
		this.id = -1;
	}
	
	//costruttore con id utilizzato per la lettura da DB
	public Bonifico(int id, String IBAN, LocalDate dataValuta, String causale, double importo) {
		this.id = id;
		this.IBAN = IBAN;
		this.dataValuta = dataValuta;
		this.causale = causale;
		this.importo = importo;
	}
	
	//costruttore vuoto
	public Bonifico() {
		
	}

	//getters e setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIBAN() {
		return IBAN;
	}

	public void setIBAN(String IBAN) {
		this.IBAN = IBAN;
	}

	public LocalDate getDataValuta() {
		return dataValuta;
	}

	public void setDataValuta(LocalDate dataValuta) {
		this.dataValuta = dataValuta;
	}

	public String getCausale() {
		return causale;
	}

	public void setCausale(String causale) {
		this.causale = causale;
	}

	public double getImporto() {
		return importo;
	}

	public void setImporto(double importo) {
		this.importo = importo;
	}

	public String stampaBonifico() {
		return this.getCausale() + " - " + this.getImporto() + " - " + this.getIBAN() + " - " + this.getDataValuta();
	}

}
