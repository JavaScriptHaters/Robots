package gui;

import java.awt.*;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JLabel;

public class RobotInfo extends AbstractWindowState implements Observer {
    private final Robot robot;
    private final JLabel label;


    public RobotInfo(Robot robot) {
        super();
        this.robot = robot;
        this.label = new JLabel();

        robot.addObserver(this);
        setResizable(true);
        setClosable(true);
        setMaximizable(true);
        setIconifiable(true);

        getContentPane().add(label, BorderLayout.CENTER);
        pack();
    }

    @Override
    public void update(Observable o, Object arg) {
        label.setText(robot.log());
    }
}
