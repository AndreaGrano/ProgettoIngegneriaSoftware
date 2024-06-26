package dominioLog;

import java.time.LocalDateTime;

public class EntryMessaggio extends Entry {
	private String messaggio;
	
	public EntryMessaggio(LocalDateTime dataOra, String messaggio) {
		super(dataOra);

		this.messaggio = messaggio; 
	}

	public String getMessaggio() {
		return messaggio;
	}
	
	@Override
	public String toString() {
		return this.messaggio;
	}
}
