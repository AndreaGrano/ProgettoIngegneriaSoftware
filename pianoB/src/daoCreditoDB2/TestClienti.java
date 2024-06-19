package daoCreditoDB2;

import java.time.LocalDate;
import java.util.Set;

import daoCredito.ClienteDAO;
import daoCredito.DAOFactoryCredito;
import dominioCredito.Blacklist;
import dominioCredito.Cliente;

public class TestClienti { 

	public static void main(String[] args) {
		
		DAOFactoryCredito daoFactoryInstance = DAOFactoryCredito.getDAOFactoryCredito(DAOFactoryCredito.DB2);

		ClienteDAO clDAO = daoFactoryInstance.getClienteDAO();
		clDAO.dropTable();
		clDAO.createTable();
		
		LocalDate lc = LocalDate.now();
		LocalDate lc2 = LocalDate.of(2002, 2, 5);
		Cliente cl = new Cliente("Roberto", "Chiesa", lc, "123123123", "22,Andrea da Formaggine,Bologna,BO", "RCHIESACF");
		Cliente cl2 = new Cliente("Andrea", "Frumento", lc, "321321321", "3,Caduti di Cefalonia,Bologna,BO", "ANDRFRMNT");
		Cliente cl3 = new Cliente("Gianmarchino", "Campanaro", lc2, "222222222", "2,Suona le campane,Monghidoro,BO", "CMPNR");
		Cliente cl4 = new Cliente("Ilaria", "Ciaccia", lc2.minusDays(3), "10101010", "5,X Settembre MCCVX,Milano,MI", "BOH");
		clDAO.create(cl);
		clDAO.create(cl2);
		clDAO.create(cl3);
		clDAO.create(cl4);
		
		cl4.setId(clDAO.readByCF(cl4.getCodiceFiscale()).getId());
		cl3.setId(clDAO.readByCF(cl3.getCodiceFiscale()).getId());
		clDAO.inserisciInBlacklist(cl4);
		clDAO.inserisciInBlacklist(cl3);
		
		Set<Cliente> clienti = clDAO.readAll();
		
		for (Cliente c : clienti) {
			System.out.println(c.stampaCliente());
		}
		
		System.out.println("-----------------------");
		Blacklist bl = clDAO.readClientiInBlacklist();

		Cliente boh = null;
		for (Cliente c : bl) {
			System.out.println(c.stampaCliente());
			if (c.getNome().equals("Gianmarchino") ) {
				System.out.println("gianmaaaaaa");
				boh = c;
			}
		}
		
		if (bl.remove(boh)) {
			System.out.println("rimosso!!!!!!!!\n\n");
		}
		
		System.out.println("-----------------------");
		
		for (Cliente c : bl) {
			System.out.println(c.stampaCliente());
		}
		
//		Cliente cl3 = clDAO.read(1);
//		System.out.println(cl3.stampaCliente());
//		cl3.setTelefono("10000000");
//		clDAO.update(cl3);
//		Cliente cl4 = clDAO.readByCF("ANDRFRMNT");
//		System.out.println(cl4.stampaCliente());
		
		//clDAO.delete(2);
		
//		ParametriSistemaDAO psDAO = daoFactoryInstance.getParametriSistemaDAO();
//		psDAO.dropTable(); // ---> da cancellare eventualmente in fase di produzione
//		psDAO.createTable();
		
//		ParametriSistema ps = ParametriSistema.getInstance();
//		ps.setIntervalloRitardoCredito(100);
//		ps.setIntervalloRitardoBlacklist(1000);
//		ps.setPercentualeMulta(5.0);
//		ps.setFormatoCausale("XYZ");
//		
//		psDAO.updateFormatoCausale(ps.getFormatoCausale());

	}

}
