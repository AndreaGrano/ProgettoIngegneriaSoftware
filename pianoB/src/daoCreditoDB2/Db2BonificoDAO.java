package daoCreditoDB2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

import daoCredito.BonificoDAO;
import dominioBonifico.*;
import log.LogController;

public class Db2BonificoDAO implements BonificoDAO {

	// === Costanti letterali per non sbagliarsi a scrivere !!!
	// ============================

	static final String TABLE = "bonifici";

	// -------------------------------------------------------------------------------------

	static final String ID = "id";
	static final String IBAN = "iban";
	static final String DATA_VALUTA = "data_valuta";
	static final String CAUSALE = "causale";
	static final String IMPORTO = "importo";
	static final String CREDITI_DA_RICONCILIARE = "crediti_da_riconciliare";
	static final String CREDITI_RICONCILIATI = "crediti_riconciliati";

	static final String CONSTRAINT = "chk_num_crediti";

	// == STATEMENT SQL
	// ====================================================================

	// INSERT INTO table ( name,description, ...) VALUES ( ?,?, ... );
	static final String create = "INSERT " + "INTO " + TABLE + " ( " + IBAN + ", " + DATA_VALUTA + ", " + CAUSALE + ", "
			+ IMPORTO + ", " + CREDITI_DA_RICONCILIARE + ", " + CREDITI_RICONCILIATI + " " + ") "
			+ "VALUES (?,?,?,?,?,?) ";

	// SELECT * FROM table WHERE idcolumn = ?;
	static String read_by_id = "SELECT * " + "FROM " + TABLE + " " + "WHERE " + ID + " = ? ";

	static String read_by_bonifico = "SELECT * " + " FROM " + TABLE + " " + "WHERE " + IBAN + " = ? AND " + DATA_VALUTA
			+ " = ? AND " + CAUSALE + " = ? AND " + IMPORTO + " = ? ";

	// DELETE FROM table WHERE idcolumn = ?;
	static String delete = "DELETE " + "FROM " + TABLE + " " + "WHERE " + ID + " = ? ";

	// UPDATE table SET xxxcolumn = ?, ... WHERE idcolumn = ?;
	static String update = "UPDATE " + TABLE + " " + "SET " + IBAN + " = ?, " + DATA_VALUTA + " = ?, " + CAUSALE
			+ " = ?, " + IMPORTO + " = ?, " + CREDITI_DA_RICONCILIARE + " = ?, " + CREDITI_RICONCILIATI + " = "
			+ CREDITI_RICONCILIATI + " + ? " + "WHERE " + ID + " = ? ";

	// SELECT *
	// FROM BONIFICI
	// WHERE ID_RICONCILIAZIONE IS NULL
	// static String read_bonifici_non_riconciliati = "SELECT * FROM " + TABLE + "
	// WHERE " + ID_RICONCILIAZIONE
	// + " IS NULL";

	// static String read_bonifici_riconciliati = "SELECT * FROM " + TABLE + " WHERE
	// " + ID_RICONCILIAZIONE
	// + " IS NOT NULL";

	// SELECT * FROM table;
	static String read_all = "SELECT * " + "FROM " + TABLE + " ";

	// SELECT *
	// FROM BONIFICI
	// WHERE CREDITI_DA_RICONCILIARE > CREDITI_RICONCILIATI
	static String read_bonifici_non_riconciliati = "SELECT * FROM " + TABLE + " WHERE " + CREDITI_DA_RICONCILIARE
			+ " > " + CREDITI_RICONCILIATI + " ";

	static String read_bonifici_riconciliati = "SELECT * FROM " + TABLE + " WHERE " + CREDITI_DA_RICONCILIARE + " = "
			+ CREDITI_RICONCILIATI + " ";

	// -------------------------------------------------------------------------------------

