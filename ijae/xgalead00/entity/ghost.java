package ijae.xgalead00.entity;

import ijae.xgalead00.Direction;
import ijae.xgalead00.TileType;
import javafx.scene.image.Image;
import java.util.Random;

public class Ghost extends Entity {
	private final Random random = new Random();

	public Ghost(int initx, int inity, Image[] BaseImages, int TileWidth, int TileHeight) {
		super (initx, inity, BaseImages, TileWidth, TileHeight);
		RotateSprite = false;
	}

	// chooses a random direction and moves if possible
	public void update(TileType[][] board) {
		// occasionally change direction 
		if (random.nextInt(10) == 0) {
			Direction = Direction.values()[random.nextInt(4)];
		}

		// move only if the next tile is accessible 
		int nx = x + directiondx();
		int ny = y + directiondy();

		// bounds 
		if (ny < 0 || ny >= board.length ||
			nx < 0 || nx >= board[0].length) {
			return;
		}

		// Check if tile is accessible / not a wall 
		if (board[ny][nx].IsAccessible()){
			move();
		}
	}
}