package simulationHpc;

public class PairElement {
	
	public PairElement(Long identifiantHuman, Integer nbVoisins)
	{
		this.nbVoisins = nbVoisins;
		this.identifiantHuman = identifiantHuman;
	}
	
	Integer nbVoisins;
	
	Long identifiantHuman;

	public Integer getNbVoisins() {
		return nbVoisins;
	}

	public void setNbVoisins(Integer nbVoisins) {
		this.nbVoisins = nbVoisins;
	}

	public Long getIndentifiantHuman() {
		return identifiantHuman;
	}

	public void setIndentifiantHuman(Long indentifiantHuman) {
		this.identifiantHuman = indentifiantHuman;
	}
	
	

}
