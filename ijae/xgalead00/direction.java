package ijae.xgalead00;

public enum Direction {
	UP, DOWN, LEFt, RIGHT

	public int dx() {
		switch(this) {
			case LEFT: return -1;
			case RIGHT: return 1;
			default: return 0;
		}
	}

	public int dy() {
		switch(this) {
			case UP: return -1;
			case DOWN: return 1;
			default: return 0;
		}
	}
}