	// CREATE entrytable ( code INT NOT NULL PRIMARY KEY, ... );
	static String create_table = "CREATE " + "TABLE " + TABLE + " ( " + ID
			+ " INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1), " + IBAN
			+ " VARCHAR(30) NOT NULL, " + DATA_VALUTA + " DATE NOT NULL" + ", " + CAUSALE + " VARCHAR(40) NOT NULL, "
			+ IMPORTO + " DOUBLE NOT NULL, " + CREDITI_DA_RICONCILIARE + " INT NOT NULL, " + CREDITI_RICONCILIATI
			+ " INT NOT NULL" + ", " + "CONSTRAINT " + CONSTRAINT + " CHECK" + " ( " + CREDITI_RICONCILIATI + " <= "
			+ CREDITI_DA_RICONCILIARE + " ) " + " ) ";

	// bisognerebbe aggiungere constraint crediti_riconciliati <=
	// crediti_da_riconciliare

	static String drop = "DROP " + "TABLE " + TABLE + " ";

	// === METODI DAO
	// =========================================================================

	@Override
	public void create(Bonifico bonifico) {
		LogController logController = new LogController();
		String[] operazione = { LocalDateTime.now().toString(), " - create (Bonifico) - ",
				this.getClass().getSimpleName() };
		logController.printLogOperazione(operazione);
		if (bonifico == null) {
			System.err.println("create(): failed to insert a null entry");
			return;
		}
		String[] messaggio = { LocalDateTime.now().toString(), " - insert tupla del bonifico: " + bonifico.getId(),
				" - invio - ", this.getClass().getSimpleName() };
		logController.printLogMessaggio(messaggio);
		Connection conn = Db2DAOFactoryCredito.createConnection();

		try {
			PreparedStatement prep_stmt = conn.prepareStatement(create);
			prep_stmt.clearParameters();
			prep_stmt.setString(1, bonifico.getIBAN());
			prep_stmt.setDate(2, java.sql.Date.valueOf(bonifico.getDataValuta()));
			prep_stmt.setString(3, bonifico.getCausale());
			prep_stmt.setDouble(4, bonifico.getImporto());
			prep_stmt.setInt(5, bonifico.getCausale().split(" ").length);
			prep_stmt.setInt(6, 0);
			prep_stmt.executeUpdate();
			prep_stmt.close();
		} catch (Exception e) {
			System.err.println("create(): failed to insert entry: " + e.getMessage());
			e.printStackTrace();
		} finally {
			Db2DAOFactoryCredito.closeConnection(conn);
		}

	}

	@Override
	public Bonifico read(int id) {
		LogController logController = new LogController();
		String[] operazione = { LocalDateTime.now().toString(), " - read Bonifico (id) - ",
				this.getClass().getSimpleName() };
		logController.printLogOperazione(operazione);
		Bonifico bon = null;
		if (id < 0) {
			System.err.println("read(): cannot read an entry with a negative id");
			return bon;
		}
		String[] messaggio = { LocalDateTime.now().toString(), " - read tupla del bonifico: " + id, " - ricezione - ",
				this.getClass().getSimpleName() };
		logController.printLogMessaggio(messaggio);
		Connection conn = Db2DAOFactoryCredito.createConnection();
		try {
			PreparedStatement prep_stmt = conn.prepareStatement(read_by_id);
			prep_stmt.clearParameters();
			prep_stmt.setInt(1, id);
			ResultSet rs = prep_stmt.executeQuery();
			if (rs.next()) {
				Bonifico entry = new BonificoNonRiconciliato();
				entry.setId(rs.getInt(ID));
				entry.setIBAN(rs.getString(IBAN));
				java.sql.Date d = rs.getDate(DATA_VALUTA);
				entry.setDataValuta(d.toLocalDate());
				entry.setCausale(rs.getString(CAUSALE));
				entry.setImporto(rs.getDouble(IMPORTO));
				bon = entry;
			}
			rs.close();
			prep_stmt.close();
		} catch (Exception e) {
			System.err.println("read(): failed to retrieve entry with id = " + id + ": " + e.getMessage());
			e.printStackTrace();
		} finally {
			Db2DAOFactoryCredito.closeConnection(conn);
		}
		return bon;
	}

