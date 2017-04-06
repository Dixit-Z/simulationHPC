package simulationHpc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
/**
 * Classe utilitaire permettant de réaliser les différentes étapes de la simulation de l'épidémie.
 * @author Bruno
 *
 */
public class SimulationUtil {
	
	private SimulationUtil()
	{
		//Constructeur explicite private car on ne l'instancie pas.
	}
	/**
	 * On lance l'infection sur 5 % de la population.
	 * @param matrice : Représente les individus une ligne pas individu
	 */
	public static void startInfection(Organization orga, Double percentInfection, Double percentVaccination, Double percentFirstCured)
	{
		Double nbIndividus = Double.valueOf(orga.getMatrice().getCoteMatrice().toString())*(percentInfection/100);
		
//		System.out.println("Pour infecter " + percentInfection + "% de la population "
//				+"il faudra infecter " + nbIndividus.intValue() + " individu(s)");
		//On commence à infecter aléatoirement
		List<Human> listHumans = new ArrayList<Human>(orga.getListHumans().values()); 
		Collections.shuffle(listHumans);
//		StringBuilder sb = new StringBuilder("Les humains suivants ont été infectés : ");
		for(int i = 0; i < nbIndividus.intValue(); i++)
		{
			Human human = listHumans.get(i);
			orga.getListInfected().add(human);
//			sb.append(" - "+human.getId());
		}
//		System.out.println(sb);
		
		//Soin des premiers infectés
		if(percentFirstCured != 0.00)
		{
			Double nbIndividusInfecteVaccine = nbIndividus*(percentFirstCured/100);
			List<Human> listInfected = orga.getListInfected();
			Collections.shuffle(listInfected);
//			sb = new StringBuilder("Les infectés suivant ont été vaccinés : ");
			for(int i=0; i < nbIndividusInfecteVaccine.intValue(); i++)
			{
				Human infected = listInfected.get(i);
				orga.getListVaccinated().add(infected);
				orga.getListInfected().remove(infected);
//				sb.append(" - "+infected.getId());
			}
//			System.out.println(sb);
		}
		
		if(percentVaccination != 0.00)
		{
			 nbIndividus = Double.valueOf(orga.getMatrice().getCoteMatrice().toString())*(percentVaccination/100);
			//Réaliser la vaccination
			//Attention si on vaccine un humain infecté, il ne l'est plus (il doit sortir de la liste
//			sb  = new StringBuilder("Les humains suivants ont été vaccinés (soignés s'ils étaient infectés) : ");
			//Nous permet de gérer le cas où on veut vacciner les meilleurs gens
			if(!orga.getMatrice().getVecteurInfectes().isEmpty())
			{
				for(int j = 0; j < nbIndividus.intValue(); j++)
				{
					Human humanForVaccination = orga.getListHumans().get(orga.getMatrice().getVecteurInfectes().get(j).getIndentifiantHuman().intValue());
					//Duplication de code mais bon
					if(orga.getListInfected().contains(humanForVaccination))
					{
						orga.getListInfected().remove(humanForVaccination);
					}
					orga.getListVaccinated().add(humanForVaccination);
//					sb.append(" - " + humanForVaccination.getId());
				}
			}
			else
			{
				Collections.shuffle(listHumans);
				for(int j = 0; j < nbIndividus.intValue(); j++)
				{
					Human humain = listHumans.get(j);
					if(orga.getListInfected().contains(humain))
					{
						orga.getListInfected().remove(humain);
					}
					orga.getListVaccinated().add(humain);
//					sb.append(" - " + humain.getId());
				}
			}
//			System.out.println(sb);
		}
		orga.setNombreInfectes(orga.getListInfected().size());
	}
	
	public static int nextStepInfection(Organization orga, Boolean autoMedic)
	{
		int nbInfectesSupp = 0;
		int nbSoignesSupp = 0;
		List<Human> newInfectedPeople = new ArrayList<>();
		List<Human> newCuredPeople = new ArrayList<>();
		for(Human humanInfected : orga.getListInfected())
		{
			//S'il s'est auto soigné à cette round alors on le stock pour l'enlever de la liste des infectés
			//On ne remove rien d'une liste qu'on itère je vous le rappelle
			if(autoMedic)
			{
				if(humanInfected.getDelaiSoin() > 0)
				{
					humanInfected.setDelaiSoin(humanInfected.getDelaiSoin()-1);
				}
				else if(Math.random() < humanInfected.getAutoMedicChances()/100.00)
				{
					orga.getListVaccinated().add(humanInfected);
					newCuredPeople.add(humanInfected);
					nbSoignesSupp++;
					continue;
				}
			}
			//On vérifie son nombre de voisins
//			System.out.println("En tant qu'infecte j'ai " + humanInfected.getListIdContact().size() + "voisins.");
			for(Long idHuman : humanInfected.getListIdContact())
			{
				Human neighbour = orga.getListHumans().get(idHuman.intValue());
				if(neighbour != null && !orga.getListVaccinated().contains(neighbour) && Math.random() < 0.20 && !orga.getListInfected().contains(neighbour) && !newInfectedPeople.contains(neighbour))
				{
					newInfectedPeople.add(neighbour);
					nbInfectesSupp++;
				}
			}
		}
		if(newInfectedPeople != null && !newInfectedPeople.isEmpty())
		{
			orga.getListInfected().addAll(newInfectedPeople);
		}
		if(newCuredPeople != null && !newCuredPeople.isEmpty())
		{
			orga.getListInfected().removeAll(newCuredPeople);
		}
		//Redondance parce que c'est la taille de la liste en vrai mais ça me permet de vérifier
		orga.setNombreInfectes(orga.getNombreInfectes()+nbInfectesSupp-nbSoignesSupp);
		return orga.listInfected.size();
	}
	
	public static void reInitSimulation(Organization orga, ChartDrawer drawer)
	{
		//On clear les liste et on remet le nombre d'infectés à 0.
		orga.setListInfected(new ArrayList<Human>());
		orga.setListVaccinated(new ArrayList<Human>());
		orga.getMatrice().setVecteurInfectes(new ArrayList<>());
		orga.setNombreInfectes(0);
		for(Map.Entry<Integer, Human> entry : orga.getListHumans().entrySet())
		{
			entry.getValue().setDelaiSoin(50);
		}
		drawer.setNbInfectes(new ArrayList<>());
	}

}
