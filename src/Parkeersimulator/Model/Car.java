package Parkeersimulator.Model;

import java.awt.*;

/**
 * De klasse voor de auto's
 * @author Andy Perukel, Ramon kits
 * @version 01-03-2019
 *
 */
public abstract class Car {

    private Location location;
    private int minutesLeft;
    private boolean isPaying;
    private boolean hasToPay;
    private boolean canUsePassPlaces;
    private boolean carReserved;
    public double priceMinutes;
    public double priceMinutesRes;
    

    /**
     * Constructor voor de klasse Car.
     */
    public Car() {

    }

    /**
     * Geeft de locatie van de auto
     * @return location, de locatie van de auto
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Zet de locatie van de auto
     * @param location, de locatie van de auto
     */
    public void setLocation(Location location) {
        this.location = location;
    }
    
    /**
     * Geeft het aantal minuten dat over blijft voordat de auto de garage verlaat
     * @return minutesLeft, het aantal minuten 
     */
    public int getMinutesLeft() {
        return minutesLeft;
    }

    /**
     * Set het aantal minuten dat over blijft voordat de auto de garage verlaat
     * @param minutesLeft, het aantal minuten
     */
    public void setMinutesLeft(int minutesLeft) {
        this.minutesLeft = minutesLeft;
    }
    
    /**
     * Kijkt of een auto betaald
     * @return isPaying, of de auto betaald
     */
    public boolean getIsPaying() {
        return isPaying;
    }

    /**
     * Set dat een auto betaald
     * @param isPaying, of de auto betaald
     */
    public void setIsPaying(boolean isPaying) {
        this.isPaying = isPaying;
    }

    /**
     * Kijkt of een auto moet betalen
     * @return hasToPay, kijkt of een auto moet betalen
     */
    public boolean getHasToPay() {
        return hasToPay;
    }

    /**
     * Set of een auto moet betalen
     * @param hasToPay, kijkt of een auto moet betalen
     */
    public void setHasToPay(boolean hasToPay) {
        this.hasToPay = hasToPay;
    }
    
    /**
     * Set of een auto de parkeerplaatsen voor abonnementhouders mag gebruiken
     * @param canUsePassPlaces, of de auto de abonnementhouders parkeerplaats mag gebruiken.
     */ 
    public void setCanUsePassPlaces(boolean canUsePassPlaces) {
    	this.canUsePassPlaces = canUsePassPlaces;
    }
    
    /**
     * Kijkt of een auto de parkeerplaats voor abonnementhouders mag gebruiken
     * @return canUsePassPlaces, of de auto de abonnementhouders parkeerplaats mag gebruiken.
     */
    public boolean getCanUsePassPlaces() {
    	return canUsePassPlaces;
    }

    /**
     * Verminderd de tijd van de auto per minuut.
     */
    public void tick() {
        minutesLeft--;
    }
    
    
    public abstract Color getColor();
    
    /**
     * Kijkt of een auto een reserveer auto is.
     * @return carReserved, of het wel of niet een reserveer auto is.
     */
    public boolean getCarReserved() {
    	return carReserved;
    }
    
    /**
     * Set of een auto een reserveer auto is.
     * @param carReserved, of het wel of niet een reserveer auto is.
     */
    public void setCarReserved(boolean carReserved) {
    	this.carReserved = carReserved;
    }
    
    /**
     * Set de prijs per minuut die je moet betalen.
     * @param stayMinutes, het aantal minuten.
     */
    public void setMinutesBetalen(int stayMinutes) {
    	this.priceMinutes = stayMinutes * 0.05;
    }
    
    /**
     * Geeft de prijs per minuten die je moet betalen
     * @return priceMinutes, de prijs die je moet betalen.
     */
    public double getMinutesBetalen() {
    	return priceMinutes;
    }
    
    /**
     * Set de prijs per minuut die je moet betalen als reserveerder, voor een reserveerder komt er nog een extra bedrag bij.
     * @param stayMinutes, het aantal minuten.
     */
    public void setMinutesBetalenRes(int stayMinutes) {
    	this.priceMinutesRes = (stayMinutes * 0.05) + 10;
    }
    
    /**
     * Geeft de prijs per minuten die een reserveerder meot betalen.
     * @return priceMinutes, de prijs die je moet betalen.
     */
    public double getMinutesBetalenRes() {
    	return priceMinutesRes;
    }
    
}