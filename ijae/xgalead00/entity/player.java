package ijae.xgalead00.entity;

import javafx.scene.image.Image;
import ijae.xgalead00.Assets;
import ijae.xgalead00.Tiles;

/**
 * Represents the player entity (Pacman) on the board.
 * Handles score, key collection, and alive state.
 * Movement direction is controlled externally.
 */
public class Player extends Entity {

    /** Whether the player is alive */
    private boolean alive = true;

    /** Player's current score */
    private int score = 0;

    /** Whether the player has collected the key */
    private boolean hasKey = false;

    /** Callback to notify the game when a key is collected */
    private Runnable onKeyCollected;

    /**
     * Constructs a Player at the given coordinates with base images.
     *
     * @param initx initial x-coordinate
     * @param inity initial y-coordinate
     * @param BaseImages base images (not used, defaults to Assets.PACMAN_FRAMES)
     */
    public Player(int initx, int inity, Image[] BaseImages) {
        super(initx, inity, Assets.PACMAN_FRAMES);
    }

    /**
     * Sets a callback to run when the player collects a key.
     *
     * @param callback function to call on key collection
     */
    public void setOnKeyCollected(Runnable callback) {
        this.onKeyCollected = callback;
    }

    /**
     * Handles events on the tile the player is currently on.
     * Updates score, key collection, and tile state.
     *
     * @param tiles 2D array of board tiles
     */
    public void TileEvents(Tiles[][] tiles) {
        Tiles tile = tiles[y][x];

        if (tile == Tiles.POINT) {
            tiles[y][x] = Tiles.EMPTY; // remove the point
            score += 10;
        }

        if (tile == Tiles.KEY) {
            tiles[y][x] = Tiles.EMPTY; // remove the key
            hasKey = true;

            if (onKeyCollected != null) {
                onKeyCollected.run(); // notify the game
            }
        }
    }

    /** Marks the player as dead */
    public void die() {alive = false;}

    /** Returns the player's current score */
    public int getScore() {return score;}

    /** Returns whether the player is alive */
    public boolean isAlive() {return alive;}

    /** Returns whether the player has collected the key */
    public boolean hasKey() {return hasKey;}
}
