package gui;

import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.*;

import game.GameWindow;
import game.RobotLogic;
import log.Logger;

/**
 * Что требуется сделать:
 * 1. Метод создания меню перегружен функционалом и трудно читается. 
 * Следует разделить его на серию более простых методов (или вообще выделить отдельный класс).
 *
 */
public class MainApplicationFrame extends JFrame
{
    //Locale eng = new Locale("en","US");
    private final String lang = readLoc();
    private final ResourceBundle bundle =
            ResourceBundle.getBundle("resources", new Locale(lang)); // eng
    private final JDesktopPane desktopPane = new JDesktopPane();

    private String readLoc(){
        String lang;
        try {
            BufferedReader reader = new BufferedReader(new FileReader("D:\\intellij\\code\\Robots\\robots\\src\\LangData.txt"));
            lang = reader.readLine();
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return lang;
    }

    public class WindowMenu extends JMenuBar
    {
        private WindowMenu(){
            add(createLookAndFeelMenu());
            add(createTestMenu());
            add(createSwitchLanguage());
            add(createSaveMenu());
            add(createExitMenu());
        }

        private JMenuItem createJMenuItem(String s, Integer m, ActionListener l) {
            JMenuItem item = new JMenuItem(s, m);
            item.addActionListener(l);
            return item;
        }

        private JMenu createJMenu(String s, Integer m, String desc) {
            JMenu menu = new JMenu(s);
            menu.setMnemonic(m);
            menu.getAccessibleContext().setAccessibleDescription(desc);
            return menu;
        }

        private JMenu createSwitchLanguage(){
            JMenu switchLanguage = createJMenu(bundle.getString("switch.language"), KeyEvent.VK_X, "");

            switchLanguage.add(createJMenuItem("English",
                    KeyEvent.VK_X,
                    ee -> {
                try {
                BufferedWriter writer = new BufferedWriter(new FileWriter("D:\\intellij\\code\\Robots\\robots\\src\\LangData.txt"));
                writer.write("en_US");
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            JOptionPane.showMessageDialog(MainApplicationFrame.this, bundle.getString("app.rest"));
                    }));

            switchLanguage.add(createJMenuItem("Русский",
                    KeyEvent.VK_X,
                    ee ->
                    {
                        try {
                            BufferedWriter writer = new BufferedWriter(new FileWriter("D:\\intellij\\code\\Robots\\robots\\src\\LangData.txt"));
                            writer.write("ru_RU");
                            writer.close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        JOptionPane.showMessageDialog(MainApplicationFrame.this, bundle.getString("app.rest"));
                    }));

            return switchLanguage;
        }

        private JMenu createLookAndFeelMenu(){
            JMenu lookAndFeelMenu = createJMenu(bundle.getString("lookAndFeelMenu.s"), // Режим отображения
                    KeyEvent.VK_V,
                    bundle.getString("lookAndFeelMenu.getAccessibleContext")); // Управление режимом отображения приложения

            lookAndFeelMenu.add(createJMenuItem(bundle.getString("systemLookAndFeel.text"), // Системная схема
                    KeyEvent.VK_S,
                    e -> MainApplicationFrame.this
                            .setLookAndFeel(UIManager.getSystemLookAndFeelClassName())));

            lookAndFeelMenu.add(createJMenuItem(bundle.getString("crossplatformLookAndFeel.text"), // Универсальная схема
                    KeyEvent.VK_S,
                    e -> MainApplicationFrame.this
                                    .setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName())));

            return lookAndFeelMenu;
        }

        private JMenu createTestMenu(){
            JMenu testMenu = createJMenu(bundle.getString("testMenu.s"), // Тесты
                    KeyEvent.VK_T,
                    bundle.getString("testMenu.getAccessibleContext")); // Тестовые команды

            testMenu.add(createJMenuItem(bundle.getString("addLogMessageItem.text"), // Сообщение в лог
                    KeyEvent.VK_S,
                    e -> Logger
                            .debug(bundle.getString("Logger.debug.strMessage.addLine")))); // Новая строка

            return testMenu;
        }

        private JMenu createSaveMenu(){
            JMenu saveMenu = createJMenu(bundle.getString("saveMenu.s"),
                    KeyEvent.VK_M,
                    bundle.getString("saveMenu.getAccessibleContext"));
            saveMenu.add(createJMenuItem(bundle.getString("saveMenu.s"),
                    KeyEvent.VK_Q,
                    e -> MainApplicationFrame.this.saveWindows()));
            return saveMenu;
        }

        private JMenu createExitMenu(){
            JMenu exitMenu = createJMenu(bundle.getString("exitMenu.s"), // Выход
                    KeyEvent.VK_Q,
                    bundle.getString("exitMenuItem.text")); // Выйти из приложения

            exitMenu.add(createJMenuItem(bundle.getString("exitMenu.getAccessibleContext"), // Выход из приложения
                    KeyEvent.VK_Q,
                    e -> MainApplicationFrame.this.processWindowEvent(
                            new WindowEvent(
                                    MainApplicationFrame.this, WindowEvent.WINDOW_CLOSING))));

            return exitMenu;
        }
    }

    private void saveWindows() {
        for (var frame : desktopPane.getAllFrames()) {
            if (frame instanceof IFrameState)
                ((IFrameState) frame).saveWindow();
        }
    }
    
    public MainApplicationFrame() {
        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        int inset = 50;        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
            screenSize.width  - inset*2,
            screenSize.height - inset*2);

        setContentPane(desktopPane);

        var logic = new RobotLogic();
        addWindow(new game.RobotInfo(logic), 300, 300);

        addWindow(createLogWindow());
        addWindow(new GameWindow(bundle.getString("gameWindow.title"), logic),
                400, 400);

        for (var frame : desktopPane.getAllFrames())
            if (frame instanceof IFrameState)
                ((IFrameState) frame).closeWindow();

        setJMenuBar(new WindowMenu());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        Object[] options = { bundle.getString("confirm.yes"), bundle.getString("confirm.no") }; // Да, Нет
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int confirmed = JOptionPane.showOptionDialog(MainApplicationFrame.this, bundle.getString("confirm.title"),
                        bundle.getString("confirm.message"), JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                // Закрыть окно? Подтверждение
                if (confirmed == JOptionPane.YES_OPTION) {
                    int confirm = JOptionPane.showOptionDialog(MainApplicationFrame.this, bundle.getString("save.message"),
                            bundle.getString("saveMenu.s"), JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                    if (confirm == JOptionPane.YES_OPTION) {
                        saveWindows();
                    }
                    setVisible(false);
                    Arrays.asList(desktopPane.getAllFrames()).forEach(JInternalFrame::dispose);
                    dispose();
                }
            }
        });
    }

