package daoCreditoDB2;

import java.time.LocalDate;

import daoCredito.BonificoDAO;
import daoCredito.DAOFactoryCredito;
import dominioBonifico.Bonifici;
import dominioBonifico.BonificiNonRiconciliati;
import dominioBonifico.Bonifico;
import dominioBonifico.BonificoNonRiconciliato;

public class TestBonifici { 

	public static void main(String[] args) {
		
		DAOFactoryCredito daoFactoryInstance = DAOFactoryCredito.getDAOFactoryCredito(DAOFactoryCredito.DB2);

		BonificoDAO boDAO = daoFactoryInstance.getBonificoDAO();
		
		boDAO.dropTable();
		boDAO.createTable();

		Bonifico b = new BonificoNonRiconciliato(-1, "IBAN1", LocalDate.now().minusDays(20), "causale 1", 100);
		Bonifico b2 = new BonificoNonRiconciliato(-1, "IBAN2", LocalDate.now(), "causale", 200);
		
		boDAO.create(b);
		boDAO.create(b2);
		b = boDAO.read(1);
		int boId = boDAO.readId(b);
		
		System.out.println(b.stampaBonifico() + "\t" + boId);
		
		boDAO.update(b, 1);
		boDAO.update(b2, 1);
		//boDAO.delete(1);
		
		BonificiNonRiconciliati bnr = boDAO.readBonificiNonRiconciliati();
		
		for (Bonifico bon : bnr) {
			System.out.println(bon.stampaBonifico());
		}
		
		Bonifici bnrs = boDAO.readAll();
		System.out.println("Stampa tutti bonifici:");
		
		for (Bonifico bon : bnrs) {
			System.out.println(bon.stampaBonifico());
		}
		
	}

}
