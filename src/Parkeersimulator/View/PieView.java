package Parkeersimulator.View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import Parkeersimulator.Model.SimulatorLogic;

/**
 * Een view voor de cirkeldiagram
 * @author Andy Perukel, Ramon kits
 * @version 01-03-2019
 *
 */
public class PieView extends AbstractView {
	
	private Dimension size;
	private Image pieViewImage;
    
	/**
	 * Constructor voor de cirkeldiagram
	 * @param simulatorLogic
	 */
    public PieView(SimulatorLogic simulatorLogic) {
    	super(simulatorLogic);
		size = new Dimension(0, 0);
	}

    /**
     * Maakt de cirkeldiagram
     * @param g, een grafisch object
     */
    public void paintComponent(Graphics g) {
    	
    	if (pieViewImage == null) {
            return;
        }

        Dimension currentSize = getSize();
        if (size.equals(currentSize)) {
            g.drawImage(pieViewImage, 0, 0, null);
        }
        else {
            // Rescale the previous image.
            g.drawImage(pieViewImage, 0, 0, currentSize.width, currentSize.height, null);
        }
    	
    	int pieAdHocCars=simulatorLogic.getAmountOfAD_HOC();
		int piePassCars=simulatorLogic.getAmountOfPASS();
		int pieReservedCars=simulatorLogic.getAmountOfRESERVE();	
		
		
		int pieLocX = 30;
		int pieLocY = 80;
		int totalSpots = simulatorLogic.getScreenLogic().getNumberOfSpots();

        g.setFont(standard20px);
		g.setColor(Color.BLACK);
		g.drawString("Percentage auto's per type", pieLocX + 20, 30);
		g.drawString("Totaal aantal plekken is: " + simulatorLogic.getScreenLogic().getNumberOfSpots(), pieLocX + 20, 58);
		g.setColor(Color.WHITE);
		g.fillArc(pieLocX + 20, pieLocY, 250, 250, 0, 360);
		g.setColor(Color.RED);
		g.fillArc(pieLocX + 20, pieLocY, 250, 250, 0, (int)Math.round(360.0 * pieAdHocCars / totalSpots));
		g.setColor(Color.BLUE);
		g.fillArc(pieLocX + 20, pieLocY, 250, 250, (int)Math.round(360.0 * pieAdHocCars / totalSpots), (int)Math.round(360.0 * piePassCars / totalSpots));
		g.setColor(Color.GREEN);
		g.fillArc(pieLocX + 20, pieLocY, 250, 250, (int)Math.round(360.0 * pieAdHocCars / totalSpots) + (int)Math.round(360.0 * piePassCars / totalSpots), (int)Math.round(360.0 * pieReservedCars / totalSpots));
		g.setColor(Color.BLACK);
		g.drawArc(pieLocX + 20, pieLocY, 250, 250, 0, 360);
    }
    
    /**
     * Update de view
     */
    public void updateView() {
    	if (!size.equals(getSize())) {
            size = getSize();
            pieViewImage = createImage(size.width, size.height);
        }
        repaint();
    }
}
