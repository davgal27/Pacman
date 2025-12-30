package ijae.xgalead00.entity;

import ijae.xgalead00.direction;
import ijae.xgalead00.tiles;
import javafx.scene.image.Image;
import java.util.Random;
import ijae.xgalead00.assets;

public class Ghost extends Entity {
	private final Random random = new Random();

	public Ghost(int initx, int inity, Image[] BaseImages, int TileWidth, int TileHeight) {
		super (initx, inity, assets.GHOST_FRAMES, TileWidth, TileHeight);
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