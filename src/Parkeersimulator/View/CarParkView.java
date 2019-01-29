package Parkeersimulator.View;

import Parkeersimulator.Model.SimulatorLogic;

import java.awt.*;
import java.text.DecimalFormat;
import Parkeersimulator.Model.Car;
import Parkeersimulator.Model.Location;

public class CarParkView extends AbstractView {
	
	private Dimension size;
	private Image carParkImage;
    private int numberOfFloors;
    private int numberOfRows;
    private int numberOfPlaces;
    private int heightDevByPlaces;
    private int simulatorWidth = 710;
    private int simulatorHeight = 300;
    
    public CarParkView(SimulatorLogic simulatorLogic) {
		super(simulatorLogic);
		size = new Dimension(0, 0);
	}

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
        		(int)((float)simulatorWidth / 685 * (location.getFloor() * 720 / numberOfFloors
        				+ location.getRow() / 2 * 1260 / numberOfRows / numberOfFloors
        				+ location.getRow() % 2 * 330 / numberOfRows / numberOfFloors)),
                (location.getPlace() * heightDevByPlaces),
                (int)((float)simulatorWidth / 685) * ((330 / numberOfRows) / numberOfFloors - 1),
                heightDevByPlaces - 1);
    }
}