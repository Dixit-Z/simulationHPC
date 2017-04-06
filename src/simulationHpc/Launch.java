package simulationHpc;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.apache.log4j.Logger;

public class Launch {
	static Logger logger = Logger.getLogger(Launch.class);
	private static String matrixPath = "res/matrix.txt";
	public static void main(String[] args) {
		//Permet de setter les différentes map de la matrice
		Matrix matriceDuFichier = FileReader.readThisFile(matrixPath);
		ChartDrawer generalDrawer = new ChartDrawer(Boolean.TRUE);
		generalDrawer.setPreferredSize(new Dimension(800, 600));
		generalDrawer.setTitreCourbe(new ArrayList<String>(Arrays.asList("Sans vaccin","5% vaccinés aléatoirement","5% des infectés sont soignés","5% vaccinés du vecteur d'infection")));
		ChartDrawer generalDrawerAutoMedic = new ChartDrawer(Boolean.TRUE);
		generalDrawerAutoMedic.setPreferredSize(new Dimension(800, 600));
		generalDrawerAutoMedic.setTitreCourbe(new ArrayList<String>(Arrays.asList("Sans vaccin","5% vaccinés aléatoirement","5% des infectés sont soignés","5% vaccinés du vecteur d'infection")));
		ChartDrawer chartDrawer = new ChartDrawer(Boolean.FALSE);
		chartDrawer.setPreferredSize(new Dimension(800, 600));
		try {
			matriceDuFichier = FileReader.findTransitionMatrix(matriceDuFichier);
			//Permet de créer les objet et les liste d'individus
			Organization myOrganization = new Organization(matriceDuFichier);
//			matriceDuFichier.afficherData();
//			System.out.println("**********************************\n* HUMANS INFO\n**********************************");
//			for(Human humans : myOrganization.listHumans.values())
//			{
//				humans.afficherDataHuman();
//			}
			
			//Simulation n°1 : Sans vaccination.
			String fileName = "infection_sans_vaccin";
			PrintWriter writer = new PrintWriter(new File(fileName+".csv"));
			StringBuilder sb = new StringBuilder();
			SimulationUtil.startInfection(myOrganization, 5.00, 0.00, 0.00);
//			System.out.println("Nombre d'infectes initialement :" + myOrganization.getNombreInfectes().toString());
			sb.append(myOrganization.getNombreInfectes().toString()+";0;\n");
			for(int i = 1; i<101; i++)
			{
				SimulationUtil.nextStepInfection(myOrganization, Boolean.FALSE);
//				System.out.println("Nombre d'infectes à l'étape "+ i +" : " + myOrganization.getNombreInfectes().toString());
				sb.append(myOrganization.getNombreInfectes().toString()+";"+ i + ";\n");
				chartDrawer.addPoint(myOrganization.getNombreInfectes());
				generalDrawer.addPoint(myOrganization.getNombreInfectes());
			}
			generalDrawer.closeSimulation();
			writer.write(sb.toString());
			writer.flush();
			writer.close();
	        JFrame frame = new JFrame("Simulations Unitaires");
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.getContentPane().add(chartDrawer);
	        frame.pack();
	        frame.setLocationRelativeTo(null);
	        BufferedImage jpegChart = new BufferedImage(chartDrawer.getWidth(), chartDrawer.getHeight(), BufferedImage.TYPE_INT_RGB);
	        chartDrawer.print(jpegChart.getGraphics());
            ImageIO.write(jpegChart, "jpg", new File(fileName+".jpg"));
			SimulationUtil.reInitSimulation(myOrganization, chartDrawer);
			
			//Simulation n°2 : Vaccination de 5% de la population.
			SimulationUtil.startInfection(myOrganization, 5.00, 5.00, 0.00);
			fileName = "infection_avec_vaccin";
			writer = new PrintWriter(new File(fileName  + ".csv"));
			//On réinitialise le string builder
			sb.setLength(0);
			sb.append(myOrganization.getNombreInfectes().toString()+";0;\n");
//			System.out.println("Nombre d'infectes initialement :" + myOrganization.getNombreInfectes().toString());
			for(int i = 1; i<101; i++)
			{
				SimulationUtil.nextStepInfection(myOrganization, Boolean.FALSE);
//				System.out.println("Nombre d'infectes à l'étape "+ i +" : " + myOrganization.getNombreInfectes().toString());
				sb.append(myOrganization.getNombreInfectes().toString()+";" +i +";\n");
				chartDrawer.addPoint(myOrganization.getNombreInfectes());
				generalDrawer.addPoint(myOrganization.getNombreInfectes());
			}
			generalDrawer.closeSimulation();
			writer.write(sb.toString());
			writer.flush();
			writer.close();
	        jpegChart = new BufferedImage(chartDrawer.getWidth(), chartDrawer.getHeight(), BufferedImage.TYPE_INT_RGB);
	        chartDrawer.print(jpegChart.getGraphics());
            ImageIO.write(jpegChart, "jpg", new File(fileName+".jpg"));
			SimulationUtil.reInitSimulation(myOrganization, chartDrawer);
			
			//Simulation n°3 : vaccination avec 5% de la pop + 5 % des premiers inféctés vaccinés
			fileName = "infection_avec_vaccin_preventif_et_soin_rapide";
			writer = new PrintWriter(new File(fileName + ".csv"));
			sb.setLength(0);
			SimulationUtil.startInfection(myOrganization, 5.00, 5.00, 5.00);
			sb.append(myOrganization.getNombreInfectes().toString()+";0\n");
//			System.out.println("Nombre d'infectes initialement :" + myOrganization.getNombreInfectes().toString());
			for(int i = 1; i<101; i++)
			{
				SimulationUtil.nextStepInfection(myOrganization, Boolean.FALSE);
//				System.out.println("Nombre d'infectes à l'étape "+ i +" : " + myOrganization.getNombreInfectes().toString());
				sb.append(myOrganization.getNombreInfectes().toString()+";"+i+";\n");
				chartDrawer.addPoint(myOrganization.getNombreInfectes());
				generalDrawer.addPoint(myOrganization.getNombreInfectes());
			}
			generalDrawer.closeSimulation();
			writer.write(sb.toString());
			writer.flush();
			writer.close();
	        jpegChart = new BufferedImage(chartDrawer.getWidth(), chartDrawer.getHeight(), BufferedImage.TYPE_INT_RGB);
	        chartDrawer.print(jpegChart.getGraphics());
            ImageIO.write(jpegChart, "jpg", new File(fileName+".jpg"));
			SimulationUtil.reInitSimulation(myOrganization, chartDrawer);
			
			//Simulation n°4 : Vaccination de 5% de la population la plus importante.
			fileName = "infection_avec_vaccin_page_rank";
			writer = new PrintWriter(new File(fileName + ".csv"));
			sb.setLength(0);
			myOrganization.getMatrice().setVecteurInfectes(OrganizationUtils.trouverVecteurInfectionLienEntrant(myOrganization));
			SimulationUtil.startInfection(myOrganization, 5.00, 5.00, 0.00);
//			System.out.println("Nombre d'infectes initialement :" + myOrganization.getNombreInfectes().toString());
			sb.append(myOrganization.getNombreInfectes().toString()+";0\n");
			for(int i = 1; i<101; i++)
			{
				SimulationUtil.nextStepInfection(myOrganization, Boolean.FALSE);
//				System.out.println("Nombre d'infectes à l'étape "+ i +" : " + myOrganization.getNombreInfectes().toString());
				sb.append(myOrganization.getNombreInfectes().toString()+ ";" + i +"\n");
				chartDrawer.addPoint(myOrganization.getNombreInfectes());
				generalDrawer.addPoint(myOrganization.getNombreInfectes());
			}
			generalDrawer.closeSimulation();
			writer.write(sb.toString());
			writer.flush();
			writer.close();
	        jpegChart = new BufferedImage(chartDrawer.getWidth(), chartDrawer.getHeight(), BufferedImage.TYPE_INT_RGB);
	        chartDrawer.print(jpegChart.getGraphics());
            ImageIO.write(jpegChart, "jpg", new File(fileName+".jpg"));
			SimulationUtil.reInitSimulation(myOrganization, chartDrawer);
			
			//Simulation n°5 : Même que 1 mais les individus se soignent avec 10% de chance a chaque temps.
			fileName = "infection_sans_vaccin_auto_medic";
			writer = new PrintWriter(new File(fileName + ".csv"));
			sb.setLength(0);
			//On lance l'infection sur 5% de la population dans vacciner personne.
			SimulationUtil.startInfection(myOrganization, 5.00, 0.00, 0.00);
//			System.out.println("Nombre d'infectes initialement :" + myOrganization.getNombreInfectes().toString());
			sb.append(myOrganization.getNombreInfectes().toString()+";0;\n");
			for(int i = 1; i<101; i++)
			{
				SimulationUtil.nextStepInfection(myOrganization, Boolean.TRUE);
//				System.out.println("Nombre d'infectes à l'étape "+ i +" : " + myOrganization.getNombreInfectes().toString());
				sb.append(myOrganization.getNombreInfectes().toString()+";" +i +";\n");
				chartDrawer.addPoint(myOrganization.getNombreInfectes());
				generalDrawerAutoMedic.addPoint(myOrganization.getNombreInfectes());
			}
			generalDrawerAutoMedic.closeSimulation();
			writer.write(sb.toString());
			writer.flush();
			writer.close();
	        jpegChart = new BufferedImage(chartDrawer.getWidth(), chartDrawer.getHeight(), BufferedImage.TYPE_INT_RGB);
	        chartDrawer.print(jpegChart.getGraphics());
            ImageIO.write(jpegChart, "jpg", new File(fileName+".jpg"));
			SimulationUtil.reInitSimulation(myOrganization, chartDrawer);
			
			//Simulation n°6 : Même que 2 mais les individus se soignent avec 10% de chance à chaque temps.
			SimulationUtil.startInfection(myOrganization, 5.00, 5.00, 0.00);
			fileName = "infection_avec_vaccin_auto_medic";
			writer = new PrintWriter(new File(fileName + ".csv"));
			//On réinitialise le string builder
			sb.setLength(0);
			sb.append(myOrganization.getNombreInfectes().toString()+";0;\n");
//			System.out.println("Nombre d'infectes initialement :" + myOrganization.getNombreInfectes().toString());
			for(int i = 1; i<101; i++)
			{
				SimulationUtil.nextStepInfection(myOrganization, Boolean.TRUE);
//				System.out.println("Nombre d'infectes à l'étape "+ i +" : " + myOrganization.getNombreInfectes().toString());
				sb.append(myOrganization.getNombreInfectes().toString()+";" +i +";\n");
				chartDrawer.addPoint(myOrganization.getNombreInfectes());
				generalDrawerAutoMedic.addPoint(myOrganization.getNombreInfectes());
			}
			generalDrawerAutoMedic.closeSimulation();
			writer.write(sb.toString());
			writer.flush();
			writer.close();
	        jpegChart = new BufferedImage(chartDrawer.getWidth(), chartDrawer.getHeight(), BufferedImage.TYPE_INT_RGB);
	        chartDrawer.print(jpegChart.getGraphics());
            ImageIO.write(jpegChart, "jpg", new File(fileName+".jpg"));
			SimulationUtil.reInitSimulation(myOrganization, chartDrawer);
			
			//Simulation n°7 : Même que 3 mais les individus se soignent avec 10% de chance à chaque temps.
			fileName = "infection_avec_vaccin_preventif_et_soin_rapide_auto_medic";
			writer = new PrintWriter(new File(fileName + ".csv"));
			sb.setLength(0);
			SimulationUtil.startInfection(myOrganization, 5.00, 5.00, 5.00);
			sb.append(myOrganization.getNombreInfectes().toString()+";0\n");
//			System.out.println("Nombre d'infectes initialement :" + myOrganization.getNombreInfectes().toString());
			for(int i = 1; i<101; i++)
			{
				SimulationUtil.nextStepInfection(myOrganization, Boolean.TRUE);
//				System.out.println("Nombre d'infectes à l'étape "+ i +" : " + myOrganization.getNombreInfectes().toString());
				sb.append(myOrganization.getNombreInfectes().toString()+";"+i+";\n");
				chartDrawer.addPoint(myOrganization.getNombreInfectes());
				generalDrawerAutoMedic.addPoint(myOrganization.getNombreInfectes());
			}
			generalDrawerAutoMedic.closeSimulation();
			writer.write(sb.toString());
			writer.flush();
			writer.close();
	        jpegChart = new BufferedImage(chartDrawer.getWidth(), chartDrawer.getHeight(), BufferedImage.TYPE_INT_RGB);
	        chartDrawer.print(jpegChart.getGraphics());
            ImageIO.write(jpegChart, "jpg", new File(fileName+".jpg"));
			SimulationUtil.reInitSimulation(myOrganization, chartDrawer);
			
			
			//Simulation n°8 : Même que 4 mais les individus se soignent avec 10% de chance à chaque temps.
			fileName = "infection_avec_vaccin_page_rank_auto_medic";
			writer = new PrintWriter(new File(fileName + ".csv"));
			sb.setLength(0);
			myOrganization.getMatrice().setVecteurInfectes(OrganizationUtils.trouverVecteurInfectionLienEntrant(myOrganization));
			SimulationUtil.startInfection(myOrganization, 5.00, 5.00, 0.00);
//			System.out.println("Nombre d'infectes initialement :" + myOrganization.getNombreInfectes().toString());
			sb.append(myOrganization.getNombreInfectes().toString()+";0;\n");
			for(int i = 1; i<101; i++)
			{
				SimulationUtil.nextStepInfection(myOrganization, Boolean.TRUE);
//				System.out.println("Nombre d'infectes à l'étape "+ i +" : " + myOrganization.getNombreInfectes().toString());
				sb.append(myOrganization.getNombreInfectes().toString()+";" +i +";\n");
				chartDrawer.addPoint(myOrganization.getNombreInfectes());
				generalDrawerAutoMedic.addPoint(myOrganization.getNombreInfectes());
			}
			generalDrawerAutoMedic.closeSimulation();
			writer.write(sb.toString());
			writer.flush();
			writer.close();
	        jpegChart = new BufferedImage(chartDrawer.getWidth(), chartDrawer.getHeight(), BufferedImage.TYPE_INT_RGB);
	        chartDrawer.print(jpegChart.getGraphics());
            ImageIO.write(jpegChart, "jpg", new File(fileName+".jpg"));
			SimulationUtil.reInitSimulation(myOrganization, chartDrawer);
			
	        JFrame frameGeneral = new JFrame("Simulation générale");
	        frameGeneral.getContentPane().add(generalDrawer);
	        frameGeneral.pack();
	        frameGeneral.setLocationRelativeTo(null);
	        jpegChart = new BufferedImage(generalDrawer.getWidth(), generalDrawer.getHeight(), BufferedImage.TYPE_INT_RGB);
	        generalDrawer.print(jpegChart.getGraphics());
            ImageIO.write(jpegChart, "jpg", new File("toutes_simulations.jpg"));
            
            JFrame frameGeneralAutomedic = new JFrame("Simulation générale automedic");
            frameGeneralAutomedic.getContentPane().add(generalDrawerAutoMedic);
            frameGeneralAutomedic.pack();
            frameGeneralAutomedic.setLocationRelativeTo(null);
	        jpegChart = new BufferedImage(generalDrawerAutoMedic.getWidth(), generalDrawerAutoMedic.getHeight(), BufferedImage.TYPE_INT_RGB);
	        generalDrawerAutoMedic.print(jpegChart.getGraphics());
            ImageIO.write(jpegChart, "jpg", new File("toutes_simulations_auto_medic.jpg"));

		} catch (Exception e) {
			logger.error("Launch@Main : Aucune matrice n'a été trouvée.");
			e.printStackTrace();
		}
	} 

}
