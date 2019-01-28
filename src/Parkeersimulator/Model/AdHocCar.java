package Parkeersimulator.Model;

import java.util.Random;
import java.awt.*;

public class AdHocCar extends Car {
	private static final Color COLOR=Color.red;
	
    public AdHocCar() {
    	Random random = new Random();
    	int stayMinutes = (int) (15 + random.nextFloat() * 3 * 60);
        this.setMinutesLeft(stayMinutes);
        this.setMinutesBetalen(stayMinutes);
        this.setHasToPay(true);
        this.setCanUsePassPlaces(false);
        this.setCarReserved(false);
    }
    
    public Color getColor(){
    	return COLOR;
    }
}
