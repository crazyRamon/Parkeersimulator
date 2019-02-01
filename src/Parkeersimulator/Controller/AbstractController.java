package Parkeersimulator.Controller;

import Parkeersimulator.Model.SimulatorLogic;

import javax.swing.*;
/**
 * Een abstracte controller
 * @author Andy Perukel, Ramon kits
 * @version 01-03-2019
 */
public abstract class AbstractController extends JPanel{
    protected SimulatorLogic simulatorLogic;

    
    	/**
    	 * De constructor van AbstractController
    	 * @param simulatorLogic, de simulator
    	 */
    public AbstractController(SimulatorLogic simulatorLogic){
        this.simulatorLogic = simulatorLogic;
    }
}
