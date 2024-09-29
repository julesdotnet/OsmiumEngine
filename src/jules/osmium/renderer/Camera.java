package jules.osmium.renderer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import jules.osmium.object.Point;

public class Camera {
    private static double xAngle;   // Horizontal angle (yaw)
    private static double yAngle;   // Vertical angle (pitch)
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
        
        Point rayTarget = new Point(0, 0, 0);
        
        // Iterate over pixel coordinates for rendering
        for (int i = -height / 2; i < height / 2; i++) {
            for (int j = -width / 2; j < width / 2; j++) {
                // Calculate ray target based on angles and current pixel
                rayTarget.setLocation(i * angleX, j * angleY, 200);
                
                // Cast the ray and check for hits
                RaycastHit hit = Raycast.castRay(new Point(0, 0, 0), rayTarget, depth);
                
                if (hit != null) {
                    int color = hit.getColor();
                    // Set the pixel color in the BufferedImage
                    try {
                    	view.setRGB(i + width / 2, j + height / 2, color);	
                    } catch(ArrayIndexOutOfBoundsException e) {
                    	e.printStackTrace();
                    }
                }
            }
        }
        
        // Draw the generated BufferedImage onto the provided Graphics object
        g.drawImage(view, 0, 0, null);
    }



}
