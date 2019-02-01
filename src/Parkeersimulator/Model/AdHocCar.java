package Parkeersimulator.Model;

import java.util.Random;
import java.awt.*;

/**
 * Klasse voor de normale auto
 * @author Andy Perukel, Ramon kits
 * @version 01-03-2019
 *
 */
public class AdHocCar extends Car {
	private static final Color COLOR=Color.red;
	
	/**
	 * Constructor voor een normale auto
	 */
    public AdHocCar() {
    	Random random = new Random();
    	int stayMinutes = (int) (15 + random.nextFloat() * 3 * 60);
        this.setMinutesLeft(stayMinutes);
        this.setMinutesBetalen(stayMinutes);
        this.setHasToPay(true);
        this.setCanUsePassPlaces(false);
        this.setCarReserved(false);
    }
    
    /**
     * Geeft de kleur van een normale auto
     * @return COLOR, de kleur van de auto
     */
    public Color getColor(){
    	return COLOR;
    }
}
