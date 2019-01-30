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
    private JLabel label;
    
    // slider
    private int simSnelheid = simulatorLogic.getTickPause();
    private JSlider slider = new JSlider(JSlider.HORIZONTAL, 2, 100, 50);
    

    public InitController (SimulatorLogic simulatorLogic){
        super(simulatorLogic);

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
            }
        } );
        
     // Button voor een slider die de snelheid aanpast
        label = new JLabel("simulatiesnelheid 20 min/sec");
        label.setPreferredSize(new Dimension(175, 15));
        slider.setPreferredSize(new Dimension(300, 25));
        slider.addChangeListener(new ChangeListener() {
     			public void stateChanged(ChangeEvent e) {
     				double simSnelheidDouble = (100 - slider.getValue());
     				simSnelheid = (int)simSnelheidDouble;
     				if(simSnelheid < 2) {
     					simSnelheid = 2;
     				}
     				label.setText("simulatiesnelheid " + (1000 / simSnelheid) + " min/sec");
     				SimulatorLogic.setTickPause(simSnelheid);
     				
     			}
     	});

        add(label);
        add(slider);
        add(start);
        add(stop);
        add(tick);
        add(ticks60);
        add(ticks1440);
        add(reset);

        setVisible(true);
        
        stop.setVisible(false);
    }
}
