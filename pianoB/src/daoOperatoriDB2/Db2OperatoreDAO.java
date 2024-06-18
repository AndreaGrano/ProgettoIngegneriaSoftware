package daoOperatoriDB2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;

import daoOperatori.OperatoreDAO;
import dominioAmministratore.Amministratore;
import dominioOperatore.Operatore;
import log.LogController;

public class Db2OperatoreDAO implements OperatoreDAO {

	// === Costanti letterali per non sbagliarsi a scrivere !!!
	// ============================
	//
	// !!! ATTENZIONE !!!
	//
	// CON L'USO DI ID AUTO INCREMENTALI, VENGONO INSERITE TUPLE SENZA ID
	// MA POI DOPO LA PERSISTENZA, NEL BEAN VA AGGIORNATO L'ID!!!

	static final String TABLE = "operatori";

	// -------------------------------------------------------------------------------------

	static final String ID = "id";
	static final String USERNAME = "username";
	static final String NOME = "nome";
	static final String COGNOME = "cognome";
	static final String TELEFONO = "telefono";
	static final String PWDHASH = "pwdhash";
	static final String BLOCCATO = "bloccato";
	static final String AMMINISTRATORE = "amministratore";

	// == STATEMENT SQL
	// ====================================================================

	// INSERT INTO table ( name,description, ...) VALUES ( ?,?, ... );
	static final String create = "INSERT " + "INTO " + TABLE + " ( " + USERNAME + ", " + NOME + ", " + COGNOME + ", "
			+ TELEFONO + ", " + PWDHASH + ", " + BLOCCATO + ", " + AMMINISTRATORE + " " + ") "
			+ "VALUES (?,?,?,?,?,?,?) ";

	// SELECT * FROM table WHERE idcolumn = ?;
	static String read_by_id = "SELECT * " + "FROM " + TABLE + " " + "WHERE " + ID + " = ? ";

	static String read_by_username = "SELECT * " + "FROM " + TABLE + " " + "WHERE " + USERNAME + " = ? ";
	
	// DELETE FROM table WHERE idcolumn = ?;
	static String delete = "DELETE " + "FROM " + TABLE + " " + "WHERE " + ID + " = ? ";

	static String delete_by_username = "DELETE " + "FROM " + TABLE + " " + "WHERE " + USERNAME + " = ? ";

	// UPDATE table SET xxxcolumn = ?, ... WHERE idcolumn = ?;
	static String update_all = "UPDATE " + TABLE + " " + "SET " + USERNAME + " = ?, " + NOME + " = ?, " + COGNOME + " = ?, "
			+ TELEFONO + " = ?, " + PWDHASH + " = ?, " + BLOCCATO + " = ?, " + AMMINISTRATORE + " = ? " + "WHERE " + ID
			+ " = ? ";

	static String update_operatore = "UPDATE " + TABLE + " " + "SET " + USERNAME + " = ?, " + NOME + " = ?, " + COGNOME
			+ " = ?, " + TELEFONO + " = ? " + "WHERE " + ID + " = ? ";
	
	static String update_hash = "UPDATE " + TABLE + " " + "SET " + PWDHASH + " = ? " + "WHERE " + USERNAME + " = ? ";

	static String update_bloccato = "UPDATE " + TABLE + " " + "SET " + BLOCCATO + " = ? " + "WHERE " + USERNAME + " = ? ";
	
	
	// SELECT * FROM table;
	static String query = "SELECT * " + "FROM " + TABLE + " ";
	

	// -------------------------------------------------------------------------------------

	// CREATE entrytable ( code INT NOT NULL PRIMARY KEY, ... );
	static String create_table = "CREATE " + "TABLE " + TABLE + " ( " + ID
			+ " INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1), " + USERNAME
			+ " VARCHAR(20) NOT NULL UNIQUE, " + NOME + " VARCHAR(40) NOT NULL, " + COGNOME + " VARCHAR(40) NOT NULL, "
			+ TELEFONO + " VARCHAR(20) NOT NULL, " + PWDHASH + " VARCHAR(200) NOT NULL, " + BLOCCATO
			+ " CHAR(1) NOT NULL CHECK ( " + BLOCCATO + " = 'Y' OR " + BLOCCATO + " = 'N' ), " + AMMINISTRATORE
			+ " CHAR(1) NOT NULL CHECK ( " + AMMINISTRATORE + " = 'Y' OR " + AMMINISTRATORE + " = 'N' ) " + ") ";

	static String drop_table = "DROP " + "TABLE " + TABLE + " ";

	// === METODI DAO
	// =========================================================================

