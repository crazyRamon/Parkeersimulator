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

        start = new JButton("Start");
        start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                simulatorLogic.start();
            }
        } );

        stop = new JButton("Stop");
        stop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                simulatorLogic.stop();
            }
        } );

        tick = new JButton("Tick");
        tick.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                simulatorLogic.tick();
            }
        } );
        
        
        ticks = new JButton("Ticks x100");
        ticks.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                simulatorLogic.ticks(100);
            }
        } );
        
        reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                simulatorLogic.reset();
                simulatorLogic.tick();
            }
        } );
        
     // slider
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
