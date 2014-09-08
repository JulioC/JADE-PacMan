package sma.pacman.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;

public abstract class Engine extends JPanel {

    /* difference between time of update and world step time */
    float localTime = 0f;

    private Boolean canvasRationFractional;
    private Dimension panelSize;
    private Dimension canvasSize;
    private Point canvasPosition;

    /** Creates new form Engine */
    public Engine() {
        this.canvasSize = new Dimension(800, 600);
        this.canvasRationFractional = true;

        setBackground(Color.BLACK);
        setDoubleBuffered(true);

        setPreferredSize(canvasSize);

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resize();
            }
        });
    }

    public final void start(final float fixedTimeStep, final int maxSubSteps) {
        init();
        new Thread() {

            {
                setDaemon(true);
            }

            @Override
            public void run() {
                long start = System.nanoTime();
                while (true) {
                    long now = System.nanoTime();
                    float elapsed = (now - start) / 1000000000f;
                    start = now;

                    internalUpdate(elapsed, maxSubSteps, fixedTimeStep);

                    repaint();

                    if (1000000000 * fixedTimeStep - (System.nanoTime() - start) > 1000000) {
                        try {
                            Thread.sleep(0, 999999);
                        } catch (InterruptedException ex) {
                        }
                    }
                }
            }
        }.start();
    }

    private final void internalUpdate(float elapsedSeconds, int maxSubSteps, float fixedTimeStep) {
        int numSubSteps = 0;
        if (maxSubSteps != 0) {
            // fixed timestep with interpolation
            localTime += elapsedSeconds;
            if (localTime >= fixedTimeStep) {
                numSubSteps = (int) (localTime / fixedTimeStep);
                localTime -= numSubSteps * fixedTimeStep;
            }
        }
        if (numSubSteps != 0) {
            // clamp the number of substeps, to prevent simulation grinding spiralling down to a halt
            int clampedSubSteps = (numSubSteps > maxSubSteps) ? maxSubSteps : numSubSteps;
            for (int i = 0; i < clampedSubSteps; i++) {
                update(fixedTimeStep);
            }
        }
    }

    @Override
    public final void paint(Graphics g) {
        super.paint(g);

        try {
            int canvasWidth = canvasSize.width;
            int canvasHeight = canvasSize.height;

            BufferedImage image = new BufferedImage(canvasWidth, canvasHeight, BufferedImage.TYPE_INT_RGB);

            render(image.getGraphics(), localTime);

            int width = panelSize.width;
            int height = panelSize.height;

            int x = canvasPosition.x;
            int y = canvasPosition.y;

            g.drawImage(image, x, y, width, height, null);
        } catch (NullPointerException e) {
        } finally {
            g.dispose();
        }

        Toolkit.getDefaultToolkit().sync();
    }

    private void resize() {
        int mazeWidth = canvasSize.width;
        int mazeHeight = canvasSize.height;

        int width = getWidth();
        int height = getHeight();

        float widthRatio = (float)width / (float)mazeWidth;
        float heightRatio = (float)height / (float)mazeHeight;

        float minRatio = (widthRatio < heightRatio ? widthRatio : heightRatio);
        if(!canvasRationFractional) {
            minRatio = (int)minRatio;
        }

        int canvasWidth = (int)(mazeWidth * minRatio);
        int canvasHeight = (int)(mazeHeight * minRatio);

        int x = (width - canvasWidth) / 2;
        int y = (height - canvasHeight) / 2;

        panelSize = new Dimension(canvasWidth, canvasHeight);
        canvasPosition = new Point(x, y);
    }

    public void setCanvasRatioFractional(Boolean canvasRationFractional) {
        this.canvasRationFractional = canvasRationFractional;
    }

    public void setCanvasSize(Dimension canvasSize) {
        this.canvasSize = canvasSize;

        setPreferredSize(canvasSize);
    }

    protected abstract void init();

    protected abstract void update(float elapsedTime);

    protected abstract void render(Graphics g, float interpolationTime);

//    /**
//     * @param args the command line arguments
//     */
//    public static void main(String args[]) {
//        /* Create and display the form */
//        new Thread() {
//
//            {
//                setDaemon(true);
//                start();
//            }
//
//            public void run() {
//                while (true) {
//                    try {
//                        Thread.sleep(Integer.MAX_VALUE);
//                    } catch (Throwable t) {
//                    }
//                }
//            }
//        };
//        java.awt.EventQueue.invokeLater(new Runnable() {
//
//            @Override
//            public void run() {
//                Engine game = new Engine();
//                game.setVisible(true);
//                game.start(1 / 60f, 5);
//            }
//        });
//    }
}