    protected LogWindow createLogWindow()
    {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource(), bundle.getString("logWindow.title"));
        logWindow.setLocation(10,10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug(bundle.getString("Logger.debug.strMessage.status")); // Протокол работает
        return logWindow;
    }
    
    protected void addWindow(JInternalFrame frame)
    {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    private void addWindow(JInternalFrame frame, int width, int height) {
        frame.setSize(width, height);
        addWindow(frame);
    }
    
//    protected JMenuBar createMenuBar() {
//        JMenuBar menuBar = new JMenuBar();
// 
//        //Set up the lone menu.
//        JMenu menu = new JMenu("Document");
//        menu.setMnemonic(KeyEvent.VK_D);
//        menuBar.add(menu);
// 
//        //Set up the first menu item.
//        JMenuItem menuItem = new JMenuItem("New");
//        menuItem.setMnemonic(KeyEvent.VK_N);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(
//                KeyEvent.VK_N, ActionEvent.ALT_MASK));
//        menuItem.setActionCommand("new");
////        menuItem.addActionListener(this);
//        menu.add(menuItem);
// 
//        //Set up the second menu item.
//        menuItem = new JMenuItem("Quit");
//        menuItem.setMnemonic(KeyEvent.VK_Q);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(
//                KeyEvent.VK_Q, ActionEvent.ALT_MASK));
//        menuItem.setActionCommand("quit");
////        menuItem.addActionListener(this);
//        menu.add(menuItem);
// 
//        return menuBar;
//    }
/*
    private JMenuBar generateMenuBar()
    {
        JMenuBar menuBar = new JMenuBar();
        
        JMenu lookAndFeelMenu = new JMenu("Режим отображения");
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        lookAndFeelMenu.getAccessibleContext().setAccessibleDescription(
                "Управление режимом отображения приложения");
        
        {
            JMenuItem systemLookAndFeel = new JMenuItem("Системная схема", KeyEvent.VK_S);
            systemLookAndFeel.addActionListener((event) -> {
                setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                this.invalidate();
            });
            lookAndFeelMenu.add(systemLookAndFeel);
        }

        {
            JMenuItem crossplatformLookAndFeel = new JMenuItem("Универсальная схема", KeyEvent.VK_S);
            crossplatformLookAndFeel.addActionListener((event) -> {
                setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                this.invalidate();
            });
            lookAndFeelMenu.add(crossplatformLookAndFeel);
        }

        JMenu testMenu = new JMenu("Тесты");
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription(
                "Тестовые команды");
        
        {
            JMenuItem addLogMessageItem = new JMenuItem("Сообщение в лог", KeyEvent.VK_S);
            addLogMessageItem.addActionListener((event) -> {
                Logger.debug("Новая строка");
            });
            testMenu.add(addLogMessageItem);
        }

        menuBar.add(lookAndFeelMenu);
        menuBar.add(testMenu);
        return menuBar;
    }
   */
    private void setLookAndFeel(String className)
    {
        try
        {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        }
        catch (ClassNotFoundException | InstantiationException
            | IllegalAccessException | UnsupportedLookAndFeelException e)
        {
            // just ignore
        }
    }
}
