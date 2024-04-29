package game;

import gui.AbstractWindowState;

import java.awt.BorderLayout;

import javax.swing.JPanel;

public class GameWindow extends AbstractWindowState
{
    private final GameVisualizer m_visualizer;
    private final RobotLogic logic;
    public GameWindow(String title, RobotLogic logic)
    {
        super();

        this.logic = logic;
        logic.startTimer();

        setTitle(title);
        setResizable(true);
        setClosable(true);
        setMaximizable(true);
        setIconifiable(true);

        m_visualizer = new GameVisualizer(logic);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }

    @Override
    public void dispose() {
        super.dispose();
        logic.stopTimer();
    }
}
