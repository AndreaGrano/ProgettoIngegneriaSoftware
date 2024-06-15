package dominioCredito;

import java.time.LocalDate;

public abstract class Credito {
	private int id;
	private double importo;
	private LocalDate dataStipula;
	private String causale;
	private Cliente cliente;
	
	//costruttore senza id utilizzato alla prima creazione dell'oggetto
	public Credito(double importo, LocalDate dataStipula, String causale, Cliente cliente) {
		this.importo = importo;
		this.dataStipula = dataStipula;
		this.causale = causale;
		this.cliente = cliente;
		this.id = -1;
	}
	
	//costruttore con id utilizzato per la lettura da DB
	public Credito(int id, double importo, LocalDate dataStipula, String causale, Cliente cliente) {
		this.id = id;
		this.importo = importo;
		this.dataStipula = dataStipula;
		this.causale = causale;
		this.cliente = cliente;
	}

	//costruttore vuoto
	public Credito() {
		
	}
	
	//getters e setters
	public int getId() {
		return id;
	}

	public Cliente getCliente() {
		return this.cliente;
	}
	
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public double getImporto() {
		return importo;
	}

	public void setImporto(double importo) {
		this.importo = importo;
	}

	public LocalDate getDataStipula() {
		return dataStipula;
	}

	public void setDataStipula(LocalDate dataStipula) {
		this.dataStipula = dataStipula;
	}

	public String getCausale() {
		return causale;
	}

	public void setCausale(String casuale) {
		this.causale = casuale;
	}
	
	public String stampaCredito() {
		return this.getCausale() + " - " + this.getImporto() + " - " + this.getDataStipula();
	}
}
