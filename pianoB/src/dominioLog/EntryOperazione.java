package dominioLog;

import java.time.LocalDateTime;

public class EntryOperazione extends Entry {
	private String operazione;
	
	public EntryOperazione(LocalDateTime dataOra, String operazione) {
		super(dataOra);

		this.operazione = operazione;
	}

	public String getOperazione() {
		return operazione;
	}

}
