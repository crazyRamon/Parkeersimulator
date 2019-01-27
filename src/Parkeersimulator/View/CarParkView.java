package Parkeersimulator.View;

import Parkeersimulator.Model.Car;
import Parkeersimulator.Model.CarQueue;
import Parkeersimulator.Model.Location;
import Parkeersimulator.Model.SimulatorLogic;

import java.awt.*;

public class CarParkView extends AbstractView {

    private Dimension size;
    private Image carParkImage;
    private SimulatorLogic simulatorLogic;
    private CarQueue carQueue;
    private int maxCarCount = 1;
    private String maxCarCountString;
    private int barGraphNumberOffset;

    /**
     * Constructor for objects of class CarPark
     */
    public CarParkView(SimulatorLogic simulatorLogic) {
        this.simulatorLogic = simulatorLogic;
        simulatorLogic.addView(this);
        size = new Dimension(0, 0);
    }

    /**
     * Overridden. Tell the GUI manager how big we would like to be.
     */
    public Dimension getPreferredSize() {
        return new Dimension(1600, 900);
    }

    /**
     * Overriden. The car park view component needs to be redisplayed. Copy the
     * internal image to screen.
     */
    public void paintComponent(Graphics g) {
        if (carParkImage == null) {
            return;
        }

        Dimension currentSize = getSize();
        if (size.equals(currentSize)) {
            g.drawImage(carParkImage, 0, 0, null);
        }
        else {
            // Rescale the previous image
            g.drawImage(carParkImage, 0, 0, currentSize.width, currentSize.height, null);
            };
        if(simulatorLogic.getHour() < 10) {
        	String hour = "" + 0 + simulatorLogic.getHour();
        }
        //graphLocX en graphLocY geven de locatie voor de staafdiafgram weer
        int graphLocX = 850;
        int graphLocY = 60;
        int spots = simulatorLogic.getScreenLogic().getNumberOfSpots();
        //Tijd en dag weergeven
        g.drawString(simulatorLogic.getDayWord() + " " + simulatorLogic.getTime() + " uur", 20, 20);
        //begin staafdiagram
        
        maxCarCount = simulatorLogic.getMaxCarCount();
        maxCarCountString = "" + maxCarCount;
        barGraphNumberOffset = maxCarCountString.length() * 6 + 10;
        g.drawRect(graphLocX - 1,  graphLocY - 1, 331, 301);
        g.setColor(Color.WHITE);
        g.fillRect(graphLocX,  graphLocY, 330, 300);
        for(int day = 0; day < 7; day++) {
	        g.setColor(Color.RED);
	        g.fillRect(graphLocX + day * 50, (int)(graphLocY + 300 - (int)(300 * simulatorLogic.getDailyCarCountAD_HOC(day) / maxCarCount)), 10, (int)(300 * simulatorLogic.getDailyCarCountAD_HOC(day) / maxCarCount));
	        g.setColor(Color.GREEN);
	        g.fillRect(graphLocX + 10 + day * 50, (int)(graphLocY + 300 - (int)(300 * simulatorLogic.getDailyCarCountRESERVE(day) / maxCarCount)), 10, (int)(300 * simulatorLogic.getDailyCarCountRESERVE(day) / maxCarCount));
	        g.setColor(Color.BLUE);
	        g.fillRect(graphLocX + 20 + day * 50, (int)(graphLocY + 300 - (int)(300 * simulatorLogic.getDailyCarCountPASS(day) / maxCarCount)), 10, (int)(300 * simulatorLogic.getDailyCarCountPASS(day) / maxCarCount));
	        g.setColor(Color.ORANGE);
	        g.fillRect(graphLocX + 30 + day * 50, (int)(graphLocY + 300 - (int)(300 * simulatorLogic.getDailyPassingCars(day) / maxCarCount)), 10, (int)(300 * simulatorLogic.getDailyPassingCars(day) / maxCarCount));
	        g.setColor(Color.BLACK);
    	}
        g.drawString("Aantal gearriveerde autos per type per dag", graphLocX, graphLocY - 10);
        g.drawString("" + maxCarCount, graphLocX - barGraphNumberOffset, graphLocY + 10);
        //eind staafdiagram
        g.drawString("Aantal simulatie minuten per seconde: " + (1000 / simulatorLogic.getTickPause()) + " minuten", 870, 912);
     // pieview
        int pieAdHocCars=(int) (simulatorLogic.getAmountOfAD_HOC() / 1.5);
		int piePassCars=(int) (simulatorLogic.getAmountOfPASS() / 1.5);
		int pieReservedCars=(int) (simulatorLogic.getAmountOfRESERVE() / 1.5);	
		
		
		int pieLocX = 30;
		int pieLocY = 540;
		int pieLocXcolor = pieLocX + 10;
		
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(pieLocX, pieLocY-50, 200, 250);
		g.setColor(Color.BLACK);
		g.drawString("Pie Chart", pieLocX + 20, pieLocY - 30);
		g.setColor(Color.BLACK);
		g.drawRect(pieLocX, pieLocY-50, 200, 250);
		g.setColor(Color.WHITE);
		g.fillArc(pieLocXcolor, pieLocY, 180, 180, 0, 360);
		g.setColor(Color.RED);
		g.fillArc(pieLocXcolor, pieLocY, 180, 180, 0, pieAdHocCars);
		g.setColor(Color.BLUE);
		g.fillArc(pieLocXcolor, pieLocY, 180, 180, pieAdHocCars, piePassCars);
		g.setColor(Color.GREEN);
		g.fillArc(pieLocXcolor, pieLocY, 180, 180, pieAdHocCars + piePassCars, pieReservedCars);
		
        //queue balk
        g.setColor(Color.WHITE);
        g.fillRect(75, 370, 710, 80);
        g.setColor(Color.BLACK);
        float percentageEntranceCarQueue = (float)simulatorLogic.getCarsInEntranceCarQueue() / 10 * 710;
        float percentageEntrancePassQueue = (float)simulatorLogic.getCarsInEntrancePassQueue() / 10 * 710;
        float percentagePaymentCarQueue = (float)simulatorLogic.getCarsInPaymentCarQueue() / 10 * 710;
        float percentageExitCarQueue = (float)simulatorLogic.getCarsInExitCarQueue() / 10 * 710;
        g.setColor(Color.BLACK);
        g.fillRect(75, 370, (int)percentageEntranceCarQueue, 20);
        g.fillRect(75, 390, (int)percentageEntrancePassQueue, 20);
        g.fillRect(75, 410, (int)percentagePaymentCarQueue, 20);
        g.fillRect(75, 430, (int)percentageExitCarQueue, 20);
        g.setColor(Color.RED);
        g.drawString("EntranceCarQueue", 80, 385);
        g.drawString("EntrancePassQueue", 80, 405);
        g.drawString("PaymentCarQueue", 80, 425);
        g.drawString("ExitCarQueue", 80, 445);
        g.drawString(simulatorLogic.getDailyPassingCars(simulatorLogic.getDay()) + "", 80, 465);
    }

