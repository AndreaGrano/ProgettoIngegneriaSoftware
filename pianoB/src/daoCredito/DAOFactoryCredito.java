package daoCredito;

import daoCreditoDB2.Db2DAOFactoryCredito;


public abstract class DAOFactoryCredito {
//	public DAOFactoryCredito() {
//		super();
//	}
	
	//
	public static final int DB2 = 0;
	
	public static DAOFactoryCredito getDAOFactoryCredito(int whichFactory) {
		switch ( whichFactory ) {
		case DB2:
			return new Db2DAOFactoryCredito();
		default:
			return null;
		}
	}
	
	public abstract CreditoDAO getCreditoDAO();
	
	public abstract BonificoDAO getBonificoDAO();
	
	public abstract ClienteDAO getClienteDAO();
	public abstract ParametriSistemaDAO getParametriSistemaDAO();
}
