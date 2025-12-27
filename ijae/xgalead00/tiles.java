package ijae.xgalead00;

public enum TileType {
	WALL("wall.png"),
	GATE("gate.png"),
	EMPTY("empty.png"),
	POINT("point.png"),
	KEY("key.png");

	private final String Image;

	TileType(String Image){
		this.Image = Image;
	}

	public String GetImage() { 
		return Image;
	}

	public boolean IsAccessible() {
		return this != WALL;
	}
}

