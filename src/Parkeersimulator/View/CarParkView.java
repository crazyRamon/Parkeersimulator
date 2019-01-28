package Parkeersimulator.View;

import Parkeersimulator.Model.Car;
import Parkeersimulator.Model.CarQueue;
import Parkeersimulator.Model.Location;
import Parkeersimulator.Model.SimulatorLogic;

import java.awt.*;

public class CarParkView extends AbstractView {
	
	public static final Color LIGHTRED = new Color(255, 200, 200);
	public static final Color LIGHTGREEN = new Color(200, 255, 200);
	public static final Color LIGHTBLUE = new Color(200, 200, 255);

    private Dimension size;
    private Image carParkImage;
    private SimulatorLogic simulatorLogic;
    private CarQueue carQueue;
    private int maxCarCount = 1;
    private int numberOfFloors;
    private int numberOfRows;
    private int numberOfPlaces;
    private int heightDevByPlaces;
    
    private int simulatorLocX = 75;
    private int simulatorLocY = 60;
    private int simulatorWidth = 710;
    private int simulatorHeight = 300;
    
    private int parkPrice;
    private int profit;
    private int expectedProfit;
    

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
        g.setColor(Color.BLACK);
        g.drawRect(graphLocX - 1,  graphLocY - 1, 331, 301);
        g.setColor(Color.WHITE);
        g.fillRect(graphLocX,  graphLocY, 330, 300);
        g.setColor(Color.LIGHT_GRAY);
        g.drawLine(graphLocX, graphLocY + 75, graphLocX + 330, graphLocY + 75);
        g.drawLine(graphLocX, graphLocY + 150, graphLocX + 330, graphLocY + 150);
        g.drawLine(graphLocX, graphLocY + 225, graphLocX + 330, graphLocY + 225);
        for(int day = 0; day < 7; day++) {
        	g.setColor(LIGHTRED);
	        g.fillRect(graphLocX + day * 50, (int)(graphLocY + 300 - (int)(300 * simulatorLogic.getPreviousWeekDailyCarCountAD_HOC(day) / maxCarCount)), 10, (int)(300 * simulatorLogic.getPreviousWeekDailyCarCountAD_HOC(day) / maxCarCount));
	        g.setColor(LIGHTGREEN);
	        g.fillRect(graphLocX + 10 + day * 50, (int)(graphLocY + 300 - (int)(300 * simulatorLogic.getPreviousWeekDailyCarCountRESERVE(day) / maxCarCount)), 10, (int)(300 * simulatorLogic.getPreviousWeekDailyCarCountRESERVE(day) / maxCarCount));
	        g.setColor(LIGHTBLUE);
	        g.fillRect(graphLocX + 20 + day * 50, (int)(graphLocY + 300 - (int)(300 * simulatorLogic.getPreviousWeekDailyCarCountPASS(day) / maxCarCount)), 10, (int)(300 * simulatorLogic.getPreviousWeekDailyCarCountPASS(day) / maxCarCount));
	        g.setColor(Color.RED);
	        g.fillRect(graphLocX + day * 50, (int)(graphLocY + 300 - (int)(300 * simulatorLogic.getDailyCarCountAD_HOC(day) / maxCarCount)), 10, (int)(300 * simulatorLogic.getDailyCarCountAD_HOC(day) / maxCarCount));
	        g.setColor(Color.GREEN);
	        g.fillRect(graphLocX + 10 + day * 50, (int)(graphLocY + 300 - (int)(300 * simulatorLogic.getDailyCarCountRESERVE(day) / maxCarCount)), 10, (int)(300 * simulatorLogic.getDailyCarCountRESERVE(day) / maxCarCount));
	        g.setColor(Color.BLUE);
	        g.fillRect(graphLocX + 20 + day * 50, (int)(graphLocY + 300 - (int)(300 * simulatorLogic.getDailyCarCountPASS(day) / maxCarCount)), 10, (int)(300 * simulatorLogic.getDailyCarCountPASS(day) / maxCarCount));
	        g.setColor(Color.BLACK);
    	}
        g.drawString("Aantal gearriveerde autos per type per dag", graphLocX, graphLocY - 10);
        g.drawString("" + maxCarCount, graphLocX - (int)(Math.log10(maxCarCount) * 7 + 10), graphLocY + 6);
        g.drawString("" + (int)(maxCarCount * 0.75), graphLocX - ((int)Math.log10(maxCarCount * 0.75) * 7 + 10), graphLocY + 80);
        g.drawString("" + (int)(maxCarCount * 0.5), graphLocX - ((int)Math.log10(maxCarCount * 0.5) * 7 + 10), graphLocY + 155);
        g.drawString("" + (int)(maxCarCount * 0.25), graphLocX - ((int)Math.log10(maxCarCount * 0.25) * 7 + 10), graphLocY + 229);
        g.drawString("0", graphLocX - 10, graphLocY + 302);
        //eind staafdiagram
        g.drawString("Aantal simulatie minuten per seconde: " + (1000 / simulatorLogic.getTickPause()) + " minuten", 870, 912);
        // pieview
        int pieAdHocCars=simulatorLogic.getAmountOfAD_HOC();
		int piePassCars=simulatorLogic.getAmountOfPASS();
		int pieReservedCars=simulatorLogic.getAmountOfRESERVE();	
		
		
		int pieLocX = 330;
		int pieLocY = 540;
		int pieLocXcolor = pieLocX + 10;
		int totalSpots = simulatorLogic.getScreenLogic().getNumberOfSpots();
		
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(pieLocX, pieLocY-50, 200, 250);
		g.setColor(Color.BLACK);
		g.drawString("Pie Chart" + simulatorLogic.getScreenLogic().getNumberOfSpots(), pieLocX + 20, pieLocY - 30);
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
        
