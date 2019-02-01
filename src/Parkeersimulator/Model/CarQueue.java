package Parkeersimulator.Model;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Een rij voor de auto's
 * 
 * @author Andy Perukel, Ramon kits
 * @version 01-03-2019
 *
 */
public class CarQueue {
    private Queue<Car> queue = new LinkedList<>();
    
    private int maxCars = 0;

    /**
     * Voegt een auto toe aan de rij
     * @param car, een auto om toe te voegen aan de rij
     * @return of de auto is toegevoegd of niet
     */
    public boolean addCar(Car car) {
    	if(maxCars == 0) {
    		return queue.add(car);
    	} else if(carsInQueue() < maxCars) {
    		return queue.add(car);
    	} else {
    		return false;
    	}
    }

    /**
     * Verwijderd een auto van de rij
     * @return Car de verwijderde auto
     */
    public Car removeCar() {
        return queue.poll();
    }

    /**
     * Geeft het aantal auto's dat in de rij staat.
     * @return het aantal auto's
     */
    public int carsInQueue(){
    	return queue.size();
    }
    
    /**
     * Wordt gebruikt om de rij leeg te maken
     */
    public void clearQueue() {
    	queue.clear();
    }
    
    /**
     * Set het maximaal aantal auto's
     * @param maxCars, het aantal auto's
     */
    public void setMaxCars(int maxCars) {
    	this.maxCars = maxCars;
    }
}
