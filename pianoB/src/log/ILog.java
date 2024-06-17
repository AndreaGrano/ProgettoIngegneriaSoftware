package log;

import java.time.*;
import java.util.ArrayList;

import dominioLog.*;

public interface ILog {
	public ArrayList<Entry> getEntry(LocalDateTime start, LocalDateTime end);
	
	public ArrayList<Entry> getEntry(LocalDate data);
	
	public Log getLog();
	
	public void printLogOperazione(String operazione[]);
	
	public void printLogMessaggio(String messaggio[]);
}
