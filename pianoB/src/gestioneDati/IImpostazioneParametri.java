package gestioneDati;

public interface IImpostazioneParametri {
	public void impostaIntervalloRitardoCredito(int intervallo);
	
	public void impostaIntervalloRitardoBlacklist(int intervallo);
	
	public void impostaPercentualeMulta(double percentualeMulta);
	
	public void impostaFormatoCausale(String formatoCausale);
}
