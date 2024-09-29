package jules.osmium.renderer;

import jules.osmium.object.Cuboid;
import jules.osmium.object.ObjectHandler;
import jules.osmium.object.Point;
import jules.osmium.object.Vector;

public class Raycast {
	public static RaycastHit castRay(Point start, Point end, double maxDistance) {
		// Calculate the direction vector components
		double rayX = end.getX() - start.getX();
		double rayY = end.getY() - start.getY();
		double rayZ = end.getZ() - start.getZ();

		Vector rayDirection = new Vector(rayX, rayY, rayZ);
		double rayLength = rayDirection.length(); // Assume this method calculates the length directly

		if (rayLength == 0) {
			return null;
		}

		rayDirection.normalize();

		Point currentPoint = new Point(start.getX(), start.getY(), start.getZ());
		double normalStepSize = 5;
		double proximityStepSize = 0.1;

		// Calculate total steps to take
		int steps = (int) Math.min(rayLength / normalStepSize, maxDistance / normalStepSize);

		for (int step = 0; step < steps; step++) {
			// Update current point using the direction vector
			currentPoint.setX(currentPoint.getX() + rayDirection.getX() * normalStepSize);
			currentPoint.setY(currentPoint.getY() + rayDirection.getY() * normalStepSize);
			currentPoint.setZ(currentPoint.getZ() + rayDirection.getZ() * normalStepSize);
			
			for(Cuboid object : ObjectHandler.getCuboids()) {
				if(object.contains(currentPoint)) {
					currentPoint = new Point(start.getX(), start.getY(), start.getZ());
					
					while(!object.contains(currentPoint)) {
						currentPoint.setX(currentPoint.getX() + rayDirection.getX() * proximityStepSize);
						currentPoint.setY(currentPoint.getY() + rayDirection.getY() * proximityStepSize);
						currentPoint.setZ(currentPoint.getZ() + rayDirection.getZ() * proximityStepSize);
					}
					return new RaycastHit(currentPoint, object.getColor());
				}
			}

		}
		return null;
	}
}
