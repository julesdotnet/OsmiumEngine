package jules.osmium.object;

public class Vector {
	private double x;
	private double y;
	private double z;
	
	
	public Vector(double rayX, double rayY, double rayZ) {
		this.x = rayX;
		this.y = rayY;
		this.z = rayZ;
	}
	
	public double length() {
		 return Math.sqrt(x * x + y * y + z * z);
	}
	
	public double getX() {
		return x;
	}
	
	 public void normalize() {
	        double len = length(); // Get the length of the vector
	        if (len != 0) { // Avoid division by zero
	            x /= len; // Normalize x component
	            y /= len; // Normalize y component
	            z /= len; // Normalize z component
	        }
	    }

	
	public double getY() {
		return y;
	}
	
	public double getZ() {
		return z;
	}
}
