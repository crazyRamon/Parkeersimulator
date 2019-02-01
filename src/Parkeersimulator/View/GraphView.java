package Parkeersimulator.View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.text.DecimalFormat;

import Parkeersimulator.Model.SimulatorLogic;

/**
 * Een view van de lijngrafiek
 * @author Andy Perukel, Ramon kits
 * @version 01-03-2019
 *
 */
public class GraphView extends AbstractView {
	
	private Dimension size;
	private Image graphView;
	private int ticksSkipped = 0;
	private boolean start = false;
    
    /**
     * De constructor voor de view van de lijngrafiek
     * @param simulatorLogic, de simulator
     */
    public GraphView(SimulatorLogic simulatorLogic) {
    	super(simulatorLogic);
		size = new Dimension(0, 0);
	}

    /**
     * @param g, een grafisch opject
     */
    public void paintComponent(Graphics g) {
    	
    	if (graphView == null) {
            return;
        }

        Dimension currentSize = getSize();
        if (size.equals(currentSize)) {
            g.drawImage(graphView, 0, 0, null);
        }
        else {
            // Rescale the previous image.
            g.drawImage(graphView, 0, 0, currentSize.width, currentSize.height, null);
        }
        
    	for(int x = 1; x < simulatorLogic.amountOfCarsList.size(); x++) {
    		try {
	    		//alle auto's
	    		g.setColor(Color.BLACK);
	    		g.drawLine((int)(45.0 + 500.0 / simulatorLogic.getGraphLength() * x),
	    				405 - (int)(simulatorLogic.amountOfCarsList.get(x) * 355 / simulatorLogic.getMaxCars()),
	    				(int)(45.0 + 500 / simulatorLogic.getGraphLength() + 500.0 / simulatorLogic.getGraphLength() * x),
	    				405 - (int)(simulatorLogic.amountOfCarsList.get(x - 1) * 355  / simulatorLogic.getMaxCars()));
	    		g.setColor(new Color(0, 0, 0, 10));
	    		g.fillRect((int)(45.0 + 500.0 / simulatorLogic.getGraphLength() * x),
	    				405 - (int)(simulatorLogic.amountOfCarsList.get(x) * 355 / simulatorLogic.getMaxCars()),
	    				(int)(Math.ceil(500.0 / simulatorLogic.getGraphLength())),
	    				(int)(simulatorLogic.amountOfCarsList.get(x) * 355 / simulatorLogic.getMaxCars()));
	    		
	    		//AdHoc auto's
	    		g.setColor(Color.RED);
	    		g.drawLine((int)(45.0 + 500.0 / simulatorLogic.getGraphLength() * x),
	    				405 - (int)(simulatorLogic.amountOfAD_HOCCarsList.get(x) * 355 / simulatorLogic.getMaxCars()),
	    				(int)(45.0 + 500 / simulatorLogic.getGraphLength() + 500.0 / simulatorLogic.getGraphLength() * x),
	    				405 - (int)(simulatorLogic.amountOfAD_HOCCarsList.get(x - 1) * 355  / simulatorLogic.getMaxCars()));
	    		g.setColor(new Color(255, 0, 0, 10));
	    		g.fillRect((int)(45.0 + 500.0 / simulatorLogic.getGraphLength() * x),
	    				405 - (int)(simulatorLogic.amountOfAD_HOCCarsList.get(x) * 355 / simulatorLogic.getMaxCars()),
	    				(int)(Math.ceil(500.0 / simulatorLogic.getGraphLength())),
	    				(int)(simulatorLogic.amountOfAD_HOCCarsList.get(x) * 355 / simulatorLogic.getMaxCars()));
	    		
	    		//ParkingPass auto's
	    		g.setColor(Color.BLUE);
	    		g.drawLine((int)(45.0 + 500.0 / simulatorLogic.getGraphLength() * x),
	    				405 - (int)(simulatorLogic.amountOfPASSCarsList.get(x) * 355 / simulatorLogic.getMaxCars()),
	    				(int)(45.0 + 500 / simulatorLogic.getGraphLength() + 500.0 / simulatorLogic.getGraphLength() * x),
	    				405 - (int)(simulatorLogic.amountOfPASSCarsList.get(x - 1) * 355  / simulatorLogic.getMaxCars()));
	    		g.setColor(new Color(0, 0, 255, 10));
	    		g.fillRect((int)(45.0 + 500.0 / simulatorLogic.getGraphLength() * x),
	    				405 - (int)(simulatorLogic.amountOfPASSCarsList.get(x) * 355 / simulatorLogic.getMaxCars()),
	    				(int)(Math.ceil(500.0 / simulatorLogic.getGraphLength())),
	    				(int)(simulatorLogic.amountOfPASSCarsList.get(x) * 355 / simulatorLogic.getMaxCars()));
	    		
	    		//Reserved auto's
	    		g.setColor(Color.GREEN);
	    		g.drawLine((int)(45.0 + 500.0 / simulatorLogic.getGraphLength() * x),
	    				405 - (int)(simulatorLogic.amountOfRESERVECarsList.get(x) * 355 / simulatorLogic.getMaxCars()),
	    				(int)(45.0 + 500 / simulatorLogic.getGraphLength() + 500.0 / simulatorLogic.getGraphLength() * x),
	    				405 - (int)(simulatorLogic.amountOfRESERVECarsList.get(x - 1) * 355  / simulatorLogic.getMaxCars()));
	    		g.setColor(new Color(0, 255, 0, 10));
	    		g.fillRect((int)(45.0 + 500.0 / simulatorLogic.getGraphLength() * x),
	    				405 - (int)(simulatorLogic.amountOfRESERVECarsList.get(x) * 355 / simulatorLogic.getMaxCars()),
	    				(int)(Math.ceil(500.0 / simulatorLogic.getGraphLength())),
	    				(int)(simulatorLogic.amountOfRESERVECarsList.get(x) * 355 / simulatorLogic.getMaxCars()));
    		} catch(NullPointerException e) {
    			ticksSkipped++;
    			DecimalFormat df = new DecimalFormat("0,00");
    			System.out.println("Skipped "+df.format(10000*ticksSkipped/simulatorLogic.getTotalTicks())+"% of the ticks due to heavy graph");
    		}
    		
    	}
		g.setColor(Color.BLACK);
		g.drawRect(45, 50, 500, 355);		

		g.setColor(Color.BLACK);
		g.setFont(standard18px);
		if((int)simulatorLogic.getMaxCars() != 1) {
			g.drawString("Aantal auto's in percentage van het aantal auto's op het drukste moment (" + (int)simulatorLogic.getMaxCars() + " auto's)" , 30, 30);
		} else {
			g.drawString("Aantal auto's in percentage van het aantal auto's op het drukste moment (" + (int)simulatorLogic.getMaxCars() + " auto)" , 30, 30);
		}
    	
    }
    
