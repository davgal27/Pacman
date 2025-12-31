package ijae.xgalead00;
// enum direction representing 4 possible directions
public enum Direction { 
	UP, DOWN, LEFT, RIGHT;

	//change in x coordinate triggered by left and right
	public int dx() {
		switch(this) {
			case LEFT: return -1;
			case RIGHT: return 1;
			default: return 0;
		}
	}
	// change in y coordinate triggered by up and down 
	public int dy() {
		switch(this) {
			case UP: return -1;
			case DOWN: return 1;
			default: return 0;
		}
	}
}