        // legenda  
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(30, 490, 200, 250);
        g.setColor(Color.BLACK);
        g.drawRect(30, 490, 200, 250);
        g.drawString("Normale vakken", 90, 520);
        g.setColor(Color.RED);
        g.fillRect(50, 510, 25, 10);
        g.setColor(Color.BLACK);
        g.drawRect(50, 510, 25, 10);
        g.drawString("Abbonement vakken", 90, 550);
        g.setColor(Color.BLUE);
        g.fillRect(50, 540, 25, 10);
        g.setColor(Color.BLACK);
        g.drawRect(50, 540, 25, 10);
        g.drawString("Reserveer vakken", 90, 580);
        g.setColor(Color.GREEN);
        g.fillRect(50, 570, 25, 10);
        g.setColor(Color.BLACK);
        g.drawRect(50, 570, 25, 10);
        g.drawString("Lege vakken", 90, 610);
        g.setColor(Color.WHITE);
        g.fillRect(50, 600, 25, 10);
        g.setColor(Color.BLACK);
        g.drawRect(50, 600, 25, 10);
        
        // betalen
        this.parkPrice = 10;
        int profitCar = simulatorLogic.getProfitCar();
        int profitRes = simulatorLogic.getProfitReserved();
        int profitTot = profitCar + profitRes;
        g.drawString("Winst normale auto's: " + profitCar, 50, 640);
        g.drawString("Winst gereserveerde auto's: " + profitRes, 50, 660);
        g.drawString("Totale winst: " + profitTot, 50, 680);  
        g.drawString("Misgelopen winst: ", 50, 700);
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
	                    Color color = car == null ? LIGHTBLUE : car.getColor();
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
	                    Color color = Color.WHITE;
	                    drawPlace(graphics, location, color);
	                }
	            }
	            for(int passRow = 0; passRow < simulatorLogic.getScreenLogic().getNumberOfPassRows(); passRow++) {
	        		for(int place = 0; place < simulatorLogic.getScreenLogic().getNumberOfPlaces(); place++) {
	                	Location location = new Location(true, floor, passRow, place);
	                    Car car = simulatorLogic.getScreenLogic().getCarAt(location);
	                    simulatorLogic.getScreenLogic().removeCarAt(location);
	                    Color color = LIGHTBLUE;
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
    	numberOfFloors = simulatorLogic.getScreenLogic().getNumberOfFloors();
    	numberOfRows = simulatorLogic.getScreenLogic().getNumberOfNormalRows();
    	numberOfPlaces = simulatorLogic.getScreenLogic().getNumberOfPlaces();
    	if(numberOfFloors < 3) {
    		numberOfFloors = 3;
    	}
    	if(numberOfPlaces < 30) {
    		heightDevByPlaces = 10;
    	} else {
    		heightDevByPlaces = simulatorHeight / numberOfPlaces;
    	}
        graphics.setColor(color);
        graphics.fillRect(
        		(int)(simulatorLocX + (float)simulatorWidth / 685 * (location.getFloor() * 720 / numberOfFloors - 1
        				+ location.getRow() / 2 * 1260 / numberOfRows / numberOfFloors
        				+ location.getRow() % 2 * 330 / numberOfRows / numberOfFloors)),
                simulatorLocY + (location.getPlace() * heightDevByPlaces),
                (int)((float)simulatorWidth / 685) * ((330 / numberOfRows) / numberOfFloors - 1),
                heightDevByPlaces - 1);
    }
}