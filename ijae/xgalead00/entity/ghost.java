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
		
		// Pick a random initial direction
	    Direction[] dirs = Direction.values();
	    direction = dirs[new Random().nextInt(dirs.length)];
	}

	// chooses a random direction and moves if possible
	public void update(Tiles[][] tiles) {

	    // Try to move forward
	    int nx = x + direction.dx();
	    int ny = y + direction.dy();

	    boolean canMove =
	        ny >= 0 && ny < tiles.length &&
	        nx >= 0 && nx < tiles[0].length &&
	        tiles[ny][nx].IsAccessible();

	    // If blocked, choose a new valid direction
	    if (!canMove) {
	        ChooseRandomDirection(tiles);
	        return;
	    }

	    // Occasionally change direction anyway
	    if (random.nextInt(10) == 0) {
	        ChooseRandomDirection(tiles);
	    }

	    // Move
	    x = nx;
	    y = ny;
	}
	private void ChooseRandomDirection(Tiles[][] tiles) {
	    Direction[] dirs = Direction.values();

	    for (int i = 0; i < dirs.length; i++) {
	        Direction d = dirs[random.nextInt(dirs.length)];
	        int nx = x + d.dx();
	        int ny = y + d.dy();

	        if (ny >= 0 && ny < tiles.length &&
	            nx >= 0 && nx < tiles[0].length &&
	            tiles[ny][nx].IsAccessible()) {

	            direction = d;
	            return;
	        }
	    }
	}

	// Collision with player
    public boolean CollidesWith(Player player) {
        return player != null && player.x == x && player.y == y;
    }


}