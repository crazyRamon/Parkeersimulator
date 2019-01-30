package Parkeersimulator.View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import Parkeersimulator.Model.SimulatorLogic;

public class TimeView extends AbstractView {
	
	private Dimension size;
	private Image timeViewImage;

	public TimeView(SimulatorLogic simulatorLogic) {
		super(simulatorLogic);
		size = new Dimension(0, 0);
	}
	
    public void paintComponent(Graphics g) {
    	
    	if (timeViewImage == null) {
            return;
        }

        Dimension currentSize = getSize();
        if (size.equals(currentSize)) {
            g.drawImage(timeViewImage, 0, 0, null);
        }
        else {
            // Rescale the previous image.
            g.drawImage(timeViewImage, 0, 0, currentSize.width, currentSize.height, null);
        }
        Font currentFont = g.getFont();
		Font clockDisplay = null;
		try {
			clockDisplay = Font.createFont(Font.TRUETYPE_FONT, new File("src/digital-7 (mono).ttf"));
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Font font = clockDisplay.deriveFont(Font.PLAIN,70);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.registerFont(font);
        g.setFont(font);
        g.setColor(LIGHTGRAY);
        g.drawString("88:88", 220, 75);
        g.setColor(Color.BLACK);
        g.drawString(simulatorLogic.getTime(), 220, 75);
        g.setFont(standard40px);
        g.drawString(simulatorLogic.getDayWord(), 25, 63);
    }
    
    public void updateView() {
    	if (!size.equals(getSize())) {
            size = getSize();
            timeViewImage = createImage(size.width, size.height);
        }
        repaint();
    }

}
