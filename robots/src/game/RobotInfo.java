package game;

import gui.AbstractWindowState;

import java.awt.*;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JLabel;

public class RobotInfo extends AbstractWindowState implements Observer {
    private final JLabel label;


    public RobotInfo(RobotLogic logic) {
        super();
        this.label = new JLabel();

        logic.addObserver(this);
        setResizable(true);
        setClosable(true);
        setMaximizable(true);
        setIconifiable(true);

        getContentPane().add(label, BorderLayout.CENTER);
        pack();
    }

    @Override
    public void update(Observable o, Object arg) {
        RobotLogic lg = (RobotLogic) o;
        label.setText("x=%f y=%f dir=%f".formatted(lg.getRobot().getPosition().getX(),
                lg.getRobot().getPosition().getY(), lg.getRobot().getDirection()));
    }
}
