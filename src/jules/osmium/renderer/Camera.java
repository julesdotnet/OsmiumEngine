package jules.osmium.renderer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import jules.osmium.object.Point;

public class Camera {
	private static double xAngle; // Horizontal angle (yaw)
	private static double yAngle; // Vertical angle (pitch)
	private double depthOfView;
	public static double angularOffsetX = 0;

	private static Camera camera;

	// Camera position in 3D space
	private static Point position;

	private Camera(double xAngle, double yAngle, double depthOfView) {
		Camera.xAngle = xAngle;
		Camera.yAngle = yAngle;
		this.depthOfView = depthOfView; // Initialize camera position // Adjust position if necessary
	}

	public static Camera getInstance() {
		if (camera == null) {
			camera = new Camera(60, 45, 200);
		}
		return camera;
	}

	public double getXAngle() {
		return xAngle;
	}

	public double getYAngle() {
		return yAngle;
	}

	public double getDepthOfView() {
		return depthOfView;
	}

	public static Point getPosition() {
		return position;
	}

	public static void setAngles(double xAngle, double yAngle) {
		Camera.xAngle = xAngle;
		Camera.yAngle = yAngle;
	}

	public static void renderView(int width, int height, double depth, Graphics g) {
		BufferedImage view = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		double angleX = 1.4; // Horizontal field of view adjustment
		double angleY = 1.18; // Vertical field of view adjustment

		// Create a thread pool with a number of threads equal to available processors
		int numThreads = Runtime.getRuntime().availableProcessors();
		ExecutorService executor = Executors.newFixedThreadPool(numThreads);

		// Calculate the height segment for each thread
		int segmentHeight = height / numThreads;

		// Split work between threads
		for (int threadId = 0; threadId < numThreads; threadId++) {
			final int startHeight = threadId * segmentHeight;
			final int endHeight = (threadId == numThreads - 1) ? height : startHeight + segmentHeight;

			executor.submit(() -> {
				Point rayTarget[] = { new Point(0, 0, 0), new Point(0, 0, 0), new Point(0, 0, 0), new Point(0, 0, 0),
						new Point(0, 0, 0), new Point(0, 0, 0) };
				for (int i = -width / 2; i < width / 2; i += 6) {
					for (int j = startHeight - height / 2; j < endHeight - height / 2; j++) {
						
						rayTarget[0].setLocation(i * angleX, j * angleY, 200);
						rayTarget[1].setLocation((i + 1) * angleX, j * angleY, 200);
						rayTarget[2].setLocation((i + 2) * angleX, j * angleY, 200);
						rayTarget[3].setLocation((i + 3) * angleX, j * angleY, 200);
						rayTarget[4].setLocation((i + 4) * angleX, j * angleY, 200);
						rayTarget[5].setLocation((i + 5) * angleX, j * angleY, 200);

						// Cast the ray and check for hits
						RaycastHit[] hits = { Raycast.castRay(new Point(0, 0, 0), rayTarget[0], depth),
								Raycast.castRay(new Point(0, 0, 0), rayTarget[1], depth),
								Raycast.castRay(new Point(0, 0, 0), rayTarget[2], depth),
								Raycast.castRay(new Point(0, 0, 0), rayTarget[0], depth),
								Raycast.castRay(new Point(0, 0, 0), rayTarget[1], depth),
								Raycast.castRay(new Point(0, 0, 0), rayTarget[2], depth)};

						try {
							int addedOffset = 0;
							for (RaycastHit hit : hits) {
								if (hit == null) {
									continue;
								}
								try {
									if (i + (width / 2) + addedOffset >= width) {
										continue;
									}
									view.setRGB(i + (width / 2) + addedOffset, j + height / 2, hit.getColor());
								} catch (ArrayIndexOutOfBoundsException e) {
									e.printStackTrace();
								}
								addedOffset++;
							}
						} catch (NullPointerException e) {
							e.printStackTrace();
						}
					}
				}
			});
		}

		// Wait for all threads to complete
		executor.shutdown();
		try {
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

		// Draw the generated BufferedImage onto the provided Graphics object
		g.drawImage(view, 0, 0, null);
	}

}
