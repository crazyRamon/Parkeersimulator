package Parkeersimulator.View;

import Parkeersimulator.Model.Car;
import Parkeersimulator.Model.Location;
import Parkeersimulator.Model.SimulatorLogic;

import java.awt.*;

public class CarParkView extends AbstractView {

    private Dimension size;
    private Image carParkImage;
    private SimulatorLogic simulatorLogic;

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
        return new Dimension(800, 500);
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
            // Rescale the previous image.
            g.drawImage(carParkImage, 0, 0, currentSize.width, currentSize.height, null);
            };
        if(simulatorLogic.getHour() < 10) {
        	String hour = "" + 0 + simulatorLogic.getHour();
        }
        g.drawString(simulatorLogic.getDayWord() + " " + simulatorLogic.getTime() + " uur", 20, 20);
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
	            for(int row = 0; row < simulatorLogic.getScreenLogic().getNumberOfRows(); row++) {
	                for(int place = 0; place < simulatorLogic.getScreenLogic().getNumberOfPlaces(); place++) {
	                    Location location = new Location(floor, row, place);
	                    Car car = simulatorLogic.getScreenLogic().getCarAt(location);
	                    Color color = car == null ? Color.white : car.getColor();
	                    drawPlace(graphics, location, color);
	                }
	            }
	        }
        } else {
        	for(int floor = 0; floor < simulatorLogic.getScreenLogic().getNumberOfFloors(); floor++) {
	            for(int row = 0; row < simulatorLogic.getScreenLogic().getNumberOfRows(); row++) {
	                for(int place = 0; place < simulatorLogic.getScreenLogic().getNumberOfPlaces(); place++) {
	                    Location location = new Location(floor, row, place);
	                    Car car = simulatorLogic.getScreenLogic().removeCarAt(location);
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