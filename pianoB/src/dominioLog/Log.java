package dominioLog;

import java.time.*;
import java.util.*;

public class Log extends HashSet<Entry>{
	private static final long serialVersionUID = 1L;
	
	public HashSet<Entry> getEntry(LocalDateTime start, LocalDateTime end) {
		HashSet<Entry> matchingSet = new HashSet<Entry>();
		
		for(Entry entry : this) {
			if(entry.getDataOra().isAfter(start) && entry.getDataOra().isBefore(end)) {
				matchingSet.add(entry);
			}
		}
		
		return matchingSet;
	}
	
	public HashSet<Entry> getEntry(LocalDate date) {
		HashSet<Entry> matchingSet = new HashSet<Entry>();
		
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
