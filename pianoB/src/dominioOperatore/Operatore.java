package dominioOperatore;

public class Operatore {
	private int id;
	private String username;
	private String nome;
	private String cognome;
	private String telefono;

	//costruttore senza id utilizzato alla prima creazione dell'oggetto
	public Operatore(String username, String nome, String cognome, String telefono) {
		this.username = username;
		this.nome = nome;
		this.cognome = cognome;
		this.telefono = telefono;
		
		this.id = -1;
	}
	
	//costruttore con id utilizzato per le letture da DB

	public Operatore(int id, String username, String nome, String cognome, String telefono) {
		this.id = id;
		this.username = username;
		this.nome = nome;
		this.cognome = cognome;
		this.telefono = telefono;
	}
	
	public Operatore() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

}
