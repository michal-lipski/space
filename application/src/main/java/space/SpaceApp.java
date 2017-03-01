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

public class SpaceApp extends JFrame implements MouseWheelListener,
        MouseMotionListener, KeyListener {


    private static final long serialVersionUID = 1532817796535372081L;

    static double centrex = 0.0;
    static double centrey = 0.0;
    static double scale = 10;
    private static boolean showWake = false;
    private static int frameRate = 25;

    static JFrame frame;
    public final Space space = new Space();

    public SpaceApp() {
        setBackground(Color.BLACK);
        SpaceApp.frame = this;
    }

    public static void main(String[] args) throws InterruptedException, InvocationTargetException {
        final SpaceApp spaceApp = new SpaceApp();
        spaceApp.addMouseWheelListener(spaceApp);
        spaceApp.addMouseMotionListener(spaceApp);
        spaceApp.addKeyListener(spaceApp);
        spaceApp.setSize(800, 820);
        spaceApp.runApp();

    }

    static String spaceInfo(Space space) {
        return "Objects:" + space.getSize() + " scale:" + scale + " steps:" + space.getStep();
    }

    private void runApp() throws InvocationTargetException, InterruptedException {
        space.create(this.getWidth());
        setVisible(true);
        run();
    }

    private void run() throws InterruptedException, InvocationTargetException {
        while (true) {
            final long start = System.currentTimeMillis();
            EventQueue.invokeAndWait(new Runnable() {
                public void run() {
                    space.collide();
                    space.step();
                    paint(getGraphics());
                }
            });
            sleep(start);
        }
    }

    private void sleep(long start) {
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


    @Override
    public void paint(Graphics original) {
        if (original != null) {
            BufferedImage buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = buffer.createGraphics();

            if (!showWake) {
                graphics.clearRect(0, 0, getWidth(), getHeight());
            }
            for (PhysicalObject po : space.objects) {
                new SpaceGraphics().paintPhysicalObject(graphics,po);
                String string = spaceInfo(space) + " frame rate: " + frameRate;
                setTitle(string);
            }
            original.drawImage(buffer, 0, 0, getWidth(), getHeight(), null);
        }

    }


    public void mouseWheelMoved(final MouseWheelEvent e) {
        if (space.isNotBalls()) {
            scale = scale + scale * (Math.min(9, e.getWheelRotation())) / 10 + 0.0001;
            getGraphics().clearRect(0, 0, getWidth(), getHeight());
        }
    }

    private static Point lastDrag = null;


    public void mouseDragged(final MouseEvent e) {
        if (space.isNotBalls()) {
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
