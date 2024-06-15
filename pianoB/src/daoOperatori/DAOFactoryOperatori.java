package daoOperatori;

import daoOperatoriDB2.Db2DAOFactoryOperatori;

public abstract class DAOFactoryOperatori {

	public DAOFactoryOperatori() {
		super(); //
	}

	public static final int DB2 = 0;

	public static DAOFactoryOperatori getDAOFactoryOperatori(int id) {
		switch (id) {
		case DB2:
			return new Db2DAOFactoryOperatori();
		default:
			return null;
		}

	}

	public abstract OperatoreDAO getOperatoreDAO();

}
