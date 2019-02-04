package Parkeersimulator.View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.text.NumberFormat;
import java.util.Locale;

import Parkeersimulator.Model.SimulatorLogic;

/**
 * Een view voor de legenda, de omzet en het aantal doorgereden auto's
 * @author Andy Perukel, Ramon kits
 * @version 01-03-2019
 *
 */
public class LegendView extends AbstractView {
	
	private Dimension size;
	private Image legendViewImage;
	private int profitMissCheck;
	private float warningColor;
    
	/**
	 * Constructor voor de legenda
	 * @param simulatorLogic, de simulator
	 */
    public LegendView(SimulatorLogic simulatorLogic) {
    	super(simulatorLogic);
		size = new Dimension(0, 0);
	}

    /**
     * Maakt de legenda, de omzet en het aantal doorgereden auto's
     * @param g, een grafisch object
     */
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
        g.setFont(standard20px);
        g.setColor(Color.RED);
        g.fillRect(20, 20, 50, 20);
        g.setColor(Color.BLUE);
        g.fillRect(20, 60, 50, 20);
        g.setColor(Color.GREEN);
        g.fillRect(20, 100, 50, 20);
        g.setColor(Color.WHITE);
        g.fillRect(20, 140, 50, 20);
        g.setColor(Color.BLACK);
        g.drawString("Normale auto's", 75, 38);
        g.drawRect(20, 20, 50, 20);
        g.drawString("Abonnement auto's", 75, 78);
        g.drawRect(20, 60, 50, 20);
        g.drawString("Reserveer auto's", 75, 118);
        g.drawRect(20, 100, 50, 20);
        g.drawString("Lege plaatsen", 75, 158);
        g.drawRect(20, 140, 50, 20);
        
        NumberFormat nf = NumberFormat.getInstance(Locale.GERMAN);
        int profitMiss = simulatorLogic.getTotalDailyPassingCars();
        double profitCar = simulatorLogic.getProfitCar();
        double profitRes = simulatorLogic.getProfitReserved();
        double profitTot = profitCar + profitRes;
        int totalTicks = simulatorLogic.getTotalTicks();
        String timeString = "";
        if(totalTicks % 60 < 10) { timeString += "0" + totalTicks % 60 + " minuten"; }
        else if(totalTicks > 0) { timeString += totalTicks % 60 + " minuten"; }
        if(totalTicks >= 60 && totalTicks < 1440) { timeString += " en "; } else if(totalTicks >= 60) { timeString += ", "; }
        if(totalTicks >= 60 && totalTicks / 60 % 24 == 1) { timeString += totalTicks / 60 % 24 + " uur"; }
        else if(totalTicks >= 60) { timeString += totalTicks / 60 % 24 + " uren"; }
        if(totalTicks >= 1440 && totalTicks < 10080) { timeString += " en "; } else if(totalTicks >= 1440) { timeString += ", "; }
        if(totalTicks >= 1440 && totalTicks / 1440 % 7 == 1) { timeString += totalTicks / 1440 % 7 + " dag"; }
        else if(totalTicks >= 1440) { timeString += totalTicks / 1440 % 7 + " dagen"; }
        if(totalTicks >= 10080 && totalTicks / 10080 == 1) { timeString += " en " + totalTicks / 10080 + " week"; }
        else if(totalTicks >= 10080) { timeString += " en " + totalTicks / 10080 + " weken"; }
        if(totalTicks == 0) {timeString = "Simulator nog niet gestart";}
		g.drawString(timeString , 20, 200);
		g.translate(0, 35);
        g.drawString("Winst normale auto's:", 20, 200);
        g.drawString("\u20ac" + nf.format(profitCar), 270, 200);
        g.drawString("Winst gereserveerde auto's:", 20, 225);
        g.drawString("\u20ac" + nf.format(profitRes), 270, 225);
        g.drawString("+", 380, 225);
        g.drawLine(270, 230, 375, 230);
        g.drawString("Totale winst:", 20, 250);  
        g.drawString("\u20ac" + nf.format(profitTot), 270, 250);  
        g.drawString("Doorgereden auto's:", 20, 280);
        if(profitMiss > profitMissCheck) {
        	warningColor = 70;
        	profitMissCheck = profitMiss;
        } else if(warningColor > 0){
        	warningColor -= 3*((double)simulatorLogic.getTickPause() / 20.00);
        }
        if(warningColor < 0) warningColor = 0;
        g.setColor(new Color(255, 0, 0, (int)warningColor));
        if(!simulatorLogic.getRunning()) warningColor = 0;
        g.fillRect(267, 262, 100, 20);
        g.setColor(Color.BLACK);
        g.drawString("" + profitMiss, 270, 280);
    }
    
    /**
     * Update de view
     */
    public void updateView() {
    	if (!size.equals(getSize())) {
            size = getSize();
            legendViewImage = createImage(size.width, size.height);
        }
        repaint();
    }
}
