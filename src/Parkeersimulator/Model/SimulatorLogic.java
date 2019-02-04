package Parkeersimulator.Model;

import Parkeersimulator.View.AbstractView;

import java.awt.Color;
import java.util.LinkedList;
import java.util.Random;

/**
 * De klasse voor wat er in de simulator
 * @author Andy Perukel, Ramon kits
 * @version 01-03-2019
 *
 */
public class SimulatorLogic extends AbstractModel implements Runnable{

	private static final String AD_HOC = "1";
	private static final String PASS = "2";
	private static final String RESERVE = "3";
	
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
	public LinkedList<Integer> amountOfCarsList = new LinkedList<>();
	public LinkedList<Integer> amountOfAD_HOCCarsList = new LinkedList<>();
	public LinkedList<Integer> amountOfRESERVECarsList = new LinkedList<>();
	public LinkedList<Integer> amountOfPASSCarsList = new LinkedList<>();
	private int graphLength = 1440;
	private double maxCars = 0;
	private int maxQueueLength = 5;

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

    /**
     * Constructor voor een nieuwe simulator
     */
    public SimulatorLogic() {
        entranceCarQueue = new CarQueue();
        entranceCarQueue.setMaxCars(maxQueueLength);
        entrancePassQueue = new CarQueue();
        entrancePassQueue.setMaxCars(maxQueueLength);
        paymentCarQueue = new CarQueue();
        exitCarQueue = new CarQueue();
        screenLogic = new ScreenLogic(2, 3, 6, 30);
    }

    /*
     * start de simulator
     */
    public void start(){
        new Thread(this).start();
        minute= -1;
    	updateViews();
    	
    }
    
    /**
     * zet de simulator aan
     */
    public void play(){
        running = true;
    }
    
    /**
     * pauzeert de simulator
     */
    public void stop() {
        running = false;
    }
    
    /**
     * Kijkt of de simulatie bezig is
     */
    public boolean getRunning() {
        return running;
    }
    
    /**
     * Set of de simulatie bezig is
     * @param b, of de simulatie bezig is
     */
    public void setRunning(boolean b) {
    	running = b;
    }
    
