package Parkeersimulator.Controller;

import Parkeersimulator.Model.SimulatorLogic;

import javax.swing.*;
import java.awt.event.*;

public class InitController extends AbstractController{
    private JButton start;
    private JButton stop;
    private JButton tick;
    private JButton reset;

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
        
        reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                simulatorLogic.reset();
            }
        } );

        add(start);
        add(stop);
        add(tick);
        add(reset);

        setVisible(true);
    }
}
