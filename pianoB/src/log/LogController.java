package log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.StringTokenizer;

import dominioLog.Entry;
import dominioLog.EntryMessaggio;
import dominioLog.EntryOperazione;
import dominioLog.Log;

public class LogController implements ILog{
	private File fileLog;
	
	public LogController() {
		fileLog = new File(".\\log.txt");
		
		if(!fileLog.exists()) {
			try {
				fileLog.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public HashSet<Entry> getEntry(LocalDateTime start, LocalDateTime end) {
		Log log = this.getLog();
		
		return log.getEntry(start, end);
	}

	@Override
	public HashSet<Entry> getEntry(LocalDate data) {
		Log log = this.getLog();
		
		return log.getEntry(data);
	}

	@Override
	public Log getLog() {
		Log log = new Log();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileLog));
			DateTimeFormatter dtFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
			String line;
			while((line = br.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line, "-");
				LocalDateTime dataOra = LocalDateTime.parse(line, dtFormatter);
				if(st.countTokens() <= 2) {
					String operazione = "";
					while(st.hasMoreTokens()) {
						operazione = operazione += st.nextToken();
					}
					
					EntryOperazione entryOperazione = new EntryOperazione(dataOra, operazione);
					log.add(entryOperazione);
				} else {
					String messaggio = "";
					while(st.hasMoreTokens()) {
						messaggio = messaggio += st.nextToken();
					}
					
					EntryMessaggio entryMessaggio = new EntryMessaggio(dataOra, messaggio);
					log.add(entryMessaggio);
				}			
			}
			
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return log;
	}

	@Override
	public void printLogOperazione(String[] operazione) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(fileLog));
			
			for(int i = 0; i < operazione.length; i++) {
				bw.append(operazione[i]);
			}
			
			bw.append("\n");
			
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void printLogMessaggio(String[] messaggio) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(fileLog));
			
			for(int i = 0; i < messaggio.length; i++) {
				bw.append(messaggio[i]);
			}
			
			bw.append("\n");
			
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
