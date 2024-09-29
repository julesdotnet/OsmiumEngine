package jules.osmium.object;

import java.util.ArrayList;

public class ObjectHandler {
	static ArrayList<Cuboid> cuboids;
	
	public ObjectHandler() {
		cuboids = new ArrayList<>();
	}
	
	public static void spawnCuboid(Cuboid newCuboid) {
		if(cuboids == null) {
			cuboids = new ArrayList<>();
		}
		cuboids.add(newCuboid);
	}
	
	public static ArrayList<Cuboid> getCuboids(){
		return cuboids;
	}
}
