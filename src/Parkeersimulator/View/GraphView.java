package Parkeersimulator.View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import Parkeersimulator.Model.SimulatorLogic;

public class GraphView extends AbstractView {
	
	private Dimension size;
	private Image graphView;
    
    public GraphView(SimulatorLogic simulatorLogic) {
    	super(simulatorLogic);
		size = new Dimension(0, 0);
	}

    public void paintComponent(Graphics g) {
    	
    	if (graphView == null) {
            return;
        }

        Dimension currentSize = getSize();
        if (size.equals(currentSize)) {
            g.drawImage(graphView, 0, 0, null);
        }
        else {
            // Rescale the previous image.
            g.drawImage(graphView, 0, 0, currentSize.width, currentSize.height, null);
        }
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 200, 200);
    }
    
    public void updateView() {
    	if (!size.equals(getSize())) {
            size = getSize();
            graphView = createImage(size.width, size.height);
        }
        repaint();
    }
}