	// metodo blocca(operatore)
	// metodo sblocca(operatore)
	// metodo isBloccato(operatore)

	@Override
	public void create(Operatore operatore, String hash) {
		LogController logController = new LogController();
		String[] operazione = {LocalDateTime.now().toString(), " - create (Operatore, hash) - ", this.getClass().getSimpleName()};
		logController.printLogOperazione(operazione);
		Connection conn = Db2DAOFactoryOperatori.createConnection();
		if (operatore == null) {
			System.err.println("create(): failed to insert a null entry");
			return;
		}
		String[] messaggio = {LocalDateTime.now().toString(), " - insert tupla dell'operatore: " + operatore.getId() + ", " + hash, " - invio - ", this.getClass().getSimpleName()};
		logController.printLogMessaggio(messaggio);
		try {
			String amministratore = operatore instanceof Amministratore ? "Y" : "N";
			PreparedStatement prep_stmt = conn.prepareStatement(create);
			prep_stmt.clearParameters();
			prep_stmt.setString(1, operatore.getUsername());
			prep_stmt.setString(2, operatore.getNome());
			prep_stmt.setString(3, operatore.getCognome());
			prep_stmt.setString(4, operatore.getTelefono());
			prep_stmt.setString(5, hash);
			prep_stmt.setString(6, "N");
			prep_stmt.setString(7, amministratore);
			prep_stmt.executeUpdate();
			prep_stmt.close();
		} catch (Exception e) {
			System.err.println("create(): failed to insert entry: " + e.getMessage());
			e.printStackTrace();
		} finally {
			Db2DAOFactoryOperatori.closeConnection(conn);
		}

	}

	@Override
	public Operatore read(int id) {
		LogController logController = new LogController();
		String[] operazione = {LocalDateTime.now().toString(), " - read Operatore (id) - ", this.getClass().getSimpleName()};
		logController.printLogOperazione(operazione);
		Operatore operatore = null;
		if (id < 0) {
			System.err.println("read(): cannot read an entry with a negative id");
			return operatore;
		}
		String[] messaggio = {LocalDateTime.now().toString(), " - read tupla dell'operatore: " + id, " - ricezione - ", this.getClass().getSimpleName()};
		logController.printLogMessaggio(messaggio);
		Connection conn = Db2DAOFactoryOperatori.createConnection();
		try {
			PreparedStatement prep_stmt = conn.prepareStatement(read_by_id);
			prep_stmt.clearParameters();
			prep_stmt.setInt(1, id);
			ResultSet rs = prep_stmt.executeQuery();
			if (rs.next()) {
				Operatore entry = new Operatore();
				entry.setId(rs.getInt(ID));
				entry.setUsername(rs.getString(USERNAME));
				entry.setNome(rs.getString(NOME));
				entry.setCognome(rs.getString(COGNOME));
				entry.setTelefono(rs.getString(TELEFONO));
				operatore = entry;
			}
			rs.close();
			prep_stmt.close();
		} catch (Exception e) {
			System.err.println("read(): failed to retrieve entry with id = " + id + ": " + e.getMessage());
			e.printStackTrace();
		} finally {
			Db2DAOFactoryOperatori.closeConnection(conn);
		}
		return operatore;
	}

	
	@Override
	public Operatore readByUsername(String username) {
		LogController logController = new LogController();
		String[] operazione = {LocalDateTime.now().toString(), " - read Operatore - ", this.getClass().getSimpleName()};
		logController.printLogOperazione(operazione);
		Operatore operatore = null;
		if (username == null) {
			System.err.println("read(): cannot read an entry with no username");
			return operatore;
		}
		String[] messaggio = {LocalDateTime.now().toString(), " - read tupla dell'operatore: " + username, " - ricezione - ", this.getClass().getSimpleName()};
		logController.printLogMessaggio(messaggio);
		Connection conn = Db2DAOFactoryOperatori.createConnection();
		try {
			PreparedStatement prep_stmt = conn.prepareStatement(read_by_username);
			prep_stmt.clearParameters();
			prep_stmt.setString(1, username);
			ResultSet rs = prep_stmt.executeQuery();
			if (rs.next()) {
				Operatore entry = new Operatore();
				entry.setId(rs.getInt(ID));
				entry.setUsername(rs.getString(USERNAME));
				entry.setNome(rs.getString(NOME));
				entry.setCognome(rs.getString(COGNOME));
				entry.setTelefono(rs.getString(TELEFONO));
				operatore = entry;
			}
			rs.close();
			prep_stmt.close();
		} catch (Exception e) {
			System.err.println("read(): failed to retrieve entry with no username: " + e.getMessage());
			e.printStackTrace();
		} finally {
			Db2DAOFactoryOperatori.closeConnection(conn);
		}
		return operatore;
	}
	
