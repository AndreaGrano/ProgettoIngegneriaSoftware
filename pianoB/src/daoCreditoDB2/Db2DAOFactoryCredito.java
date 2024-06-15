package daoCreditoDB2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalDateTime;

import daoCredito.BonificoDAO;
import daoCredito.ClienteDAO;
import daoCredito.CreditoDAO;
import daoCredito.DAOFactoryCredito;
import daoCredito.ParametriSistemaDAO;
import log.LogController;

public class Db2DAOFactoryCredito extends DAOFactoryCredito{
	public static final String DRIVER = "com.ibm.db2.jcc.DB2Driver";
	public static final String DBURL = "jdbc:db2://diva.deis.unibo.it:50000/tw_stud";
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	
	//
	
	static {
		try {
			Class.forName(DRIVER);
		} catch(Exception e) {
			System.err.println("Db2DAOFactoryCredito: failed to load DB2 driver");
			e.printStackTrace();
		}
	}
	
	public static Connection createConnection() {
		LogController logController = new LogController();
		String[] operazione = {LocalDateTime.now().toString(), " - create Connection - ", "Db2DAOFactoryCredito"};
		logController.printLogOperazione(operazione);
		String[] messaggio = {LocalDateTime.now().toString(), " - create Connection - ", "invio - ", "Db2DAOFactoryCredito"};
		logController.printLogMessaggio(messaggio);
		try {
			return DriverManager.getConnection(DBURL, USERNAME, PASSWORD);
		} catch(Exception e) {
			System.err.println("Db2DAOFactoryCredito: create connection failed");
			e.printStackTrace();
			return null;
		}
	}
	
	public static void closeConnection(Connection connection) {
		LogController logController = new LogController();
		String[] operazione = {LocalDateTime.now().toString(), " - close Connection - ", "Db2DAOFactoryCredito"};
		logController.printLogOperazione(operazione);
		String[] messaggio = {LocalDateTime.now().toString(), " - close Connection - ", "invio - ", "Db2DAOFactoryCredito"};
		logController.printLogMessaggio(messaggio);
		try {
			connection.close();
		} catch(Exception e) {
			System.err.println("Db2DAOFactoryCredito: close connection failed");
			e.printStackTrace();
		}
	}
	
	public CreditoDAO getCreditoDAO() {
		return new Db2CreditoDAO();
	}

	@Override
	public BonificoDAO getBonificoDAO() {
		return new Db2BonificoDAO();
	}

	@Override
	public ClienteDAO getClienteDAO() {
		return new Db2ClienteDAO();
	}

	@Override
	public ParametriSistemaDAO getParametriSistemaDAO() {
		return new Db2ParametriSistemaDAO();
	}
}
