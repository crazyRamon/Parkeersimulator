package Parkeersimulator.Model;

import Parkeersimulator.View.AbstractView;

import java.awt.Color;
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
    private boolean reset = false;
	private boolean running;
    private int totalAD_HOC;
    private int totalPASS;
    private int totalRESERVE;

    private int day = 0;
    private int hour = 0;
    private int minute = 0;

    private static int tickPause = 100;

 // ints for the average number of arriving cars per hour
    private int weekDayArrivals; 
    private int weekendArrivals; 
    private int weekDayPassArrivals; 
    private int weekendPassArrivals;
    private int weekDayResArrivals;
    private int weekendResArrivals;

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

    // Start de simulatie
    public void start(){
        new Thread(this).start();
    	updateViews();
    	
    }
    
    public void play(){
        running = true;
    }
    
    // Pauzeert de simulatie
    public void stop() {
        running = false;
    }
    
    public boolean getRunning() {
        return running;
    }
    
    public void setRunning(boolean b) {
    	running = b;
    }
    
    // Reset de simulatie
    public void reset() {
        reset = true;
        entranceCarQueue.clearQueue();
        entrancePassQueue.clearQueue();
        paymentCarQueue.clearQueue();
        exitCarQueue.clearQueue();
        day = 0;
        hour = 0;
        minute = 0;
        totalAD_HOC = 0;
        totalPASS = 0;
        totalRESERVE = 0;
    }
    
    // Setter voor TickPause
    public static void setTickPause(int tickSpeed) {
    	tickPause = tickSpeed;
    }   

    @Override
    //laat de simulator 1 week afspelen
    public void run() {
            while(true) tick(false);
    }

    public ScreenLogic getScreenLogic() {
        return screenLogic;
    }

    public void tick(boolean b) {
    	if(running || b) {
	    	advanceTime();
	    	handleExit();
	    	crowdsTime(day, hour, minute);
	    	updateViews();
    	}
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
        numberOfCars=getNumberOfCars(weekDayResArrivals, weekendResArrivals);
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
            if(car.getColor() == Color.RED) {
            	totalAD_HOC++;
            } else if(car.getColor() == Color.GREEN) {
            	totalRESERVE++;
            } else if(car.getColor() == Color.BLUE) {
            	totalPASS++;
            }
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
    	if(car.getColor() == Color.RED) {
        	totalAD_HOC--;
        } else if(car.getColor() == Color.GREEN) {
        	totalRESERVE--;
        } else if(car.getColor() == Color.BLUE) {
        	totalPASS--;
        }
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
		for(int x = 0; x < i; x++) {
			advanceTime();
	    	handleExit();
	    	crowdsTime(day, hour, minute);
	    	updateViews();
	    	handleEntrance();
		}
	}
	
	// Methode om de drukte te bepalen
	private void crowdsTime(int day, int hour, int minute) {
		//  's Nachts
		if (day < 8 && hour == 0) {
			weekDayArrivals = 20;
			weekDayPassArrivals = 10;
			weekDayResArrivals = 3;
		}
		else if (day < 5 && hour == 6) {
			weekDayArrivals = 140;
			weekDayPassArrivals = 30;
			weekDayResArrivals = 20;
		}
		
		// Maandag, dinsdag, woensdag 's avonds
		if (day < 3 && hour == 16 && minute == 30) {
			weekDayArrivals = 30;
			weekDayPassArrivals = 20;
			weekDayResArrivals = 10;
		}
		else if( day < 3 && hour == 20 ) {
			weekDayArrivals = 25;
			weekDayPassArrivals = 12;
			weekDayResArrivals = 5;
		}
		
		// Donderdag met koopavond
		if (day == 3 && hour == 18 && minute == 30) {
			weekDayArrivals = 100;
			weekDayPassArrivals = 50;
			weekDayResArrivals = 60;
		}
		else if(day == 3 && hour == 21) {
			weekDayArrivals = 20;
			weekDayPassArrivals = 15;
			weekDayResArrivals = 5;
		}
		
		// Vrijdag met concertavond
		if (day == 4 && hour == 20) {
			weekDayArrivals = 100;
			weekDayPassArrivals = 50;
			weekDayResArrivals = 150;
		}
		else if(day == 4 && hour == 23) {
			weekDayArrivals = 20;
			weekDayArrivals = 12;
			weekDayResArrivals = 5;
		}		
		// Zaterdag met concertavond
		if (day == 5 && hour == 0) {
			weekendArrivals = 20;
			weekendPassArrivals = 12;
			weekendResArrivals = 5;
		}
		else if (day == 5 && hour == 7) {
			weekendArrivals = 50;
			weekendPassArrivals = 20;
			weekendResArrivals = 15;
		}
		else if(day == 5 && hour == 20) {
			weekendArrivals = 100;
			weekendPassArrivals = 50;
			weekendResArrivals = 150;
		}
		else if(day == 5 && hour == 23) {
			weekendArrivals = 20;
			weekendPassArrivals = 12;
			weekendResArrivals = 5;
		}
		
		// Zondag met concertmiddag
		if (day == 6 && hour == 0) {
			weekendArrivals = 20;
			weekendPassArrivals = 12;
			weekendResArrivals = 5;
		}
		else if (day == 6 && hour == 7) {
			weekendArrivals = 50;
			weekendPassArrivals = 20;
			weekendResArrivals = 15;
		}
		else if(day == 6 && hour == 13) {
			weekendArrivals = 100;
			weekendPassArrivals = 50;
			weekendResArrivals = 150;
		}
		else if(day == 6 && hour == 16) {
			weekendArrivals = 20;
			weekendPassArrivals = 12;
			weekendResArrivals = 5;
		}		
	}
	public int getAmountOfAD_HOC() {
		return totalAD_HOC;
	}
	
	public int getAmountOfRESERVE() {
		return totalRESERVE;
	}
	
	public int getAmountOfPASS() {
		return totalPASS;
	}
	
	public int getTickPause() {
		return tickPause;
	}

}
