package jules.osmium.utils;

import jules.osmium.object.Cuboid;
import jules.osmium.object.ObjectHandler;
import jules.osmium.object.Point;
import jules.osmium.object.Vector;

public class Raycast {
	public static RaycastHit castRay(Point start, Point end, double maxDistance) {
		double rayX = end.getX() - start.getX();
		double rayY = end.getY() - start.getY();
		double rayZ = end.getZ() - start.getZ();

		Vector rayDirection = new Vector(rayX, rayY, rayZ);
		double rayLength = rayDirection.length(); 

		if (rayLength == 0) {
			return null;
		}

		rayDirection.normalize();

		Point currentPoint = new Point(start.getX(), start.getY(), start.getZ());
		Point currentPoint2 = new Point(start.getX() + rayDirection.getX(), start.getY() + rayDirection.getY(), start.getZ() + rayDirection.getZ());
		Point currentPoint3 = new Point(start.getX() + rayDirection.getX() * 2, start.getY() + rayDirection.getY() * 2, start.getZ() + rayDirection.getZ() * 2);
		double normalStepSize = 3.5;
		double proximityStepSize = 0.1;

		int steps = (int) Math.min(rayLength / normalStepSize, maxDistance / normalStepSize);

		for (int step = 0; step < steps; step++) {
			currentPoint.setX(currentPoint.getX() + rayDirection.getX() * normalStepSize);
			currentPoint.setY(currentPoint.getY() + rayDirection.getY() * normalStepSize);
			currentPoint.setZ(currentPoint.getZ() + rayDirection.getZ() * normalStepSize);
			
			currentPoint2.setX(currentPoint2.getX() + rayDirection.getX() * normalStepSize);
			currentPoint2.setY(currentPoint2.getY() + rayDirection.getY() * normalStepSize);
			currentPoint2.setZ(currentPoint.getZ() + rayDirection.getZ() * normalStepSize);
			
			currentPoint3.setX(currentPoint2.getX() + rayDirection.getX() * normalStepSize);
			currentPoint3.setY(currentPoint2.getY() + rayDirection.getY() * normalStepSize);
			currentPoint3.setZ(currentPoint2.getZ() + rayDirection.getZ() * normalStepSize);
			
			for(Cuboid object : ObjectHandler.getCuboids()) { 
				if(object.contains(currentPoint)) {
					return new RaycastHit(currentPoint, object.getColor());
				}
				if(object.contains(currentPoint2)) {
					return new RaycastHit(currentPoint, object.getColor());
				}
				if(object.contains(currentPoint3)) {
					return new RaycastHit(currentPoint, object.getColor());
				}
			}
		}
		return null;
	}
}
