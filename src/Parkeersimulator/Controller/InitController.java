package Parkeersimulator.Controller;

import Parkeersimulator.Model.SimulatorLogic;

import javax.swing.*;

import java.awt.Dimension;
import java.awt.event.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class InitController extends AbstractController{
    private JButton start;
    private JButton stop;
    private JButton tick;
    private JButton reset;
    private JButton ticks60;
    private JButton ticks1440;
    
    // slider
    private int simSnelheid = simulatorLogic.getTickPause();
    private JSlider slider = new JSlider(JSlider.HORIZONTAL, 2, 100, 50);
    

    public InitController (SimulatorLogic simulatorLogic){
        super(simulatorLogic);

        // Button om de simulatie te starten
        start = new JButton("Start");
        start.setPreferredSize(new Dimension(80, 26));
        start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                simulatorLogic.setRunning(true);
                start.setVisible(false);
                stop.setVisible(true);
            }
        } );

        // Button om de simulatie te stoppen
        stop = new JButton("Pauze");
        stop.setPreferredSize(new Dimension(80, 26));
        stop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                simulatorLogic.stop();
                stop.setVisible(false);
                start.setVisible(true);
            }
        } );
        
        // Button voor 1 tick
        tick = new JButton("+1 minuut");
        tick.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                simulatorLogic.tick(true);
            }
        } );
        
       // Button voor 60 ticks
        ticks60 = new JButton("+1 uur");
        ticks60.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                simulatorLogic.ticks(60);
            }
        } );
        
     // Button voor 3600 (1 dag) ticks
        ticks1440 = new JButton("+1 dag");
        ticks1440.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                simulatorLogic.ticks(1440);
            }
        } );
        
      // Button voor reset knop
        reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                simulatorLogic.reset();
                simulatorLogic.tick(true);
                stop.setVisible(false);
                start.setVisible(true);
            }
        } );
        
     // Button voor een slider die de snelheid aanpast    
        slider.setPreferredSize(new Dimension(300, 25));
        slider.addChangeListener(new ChangeListener() {
     			public void stateChanged(ChangeEvent e) {
     				double simSnelheidDouble = (100 - slider.getValue());
     				simSnelheid = (int)simSnelheidDouble;
     				if(simSnelheid < 2) {
     					simSnelheid = 2;
     				}
     				SimulatorLogic.setTickPause(simSnelheid);
     				
     			}
     	});
        
        add(start);
        add(stop);
        add(tick);
        add(ticks60);
        add(ticks1440);
        add(reset);
        add(slider);

        setVisible(true);
        
        stop.setVisible(false);
    }
}
