package autenticazione;

public interface IAutenticazione {
	public boolean verificaCredenziali(String username, String hash);
}
