package log;

import java.time.*;
import java.util.HashSet;

import dominioLog.*;

public interface ILog {
	public HashSet<Entry> getEntry(LocalDateTime start, LocalDateTime end);
	
	public HashSet<Entry> getEntry(LocalDate data);
	
	public Log getLog();
	
	public void printLogOperazione(String operazione[]);
	
	public void printLogMessaggio(String messaggio[]);
}
