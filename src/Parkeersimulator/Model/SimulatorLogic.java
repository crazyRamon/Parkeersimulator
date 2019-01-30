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
    private int countAD_HOC;
    private int countPASS;
    private int countRESERVE;
    private double profitReserved;
    private double profitCar;
    private int maxCarCount = 1;
    private int[] dailyCarCountAD_HOC = {0, 0, 0, 0, 0, 0, 0};
    private int[] dailyCarCountPASS = {0, 0, 0, 0, 0, 0, 0};
    private int[] dailyCarCountRESERVE = {0, 0, 0, 0, 0, 0, 0};
    private int[] previousWeekDailyCarCountAD_HOC = {0, 0, 0, 0, 0, 0, 0};
    private int[] previousWeekDailyCarCountPASS = {0, 0, 0, 0, 0, 0, 0};
    private int[] previousWeekDailyCarCountRESERVE = {0, 0, 0, 0, 0, 0, 0};
    private int[] dailyPassingCars = {0, 0, 0, 0, 0, 0, 0};
    private int totalDailyPassingCars = 0;
	private int totalTicks = -1;

    private int day = 0;
    private int hour = 0;
    private int minute = 0;

    private static int tickPause = 50;

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
        entranceCarQueue.setMaxCars(10);
        entrancePassQueue = new CarQueue();
        entrancePassQueue.setMaxCars(10);
        paymentCarQueue = new CarQueue();
        exitCarQueue = new CarQueue();
        screenLogic = new ScreenLogic(2, 3, 6, 30);
    }

    // Start de simulatie
    public void start(){
        new Thread(this).start();
        minute= -1;
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
        running = false;
        reset = true;
        entranceCarQueue.clearQueue();
        entrancePassQueue.clearQueue();
        paymentCarQueue.clearQueue();
        exitCarQueue.clearQueue();
        resetCarCount();
        resetMaxCarCount();
        resetDailyCarCount();
        resetDailyPassingCars();
        resetPreviousDailyCarCount();
        day = 0;
        hour = 0;
        minute = 0;
        totalAD_HOC = 0;
        totalPASS = 0;
        totalRESERVE = 0;
        profitReserved = 0;
        profitCar = 0;
        totalDailyPassingCars = 0;
        totalTicks = 0;
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
    
    /*
     * @param b geeft aan of je de boolean running wil negeren
     */
    public void tick(boolean b) {
    	if(running || b) {
	    	advanceTime();
	    	handleExit();
	    	crowdsTime(day, hour, minute);
	    	updateViews();
	    	maxCarCount();
	    	setDailyCarCount();
	    	totalTicks++;
    	}
    	// Pause.
        try {
            Thread.sleep(tickPause);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(running || b) {
        	handleEntrance();
        }
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
            resetCarCount();
        }
        while (day > 6) {
        	setBackDailyCarCount();
        	resetDailyCarCount();
        	resetDailyPassingCars();
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
        int numberOfCars=getNumberOfCars(weekDayResArrivals, weekendResArrivals);
        addArrivingCars(numberOfCars, RESERVE);    	
    	numberOfCars=getNumberOfCars(weekDayPassArrivals, weekendPassArrivals);
        addArrivingCars(numberOfCars, PASS);
    	numberOfCars=getNumberOfCars(weekDayArrivals, weekendArrivals);
        addArrivingCars(numberOfCars, AD_HOC);
    }

    private void carsEntering(CarQueue queue){
        int i=0;
        // Remove car from the front of the queue and assign to a parking space.
    	while (queue.carsInQueue()>0 && 
    			screenLogic.getNumberOfOpenSpots()>0 &&
    			i<enterSpeed) {
            Car car = queue.removeCar();
            Location freeLocation = null;
            if(car.getCanUsePassPlaces()) {
            	freeLocation = screenLogic.getFirstFreePassLocation();
            }
            if(freeLocation == null) {
            	freeLocation = screenLogic.getFirstFreeLocation();
            }
            if(freeLocation == null) {
            	queue.addCar(car);
            } else {
	            screenLogic.setCarAt(freeLocation, car);
	            if(car.getColor() == Color.RED) {
	            	totalAD_HOC++;
	            	countAD_HOC++;
	            } else if(car.getColor() == Color.GREEN) {
	            	totalRESERVE++;
	            	countRESERVE++;
	            } else if(car.getColor() == Color.BLUE) {
	            	totalPASS++;
	            	countPASS++;
	            }
            }
            i++;
        }
    }
    
    private void carsReadyToLeave(){
        // Add leaving cars to the payment queue.
        Car car = screenLogic.getFirstLeavingCar();
        while (car!=null) {
        	if (car.getHasToPay()){
	            boolean addedToPaymentCarQueue = paymentCarQueue.addCar(car);
	            if(addedToPaymentCarQueue) {
	            	 car.setIsPaying(true);
	            }
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
            if(car.getCarReserved() == true) {
            	profitReserved += car.getMinutesBetalenRes();
            }
            else {
            	profitCar += car.getMinutesBetalen();
            }
            carLeavesSpot(car);
            i++;
    	}
    }
    
    // Getter voor de winst van de gereserveede auto's
    public double getProfitReserved() {
    	return profitReserved;
    }
    // Getter voor de winst van de normale auto's
    public double getProfitCar() {
    	return profitCar;
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
            	boolean canAddCar = entranceCarQueue.addCar(new AdHocCar());
            	if(!canAddCar) {
            		dailyPassingCars[getDay()]++;
            		totalDailyPassingCars++;
            	}
            }
            break;
    	case PASS:
            for (int i = 0; i < numberOfCars; i++) {
            	boolean canAddCar = entranceCarQueue.addCar(new ParkingPassCar());
            	if(!canAddCar) {
            		dailyPassingCars[getDay()]++;
            		totalDailyPassingCars++;
            	}
            }
            break;	  
    	case RESERVE:
    		for (int i = 0; i < numberOfCars; i++) {
    			boolean canAddCar = entranceCarQueue.addCar(new ReservedCar());
            	if(!canAddCar) {
            		dailyPassingCars[getDay()]++;
            		totalDailyPassingCars++;
            	}
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
	    	maxCarCount();
	    	setDailyCarCount();
	    	totalTicks++;
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
	
	//volgende 3 getters geven het aantal auto's van een bapaald type weer.
	public int getAmountOfAD_HOC() {
		return totalAD_HOC;
	}
	
	public int getAmountOfRESERVE() {
		return totalRESERVE;
	}
	
	public int getAmountOfPASS() {
		return totalPASS;
	}
	
	public int getTotalAmountOfCars() {
		return totalPASS + totalRESERVE + totalAD_HOC;
	}
	
	public int getTickPause() {
		return tickPause;
	}
	
	//volgende 3 getters geven het aantal gearriveerde auto's van een bapaald type weer.
	public int getAD_HOCCount() {
		return countAD_HOC;
	}
	
	public int getRESERVECount() {
		return countRESERVE;
	}
	
	public int getPASSCount() {
		return countPASS;
	}
	
	private void resetCarCount() {
        countAD_HOC = 0;
        countPASS = 0;
        countRESERVE = 0;
	}
	
	private void maxCarCount() {
		if(getAD_HOCCount() > maxCarCount) {
        	maxCarCount = getAD_HOCCount();
        } else if(getRESERVECount() > maxCarCount) {
        	maxCarCount = getRESERVECount();
        } else if(getPASSCount() > maxCarCount) {
        	maxCarCount = getPASSCount();
        }
	}
	
	public void resetMaxCarCount() {
		maxCarCount = 1;
	}
	
	public int getMaxCarCount() {
		return maxCarCount;
	}
	
	public int getDailyCarCountAD_HOC(int day) {
		return dailyCarCountAD_HOC[day];
	}
	
	public int getDailyCarCountPASS(int day) {
		return dailyCarCountPASS[day];
	}
	
	public int getDailyCarCountRESERVE(int day) {
		return dailyCarCountRESERVE[day];
	}
	
	public int getPreviousWeekDailyCarCountAD_HOC(int day) {
		return previousWeekDailyCarCountAD_HOC[day];
	}
	
	public int getPreviousWeekDailyCarCountPASS(int day) {
		return previousWeekDailyCarCountPASS[day];
	}
	
	public int getPreviousWeekDailyCarCountRESERVE(int day) {
		return previousWeekDailyCarCountRESERVE[day];
	}
	
	public void setBackDailyCarCount() {
		for(int x = 0; x < 7; x++) {
			previousWeekDailyCarCountRESERVE[x] = dailyCarCountRESERVE[x];
			previousWeekDailyCarCountPASS[x] = dailyCarCountPASS[x];
			previousWeekDailyCarCountAD_HOC[x] = dailyCarCountAD_HOC[x];
		}
	}
	
	public void setDailyCarCount() {
		dailyCarCountAD_HOC[day] = countAD_HOC;
		dailyCarCountRESERVE[day] = countRESERVE;
		dailyCarCountPASS[day] = countPASS;
	}
	
	public void resetDailyCarCount() {
		for(int x = 0; x < 7; x++) {
			dailyCarCountAD_HOC[x] = 0;
			dailyCarCountRESERVE[x] = 0;
			dailyCarCountPASS[x] = 0;
		}
	}
	
	public void resetPreviousDailyCarCount() {
		for(int x = 0; x < 7; x++) {
			previousWeekDailyCarCountAD_HOC[x] = 0;
			previousWeekDailyCarCountRESERVE[x] = 0;
			previousWeekDailyCarCountPASS[x] = 0;
		}
	}
	
	public void resetDailyPassingCars() {
		for(int x = 0; x < 7; x++) {
			dailyPassingCars[x] = 0;
		}
	}
	
	public int getTotalCarsInQueue() {
		return (
		entranceCarQueue.carsInQueue() +
	    entrancePassQueue.carsInQueue() +
	    paymentCarQueue.carsInQueue() +
	    exitCarQueue.carsInQueue()
	    );
	}
	
	public int getCarsInEntranceCarQueue() {
		return entranceCarQueue.carsInQueue();
	}
	
	public int getCarsInEntrancePassQueue() {
		return entrancePassQueue.carsInQueue();
	}
	
	public int getCarsInPaymentCarQueue() {
		return paymentCarQueue.carsInQueue();
	}
	
	public int getCarsInExitCarQueue() {
		return exitCarQueue.carsInQueue();
	}
	
	public int getDailyPassingCars(int day) {
		return dailyPassingCars[day];
	}
	
	public int getTotalDailyPassingCars() {
		return totalDailyPassingCars;
	}
	
	public int getTotalTicks() {
		return totalTicks;
	}

}
