package gui;

import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.*;

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
    private String lang = readLoc();
    private final ResourceBundle bundle =
            ResourceBundle.getBundle("resources", new Locale(lang)); // eng
    private final JDesktopPane desktopPane = new JDesktopPane();

    private String readLoc(){
        String lang;
        try {
            BufferedReader reader = new BufferedReader(new FileReader("D:\\intellij\\code\\Robots\\robots\\src\\LangData.txt"));
            lang = reader.readLine();
            reader.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return lang;
    }

    public class WindowMenu extends JMenuBar
    {
        private WindowMenu(){
            JMenu lookMenu = lookAndFeelMenu();
            JMenu testingMenu = testMenu();
            JMenu extMenu = exitMenu();
            JMenu switchLoc = SwitchLanguage();

            add(lookMenu);
            add(testingMenu);
            add(switchLoc);
            add(extMenu);
        }

        private JMenu SwitchLanguage(){
            JMenu SwitchLanguage = new JMenu(bundle.getString("switch.language"));
            SwitchLanguage.setMnemonic(KeyEvent.VK_X);
            {
                JMenuItem englishLang = new JMenuItem("English");

                englishLang.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent ee) {
                        try {
                            BufferedWriter writer = new BufferedWriter(new FileWriter("D:\\intellij\\code\\Robots\\robots\\src\\LangData.txt"));
                            writer.write("en_US");
                            writer.close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        JOptionPane.showMessageDialog(MainApplicationFrame.this, bundle.getString("app.rest"));
                    }
                });

                SwitchLanguage.add(englishLang);
            }

            {
                JMenuItem rusLang = new JMenuItem("Русский");

                rusLang.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent ee) {
                        try {
                            BufferedWriter writer = new BufferedWriter(new FileWriter("D:\\intellij\\code\\Robots\\robots\\src\\LangData.txt"));
                            writer.write("ru_RU");
                            writer.close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        JOptionPane.showMessageDialog(MainApplicationFrame.this, bundle.getString("app.rest"));
                    }
                });

                SwitchLanguage.add(rusLang);
            }
            return SwitchLanguage;
        }

        private JMenu lookAndFeelMenu(){
            JMenu lookAndFeelMenu = new JMenu(bundle.getString("lookAndFeelMenu.s")); // Режим отображения
            lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
            lookAndFeelMenu.getAccessibleContext().setAccessibleDescription(
                    bundle.getString("lookAndFeelMenu.getAccessibleContext")); // Управление режимом отображения приложения

            {
                JMenuItem systemLookAndFeel = new JMenuItem(bundle.getString("systemLookAndFeel.text"), KeyEvent.VK_S); // Системная схема
                systemLookAndFeel.addActionListener((event) -> {
                    setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    this.invalidate();
                });
                lookAndFeelMenu.add(systemLookAndFeel);
            }

            {
                JMenuItem crossplatformLookAndFeel = new JMenuItem(bundle.getString("crossplatformLookAndFeel.text"), KeyEvent.VK_S); // Универсальная схема
                crossplatformLookAndFeel.addActionListener((event) -> {
                    setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                    this.invalidate();
                });
                lookAndFeelMenu.add(crossplatformLookAndFeel);
            }

            return lookAndFeelMenu;
        }

        private JMenu testMenu(){
            JMenu testMenu = new JMenu(bundle.getString("testMenu.s")); // Тесты
            testMenu.setMnemonic(KeyEvent.VK_T);
            testMenu.getAccessibleContext().setAccessibleDescription(
                    bundle.getString("testMenu.getAccessibleContext")); // Тестовые команды

            {
                JMenuItem addLogMessageItem = new JMenuItem(bundle.getString("addLogMessageItem.text"), KeyEvent.VK_S); // Сообщение в лог
                addLogMessageItem.addActionListener((event) -> {
                    Logger.debug(bundle.getString("Logger.debug.strMessage.addLine")); // Новая строка
                });
                testMenu.add(addLogMessageItem);
            }
            return testMenu;
        }

        private JMenu exitMenu(){
            JMenu exitMenu = new JMenu(bundle.getString("exitMenu.s")); // Выход
            exitMenu.setMnemonic(KeyEvent.VK_Q);
            exitMenu.getAccessibleContext().setAccessibleDescription(
                    bundle.getString("exitMenuItem.text") // Выйти из приложения
            );

            JMenuItem exitMenuItem = new JMenuItem(bundle.getString("exitMenu.getAccessibleContext"), KeyEvent.VK_Q); // Выход из приложения
            exitMenuItem.addActionListener((event) -> {
                MainApplicationFrame.this.processWindowEvent(
                        new WindowEvent(
                                MainApplicationFrame.this, WindowEvent.WINDOW_CLOSING));
            });
            exitMenu.add(exitMenuItem);
            return exitMenu;
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
        
        
        LogWindow logWindow = createLogWindow();
        addWindow(logWindow);

        GameWindow gameWindow = new GameWindow(bundle.getString("gameWindow.title"));
        gameWindow.setSize(400,  400);
        addWindow(gameWindow);

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
                    setDefaultCloseOperation(EXIT_ON_CLOSE);
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
