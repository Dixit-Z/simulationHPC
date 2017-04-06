package simulationHpc;

import java.util.ArrayList;
import java.util.List;

public class Human {
	private int delaiSoin;
	private int id;
	@Deprecated
	private Boolean infected;
	@Deprecated
	private Boolean vaccinated;
	//Liste des id des colonnes (Donc id des humains) associées à cet humain
	private List<Long> listIdContact;
	//Attribut d'humain car on peut changer pour chaque humain selon l'âge ou ses activités par exemple
	private Double autoMedicChances;

	public Human(int id)
	{
		this.id = id;
		this.infected = false;
		this.vaccinated = false;
		this.setDelaiSoin(10);
		this.setListIdContact(new ArrayList<Long>());
	}
	
	public Human(int id, List<Long> list)
	{
		this.id = id;
		this.autoMedicChances = 2.00;
		this.setDelaiSoin(50);
		this.infected = false;
		this.vaccinated = false;
		if(list != null)
		{
			this.setListIdContact(list);
		}
		else
		{
			this.setListIdContact(new ArrayList<>());
		}
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Boolean getInfected() {
		return infected;
	}

	public void setInfected(Boolean infected) {
		if(infected && this.vaccinated)
		{
			this.infected = false;
		}
		this.infected = infected;
	}

	public Boolean getVaccinated() {
		return vaccinated;
	}

	public void setVaccinated(Boolean vaccinated) {
		if(vaccinated)
		{
			this.infected = false;
		}
		this.vaccinated = vaccinated;
	}

	public List<Long> getListIdContact() {
		return listIdContact;
	}

	public void setListIdContact(List<Long> listIdContact) {
		this.listIdContact = listIdContact;
	}

	public void afficherDataHuman()
	{
		System.out.println("Je suis l'humain : " + this.getId());
		StringBuilder sb = new StringBuilder("Mes contacts sont les humains :");
		for(Long humanId : this.listIdContact)
		{
			sb.append(" "+humanId);
		}
		System.out.println(sb);
	}

	public Double getAutoMedicChances() {
		return autoMedicChances;
	}

	public void setAutoMedicChances(Double autoMedicChances) {
		this.autoMedicChances = autoMedicChances;
	}

	public int getDelaiSoin() {
		return delaiSoin;
	}

	public void setDelaiSoin(int delaiSoin) {
		this.delaiSoin = delaiSoin;
	}
}
