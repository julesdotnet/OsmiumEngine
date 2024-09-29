package jules.osmium.renderer;

import jules.osmium.object.Point;

public class RaycastHit {
	private String material;
	private Point point;
	private double rayDistance;
	
	public RaycastHit(Point point, String material) {
		System.out.println("casted ray collided at x: " + point.getX() + " y: " + point.getY() + " z: " + point.getZ());
		System.out.println("The material is " + material);
		this.material = material;
		this.point = point;
	}
	
	public String getMaterial() {
		return material;
	}

	public Point getPoint() {
		return point;
	}
	
	public double getDistance(Point origin) {
		return point.distanceTo(origin);
	}
}
