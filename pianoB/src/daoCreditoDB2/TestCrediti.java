package daoCreditoDB2;

import java.time.LocalDate;

//import daoCredito.BonificoDAO;
import daoCredito.ClienteDAO;
import daoCredito.CreditoDAO;
import daoCredito.DAOFactoryCredito;
//import dominioBonifico.Bonifico;
//import dominioBonifico.BonificoNonRiconciliato;
import dominioCredito.*;

public class TestCrediti { 

	public static void main(String[] args) {
		
		DAOFactoryCredito daoFactoryInstance = DAOFactoryCredito.getDAOFactoryCredito(DAOFactoryCredito.DB2);

		// --------------------------------------------------------
		
		ClienteDAO clDAO = daoFactoryInstance.getClienteDAO();
//		clDAO.dropTable();
//		clDAO.createTable();
		
		LocalDate lc = LocalDate.now();
//		LocalDate lc2 = LocalDate.of(2002, 2, 5);
		Cliente cl = new Cliente("Roberto", "Chiesa", lc, "123123123", "22,Andrea da Formaggine,Bologna,BO", "RCHIESACF");
//		Cliente cl2 = new Cliente("Andrea", "Frumento", lc, "321321321", "3,Caduti di Cefalonia,Bologna,BO", "ANDRFRMNT");
//		Cliente cl3 = new Cliente("Gianmarchino", "Campanaro", lc2, "222222222", "2,Suona le campane,Monghidoro,BO", "CMPNR");
//		Cliente cl4 = new Cliente("Ilaria", "Ciaccia", lc2.minusDays(3), "10101010", "5,X Settembre MCCVX,Milano,MI", "BOH");
		
		cl.setId(clDAO.readByCF(cl.getCodiceFiscale()).getId());
		
//		clDAO.create(cl);
//		clDAO.create(cl2);
//		clDAO.create(cl3);
//		clDAO.create(cl4);
		
		// --------------------------------------------------------
		
//		BonificoDAO boDAO = daoFactoryInstance.getBonificoDAO();
		
//		boDAO.dropTable();
//		boDAO.createTable();

//		Bonifico b = new BonificoNonRiconciliato(-1, "IBAN1", LocalDate.now().minusDays(20), "causale 1", 100);
//		Bonifico b2 = new BonificoNonRiconciliato(-1, "IBAN2", LocalDate.now(), "causale", 200);
		
//		boDAO.create(b);
//		boDAO.create(b2);
//		b = boDAO.read(1);
		
		// --------------------------------------------------------
		
		CreditoDAO crDAO = daoFactoryInstance.getCreditoDAO();
		
		crDAO.dropTable();
		crDAO.createTable();
		
		CreditoNonRiconciliato cnr = new CreditoNonRiconciliato(100, LocalDate.now().minusDays(30), "causale 1", cl);
		
		crDAO.create(cnr);

		Credito c = crDAO.read(1);
		crDAO.update(c, 1);
		//System.out.println(Db2CreditoDAO.read_by_id_cliente);
		System.out.println(c.stampaCredito());
		
		Crediti crs = crDAO.readCreditiNonRiconciliati();
		for (Credito cd : crs) {
			System.out.println("entrato");
			System.out.println(cd.stampaCredito());
		}
		
		System.out.println(Db2CreditoDAO.read_by_id);
		System.out.println(Db2CreditoDAO.read_non_riconciliati);
		
		Crediti all = crDAO.readAll();
		for (Credito cd : all) {
			System.out.println("tutti");
			System.out.println(cd.stampaCredito());
		}
		
//		c.setCausale("causale boh");
//		
//		crDAO.update(c, 1);
//		
//		System.out.println(c.stampaCredito());
//		
//		crDAO.delete(1);
		
	}

}
