package eagledude4.ED4Utils;

import java.awt.*;
import java.util.LinkedList;

import org.powerbot.script.Locatable;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientAccessor;
import org.powerbot.script.rt4.ClientContext;

/**
 * MouseCamera is a class written for RSBot containing methods using the mouse (instead of keys) to perform camera
 * actions such as setting the yaw, pitch or turning to a locatable.
 * <p>
 * Note: Should only be used in fixed mode!
 *
 * @author Buccaneer
 */

public class MouseCamera extends ClientAccessor {
    private final static Rectangle canvas = new Rectangle(4, 53, 512, 334);
    private final static double ppdYaw = 2.8440;
    private final static double ppdPitch = 2.5926;

    public MouseCamera(ClientContext ctx) {
        super(ctx);
    }

    /**
     * Returns a boolean that indicates whether the camera successfully turned to given yaw and pitch with given
     * deviations.
     * <p>
     * This method should be preferred over turnTo(int yaw, int pitch) as it is less likely to be detected
     *
     * @param yaw   integer value that is the target yaw
     * @param pitch integer value that is the target pitch
     * @param ydev  integer value that is the maximum allowed deviation on the yaw
     * @param pdev  integer value that is the maximum allowed deviation on the pitch
     * @return      <tt>true</tt> if the camera is set towards the target yaw and pitch with given deviations
     * @see
     */

    public synchronized boolean turnTo(int yaw, int pitch, int ydev, int pdev){
    	int deltaYaw = (yaw - ctx.camera.yaw() + 180) % 360 - 180;
        if (deltaYaw < -180) deltaYaw += 360;
        int horizontal = (int) ((deltaYaw + Random.nextInt(-ydev,ydev)) * ppdYaw);
        int deltaPitch = pitch - ctx.camera.pitch();
        int vertical = (int) ((deltaPitch + Random.nextInt(-pdev, pdev) * ppdPitch));

        Point startPoint, endPoint;
        Rectangle startRect;

        if (horizontal > 0) { // Rect to the right
            if (vertical > 0) { // Rect to the top
                startRect = new Rectangle(canvas.x + horizontal, canvas.y, canvas.width - horizontal, canvas.height - vertical);
            } else { // Rect to the bottom
                startRect = new Rectangle(canvas.x + horizontal, canvas.y - vertical, canvas.width - horizontal, canvas.height + vertical);
            }
        } else { // Rect to the left
            if (vertical > 0) { // Rect to the top
                startRect = new Rectangle(canvas.x, canvas.y, canvas.width + horizontal, canvas.height - vertical);
            } else { // Rect to the bottom
                startRect = new Rectangle(canvas.x, canvas.y - vertical, canvas.width + horizontal, canvas.height + vertical);
            }
        }

        if (!startRect.contains(ctx.input.getLocation())) {
            final LinkedList<Point> startPoints = new LinkedList<>();

            for (int x = canvas.x; x <= canvas.x + canvas.width; x++) {
                for (int y = canvas.y; y <= canvas.y + canvas.height; y++) {
                    if (startRect.contains(x, y)) {
                        startPoints.add(new Point(x,y));
                    }
                }
            }

            startPoint = startPoints.get(Random.nextInt(0,startPoints.size()));
            endPoint = new Point(startPoint.x - horizontal, startPoint.y + vertical);
        } else {
            startPoint = ctx.input.getLocation();
            endPoint = new Point(ctx.input.getLocation().x - horizontal, ctx.input.getLocation().y + vertical);
        }

        if (!ctx.input.getLocation().equals(startPoint)) {
            ctx.input.move(startPoint);
        }
        
        ctx.input.drag(endPoint, 2);

        return ctx.input.getLocation().equals(endPoint);
    }
    
    public synchronized boolean turnTo(int yaw, int pitch) {
        return turnTo(yaw, pitch, 0, 0);
    }

    public synchronized boolean turnTo(Locatable loc, int ydev, int pdev) {
        return turnTo(getYawTo(loc), getPitchTo(loc), ydev, pdev);
    }

    public synchronized boolean turnTo(Locatable loc) {
        return turnTo(loc, 0, 0);
    }

    public synchronized boolean setYaw(int yaw, int ydev, int pdev) {
        return turnTo(yaw, ctx.camera.pitch(), ydev, pdev);
    }

    public synchronized boolean setYaw(int yaw) {
        return setYaw(yaw, 0, 0);
    }

    public synchronized boolean setPitch(int pitch, int ydev, int pdev) {
        return turnTo(ctx.camera.yaw(), pitch, ydev, pdev);
    }

    public synchronized boolean setPitch(int pitch) {
        return setPitch(pitch, 0, 0);
    }

    private int getYawTo(final Locatable locatable) {
        final Tile t = locatable.tile();
        final Tile me = ctx.players.local().tile();

        int yaw = ((int) Math.toDegrees(Math.atan2(t.y() - me.y(), t.x() - me.x()))) - 90;

        if (yaw < 0) yaw += 360;

        return yaw;
    }

    private int getPitchTo(final Locatable locatable) {
        final int distance = (int) ctx.players.local().tile().distanceTo(locatable);
        int pitch = 2;

        if (distance < 8) pitch = (int) (88 - (distance * 10.75 + Random.nextDouble(0, 10.75)));

        return pitch;
    }
}
