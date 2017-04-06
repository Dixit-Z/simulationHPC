package simulationHpc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrganizationUtils {
	
	private OrganizationUtils()
	{
		//Private constructor
	}
	/**
	 * 
	 * @param orga
	 * @deprecated : Trouve les liens qui partent pas qui rentrent
	 * @return
	 */
	@Deprecated
	static public List<PairElement> trouverVecteurInfection(Organization orga)
	{
		List<PairElement> vecteurInfecte = new ArrayList<>();
		for(Map.Entry<Long, List<Long>> entry : orga.getMatrice().getListeLiensIndividus().entrySet())
		{
			vecteurInfecte.add(new PairElement(entry.getKey(), entry.getValue().size()));
		}
		Collections.sort(vecteurInfecte, new Comparator<PairElement>() {
			@Override
			public int compare(PairElement o1, PairElement o2) {
				return o2.getNbVoisins().compareTo(o1.getNbVoisins());
			}
		});
//		for(PairElement pair : vecteurInfecte)
//		{
//			System.out.println(pair.getIndentifiantHuman() + ":" + pair.getNbVoisins());
//		}
		return vecteurInfecte;
	}
	
	static public List<PairElement> trouverVecteurInfectionLienEntrant(Organization orga)
	{
		List<PairElement> vecteurInfecte = new ArrayList<>();
		HashMap<Long, Integer> nbLiensEntrants = new HashMap<>();
		for(Map.Entry<Long, List<Long>> entry : orga.getMatrice().getListeLiensIndividus().entrySet())
		{
			for(Long idHuman : entry.getValue())
			{
				if(nbLiensEntrants.get(idHuman) != null)
				{
					nbLiensEntrants.put(idHuman, nbLiensEntrants.get(idHuman)+1);
				}
				else
				{
					nbLiensEntrants.put(idHuman, 1);
				}
			}
		}
		for(Map.Entry<Long, Integer> entry : nbLiensEntrants.entrySet())
		{
			vecteurInfecte.add(new PairElement(entry.getKey(), entry.getValue()));
		}
		Collections.sort(vecteurInfecte, new Comparator<PairElement>() {
			@Override
			public int compare(PairElement o1, PairElement o2) {
				return o2.getNbVoisins().compareTo(o1.getNbVoisins());
			}
		});
//		for(PairElement pair : vecteurInfecte)
//		{
//			System.out.println(pair.getIndentifiantHuman() + ":" + pair.getNbVoisins());
//		}
		return vecteurInfecte;
	}
}
