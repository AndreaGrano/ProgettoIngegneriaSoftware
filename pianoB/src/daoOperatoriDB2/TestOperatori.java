package daoOperatoriDB2;

import daoOperatori.DAOFactoryOperatori;
import daoOperatori.OperatoreDAO;
import dominioOperatore.Operatore;

public class TestOperatori { //

	public static void main(String[] args) {
		
		DAOFactoryOperatori daoFactoryInstance = DAOFactoryOperatori.getDAOFactoryOperatori(DAOFactoryOperatori.DB2);

		OperatoreDAO opDAO = daoFactoryInstance.getOperatoreDAO();
		opDAO.dropTable();
		opDAO.createTable();

		Operatore op = new Operatore(0, "xGianma02", "Giamarco", "Ferraguti", "123456789");
		Operatore op2 = new Operatore(0, "xXTiFalcioXx", "Andrea", "Grano", "555555555");
		
		opDAO.create(op, "mio hash");
		opDAO.create(op2, "mio hash 2");
		
		op2 = opDAO.read(2);
		
		opDAO.update(op2, "mio hash 3", true);
		opDAO.delete(1);
		
		// funziona tutto!
	}

}
