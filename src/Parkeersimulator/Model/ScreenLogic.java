package Parkeersimulator.Model;

/**
 * Een klasse voor de parkeergarage op het scherm
 * @author Andy Perukel, Ramon kits
 * @version 01-03-2019
 *
 */
public class ScreenLogic {
	private int numberOfPassRows;
    private int numberOfFloors;
    private int numberOfTotalRows;
    private int numberOfPlaces;
    private int numberOfOpenSpots;
    private Car[][][] cars;

    /**
     * De constructor voor ScreenLogic
     * @param numberOfPassRows, Het aantal rijen voor abonnementhouders
     * @param numberOfFloors, het aantal verdiepingen
     * @param numberOfTotalRows, het totaal aantal rijen
     * @param numberOfPlaces, het aantal plekken
     */
    public ScreenLogic(int numberOfPassRows, int numberOfFloors, int numberOfTotalRows, int numberOfPlaces) {
        this.numberOfFloors = numberOfFloors;
    	this.numberOfPassRows = numberOfPassRows;
        this.numberOfTotalRows = numberOfTotalRows;
        this.numberOfPlaces = numberOfPlaces;
        this.numberOfOpenSpots = numberOfFloors*numberOfTotalRows*numberOfPlaces;
        cars = new Car[numberOfFloors][numberOfTotalRows][numberOfPlaces];
    }
    
    /**
     * Het aantal verdiepingen
     * @return numberOfFloors, het aantal verdiepingen
     */
	public int getNumberOfFloors() {
        return numberOfFloors;
    }

	/**
	 * Het aantal rijden voor abonnementhouders
	 * @return numberofPassRows, het aantal rijen voor abonnementhouders
	 */
    public int getNumberOfPassRows() {
        return numberOfPassRows;
    }

    /**
     * Het aantal normale rijen
     * @return numberOfTotalRows, het aantal rijen
     */
    public int getNumberOfTotalRows() {
        return numberOfTotalRows;
    }

    /**
     * Het aantal plekken
     * @return numberOfPlaces, het aantal plekken per rij
     */
    public int getNumberOfPlaces() {
        return numberOfPlaces;
    }

    /**
     * Het aantal lege plekken
     * @return numberOfOpenSpots, het aantal lege plekken
     */
    public int getNumberOfOpenSpots(){
    	return numberOfOpenSpots;
    }
    
    /**
     * het aantal plekken
     * @return het aantal plekken
     */
    public int getNumberOfSpots(){
    	return numberOfFloors*numberOfTotalRows*numberOfPlaces;
    }
    
    /**
     * Haalt de auto van een specifieke locatie
     * @param location, de locatie
     * @return cars, de auto met die verdieping, rij en plek
     */
    public Car getCarAt(Location location) {
        if (!locationIsValid(location)) {
            return null;
        }
        return cars[location.getFloor()][location.getRow()][location.getPlace()];
    }

    /**
     * Set een auto op een specifieke locatie
     * @param location, de locatie
     * @param car, de auto
     * @return true als de auto is geplaatst
     */
    public boolean setCarAt(Location location, Car car) {
        if (!locationIsValid(location)) {
            return false;
        }
        Car oldCar = getCarAt(location);
        if (oldCar == null) {
            cars[location.getFloor()][location.getRow()][location.getPlace()] = car;
            car.setLocation(location);
            numberOfOpenSpots--;
            return true;
        }
        return false;
    }

    /**
     * Verwijderd een auto van een specifieke locatie
     * @param location, de locatie
     * @return car, de auto die wordt verwijderd
     */
    public Car removeCarAt(Location location) {
        if (!locationIsValid(location)) {
            return null;
        }
        Car car = getCarAt(location);
        if (car == null) {
            return null;
        }
        cars[location.getFloor()][location.getRow()][location.getPlace()] = null;
        car.setLocation(null);
        numberOfOpenSpots++;
        return car;
    }

    /**
     * Geeft de eerste lege plek in de garage
     * @return location, de lege plek
     */
    public Location getFirstFreeLocation() {
        for (int floor = 0; floor < getNumberOfFloors(); floor++) {
            for (int row = getNumberOfPassRows(); row < getNumberOfTotalRows(); row++) {
                for (int place = 0; place < getNumberOfPlaces(); place++) {
                    Location location = new Location(false, floor, row, place);
                    if (getCarAt(location) == null) {
                        return location;
                    }
                }
            }
        }
        return null;
    }
    
    /**
     * Geeft de eerste lege plek voor abonnement houders in de garage
     * @return location, de lege plek
     */
    public Location getFirstFreePassLocation() {
        for (int floor = 0; floor < getNumberOfFloors(); floor++) {
            for (int row = 0; row < getNumberOfPassRows(); row++) {
                for (int place = 0; place < getNumberOfPlaces(); place++) {
                    Location location = new Location(true, floor, row, place);
                    if (getCarAt(location) == null) {
                        return location;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Geeft een auto die weggaat
     * @return car, de auto die weggaat
     */
    public Car getFirstLeavingCar() {
        for (int floor = 0; floor < getNumberOfFloors(); floor++) {
            for (int row = getNumberOfPassRows(); row < getNumberOfTotalRows(); row++) {
                for (int place = 0; place < getNumberOfPlaces(); place++) {
                    Location location = new Location(false, floor, row, place);
                    Car car = getCarAt(location);
                    if (car != null && car.getMinutesLeft() <= 0 && !car.getIsPaying()) {
                        return car;
                    }
                }
            }
            for (int row = 0; row < getNumberOfPassRows(); row++) {
                for (int place = 0; place < getNumberOfPlaces(); place++) {
                    Location location = new Location(true, floor, row, place);
                    Car car = getCarAt(location);
                    if (car != null && car.getMinutesLeft() <= 0 && !car.getIsPaying()) {
                        return car;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Gaat langs elke geparkeerde auto en zorgt ervoor dat de auto's een tick krijgen
     */
    public void tick() {
        for (int floor = 0; floor < getNumberOfFloors(); floor++) {
            for (int row = getNumberOfPassRows(); row < getNumberOfTotalRows(); row++) {
                for (int place = 0; place < getNumberOfPlaces(); place++) {
                    Location location = new Location(false, floor, row, place);
                    Car car = getCarAt(location);
                    if (car != null) {
                        car.tick();
                    }
                }
            }
            for (int row = 0; row < getNumberOfPassRows(); row++) {
                for (int place = 0; place < getNumberOfPlaces(); place++) {
                    Location location = new Location(true, floor, row, place);
                    Car car = getCarAt(location);
                    if (car != null) {
                        car.tick();
                    }
                }
            }
        }
    }

    /**
     * Kijkt of een locatie goed is
     * @param location, de locatie die goed is
     * @return true als er een locatie is
     */
    private boolean locationIsValid(Location location) {
        int floor = location.getFloor();
        int row = location.getRow();
        int place = location.getPlace();
        if (floor < 0 || floor >= numberOfFloors || row < 0 || row > numberOfTotalRows || place < 0 || place > numberOfPlaces) {
            return false;
        }
        return true;
    }
}
