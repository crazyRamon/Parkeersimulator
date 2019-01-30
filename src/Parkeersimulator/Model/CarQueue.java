package Parkeersimulator.Model;

import java.util.LinkedList;
import java.util.Queue;

public class CarQueue {
    private Queue<Car> queue = new LinkedList<>();
    
    private int maxCars = 0;

    public boolean addCar(Car car) {
    	if(maxCars == 0) {
    		return queue.add(car);
    	} else if(carsInQueue() < maxCars) {
    		return queue.add(car);
    	} else {
    		return false;
    	}
    }

    public Car removeCar() {
        return queue.poll();
    }

    public int carsInQueue(){
    	return queue.size();
    }
    
    public void clearQueue() {
    	queue.clear();
    }
    
    public void setMaxCars(int maxCars) {
    	this.maxCars = maxCars;
    }
}
