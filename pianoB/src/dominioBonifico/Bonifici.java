package dominioBonifico;

import java.util.HashSet;

public class Bonifici extends HashSet<Bonifico> {
	private static final long serialVersionUID = 1L;
	
	public String stampaTuttiBonifici() {
		String bonifici = "";
		
		for(Bonifico bonifico : this) {
			bonifici += bonifico.stampaBonifico();
			bonifici += "\n";
		}
		
		return bonifici;
	}

}
