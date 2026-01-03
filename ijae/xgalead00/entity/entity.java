package ijae.xgalead00.entity;

import ijae.xgalead00.Direction;
import ijae.xgalead00.Tiles;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;
import javafx.scene.SnapshotParameters;
import javafx.scene.paint.Color;

// abstract class for any entity on the board (player or ghost) 
// handles: position, movement, animation cycling, rotation of images
public abstract class Entity {
    protected int x, y; // board coordinates 
    protected Direction direction = Direction.RIGHT; // starting direction

    // Base image: right facing
    protected Image[] BaseImages;
    // rotated images based on direction
    protected Image[] RightImages, LeftImages, UpImages, DownImages;

    // animation state
    protected int ImageIndex = 0; // current image in animation
    protected int AnimationSpeed = 1; // steps per image change
    protected int StepCounter = 0; // steps to switch frame

    // Should the sprite rotate?
    protected boolean RotateSprite = true;

    // Constructor
    public Entity(int initx, int inity, Image[] BaseImages) {
        this.x = initx;
        this.y = inity;
        this.BaseImages = BaseImages;

        // rotation of right-facing images
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

    // Rotates an image by angle
    protected Image RotateImage(Image image, double angle) {
	    ImageView imageView = new ImageView(image);

	    boolean swap = Math.abs(angle) == 90;

	    int w = (int) image.getWidth();
	    int h = (int) image.getHeight();

	    WritableImage rotated = swap
	            ? new WritableImage(h, w)
	            : new WritableImage(w, h);

	    SnapshotParameters params = new SnapshotParameters();
	    params.setFill(Color.TRANSPARENT); // for loading images without white backgrond
	    params.setTransform(
	        new Rotate(angle, w / 2.0, h / 2.0)
	    );

	    imageView.snapshot(params, rotated);
	    return rotated;
	}

    // Updates animation once per game step 
    public void UpdateAnimation() {
        StepCounter++;
        if (StepCounter >= AnimationSpeed) {
            ImageIndex = (ImageIndex + 1) % BaseImages.length; // loop
            StepCounter = 0;
        }
    }

    // Draw current image at entity position
    // TileWidth and TileHeight are passed from the Board
    public void draw(GraphicsContext gc, int TileWidth, int TileHeight) {
        gc.drawImage(
            getCurrentImage(),
            x * TileWidth,
            y * TileHeight,
            TileWidth,
            TileHeight
        );
    }

    // Return correct image based on direction
    protected Image getCurrentImage() {
        if (!RotateSprite) {
            return RightImages[ImageIndex];
        }

        switch (direction) {
            case UP: return UpImages[ImageIndex];
            case DOWN: return DownImages[ImageIndex];
            case LEFT: return LeftImages[ImageIndex];
            case RIGHT: return RightImages[ImageIndex];
            default: return RightImages[ImageIndex];
        }
    }

    // Move entity by one tile in current direction 
    public void move() {
	    x += direction.dx();
	    y += direction.dy();
	}

	// New move for Pacman to respect walls
	public void move(Tiles[][] tiles) {
	    int nx = x + direction.dx();
	    int ny = y + direction.dy();

	    if (ny >= 0 && ny < tiles.length && nx >= 0 && nx < tiles[0].length) {
	        if (tiles[ny][nx].IsAccessible()) {
	            x = nx;
	            y = ny;
	        }
	    }
	}

    // Getters & setters
    public void setDirection(Direction dir) { this.direction = dir; }
    public Direction getDirection() { return direction; }
    public int getX() { return x; }
    public int getY() { return y; }
}
