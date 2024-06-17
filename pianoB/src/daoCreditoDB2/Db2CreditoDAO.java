package daoCreditoDB2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDateTime;

import daoCredito.ClienteDAO;
import daoCredito.CreditoDAO;
import daoCredito.DAOFactoryCredito;
import dominioCredito.Cliente;
import dominioCredito.Crediti;
import dominioCredito.CreditiScaduti;
import dominioCredito.Credito;
import dominioCredito.CreditoNonRiconciliato;
import dominioCredito.CreditoRiconciliato;
import dominioCredito.CreditoScaduto;
//import timerETimerTask.AllTimerScadenza;
import log.LogController;
import timerETimerTask.AllTimerScadenza;

public class Db2CreditoDAO implements CreditoDAO {

	// === Costanti letterali per non sbagliarsi a scrivere !!!
	// ============================

	static final String TABLE = "crediti";

	// -------------------------------------------------------------------------------------

	static final String ID = "id";
	static final String DATA_STIPULA = "data_stipula";
	static final String IMPORTO = "importo";
	static final String CAUSALE = "causale";
	static final String ID_STIPULATO_DA = "id_stipulato_da";
	static final String ID_BONIFICO_RICONCILIATO = "id_bonifico_riconciliato";

	// == STATEMENT SQL
	// ====================================================================

	// INSERT INTO table ( name,description, ...) VALUES ( ?,?, ... );
	static final String create = "INSERT " + "INTO " + TABLE + " ( " + DATA_STIPULA + ", " + IMPORTO + ", " + CAUSALE
			+ ", " + ID_STIPULATO_DA + ", " + ID_BONIFICO_RICONCILIATO + " " + ") " + "VALUES (?,?,?,?,?) ";

	// SELECT * FROM table WHERE idcolumn = ?;
	// static String read_by_id = "SELECT * " + "FROM " + TABLE + " " + "WHERE " +
	// ID + " = ? ";

	// DELETE FROM table WHERE idcolumn = ?;
	static String delete = "DELETE " + "FROM " + TABLE + " " + "WHERE " + ID + " = ? ";

	// UPDATE table SET xxxcolumn = ?, ... WHERE idcolumn = ?;
	static String update = "UPDATE " + TABLE + " " + "SET " + DATA_STIPULA + " = ?, " + IMPORTO + " = ?, " + CAUSALE
			+ " = ?, " + ID_STIPULATO_DA + " = ?, " + ID_BONIFICO_RICONCILIATO + " = ? " + "WHERE " + ID + " = ? ";

	static String update_credito = "UPDATE " + TABLE + " " + "SET " + DATA_STIPULA + " = ?, " + IMPORTO + " = ?, "
			+ CAUSALE + " = ?, " + ID_STIPULATO_DA + " = ? " + "WHERE " + ID + " = ? ";

	// SELECT * FROM table;

	static String read = "SELECT * " + "FROM " + TABLE + " ";

	static String read_all = "SELECT * " + "FROM " + TABLE + ", " + Db2ClienteDAO.TABLE + " WHERE " + TABLE + "."
			+ ID_STIPULATO_DA + " = " + Db2ClienteDAO.TABLE + "." + Db2ClienteDAO.ID + " ";

	static String read_all_c = "SELECT * " + "FROM " + TABLE + ", " + Db2ClienteDAO.TABLE + " WHERE " + TABLE + "."
			+ ID_STIPULATO_DA + " = " + Db2ClienteDAO.TABLE + "." + Db2ClienteDAO.ID + " ";

	static String read_by_id = read_all + "AND " + TABLE + "." + ID + " = ? ";
	
	static String read_by_id_credito = read + "WHERE " + ID + " = ? ";

	static String read_by_id_cliente = "SELECT " + Db2ClienteDAO.TABLE + ".* " + "FROM " + TABLE + ", "
			+ Db2ClienteDAO.TABLE + " WHERE " + TABLE + "." + ID_STIPULATO_DA + " = " + Db2ClienteDAO.TABLE + "."
			+ Db2ClienteDAO.ID + " AND " + TABLE + "." + ID + " = ? ";

