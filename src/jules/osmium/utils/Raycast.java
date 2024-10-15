package jules.osmium.utils;

import jules.osmium.object.Cuboid;
import jules.osmium.object.ObjectHandler;
import jules.osmium.object.Point;
import jules.osmium.object.Vector;

public class Raycast {

    // Grid field size
    public static final double GRID_SIZE = 40.0;

    // Small tolerance to handle precision issues
    private static final double EPSILON = 1e-6;

    public static RaycastHit castRay(Point start, Point end, double maxDistance) {
        // Ray direction
        double rayX = end.getX() - start.getX();
        double rayY = end.getY() - start.getY();
        double rayZ = end.getZ() - start.getZ();

        Vector rayDirection = new Vector(rayX, rayY, rayZ);
        double rayLength = rayDirection.length();

        if (rayLength == 0) {
            return null;
        }

        rayDirection.normalize();

        // Calculate the starting voxel
        int voxelX = (int) Math.floor(start.getX() / GRID_SIZE);
        int voxelY = (int) Math.floor(start.getY() / GRID_SIZE);
        int voxelZ = (int) Math.floor(start.getZ() / GRID_SIZE);

        int stepX = rayDirection.getX() > 0 ? 1 : -1;
        int stepY = rayDirection.getY() > 0 ? 1 : -1;
        int stepZ = rayDirection.getZ() > 0 ? 1 : -1;

        double tMaxX = calculateTMax(start.getX(), voxelX, rayDirection.getX(), stepX);
        double tMaxY = calculateTMax(start.getY(), voxelY, rayDirection.getY(), stepY);
        double tMaxZ = calculateTMax(start.getZ(), voxelZ, rayDirection.getZ(), stepZ);

        double tDeltaX = GRID_SIZE / Math.abs(rayDirection.getX());
        double tDeltaY = GRID_SIZE / Math.abs(rayDirection.getY());
        double tDeltaZ = GRID_SIZE / Math.abs(rayDirection.getZ());

        // Cast the ray through the voxel grid
        while (rayLength > 0) {
            // Check if the current voxel contains an object
            Point currentPoint = new Point(voxelX * GRID_SIZE, voxelY * GRID_SIZE, voxelZ * GRID_SIZE);
            for (Cuboid object : ObjectHandler.getCuboids()) {
                if (object.contains(currentPoint)) {
                    return new RaycastHit(currentPoint, object.getColor());
                }
            }

            // Move to the next voxel based on which tMax is the smallest
            if (tMaxX + EPSILON < tMaxY) {
                if (tMaxX + EPSILON < tMaxZ) {
                    voxelX += stepX;
                    tMaxX += tDeltaX;
                } else {
                    voxelZ += stepZ;
                    tMaxZ += tDeltaZ;
                }
            } else {
                if (tMaxY + EPSILON < tMaxZ) {
                    voxelY += stepY;
                    tMaxY += tDeltaY;
                } else {
                    voxelZ += stepZ;
                    tMaxZ += tDeltaZ;
                }
            }

            // Update remaining ray length
            rayLength -= Math.min(Math.min(tDeltaX, tDeltaY), tDeltaZ);

            if (rayLength > maxDistance) {
                return null;
            }
        }

        return null;
    }

    // Calculate tMax for the next voxel boundary
    private static double calculateTMax(double pos, int voxel, double rayDirection, int step) {
        if (rayDirection == 0) {
            return Double.POSITIVE_INFINITY;
        }

        double voxelBoundary = (step > 0) ? (voxel + 1) * GRID_SIZE : voxel * GRID_SIZE;
        return (voxelBoundary - pos) / rayDirection;
    }
}
