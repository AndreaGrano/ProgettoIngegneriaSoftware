package daoCreditoDB2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;

import daoCredito.ParametriSistemaDAO;
import log.LogController;



public class Db2ParametriSistemaDAO implements ParametriSistemaDAO {

	// -----------------------------------------
	// -- IL BEAN DELLA TABLE E' UN SINGLETON --
	// -- PERCIO' CI SARA' UNA SOLA TUPLA     --
	// -----------------------------------------

	static final String TABLE = "parametri_sistema";

	// -------------------------------------------------------------------------------------

	static final String INTERVALLO_RITARDO_CREDITO = "intervallo_ritardo_credito";
	static final String INTERVALLO_RITARDO_BLACKLIST = "intervallo_ritardo_blacklist";
	static final String PERCENTUALE_MULTA = "percentuale_multa";
	static final String FORMATO_CAUSALE = "formato_causale";

	// == STATEMENT SQL!
	// ====================================================================

	// INSERT INTO table ( name,description, ...) VALUES ( ?,?, ... );
	static final String insert = "INSERT " + "INTO " + TABLE + " ( " + INTERVALLO_RITARDO_CREDITO + ", "
			+ INTERVALLO_RITARDO_BLACKLIST + ", " + PERCENTUALE_MULTA + ", " + FORMATO_CAUSALE + " " + ") "
			+ "VALUES (?,?,?,?) ";

	// SELECT * FROM table WHERE idcolumn = ?;
	static String read_all = "SELECT * " + "FROM " + TABLE + " ";

	// Le altre read qui sotto in realta' non servono
	
	static String read_intervallo_ritardo_credito = "SELECT " + INTERVALLO_RITARDO_CREDITO + "FROM " + TABLE;

	static String read_intervallo_ritardo_blacklist = "SELECT " + INTERVALLO_RITARDO_BLACKLIST + "FROM " + TABLE;
	
	static String read_percentuale_multa = "SELECT " + PERCENTUALE_MULTA + "FROM " + TABLE;
	
	static String read_formato_causale = "SELECT " + FORMATO_CAUSALE + "FROM " + TABLE;
	
	// DELETE FROM table WHERE idcolumn = ?;
	
	// ---> facciamo delete delle tuple (parametri sistema) direttamente da db
	
	//static String delete = "DELETE " + "FROM " + TABLE + " " + "WHERE " + INTERVALLO_RITARDO_CREDITO + " = ? ";

	// UPDATE table SET xxxcolumn = ?, ... WHERE idcolumn = ?;
	static String update_intervallo_ritardo_credito = "UPDATE " + TABLE + " " + "SET " + INTERVALLO_RITARDO_CREDITO + " = ? ";

	static String update_intervallo_ritardo_blacklist = "UPDATE " + TABLE + " " + "SET " + INTERVALLO_RITARDO_BLACKLIST + " = ? ";

	static String update_percentuale_multa = "UPDATE " + TABLE + " " + "SET " + PERCENTUALE_MULTA + " = ? ";

	static String update_formato_causale = "UPDATE " + TABLE + " " + "SET " + FORMATO_CAUSALE + " = ? ";
	
	
	// SELECT * FROM table;
	//static String query = "SELECT * " + "FROM " + TABLE + " ";

	// -------------------------------------------------------------------------------------

	// CREATE entrytable ( code INT NOT NULL PRIMARY KEY, ... );
	static String create = "CREATE " + "TABLE " + TABLE + " ( " + INTERVALLO_RITARDO_CREDITO + " INT NOT NULL, " + INTERVALLO_RITARDO_BLACKLIST
			+ " INT NOT NULL, " + PERCENTUALE_MULTA + " DOUBLE NOT NULL, " + FORMATO_CAUSALE + " VARCHAR(20) NOT NULL" + ") ";
	
	static String drop = "DROP " + "TABLE " + TABLE + " ";

	// === METODI DAO
	// =========================================================================

