package space;

import javax.swing.JFrame;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;

public class SpaceView extends JFrame implements MouseWheelListener,
        MouseMotionListener, KeyListener {
    public static final double EARTH_WEIGHT = 5.9736e24;
    static boolean IS_BOUNCING_BALLS = false;
    static boolean IS_BREAKOUT = false; // Opens bottom, only active if IS_BOUNCING_BALLS is true


    private static final long serialVersionUID = 1532817796535372081L;

    public static double seconds = 1;
    static double centrex = 0.0;
    static double centrey = 0.0;
    static double scale = 10;
    private static boolean showWake = false;
    private static int step = 0;
    private static int frameRate = 25;

    static JFrame frame;

    public SpaceView() {
        setBackground(Color.BLACK);
        SpaceView.frame = this;
    }

    public void paintPhysicalObject(PhysicalObject physicalObject, Graphics2D graphics) {
        if (!IS_BOUNCING_BALLS) {
            graphics.setColor(weightToColor(physicalObject.mass));
            int diameter = physicalObject.mass >= EARTH_WEIGHT * 10000 ? 7 : 2;
            int xtmp = (int) ((physicalObject.x - centrex) / scale + frame.getSize().width / 2);
            int ytmp = (int) ((physicalObject.y - centrey) / scale + frame.getSize().height / 2);
            graphics.fillOval(
                    xtmp-diameter/2,
                    ytmp-diameter/2,
                    diameter,
                    diameter);
        } else { //BREAKOUT
            graphics.setColor(Color.WHITE);
            int xtmp = (int) ((physicalObject.x - centrex)  + frame.getSize().width / 2);
            int ytmp = (int) ((physicalObject.y - centrey)  + frame.getSize().height / 2);
            graphics.fillOval(
                    (int) (xtmp - physicalObject.radius),
                    (int) (ytmp - physicalObject.radius),
                    (int) (2 * physicalObject.radius),
                    (int) (2 * physicalObject.radius));
        }
    }

    @Override
    public void paint(Graphics original) {
        if (original != null) {
            BufferedImage buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = buffer.createGraphics();

            if (!showWake) {
                graphics.clearRect(0, 0, getWidth(), getHeight());
            }
            for (PhysicalObject po : SpaceLogic.objects) {
                paintPhysicalObject(po, graphics);
                String string = "Objects:" + SpaceLogic.objects.size() + " scale:" + scale + " steps:" + step + " frame rate: " + frameRate;
                setTitle(string);
            }
            original.drawImage(buffer, 0, 0, getWidth(), getHeight(), null);
        }

    }

    public static Color weightToColor(double weight) {
        if (weight < 1e10) return Color.GREEN;
        if (weight < 1e12) return Color.CYAN;
        if (weight < 1e14) return Color.MAGENTA;
        if (weight < 1e16) return Color.BLUE;
        if (weight < 1e18) return Color.GRAY;
        if (weight < 1e20) return Color.RED;
        if (weight < 1e22) return Color.ORANGE;
        if (weight < 1e25) return Color.PINK;
        if (weight < 1e28) return Color.YELLOW;
        return Color.WHITE;
    }

    public static void main(String[] args) throws InterruptedException, InvocationTargetException {
        final SpaceView spaceView = new SpaceView();
        spaceView.addMouseWheelListener(spaceView);
        spaceView.addMouseMotionListener(spaceView);
        spaceView.addKeyListener(spaceView);
        spaceView.setSize(800, 820);

        SpaceLogic.mainLogic(spaceView, 75);
        spaceView.setVisible(true);
        while (true) {
            final long start = System.currentTimeMillis();
            EventQueue.invokeAndWait(new Runnable() {
                public void run() {
                    SpaceLogic.collide();
                    spaceView.step();
                }
            });
            try {
                long ahead = 1000 / frameRate - (System.currentTimeMillis() - start);
                if (ahead > 50) {
                    Thread.sleep(ahead);
                    if(frameRate<25) frameRate++;
                } else {
                    Thread.sleep(50);
                    frameRate--;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void setStepSize(double seconds) {
        SpaceView.seconds = seconds;
    }

    public static PhysicalObject add(double weightKilos, double x, double y,
                                     double vx, double vy, double radius) {
        PhysicalObject physicalObject = new PhysicalObject(weightKilos, x, y,
                vx, vy, radius);
        SpaceLogic.objects.add(physicalObject);
        return physicalObject;
    }

    public void step() {
        SpaceLogic.logicStep();
        step++;
        paint(getGraphics());
    }


    public void mouseWheelMoved(final MouseWheelEvent e) {
        if (!IS_BOUNCING_BALLS) {
            scale = scale + scale * (Math.min(9, e.getWheelRotation())) / 10 + 0.0001;
            getGraphics().clearRect(0, 0, getWidth(), getHeight());
        }
    }

    private static Point lastDrag = null;


    public void mouseDragged(final MouseEvent e) {
        if (!IS_BOUNCING_BALLS) {
            if (lastDrag == null) {
                lastDrag = e.getPoint();
            }
            centrex = centrex - ((e.getX() - lastDrag.x) * scale);
            centrey = centrey - ((e.getY() - lastDrag.y) * scale);
            lastDrag = e.getPoint();
            getGraphics().clearRect(0, 0, getWidth(), getHeight());
        }
    }


    public void mouseMoved(MouseEvent e) {
        lastDrag = null;
    }


    public void keyPressed(KeyEvent e) {
    }


    public void keyReleased(KeyEvent e) {
    }


    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == 'w')
            showWake = !showWake;
    }

}
