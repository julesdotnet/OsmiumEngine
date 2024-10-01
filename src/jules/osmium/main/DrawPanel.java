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
	private volatile boolean running = true;

	public static KeyInput ki = new KeyInput();
	public static MouseInput mi = new MouseInput();

	public DrawPanel() {
		setPreferredSize(new Dimension(800, 500));
		setBackground(Color.black);
		setDoubleBuffered(true);
		addKeyListener(ki);
		addMouseMotionListener(mi);
		setFocusable(true);
		requestFocus();
		SwingUtilities.invokeLater(() -> requestFocusInWindow());

	}

	public void startRenderThread() {
		if (renderThread == null) {
			renderThread = new Thread(this);
			renderThread.start();
		}
		Cuboid testCuboid = new Cuboid(new Point(30, 5, 100), 80, 60, 60, Color.ORANGE.getRGB());
		ObjectHandler.spawnCuboid(testCuboid);
	}

	float x = 0;

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g.create();
		
		Camera.setYaw(0);

		for (Cuboid cuboid : ObjectHandler.getCuboids()) {
			if (DrawPanel.ki.getWPressed()) {
				cuboid.setLocation(cuboid.getLocation().getX(), cuboid.getLocation().getY(),
						cuboid.getLocation().getZ() - 1);
			}
			
			if (DrawPanel.ki.getSPressed()) {
				cuboid.setLocation(cuboid.getLocation().getX(), cuboid.getLocation().getY(),
						cuboid.getLocation().getZ() + 1);
			}
			if (DrawPanel.ki.getAPressed()) {
				cuboid.setLocation(cuboid.getLocation().getX() + 2, cuboid.getLocation().getY(),
						cuboid.getLocation().getZ());
			}
			
			if (DrawPanel.ki.getDPressed()) {
				cuboid.setLocation(cuboid.getLocation().getX() - 2, cuboid.getLocation().getY(),
						cuboid.getLocation().getZ());
			}
		}
		Camera.renderView(getWidth(), getHeight(), 1200, 60, g);
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

			if (ki.getAPressed()) {
				System.out.println("a pressed");
			}
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
