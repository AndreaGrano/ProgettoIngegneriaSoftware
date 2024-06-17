package dominioLog;

import java.time.*;
import java.util.*;

public class Log extends ArrayList<Entry>{
	private static final long serialVersionUID = 1L;
	
	public ArrayList<Entry> getEntry(LocalDateTime start, LocalDateTime end) {
		ArrayList<Entry> matchingSet = new ArrayList<Entry>();
		
		for(Entry entry : this) {
			if((entry.getDataOra().isAfter(start) && entry.getDataOra().isBefore(end)) 
					|| entry.getDataOra().toLocalDate().isEqual(end.toLocalDate())
					|| entry.getDataOra().toLocalDate().isEqual(start.toLocalDate())) {
				matchingSet.add(entry);
			}
		}
		
		return matchingSet;
	}
	
	public ArrayList<Entry> getEntry(LocalDate date) {
		ArrayList<Entry> matchingSet = new ArrayList<Entry>();
		
		for(Entry entry : this) {
			if(entry.getDataOra().toLocalDate().equals(date)) {
				matchingSet.add(entry);
			}
		}
		
		return matchingSet;
	}
	
	public void printOperazione(String operazione) {
		EntryOperazione entryOperazione = new EntryOperazione(LocalDateTime.now(), operazione);
		this.add(entryOperazione);
	}
	
	public void printMessaggio(String messaggio) {
		EntryMessaggio entryMessaggio = new EntryMessaggio(LocalDateTime.now(), messaggio);
		this.add(entryMessaggio);
	}
}
