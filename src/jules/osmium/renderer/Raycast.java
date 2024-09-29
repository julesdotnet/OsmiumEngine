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
        double normalStepSize = 4; 

        // Calculate total steps to take
        int steps = (int) Math.min(rayLength / normalStepSize, maxDistance / normalStepSize);

        for (int step = 0; step < steps; step++) {
            // Update current point using the direction vector
            currentPoint.setX(currentPoint.getX() + rayDirection.getX() * normalStepSize);
            currentPoint.setY(currentPoint.getY() + rayDirection.getY() * normalStepSize);
            currentPoint.setZ(currentPoint.getZ() + rayDirection.getZ() * normalStepSize);

            // Check intersection with all cuboids
            for (Cuboid cuboid : ObjectHandler.getCuboids()) {
                // Check if current point is within the cuboid's bounding box
                if (cuboid.contains(currentPoint)) {
                    return new RaycastHit(currentPoint, cuboid.getMaterial());
                }
            }
        }

        return null; // No intersection found
    }

}
