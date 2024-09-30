package jules.osmium.renderer;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import jules.osmium.object.Point;

public class Camera {
    private static double xAngle;
    private static double yAngle;
    private double depthOfView;
    public static double angularOffsetX = 0;
	static double yaw = -10;
	static double pitch = -10;

    private static Camera camera;

    // Camera position in 3D space
    private static Point position;

    private Camera(double xAngle, double yAngle, double depthOfView) {
        Camera.xAngle = xAngle;
        Camera.yAngle = yAngle;
        this.depthOfView = depthOfView; // Initialize camera position
        position = new Point(0, 0, 100); // Set initial camera position
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

    public static Point getPosition() {
        return position;
    }

    public static void setAngles(double xAngle, double yAngle) {
        Camera.xAngle = xAngle;
        Camera.yAngle = yAngle;
    }
    static double yOffset = 0;

    public static void renderView(int width, int height, double depth, Graphics g) {
    	yOffset -= 40;
    	
        BufferedImage view = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        double angleX = 1.3; // Horizontal field of view adjustment
        double angleY = 1.15 ; // Vertical field of view adjustment

        int numThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        // Calculate the height segment for each thread
        int segmentHeight = height / numThreads;

        // Split work between threads
        for (int threadId = 0; threadId < numThreads; threadId++) {
            final int startHeight = threadId * segmentHeight;
            final int endHeight = (threadId == numThreads - 1) ? height : startHeight + segmentHeight;

            executor.submit(() -> {
                Point[] rayTarget = new Point[6];
                for (int i = 0; i < rayTarget.length; i++) {
                    rayTarget[i] = new Point(0, 0, 0);
                }

                for (int i = -width / 2; i < width / 2; i += 6) {
                    for (int j = startHeight - height / 2; j < endHeight - height / 2; j++) {
                    	 double originalX = i * angleX; // X before rotation
                         double originalY = j * angleY; // Y before rotation
                         double originalZ = 200;        // Z before rotation (assuming 200 is some depth constant)

                         // First, apply yaw rotation around the Y-axis
                         double xAfterYaw = originalX * Math.cos(Math.toRadians(yaw)) + originalZ * Math.sin(Math.toRadians(yaw));
                         double zAfterYaw = originalZ * Math.cos(Math.toRadians(yaw)) - originalX * Math.sin(Math.toRadians(yaw));
                         double yAfterYaw = originalY;  // Y remains the same after yaw rotation

                         // Next, apply pitch rotation around the X-axis
                         double yAfterPitch = yAfterYaw * Math.cos(Math.toRadians(pitch)) - zAfterYaw * Math.sin(Math.toRadians(pitch));
                         double zAfterPitch = yAfterYaw * Math.sin(Math.toRadians(pitch)) + zAfterYaw * Math.cos(Math.toRadians(pitch));
                        
                         rayTarget[0].setLocation(xAfterYaw * angleX, yAfterPitch, zAfterPitch);
                         for (int k = 1; k < rayTarget.length; k++) {
                             rayTarget[k].setLocation((xAfterYaw + k) * angleX, yAfterPitch, zAfterPitch);
                         }

                        // Cast the ray and check for hits
                        RaycastHit[] hits = new RaycastHit[6];
                        for (int k = 0; k < rayTarget.length; k++) {
                            hits[k] = Raycast.castRay(new Point(0, 0, 0), rayTarget[k], depth);
                        }

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