	static String read_non_riconciliati = read_all + "AND " + ID_BONIFICO_RICONCILIATO + " IS NULL ";

	// -------------------------------------------------------------------------------------

	// CREATE entrytable ( code INT NOT NULL PRIMARY KEY, ... );
	static String create_table = "CREATE " + "TABLE " + TABLE + " ( " + ID
			+ " INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1), " + DATA_STIPULA
			+ " DATE NOT NULL, " + IMPORTO + " DOUBLE NOT NULL, " + CAUSALE + " VARCHAR(40) NOT NULL UNIQUE, "
			+ ID_STIPULATO_DA + " INT NOT NULL REFERENCES " + Db2ClienteDAO.TABLE + ", " + ID_BONIFICO_RICONCILIATO
			+ " INT REFERENCES " + Db2BonificoDAO.TABLE + " ) ";

	static String drop = "DROP " + "TABLE " + TABLE + " ";

	// === METODI DAO
	// =========================================================================

	@Override
	public void create(Credito credito) {
		LogController logController = new LogController();
		String[] operazione = { LocalDateTime.now().toString(), " - create (credito) - ",
				this.getClass().getSimpleName() };
		logController.printLogOperazione(operazione);
		Connection conn = Db2DAOFactoryCredito.createConnection();
		if (credito == null) {
			System.err.println("create(): failed to insert a null entry");
			return;
		}
		String[] messaggio = { LocalDateTime.now().toString(), " - insert tupla del credito: " + credito.getId(),
				" - invio - ", this.getClass().getSimpleName() };
		logController.printLogMessaggio(messaggio);
		try {
			PreparedStatement prep_stmt = conn.prepareStatement(create);
			prep_stmt.clearParameters();
			prep_stmt.setDate(1, java.sql.Date.valueOf(credito.getDataStipula()));
			prep_stmt.setDouble(2, credito.getImporto());
			prep_stmt.setString(3, credito.getCausale());
			prep_stmt.setInt(4, credito.getCliente().getId());
			prep_stmt.setNull(5, Types.INTEGER);
			prep_stmt.executeUpdate();
			prep_stmt.close();
		} catch (Exception e) {
			System.err.println("create(): failed to insert entry: " + e.getMessage() + "\n\n" + create);
			e.printStackTrace();
		} finally {
			Db2DAOFactoryCredito.closeConnection(conn);
		}

	}

	@Override
	public Credito read(int id) {
		LogController logController = new LogController();
		String[] operazione = { LocalDateTime.now().toString(), " - read credito (id) - ",
				this.getClass().getSimpleName() };
		logController.printLogOperazione(operazione);
		Credito credito = null;
		if (id < 0) {
			System.err.println("read(): cannot read an entry with a negative id");
			return credito;
		}
		String[] messaggio = { LocalDateTime.now().toString(), " - read tupla del credito: " + id, " - ricezione - ",
				this.getClass().getSimpleName() };
		logController.printLogMessaggio(messaggio);
		Connection conn = Db2DAOFactoryCredito.createConnection();
		try {
			PreparedStatement prep_stmt = conn.prepareStatement(read_by_id);
			prep_stmt.clearParameters();
			prep_stmt.setInt(1, id);
			ResultSet rs = prep_stmt.executeQuery();
			Credito entry = null;
			if (rs.next()) {
				entry = (rs.getInt(ID_BONIFICO_RICONCILIATO) == 0) ? new CreditoNonRiconciliato()
						: new CreditoRiconciliato();
				entry.setId(rs.getInt(1));
				java.sql.Date d = rs.getDate(2);
				entry.setDataStipula(d.toLocalDate());
				entry.setImporto(rs.getDouble(3));
				entry.setCausale(rs.getString(4));
				Cliente cl = new Cliente();
				cl.setId(rs.getInt(7));
				cl.setCodiceFiscale(rs.getString(8));
				cl.setNome(rs.getString(9));
				cl.setCognome(rs.getString(10));
				cl.setDataNascita(rs.getDate(11).toLocalDate());
				cl.setTelefono(rs.getString(12));
				cl.setIndirizzo(rs.getString(13) + ","
						+ rs.getString(14) + "," + rs.getString(15)
						+ "," + rs.getString(16));
				entry.setCliente(cl);
				credito = entry;
			}
			rs.close();
			prep_stmt.close();
		} catch (Exception e) {
			System.err.println("read(): failed to retrieve entry with id = " + id + ": " + e.getMessage() + "\n\n"
					+ read_by_id);
			e.printStackTrace();
		} finally {
			Db2DAOFactoryCredito.closeConnection(conn);
		}
		return credito;
	}
	
