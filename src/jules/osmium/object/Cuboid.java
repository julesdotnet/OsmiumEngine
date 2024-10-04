package jules.osmium.object;

public class Cuboid {
	private Point location;
	private double width;
	private double length;
	private double depth;
	private int color;
	private Point[] vertices = new Point[8];
	
	
	public Cuboid(Point location, double width, double length, double depth, int color) {
		this.location = location;
		this.width = width;
		this.length= length;
		this.depth = depth;
		this.color = color;
		setColor(color);
		generateVertices();
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
	
	public int getColor() {
		return color;
	}
	
	public void setColor(int newCol) {
		color = newCol;
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
	
	public Point[] getVertices() {
		return vertices;
	}
	
	private void generateVertices() {
		double startX = getLocation().getX();
		double startY = getLocation().getY();
		double startZ = getLocation().getZ();

		double width = getWidth(); 
		double height = getLength();
		double depth = getDepth();

		vertices[0] = new Point(startX, startY, startZ);
		vertices[1] = new Point(startX + width, startY, startZ);
		vertices[2] = new Point(startX, startY + height, startZ);
		vertices[3] = new Point(startX, startY, startZ + depth);
		vertices[4] = new Point(startX + width, startY + height, startZ);
		vertices[5] = new Point(startX + width, startY, startZ + depth);
		vertices[6] = new Point(startX, startY + height, startZ + depth);
		vertices[7] = new Point(startX + width, startY + height, startZ + depth);
	}

	public void setLocation(double x, double y, double z) {
		location = new Point(x, y, z);
		
	}
}
