package jules.osmium.renderer;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import jules.osmium.main.DrawPanel;
import jules.osmium.main.KeyInput;
import jules.osmium.object.Point;
import jules.osmium.object.Vector;
import jules.osmium.utils.Raycast;
import jules.osmium.utils.RaycastHit;

public class Camera {
	private static double xAngle;
	private static double yAngle;
	private double depthOfView;
	public static double angularOffsetX = 0;
	static double yaw = -10;
	static double pitch = -10;

	private static double directionVectorX = 0;
	private static double directionVectorZ = 0;
	private static int rayStepX = 3;
	private static int rayStepY = 3;

	private static Point position = new Point(0, 0, 0);

	protected static KeyInput keyInput = new KeyInput();

	private Camera(double xAngle, double yAngle, double depthOfView) {
		Camera.xAngle = xAngle;
		Camera.yAngle = yAngle;
		this.depthOfView = depthOfView;
	}

	public double getXAngle() {
		return xAngle;
	}

	public static void changeX(double xAdd) {
		Camera.position.setX(Camera.position.getX() + xAdd);
	}

	public static void changeZ(double zAdd) {
		Camera.position.setZ(Camera.position.getZ() + zAdd);
	}

	public static void setYaw(double newYaw) {
		yaw = newYaw;
	}

	public static void setPitch(double newPitch) {
		pitch = newPitch;
	}

	public double getYAngle() {
		return yAngle;
	}

	public double getDepthOfView() {
		return depthOfView;
	}

	public static void setRayInterval(int newR) {
		rayStepX = newR;
		rayStepY = newR;
	}

	public static Point getPosition() {
		return position;
	}

	public static void setAngles(double xAngle, double yAngle) {
		Camera.xAngle = xAngle;
		Camera.yAngle = yAngle;
	}

	public static Vector cameraDirection2D() {
		Vector v = new Vector(directionVectorX, 0, directionVectorZ);

		v.normalize();

		return v;
	}

	public static void clampAngle(double upper, double lower) {
		if (-DrawPanel.mi.cameraPitch < lower) {
			DrawPanel.mi.cameraPitch = (float) lower;
		} else if (-DrawPanel.mi.cameraPitch > upper) {
			DrawPanel.mi.cameraPitch = (float) -upper;
		}
	}

	public static void moveByVector(Vector v) {
		position.setX(position.getX() + v.getX());
		position.setY(position.getY() + v.getY());
		position.setZ(position.getZ() + v.getZ());
	}

	static double yOffset = 0;

	public static void renderView(final int width, final int height, double depth, double fov, Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		yOffset -= 40;
		getPosition();

		Camera.setYaw(DrawPanel.mi.cameraYaw);
		Camera.setPitch(-DrawPanel.mi.cameraPitch);
		Camera.clampAngle(90, -90);

		// Mark width and height as final
		final BufferedImage view = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		double aspectRatio = (double) width / height;
		double horizontalFovRadians = Math.toRadians(fov);

		double angleX = 0.65 * Math.tan(horizontalFovRadians / 2) + 0.3;
		double angleY = angleX / aspectRatio;

		int numThreads = Runtime.getRuntime().availableProcessors();
		ExecutorService executor = Executors.newFixedThreadPool(numThreads);

		int segmentHeight = height / numThreads;

		for (int threadId = 0; threadId < numThreads; threadId++) {
			final int startHeight = threadId * segmentHeight;
			final int endHeight = (threadId == numThreads - 1) ? height : startHeight + segmentHeight;

			// Use lambda, ensuring width, height, and view are final
			executor.submit(() -> {
				Point[] rayTarget = new Point[6];
				for (int i = 0; i < rayTarget.length; i++) {
					rayTarget[i] = new Point(0, 0, 0);
				}

				for (int i = -width / 2; i < width / 2; i += rayStepX) {
					for (int j = startHeight - height / 2; j < endHeight - height / 2; j += rayStepY) {

						double originalX = i * angleX;
						double originalY = j * angleY;
						double originalZ = 200;

						double yAfterPitch = originalY * Math.cos(Math.toRadians(pitch))
								- originalZ * Math.sin(Math.toRadians(pitch));
						double zAfterPitch = originalY * Math.sin(Math.toRadians(pitch))
								+ originalZ * Math.cos(Math.toRadians(pitch));

						double xAfterYaw = originalX * Math.cos(Math.toRadians(yaw))
								+ zAfterPitch * Math.sin(Math.toRadians(yaw));
						double zAfterYaw = zAfterPitch * Math.cos(Math.toRadians(yaw))
								- originalX * Math.sin(Math.toRadians(yaw));

						directionVectorX = originalX * Math.sin(Math.toRadians(yaw));
						directionVectorZ = originalZ * Math.cos(Math.toRadians(yaw));

						rayTarget[0].setLocation(xAfterYaw, yAfterPitch, zAfterYaw);
						for (int k = 1; k < rayTarget.length; k++) {
							rayTarget[k].setLocation((xAfterYaw + k), yAfterPitch, zAfterYaw);
						}

						RaycastHit hit = Raycast.castRay(getPosition(), rayTarget[0], depth);
						if (hit != null) {
							int color = hit.getColor();

							for (int dx = 0; dx < rayStepX; dx++) {
								for (int dy = 0; dy < rayStepY; dy++) {
									int xPos = i + (width / 2) + dx;
									int yPos = j + (height / 2) + dy;
									if (xPos >= 0 && xPos < width && yPos >= 0 && yPos < height) {
										view.setRGB(xPos, yPos, color);
									}
								}
							}
						}
					}
				}
			});
		}

		executor.shutdown();
		try {
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

		g2.drawImage(RenderingToolkit.antiAliase(view), 0, 0, null);
		// g2.drawImage(view, 0, 0, null);
	}
	
}
