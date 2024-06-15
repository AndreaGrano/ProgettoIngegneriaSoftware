package daoCreditoDB2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.*;

import daoCredito.ClienteDAO;
import dominioCredito.Blacklist;
import dominioCredito.Cliente;
import log.LogController;

public class Db2ClienteDAO implements ClienteDAO {

	// === Costanti letterali per non sbagliarsi a scrivere !!!
	// ============================
	//
	// !!! ATTENZIONE !!!
	//
	// CON L'USO DI ID AUTO INCREMENTALI, VENGONO INSERITE TUPLE SENZA ID
	// MA POI DOPO LA PERSISTENZA, NEL BEAN VA AGGIORNATO L'ID!!!

	static final String TABLE = "clienti";

	// -------------------------------------------------------------------------------------

	static final String ID = "id";
	static final String CF = "cf";
	static final String NOME = "nome";
	static final String COGNOME = "cognome";
	static final String DATA_NASCITA = "data_nascita";
	static final String TELEFONO = "telefono";
	static final String INDIRIZZO_CIVICO = "indirizzo_civico";
	static final String INDIRIZZO_VIA = "indirizzo_via";
	static final String INDIRIZZO_COMUNE = "indirizzo_comune";
	static final String INDIRIZZO_PROVINCIA = "indirizzo_provincia";
	static final String IN_BLACKLIST = "in_blacklist";

	// == STATEMENT SQL
	// ====================================================================

	// INSERT INTO table ( name,description, ...) VALUES ( ?,?, ... );
	static final String create = "INSERT " + "INTO " + TABLE + " ( " + CF + ", " + NOME + ", " + COGNOME + ", "
			+ DATA_NASCITA + ", " + TELEFONO + ", " + INDIRIZZO_CIVICO + ", " + INDIRIZZO_VIA + ", " + INDIRIZZO_COMUNE
			+ ", " + INDIRIZZO_PROVINCIA + ", " + IN_BLACKLIST + " " + ") " + "VALUES (?,?,?,?,?,?,?,?,?,?) ";

	// SELECT * FROM table WHERE idcolumn = ?;
	static String read_by_id = "SELECT * " + "FROM " + TABLE + " " + "WHERE " + ID + " = ? ";

	static String read_by_cf = "SELECT * " + "FROM " + TABLE + " " + "WHERE " + CF + " = ? ";

	// DELETE FROM table WHERE idcolumn = ?;
	static String delete = "DELETE " + "FROM " + TABLE + " " + "WHERE " + ID + " = ? ";

	// UPDATE table SET xxxcolumn = ?, ... WHERE idcolumn = ?;
	static String update = "UPDATE " + TABLE + " " + "SET " + CF + " = ?, " + NOME + " = ?, " + COGNOME + " = ?, "
			+ DATA_NASCITA + " = ?, " + TELEFONO + " = ?, " + INDIRIZZO_CIVICO + " = ?, " + INDIRIZZO_VIA + " = ?, "
			+ INDIRIZZO_COMUNE + " = ?, " + INDIRIZZO_PROVINCIA + " = ? " + "WHERE " + ID + " = ? ";

//	static String update = "UPDATE " + TABLE + " " + "SET " + CF + " = ?, " + NOME + " = ?, " + COGNOME + " = ?, "
//			+ DATA_NASCITA + " = ?, " + TELEFONO + " = ?, " + INDIRIZZO_CIVICO + " = ?, " + INDIRIZZO_VIA + " = ?, "
//			+ INDIRIZZO_COMUNE + " = ?, " + INDIRIZZO_PROVINCIA + " = ?, " + IN_BLACKLIST + " = ? " + "WHERE " + ID
//			+ " = ? ";

	static String update_blacklist = "UPDATE " + TABLE + " " + "SET " + IN_BLACKLIST + " = ? " + "WHERE " + ID
			+ " = ? ";

	// SELECT * FROM table;
	static String read_all = "SELECT * " + "FROM " + TABLE + " ";

	static String read_clienti_in_blacklist = read_all + "WHERE " + IN_BLACKLIST + " = 'Y' ";

	// -------------------------------------------------------------------------------------

