package daoOperatoriDB2;

import java.sql.*;
import java.time.LocalDateTime;

import daoOperatori.DAOFactoryOperatori;
import daoOperatori.OperatoreDAO;
import log.LogController;

public class Db2DAOFactoryOperatori extends DAOFactoryOperatori{
	
	public static final String DRIVER = "com.ibm.db2.jcc.DB2Driver";
	public static final String DBURL = "jdbc:db2://diva.deis.unibo.it:50000/tw_stud";
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	
	static {
		try {
			Class.forName(DRIVER);
		} catch(Exception e) {
			System.err.println("Db2DAOFactoryOperatori: failed to load DB2 driver");
			e.printStackTrace();
		}
	}
	
	public static Connection createConnection() {
		LogController logController = new LogController();
		String[] operazione = {LocalDateTime.now().toString(), " - create Connection - ", "Db2DAOFactoryOperatori"};
		logController.printLogOperazione(operazione);
		String[] messaggio = {LocalDateTime.now().toString(), " - create Connection - ", "invio - ", "Db2DAOFactoryOperatori"};
		logController.printLogMessaggio(messaggio);
		try {
			return DriverManager.getConnection(DBURL, USERNAME, PASSWORD);
		} catch(Exception e) {
			System.err.println("Db2DAOFactoryOperatori: create connection failed");
			e.printStackTrace();
			return null;
		}
	}
	
	public static void closeConnection(Connection connection) {
		LogController logController = new LogController();
		String[] operazione = {LocalDateTime.now().toString(), " - close Connection - ", "Db2DAOFactoryOperatori"};
		logController.printLogOperazione(operazione);
		String[] messaggio = {LocalDateTime.now().toString(), " - close Connection - ", "invio - ", "Db2DAOFactoryOperatori"};
		logController.printLogMessaggio(messaggio);
		try {
			connection.close();
		} catch(Exception e) {
			System.err.println("Db2DAOFactoryOperatori: close connection failed");
			e.printStackTrace();
		}
	}
	
	public OperatoreDAO getOperatoreDAO() {
			return new Db2OperatoreDAO();
	}
}
