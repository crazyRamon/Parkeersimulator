package Parkeersimulator.Main;

import Parkeersimulator.Controller.InitController;
import Parkeersimulator.Model.SimulatorLogic;
import Parkeersimulator.View.CarParkView;
import Parkeersimulator.View.GraphView;
import Parkeersimulator.View.BarChartView;
import Parkeersimulator.View.PieView;
import Parkeersimulator.View.LegendView;
import Parkeersimulator.View.QueueView;
import Parkeersimulator.View.TimeView;

import java.awt.Color;

import javax.swing.*;

/**
 * De klasse voor de simulator, wordt gebruikt om een nieuwe simulator te maken met de views erin
 * @author Andy Perukel, Ramon kits
 * @version 01-03-2019
 *
 */

public class Simulator {

    private JFrame screen;
    private SimulatorLogic simulatorLogic;
    private CarParkView carParkView;
    private BarChartView barChartView;
    private GraphView graphView;
    private PieView pieView;
    private LegendView legendView;
    private QueueView queueView;
    private TimeView timeView;
    private InitController initController;
	
    /**
     * Maakt een nieuwe simulator
     */
    
    public Simulator (){
        simulatorLogic = new SimulatorLogic();
        carParkView = new CarParkView(simulatorLogic);
        barChartView = new BarChartView(simulatorLogic);
        graphView = new GraphView(simulatorLogic);
        pieView = new PieView(simulatorLogic);
        legendView = new LegendView(simulatorLogic);
        queueView = new QueueView(simulatorLogic);
        timeView = new TimeView(simulatorLogic);
        initController = new InitController(simulatorLogic);

        screen=new JFrame("Parkeergarage");
        screen.getContentPane().setBackground(Color.LIGHT_GRAY);
        screen.setSize(1600, 955);
        screen.setLayout(null);
        screen.getContentPane().add(carParkView);
        screen.getContentPane().add(barChartView);
        screen.getContentPane().add(graphView);
        screen.getContentPane().add(pieView);
        screen.getContentPane().add(legendView);
        screen.getContentPane().add(queueView);
        screen.getContentPane().add(timeView);
        screen.getContentPane().add(initController);
        carParkView.setBounds(420, 120, 755, 335);
        queueView.setBounds(420, 10, 755, 100);
        barChartView.setBounds(1185, 10, 400, 445);
        initController.setBounds(1185, 465, 400, 445);
        pieView.setBounds(10, 465, 400, 445);
        legendView.setBounds(10, 120, 400, 335);
        timeView.setBounds(10, 10, 400, 100);
        graphView.setBounds(420, 465, 755, 445);
        screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        screen.setVisible(true);
        screen.setResizable(false);
        simulatorLogic.start();
        simulatorLogic.tick(true);
    }
}