	@Override
	public String readHash(String username) {
		LogController logController = new LogController();
		String[] operazione = {LocalDateTime.now().toString(), " - read hash - ", this.getClass().getSimpleName()};
		logController.printLogOperazione(operazione);
		String hash = null;
		if (username == null) {
			System.err.println("readHash(): cannot read an entry with no username");
			return hash;
		}
		String[] messaggio = {LocalDateTime.now().toString(), " - read hash dell'operatore: " + username, " - ricezione - ", this.getClass().getSimpleName()};
		logController.printLogMessaggio(messaggio);
		Connection conn = Db2DAOFactoryOperatori.createConnection();
		try {
			PreparedStatement prep_stmt = conn.prepareStatement(read_by_username);
			prep_stmt.clearParameters();
			prep_stmt.setString(1, username);
			ResultSet rs = prep_stmt.executeQuery();
			if (rs.next()) {
				hash = rs.getString(PWDHASH);
			}
			rs.close();
			prep_stmt.close();
		} catch (Exception e) {
			System.err.println("readHash(): failed to retrieve entry with no username: " + e.getMessage());
			e.printStackTrace();
		} finally {
			Db2DAOFactoryOperatori.closeConnection(conn);
		}
		return hash;
	}
	
	@Override
	public void update(Operatore operatore) {
		LogController logController = new LogController();
		String[] operazione = {LocalDateTime.now().toString(), " - update Operatore - ", this.getClass().getSimpleName()};
		logController.printLogOperazione(operazione);
		if (operatore == null) {
			System.err.println("update(): failed to update a null entry/not char bloccato");
			return;
		}
		String[] messaggio = {LocalDateTime.now().toString(), " - update dell'operatore: " + operatore.getId(), " - invio - ", this.getClass().getSimpleName()};
		logController.printLogMessaggio(messaggio);
		Connection conn = Db2DAOFactoryOperatori.createConnection();
		try {
			PreparedStatement prep_stmt = conn.prepareStatement(Db2OperatoreDAO.update_operatore);
			prep_stmt.clearParameters();
			prep_stmt.setString(1, operatore.getUsername());
			prep_stmt.setString(2, operatore.getNome());
			prep_stmt.setString(3, operatore.getCognome());
			prep_stmt.setString(4, operatore.getTelefono());
			prep_stmt.executeUpdate();
			prep_stmt.close();
		} catch (Exception e) {
			System.err.println("insert(): failed to update entry: " + e.getMessage() + "\n\n" + update_operatore);
			e.printStackTrace();
		} finally {
			Db2DAOFactoryOperatori.closeConnection(conn);
		}

	}

	@Override
	public void update(Operatore operatore, String hash) {
		LogController logController = new LogController();
		String[] operazione = {LocalDateTime.now().toString(), " - update (operatore, hash)  - ", this.getClass().getSimpleName()};
		logController.printLogOperazione(operazione);
		if (operatore == null) {
			System.err.println("update(): failed to update a null entry/not char bloccato");
			return;
		}
		String[] messaggio = {LocalDateTime.now().toString(), " - update dell'operatore: " + operatore.getId() + ", con hash: " + hash, " - invio - ", this.getClass().getSimpleName()};
		logController.printLogMessaggio(messaggio);
		Connection conn = Db2DAOFactoryOperatori.createConnection();
		try {
			PreparedStatement prep_stmt = conn.prepareStatement(Db2OperatoreDAO.update_hash);
			prep_stmt.clearParameters();
			prep_stmt.setString(1, hash);
			prep_stmt.setString(2, operatore.getUsername());
			prep_stmt.executeUpdate();
			prep_stmt.close();
		} catch (Exception e) {
			System.err.println("insert(): failed to update entry: " + e.getMessage() + "\n\n" + update_hash);
			e.printStackTrace();
		} finally {
			Db2DAOFactoryOperatori.closeConnection(conn);
		}

	}

