package islandcommander;


public final class MapSquare {
	
	public float minerals = 0;
	public int owner = -1;
	public boolean seen = !Main.FOG_OF_WAR;
	public boolean blocked = false;
	
	public MapSquare() {
	}

}
