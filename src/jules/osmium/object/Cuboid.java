package jules.osmium.object;

import java.awt.Graphics2D;

public class Cuboid {
	private Point location;
	private double width;
	private double length;
	private double depth;
	private String material;
	
	public Cuboid(Point location, double width, double length, double depth) {
		this.location = location;
		this.width = width;
		this.length= length;
		this.depth = depth;
		setMaterial("RED");
	}
	
	public Point getLocation() {
		return location;
	}
	
	public double getWidth() {
		return width;
	}
	
	public double getLength() {
		return length;
	}
	
	public double getDepth() {
		return depth;
	}
	
	public String getMaterial() {
		return material;
	}
	
	public void setMaterial(String newMat) {
		material = newMat;
	}
	
	public boolean contains(Point point) {
	    double minX = location.getX();
	    double minY = location.getY();
	    double minZ = location.getZ();
	    
	    double maxX = location.getX() + width;
	    double maxY = location.getY() + length;
	    double maxZ = location.getZ() + depth;

	    // Check if the point is within the cuboid's boundaries
	    return (point.getX() >= minX && point.getX() <= maxX) &&
	           (point.getY() >= minY && point.getY() <= maxY) &&
	           (point.getZ() >= minZ && point.getZ() <= maxZ);
	}
	
}
