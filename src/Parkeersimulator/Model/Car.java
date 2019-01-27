package Parkeersimulator.Model;

import java.awt.*;

public abstract class Car {

    private Location location;
    private int minutesLeft;
    private boolean isPaying;
    private boolean hasToPay;
    private boolean canUsePassPlaces;

    /**
     * Constructor for objects of class Car
     */
    public Car() {

    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getMinutesLeft() {
        return minutesLeft;
    }

    public void setMinutesLeft(int minutesLeft) {
        this.minutesLeft = minutesLeft;
    }
    
    public boolean getIsPaying() {
        return isPaying;
    }

    public void setIsPaying(boolean isPaying) {
        this.isPaying = isPaying;
    }

    public boolean getHasToPay() {
        return hasToPay;
    }

    public void setHasToPay(boolean hasToPay) {
        this.hasToPay = hasToPay;
    }
    
    public void setCanUsePassPlaces(boolean canUsePassPlaces) {
    	this.canUsePassPlaces = canUsePassPlaces;
    }
    
    public boolean getCanUsePassPlaces() {
    	return canUsePassPlaces;
    }

    public void tick() {
        minutesLeft--;
    }
    
    public abstract Color getColor();
}