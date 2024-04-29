package game;

import java.awt.geom.Point2D;

public class Target {
    private final Point2D.Double position = new Point2D.Double();

    public Target(double x, double y) {
        double rate = 3/2f;
        position.setLocation(x * rate, y * rate);
    }

    public Point2D.Double getPosition() {
        return position;
    }
}