	@Override
	public int readId(Bonifico bonifico) {
		LogController logController = new LogController();
		String[] operazione = { LocalDateTime.now().toString(), " - read id (Bonifico) - ",
				this.getClass().getSimpleName() };
		logController.printLogOperazione(operazione);
		int id = -1;
		if (bonifico == null) {
			System.err.println("read(): cannot read an entry with a null bonifico");
			return id;
		}
		String[] messaggio = { LocalDateTime.now().toString(), " - read id del bonifico: " + bonifico.toString(),
				" - ricezione - ", this.getClass().getSimpleName() };
		logController.printLogMessaggio(messaggio);
		Connection conn = Db2DAOFactoryCredito.createConnection();
		try {
			PreparedStatement prep_stmt = conn.prepareStatement(read_by_bonifico);
			prep_stmt.clearParameters();
			prep_stmt.setString(1, bonifico.getIBAN());
			prep_stmt.setDate(2, java.sql.Date.valueOf(bonifico.getDataValuta()));
			prep_stmt.setString(3, bonifico.getCausale());
			prep_stmt.setDouble(4, bonifico.getImporto());
			ResultSet rs = prep_stmt.executeQuery();
			if (rs.next()) {
				id = rs.getInt(ID);
			}
			rs.close();
			prep_stmt.close();
		} catch (Exception e) {
			System.err.println("read(): failed to retrieve entry with id = " + id + ": " + e.getMessage());
			e.printStackTrace();
		} finally {
			Db2DAOFactoryCredito.closeConnection(conn);
		}
		return id;
	}

	@Override
	public boolean update(Bonifico bonifico, int nuoveRiconciliazioni) {
		LogController logController = new LogController();
		String[] operazione = { LocalDateTime.now().toString(), " - update Bonifico, nuoveRiconciliazioni  - ",
				this.getClass().getSimpleName() };
		logController.printLogOperazione(operazione);
		boolean res = true;
		if (bonifico == null) {
			res = false;
			System.err.println("update(): cannot update an entry with a null bonifico");
			return res;
		}
		String[] messaggio = { LocalDateTime.now().toString(),
				" - update del bonifico + nuoveRiconciliazioni: " + bonifico.getId(), " - invio - ",
				this.getClass().getSimpleName() };
		logController.printLogMessaggio(messaggio);
		Connection conn = Db2DAOFactoryCredito.createConnection();
		try {
			PreparedStatement prep_stmt = conn.prepareStatement(update);
			prep_stmt.clearParameters();
			prep_stmt.setString(1, bonifico.getIBAN());
			prep_stmt.setDate(2, java.sql.Date.valueOf(bonifico.getDataValuta()));
			prep_stmt.setString(3, bonifico.getCausale());
			prep_stmt.setDouble(4, bonifico.getImporto());
			prep_stmt.setInt(5, bonifico.getCausale().split(" ").length); // crediti da riconciliare
			prep_stmt.setInt(6, nuoveRiconciliazioni); // crediti riconciliati
			prep_stmt.setInt(7, readMyId(bonifico)); // id del bonifico -> read
			prep_stmt.executeUpdate();
			prep_stmt.close();
		} catch (Exception e) {
			res = false;
			System.err.println(
					"update(): failed to retrieve entry with id = " + bonifico.getId() + ": " + e.getMessage());
			e.printStackTrace();
			return res;
		} finally {
			Db2DAOFactoryCredito.closeConnection(conn);
		}

		return res;
	}

	@Override
	public boolean delete(int id) {
		LogController logController = new LogController();
		String[] operazione = { LocalDateTime.now().toString(), " - delete Bonifico (id)  - ",
				this.getClass().getSimpleName() };
		logController.printLogOperazione(operazione);
		if (id < 0) {
			System.err.println("delete(): cannot delete an entry with an invalid id ");
			return false;
		}
		String[] messaggio = { LocalDateTime.now().toString(), " - delete del bonifico: " + id, " - invio - ",
				this.getClass().getSimpleName() };
		logController.printLogMessaggio(messaggio);
		Connection conn = Db2DAOFactoryCredito.createConnection();
		try {
			PreparedStatement prep_stmt = conn.prepareStatement(Db2BonificoDAO.delete);
			prep_stmt.clearParameters();
			prep_stmt.setInt(1, id);
			prep_stmt.executeUpdate();
			prep_stmt.close();
		} catch (Exception e) {
			System.err.println("delete(): failed to delete entry with id = " + id + ": " + e.getMessage());
			e.printStackTrace();
			return false;
		} finally {
			Db2DAOFactoryCredito.closeConnection(conn);
		}
		return true;
	}

