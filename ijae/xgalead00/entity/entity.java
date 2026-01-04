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

/**
 * Abstract class representing any entity on the board, such as a player or ghost.
 * Handles position, movement, animation cycling, and optional sprite rotation.
 */
public abstract class Entity {
    /** X-coordinate on the board */
    protected int x;

    /** Y-coordinate on the board */
    protected int y;

    /** Current movement direction; default is RIGHT */
    protected Direction direction = Direction.RIGHT;

    /** Base images facing right */
    protected Image[] BaseImages;

    /** Images rotated to match each direction */
    protected Image[] RightImages, LeftImages, UpImages, DownImages;

    /** Current animation frame index */
    protected int ImageIndex = 0;

    /** Number of steps before switching to the next animation frame */
    protected int AnimationSpeed = 1;

    /** Counter for tracking steps toward the next frame */
    protected int StepCounter = 0;

    /** Whether the sprite should rotate based on direction */
    protected boolean RotateSprite = true;

    /**
     * Creates an entity at the given coordinates with base images.
     * Generates rotated images for all directions.
     *
     * @param initx initial x-coordinate
     * @param inity initial y-coordinate
     * @param BaseImages base images facing right
     */
    public Entity(int initx, int inity, Image[] BaseImages) {
        this.x = initx;
        this.y = inity;
        this.BaseImages = BaseImages;

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

    /**
     * Rotates an image by the specified angle.
     *
     * @param image image to rotate
     * @param angle rotation angle in degrees
     * @return rotated image
     */
    protected Image RotateImage(Image image, double angle) {
        ImageView imageView = new ImageView(image);

        boolean swap = Math.abs(angle) == 90; // swap width and height for 90° rotations

        int w = (int) image.getWidth();
        int h = (int) image.getHeight();

        WritableImage rotated = swap ? new WritableImage(h, w) : new WritableImage(w, h);

        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT); // preserve transparency
        params.setTransform(new Rotate(angle, w / 2.0, h / 2.0));

        imageView.snapshot(params, rotated);
        return rotated;
    }

    /**
     * Advances the animation frame according to AnimationSpeed.
     */
    public void UpdateAnimation() {
        StepCounter++;
        if (StepCounter >= AnimationSpeed) {
            ImageIndex = (ImageIndex + 1) % BaseImages.length; // loop animation
            StepCounter = 0;
        }
    }

    /**
     * Draws the entity on the board at its current position.
     *
     * @param gc graphics context
     * @param TileWidth width of a board tile
     * @param TileHeight height of a board tile
     */
    public void draw(GraphicsContext gc, int TileWidth, int TileHeight) {
        gc.drawImage(
            getCurrentImage(),
            x * TileWidth,
            y * TileHeight,
            TileWidth,
            TileHeight
        );
    }

    /**
     * Returns the current image based on the direction and rotation setting.
     *
     * @return current image to draw
     */
    protected Image getCurrentImage() {
        if (!RotateSprite) {
            return RightImages[ImageIndex]; // ignore rotation if disabled
        }

        switch (direction) {
            case UP: return UpImages[ImageIndex];
            case DOWN: return DownImages[ImageIndex];
            case LEFT: return LeftImages[ImageIndex];
            case RIGHT: return RightImages[ImageIndex];
            default: return RightImages[ImageIndex];
        }
    }

    /**
     * Moves the entity one tile in its current direction without checking collisions.
     */
    public void move() {
        x += direction.dx();
        y += direction.dy();
    }

    /**
     * Moves the entity one tile in its current direction,
     * only if the target tile is accessible.
     *
     * @param tiles 2D array of board tiles
     */
    public void move(Tiles[][] tiles) {
        int nx = x + direction.dx();
        int ny = y + direction.dy();

        if (ny >= 0 && ny < tiles.length && nx >= 0 && nx < tiles[0].length) {
            if (tiles[ny][nx].IsAccessible()) { // move only if tile is free
                x = nx;
                y = ny;
            }
        }
    }

    /** Sets the entity's movement direction. */
    public void setDirection(Direction dir) {this.direction = dir;}

    /** Returns the entity's current direction. */
    public Direction getDirection() {return direction;}

    /** Returns the entity's X-coordinate. */
    public int getX() {return x;}

    /** Returns the entity's Y-coordinate. */
    public int getY() {return y;}
}
