package jules.osmium.player;

import jules.osmium.main.DrawPanel;
import jules.osmium.object.Vector;
import jules.osmium.renderer.Camera;

public class FirstPerson {
	private DrawPanel dp;
	private static double speed = 2;
	
	public FirstPerson(DrawPanel dp) {
		//this.dp = dp;
		System.out.println("LOGGING: Player initialized!");
		
	}
	
	public void update() {
		handleMovement();
	}
	
	public void handleMovement() {
		Camera.setYaw(0);
		Vector movVec = Camera.cameraDirection2D();

		switch(DrawPanel.keyInput.getDirectionAsString()) {
		case "FORWARD":
			Camera.moveByVector(movVec.multiply(speed));
			break;
			
		case "BACK":
			Camera.moveByVector(movVec.reverse().multiply(speed));
			break;
			
		case "LEFT":
			Camera.moveByVector(movVec.getLeftPerpendicular().multiply(speed));
			break;
			
		case "RIGHT":
			Camera.moveByVector(movVec.getRightPerpendicular().multiply(speed));
			break;
		case "FORWARD_RIGHT":
			Camera.moveByVector(movVec.add(movVec.getRightPerpendicular()).normalize().multiply(speed));
			break;
			
		case "FORWARD_LEFT":
			Camera.moveByVector(movVec.add(movVec.getLeftPerpendicular()).normalize().multiply(speed));
			break;
			
		case "BACK_RIGHT":
			movVec.reverse();
			Camera.moveByVector(movVec.add(movVec.getRightPerpendicular().reverse()).normalize().multiply(speed));
			break;
			
		case "BACK_LEFT":
			movVec.reverse();
			Camera.moveByVector(movVec.add(movVec.getLeftPerpendicular().reverse()).normalize().multiply(speed));
			break;
		}
	}
}
