package Parkeersimulator.Controller;

import Parkeersimulator.Model.SimulatorLogic;

import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class InitController extends AbstractController{
    private JButton start;
    private JButton stop;
    private JButton tick;
    private JButton reset;
    private JButton ticks;
    
    // slider
    private int simSnelheid = 100;
    private JLabel titel = new JLabel("Pas de snelheid aan");
    private JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 50, 25);
    

    public InitController (SimulatorLogic simulatorLogic){
        super(simulatorLogic);

        // Button om de simulatie te starten
        start = new JButton("Start");
        start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                simulatorLogic.start();
            }
        } );

        // Button om de simulatie te stoppen
        stop = new JButton("Stop");
        stop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                simulatorLogic.stop();
            }
        } );
        
        // Button voor 1 tick
        tick = new JButton("Tick");
        tick.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                simulatorLogic.tick();
            }
        } );
        
       // Button voor 100 ticks
        ticks = new JButton("Ticks x100");
        ticks.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                simulatorLogic.ticks(100);
            }
        } );
        
      // Button voor reset knop
        reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                simulatorLogic.reset();
                simulatorLogic.tick();
            }
        } );
        
     // Button voor een slider die de snelheid aanpast
     		slider.setPaintTicks(true);
     		slider.setPaintLabels(true);
     		slider.setMajorTickSpacing(10);
     		slider.setMinorTickSpacing(1);
     		
             slider.addChangeListener(new ChangeListener() {
     			public void stateChanged(ChangeEvent e) {
     				
     				simSnelheid = slider.getValue();
     				simSnelheid *= 20;
     				if(simSnelheid < 3) {
     					simSnelheid = 3;
     				}
     				SimulatorLogic.setTickPause(simSnelheid);
     				
     			}
     		});        

        add(start);
        add(stop);
        add(tick);
        add(ticks);
        add(reset);
        add(slider);
        

        setVisible(true);
    }
}
