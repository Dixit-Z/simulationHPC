package simulationHpc;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JPanel;
/**
 * Classe dessinatrice de chart baby.
 * @author Bruno
 *
 */
public class ChartDrawer extends JPanel {

	private static final long serialVersionUID = -4249593814889749532L;
    private int padding = 25;
    private int labelPadding = 25;
    private Color lineColor = new Color(44, 102, 230, 180);
    private Color pointColor = new Color(100, 100, 100, 180);
    private Color gridColor = new Color(200, 200, 200, 200);
    private List<Color> listUnusedColor = new ArrayList<>(Arrays.asList(Color.BLACK, Color.BLUE, Color.GREEN, Color.MAGENTA, Color.ORANGE, Color.PINK, Color.CYAN, Color.RED, Color.YELLOW, Color.DARK_GRAY));
    private static final Stroke GRAPH_STROKE = new BasicStroke(2f);
    private int pointWidth = 4;
    private int numberYDivisions = 10;
    private int xsize = 0;
    private Boolean multiColoring = false;
    private List<String> titreCourbe;
    private Integer lastTextPositionY;
    private List<Integer> nbInfectes;
    private List<List<Integer>> nbInfectesParSimulation;

    public List<String> getTitreCourbe() {
		return titreCourbe;
	}

	public void setTitreCourbe(List<String> titreCourbe) {
		this.titreCourbe = titreCourbe;
	}

	public ChartDrawer(Boolean multiColoring) {
        this.nbInfectes = new ArrayList<>();
        this.nbInfectesParSimulation = new ArrayList<>();
        this.multiColoring = multiColoring;
        this.titreCourbe = new ArrayList<>();
        this.lastTextPositionY = getHeight();
    }
    
    public void addPoint(Integer nbInfectes)
    {
    	if(nbInfectes != null && this.nbInfectes != null)
    	{
    		this.nbInfectes.add(nbInfectes);
    	}
    }
    
    public void closeSimulation()
    {
    	addSimulation(this.nbInfectes);
    	this.nbInfectes.clear();
    }
    
