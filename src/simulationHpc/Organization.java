package simulationHpc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Organization {
	Matrix matrice;
	List<Human> listInfected;
	List<Human> listVaccinated;
	HashMap<Integer, Human> listHumans;
	Integer nombreInfectes;
	
	public List<Human> getListInfected() {
		return listInfected;
	}

	public void setListInfected(List<Human> listInfected) {
		this.listInfected = listInfected;
	}

	public List<Human> getListVaccinated() {
		return listVaccinated;
	}

	public void setListVaccinated(List<Human> listVaccinated) {
		this.listVaccinated = listVaccinated;
	}
	
	public Integer getNombreInfectes() {
		return nombreInfectes;
	}

	public void setNombreInfectes(Integer nombreInfectes) {
		this.nombreInfectes = nombreInfectes;
	}

	public Matrix getMatrice() {
		return matrice;
	}

	public void setMatrice(Matrix matrice) {
		this.matrice = matrice;
	}

	public Organization(Matrix matrice) {
		this.matrice = matrice;
		this.listInfected = new ArrayList<>();
		this.listVaccinated = new ArrayList<>();
		this.listHumans = initializeMapHumans(this.matrice);
	}

	public HashMap<Integer, Human> getListHumans() {
		return listHumans;
	}

	public void setListHumans(HashMap<Integer, Human> listHumans) {
		this.listHumans = listHumans;
	}
	
	private HashMap<Integer, Human> initializeMapHumans(Matrix matrice) {
		HashMap<Integer, Human> listHumans = new HashMap<>();
		for (Map.Entry<Long, List<Long>> entry : matrice.getListeLiensIndividus().entrySet()) {
			//Quoi qu'il arrive on le met dans la liste avec ses voisins
			listHumans.put(entry.getKey().intValue(),new Human(entry.getKey().intValue(), entry.getValue()));
			for(Long idHumanLinked : entry.getValue())
			{
				//S'il est juste à droite et qu'il est pas déjà dans la liste on le met dans la liste d'humain
				if(!listHumans.containsKey(idHumanLinked))
				{
					listHumans.put(idHumanLinked.intValue(),new Human(entry.getKey().intValue(), null));
				}
			}
		}
		return listHumans;
	}
	
}