	// CREATE entrytable ( code INT NOT NULL PRIMARY KEY, ... );
	static String create_table = "CREATE " + "TABLE " + TABLE + " ( " + ID
			+ " INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1), " + CF
			+ " VARCHAR(20) NOT NULL UNIQUE, " + NOME + " VARCHAR(40) NOT NULL, " + COGNOME + " VARCHAR(40) NOT NULL, "
			+ DATA_NASCITA + " DATE NOT NULL, " + TELEFONO + " VARCHAR(20) NOT NULL, " + INDIRIZZO_CIVICO
			+ " VARCHAR(40) NOT NULL, " + INDIRIZZO_VIA + " VARCHAR(40) NOT NULL, " + INDIRIZZO_COMUNE
			+ " VARCHAR(40) NOT NULL, " + INDIRIZZO_PROVINCIA + " VARCHAR(40) NOT NULL, " + IN_BLACKLIST
			+ " CHAR(1) NOT NULL CHECK ( " + IN_BLACKLIST + " = 'Y' OR " + IN_BLACKLIST + " = 'N' ) " + ") ";

	static String drop = "DROP " + "TABLE " + TABLE + " ";

	// === METODI DAO
	// =========================================================================

	// ---------------
	// - 2 PROBLEMI: -
	// ---------------
	// 1) come gestire la blacklist
	// 2) come gestire l'indirizzo

