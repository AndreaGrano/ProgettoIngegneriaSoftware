package daoCreditoDB2;

import daoCredito.DAOFactoryCredito;
import daoCredito.ParametriSistemaDAO;
import dominioParametriSistema.ParametriSistema;


public class TestParametriSistema { 
	
	// classe che non ci dovrebbe essere
	// e' giusto una classe per fare test!
	
	public static void main(String[] args) {
		
		DAOFactoryCredito daoFactoryInstance = DAOFactoryCredito.getDAOFactoryCredito(DAOFactoryCredito.DB2);

		ParametriSistemaDAO psDAO = daoFactoryInstance.getParametriSistemaDAO();
		//psDAO.dropTable(); // ---> da cancellare eventualmente in fase di produzione
		psDAO.createTable();
		
		ParametriSistema ps = ParametriSistema.getInstance();
		ps.setIntervalloRitardoCredito(100);
		ps.setIntervalloRitardoBlacklist(1000);
		ps.setPercentualeMulta(5.0);
		ps.setFormatoCausale("XYZ");
		
		psDAO.updateFormatoCausale(ps.getFormatoCausale());

	}

}