	@Override
	public void update(Credito credito) {
		LogController logController = new LogController();
		String[] operazione = { LocalDateTime.now().toString(), " - update Credito  - ",
				this.getClass().getSimpleName() };
		logController.printLogOperazione(operazione);
		if (credito == null) {
			System.err.println("update(): cannot update an entry with a null credito");
			return;
		}
		String[] messaggio = { LocalDateTime.now().toString(), " - update del credito: " + credito.getId(),
				" - invio - ", this.getClass().getSimpleName() };
		logController.printLogMessaggio(messaggio);
		Connection conn = Db2DAOFactoryCredito.createConnection();
		try {
			PreparedStatement prep_stmt = conn.prepareStatement(update_credito);
			prep_stmt.clearParameters();
			prep_stmt.setDate(1, java.sql.Date.valueOf(credito.getDataStipula()));
			prep_stmt.setDouble(2, credito.getImporto());
			prep_stmt.setString(3, credito.getCausale());
			prep_stmt.setInt(4, credito.getCliente().getId()); // id stipulato
			prep_stmt.setInt(5, credito.getId());
			prep_stmt.executeUpdate();
			prep_stmt.close();
		} catch (Exception e) {
			System.err
					.println("update(): failed to retrieve entry with id = " + credito.getId() + ": " + e.getMessage());
			e.printStackTrace();
		} finally {
			Db2DAOFactoryCredito.closeConnection(conn);
		}

	}

	@Override
	public void update(Credito credito, int idBonifico) {
		LogController logController = new LogController();
		String[] operazione = { LocalDateTime.now().toString(), " - update Credito, idBonifico - ",
				this.getClass().getSimpleName() };
		logController.printLogOperazione(operazione);
		if (credito == null) {
			System.err.println("update(): cannot update an entry with a null credito");
			return;
		}
		String[] messaggio = { LocalDateTime.now().toString(),
				" - update del credito riconciliato (credito, idBonifico): " + credito.getId() + ", " + idBonifico,
				" - invio - ", this.getClass().getSimpleName() };
		logController.printLogMessaggio(messaggio);
		Connection conn = Db2DAOFactoryCredito.createConnection();
		try {
			PreparedStatement prep_stmt = conn.prepareStatement(update);
			prep_stmt.clearParameters();
			prep_stmt.setDate(1, java.sql.Date.valueOf(credito.getDataStipula()));
			prep_stmt.setDouble(2, credito.getImporto());
			prep_stmt.setString(3, credito.getCausale());
			prep_stmt.setInt(4, credito.getCliente().getId()); // id stipulato
			prep_stmt.setInt(5, idBonifico); // id bonifico
			prep_stmt.setInt(6, credito.getId());
			prep_stmt.executeUpdate();
			prep_stmt.close();
		} catch (Exception e) {
			System.err
					.println("update(): failed to retrieve entry with id = " + credito.getId() + ": " + e.getMessage());
			e.printStackTrace();
		} finally {
			Db2DAOFactoryCredito.closeConnection(conn);
		}

	}