    public void updateView() {
        // Create a new car park image if the size has changed.
        if (!size.equals(getSize())) {
            size = getSize();
            carParkImage = createImage(size.width, size.height);
        }
        Graphics graphics = carParkImage.getGraphics();
        if(simulatorLogic.getReset() == false) {
	        for(int floor = 0; floor < simulatorLogic.getScreenLogic().getNumberOfFloors(); floor++) {
	        	for(int row = simulatorLogic.getScreenLogic().getNumberOfPassRows(); row < simulatorLogic.getScreenLogic().getNumberOfNormalRows(); row++) {
	                for(int place = 0; place < simulatorLogic.getScreenLogic().getNumberOfPlaces(); place++) {
	                    Location location = new Location(false, floor, row, place);
	                    Car car = simulatorLogic.getScreenLogic().getCarAt(location);
	                    Color color = car == null ? Color.WHITE : car.getColor();
	                    drawPlace(graphics, location, color);
	                }
	            }
	        	for(int passRow = 0; passRow < simulatorLogic.getScreenLogic().getNumberOfPassRows(); passRow++) {
	        		for(int place = 0; place < simulatorLogic.getScreenLogic().getNumberOfPlaces(); place++) {
	                    Location location = new Location(true, floor, passRow, place);
	                    Car car = simulatorLogic.getScreenLogic().getCarAt(location);
	                    Color color = car == null ? Color.CYAN : car.getColor();
	                    drawPlace(graphics, location, color);
	                    
	                }
	        	}
	        }
        } else {
        	for(int floor = 0; floor < simulatorLogic.getScreenLogic().getNumberOfFloors(); floor++) {
		            for(int row = 0; row < simulatorLogic.getScreenLogic().getNumberOfNormalRows(); row++) {
		                for(int place = 0; place < simulatorLogic.getScreenLogic().getNumberOfPlaces(); place++) {
		                	Location location = new Location(false, floor, row, place);
		                    Car car = simulatorLogic.getScreenLogic().getCarAt(location);
		                    simulatorLogic.getScreenLogic().removeCarAt(location);
		                    Color color = Color.white;
		                    drawPlace(graphics, location, color);
		                }
		            }
		            for(int passRow = 0; passRow < simulatorLogic.getScreenLogic().getNumberOfPassRows(); passRow++) {
		        		for(int place = 0; place < simulatorLogic.getScreenLogic().getNumberOfPlaces(); place++) {
		                	Location location = new Location(true, floor, passRow, place);
		                    Car car = simulatorLogic.getScreenLogic().getCarAt(location);
		                    simulatorLogic.getScreenLogic().removeCarAt(location);
		                    Color color = Color.white;
		                    drawPlace(graphics, location, color);
		                }
		            }
	        }
        	simulatorLogic.setReset(false); 
        }
        repaint();
    }

    /**
     * Paint a place on this car park view in a given color.
     */
    private void drawPlace(Graphics graphics, Location location, Color color) {
        graphics.setColor(color);
        graphics.fillRect(
                location.getFloor() * 260 + (1 + (int)Math.floor(location.getRow() * 0.5)) * 75 + (location.getRow() % 2) * 20,
                60 + location.getPlace() * 10,
                20 - 1,
                10 - 1); // TODO use dynamic size or constants
    }
}