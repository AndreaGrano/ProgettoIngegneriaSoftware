package dominioLog;

import java.time.LocalDateTime;

public class Entry {
	private LocalDateTime dataOra;
	
	public Entry(LocalDateTime dataOra) {
		this.dataOra = dataOra;
	}

	public LocalDateTime getDataOra() {
		return dataOra;
	}
}
