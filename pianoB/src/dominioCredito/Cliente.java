package dominioCredito;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Cliente {
	private int id;
	private String nome;
	private String cognome;
	private LocalDate dataNascita;
	private String telefono;
	private String indirizzo;
	private String codiceFiscale;
	
	private String dataStringa;
	
	//anche in questo caso, la prima volta che creeremo un nuovo Cliente non avrà l'id
	public Cliente(String nome, String cognome, LocalDate dataNascita, String telefono, String indirizzo, String codiceFiscale) {
		this.nome = nome;
		this.cognome = cognome;
		this.dataNascita = dataNascita;
		this.telefono = telefono;
		this.indirizzo = indirizzo;
		this.codiceFiscale = codiceFiscale;
		
		this.id = -1;
		
		this.dataStringa = dataNascita.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}
	
	//ma quando istanzieremo l'oggetto in seguito a una lettura da DB avrà l'id
	public Cliente(int id, String nome, String cognome, LocalDate dataNascita, String telefono, String indirizzo, String codiceFiscale) {
		this.id = id;
		this.nome = nome;
		this.cognome = cognome;
		this.dataNascita = dataNascita;
		this.telefono = telefono;
		this.indirizzo = indirizzo;
		this.codiceFiscale = codiceFiscale;
		
		this.dataStringa = dataNascita.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}
	
	//costruttore vuoto
	public Cliente() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public LocalDate getDataNascita() {
		return dataNascita;
	}

	public void setDataNascita(LocalDate dataNascita) {
		this.dataNascita = dataNascita;
		
		this.dataStringa = dataNascita.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}
	
	public void setDataNascita(String dataNascita) {
		this.dataNascita = LocalDate.parse(dataNascita, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
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

	public String stampaCliente() {
		return this.getCodiceFiscale() + " - " + this.getNome() + " - " + this.getCognome() + " - " + this.getDataNascita() +
				" - " + this.getIndirizzo() + " - " + this.getTelefono();
	}
}
