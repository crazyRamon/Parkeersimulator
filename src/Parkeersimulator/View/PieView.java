package Parkeersimulator.View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import Parkeersimulator.Model.SimulatorLogic;

public class PieView extends AbstractView {
	
	private Dimension size;
	private Image pieViewImage;
    
    public PieView(SimulatorLogic simulatorLogic) {
    	super(simulatorLogic);
		size = new Dimension(0, 0);
	}

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
		
		
		int pieLocX = 0;
		int pieLocY = 50;
		int pieLocXcolor = pieLocX + 10;
		int totalSpots = simulatorLogic.getScreenLogic().getNumberOfSpots();
		
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(pieLocX, pieLocY-50, 200, 250);
		g.setColor(Color.BLACK);
		g.drawString("Pie Chart", pieLocX + 20, pieLocY - 30);
		g.setColor(Color.BLACK);
		g.drawRect(pieLocX, pieLocY-50, 200, 250);
		g.setColor(Color.WHITE);
		g.fillArc(pieLocXcolor, pieLocY, 180, 180, 0, 360);
		g.setColor(Color.RED);
		g.fillArc(pieLocXcolor, pieLocY, 180, 180, 0, (int)Math.round(360.0 * pieAdHocCars / totalSpots));
		g.setColor(Color.BLUE);
		g.fillArc(pieLocXcolor, pieLocY, 180, 180, (int)Math.round(360.0 * pieAdHocCars / totalSpots), (int)Math.round(360.0 * piePassCars / totalSpots));
		g.setColor(Color.GREEN);
		g.fillArc(pieLocXcolor, pieLocY, 180, 180, (int)Math.round(360.0 * pieAdHocCars / totalSpots) + (int)Math.round(360.0 * piePassCars / totalSpots), (int)Math.round(360.0 * pieReservedCars / totalSpots));
    }
    
    public void updateView() {
    	if (!size.equals(getSize())) {
            size = getSize();
            pieViewImage = createImage(size.width, size.height);
        }
        repaint();
    }
}
