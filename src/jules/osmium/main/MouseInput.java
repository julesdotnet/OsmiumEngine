package jules.osmium.main;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class MouseInput implements MouseMotionListener {
	
	 public float cameraYaw = 0.0f;
	 public float cameraPitch = 0.0f;
	    private int lastMouseX = -1;
	    private int lastMouseY = -1;

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	 @Override
     public void mouseMoved(MouseEvent e) {
         if (lastMouseX == -1) {
             lastMouseX = e.getX();
             return;
         }
         if (lastMouseY == -1) {
             lastMouseY = e.getY();
             return;
         }

         int deltaX = e.getX() - lastMouseX;
         lastMouseX = e.getX(); 
         float sensitivity = 0.2f;
         cameraYaw += deltaX * sensitivity;

         int deltaY = e.getY() - lastMouseY;
         lastMouseY = e.getY();

         cameraPitch += deltaY * sensitivity;

     }	
}
