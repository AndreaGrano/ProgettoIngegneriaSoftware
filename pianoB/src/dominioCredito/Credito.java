package dominioCredito;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public abstract class Credito {
	private int id;
	private double importo;
	private LocalDate dataStipula;
	private String causale;
	private Cliente cliente;
	
	private String codiceFiscale;
	private String dataStringa;
	private String importoStringa;
	
	//costruttore senza id utilizzato alla prima creazione dell'oggetto
	public Credito(double importo, LocalDate dataStipula, String causale, Cliente cliente) {
		this.importo = importo;
		this.dataStipula = dataStipula;
		this.causale = causale;
		this.cliente = cliente;
		this.id = -1;
		
		this.codiceFiscale = cliente.getCodiceFiscale();
		this.dataStringa = dataStipula.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		this.importoStringa = Double.toString(importo);
	}
	
	//costruttore con id utilizzato per la lettura da DB
	public Credito(int id, double importo, LocalDate dataStipula, String causale, Cliente cliente) {
		this.id = id;
		this.importo = importo;
		this.dataStipula = dataStipula;
		this.causale = causale;
		this.cliente = cliente;
		
		this.codiceFiscale = cliente.getCodiceFiscale();
		this.dataStringa = dataStipula.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		this.importoStringa = Double.toString(importo);
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
		
		this.codiceFiscale = cliente.getCodiceFiscale();
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public double getImporto() {
		return importo;
	}

	public void setImporto(double importo) {
		this.importo = importo;
		this.importoStringa = Double.toString(importo);
	}
	
	public void setImporto(String importo) {
		this.importo = Double.parseDouble(importo);
	}

	public LocalDate getDataStipula() {
		return dataStipula;
	}

	public void setDataStipula(LocalDate dataStipula) {
		this.dataStipula = dataStipula;
		this.dataStringa = dataStipula.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}
	
	public void setDataStipula(String dataStipula) {
		this.dataStipula = LocalDate.parse(dataStipula, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}

	public String getCausale() {
		return causale;
	}

	public void setCausale(String casuale) {
		this.causale = casuale;
	}
	
	public String getCodiceFiscale() {
		return codiceFiscale;
	}
	
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}
	
	public String getDataStringa() {
		return dataStringa;
	}

	public String getImportoStringa() {
		return importoStringa;
	}

	public String stampaCredito() {
		return this.getCausale() + " - " + this.getImporto() + " - " + this.getDataStipula();
	}
}