	@Override
	public void delete(int id) {
		LogController logController = new LogController();
		String[] operazione = { LocalDateTime.now().toString(), " - delete Credito (id)  - ",
				this.getClass().getSimpleName() };
		logController.printLogOperazione(operazione);
		if (id < 0) {
			System.err.println("delete(): cannot delete an entry with an invalid id ");
			return;
		}
		String[] messaggio = { LocalDateTime.now().toString(), " - delete del credito: " + id, " - invio - ",
				this.getClass().getSimpleName() };
		logController.printLogMessaggio(messaggio);
		Connection conn = Db2DAOFactoryCredito.createConnection();
		try {
			PreparedStatement prep_stmt = conn.prepareStatement(delete);
			prep_stmt.clearParameters();
			prep_stmt.setInt(1, id);
			prep_stmt.executeUpdate();
			prep_stmt.close();
		} catch (Exception e) {
			System.err.println("delete(): failed to delete entry with id = " + id + ": " + e.getMessage());
			e.printStackTrace();
		} finally {
			Db2DAOFactoryCredito.closeConnection(conn);
		}

	}

	@Override
	public void createTable() {
		LogController logController = new LogController();
		String[] operazione = { LocalDateTime.now().toString(), " - create Table Crediti - ",
				this.getClass().getSimpleName() };
		logController.printLogOperazione(operazione);
		String[] messaggio = { LocalDateTime.now().toString(), " - create Table Crediti - ", " - invio - ",
				this.getClass().getSimpleName() };
		logController.printLogMessaggio(messaggio);
		Connection conn = Db2DAOFactoryCredito.createConnection();
		try {
			Statement stmt = conn.createStatement();
			stmt.execute(create_table);
			stmt.close();
		} catch (Exception e) {
			System.err.println(
					"createTable(): failed to create table '" + TABLE + "': " + e.getMessage() + "\n\n" + create_table);
		} finally {
			Db2DAOFactoryCredito.closeConnection(conn);
		}

	}

	@Override
	public void dropTable() {
		LogController logController = new LogController();
		String[] operazione = { LocalDateTime.now().toString(), " - drop Table Crediti - ",
				this.getClass().getSimpleName() };
		logController.printLogOperazione(operazione);
		String[] messaggio = { LocalDateTime.now().toString(), " - drop Table Crediti - ", " - invio - ",
				this.getClass().getSimpleName() };
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

	@Override
	public CreditiScaduti readCreditiScaduti() {
		LogController logController = new LogController();
		String[] operazione = {LocalDateTime.now().toString(), " - read Crediti Scaduti - ", this.getClass().getSimpleName()};
		logController.printLogOperazione(operazione);
		String[] messaggio = {LocalDateTime.now().toString(), " - read Crediti Scaduti", " - ricezione - ", this.getClass().getSimpleName()};
		logController.printLogMessaggio(messaggio);
		CreditiScaduti crediti = new CreditiScaduti();
		Connection conn = Db2DAOFactoryCredito.createConnection();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(read_all);
			while (rs.next()) {
				if (rs.getInt(ID_BONIFICO_RICONCILIATO) == 0) { 
					CreditoScaduto credito = new CreditoScaduto();
					Cliente cl = new Cliente();
					credito.setId(rs.getInt(1));
					java.sql.Date d = rs.getDate(2);
					credito.setDataStipula(d.toLocalDate());
					credito.setImporto(rs.getDouble(3));
					credito.setCausale(rs.getString(4)); // -----> E' CAUSALE!!!
					cl.setId(rs.getInt(7));
					cl.setCodiceFiscale(rs.getString(8));
					cl.setNome(rs.getString(9));
					cl.setCognome(rs.getString(10));
					cl.setDataNascita(rs.getDate(11).toLocalDate());
					cl.setTelefono(rs.getString(12));
					cl.setIndirizzo(rs.getString(13) + "," + rs.getString(14) + ","
							+ rs.getString(15) + "," + rs.getString(16));
					credito.setCliente(cl);
					if (AllTimerScadenza.getInstance().getTimerScadenza(credito) == null) {
						crediti.add(credito);
					}	
				}
				
			}
			rs.close();
			stmt.close();
		} catch (Exception e) {
			System.err.println("read(): failed to retrieve entries: " + e.getMessage());
			e.printStackTrace();
		} finally {
			Db2DAOFactoryCredito.closeConnection(conn);
		}
		return crediti;
//		return null;
	}

