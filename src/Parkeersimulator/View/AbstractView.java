package Parkeersimulator.View;

import java.awt.*;

import javax.swing.*;

import Parkeersimulator.Model.SimulatorLogic;

public abstract class AbstractView extends JPanel {
	
	protected static final Color LIGHTRED = new Color(255, 200, 200);
	protected static final Color LIGHTGREEN = new Color(200, 255, 200);
	protected static final Color LIGHTBLUE = new Color(200, 200, 255);
	
	protected SimulatorLogic simulatorLogic;
	
	public AbstractView(SimulatorLogic simulatorLogic) {
		this.simulatorLogic=simulatorLogic;
		simulatorLogic.addView(this);
	}
	
    public void updateView() {
		repaint();
	}
}
