package jules.osmium.renderer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import jules.osmium.main.DrawPanel;
import jules.osmium.main.KeyInput;
import jules.osmium.object.Point;

public class Camera {
	private static double xAngle;
	private static double yAngle;
	private double depthOfView;
	public static double angularOffsetX = 0;
	static double yaw = -10;
	static double pitch = -10;

    private static int rayStepX = 4; 
    private static int rayStepY = 4;

	private static Camera camera;

	private static Point position = new Point(0, 0, 0);

	protected static KeyInput keyInput = new KeyInput();

	private Camera(double xAngle, double yAngle, double depthOfView) {
		Camera.xAngle = xAngle;
		Camera.yAngle = yAngle;
		this.depthOfView = depthOfView;
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

	static double yOffset = 0;

	public static void renderView(int width, int height, double depth, double fov, Graphics g) {
	    yOffset -= 40;
	    getPosition();

	    Camera.setYaw(DrawPanel.mi.cameraYaw);
	    Camera.setPitch(-DrawPanel.mi.cameraPitch);

	    BufferedImage view = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

	    double aspectRatio = (double) width / height;
	    double horizontalFovRadians = Math.toRadians(fov);

	    double angleX = 2 * Math.tan(horizontalFovRadians / 2);
	    double angleY = angleX / aspectRatio;

	    int numThreads = Runtime.getRuntime().availableProcessors();
	    ExecutorService executor = Executors.newFixedThreadPool(numThreads);

	    int segmentHeight = height / numThreads;

	    for (int threadId = 0; threadId < numThreads; threadId++) {
	        final int startHeight = threadId * segmentHeight;
	        final int endHeight = (threadId == numThreads - 1) ? height : startHeight + segmentHeight;

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

	                    double xAfterYaw = originalX * Math.cos(Math.toRadians(yaw))
	                            + originalZ * Math.sin(Math.toRadians(yaw));
	                    double zAfterYaw = originalZ * Math.cos(Math.toRadians(yaw))
	                            - originalX * Math.sin(Math.toRadians(yaw));
	                    double yAfterYaw = originalY;

	                    double yAfterPitch = yAfterYaw * Math.cos(Math.toRadians(pitch))
	                            - zAfterYaw * Math.sin(Math.toRadians(pitch));
	                    double zAfterPitch = yAfterYaw * Math.sin(Math.toRadians(pitch))
	                            + zAfterYaw * Math.cos(Math.toRadians(pitch));

	                    rayTarget[0].setLocation(xAfterYaw, yAfterPitch, zAfterPitch);
	                    for (int k = 1; k < rayTarget.length; k++) {
	                        rayTarget[k].setLocation((xAfterYaw + k), yAfterPitch, zAfterPitch);
	                    }

	                    RaycastHit hit = Raycast.castRay(getPosition(), rayTarget[0], depth);
	                    if (hit != null) {
	                        int color = hit.getColor();

	                        for (int dx = 0; dx < rayStepX; dx++) {
	                            for (int dy = 0; dy < rayStepY; dy++) {
	                                int xPos = i + (width / 2) + dx;
	                                int yPos = j + (height / 2) + dy;
	                                if (xPos < width && yPos < height) {
	                                    view.setRGB(xPos, yPos, color);
	                                }
	                            }
	                        }
	                    } else {
	                        int backgroundColor = Color.BLACK.getRGB();
	                        for (int dx = 0; dx < rayStepX; dx++) {
	                            for (int dy = 0; dy < rayStepY; dy++) {
	                                int xPos = i + (width / 2) + dx;
	                                int yPos = j + (height / 2) + dy;
	                                if (xPos < width && yPos < height) {
	                                    view.setRGB(xPos, yPos, backgroundColor);
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

	    g.drawImage(view, 0, 0, null);
	}

}