	@Override
	public Crediti readAll() {
		LogController logController = new LogController();
		String[] operazione = { LocalDateTime.now().toString(), " - read all Crediti - ",
				this.getClass().getSimpleName() };
		logController.printLogOperazione(operazione);
		String[] messaggio = { LocalDateTime.now().toString(), " - read all Crediti - ", "ricezione - ",
				this.getClass().getSimpleName() };
		logController.printLogMessaggio(messaggio);
		Crediti crediti = new Crediti();
		Connection conn = Db2DAOFactoryCredito.createConnection();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(read_all);
			while (rs.next()) {
				Credito credito = (rs.getInt(ID_BONIFICO_RICONCILIATO) == 0) ? new CreditoNonRiconciliato()
						: new CreditoRiconciliato();
				Cliente cl = new Cliente();
				credito.setId(rs.getInt(1));
				java.sql.Date d = rs.getDate(2);
				credito.setDataStipula(d.toLocalDate());
				credito.setImporto(rs.getDouble(3));
				credito.setCausale(rs.getString(4)); // -----> E' CAUSALE!!!
				cl.setId(rs.getInt(7));
				cl.setCodiceFiscale(rs.getString(8));
				cl.setNome(rs.getString(9));
				cl.setCognome(rs.getString(10));
				cl.setDataNascita(rs.getDate(11).toLocalDate());
				cl.setTelefono(rs.getString(12));
				cl.setIndirizzo(
						rs.getString(13) + "," + rs.getString(14)
								+ "," + rs.getString(15) + ","
								+ rs.getString(16));
				credito.setCliente(cl);
				crediti.add(credito);
			}
			rs.close();
			stmt.close();
		} catch (Exception e) {
			System.err.println("read(): failed to retrieve entries: " + e.getMessage());
			e.printStackTrace();
		} finally {
			Db2DAOFactoryCredito.closeConnection(conn);
		}
		return crediti;
	}

	@Override
	public Crediti readCreditiNonRiconciliati() {
		LogController logController = new LogController();
		String[] operazione = { LocalDateTime.now().toString(), " - read Crediti Non Riconciliati - ",
				this.getClass().getSimpleName() };
		logController.printLogOperazione(operazione);
		String[] messaggio = { LocalDateTime.now().toString(), " - read Crediti non Riconciliati - ", "ricezione - ",
				this.getClass().getSimpleName() };
		logController.printLogMessaggio(messaggio);
		Crediti crediti = new Crediti();
		Connection conn = Db2DAOFactoryCredito.createConnection();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(read_non_riconciliati);
			while (rs.next()) {
				Credito credito = new CreditoNonRiconciliato();
				Cliente cl = new Cliente();
				credito.setId(rs.getInt(1));
				java.sql.Date d = rs.getDate(2);
				credito.setDataStipula(d.toLocalDate());
				credito.setImporto(rs.getDouble(3));
				credito.setCausale(rs.getString(4)); // -----> E' CAUSALE!!!
				cl.setId(rs.getInt(7));
				cl.setCodiceFiscale(rs.getString(8));
				cl.setNome(rs.getString(9));
				cl.setCognome(rs.getString(10));
				cl.setDataNascita(rs.getDate(11).toLocalDate());
				cl.setTelefono(rs.getString(12));
				cl.setIndirizzo(
						rs.getString(13) + "," + rs.getString(14) + "," + rs.getString(15) + "," + rs.getString(16));
				credito.setCliente(cl);
				crediti.add(credito);
			}
			rs.close();
			stmt.close();
		} catch (Exception e) {
			System.err.println("read(): failed to retrieve entries: " + e.getMessage() + "\n\nread_all: " + read_non_riconciliati);
			e.printStackTrace();
		} finally {
			Db2DAOFactoryCredito.closeConnection(conn);
		}
		return crediti;
	}

}
