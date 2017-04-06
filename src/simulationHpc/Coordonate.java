package simulationHpc;

public class Coordonate {
	
	Long line;
	
	Integer valeur;
	
	public Long getLine() {
		return line;
	}

	public Integer getValeur() {
		return valeur;
	}

	public void setValeur(Integer valeur) {
		this.valeur = valeur;
	}

	public void setLine(Long line) {
		this.line = line;
	}

	public Long getColumn() {
		return column;
	}

	public void setColumn(Long column) {
		this.column = column;
	}

	Long column;
	
	public Coordonate()
	{
		//Constructeur
	}
	public Coordonate(Long line, Long column)
	{
		if(line != null)
		{
			this.setLine(line);
		}
		if(column != null)
		{
			this.setColumn(column);
		}
	}
}
