package Parkeersimulator.Model;

import Parkeersimulator.View.AbstractView;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class SimulatorLogic extends AbstractModel implements Runnable{

	private static final String AD_HOC = "1";
	private static final String PASS = "2";
	private static final String RESERVE = "3";
	
	private Location locations;	
	
	private CarQueue entranceCarQueue;
    private CarQueue entrancePassQueue;
    private CarQueue paymentCarQueue;
    private CarQueue exitCarQueue;
    private ScreenLogic screenLogic;
    private boolean run;
    private boolean reset = false;

    private int day = 0;
    private int hour = 0;
    private int minute = 0;

    private int tickPause = 100;

    int weekDayArrivals= 100; // average number of arriving cars per hour
    int weekendArrivals = 200; // average number of arriving cars per hour
    int weekDayPassArrivals= 50; // average number of arriving cars per hour
    int weekendPassArrivals = 5; // average number of arriving cars per hour

    int enterSpeed = 3; // number of cars that can enter per minute
    int paymentSpeed = 7; // number of cars that can pay per minute
    int exitSpeed = 5; // number of cars that can leave per minute

    public SimulatorLogic() {
        entranceCarQueue = new CarQueue();
        entrancePassQueue = new CarQueue();
        paymentCarQueue = new CarQueue();
        exitCarQueue = new CarQueue();
        screenLogic = new ScreenLogic(3, 6, 30);
    }

    public void start(){
        new Thread(this).start();
    }

    public void stop() {
        run=false;
    }
    
    public void reset() {
        entranceCarQueue.clearQueue();
        entrancePassQueue.clearQueue();
        paymentCarQueue.clearQueue();
        exitCarQueue.clearQueue();
        day = 0;
        hour = 0;
        minute = 0;
        run = false;
        reset = true;
    }

    @Override
    public void run() {
        run=true;
        for (int i = 0; i < 10000 && run; i++) {
            tick();
        }
        run=false;
    }

    public ScreenLogic getScreenLogic() {
        return screenLogic;
    }

    public void tick() {
    	advanceTime();
    	handleExit();
    	updateViews();
    	// Pause.
        try {
            Thread.sleep(tickPause);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    	handleEntrance();
    }

    public void updateViews(){
        screenLogic.tick();
        // Update the car park view.
        for (AbstractView v: views){
            v.updateView();
        }
    }

    private void advanceTime(){
        // Advance the time by one minute.
        minute++;
        while (minute > 59) {
            minute -= 60;
            hour++;
        }
        while (hour > 23) {
            hour -= 24;
            day++;
        }
        while (day > 6) {
            day -= 7;
        }

    }

    private void handleEntrance(){
    	carsArriving();
    	carsEntering(entrancePassQueue);
    	carsEntering(entranceCarQueue);  	
    }
    
    private void handleExit(){
        carsReadyToLeave();
        carsPaying();
        carsLeaving();
    }
    
    private void carsArriving(){
    	int numberOfCars=getNumberOfCars(weekDayArrivals, weekendArrivals);
        addArrivingCars(numberOfCars, AD_HOC);    	
    	numberOfCars=getNumberOfCars(weekDayPassArrivals, weekendPassArrivals);
        addArrivingCars(numberOfCars, PASS);   
        numberOfCars=getNumberOfCars(weekDayPassArrivals, weekendPassArrivals);
        addArrivingCars(numberOfCars, RESERVE);  
    }

    private void carsEntering(CarQueue queue){
        int i=0;
        // Remove car from the front of the queue and assign to a parking space.
    	while (queue.carsInQueue()>0 && 
    			screenLogic.getNumberOfOpenSpots()>0 &&
    			i<enterSpeed) {
            Car car = queue.removeCar();
            Location freeLocation = screenLogic.getFirstFreeLocation();
            screenLogic.setCarAt(freeLocation, car);
            i++;
        }
    }
    
    private void carsReadyToLeave(){
        // Add leaving cars to the payment queue.
        Car car = screenLogic.getFirstLeavingCar();
        while (car!=null) {
        	if (car.getHasToPay()){
	            car.setIsPaying(true);
	            paymentCarQueue.addCar(car);
        	}
        	else {
        		carLeavesSpot(car);
        	}
            car = screenLogic.getFirstLeavingCar();
        }
    }

    private void carsPaying(){
        // Let cars pay.
    	int i=0;
    	while (paymentCarQueue.carsInQueue()>0 && i < paymentSpeed){
            Car car = paymentCarQueue.removeCar();
            // TODO Handle payment.
            carLeavesSpot(car);
            i++;
    	}
    }
    
    private void carsLeaving(){
        // Let cars leave.
    	int i=0;
    	while (exitCarQueue.carsInQueue()>0 && i < exitSpeed){
            exitCarQueue.removeCar();
            i++;
    	}	
    }
    
    private int getNumberOfCars(int weekDay, int weekend){
        Random random = new Random();

        // Get the average number of cars that arrive per hour.
        int averageNumberOfCarsPerHour = day < 5
                ? weekDay
                : weekend;

        // Calculate the number of cars that arrive this minute.
        double standardDeviation = averageNumberOfCarsPerHour * 0.3;
        double numberOfCarsPerHour = averageNumberOfCarsPerHour + random.nextGaussian() * standardDeviation;
        return (int)Math.round(numberOfCarsPerHour / 60);	
    }
    
    private void addArrivingCars(int numberOfCars, String type){
        // Add the cars to the back of the queue.
    	switch(type) {
    	case AD_HOC: 
            for (int i = 0; i < numberOfCars; i++) {
            	entranceCarQueue.addCar(new AdHocCar());
            }
            break;
    	case PASS:
            for (int i = 0; i < numberOfCars; i++) {
            	entrancePassQueue.addCar(new ParkingPassCar());
            }
            break;	  
    	case RESERVE:
    		for (int i = 0; i < numberOfCars; i++) {
            	entrancePassQueue.addCar(new ReservedCar());
            }
            break;
    	}
    }
    
    private void carLeavesSpot(Car car){
    	screenLogic.removeCarAt(car.getLocation());
        exitCarQueue.addCar(car);
    }
    
    public int getMinute() {
    	return minute;
    }
    
    public int getHour() {
    	return hour;
    }
    
    public int getDay() {
    	return day;
    }

	public String getTime() {
		String time = "";
		if(hour < 10) {
			time += "0" + hour + ":";
		} else {
			time += hour + ":";
		}
		if(minute < 10) {
			time += "0" + minute;
		} else {
			time += minute;
		}
		return time;
	}
	
	public String getDayWord() {
		switch(day) {
			case 0:
				return "Maandag";
			case 1:
				return "Dinsdag";
			case 2:
				return "Woensdag";
			case 3:
				return "Donderdag";
			case 4:
				return "Vrijdag";
			case 5:
				return "Zaterdag";
			case 6:
				return "Zondag";
			default:
				return "";
		}
	}
	
	public boolean getReset() {
		return reset;
	}

	public void setReset(boolean b) {
		this.reset = b;
	}

	public void ticks(int i) {
		tickPause = 0;
		
		for(int x = 0; x < i; x++) {
			tick();
		}
		tickPause = 100;
	}

}
