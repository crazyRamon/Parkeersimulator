package Parkeersimulator.Main;

import Parkeersimulator.Controller.InitController;
import Parkeersimulator.Model.ScreenLogic;
import Parkeersimulator.Model.SimulatorLogic;
import Parkeersimulator.View.CarParkView;

import javax.swing.*;
import java.awt.*;

public class Simulator {

    private JFrame screen;
    private SimulatorLogic simulatorLogic;
    private ScreenLogic screenLogic;
    private CarParkView carParkView;
    private InitController initController;

    public Simulator (){
        simulatorLogic = new SimulatorLogic();
        carParkView = new CarParkView(simulatorLogic);
        initController = new InitController(simulatorLogic);

        screen=new JFrame("Parkeergarage");
        screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container contentPane = screen.getContentPane();
        contentPane.add(carParkView, BorderLayout.CENTER);
        contentPane.add(initController, BorderLayout.SOUTH);
        screen.pack();
        screen.setVisible(true);
        screen.setResizable(false);
        simulatorLogic.start();
        simulatorLogic.tick(true);
    }
}
