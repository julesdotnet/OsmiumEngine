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
	        double len = length();
	        if (len != 0) {
	            x /= len;
	            y /= len;
	            z /= len;
	        }
	    }
	 
	public Vector multiply(double d) {
		x *= d;
		y *= d;
		z *= d;
		
		return this;
	}

	public Vector reverse() {
		x = (-getX());
		y = (-getY());
		z = (-getZ());
		
		return this;
	}
	public Vector getLeftPerpendicular() {
	    double newX = -getZ();  // Swap X and Z, negate Z
	    double newZ = getX();
	    
	    return new Vector(newX, 0, newZ);  // Y remains 0 since we're in 2D
	}

	public Vector getRightPerpendicular() {
	    double newX = getZ();  // Swap X and Z, negate X
	    double newZ = -getX();
	    
	    return new Vector(newX, 0, newZ);  // Y remains 0
	}

	public double getY() {
		return y;
	}
	
	public double getZ() {
		return z;
	}
}
