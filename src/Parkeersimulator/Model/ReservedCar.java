package Parkeersimulator.Model;

import Parkeersimulator.Model.Car;

import java.util.Random;
import java.awt.*;

public class ReservedCar extends Car {
	private static final Color COLOR=Color.green;
	
    public ReservedCar() {
    	Random random = new Random();
    	int stayMinutes = (int) (60 + random.nextFloat() * 2 * 60);
        this.setMinutesLeft(stayMinutes);
        this.setHasToPay(false);
        this.setCanUsePassPlaces(false);
    }
    
    public Color getColor(){
    	return COLOR;
    }
}
