package Parkeersimulator.Model;

import Parkeersimulator.Model.Car;

import java.util.Random;
import java.awt.*;

/**
 * Een auto die gereserveerd heeft
 * @author Andy Perukel, Ramon kits
 * @version 01-03-2019
 *
 */
public class ReservedCar extends Car {
	private static final Color COLOR=Color.green;
	
    /**
     * Contructor voor de gereserveerde auto
     */
	public ReservedCar() {
    	Random random = new Random();
    	int stayMinutes = (int) (60 + random.nextFloat() * 2 * 60);
        this.setMinutesLeft(stayMinutes);
        this.setMinutesBetalenRes(stayMinutes);
        this.setHasToPay(true);
        this.setCanUsePassPlaces(false);
        this.setCarReserved(true);
    }
    
    /**
     * Geeft de kleur terug van de gereserveerde auto
     * @return COLOR, de kleur die bij een gereserveerde auto hoort
     */
    public Color getColor(){
    	return COLOR;
    }
}
