package gui;

import javax.swing.JInternalFrame;
import java.beans.PropertyVetoException;
import java.util.prefs.Preferences;

public abstract class AbstractWindowState extends JInternalFrame implements IFrameState {
    private static final String prefixWindowPreferences = "WINDOW PREFERENCES"; // WINDOW PREFERENCES
    private static final String prefixWindowPositionX = "POSITION X"; // POSITION X
    private static final String prefixWindowPositionY = "POSITION Y"; // POSITION Y
    private static final String prefixWindowSizeWidth = "SIZE WIDTH"; // SIZE WIDTH
    private static final String prefixWindowSizeHeight = "SIZE HEIGHT"; // SIZE HEIGHT
    private static final String prefixWindowIsMinimised = "IS MINIMISED"; // IS MINIMISED

    public AbstractWindowState(String string, boolean b1, boolean b2, boolean b3, boolean b4){
        super(string, b1, b2, b3, b4);
    }

    private static Preferences getPreferences() {
        return Preferences.userRoot().node(prefixWindowPreferences);
    }

    private static String formatTitle(String title) {
        String cased = title.toUpperCase();

        return cased.replaceAll(" +", "_");
    }

    public void saveWindow() {
        Preferences preferences = getPreferences();

        String title = formatTitle(this.getTitle());

        preferences.putInt(prefixWindowPositionX + title, this.getX());
        preferences.putInt(prefixWindowPositionY + title, this.getY());
        preferences.putInt(prefixWindowSizeWidth + title, this.getWidth());
        preferences.putInt(prefixWindowSizeHeight + title, this.getHeight());
        preferences.putBoolean(prefixWindowIsMinimised + title, this.isIcon());
    }

    public  void closeWindow() {
        Preferences preferences = getPreferences();
        final int missing = -1;

        String title = formatTitle(this.getTitle());

        int x = preferences.getInt(prefixWindowPositionX + title, missing);
        int y = preferences.getInt(prefixWindowPositionY + title, missing);
        int width = preferences.getInt(prefixWindowSizeWidth + title, missing);
        int height = preferences.getInt(prefixWindowSizeHeight + title, missing);
        boolean isMinimised = preferences.getBoolean(prefixWindowIsMinimised + title, false);

        if (x == -1 || y == -1 || width == -1 || height == -1)
            return;

        this.setBounds(x, y, width, height);

        try {
            this.setIcon(isMinimised);
        } catch (PropertyVetoException e) {
            e.printStackTrace(System.out);
        }
    }
}
