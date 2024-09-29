package jules.osmium.renderer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import jules.osmium.object.Point;

public class Camera {
    private double xAngle;   // Horizontal angle (yaw)
    private double yAngle;   // Vertical angle (pitch)
    private double depthOfView;

    private static Camera camera;

    // Camera position in 3D space
    private Point position;

    private Camera(double xAngle, double yAngle, double depthOfView) {
        this.xAngle = xAngle;
        this.yAngle = yAngle;
        this.depthOfView = depthOfView;
        this.position = new Point(0, 0, 0); // Initialize camera position
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

    public Point getPosition() {
        return position;
    }

    public void setPosition(double x, double y, double z) {
        this.position.setX(x);
        this.position.setY(y);
        this.position.setZ(z);
    }

    public void setAngles(double xAngle, double yAngle) {
        this.xAngle = xAngle;
        this.yAngle = yAngle;
    }

    // This method is optimized with multi-threading and thread pooling
    public static void renderView(int width, int height, Graphics g) {
        // Create a BufferedImage to store the rendered view
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Precompute constant offsets
        int offsetX = width / 2;  // Centering the view horizontally
        int offsetY = height / 2; // Centering the view vertically
        int rayDepth = 650;

        // Precompute trigonometric values for camera angles to avoid recalculating for each pixel
        double cosYAngle = Math.cos(Math.toRadians(camera.getYAngle()));
        double sinYAngle = Math.sin(Math.toRadians(camera.getYAngle()));
        double sinXAngle = Math.sin(Math.toRadians(camera.getXAngle()));

        // Use a thread pool to manage multiple threads efficiently
        int numThreads = Runtime.getRuntime().availableProcessors(); // Use all available cores
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        Point origin = camera.getPosition(); // Use camera's position

        // Split the workload into segments, each segment handled by a thread
        int segmentHeight = height / numThreads;

        for (int threadId = 0; threadId < numThreads; threadId++) {
            final int startY = threadId * segmentHeight;
            final int endY = (threadId == numThreads - 1) ? height : startY + segmentHeight;

            executor.submit(() -> {
                // This block will be executed by a separate thread
                Point rayTarget = new Point(0, 0, 50); // Local reusable rayTarget
                for (int y = startY; y < endY; y++) {
                    for (int x = 0; x < width; x++) {
                        // Update the ray target for the current pixel based on precomputed angles
                        double xOffset = (x - offsetX) * cosYAngle;
                        double zOffset = (x - offsetX) * sinYAngle;
                        double yOffset = (y - offsetY) * sinXAngle;

                        rayTarget.setX(origin.getX() + xOffset);
                        rayTarget.setY(origin.getY() + yOffset);
                        rayTarget.setZ(origin.getZ() + zOffset);

                        RaycastHit target = Raycast.castRay(origin, rayTarget, rayDepth);

                        if (target != null) {
                            // Calculate color based on the distance to the target point
                            double distance = target.getPoint().distanceTo(origin);
                            int color = (distance > 150) ? Color.RED.getRGB() : Color.YELLOW.getRGB();
                            color = (distance > 300) ? Color.GREEN.getRGB() : color;

                            // Set the color of the pixel in the buffered image
                            bufferedImage.setRGB(x, y, color);
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

        // Render the buffered image to the Graphics object in one step
        g.drawImage(bufferedImage, 0, 0, null);
    }
}