	@Override
	public void update(Operatore operatore, String hash, boolean isBloccato) {
		LogController logController = new LogController();
		String[] operazione = {LocalDateTime.now().toString(), " - update (operatore, hash, isBloccato)  - ", this.getClass().getSimpleName()};
		logController.printLogOperazione(operazione);
		if (operatore == null) {
			System.err.println("update(): failed to update a null entry/not char bloccato");
			return;
		}
		String[] messaggio = {LocalDateTime.now().toString(), " - update dell'operatore: " + operatore.getId() + ", con hash: " + hash + ", bloccato: " + isBloccato, " - invio - ", this.getClass().getSimpleName()};
		logController.printLogMessaggio(messaggio);
		Connection conn = Db2DAOFactoryOperatori.createConnection();
		try {
			String amministratore = operatore instanceof Amministratore ? "Y" : "N";
			String bloccato = isBloccato ? "Y" : "N";
			PreparedStatement prep_stmt = conn.prepareStatement(Db2OperatoreDAO.update_all);
			prep_stmt.clearParameters();
			prep_stmt.setString(1, operatore.getUsername());
			prep_stmt.setString(2, operatore.getNome());
			prep_stmt.setString(3, operatore.getCognome());
			prep_stmt.setString(4, operatore.getTelefono());
			prep_stmt.setString(5, hash);
			prep_stmt.setString(6, bloccato);
			prep_stmt.setString(7, amministratore);
			prep_stmt.setInt(8, operatore.getId());
			prep_stmt.executeUpdate();
			prep_stmt.close();
		} catch (Exception e) {
			System.err.println("insert(): failed to update entry: " + e.getMessage() + "\n\n" + update_all);
			e.printStackTrace();
		} finally {
			Db2DAOFactoryOperatori.closeConnection(conn);
		}

	}

	@Override
	public void delete(int id) {
		LogController logController = new LogController();
		String[] operazione = {LocalDateTime.now().toString(), " - delete Operatore (id)  - ", this.getClass().getSimpleName()};
		logController.printLogOperazione(operazione);
		if (id < 0) {
			System.err.println("delete(): cannot delete an entry with an invalid id ");
			return;
		}
		String[] messaggio = {LocalDateTime.now().toString(), " - delete dell'operatore: " + id, " - invio - ", this.getClass().getSimpleName()};
		logController.printLogMessaggio(messaggio);
		Connection conn = Db2DAOFactoryOperatori.createConnection();
		try {
			PreparedStatement prep_stmt = conn.prepareStatement(Db2OperatoreDAO.delete);
			prep_stmt.clearParameters();
			prep_stmt.setInt(1, id);
			prep_stmt.executeUpdate();
			prep_stmt.close();
		} catch (Exception e) {
			System.err.println("delete(): failed to delete entry with id = " + id + ": " + e.getMessage());
			e.printStackTrace();
		} finally {
			Db2DAOFactoryOperatori.closeConnection(conn);
		}

	}

	public void delete(String username) {
		LogController logController = new LogController();
		String[] operazione = {LocalDateTime.now().toString(), " - delete Operatore (username)  - ", this.getClass().getSimpleName()};
		logController.printLogOperazione(operazione);
		if (username == null) {
			System.err.println("delete(): cannot delete an entry with an invalid username ");
			return;
		}
		String[] messaggio = {LocalDateTime.now().toString(), " - delete dell'operatore: " + username, " - invio - ", this.getClass().getSimpleName()};
		logController.printLogMessaggio(messaggio);
		Connection conn = Db2DAOFactoryOperatori.createConnection();
		try {
			PreparedStatement prep_stmt = conn.prepareStatement(Db2OperatoreDAO.delete_by_username);
			prep_stmt.clearParameters();
			prep_stmt.setString(1, username);
			prep_stmt.executeUpdate();
			prep_stmt.close();
		} catch (Exception e) {
			System.err.println("delete(): failed to delete entry with invalid username: " + e.getMessage());
			e.printStackTrace();
		} finally {
			Db2DAOFactoryOperatori.closeConnection(conn);
		}

	}

	@Override
	public void blocca(Operatore operatore) {
		LogController logController = new LogController();
		String[] operazione = {LocalDateTime.now().toString(), " - blocca (operatore)  - ", this.getClass().getSimpleName()};
		logController.printLogOperazione(operazione);
		if (operatore == null) {
			System.err.println("update(): failed to update a null entry/not char bloccato");
			return;
		}
		String[] messaggio = {LocalDateTime.now().toString(), " - blocca l'operatore " + operatore.getId(), " - invio - ", this.getClass().getSimpleName()};
		logController.printLogMessaggio(messaggio);
		Connection conn = Db2DAOFactoryOperatori.createConnection();
		try {
			PreparedStatement prep_stmt = conn.prepareStatement(Db2OperatoreDAO.update_bloccato);
			prep_stmt.clearParameters();
			prep_stmt.setString(1, "Y");
			prep_stmt.setString(2, operatore.getUsername());
			prep_stmt.executeUpdate();
			prep_stmt.close();
		} catch (Exception e) {
			System.err.println("blocca(): failed to update entry: " + e.getMessage() + "\n\n" + update_bloccato);
			e.printStackTrace();
		} finally {
			Db2DAOFactoryOperatori.closeConnection(conn);
		}
		
	}