	@Override
	public double readPercentualeMulta() {
		LogController logController = new LogController();
		String[] operazione = {LocalDateTime.now().toString(), " - read Percentuale Multa - ", this.getClass().getSimpleName()};
		logController.printLogOperazione(operazione);
		String[] messaggio = {LocalDateTime.now().toString(), " - read percentuale multa - ", "ricezione - ", this.getClass().getSimpleName()};
		logController.printLogMessaggio(messaggio);
		double multa = 0;
		Connection conn = Db2DAOFactoryCredito.createConnection();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(Db2ParametriSistemaDAO.read_all);
			if (rs.next()) {
				multa = rs.getDouble(Db2ParametriSistemaDAO.PERCENTUALE_MULTA);
			}
			rs.close();
			stmt.close();
		} catch (Exception e) {
			System.err.println("read(): failed to retrieve percentuale multa");
			e.printStackTrace();
		} finally {
			Db2DAOFactoryCredito.closeConnection(conn);
		}
		return multa;
	}

	@Override
	public int readIntervalloRitardoCredito() {
		LogController logController = new LogController();
		String[] operazione = {LocalDateTime.now().toString(), " - read Intervallo Ritardo Credito - ", this.getClass().getSimpleName()};
		logController.printLogOperazione(operazione);
		String[] messaggio = {LocalDateTime.now().toString(), " - read Intervallo Ritardo Credito - ", "ricezione - ", this.getClass().getSimpleName()};
		logController.printLogMessaggio(messaggio);
		int credito = 0;
		Connection conn = Db2DAOFactoryCredito.createConnection();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(Db2ParametriSistemaDAO.read_all);
			if (rs.next()) {
				credito = rs.getInt(Db2ParametriSistemaDAO.INTERVALLO_RITARDO_CREDITO);
			}
			rs.close();
			stmt.close();
		} catch (Exception e) {
			System.err.println("read(): failed to retrieve intervallo ritardo credito");
			e.printStackTrace();
		} finally {
			Db2DAOFactoryCredito.closeConnection(conn);
		}
		return credito;
	}

	@Override
	public int readIntervalloRitardoBlacklist() {
		LogController logController = new LogController();
		String[] operazione = {LocalDateTime.now().toString(), " - read Intervallo Ritardo Blacklist - ", this.getClass().getSimpleName()};
		logController.printLogOperazione(operazione);
		String[] messaggio = {LocalDateTime.now().toString(), " - read Intervallo Ritardo Blacklist - ", "ricezione - ", this.getClass().getSimpleName()};
		logController.printLogMessaggio(messaggio);
		int blacklist = 0;
		Connection conn = Db2DAOFactoryCredito.createConnection();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(Db2ParametriSistemaDAO.read_all);
			if (rs.next()) {
				blacklist = rs.getInt(Db2ParametriSistemaDAO.INTERVALLO_RITARDO_BLACKLIST);
			}
			rs.close();
			stmt.close();
		} catch (Exception e) {
			System.err.println("read(): failed to retrieve intervallo ritardo blacklist");
			e.printStackTrace();
		} finally {
			Db2DAOFactoryCredito.closeConnection(conn);
		}
		return blacklist;
	}

	@Override
	public String readFormatoCausale() {
		LogController logController = new LogController();
		String[] operazione = {LocalDateTime.now().toString(), " - read Formato Causale - ", this.getClass().getSimpleName()};
		logController.printLogOperazione(operazione);
		String[] messaggio = {LocalDateTime.now().toString(), " - read Formato Causale - ", "ricezione - ", this.getClass().getSimpleName()};
		logController.printLogMessaggio(messaggio);
		String formato = "";
		Connection conn = Db2DAOFactoryCredito.createConnection();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(Db2ParametriSistemaDAO.read_all);
			if (rs.next()) {
				formato = rs.getString(Db2ParametriSistemaDAO.FORMATO_CAUSALE);
			}
			rs.close();
			stmt.close();
		} catch (Exception e) {
			System.err.println("read(): failed to retrieve intervallo ritardo credito");
			e.printStackTrace();
		} finally {
			Db2DAOFactoryCredito.closeConnection(conn);
		}
		return formato;
	}

	@Override
	public boolean updateFormatoCausale(String nuovoFormato) {
		LogController logController = new LogController();
		String[] operazione = {LocalDateTime.now().toString(), " - update FormatoCausale (nuovoFormato)  - ", this.getClass().getSimpleName()};
		logController.printLogOperazione(operazione);
		boolean result = false;
		if (nuovoFormato == null) {
			System.err.println("updateFormatoCausale(): failed to update a null entry");
			return result;
		}
		String[] messaggio = {LocalDateTime.now().toString(), " - update del formato causale col seguente: " + nuovoFormato, " - invio - ", this.getClass().getSimpleName()};
		logController.printLogMessaggio(messaggio);
		Connection conn = Db2DAOFactoryCredito.createConnection();
		try {
			PreparedStatement prep_stmt = conn.prepareStatement(Db2ParametriSistemaDAO.update_formato_causale);
			prep_stmt.clearParameters();
			prep_stmt.setString(1, nuovoFormato);
			prep_stmt.executeUpdate();
			result = true;
			prep_stmt.close();
		} catch (Exception e) {
			System.err.println("update(): failed to update entry: " + e.getMessage());
			e.printStackTrace();
		} finally {
			Db2DAOFactoryCredito.closeConnection(conn);
		}
		return result;
	}
	
	@Override
	public boolean updatePercentualeMulta(double valore) {
		LogController logController = new LogController();
		String[] operazione = {LocalDateTime.now().toString(), " - update percentuale multa (valore)  - ", this.getClass().getSimpleName()};
		logController.printLogOperazione(operazione);
		boolean result = false;
		if (valore == Double.NaN) {
			System.err.println("updatePercentualeMulta(): failed to update NaN entry");
			return result;
		}
		String[] messaggio = {LocalDateTime.now().toString(), " - update percentuale multa: " + valore, " - invio - ", this.getClass().getSimpleName()};
		logController.printLogMessaggio(messaggio);
		Connection conn = Db2DAOFactoryCredito.createConnection();
		try {
			PreparedStatement prep_stmt = conn.prepareStatement(Db2ParametriSistemaDAO.update_percentuale_multa);
			prep_stmt.clearParameters();
			prep_stmt.setDouble(1, valore);
			prep_stmt.executeUpdate();
			result = true;
			prep_stmt.close();
		} catch (Exception e) {
			System.err.println("update(): failed to update entry: " + e.getMessage());
			e.printStackTrace();
		} finally {
			Db2DAOFactoryCredito.closeConnection(conn);
		}
		return result;
	}

	@Override
	public boolean updateIntervalloRitardoCredito(int valore) {
		LogController logController = new LogController();
		String[] operazione = {LocalDateTime.now().toString(), " - update Intervallo Ritardo Credito (valore)  - ", this.getClass().getSimpleName()};
		logController.printLogOperazione(operazione);
		boolean result = false;
		if (valore < 0) {
			System.err.println("updateIntervalloRitardoCredito(): failed to update a negative entry");
			return result;
		}
		String[] messaggio = {LocalDateTime.now().toString(), " - update Intervallo Ritardo Credito (valore): " + valore, " - invio - ", this.getClass().getSimpleName()};
		logController.printLogMessaggio(messaggio);
		Connection conn = Db2DAOFactoryCredito.createConnection();
		try {
			PreparedStatement prep_stmt = conn.prepareStatement(Db2ParametriSistemaDAO.update_intervallo_ritardo_credito);
			prep_stmt.clearParameters();
			prep_stmt.setInt(1, valore);
			prep_stmt.executeUpdate();
			result = true;
			prep_stmt.close();
		} catch (Exception e) {
			System.err.println("update(): failed to update entry: " + e.getMessage());
			e.printStackTrace();
		} finally {
			Db2DAOFactoryCredito.closeConnection(conn);
		}
		return result;
	}

	@Override
	public boolean updateIntervalloRitardoBlacklist(int valore) {
		LogController logController = new LogController();
		String[] operazione = {LocalDateTime.now().toString(), " - update Intervallo Ritardo Blacklist (valore)  - ", this.getClass().getSimpleName()};
		logController.printLogOperazione(operazione);
		boolean result = false;
		if (valore < 0) {
			System.err.println("updateIntervalloRitardoBlacklist(): failed to update a negative entry");
			return result;
		}
		String[] messaggio = {LocalDateTime.now().toString(), " - update Intervallo Ritardo Blacklist (valore): " + valore, " - invio - ", this.getClass().getSimpleName()};
		logController.printLogMessaggio(messaggio);
		Connection conn = Db2DAOFactoryCredito.createConnection();
		try {
			PreparedStatement prep_stmt = conn.prepareStatement(Db2ParametriSistemaDAO.update_intervallo_ritardo_blacklist);
			prep_stmt.clearParameters();
			prep_stmt.setInt(1, valore);
			prep_stmt.executeUpdate();
			result = true;
			prep_stmt.close();
		} catch (Exception e) {
			System.err.println("update(): failed to update entry: " + e.getMessage());
			e.printStackTrace();
		} finally {
			Db2DAOFactoryCredito.closeConnection(conn);
		}
		return result;
	}

	@Override
	public void createTable() {
		LogController logController = new LogController();
		String[] operazione = {LocalDateTime.now().toString(), " - create Table ParametriSistema - ", this.getClass().getSimpleName()};
		logController.printLogOperazione(operazione);
		String[] messaggio = {LocalDateTime.now().toString(), " - create Table ParametriSistema - ", "invio - ", this.getClass().getSimpleName()};
		logController.printLogMessaggio(messaggio);
		Connection conn = Db2DAOFactoryCredito.createConnection();
		try {
			Statement stmt = conn.createStatement();
			stmt.execute(Db2ParametriSistemaDAO.create);
			stmt.close();
		} catch (Exception e) {
			System.err.println("createTable(): failed to create table '" + TABLE + "': " + e.getMessage() + "\n\n" + create);
		} finally {
			Db2DAOFactoryCredito.closeConnection(conn);
		}

	}

	@Override
	public void dropTable() {
		LogController logController = new LogController();
		String[] operazione = {LocalDateTime.now().toString(), " - drop Table ParametriSistema - ", this.getClass().getSimpleName()};
		logController.printLogOperazione(operazione);
		String[] messaggio = {LocalDateTime.now().toString(), " - drop Table ParametriSistema - ", "invio - ", this.getClass().getSimpleName()};
		logController.printLogMessaggio(messaggio);
		Connection conn = Db2DAOFactoryCredito.createConnection();
		try {
			Statement stmt = conn.createStatement();
			stmt.execute(drop);
			stmt.close();
		} catch (Exception e) {
			System.err.println("dropTable(): failed to drop table '" + TABLE + "': " + e.getMessage());
		} finally {
			Db2DAOFactoryCredito.closeConnection(conn);
		}

	}


}
