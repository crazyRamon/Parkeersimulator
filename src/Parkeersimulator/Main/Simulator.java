package Parkeersimulator.Main;

import Parkeersimulator.Controller.InitController;
import Parkeersimulator.Model.SimulatorLogic;
import Parkeersimulator.View.CarParkView;
import Parkeersimulator.View.BarChartView;
import Parkeersimulator.View.PieView;
import Parkeersimulator.View.LegendView;
import Parkeersimulator.View.QueueView;

import java.text.DecimalFormat;

import javax.swing.*;

public class Simulator {

    private JFrame screen;
    private SimulatorLogic simulatorLogic;
    private CarParkView carParkView;
    private InitController initController;
    private BarChartView barChartView;
    private PieView pieView;
    private LegendView legendView;
    private QueueView queueView;

    public Simulator (){
        simulatorLogic = new SimulatorLogic();
        carParkView = new CarParkView(simulatorLogic);
        barChartView = new BarChartView(simulatorLogic);
        initController = new InitController(simulatorLogic);
        pieView = new PieView(simulatorLogic);
        legendView = new LegendView(simulatorLogic);
        queueView = new QueueView(simulatorLogic);

        screen=new JFrame("Parkeergarage");
        screen.setSize(1600, 900);
        screen.setLayout(null);
        screen.getContentPane().add(carParkView);
        screen.getContentPane().add(barChartView);
        screen.getContentPane().add(pieView);
        screen.getContentPane().add(legendView);
        screen.getContentPane().add(queueView);
        screen.getContentPane().add(initController);
        carParkView.setBounds(400, 60, 750, 300);
        queueView.setBounds(400, 370, 710, 100);
        barChartView.setBounds(1150, 70, 400, 400);
        pieView.setBounds(90, 400, 210, 300);
        legendView.setBounds(70, 70, 270, 260);
        initController.setBounds(600, 700, 300, 150);
        screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        screen.setVisible(true);
        screen.setResizable(false);
        simulatorLogic.start();
        simulatorLogic.tick(true);
    }
}
