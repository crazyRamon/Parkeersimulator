package Parkeersimulator.View;

import java.awt.*;

import Parkeersimulator.Model.SimulatorLogic;

public class BarChartView extends AbstractView {
	
	private int maxCarCount = 1;
	private Dimension size;
	private Image barChartImage;
    
    public BarChartView(SimulatorLogic simulatorLogic) {
		super(simulatorLogic);
		size = new Dimension(0, 0);
	}
    
	public void paintComponent(Graphics g) {

        if (barChartImage == null) {
            return;
        }

        Dimension currentSize = getSize();
        if (size.equals(currentSize)) {
            g.drawImage(barChartImage, 0, 0, null);
        }
        else {
            // Rescale the previous image.
            g.drawImage(barChartImage, 0, 0, currentSize.width, currentSize.height, null);
        }
        
        maxCarCount = simulatorLogic.getMaxCarCount();
        g.setColor(Color.WHITE);
        g.fillRect(40,  15, 330, 300);
        g.setColor(Color.LIGHT_GRAY);
        g.drawLine(40, 15 + 75, 370, 15 + 75);
        g.drawLine(40, 15 + 150, 370, 15 + 150);
        g.drawLine(40, 15 + 225, 370, 15 + 225);
        for(int day = 0; day < 7; day++) {
        	g.setColor(LIGHTRED);
	        g.fillRect(40 + day * 50, (int)(316 - (int)(300 * simulatorLogic.getPreviousWeekDailyCarCountAD_HOC(day) / maxCarCount)), 10, (int)(300 * simulatorLogic.getPreviousWeekDailyCarCountAD_HOC(day) / maxCarCount));
	        g.setColor(LIGHTGREEN);
	        g.fillRect(40 + 10 + day * 50, (int)(316 - (int)(300 * simulatorLogic.getPreviousWeekDailyCarCountRESERVE(day) / maxCarCount)), 10, (int)(300 * simulatorLogic.getPreviousWeekDailyCarCountRESERVE(day) / maxCarCount));
	        g.setColor(LIGHTBLUE);
	        g.fillRect(40 + 20 + day * 50, (int)(316 - (int)(300 * simulatorLogic.getPreviousWeekDailyCarCountPASS(day) / maxCarCount)), 10, (int)(300 * simulatorLogic.getPreviousWeekDailyCarCountPASS(day) / maxCarCount));
	        g.setColor(Color.RED);
	        g.fillRect(40 + day * 50, (int)(316 - (int)(300 * simulatorLogic.getDailyCarCountAD_HOC(day) / maxCarCount)), 10, (int)(300 * simulatorLogic.getDailyCarCountAD_HOC(day) / maxCarCount));
	        g.setColor(Color.GREEN);
	        g.fillRect(40 + 10 + day * 50, (int)(316 - (int)(300 * simulatorLogic.getDailyCarCountRESERVE(day) / maxCarCount)), 10, (int)(300 * simulatorLogic.getDailyCarCountRESERVE(day) / maxCarCount));
	        g.setColor(Color.BLUE);
	        g.fillRect(40 + 20 + day * 50, (int)(316 - (int)(300 * simulatorLogic.getDailyCarCountPASS(day) / maxCarCount)), 10, (int)(300 * simulatorLogic.getDailyCarCountPASS(day) / maxCarCount));
	        g.setColor(Color.BLACK);
    	}
        g.setColor(Color.BLACK);
        g.drawRect(40, 15, 330, 300);
        g.drawString("Aantal gearriveerde autos per type per dag", 40, 10);
        g.drawString("" + maxCarCount, (int)Math.log10(maxCarCount) * -7 + 30, 19);
        g.drawString("" + (int)(maxCarCount * 0.75), (int)Math.log10(maxCarCount * 0.75) * -7 + 30, 93);
        g.drawString("" + (int)(maxCarCount * 0.5), (int)Math.log10(maxCarCount * 0.5) * -7 + 30, 168);
        g.drawString("" + (int)(maxCarCount * 0.25), (int)Math.log10(maxCarCount * 0.25) * -7 + 30, 242);
        g.drawString("0", 30, 315);
        Graphics2D g2 = (Graphics2D) g;
        g.translate(45, 325);
        String dayString;
        for(int x = 0; x < 7; x++) {
        	switch(x) {
	        	case 0: dayString = "Maandag";
	        		break;
	        	case 1: dayString = "Dinsdag";
	        		break;
	        	case 2: dayString = "Woensdag";
	        		break;
	        	case 3: dayString = "Donderdag";
	        		break;
	        	case 4: dayString = "Vrijdag";
	        		break;
	        	case 5: dayString = "Zaterdag";
	        		break;
	        	case 6: dayString = "Zondag";
	        		break;
	        	default: dayString = "######";
        	}
        	g2.rotate(0.25 * Math.PI);
        	g.drawString(dayString, 0, 0);
        	g2.rotate(1.75 * Math.PI);
        	g.translate(50, 0);
        }
        g.translate(-395, -325);
        
    }
	
	public void updateView() {
        if (!size.equals(getSize())) {
            size = getSize();
            barChartImage = createImage(size.width, size.height);
        }		
        repaint();
	}
}
