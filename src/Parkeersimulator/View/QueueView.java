package Parkeersimulator.View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import Parkeersimulator.Model.SimulatorLogic;

/**
 * Een view voor de rijen
 * @author Andy Perukel, Ramon kits
 * @version 01-03-2019
 * 
 */
public class QueueView extends AbstractView {	
	
	private Dimension size;
	private Image queueViewImage;
    
	/**
	 * Constructor voor de rijen
	 * @param simulatorLogic, de simulator
	 */
    public QueueView(SimulatorLogic simulatorLogic) {
    	super(simulatorLogic);
		size = new Dimension(0, 0);
	}
    
    /**
     * Maakt de queue balken
     * @param g, een grafisch object
     */
    public void paintComponent(Graphics g) {
    	
    	if (queueViewImage == null) {
            return;
        }

        Dimension currentSize = getSize();
        if (size.equals(currentSize)) {
            g.drawImage(queueViewImage, 0, 0, null);
        }
        else {
            // Rescale the previous image.
            g.drawImage(queueViewImage, 0, 0, currentSize.width, currentSize.height, null);
        }
        
        g.translate(10, 10);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 735, 80);
        g.setColor(Color.BLACK);
        float percentageEntranceCarQueue = (float)simulatorLogic.getCarsInEntranceCarQueue() / 10;
        float percentageEntrancePassQueue = (float)simulatorLogic.getCarsInEntrancePassQueue() / 10;
        float percentagePaymentCarQueue = (float)simulatorLogic.getCarsInPaymentCarQueue() / 10;
        float percentageExitCarQueue = (float)simulatorLogic.getCarsInExitCarQueue() / 10;
        g.setColor(new Color((int)(percentageEntranceCarQueue * 255), 255 - (int)(percentageEntranceCarQueue * 255), 0));
        g.fillRect(0, 0, (int)(percentageEntranceCarQueue * 735), 20);
        g.setColor(new Color((int)(percentageEntrancePassQueue * 255), 255 - (int)(percentageEntrancePassQueue * 255), 0));
        g.fillRect(0, 20, (int)(percentageEntrancePassQueue * 735), 20);
        g.setColor(new Color((int)(percentagePaymentCarQueue * 255), 255 - (int)(percentagePaymentCarQueue * 255), 0));
        g.fillRect(0, 40, (int)(percentagePaymentCarQueue * 735), 20);
        g.setColor(new Color((int)(percentageExitCarQueue * 255), 255 - (int)(percentageExitCarQueue * 255), 0));
        g.fillRect(0, 60, (int)(percentageExitCarQueue * 735), 20);
        g.setColor(Color.BLACK);
        g.drawString("EntranceCarQueue", 3, 12);
        g.drawString("EntrancePassQueue", 3, 32);
        g.drawString("PaymentCarQueue", 3, 52);
        g.drawString("ExitCarQueue", 3, 72);
    }
    
    /**
     * Update de view
     */
    public void updateView() {
    	if (!size.equals(getSize())) {
            size = getSize();
            queueViewImage = createImage(size.width, size.height);
        }
        repaint();
    }
}
