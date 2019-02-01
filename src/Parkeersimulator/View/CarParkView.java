package Parkeersimulator.View;

import Parkeersimulator.Model.SimulatorLogic;

import java.awt.*;
import java.text.DecimalFormat;
import Parkeersimulator.Model.Car;
import Parkeersimulator.Model.Location;

/**
 * Een view van de parkeergarage
 * @author Andy Perukel, Ramon kits
 * @version 01-03-2019
 *
 */
public class CarParkView extends AbstractView {
	
	private Dimension size;
	private Image carParkImage;
    private int numberOfFloors;
    private int numberOfRows;
    private int numberOfPlaces;
    private int heightDevByPlaces;
    private int simulatorWidth = 710;
    private int simulatorHeight = 280;
    
    /**
     * De constructor voor de view van de parkeergarage
     * @param simulatorLogic, de simulator
     */
    public CarParkView(SimulatorLogic simulatorLogic) {
		super(simulatorLogic);
		size = new Dimension(0, 0);
	}

    /**
     * @param g, een grafisch opject
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
            // Rescale the previous image.
            g.drawImage(carParkImage, 0, 0, currentSize.width, currentSize.height, null);
        }
            	
    }
    
    /**
     * Update de view
     */
    public void updateView() {
        // Create a new car park image if the size has changed.
        if (!size.equals(getSize())) {
            size = getSize();
            carParkImage = createImage(size.width, size.height);
        }
        
        Graphics graphics = carParkImage.getGraphics();
        
        graphics.translate(10, 10);
        if(simulatorLogic.getReset() == false) {
	        for(int floor = 0; floor < simulatorLogic.getScreenLogic().getNumberOfFloors(); floor++) {
	        	for(int row = simulatorLogic.getScreenLogic().getNumberOfPassRows(); row < simulatorLogic.getScreenLogic().getNumberOfTotalRows(); row++) {
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
	            for(int row = 0; row < simulatorLogic.getScreenLogic().getNumberOfTotalRows(); row++) {
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
        if(simulatorLogic.getTotalAmountOfCars() > 0) {
	        graphics.setColor(Color.WHITE);
	        float carPercentage = (float)simulatorLogic.getTotalAmountOfCars() / simulatorLogic.getScreenLogic().getNumberOfSpots();
	        graphics.fillRect((int)(735 * carPercentage), 280, 735 - (int)(735 * carPercentage), 30);
	        graphics.setColor(new Color((int)(carPercentage * 255), 255 - (int)(carPercentage * 255), 0));
			graphics.fillRect(0, 280, (int)(735 * carPercentage), 30);
	        graphics.setColor(Color.BLACK);
	        graphics.setFont(standard20px);
			graphics.drawString(new DecimalFormat("0.00").format(carPercentage * 100) + "%", 338, 302);
        } else {
	        graphics.setColor(Color.WHITE);
	        graphics.fillRect(0, 280, 735, 30);
	        graphics.setColor(Color.BLACK);
	        graphics.setFont(standard20px);
        	graphics.drawString("0,00%", 338, 302);
        }
        repaint();
    }
    
    /**
     * Paint a place on this car park view in a given color.
     * @param Graphics, een grafisch object
     * @param location, de locatie
     * @param color, de kleur
     */
    private void drawPlace(Graphics graphics, Location location, Color color) {
    	numberOfFloors = simulatorLogic.getScreenLogic().getNumberOfFloors();
    	numberOfRows = simulatorLogic.getScreenLogic().getNumberOfTotalRows();
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
        		(int)((float)simulatorWidth / 685 * (location.getFloor() * 720 / numberOfFloors
        				+ location.getRow() / 2 * 1260 / numberOfRows / numberOfFloors
        				+ location.getRow() % 2 * 330 / numberOfRows / numberOfFloors)),
                (location.getPlace() * heightDevByPlaces),
                (int)((float)simulatorWidth / 685) * ((330 / numberOfRows) / numberOfFloors - 1),
                heightDevByPlaces - 1);
    }
}