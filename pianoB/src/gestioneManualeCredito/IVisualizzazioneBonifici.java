package gestioneManualeCredito;

import dominioBonifico.*;
import java.util.*;

public interface IVisualizzazioneBonifici {
	public HashSet<Bonifico> visualizzaTuttiBonifici();
	
	public BonificiRiconciliati visualizzaBonificiRiconciliati();
	
	public BonificiNonRiconciliati visualizzaBonificiNonRiconciliati();
}
