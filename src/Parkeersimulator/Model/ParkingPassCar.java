package Parkeersimulator.Model;

import Parkeersimulator.Model.Car;

import java.util.Random;
import java.awt.*;

/**
 * Een auto met een abonnement
 * @author Andy Perukel, Ramon kits
 * @version 01-03-2019
 *
 */
public class ParkingPassCar extends Car {
	private static final Color COLOR=Color.blue;
	
	/**
	 * Constructor voor de auto met abonnement
	 */
    public ParkingPassCar() {
    	Random random = new Random();
    	int stayMinutes = (int) (60 + random.nextFloat() * 7 * 60);
        this.setMinutesLeft(stayMinutes);
        this.setMinutesBetalen(stayMinutes);
        this.setHasToPay(false);
        this.setCanUsePassPlaces(true);
        this.setCarReserved(false);
    }
    
    /**
     * Geeft de kleur terug van een auto met abonnement
     * @return COLOR, de kleur die bij een abonnement hoort
     */
    public Color getColor(){
    	return COLOR;
    }
}