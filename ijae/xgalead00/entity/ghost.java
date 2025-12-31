package ijae.xgalead00.entity;

import ijae.xgalead00.Direction;
import ijae.xgalead00.Board;
import ijae.xgalead00.Tiles;
import javafx.scene.image.Image;
import java.util.Random;
import ijae.xgalead00.Assets;

public class Ghost extends Entity {
	private final Random random = new Random();

	public Ghost(int initx, int inity, Image[] BaseImages) {
		super (initx, inity, Assets.GHOST_FRAMES);
		RotateSprite = false;
	}

	// chooses a random direction and moves if possible
	// chooses a random direction and moves if possible
	public void update(Tiles[][] tiles) {
	    // occasionally change direction 
	    if (random.nextInt(10) == 0) {
	        this.direction = Direction.values()[random.nextInt(4)];
	    }

	    // Compute next position
	    int nx = x + direction.dx();
	    int ny = y + direction.dy();

	    // Check bounds
	    if (ny < 0 || ny >= tiles.length || nx < 0 || nx >= tiles[0].length) {
	        return;
	    }

	    // Only move if tile is accessible
	    if (tiles[ny][nx].IsAccessible()) {
	        x = nx;
	        y = ny; // update directly instead of calling move()
	    }
	}
}