	@Override
	public void create(Cliente cliente) {
		LogController logController = new LogController();
		String[] operazione = { LocalDateTime.now().toString(), " - create (cliente) - ",
				this.getClass().getSimpleName() };
		logController.printLogOperazione(operazione);
		Connection conn = Db2DAOFactoryCredito.createConnection();
		if (cliente == null) {
			System.err.println("create(): failed to insert a null entry");
			return;
		}
		String[] messaggio = { LocalDateTime.now().toString(), " - insert tupla del cliente: " + cliente.getId(),
				" - invio - ", this.getClass().getSimpleName() };
		logController.printLogMessaggio(messaggio);
		try {
			PreparedStatement prep_stmt = conn.prepareStatement(create);
			prep_stmt.clearParameters();
			String[] indirizzi = cliente.getIndirizzo().split(",");
			prep_stmt.setString(1, cliente.getCodiceFiscale());
			prep_stmt.setString(2, cliente.getNome());
			prep_stmt.setString(3, cliente.getCognome());
			prep_stmt.setDate(4, java.sql.Date.valueOf(cliente.getDataNascita()));
			prep_stmt.setString(5, cliente.getTelefono());
			prep_stmt.setString(6, indirizzi[0]); // civico
			prep_stmt.setString(7, indirizzi[1]); // via
			prep_stmt.setString(8, indirizzi[2]); // comune
			prep_stmt.setString(9, indirizzi[3]); // provincia
			prep_stmt.setString(10, "N"); // inizialmente non in blacklist
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
	public Cliente read(int id) {
		LogController logController = new LogController();
		String[] operazione = { LocalDateTime.now().toString(), " - read cliente (id) - ",
				this.getClass().getSimpleName() };
		logController.printLogOperazione(operazione);
		Cliente cl = null;
		if (id < 0) {
			System.err.println("read(): cannot read an entry with a negative id");
			return cl;
		}
		String[] messaggio = { LocalDateTime.now().toString(), " - read tupla del cliente: " + id, " - ricezione - ",
				this.getClass().getSimpleName() };
		logController.printLogMessaggio(messaggio);
		Connection conn = Db2DAOFactoryCredito.createConnection();
		try {
			PreparedStatement prep_stmt = conn.prepareStatement(read_by_id);
			prep_stmt.clearParameters();
			prep_stmt.setInt(1, id);
			ResultSet rs = prep_stmt.executeQuery();
			if (rs.next()) {
				Cliente entry = new Cliente();
				String indirizzo = "";
				entry.setId(rs.getInt(ID));
				entry.setCodiceFiscale(rs.getString(CF));
				entry.setNome(rs.getString(NOME));
				entry.setCognome(rs.getString(COGNOME));
				java.sql.Date d = rs.getDate(DATA_NASCITA);
				entry.setDataNascita(d.toLocalDate());
				entry.setTelefono(rs.getString(TELEFONO));
				indirizzo = rs.getString(INDIRIZZO_CIVICO) + "," + rs.getString(INDIRIZZO_VIA) + ","
						+ rs.getString(INDIRIZZO_COMUNE) + "," + rs.getString(INDIRIZZO_PROVINCIA);
				entry.setIndirizzo(indirizzo);
				// blacklist chissenefrega perche' leggo solo un cliente
				cl = entry;
			}
			rs.close();
			prep_stmt.close();
		} catch (Exception e) {
			System.err.println("read(): failed to retrieve entry with id = " + id + ": " + e.getMessage());
			e.printStackTrace();
		} finally {
			Db2DAOFactoryCredito.closeConnection(conn);
		}
		return cl;
	}

	@Override
	public Cliente readByCF(String CodF) {
		LogController logController = new LogController();
		String[] operazione = { LocalDateTime.now().toString(), " - read cliente (CF) - ",
				this.getClass().getSimpleName() };
		logController.printLogOperazione(operazione);
		Cliente cl = null;
		if (CodF == null) {
			System.err.println("read(): cannot read an entry with a null CF");
			return cl;
		}
		String[] messaggio = { LocalDateTime.now().toString(), " - read tupla del cliente: " + CodF, " - ricezione - ",
				this.getClass().getSimpleName() };
		logController.printLogMessaggio(messaggio);
		Connection conn = Db2DAOFactoryCredito.createConnection();
		try {
			PreparedStatement prep_stmt = conn.prepareStatement(read_by_cf);
			prep_stmt.clearParameters();
			prep_stmt.setString(1, CodF);
			ResultSet rs = prep_stmt.executeQuery();
			if (rs.next()) {
				Cliente entry = new Cliente();
				String indirizzo = "";
				entry.setId(rs.getInt(ID));
				entry.setCodiceFiscale(rs.getString(CF));
				entry.setNome(rs.getString(NOME));
				entry.setCognome(rs.getString(COGNOME));
				java.sql.Date d = rs.getDate(DATA_NASCITA);
				entry.setDataNascita(d.toLocalDate());
				entry.setTelefono(rs.getString(TELEFONO));
				indirizzo = rs.getString(INDIRIZZO_CIVICO) + "," + rs.getString(INDIRIZZO_VIA) + ","
						+ rs.getString(INDIRIZZO_COMUNE) + "," + rs.getString(INDIRIZZO_PROVINCIA);
				entry.setIndirizzo(indirizzo);
				// blacklist chissenefrega perche' leggo solo un cliente
				cl = entry;
			}
			rs.close();
			prep_stmt.close();
		} catch (Exception e) {
			System.err.println("readByCF(): failed to retrieve entry with CF = " + CodF + ": " + e.getMessage());
			e.printStackTrace();
		} finally {
			Db2DAOFactoryCredito.closeConnection(conn);
		}
		return cl;
	}
	
	@Override
	public boolean update(Cliente cliente) {
		// L'ID del cliente va settato prima, leggendo lo stesso cliente da DB!
		LogController logController = new LogController();
		String[] operazione = { LocalDateTime.now().toString(), " - update cliente - ",
				this.getClass().getSimpleName() };
		logController.printLogOperazione(operazione);
		boolean res = true;
		if (cliente == null) {
			res = false;
			System.err.println("update(): cannot update an entry with a null bonifico");
			return res;
		}
		String[] messaggio = { LocalDateTime.now().toString(), " - update del cliente: " + cliente.getId(),
				" - invio - ", this.getClass().getSimpleName() };
		logController.printLogMessaggio(messaggio);
		Connection conn = Db2DAOFactoryCredito.createConnection();
		try {
			String[] indirizzi = cliente.getIndirizzo().split(",");
			PreparedStatement prep_stmt = conn.prepareStatement(update);
			prep_stmt.clearParameters();
			prep_stmt.setString(1, cliente.getCodiceFiscale());
			prep_stmt.setString(2, cliente.getNome());
			prep_stmt.setString(3, cliente.getCognome());
			prep_stmt.setDate(4, java.sql.Date.valueOf(cliente.getDataNascita()));
			prep_stmt.setString(5, cliente.getTelefono());
			prep_stmt.setString(6, indirizzi[0]); // civico
			prep_stmt.setString(7, indirizzi[1]); // via
			prep_stmt.setString(8, indirizzi[2]); // comune
			prep_stmt.setString(9, indirizzi[3]); // provincia
			prep_stmt.setInt(10, cliente.getId());
			prep_stmt.executeUpdate();
			prep_stmt.close();
		} catch (Exception e) {
			res = false;
			System.err.println("update(): failed to retrieve entry with id = " + cliente.getId() + ": " + e.getMessage()
					+ "\n\n" + " Hai settato l'id del cliente passato come parametro, oppure e' vuoto? " + "\n");
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
		String[] operazione = { LocalDateTime.now().toString(), " - delete cliente (id)  - ",
				this.getClass().getSimpleName() };
		logController.printLogOperazione(operazione);
		if (id < 0) {
			System.err.println("delete(): cannot delete an entry with an invalid id ");
			return false;
		}
		String[] messaggio = { LocalDateTime.now().toString(), " - delete del cliente: " + id, " - invio - ",
				this.getClass().getSimpleName() };
		logController.printLogMessaggio(messaggio);
		Connection conn = Db2DAOFactoryCredito.createConnection();
		try {
			PreparedStatement prep_stmt = conn.prepareStatement(Db2ClienteDAO.delete);
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
		String[] operazione = { LocalDateTime.now().toString(), " - create Table Clienti - ",
				this.getClass().getSimpleName() };
		logController.printLogOperazione(operazione);
		String[] messaggio = { LocalDateTime.now().toString(), " - create Table Clienti - ", "invio - ",
				this.getClass().getSimpleName() };
		logController.printLogMessaggio(messaggio);
		boolean res = true;
		Connection conn = Db2DAOFactoryCredito.createConnection();
		try {
			Statement stmt = conn.createStatement();
			stmt.execute(Db2ClienteDAO.create_table);
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
		String[] operazione = { LocalDateTime.now().toString(), " - drop Table Clienti - ",
				this.getClass().getSimpleName() };
		logController.printLogOperazione(operazione);
		String[] messaggio = { LocalDateTime.now().toString(), " - drop Table Clienti - ", "invio - ",
				this.getClass().getSimpleName() };
		logController.printLogMessaggio(messaggio);
		boolean res = true;
		Connection conn = Db2DAOFactoryCredito.createConnection();
		try {
			Statement stmt = conn.createStatement();
			stmt.execute(Db2ClienteDAO.drop);
			stmt.close();
		} catch (Exception e) {
			System.err.println("dropTable(): failed to drop table '" + TABLE + "': " + e.getMessage());
			res = false;
		} finally {
			Db2DAOFactoryCredito.closeConnection(conn);
		}
		return res;
	}

	

	@Override
	public Blacklist readClientiInBlacklist() { // return blacklist
		LogController logController = new LogController();
		String[] operazione = { LocalDateTime.now().toString(), " - read Blacklist - ",
				this.getClass().getSimpleName() };
		logController.printLogOperazione(operazione);
		String[] messaggio = { LocalDateTime.now().toString(), " - read clienti in blacklist - ", "ricezione",
				this.getClass().getSimpleName() };
		logController.printLogMessaggio(messaggio);
		Blacklist clienti = new Blacklist();
		Connection conn = Db2DAOFactoryCredito.createConnection();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(read_clienti_in_blacklist);
			while (rs.next()) {
				Cliente cliente = new Cliente();
				String indirizzo = "";
				cliente.setId(rs.getInt(ID));
				cliente.setCodiceFiscale(rs.getString(CF));
				cliente.setNome(rs.getString(NOME));
				cliente.setCognome(rs.getString(COGNOME));
				cliente.setDataNascita(rs.getDate(DATA_NASCITA).toLocalDate());
				cliente.setTelefono(rs.getString(TELEFONO));
				indirizzo = rs.getString(INDIRIZZO_CIVICO) + "," + rs.getString(INDIRIZZO_VIA) + ","
						+ rs.getString(INDIRIZZO_COMUNE) + "," + rs.getString(INDIRIZZO_PROVINCIA);
				cliente.setIndirizzo(indirizzo);
				// blacklist chissenefrega perche' leggo solo un cliente
				clienti.add(cliente);
			}
			rs.close();
			stmt.close();
		} catch (Exception e) {
			System.err.println(
					"readClientiInBlacklist(): failed to retrieve entry with CF = " + CF + ": " + e.getMessage());
			e.printStackTrace();
		} finally {
			Db2DAOFactoryCredito.closeConnection(conn);
		}
		return clienti;
	}

	@Override
	public Set<Cliente> readAll() { // return hashSet di clienti
		LogController logController = new LogController();
		String[] operazione = { LocalDateTime.now().toString(), " - read all clienti - ",
				this.getClass().getSimpleName() };
		logController.printLogOperazione(operazione);
		String[] messaggio = { LocalDateTime.now().toString(), " - read tutti i clienti - ", "ricezione - ",
				this.getClass().getSimpleName() };
		logController.printLogMessaggio(messaggio);
		Set<Cliente> clienti = new HashSet<Cliente>();
		Connection conn = Db2DAOFactoryCredito.createConnection();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(read_all);
			while (rs.next()) {
				Cliente cliente = new Cliente();
				String indirizzo = "";
				cliente.setId(rs.getInt(ID));
				cliente.setCodiceFiscale(rs.getString(CF));
				cliente.setNome(rs.getString(NOME));
				cliente.setCognome(rs.getString(COGNOME));
				cliente.setDataNascita(rs.getDate(DATA_NASCITA).toLocalDate());
				cliente.setTelefono(rs.getString(TELEFONO));
				indirizzo = rs.getString(INDIRIZZO_CIVICO) + "," + rs.getString(INDIRIZZO_VIA) + ","
						+ rs.getString(INDIRIZZO_COMUNE) + "," + rs.getString(INDIRIZZO_PROVINCIA);
				cliente.setIndirizzo(indirizzo);
				// blacklist chissenefrega perche' leggo solo un cliente
				clienti.add(cliente);
			}
			rs.close();
			stmt.close();
		} catch (Exception e) {
			System.err.println(
					"readClientiInBlacklist(): failed to retrieve entry with CF = " + CF + ": " + e.getMessage());
			e.printStackTrace();
		} finally {
			Db2DAOFactoryCredito.closeConnection(conn);
		}
		return clienti;
	}

	@Override
	public boolean inserisciInBlacklist(Cliente cliente) { // update inBlacklist = 'Y'
		LogController logController = new LogController();
		String[] operazione = { LocalDateTime.now().toString(), " - inserisci in blacklist (cliente) - ",
				this.getClass().getSimpleName() };
		logController.printLogOperazione(operazione);
		boolean res = true;
		if (cliente == null) {
			res = false;
			System.err.println("update(): cannot update an entry with a null bonifico");
			return res;
		}
		String[] messaggio = { LocalDateTime.now().toString(),
				" - inserisci in blacklist il cliente: " + cliente.getId(), " - invio - ",
				this.getClass().getSimpleName() };
		logController.printLogMessaggio(messaggio);
		Connection conn = Db2DAOFactoryCredito.createConnection();
		try {
			PreparedStatement prep_stmt = conn.prepareStatement(update_blacklist);
			prep_stmt.clearParameters();
			prep_stmt.setString(1, "Y");
			prep_stmt.setInt(2, cliente.getId());
			prep_stmt.executeUpdate();
			prep_stmt.close();
		} catch (Exception e) {
			res = false;
			System.err.println("inserisciInBlacklist(): failed to retrieve entry with id = " + cliente.getId() + ": "
					+ e.getMessage() + "\n\n" + " Hai settato l'id del cliente passato come parametro, oppure e' vuoto? " + "\n");
			e.printStackTrace();
			return res;
		} finally {
			Db2DAOFactoryCredito.closeConnection(conn);
		}

		return res;
	}

	@Override
	public boolean rimuoviDaBlacklist(Cliente cliente) { // update inBlacklist = 'N'
		LogController logController = new LogController();
		String[] operazione = { LocalDateTime.now().toString(), " - rimuovi da blacklist (cliente) - ",
				this.getClass().getSimpleName() };
		logController.printLogOperazione(operazione);
		boolean res = true;
		if (cliente == null) {
			res = false;
			System.err.println("update(): cannot update an entry with a null bonifico");
			return res;
		}
		String[] messaggio = { LocalDateTime.now().toString(),
				" - rimuovi dalla blacklist il cliente: " + cliente.getId(), " - invio - ",
				this.getClass().getSimpleName() };
		logController.printLogMessaggio(messaggio);
		Connection conn = Db2DAOFactoryCredito.createConnection();
		try {
			PreparedStatement prep_stmt = conn.prepareStatement(update_blacklist);
			prep_stmt.clearParameters();
			prep_stmt.setString(1, "N");
			prep_stmt.setInt(2, cliente.getId());
			prep_stmt.executeUpdate();
			prep_stmt.close();
		} catch (Exception e) {
			res = false;
			System.err.println("rimuoviDaBlacklist(): failed to retrieve entry with id = " + cliente.getId() + ": "
					+ e.getMessage() + "\n\n" + " Hai settato l'id del cliente passato come parametro, oppure e' vuoto? " + "\n");
			e.printStackTrace();
			return res;
		} finally {
			Db2DAOFactoryCredito.closeConnection(conn);
		}

		return res;
	}

}