	@Override
	public boolean createTable() {
		LogController logController = new LogController();
		String[] operazione = { LocalDateTime.now().toString(), " - create Table Bonifici - ",
				this.getClass().getSimpleName() };
		logController.printLogOperazione(operazione);
		String[] messaggio = { LocalDateTime.now().toString(), " - create Table Bonifici - ", "invio - ",
				this.getClass().getSimpleName() };
		logController.printLogMessaggio(messaggio);
		boolean res = true;
		Connection conn = Db2DAOFactoryCredito.createConnection();
		try {
			Statement stmt = conn.createStatement();
			stmt.execute(Db2BonificoDAO.create_table);
			stmt.close();
		} catch (Exception e) {
			System.err.println(
					"createTable(): failed to create table '" + TABLE + "': " + e.getMessage() + "\n\n" + create_table);
			res = false;
		} finally {
			Db2DAOFactoryCredito.closeConnection(conn);
		}
		return res;
	}

	@Override
	public boolean dropTable() {
		LogController logController = new LogController();
		String[] operazione = { LocalDateTime.now().toString(), " - delete Table Bonifici - ",
				this.getClass().getSimpleName() };
		logController.printLogOperazione(operazione);
		String[] messaggio = { LocalDateTime.now().toString(), " - delete Table Bonifici - ", "invio - ",
				this.getClass().getSimpleName() };
		logController.printLogMessaggio(messaggio);
		boolean res = true;
		Connection conn = Db2DAOFactoryCredito.createConnection();
		try {
			Statement stmt = conn.createStatement();
			stmt.execute(Db2BonificoDAO.drop);
			stmt.close();
		} catch (Exception e) {
			System.err.println("dropTable(): failed to drop table '" + TABLE + "': " + e.getMessage());
			res = false;
		} finally {
			Db2DAOFactoryCredito.closeConnection(conn);
		}
		return res;
	}

	// !!!
	// Se esistono questi due metodi sotto, vuol dire presumibilmente
	// che ho bisogno di un altro attributo, l'id del credito relativo,
	// perche' il primo metodo richiede solo i bonifici non riconciliati
	// mentre l'altro tutti, riconciliati compresi
	// !!!

	@Override
	public BonificiNonRiconciliati readBonificiNonRiconciliati() {
		LogController logController = new LogController();
		String[] operazione = { LocalDateTime.now().toString(), " - read bonifici non riconciliati - ",
				this.getClass().getSimpleName() };
		logController.printLogOperazione(operazione);
		String[] messaggio = { LocalDateTime.now().toString(), " - read bonifici non riconciliati - ", "ricevo - ",
				this.getClass().getSimpleName() };
		logController.printLogMessaggio(messaggio);
		Connection conn = Db2DAOFactoryCredito.createConnection();
		BonificiNonRiconciliati bonifici = new BonificiNonRiconciliati();
		BonificoNonRiconciliato bonifico = null;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(read_bonifici_non_riconciliati);
			while (rs.next()) {
				bonifico = new BonificoNonRiconciliato();
				bonifico.setId(rs.getInt(ID));
				bonifico.setIBAN(rs.getString(IBAN));
				bonifico.setDataValuta(rs.getDate(DATA_VALUTA).toLocalDate());
				bonifico.setCausale(rs.getString(CAUSALE));
				bonifico.setImporto(rs.getDouble(IMPORTO));
				bonifici.add(bonifico);
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			System.err.println(
					"readBonificiNonRiconciliati(): failed to read bonifici non riconciliati: " + e.getMessage());
		} finally {
			Db2DAOFactoryCredito.closeConnection(conn);
		}
		return bonifici;
	}