    private void addSimulation(List<Integer> simulation)
    {
    	if(simulation != null && this.nbInfectesParSimulation != null)
    	{
    		List<Integer> list = new ArrayList<>();
    		list.addAll(simulation);
    		this.nbInfectesParSimulation.add(list);
    	}
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if((nbInfectes == null || nbInfectes.isEmpty()) && !nbInfectesParSimulation.isEmpty())
        {
        	this.xsize = nbInfectesParSimulation.get(0).size();
        }
        else
        {
        	this.xsize = nbInfectes.size();
        }
        double xScale = ((double) getWidth() - (2 * padding) - labelPadding) / (this.xsize - 1);
        double yScale = ((double) getHeight() - 2 * padding - labelPadding) / (getMaxInfectes() - getMinInfectes());

        List<List<Point>> graphPoints = defineGraphPoints(xScale, yScale);

        // draw white background
        g2.setColor(Color.WHITE);
        g2.fillRect(padding + labelPadding, padding, getWidth() - (2 * padding) - labelPadding, getHeight() - 2 * padding - labelPadding);
        g2.setColor(Color.BLACK);

        // create hatch marks and grid lines for y axis.
        drawYAxis(g2);

        // and for x axis
        drawXAxis(g2);

        // create x and y axes 
        g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, padding + labelPadding, padding);
        g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, getWidth() - padding, getHeight() - padding - labelPadding);

        Stroke oldStroke = g2.getStroke();
        for(List<Point> graphUnit : graphPoints)
        {
        	g2.setStroke(GRAPH_STROKE);
        	if(this.multiColoring)
        	{
        		g2.setColor(generateNewColor());
        	}
        	else
        	{
        		g2.setColor(lineColor);
        	}
        	for (int i = 0; i < graphUnit.size() - 1; i++) {
        		int x1 = graphUnit.get(i).x;
        		int y1 = graphUnit.get(i).y;
        		int x2 = graphUnit.get(i + 1).x;
        		int y2 = graphUnit.get(i + 1).y;
        		g2.drawLine(x1, y1, x2, y2);
        	}
        	if(this.titreCourbe != null && !this.titreCourbe.isEmpty())
        	{
        		List<String> lesTitres = new ArrayList<>();
        		if(this.lastTextPositionY == 0)
        		{
        			this.lastTextPositionY = getHeight()-430;
        		}
        		else
        		{
        			this.lastTextPositionY += 20;
        		}
        		lesTitres.addAll(titreCourbe);
        		g2.drawString(lesTitres.get(0), getWidth()-300, lastTextPositionY);
        		if(this.titreCourbe.size() == 1)
        		{
        			lesTitres.clear();
        			this.titreCourbe = lesTitres;
        		}
        		else
        		{
        			lesTitres.remove(0);
        			this.titreCourbe = lesTitres;
        		}
        	}
        	g2.setStroke(oldStroke);
        	g2.setColor(g2.getColor().darker());
        	for (int i = 0; i < graphUnit.size(); i++) {
        		int x = graphUnit.get(i).x - pointWidth / 2;
        		int y = graphUnit.get(i).y - pointWidth / 2;
        		int ovalW = pointWidth;
        		int ovalH = pointWidth;
        		g2.fillOval(x, y, ovalW, ovalH);
        	}
        }
    }

	private List<List<Point>> defineGraphPoints(double xScale, double yScale) {
		List<List<Point>> listOfGraphPoints = new ArrayList<>(); 
		if(this.nbInfectesParSimulation != null && !this.nbInfectesParSimulation.isEmpty())
		{
			for(List<Integer> listOfPoint : nbInfectesParSimulation)
			{
				List<Point> graphPoints = new ArrayList<>();
				for(int i = 0; i < listOfPoint.size() ; i++)
				{
					int x1 = (int) (i * xScale + padding + labelPadding);
					int y1 = (int) ((getMaxInfectes() - listOfPoint.get(i)) * yScale + padding);
					graphPoints.add(new Point(x1, y1));
				}
				listOfGraphPoints.add(graphPoints);
			}
		}
		else
		{
			List<Point> graphPoints = new ArrayList<>();
			for (int i = 0; i < nbInfectes.size(); i++) {
				int x1 = (int) (i * xScale + padding + labelPadding);
				int y1 = (int) ((getMaxInfectes() - nbInfectes.get(i)) * yScale + padding);
				graphPoints.add(new Point(x1, y1));
			}
			listOfGraphPoints.add(graphPoints);
		}
		return listOfGraphPoints;
	}

    /**
     * Permet de dessiner l'axe des ordonnées selon le nombre d'infectés.
     * @param g2
     */
	private void drawYAxis(Graphics2D g2) {
		for (int i = 0; i < numberYDivisions + 1; i++) {
            int x0 = padding + labelPadding;
            int x1 = pointWidth + padding + labelPadding;
            int y0 = getHeight() - ((i * (getHeight() - padding * 2 - labelPadding)) / numberYDivisions + padding + labelPadding);
            int y1 = y0;
            if (this.xsize > 0) {
                g2.setColor(gridColor);
                g2.drawLine(padding + labelPadding + 1 + pointWidth, y0, getWidth() - padding, y1);
                g2.setColor(Color.BLACK);
                String yLabel = ((int) ((getMinInfectes() + (getMaxInfectes() - getMinInfectes()) * ((i * 1.0) / numberYDivisions)) * 100)) / 100.0 + "";
                FontMetrics metrics = g2.getFontMetrics();
                int labelWidth = metrics.stringWidth(yLabel);
                g2.drawString(yLabel, x0 - labelWidth - 5, y0 + (metrics.getHeight() / 2) - 3);
            }
            g2.drawLine(x0, y0, x1, y1);
        }
	}

	/**
	 * Permet de dessiner l'axe des absices selon le temps.
	 * @param g2
	 */
	private void drawXAxis(Graphics2D g2) {
		for (int i = 0; i < this.xsize; i++) {
            if (this.xsize > 1) {
                int x0 = i * (getWidth() - padding * 2 - labelPadding) / (this.xsize - 1) + padding + labelPadding;
                int x1 = x0;
                int y0 = getHeight() - padding - labelPadding;
                int y1 = y0 - pointWidth;
                if ((i % ((int) ((this.xsize / 20.0)) + 1)) == 0) {
                    g2.setColor(gridColor);
                    g2.drawLine(x0, getHeight() - padding - labelPadding - 1 - pointWidth, x1, padding);
                    g2.setColor(Color.BLACK);
                    String xLabel = i + "";
                    FontMetrics metrics = g2.getFontMetrics();
                    int labelWidth = metrics.stringWidth(xLabel);
                    g2.drawString(xLabel, x0 - labelWidth / 2, y0 + metrics.getHeight() + 3);
                }
                g2.drawLine(x0, y0, x1, y1);
            }
        }
	}
    /**
     * Méthode permettant de retourner le nombre minimum d'infecté pour la simulation.
     * @return minInfectes : Le nombre minimum d'infectés pour la simulation.
     */
    private double getMinInfectes() {
        double minInfectes = Integer.MAX_VALUE;
        if(this.nbInfectesParSimulation != null && !this.nbInfectesParSimulation.isEmpty())
        {
        	for(int i = 0 ; i< this.nbInfectesParSimulation.size() ; i++)
        	{
        		for(Integer infectesParSimulation : this.nbInfectesParSimulation.get(i))
        		{
        			minInfectes = Math.min(minInfectes, infectesParSimulation);
        		}
        	}
        }
        else
        {
		    for (Integer infectes : nbInfectes) {
		    	minInfectes = Math.min(minInfectes, infectes);
		    }
        }
        return minInfectes;
    }

    /**
     * Méthode permettant de retourner le nombre maximum d'infectés pour la simulation.
     * @return maxInfectes : Le nombre maximum d'infectés pour la simulation.
     */
    private double getMaxInfectes() {
        double maxInfectes = Integer.MIN_VALUE;
        if(this.nbInfectesParSimulation != null && !this.nbInfectesParSimulation.isEmpty())
        {
        	maxInfectes = 10000;
        }
        else
        {
		    for (Integer infectes : nbInfectes) {
		    	maxInfectes = Math.max(maxInfectes, infectes);
		    }
        }
        return maxInfectes;
    }
    
    private Color generateNewColor()
    {
    	if(!this.listUnusedColor.isEmpty())
    	{
    		Color unusedColor = listUnusedColor.get(0);
    		listUnusedColor.remove(0);
    		return unusedColor;
    	}
    	else
    	{
    		return new Color(integerGenerator(), integerGenerator(), integerGenerator());
    	}
    }
    
    private Integer integerGenerator()
    {
    	Double doubleValue = Math.random();
    	return doubleValue.intValue()*100;
    }

    public void setNbInfectes(List<Integer> nbInfectes) {
        this.nbInfectes = nbInfectes;
        invalidate();
        this.repaint();
    }

    public List<Integer> getNbInfectes() {
        return nbInfectes;
   }

	public List<List<Integer>> getNbInfectesParSimulation() {
		return nbInfectesParSimulation;
	}

	public void setNbInfectesParSimulation(List<List<Integer>> nbInfectesParSimulation) {
		this.nbInfectesParSimulation = nbInfectesParSimulation;
	}
}