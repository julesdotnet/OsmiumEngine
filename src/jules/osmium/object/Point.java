package jules.osmium.object;

public class Point {
	private double x;
	private double y;
	private double z;
	
	
	public Point(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public double distanceTo(Point other) {
		return Math.sqrt(Math.pow(getX() - other.getX(), 2) + Math.pow(getY() - other.getY(), 2) + Math.pow(getZ()- other.getZ(), 2));
	}
	
	
	
	public double getX() {
		return x;
	}
	
	public void setX(double newX) {
		x = newX;
	}
	
	public double getY() {
		return y;
	}
	
	public void setY(double newY) {
		y = newY;
	}
	
	public double getZ() {
		return z;
	}
	
	public void setZ(double newZ) {
		z = newZ;
	}

	public void setLocation(double x, double y, double z2) {
		setX(x);
		setY(y);
		setZ(z2);
		
	}
}
