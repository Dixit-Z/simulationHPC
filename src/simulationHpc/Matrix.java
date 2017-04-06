package simulationHpc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Matrix {
	
	//Liste des coordonnées non null dans matrice
	private List<Coordonate> listCoordNonNull;

	// Valeur pour chaque ligne de son équiprobabilité
	//<IdLigne, NbDeLienVersColonne>
	private HashMap<Long, Long> probabiliteParLigne;
	
	//HashMap pour chaque ligne de toutes les possibilités de transmission
	//<IdLigne, [idColonne1, idColonne2, idColonne5]>
	private HashMap<Long, List<Long>> listeLiensIndividus;
	
	private List<PairElement> vecteurInfectes;

	//Taille n * n de la matrice
	private Long tailleMatrice;

	//Nombre d'individu (côté de la matrice)
	private Long coteMatrice;
	
	//Nombre de valeurs non null dans la mtrice.
	private Long nombreValeurNonNull;

	private List<Long> firstVector;
	
	public List<Long> getFirstVector() {
		return firstVector;
	}

	public void setFirstVector(List<Long> firstVector) {
		this.firstVector = firstVector;
	}

	public HashMap<Long, Long> getProbabiliteParLigne() {
		return probabiliteParLigne;
	}

	public void setProbabiliteParLigne(HashMap<Long, Long> probabiliteParLigne) {
		this.probabiliteParLigne = probabiliteParLigne;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private Long id;

	// Constructeur de l'objet java matrix contenant les infos utiles de la
	// matrices.
	public Matrix() {
		this.listCoordNonNull = new ArrayList<>();
		this.vecteurInfectes = new ArrayList<>();
		this.probabiliteParLigne = new HashMap<>();
		this.listeLiensIndividus = new HashMap<>();
	}

	public Long getCoteMatrice() {
		return coteMatrice;
	}

	public void setCoteMatrice(Long coteMatrice) {
		this.coteMatrice = coteMatrice;
	}

	public Long getTailleMatrice() {
		return tailleMatrice;
	}

	public void setTailleMatrice(Long tailleMatrice) {
		this.tailleMatrice = tailleMatrice;
	}

	public Long getNombreValeurNonNull() {
		return nombreValeurNonNull;
	}

	public void setNombreValeurNonNull(Long nombreValeurNonNull) {
		this.nombreValeurNonNull = nombreValeurNonNull;
	}

	public List<Coordonate> getListCoordNonNull() {
		return listCoordNonNull;
	}

	public void setListCoordNonNull(List<Coordonate> listCoordNonNull) {
		this.listCoordNonNull = listCoordNonNull;
	}
	
	public void afficherData()
	{
		//Afficher les données qui nous intéresse
		for(Map.Entry<Long, Long> entry : probabiliteParLigne.entrySet())
		{
			Float probaReelle = 1/Float.valueOf(entry.getValue().toString());
			System.out.println("Pour la ligne : " + entry.getKey().toString() + " - " + probaReelle.toString() + "( 1/" + entry.getValue().toString() + " )");
			System.out.println("Nombre de contact : " + listeLiensIndividus.get(entry.getKey()).size());
		}
	}

	public HashMap<Long, List<Long>> getListeLiensIndividus() {
		return listeLiensIndividus;
	}

	public void setListeLiensIndividus(HashMap<Long, List<Long>> listeLiensIndividus) {
		this.listeLiensIndividus = listeLiensIndividus;
	}

	public List<PairElement> getVecteurInfectes() {
		return vecteurInfectes;
	}

	public void setVecteurInfectes(List<PairElement> vecteurInfectes) {
		this.vecteurInfectes = vecteurInfectes;
	}

}