    /**
     * Update de view en rendert de beelden die altijd zullen blijven staan
     */
    public void updateView() {
    	if (!size.equals(getSize())) {
            size = getSize();
            graphView = createImage(size.width, size.height);
        }
    	//word op het moment van opstarten van het programma 1 keer gerunt
    	if(!start) {
    		
    		Graphics g = graphView.getGraphics();
    		
    		g.setColor(Color.WHITE);
    		g.fillRect(45, 50, 500, 355);

    		g.setColor(Color.BLACK);
    		g.setFont(standard15px);
    		g.drawString("0%", 20, 405);
    		g.drawString("10%", 12, 370);
    		g.drawString("20%", 12, 336);
    		g.drawString("30%", 12, 301);
    		g.drawString("40%", 12, 267);
    		g.drawString("50%", 12, 232);
    		g.drawString("60%", 12, 198);
    		g.drawString("70%", 12, 163);
    		g.drawString("80%", 12, 129);
    		g.drawString("90%", 12, 94);
    		g.drawString("100%", 5, 60);
    		
    		g.setColor(new Color(0,0,0,30));
    		g.drawLine(45, 85, 545, 85);
    		g.drawLine(45, 121, 545, 121);
    		g.drawLine(45, 156, 545, 156);
    		g.drawLine(45, 192, 545, 192);
    		g.drawLine(45, 227, 545, 227);
    		g.drawLine(45, 263, 545, 263);
    		g.drawLine(45, 298, 545, 298);
    		g.drawLine(45, 334, 545, 334);
    		g.drawLine(45, 369, 545, 369);
    		

    		g.setColor(Color.RED);
    		g.fillRect(565, 100, 30, 15);
    		g.setColor(Color.BLUE);
    		g.fillRect(565, 130, 30, 15);
    		g.setColor(Color.GREEN);
    		g.fillRect(565, 160, 30, 15);
    		g.setFont(standard13px);
    		g.setColor(Color.BLACK);
    		g.drawRect(565, 70, 30, 15);
    		g.drawRect(565, 100, 30, 15);
    		g.drawRect(565, 130, 30, 15);
    		g.drawRect(565, 160, 30, 15);
    		g.fillRect(565, 70, 30, 15);
    		g.drawString("Totaal aantal auto's", 600, 82);
    		g.drawString("Aantal normale auto's", 600, 112);
    		g.drawString("Aantal abonnement auto's", 600, 142);
    		g.drawString("Aantal reserveer auto's", 600, 172);
    		
    		start = true;
    	}
        repaint();
    }
}
