package jules.osmium.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import jules.osmium.object.Cuboid;
import jules.osmium.object.ObjectHandler;
import jules.osmium.object.Point;
import jules.osmium.player.FirstPerson;
import jules.osmium.renderer.Camera;

public class DrawPanel extends JPanel implements Runnable {
	private static final long serialVersionUID = 1L;
	double z = 0;

	private long lastTime = System.nanoTime();
	private long lastFPSUpdateTime = System.nanoTime();
	private int frames = 0;
	int fps = 0;
	FirstPerson player = new FirstPerson(this);

	private Thread renderThread;
	private volatile boolean running = true;

	public static KeyInput keyInput = new KeyInput();
	public static MouseInput mi = new MouseInput();

	public DrawPanel() {
		setPreferredSize(new Dimension(800, 500));
		setBackground(Color.black);
		setDoubleBuffered(true);
		addKeyListener(keyInput);
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
		Cuboid testCuboid = new Cuboid(new Point(0, 0, 0), 1, 1, 1, Color.RED.getRGB());
		Cuboid testCuboid2= new Cuboid(new Point(1, 0, 0), 1, 1, 1, Color.CYAN.getRGB());
		Cuboid testCuboid3 = new Cuboid(new Point(-1, 0, 0), 1, 1, 1, Color.MAGENTA.getRGB());

		ObjectHandler.spawnCuboid(testCuboid);
		ObjectHandler.spawnCuboid(testCuboid2);
		ObjectHandler.spawnCuboid(testCuboid3);

	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g.create();

		player.update();

		Camera.renderView(getWidth(), getHeight(), 1200, 60, g);
		
		if(!KeyInput.getEscapeReleased()) {
			g2.setColor(Color.red);
			g2.setFont(new Font("Helvetica", Font.PLAIN, 30));
			g2.drawString("Paused", 10, 30);
		}
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
					Thread.sleep(10);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 
		}
	}

	public void stopRenderThread() {
		running = false;
		if (renderThread != null) {
			renderThread.interrupt();
		}
	}

}
