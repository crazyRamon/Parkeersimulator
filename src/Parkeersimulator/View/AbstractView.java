package Parkeersimulator.View;

import java.awt.*;

import javax.swing.*;

import Parkeersimulator.Model.SimulatorLogic;

/**
 * De abstracte klasse van de views
 * @author Andy Perukel, Ramon kits
 * @version 01-03-2019
 *
 */
public abstract class AbstractView extends JPanel {
	
	protected static final Color LIGHTRED = new Color(255, 200, 200);
	protected static final Color LIGHTGREEN = new Color(200, 255, 200);
	protected static final Color LIGHTBLUE = new Color(200, 200, 255);
	protected static final Color LIGHTGRAY = new Color(100, 100, 100, 30);
	protected static final Font standard40px = new Font("Arial", Font.PLAIN, 40);
	protected static final Font standard30px = new Font("Arial", Font.PLAIN, 30);
	protected static final Font standard20px = new Font("Arial", Font.PLAIN, 20);
	protected static final Font standard18px = new Font("Arial", Font.PLAIN, 18);
	protected static final Font standard15px = new Font("Arial", Font.PLAIN, 15);
	protected static final Font standard13px = new Font("Arial", Font.PLAIN, 13);
	
	protected SimulatorLogic simulatorLogic;
	
	/**
	 * De constructor van de abstracte klasse
	 * @param simulatorLogic, de simulator
	 */
	public AbstractView(SimulatorLogic simulatorLogic) {
		this.simulatorLogic=simulatorLogic;
		simulatorLogic.addView(this);
	}
	
	/**
	 * Update de views
	 */
    public void updateView() {
		repaint();
	}
}