    /**
     * reset de simulator
     */
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
        resetGraphData();
    }
    
    /**
     * Set de snelheid van de simulatie
     * @param tickSpeed, de snelheid
     */
    public static void setTickPause(int tickSpeed) {
    	tickPause = tickSpeed;
    }
	
	/**
	 * Geeft de snelheid van de simulatie
	 * @return de snelheid van de simulatie
	 */
	public int getTickPause() {
		return tickPause;
	}

    @Override
    /**
     * laat de simulator een aantal tick afspelen zonder dat er vertraging tussen de ticks zit
     */    
    public void run() {
            while(true) tick(false);
    }

    /**
     * Geeft ScreenLogic
     * @return screenLogic
     */
    public ScreenLogic getScreenLogic() {
        return screenLogic;
    }
    
    /**
     * Laat de simulatie met één tick vooruit gaan
     * @param b geeft aan of je de boolean running wil negeren
     */
    public void tick(boolean b) {
    	if(running || b) {
    		updateGraphList();
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

    /**
     * Update de views
     */
    public void updateViews(){
        screenLogic.tick();
        // Update the car park view.
        for (AbstractView v: views){
            v.updateView();
        }
    }

    /**
     * Laat de tijd vooruit gaan
     */
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

    /**
     * Regelt de auto's die aankomen en de rijen
     */
    private void handleEntrance(){
    	carsArriving();
    	carsEntering(entrancePassQueue);
    	carsEntering(entranceCarQueue);  	
    }
    
    /**
     * Regelt de auto's die weggaan
     */
    private void handleExit(){
        carsReadyToLeave();
        carsPaying();
        carsLeaving();
    }
    
    /**
     * Regelt het aantal auto's dat aankomt
     */
    private void carsArriving(){   
        int numberOfCars=getNumberOfCars(weekDayResArrivals, weekendResArrivals);
        addArrivingCars(numberOfCars, RESERVE);    	
    	numberOfCars=getNumberOfCars(weekDayPassArrivals, weekendPassArrivals);
        addArrivingCars(numberOfCars, PASS);
    	numberOfCars=getNumberOfCars(weekDayArrivals, weekendArrivals);
        addArrivingCars(numberOfCars, AD_HOC);
    }

    /**
     * Regelt de auto's die aankomen
     * @param queue, de rij voor auto's
     */
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
    
    /**
     * Regelt de auto's die de garage verlaten
     */
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

    /**
     * Regelt de betaling van de auto's
     */
    private void carsPaying(){
        // Let cars pay.
    	int i=0;
    	while (paymentCarQueue.carsInQueue()>0 && i < paymentSpeed){
            Car car = paymentCarQueue.removeCar();
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
    
    /**
     * Geeft de omzet van de gereserveerde auto's
     * @return profitReserved, de omzet
     */
    public double getProfitReserved() {
    	return profitReserved;
    }
    
    /**
     * Geeft de omzet van de normale auto's
     * @return profitReserved, de omzet
     */
    public double getProfitCar() {
    	return profitCar;
    }
    
    /**
     * Regelt het weggaan van auto's
     */
    private void carsLeaving(){
        // Let cars leave.
    	int i=0;
    	while (exitCarQueue.carsInQueue()>0 && i < exitSpeed){
            exitCarQueue.removeCar();
            i++;
    	}	
    }
    
    /**
     * Berekent het aantal auto's dat aankomt
     * @param weekDay, aantal auto's op een weekdag
     * @param weekend, aantal auto's in het weekend
     * @return het aantal auto's dat aankomt
     */
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
    
    /**
     * Voegt auto's toe aan de garage
     * @param numberOfCars, het aantal auto's dat wordt toegevoegd
     * @param type, het soort auto dat wordt toegeveogd
     */
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
    
    /**
     * Verwijderd een auto van zijn parkeerplek een voegt het toe aan de exit rij. 
     * @param car, de auto
     */
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
    
    /**
     * Geeft de minuut
     * @return minute, de minuut
     */
    public int getMinute() {
    	return minute;
    }
    
    /**
     * Geeft het uur
     * @return hour. het uur
     */
    public int getHour() {
    	return hour;
    }
    
    /**
     * Geeft de dag
     * @return day, de dag
     */
    public int getDay() {
    	return day;
    }

    /**
     * Geeft de minuut en het uur in de form van een digitale klok (00:00)
     * @return time, de tijd
     */
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
	
	/**
	 * Geeft de dag
	 * @return de juiste naam van de dag
	 */
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
	
	/**
	 * Geeft de reset
	 * @return reset, de reset
	 */
	public boolean getReset() {
		return reset;
	}

	/**
	 * Set de reset
	 * @param b, de reset
	 */
	public void setReset(boolean b) {
		this.reset = b;
	}

	/**
	 * De ticks die je in 1 keer wil laten afspelen zonder delay
	 * @param i het aantal ticks
	 */
	public void ticks(int i) {
		for(int x = 0; x < i; x++) {
	    	updateGraphList();
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
	
	
	//volgende 3 getters geven het aantal auto's van een bapaald type weer.
	/**
	 * Geeft het aantal normale auto's weer
	 * @return het aantal normale auto's
	 */
	public int getAmountOfAD_HOC() {
		return totalAD_HOC;
	}
	
	/**
	 * Geeft het aantal gereserveerde auto's weer
	 * @return het aantal reserveer auto's
	 */
	public int getAmountOfRESERVE() {
		return totalRESERVE;
	}
	
	/**
	 * Geeft het aantal geabonneerde auto's weer.
	 * @return het aantal abonnements auto's
	 */
	public int getAmountOfPASS() {
		return totalPASS;
	}
	
	/**
	 * Geeft het totaal aantal auto's weer
	 * @return het aantal auto's
	 */
	public int getTotalAmountOfCars() {
		return totalPASS + totalRESERVE + totalAD_HOC;
	}
	
	//volgende 3 getters geven het aantal gearriveerde auto's van een bapaald type weer.
	/**
	 * Geeft het gearriveerde aantal normale auto's weer
	 * @return het gearriveerde aantal normale auto's
	 */
	public int getAD_HOCCount() {
		return countAD_HOC;
	}
	
	/**
	 * Geeft het gearriveerde aantal reserveer auto's weer
	 * @return het gearriveerde aantal reserveer auto's
	 */
	public int getRESERVECount() {
		return countRESERVE;
	}

	/**
	 * Geeft het gearriveerde aantal abonnement auto's weer
	 * @return het aantal abonnement auto's
	 */
	public int getPASSCount() {
		return countPASS;
	}

	/**
	 * Zet het aantal gearriveerde auto's weer op 0;
	 */
	private void resetCarCount() {
        countAD_HOC = 0;
        countPASS = 0;
        countRESERVE = 0;
	}
	
	/**
	 * Update het maximaal aantal auto's
	 */
	private void maxCarCount() {
		if(getAD_HOCCount() > maxCarCount) {
        	maxCarCount = getAD_HOCCount();
        } else if(getRESERVECount() > maxCarCount) {
        	maxCarCount = getRESERVECount();
        } else if(getPASSCount() > maxCarCount) {
        	maxCarCount = getPASSCount();
        }
	}
	
	/**
	 * Reset het maximaal aantal auto's
	 */
	public void resetMaxCarCount() {
		maxCarCount = 1;
	}
	
	/**
	 * Geeft het maximaal aantal auto's
	 * @return maxCarCount, het maximaal aantal auto's
	 */
	public int getMaxCarCount() {
		return maxCarCount;
	}

	/**
	 *  Geeft het aantal normale auto's dat bij de dag hoort
	 * @param day, de dag
	 * @return het normale aantal auto's bij de dag
	 */
	public int getDailyCarCountAD_HOC(int day) {
		return dailyCarCountAD_HOC[day];
	}

	/**
	 *  Geeft het aantal abonnement auto's dat bij de dag hoort
	 * @param day, de dag
	 * @return het aantal abonnement auto's bij de dag
	 */
	public int getDailyCarCountPASS(int day) {
		return dailyCarCountPASS[day];
	}

	/**
	 * Geeft het aantal reserveer auto's dat bij de dag hoort
	 * @param day, de dag
	 * @return het aantal reserveer auto's bij de dag
	 */
	public int getDailyCarCountRESERVE(int day) {
		return dailyCarCountRESERVE[day];
	}

	/**
	 *  Geeft het aantal normale auto's dat bij de dag hoort van de week ervoor
	 * @param day, de dag
	 * @return het aantal normale auto's bij de dag van de week ervoor
	 */
	public int getPreviousWeekDailyCarCountAD_HOC(int day) {
		return previousWeekDailyCarCountAD_HOC[day];
	}

	/**
	 * Geeft het aantal abonnement auto's dat bij de dag hoort van de week ervoor
	 * @param day , de dag
	 * @return het aantal abonnement auto's bij de dag van de week ervoor
	 */
	public int getPreviousWeekDailyCarCountPASS(int day) {
		return previousWeekDailyCarCountPASS[day];
	}

	/**
	 * Geeft het aantal reserveer auto's dat bij de dag hoort van de week ervoor
	 * @param day, de dag
	 * @return het aantal reserveer auto's bij de dag van de week ervoor
	 */
	public int getPreviousWeekDailyCarCountRESERVE(int day) {
		return previousWeekDailyCarCountRESERVE[day];
	}

	/**
	 * zet de dagelijkse maximum auto's om naar die voor een week terug
	 */
	public void setBackDailyCarCount() {
		for(int x = 0; x < 7; x++) {
			previousWeekDailyCarCountRESERVE[x] = dailyCarCountRESERVE[x];
			previousWeekDailyCarCountPASS[x] = dailyCarCountPASS[x];
			previousWeekDailyCarCountAD_HOC[x] = dailyCarCountAD_HOC[x];
		}
	}
	
	/**
	 * update de dagelijkse car count
	 */
	public void setDailyCarCount() {
		dailyCarCountAD_HOC[day] = countAD_HOC;
		dailyCarCountRESERVE[day] = countRESERVE;
		dailyCarCountPASS[day] = countPASS;
	}
	
	/**
	 *  reset de dagelijkse car count van de week
	 */
	public void resetDailyCarCount() {
		for(int x = 0; x < 7; x++) {
			dailyCarCountAD_HOC[x] = 0;
			dailyCarCountRESERVE[x] = 0;
			dailyCarCountPASS[x] = 0;
		}
	}
	
	/**
	 *  reset de dagelijkse car count van de vorige week
	 */
	public void resetPreviousDailyCarCount() {
		for(int x = 0; x < 7; x++) {
			previousWeekDailyCarCountAD_HOC[x] = 0;
			previousWeekDailyCarCountRESERVE[x] = 0;
			previousWeekDailyCarCountPASS[x] = 0;
		}
	}

	/**
	 *  reset de dagelijkse car count van de gepasseerde auto's van de week
	 */
	public void resetDailyPassingCars() {
		for(int x = 0; x < 7; x++) {
			dailyPassingCars[x] = 0;
		}
	}
	
	/**
	 * geeft het totale aantal auto's in alle queue's
	 * @return aantal auto's
	 */
	public int getTotalCarsInQueue() {
		return (
		entranceCarQueue.carsInQueue() +
	    entrancePassQueue.carsInQueue() +
	    paymentCarQueue.carsInQueue() +
	    exitCarQueue.carsInQueue()
	    );
	}
	
	/**
	 * geeft het totale aantal auto's in de ingang queue
	 * @return aantal auto's
	 */	
	public int getCarsInEntranceCarQueue() {
		return entranceCarQueue.carsInQueue();
	}
	
	/**
	 * geeft het totale aantal auto's in de abonnement ingang queue
	 * @return aantal auto's
	 */	
	public int getCarsInEntrancePassQueue() {
		return entrancePassQueue.carsInQueue();
	}
	
	/**
	 * geeft het totale aantal auto's in de betaal queue
	 * @return aantal auto's
	 */	
	public int getCarsInPaymentCarQueue() {
		return paymentCarQueue.carsInQueue();
	}
	
	/**
	 * geeft het totale aantal auto's in de uitgang queue
	 * @return aantal auto's
	 */	
	public int getCarsInExitCarQueue() {
		return exitCarQueue.carsInQueue();
	}
	
	/** 
	 * geeft het aantal gepasseerde auto's van een dag
	 * @param day int van 0 tot en met 6. 0 is maandag enzovoort
	 * @return aantal gepasseerde auto's
	 */
	public int getDailyPassingCars(int day) {
		return dailyPassingCars[day];
	}
	
	/**
	 * totaal aantal gepasseerde autos van de dag
	 * @return aantal auto's
	 */
	public int getTotalDailyPassingCars() {
		return totalDailyPassingCars;
	}
	
	/**
	 * geeft het totaal aantal ticks die de simulator heeft afgespeeld
	 * @return aantal ticks
	 */
	public int getTotalTicks() {
		return totalTicks;
	}
	
	
	/**
	 * update de informatie die de lijngrafiek nodig heeft
	 */
	public void updateGraphList() {
		if(graphLength >= 500 && totalTicks % (graphLength / 500) == 0) {
			amountOfCarsList.add(getTotalAmountOfCars());
			amountOfAD_HOCCarsList.add(getAmountOfAD_HOC());
			amountOfRESERVECarsList.add(getAmountOfRESERVE());
			amountOfPASSCarsList.add(getAmountOfPASS());
			if(totalTicks > graphLength) {
				amountOfCarsList.removeFirst();
				amountOfAD_HOCCarsList.removeFirst();
				amountOfRESERVECarsList.removeFirst();
				amountOfPASSCarsList.removeFirst();
			}
		} else if(graphLength < 500) {
			amountOfCarsList.add(getTotalAmountOfCars());
			amountOfAD_HOCCarsList.add(getAmountOfAD_HOC());
			amountOfRESERVECarsList.add(getAmountOfRESERVE());
			amountOfPASSCarsList.add(getAmountOfPASS());
			if(totalTicks > graphLength) {
				amountOfCarsList.removeFirst();
				amountOfAD_HOCCarsList.removeFirst();
				amountOfRESERVECarsList.removeFirst();
				amountOfPASSCarsList.removeFirst();
			}
		}
		if(getTotalAmountOfCars() > maxCars) {
        	maxCars = getTotalAmountOfCars();
        }
	}
	
	/**
	 * geeft het aantal eenheden die de lijngrafiek gebruikt om de grafiek weer te geven
	 * @return
	 */
	public int getGraphLength() {
		if(graphLength > 500) {
			return graphLength / (graphLength / 500);
		} else {
			return graphLength;
		}
	}
	
	/**
	 * zet het aantal ticks in wat de grafiek weergeeft
	 * @param i aantal ticks
	 */
	public void setGraphLength(int i) {
		graphLength = i;
		reset();
		updateViews();
	}
	
	/*
	 * geeft het maximaal aantal auto's weer
	 */
	public double getMaxCars() {
		return maxCars;
	}
	
	/*
	 * reset het maximaal aantal auto's
	 */
	public void resetMaxCars() {
		maxCars = 0;
	}
	
	/*
	 * reset de date van de grafiek
	 */
	public void resetGraphData() {
        amountOfCarsList.clear();
        amountOfAD_HOCCarsList.clear();
        amountOfRESERVECarsList.clear();
        amountOfPASSCarsList.clear();
        resetMaxCars();
	}
	
	/**
	 * Bepaald de drukte op de dagen
	 * @param day, de dag
	 * @param hour, het uur
	 * @param minute, de minuut
	 */
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
	
	public int getMaxQueueLength() {
		return maxQueueLength;
	}
}
