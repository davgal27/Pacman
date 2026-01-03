package ijae.xgalead00.entity;

import ijae.xgalead00.Direction;
import ijae.xgalead00.Board;
import ijae.xgalead00.Tiles;
import javafx.scene.image.Image;
import java.util.Random;
import ijae.xgalead00.Assets;

public class Ghost extends Entity {
	private final Random random = new Random();
	private boolean enraged = false;
	private Image[] normalFrames;
	private Image[] enragedFrames;


	public Ghost(int initx, int inity, Image[] BaseImages, Image[] enragedFrames) {
		super(initx, inity, BaseImages);
	    this.normalFrames = BaseImages;
	    this.enragedFrames = enragedFrames;
		RotateSprite = false;

		// Pick a random initial direction
	    Direction[] dirs = Direction.values();
	    direction = dirs[new Random().nextInt(dirs.length)];
	}

	// chooses a random direction and moves if possible
	public void update(Tiles[][] tiles) {
	    // Move twice if enraged
	    int moves = enraged ? 2 : 1;
	    for (int i = 0; i < moves; i++) {
	        int nx = x + direction.dx();
	        int ny = y + direction.dy();

	        boolean canMove =
	            ny >= 0 && ny < tiles.length &&
	            nx >= 0 && nx < tiles[0].length &&
	            tiles[ny][nx].IsAccessible();

	        if (!canMove) {
	            ChooseRandomDirection(tiles);
	            continue;
	        }

	        // Occasionally change direction anyway
	        if (new Random().nextInt(10) == 0) {
	            ChooseRandomDirection(tiles);
	        }

	        // Move
	        x = nx;
	        y = ny;
	    }
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
	public boolean CollidesWith(Player player, int prevPlayerX, int prevPlayerY, int prevGhostX, int prevGhostY) {
	    int playerX = (int) player.x;
	    int playerY = (int) player.y;
	    int ghostX = (int) this.x;
	    int ghostY = (int) this.y;

	    // 1) Current positions overlap
	    if (playerX == ghostX && playerY == ghostY) return true;

	    // 2) Crossed paths
	    if (playerX == prevGhostX && playerY == prevGhostY &&
	        prevPlayerX == ghostX && prevPlayerY == ghostY) return true;

	    // 3) If ghost moves more than 1 tile, check intermediate positions
	    int dx = ghostX - prevGhostX;
	    int dy = ghostY - prevGhostY;

	    // Check along the path
	    int steps = Math.max(Math.abs(dx), Math.abs(dy));
	    for (int i = 1; i < steps; i++) {
	        int intermediateX = prevGhostX + (dx * i) / steps;
	        int intermediateY = prevGhostY + (dy * i) / steps;
	        if (playerX == intermediateX && playerY == intermediateY) return true;
	    }

	    return false;
	}
		

	public void enraged() {
	    if (!enraged) {
	        enraged = true;
	        this.BaseImages = enragedFrames;

	        // Rebuild rotated images
	        RightImages = BaseImages;
	        DownImages = new Image[BaseImages.length];
	        LeftImages = new Image[BaseImages.length];
	        UpImages = new Image[BaseImages.length];

	        for (int i = 0; i < BaseImages.length; i++) {
	            DownImages[i] = RotateImage(BaseImages[i], 90);
	            LeftImages[i] = RotateImage(BaseImages[i], 180);
	            UpImages[i] = RotateImage(BaseImages[i], -90);
	        }
	    }
	}





}