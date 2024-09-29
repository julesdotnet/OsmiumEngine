package jules.osmium.renderer;

import jules.osmium.object.Point;

public class RaycastHit {
	private int color;
	private Point point;
	private double rayDistance;
	
	public RaycastHit(Point point, int color) {
		System.out.println("casted ray collided at x: " + point.getX() + " y: " + point.getY() + " z: " + point.getZ());
		System.out.println("The material is " + color);
		this.color = color;
		this.point = point;
	}
	
	public int getColor() {
		return color;
	}

	public Point getPoint() {
		return point;
	}
	
	public double getDistance(Point origin) {
		return point.distanceTo(origin);
	}
}
