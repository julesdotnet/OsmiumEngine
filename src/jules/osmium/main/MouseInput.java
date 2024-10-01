package jules.osmium.main;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import jules.osmium.renderer.Camera;

public class MouseInput implements MouseMotionListener {
	
	 public float cameraYaw = 0.0f;
	    private int lastMouseX = -1;

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	 @Override
     public void mouseMoved(MouseEvent e) {
         // If this is the first movement, initialize lastMouseX
         if (lastMouseX == -1) {
             lastMouseX = e.getX();
             return;
         }

         // Calculate the difference in X (delta X)
         int deltaX = e.getX() - lastMouseX;
         lastMouseX = e.getX();  // Update last mouse position

         // Adjust yaw based on deltaX (you can scale it for smoother rotation)
         float sensitivity = 0.2f;  // Sensitivity for how much movement affects the yaw
         cameraYaw += deltaX * sensitivity;

         System.out.println("Camera yaw: " + cameraYaw);

         Camera.setYaw(cameraYaw);
         Camera.setYaw(50);

     }

	
}
