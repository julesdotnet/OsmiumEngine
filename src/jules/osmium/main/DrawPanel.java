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
	double z = 0;

	private long lastTime = System.nanoTime();
	private long lastFPSUpdateTime = System.nanoTime();
	private int frames = 0;
	int fps = 0;
	
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
		Cuboid testCuboid = new Cuboid(new Point(100 - x, 10, 30 + x), 40, 20, 60, Color.red.getRGB());
		ObjectHandler.spawnCuboid(testCuboid);
	}
	int x = 0;
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g.create();

		// Create and spawn cuboid
		for(Cuboid cuboid : ObjectHandler.getCuboids()) {
			cuboid.setLocation(cuboid.getLocation().getX() - x, cuboid.getLocation().getY(), cuboid.getLocation().getZ());
		}
		x++;
		Camera.renderView(getWidth(), getHeight(), 1200, g);
		g2.dispose();
	} 

	@Override
	public void run() {
		int TARGET_FPS = 120;
		final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;

		while (running) {
			long now = System.nanoTime();
			lastTime = now;

			frames++;
			if (now - lastFPSUpdateTime >= 1000000000) {
				fps = frames;
				frames = 0;
				lastFPSUpdateTime += 1000000000;
			}
			SwingUtilities.invokeLater(() -> repaint());
			try {
				long sleepTime = (lastTime - System.nanoTime() + OPTIMAL_TIME) / 1000000;
				if (sleepTime > 0) {
					Thread.sleep(sleepTime);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
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
