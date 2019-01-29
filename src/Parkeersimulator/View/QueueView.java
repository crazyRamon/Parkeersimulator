package Parkeersimulator.View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import Parkeersimulator.Model.SimulatorLogic;

public class QueueView extends AbstractView {	
	
	private Dimension size;
	private Image queueViewImage;
    
    public QueueView(SimulatorLogic simulatorLogic) {
    	super(simulatorLogic);
		size = new Dimension(0, 0);
	}
    
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
        
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 710, 80);
        g.setColor(Color.BLACK);
        float percentageEntranceCarQueue = (float)simulatorLogic.getCarsInEntranceCarQueue() / 10 * 710;
        float percentageEntrancePassQueue = (float)simulatorLogic.getCarsInEntrancePassQueue() / 10 * 710;
        float percentagePaymentCarQueue = (float)simulatorLogic.getCarsInPaymentCarQueue() / 10 * 710;
        float percentageExitCarQueue = (float)simulatorLogic.getCarsInExitCarQueue() / 10 * 710;
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, (int)percentageEntranceCarQueue, 20);
        g.fillRect(0, 20, (int)percentageEntrancePassQueue, 20);
        g.fillRect(0, 40, (int)percentagePaymentCarQueue, 20);
        g.fillRect(0, 60, (int)percentageExitCarQueue, 20);
        g.setColor(Color.RED);
        g.drawString("EntranceCarQueue", 3, 12);
        g.drawString("EntrancePassQueue", 3, 32);
        g.drawString("PaymentCarQueue", 3, 52);
        g.drawString("ExitCarQueue", 3, 72);
    }
    
    public void updateView() {
    	if (!size.equals(getSize())) {
            size = getSize();
            queueViewImage = createImage(size.width, size.height);
        }
        repaint();
    }
}
