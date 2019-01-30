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
		int totalSpots = simulatorLogic.getScreenLogic().getNumberOfSpots();

        g.setFont(standard20px);
		g.setColor(Color.BLACK);
		g.drawString("Percentage auto's per type", pieLocX + 20, 30);
		g.setColor(Color.WHITE);
		g.fillArc(20, pieLocY, 250, 250, 0, 360);
		g.setColor(Color.RED);
		g.fillArc(20, pieLocY, 250, 250, 0, (int)Math.round(360.0 * pieAdHocCars / totalSpots));
		g.setColor(Color.BLUE);
		g.fillArc(20, pieLocY, 250, 250, (int)Math.round(360.0 * pieAdHocCars / totalSpots), (int)Math.round(360.0 * piePassCars / totalSpots));
		g.setColor(Color.GREEN);
		g.fillArc(20, pieLocY, 250, 250, (int)Math.round(360.0 * pieAdHocCars / totalSpots) + (int)Math.round(360.0 * piePassCars / totalSpots), (int)Math.round(360.0 * pieReservedCars / totalSpots));
		g.setColor(Color.BLACK);
		g.drawArc(20, pieLocY, 250, 250, 0, 360);
    }
    
    public void updateView() {
    	if (!size.equals(getSize())) {
            size = getSize();
            pieViewImage = createImage(size.width, size.height);
        }
        repaint();
    }
    
    public void firstFrame() {
    	Graphics g = pieViewImage.getGraphics();
    	g.fillRect(-100, -100, 1000, 1000);
    }
}
