package Parkeersimulator.View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.text.DecimalFormat;

import Parkeersimulator.Model.SimulatorLogic;

public class LegendView extends AbstractView {
	
	private Dimension size;
	private Image legendViewImage;
    
    public LegendView(SimulatorLogic simulatorLogic) {
    	super(simulatorLogic);
		size = new Dimension(0, 0);
	}

    public void paintComponent(Graphics g) {
    	
    	if (legendViewImage == null) {
            return;
        }

        Dimension currentSize = getSize();
        if (size.equals(currentSize)) {
            g.drawImage(legendViewImage, 0, 0, null);
        }
        else {
            // Rescale the previous image.
            g.drawImage(legendViewImage, 0, 0, currentSize.width, currentSize.height, null);
        }
        
        // legenda  
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, 260, 250);
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, 260, 250);
        g.drawString("Normale vakken", 50, 30);
        g.setColor(Color.RED);
        g.fillRect(20, 20, 25, 10);
        g.setColor(Color.BLACK);
        g.drawRect(20, 20, 25, 10);
        g.drawString("Abbonement vakken", 50, 60);
        g.setColor(Color.BLUE);
        g.fillRect(20, 50, 25, 10);
        g.setColor(Color.BLACK);
        g.drawRect(20, 50, 25, 10);
        g.drawString("Reserveer vakken", 50, 90);
        g.setColor(Color.GREEN);
        g.fillRect(20, 80, 25, 10);
        g.setColor(Color.BLACK);
        g.drawRect(20, 80, 25, 10);
        g.drawString("Lege vakken", 50, 120);
        g.setColor(Color.WHITE);
        g.fillRect(20, 110, 25, 10);
        g.setColor(Color.BLACK);
        g.drawRect(20, 110, 25, 10);
        
        DecimalFormat df = new DecimalFormat("0.00");
        int profitMiss = simulatorLogic.getTotalDailyPassingCars();
        double profitCar = simulatorLogic.getProfitCar();
        double profitRes = simulatorLogic.getProfitReserved();
        double profitTot = profitCar + profitRes;
        g.drawString("Winst normale auto's:  " + df.format(profitCar), 20, 150);
        g.drawString("Winst gereserveerde auto's: " + df.format(profitRes), 20, 170);
        g.drawString("Totale winst: " + df.format(profitTot), 20, 190);  
        g.drawString("Doorgereden auto's: " + profitMiss, 20, 210);
    }
    
    public void updateView() {
    	if (!size.equals(getSize())) {
            size = getSize();
            legendViewImage = createImage(size.width, size.height);
        }
        repaint();
    }
}
