package Parkeersimulator.Model;

import java.util.LinkedList;
import java.util.Queue;

public class CarQueue {
    private Queue<Car> queue = new LinkedList<>();

    public boolean addCar(Car car) {
    	if(queue.size() < 10) {
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
}
