package jules.osmium.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import jules.osmium.object.Cuboid;
import jules.osmium.object.ObjectHandler;
import jules.osmium.object.Point;
import jules.osmium.renderer.Camera;

public class DrawPanel extends JPanel implements Runnable {
    private static final long serialVersionUID = 1L;
    int z = 0;

    private Thread renderThread;
    private volatile boolean running = true; // Control flag for the thread

    public DrawPanel() {
        setPreferredSize(new Dimension(800, 500));
        setBackground(Color.blue);
        setDoubleBuffered(true);
    }

    public void startRenderThread() {
        if (renderThread == null) {
            renderThread = new Thread(this);
            renderThread.start(); // Start the thread here
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // Always call the superclass method first
        Graphics2D g2 = (Graphics2D) g.create();

        // Create and spawn cuboids
        ObjectHandler.spawnCuboid(new Cuboid(new Point(102, 0, 0), 150, 60, 50));
        ObjectHandler.spawnCuboid(new Cuboid(new Point(102, 00, 400), 18, 60, 50));

        Camera camera = Camera.getInstance();

        // Position the camera to view the cuboids
        camera.setPosition(100, -10 + z, 10); // Adjust position if necessary
        camera.setAngles(90, 50); // Adjust angles if necessary
        
        z++;

        // Render the camera view
        Camera.renderView(getWidth(), getHeight(), g2);
        g2.dispose();
    }

    @Override
    public void run() {
        while (running) {
            // Update z and y for animation or movement // Move z inside for thread control
            // Ensure thread-safe repainting
            z++; // Update z position
            // y -= 5; // If y is needed for something, adjust accordingly
            
            // Request the panel to repaint on the Event Dispatch Thread
            SwingUtilities.invokeLater(this::repaint);

            try {
                Thread.sleep(300); // Control frame rate, adjust as necessary
            } catch (InterruptedException e) {
                running = false; // Stop the thread on interrupt
            }
        }
    }

    // Optional method to stop the render thread gracefully
    public void stopRenderThread() {
        running = false; // Set the running flag to false
        if (renderThread != null) {
            renderThread.interrupt(); // Interrupt the thread if it's sleeping
        }
    }
}
