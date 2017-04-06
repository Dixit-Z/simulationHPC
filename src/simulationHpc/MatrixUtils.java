package simulationHpc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe utilitaire permettant de calculer le vecteur propre et le reste de la ballade.
 * @author Bruno
 *
 */
public class MatrixUtils {

	private MatrixUtils()
	{
		//Private constructor so we don't call it.
	}

	static public void calculerPremierVecteur(Organization orga)
	{
		//On veut trouver P0 donc les lignes qui représentent les gens infectés
		orga.getMatrice().setFirstVector(new ArrayList<Long>(Collections.nCopies(orga.getMatrice().getCoteMatrice().intValue(), 0L)));
		for(Human human : orga.getListInfected())
		{
			orga.getMatrice().getFirstVector().set(human.getId()-1, 1L);
		}
		afficherVecteur(orga.getMatrice().getFirstVector());
	}

	static public void afficherVecteur(List<Long> vecteur)
	{
		StringBuilder sb = new StringBuilder("Vecteur : {");
		for(Long value : vecteur)
		{
			sb.append(value.toString()+",");
		}
		sb.append("}");
		System.out.println(sb);
	}

	static public void etatPromeneur(int etape, Matrix matrice)
	{
		Matrix matricePuissance = new Matrix();
		matricePuissance.setCoteMatrice(matrice.getCoteMatrice());
		HashMap<String, Double> listCoordValue = new HashMap<>();
		HashMap<String, Double> listCoordValueTemp = new HashMap<>();
		//Calcul de la matrice au carré
		for(Map.Entry<Long, List<Long>> ligne : matrice.getListeLiensIndividus().entrySet())
		{
			for(Long colonne : ligne.getValue())
			{
				for(Long k = 1L; k<matrice.getCoteMatrice()+1L; k=k+1L)
				{
					if(matrice.getListeLiensIndividus().get(colonne) != null && matrice.getListeLiensIndividus().get(colonne).contains(k))
					{
						if(listCoordValue.get(ligne.getKey().toString()+":"+k.toString()) != null)
						{
							Double newSum = listCoordValue.get(ligne.getKey().toString()+":"+k.toString());
							newSum +=1/((double) ((matrice.getProbabiliteParLigne().get(ligne.getKey())))*(double)((matrice.getProbabiliteParLigne().get(colonne))));
							listCoordValue.put(ligne.getKey().toString()+":"+k.toString(), newSum);

						}
						else
						{
							listCoordValue.put(ligne.getKey().toString()+":"+k.toString(),1/( (double) ((matrice.getProbabiliteParLigne().get(ligne.getKey())))*(double)((matrice.getProbabiliteParLigne().get(colonne)))));
						}

						if(matricePuissance.getListeLiensIndividus().get(ligne.getKey()) != null)
						{
							List<Long> list = new ArrayList<>();
							list.addAll(matricePuissance.getListeLiensIndividus().get(ligne.getKey()));
							list.add(k);
							matricePuissance.getListeLiensIndividus().put(ligne.getKey(), Arrays.asList(k));
						}
						else
						{
							matricePuissance.getListeLiensIndividus().put(ligne.getKey(), Arrays.asList(k));
						}
					}
				}
			}
		}
		for(int i=2; i<= etape;i++)
		{
			listCoordValueTemp.clear();
			for(Map.Entry<Long, List<Long>> ligne : matrice.getListeLiensIndividus().entrySet())
			{
				for(Long colonne : ligne.getValue())
				{
					for(Long k = 1L; k<matrice.getCoteMatrice()+1L; k=k+1L)
					{
						if(matrice.getListeLiensIndividus().get(colonne) != null && matrice.getListeLiensIndividus().get(colonne).contains(k))
						{
							if(listCoordValueTemp.get(ligne.getKey().toString()+":"+k.toString()) != null)
							{
								Double newSum = listCoordValueTemp.get(ligne.getKey().toString()+":"+k.toString());
								Double temp = (double)(listCoordValue.get(ligne.getKey().toString()+":"+k.toString())==null? 0.0 : listCoordValue.get(ligne.getKey().toString()+":"+k.toString()));
								newSum +=1/((double) ((matrice.getProbabiliteParLigne().get(ligne.getKey())))*temp);
								listCoordValueTemp.put(ligne.getKey().toString()+":"+k.toString(), newSum);

							}
							else
							{
								Double temp = (double)(listCoordValue.get(ligne.getKey().toString()+":"+k.toString())==null? 0.0 : listCoordValue.get(ligne.getKey().toString()+":"+k.toString()));
								listCoordValueTemp.put(ligne.getKey().toString()+":"+k.toString(),1/( (double) ((matrice.getProbabiliteParLigne().get(ligne.getKey())))*temp));
							}
							if(matricePuissance.getListeLiensIndividus().get(ligne.getKey()) != null)
							{
								List<Long> list = new ArrayList<>();
								list.addAll(matricePuissance.getListeLiensIndividus().get(ligne.getKey()));
								list.add(k);
								matricePuissance.getListeLiensIndividus().put(ligne.getKey(), Arrays.asList(k));
							}
							else
							{
								matricePuissance.getListeLiensIndividus().put(ligne.getKey(), Arrays.asList(k));
							}
						}
					}
				}
			}
			listCoordValue=listCoordValueTemp;
		}
		afficherMatrice(matricePuissance, listCoordValue);
	}

	static public void afficherMatrice(Matrix matrice, HashMap<String, Double> listValue)
	{
		StringBuilder sb = new StringBuilder("Affichage de la matrice entière: \n");
		for(Integer i = 0; i < matrice.getCoteMatrice().intValue(); i++)
		{
			for(Integer j = 0; j < matrice.getCoteMatrice().intValue(); j++)
			{
				if(listValue.get(i.toString()+":"+j.toString()) != null)
				{
					sb.append(listValue.get(i.toString()+":"+j.toString())+ " ");
				}
				else
				{
					sb.append("0 ");
				}
			}
			sb.append("\n");
		}
		System.out.println(sb);
	}
}
