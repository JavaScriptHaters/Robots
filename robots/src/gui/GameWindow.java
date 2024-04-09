package gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;

public class GameWindow extends AbstractWindowState
{
    private final GameVisualizer m_visualizer;
    public GameWindow(String title, Robot robot)
    {
        super(title, true, true, true, true);
        m_visualizer = new GameVisualizer(robot);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }

    @Override
    public void dispose() {
        super.dispose();
        m_visualizer.stopTimer();
    }
}
