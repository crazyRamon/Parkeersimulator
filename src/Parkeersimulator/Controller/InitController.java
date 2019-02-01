package Parkeersimulator.Controller;

import Parkeersimulator.Model.ScreenLogic;
import Parkeersimulator.Model.SimulatorLogic;

import javax.swing.*;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Een klasse voor de knoppen
 * @author Andy Perukel, Ramon kits
 * @version 01-03-2019
 *
 */

public class InitController extends AbstractController{
	
    private JButton start;
    private JButton stop;
    private JButton tick;
    private JButton reset;
    private JButton ticks60;
    private JButton ticks1440;
    private JButton day;
    private JButton threeDays;
    private JButton week;
    private JLabel label;
	private JLabel graphLabel;
	private JLabel controlPanel;
    
    // slider
    private int simSnelheid = simulatorLogic.getTickPause();
    private JSlider slider = new JSlider(JSlider.HORIZONTAL, 2, 95, 50);

    /**
     * Constructor voor de knoppen
     * @param simulatorLogic, de simulator
     */
    public InitController (SimulatorLogic simulatorLogic){
        super(simulatorLogic);

        controlPanel = new JLabel("Controlepaneel");
        controlPanel.setFont(new Font("Arial", Font.PLAIN, 40));
        controlPanel.setPreferredSize(new Dimension(280, 35));
        
        // Button om de simulatie te starten
        start = new JButton("Start");
        start.setPreferredSize(new Dimension(350, 40));
        start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                simulatorLogic.setRunning(true);
                start.setVisible(false);
                stop.setVisible(true);
                tick.setEnabled(false);
                ticks60.setEnabled(false);
                ticks1440.setEnabled(false);
            }
        } );

        // Button om de simulatie te stoppen
        stop = new JButton("Pauze");
        stop.setPreferredSize(new Dimension(350, 40));
        stop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                simulatorLogic.stop();
                stop.setVisible(false);
                start.setVisible(true);
                tick.setEnabled(true);
                ticks60.setEnabled(true);
                ticks1440.setEnabled(true);
            }
        } );
        
        // Button voor 1 tick
        tick = new JButton("+1 minuut");
        tick.setPreferredSize(new Dimension(113, 30));
        tick.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                simulatorLogic.tick(true);
            }
        } );
        
       // Button voor 60 ticks
        ticks60 = new JButton("+1 uur");
        ticks60.setPreferredSize(new Dimension(113, 30));
        ticks60.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                simulatorLogic.ticks(60);
            }
        } );
        
     // Button voor 3600 (1 dag) ticks
        ticks1440 = new JButton("+1 dag");
        ticks1440.setPreferredSize(new Dimension(113, 30));
        ticks1440.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                simulatorLogic.ticks(1440);
            }
        } );
        
      // Button voor reset knop
        reset = new JButton("Reset");
        reset.setPreferredSize(new Dimension(350, 40));
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                simulatorLogic.reset();
                simulatorLogic.tick(true);
                stop.setVisible(false);
                start.setVisible(true);
                simulatorLogic.reset();
                tick.setEnabled(true);
                ticks60.setEnabled(true);
                ticks1440.setEnabled(true);
            }
        } );
        
     // Button voor een slider die de snelheid aanpast
        label = new JLabel("Simulatiesnelheid 20 min/sec");
        label.setPreferredSize(new Dimension(200, 15));
        label.setFont(new Font("Arial", Font.PLAIN, 15));
        slider.setPreferredSize(new Dimension(300, 25));
        slider.addChangeListener(new ChangeListener() {
     			public void stateChanged(ChangeEvent e) {
     				double simSnelheidDouble = (100 - slider.getValue());
     				simSnelheid = (int)simSnelheidDouble;
     				if(simSnelheid < 2) {
     					simSnelheid = 2;
     				}
     				label.setText("Simulatiesnelheid " + (1000 / simSnelheid) + " min/sec");
     				SimulatorLogic.setTickPause(simSnelheid);
     				
     			}
     	});
        
        
        //label boven de 3 opties van het lijngrafiek
        graphLabel = new JLabel("\u2193        Pas x-as van de lijngrafiek aan        \u2193");
        graphLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        graphLabel.setPreferredSize(new Dimension(280, 35));
        
        
        //lengte van lijngrafiek omzetten naar 1 dag (1440 ticks)
        day = new JButton("1 dag");
        day.setPreferredSize(new Dimension(113, 40));
        day.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	simulatorLogic.setGraphLength(1440);
                day.setEnabled(false);
                week.setEnabled(true);
                threeDays.setEnabled(true);
                day.setText("1 dag (huidig)");
                threeDays.setText("3 dagen");
                week.setText("1 week");
                stop.setVisible(false);
                start.setVisible(true);
                tick.setEnabled(true);
                ticks60.setEnabled(true);
                ticks1440.setEnabled(true);
            }
        } );

        //lengte van lijngrafiek omzetten naar 3 dagen (4320 ticks)
        threeDays = new JButton("3 dagen");
        threeDays.setPreferredSize(new Dimension(113, 40));
        threeDays.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	simulatorLogic.setGraphLength(4320);
                day.setEnabled(true);
                week.setEnabled(true);
                threeDays.setEnabled(false);
                day.setText("1 dag");
                threeDays.setText("3 dagen (huidig)");
                week.setText("1 week");
                stop.setVisible(false);
                start.setVisible(true);
                tick.setEnabled(true);
                ticks60.setEnabled(true);
                ticks1440.setEnabled(true);
            }
        } );

        //lengte van lijngrafiek omzetten naar 1 week (10080 ticks)
        week = new JButton("1 week");
        week.setPreferredSize(new Dimension(113, 40));
        week.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	simulatorLogic.setGraphLength(10080);
                day.setEnabled(true);
                week.setEnabled(false);
                threeDays.setEnabled(true);
                day.setText("1 dag");
                threeDays.setText("3 dagen");
                week.setText("1 week (huidig)");
                stop.setVisible(false);
                start.setVisible(true);
                tick.setEnabled(true);
                ticks60.setEnabled(true);
                ticks1440.setEnabled(true);
            }
        } );
        
        //standaard beginwaardes voor de day knop
        day.setEnabled(false);
        day.setText("1 dag (huidig)");        
        
        //knoppen etc.etc toevoegen
        
        add(controlPanel);
        add(label);
        add(slider);
        add(start);
        add(stop);
        add(tick);
        add(ticks60);
        add(ticks1440);
        add(reset);
        add(graphLabel);
        add(day);
        add(threeDays);
        add(week);

        setVisible(true);
        
        stop.setVisible(false);
    }
}
