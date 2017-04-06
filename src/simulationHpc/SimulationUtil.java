package simulationHpc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
/**
 * Classe utilitaire permettant de r�aliser les diff�rentes �tapes de la simulation de l'�pid�mie.
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
	 * @param matrice : Repr�sente les individus une ligne pas individu
	 */
	public static void startInfection(Organization orga, Double percentInfection, Double percentVaccination, Double percentFirstCured)
	{
		Double nbIndividus = Double.valueOf(orga.getMatrice().getCoteMatrice().toString())*(percentInfection/100);
		
//		System.out.println("Pour infecter " + percentInfection + "% de la population "
//				+"il faudra infecter " + nbIndividus.intValue() + " individu(s)");
		//On commence � infecter al�atoirement
		List<Human> listHumans = new ArrayList<Human>(orga.getListHumans().values()); 
		Collections.shuffle(listHumans);
//		StringBuilder sb = new StringBuilder("Les humains suivants ont �t� infect�s : ");
		for(int i = 0; i < nbIndividus.intValue(); i++)
		{
			Human human = listHumans.get(i);
			orga.getListInfected().add(human);
//			sb.append(" - "+human.getId());
		}
//		System.out.println(sb);
		
		//Soin des premiers infect�s
		if(percentFirstCured != 0.00)
		{
			Double nbIndividusInfecteVaccine = nbIndividus*(percentFirstCured/100);
			List<Human> listInfected = orga.getListInfected();
			Collections.shuffle(listInfected);
//			sb = new StringBuilder("Les infect�s suivant ont �t� vaccin�s : ");
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
			//R�aliser la vaccination
			//Attention si on vaccine un humain infect�, il ne l'est plus (il doit sortir de la liste
//			sb  = new StringBuilder("Les humains suivants ont �t� vaccin�s (soign�s s'ils �taient infect�s) : ");
			//Nous permet de g�rer le cas o� on veut vacciner les meilleurs gens
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
			//S'il s'est auto soign� � cette round alors on le stock pour l'enlever de la liste des infect�s
			//On ne remove rien d'une liste qu'on it�re je vous le rappelle
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
			//On v�rifie son nombre de voisins
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
		//Redondance parce que c'est la taille de la liste en vrai mais �a me permet de v�rifier
		orga.setNombreInfectes(orga.getNombreInfectes()+nbInfectesSupp-nbSoignesSupp);
		return orga.listInfected.size();
	}
	
	public static void reInitSimulation(Organization orga, ChartDrawer drawer)
	{
		//On clear les liste et on remet le nombre d'infect�s � 0.
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
