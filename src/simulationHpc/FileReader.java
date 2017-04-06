package simulationHpc;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

//private static final String filePath = "../../matrix.txt";
public class FileReader {
	private static Logger logger = Logger.getLogger(FileReader.class);
	
	private FileReader()
	{
		//Constructeur implicite
	}
	
	public static Matrix readThisFile(String pathFile)
	{
		BufferedReader br;
		Matrix matriceFichier = new Matrix();
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(pathFile), "Cp1252"));
			String line = new String();
			if(br != null)
			{
				line = br.readLine();
				String[] tableauData = line.split(" ");
				//Deux premiers trucs c'est les tailles
				matriceFichier.setCoteMatrice(Long.valueOf(tableauData[0]));
				
				//Troisème champs correspond au nombre de valeur non null
				matriceFichier.setNombreValeurNonNull(Long.valueOf(tableauData[2]));
				System.out.println("Côté : " + matriceFichier.getCoteMatrice());
				System.out.println("Nombre valeurs non null : " + matriceFichier.getNombreValeurNonNull());
				//On parcourt toutes les valeurs non null pour les mettre dans la hashmap
				while ((line = br.readLine()) != null) {
					tableauData = line.split(" ");
					//On ne compte pas la diagonale
					if(!tableauData[0].equals(tableauData[1].toString()))
					{
						matriceFichier.getListCoordNonNull().add(new Coordonate(Long.valueOf(tableauData[0]), Long.valueOf(tableauData[1])));
					}
				}				
			}
			br.close();
		} catch (IOException e) {
			//une des trois exceptions a été catchée
			logger.error("FileReader@readThisFile : Le fichier n'a pas pu être parsé.");
			e.printStackTrace();
			
		}
	    return matriceFichier;
	}
	
	public static Matrix findTransitionMatrix(Matrix initMatrix) throws Exception
	{
		if(initMatrix == null || initMatrix.getListCoordNonNull() == null || initMatrix.getListCoordNonNull().isEmpty())
		{
			throw new Exception();
		}
		HashMap<Long, Long> probas = new HashMap<>();
		HashMap<Long, List<Long>> liens = new HashMap<>();
		for(Coordonate coord : initMatrix.getListCoordNonNull())
		{
			if(probas.containsKey(coord.getLine()))
			{
				List<Long> listeLong = new ArrayList<>();
				listeLong.addAll(liens.get(coord.getLine()));
				listeLong.add(coord.getColumn());
				liens.put(coord.getLine(), listeLong);
				probas.put(coord.getLine(), probas.get(coord.getLine())+1);
			}
			else
			{
				if(!coord.getLine().equals(coord.getColumn()))
				{
					liens.put(coord.getLine(), Arrays.asList(coord.getColumn()));	
					probas.put(coord.getLine(), 1L);
				}
			}
		}
		Matrix newMatrix = initMatrix;
		newMatrix.setListeLiensIndividus(liens);
		newMatrix.setProbabiliteParLigne(probas);
		return newMatrix;
	}
	
}
