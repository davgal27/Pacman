package ijae.xgalead00.entity;

import ijae.xgalead00.Direction;
import ijae.xgalead00.Tiles;
import javafx.scene.image.Image;
import java.util.Random;

/**
 * Represents a Ghost entity on the board.
 * Handles movement, enraged state, and collision detection with the player.
 */
public class Ghost extends Entity {
    private final Random random = new Random();

    /** Whether the ghost is in an enraged (vulnerable) state */
    private boolean enraged = false;

    /** Normal (non-enraged) animation frames */
    private Image[] normalFrames;

    /** Enraged animation frames */
    private Image[] enragedFrames;

    /**
     * Constructs a Ghost at the given position with normal and enraged images.
     * Picks a random initial direction.
     *
     * @param initx initial x-coordinate
     * @param inity initial y-coordinate
     * @param BaseImages normal frames (facing right)
     * @param enragedFrames frames for enraged state
     */
    public Ghost(int initx, int inity, Image[] BaseImages, Image[] enragedFrames) {
        super(initx, inity, BaseImages);
        this.normalFrames = BaseImages;
        this.enragedFrames = enragedFrames;
        RotateSprite = false;

        // Pick a random initial direction
        Direction[] dirs = Direction.values();
        direction = dirs[random.nextInt(dirs.length)];
    }

    /**
     * Updates the ghost's movement each game step.
     * Moves twice if enraged and may randomly change direction.
     *
     * @param tiles 2D array of board tiles
     */
    public void update(Tiles[][] tiles) {
        int moves = enraged ? 2 : 1;

        for (int i = 0; i < moves; i++) {
            int nx = x + direction.dx();
            int ny = y + direction.dy();

            boolean canMove = ny >= 0 && ny < tiles.length &&
                              nx >= 0 && nx < tiles[0].length &&
                              tiles[ny][nx].IsAccessible();

            if (!canMove) {
                ChooseRandomDirection(tiles);
                continue;
            }

            // Occasionally change direction randomly even if path is clear
            if (random.nextInt(10) == 0) {
                ChooseRandomDirection(tiles);
            }

            // Apply movement
            x = nx;
            y = ny;
        }
    }

    /**
     * Chooses a random accessible direction for the ghost.
     *
     * @param tiles 2D array of board tiles
     */
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

    /**
     * Checks if the ghost collides with the player.
     * Handles same-tile collisions, crossed paths, and multiple-step movement.
     *
     * @param player the player entity
     * @param prevPlayerX player's previous x-coordinate
     * @param prevPlayerY player's previous y-coordinate
     * @param prevGhostX ghost's previous x-coordinate
     * @param prevGhostY ghost's previous y-coordinate
     * @return true if a collision occurred
     */
    public boolean CollidesWith(Player player, int prevPlayerX, int prevPlayerY,
                                int prevGhostX, int prevGhostY) {
        int playerX = (int) player.x;
        int playerY = (int) player.y;
        int ghostX = (int) this.x;
        int ghostY = (int) this.y;

        // 1) Current positions overlap
        if (playerX == ghostX && playerY == ghostY) return true;

        // 2) Crossed paths
        if (playerX == prevGhostX && playerY == prevGhostY &&
            prevPlayerX == ghostX && prevPlayerY == ghostY) return true;

        // 3) If ghost moves multiple tiles, check intermediate positions
        int dx = ghostX - prevGhostX;
        int dy = ghostY - prevGhostY;
        int steps = Math.max(Math.abs(dx), Math.abs(dy));

        for (int i = 1; i < steps; i++) {
            int intermediateX = prevGhostX + (dx * i) / steps;
            int intermediateY = prevGhostY + (dy * i) / steps;
            if (playerX == intermediateX && playerY == intermediateY) return true;
        }

        return false;
    }

    /**
     * Puts the ghost into the enraged state.
     * Updates its animation frames accordingly.
     */
    public void enraged() {
        if (!enraged) {
            enraged = true;
            this.BaseImages = enragedFrames;

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