	@Override
	public Bonifici readAll() { // ---------> ! LEGGIMI !
		// QUESTO METODO NON E' MOLTO EFFICIENTE PERCHE' FA USO DI 2 QUERY
		// AL POSTO DI UNA. CIO' E' STATO FATTO PER GESTIRE CON PIU' SEMPLICITA'
		// IL VALORE NULL DELLA FOREIGN KEY
		// IN REALTA' BASTEREBBE ANCHE UNA SOLA QUERY UNIVERSALE MA BISOGNA
		// POI VERIFICARE IL VALORE DELLA FOREIGN KEY E AGIRE DI CONSEGUENZA
		LogController logController = new LogController();
		String[] operazione = { LocalDateTime.now().toString(), " - read tutti bonifici - ",
				this.getClass().getSimpleName() };
		logController.printLogOperazione(operazione);
		String[] messaggio = { LocalDateTime.now().toString(), " - read tutti bonifici - ", "ricevo - ",
				this.getClass().getSimpleName() };
		logController.printLogMessaggio(messaggio);
		Connection conn = Db2DAOFactoryCredito.createConnection();
		Bonifici bonifici = new Bonifici();
		Bonifico bonifico = null;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rsNonRiconciliati = stmt.executeQuery(read_bonifici_non_riconciliati);
			while (rsNonRiconciliati.next()) {
				bonifico = new BonificoNonRiconciliato();
				bonifico.setId(rsNonRiconciliati.getInt(ID));
				bonifico.setIBAN(rsNonRiconciliati.getString(IBAN));
				bonifico.setDataValuta(rsNonRiconciliati.getDate(DATA_VALUTA).toLocalDate());
				bonifico.setCausale(rsNonRiconciliati.getString(CAUSALE));
				bonifico.setImporto(rsNonRiconciliati.getDouble(IMPORTO));
				bonifici.add(bonifico);
			}
			rsNonRiconciliati.close();
			stmt.close();

			Statement stmt2 = conn.createStatement();
			ResultSet rsRiconciliati = stmt2.executeQuery(read_bonifici_riconciliati);
			while (rsRiconciliati.next()) {
				bonifico = new BonificoRiconciliato();
				bonifico.setId(rsRiconciliati.getInt(ID));
				bonifico.setIBAN(rsRiconciliati.getString(IBAN));
				bonifico.setDataValuta(rsRiconciliati.getDate(DATA_VALUTA).toLocalDate());
				bonifico.setCausale(rsRiconciliati.getString(CAUSALE));
				bonifico.setImporto(rsRiconciliati.getDouble(IMPORTO));
				bonifici.add(bonifico);
			}
			rsRiconciliati.close();
			stmt2.close();
		} catch (SQLException e) {
			System.err.println(
					"readBonificiNonRiconciliati(): failed to read bonifici non riconciliati: " + e.getMessage());
		} finally {
			Db2DAOFactoryCredito.closeConnection(conn);
		}
		return bonifici;
	}

	private static int readMyId(Bonifico bonifico) {
		int id = -1;
		if (bonifico == null) {
			System.err.println("read(): cannot read an entry with a null bonifico");
			return id;
		}
		Connection conn = Db2DAOFactoryCredito.createConnection();
		try {
			PreparedStatement prep_stmt = conn.prepareStatement(read_by_bonifico);
			prep_stmt.clearParameters();
			prep_stmt.setString(1, bonifico.getIBAN());
			prep_stmt.setDate(2, java.sql.Date.valueOf(bonifico.getDataValuta()));
			prep_stmt.setString(3, bonifico.getCausale());
			prep_stmt.setDouble(4, bonifico.getImporto());
			ResultSet rs = prep_stmt.executeQuery();
			if (rs.next()) {
				id = rs.getInt(ID);
			}
			rs.close();
			prep_stmt.close();
		} catch (Exception e) {
			System.err.println("read(): failed to retrieve entry with id = " + id + ": " + e.getMessage());
			e.printStackTrace();
		} finally {
			Db2DAOFactoryCredito.closeConnection(conn);
		}
		return id;
	}

}