	@Override
	public void sblocca(Operatore operatore) {
		LogController logController = new LogController();
		String[] operazione = {LocalDateTime.now().toString(), " - sblocca (operatore)  - ", this.getClass().getSimpleName()};
		logController.printLogOperazione(operazione);
		if (operatore == null) {
			System.err.println("update(): failed to update a null entry/not char bloccato");
			return;
		}
		String[] messaggio = {LocalDateTime.now().toString(), " - sblocca l'operatore " + operatore.getId(), " - invio - ", this.getClass().getSimpleName()};
		logController.printLogMessaggio(messaggio);
		Connection conn = Db2DAOFactoryOperatori.createConnection();
		try {
			PreparedStatement prep_stmt = conn.prepareStatement(Db2OperatoreDAO.update_bloccato);
			prep_stmt.clearParameters();
			prep_stmt.setString(1, "N");
			prep_stmt.setString(2, operatore.getUsername());
			prep_stmt.executeUpdate();
			prep_stmt.close();
		} catch (Exception e) {
			System.err.println("sblocca(): failed to update entry: " + e.getMessage() + "\n\n" + update_bloccato);
			e.printStackTrace();
		} finally {
			Db2DAOFactoryOperatori.closeConnection(conn);
		}
		
	}

	@Override
	public boolean isBloccato(Operatore operatore) {
		LogController logController = new LogController();
		String[] operazione = {LocalDateTime.now().toString(), " - isBloccato (operatore) - ", this.getClass().getSimpleName()};
		logController.printLogOperazione(operazione);
		boolean isBloccato = false;
		if (operatore == null) {
			System.err.println("isBloccato(): cannot read boolean with a null operatore");
			return isBloccato;
		}
		String[] messaggio = {LocalDateTime.now().toString(), " - leggi se l'operatore " + operatore.getId() + " e' bloccato oppure no - ", "ricezione - ", this.getClass().getSimpleName()};
		logController.printLogMessaggio(messaggio);
		Connection conn = Db2DAOFactoryOperatori.createConnection();
		try {
			PreparedStatement prep_stmt = conn.prepareStatement(read_by_username);
			prep_stmt.clearParameters();
			prep_stmt.setString(1, operatore.getUsername());
			ResultSet rs = prep_stmt.executeQuery();
			if (rs.next()) {
				isBloccato = rs.getString(BLOCCATO).equals("Y") ? true : false;
			}
			rs.close();
			prep_stmt.close();
		} catch (Exception e) {
			System.err.println("isBloccato(): failed to retrieve entry with username = " + operatore.getUsername() + ": " + e.getMessage());
			e.printStackTrace();
		} finally {
			Db2DAOFactoryOperatori.closeConnection(conn);
		}
		return isBloccato;
	}
	
	@Override
	public void createTable() {
		LogController logController = new LogController();
		String[] operazione = {LocalDateTime.now().toString(), " - create Table Operatori - ", this.getClass().getSimpleName()};
		logController.printLogOperazione(operazione);
		String[] messaggio = {LocalDateTime.now().toString(), " - create Table Operatori - ", "invio - ", this.getClass().getSimpleName()};
		logController.printLogMessaggio(messaggio);
		Connection conn = Db2DAOFactoryOperatori.createConnection();
		try {
			Statement stmt = conn.createStatement();
			stmt.execute(Db2OperatoreDAO.create_table);
			stmt.close();
		} catch (Exception e) {
			System.err.println(
					"createTable(): failed to create table '" + TABLE + "': " + e.getMessage() + "\n\n" + create_table);
		} finally {
			Db2DAOFactoryOperatori.closeConnection(conn);
		}

	}

	@Override
	public void dropTable() {
		LogController logController = new LogController();
		String[] operazione = {LocalDateTime.now().toString(), " - drop Table Operatori - ", this.getClass().getSimpleName()};
		logController.printLogOperazione(operazione);
		String[] messaggio = {LocalDateTime.now().toString(), " - drop Table Operatori - ", "invio - ", this.getClass().getSimpleName()};
		logController.printLogMessaggio(messaggio);
		Connection conn = Db2DAOFactoryOperatori.createConnection();
		try {
			Statement stmt = conn.createStatement();
			stmt.execute(Db2OperatoreDAO.drop_table);
			stmt.close();
		} catch (Exception e) {
			System.err.println("dropTable(): failed to drop table '" + TABLE + "': " + e.getMessage());
		} finally {
			Db2DAOFactoryOperatori.closeConnection(conn);
		}

	}

}
