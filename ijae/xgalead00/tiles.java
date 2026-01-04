package ijae.xgalead00;

import javafx.scene.image.Image;

/**
 * Enum representing different types of tiles on the game board.
 * Each tile can provide its corresponding image and accessibility.
 */
public enum Tiles {
    WALL,
    GATE,
    EMPTY,
    POINT,
    KEY;

    /**
     * Returns the image associated with this tile type.
     *
     * @return an Image object representing the tile
     */
    public Image GetImage() {
        return switch(this) {
            case WALL -> Assets.WALL;
            case EMPTY -> Assets.EMPTY;
            case POINT -> Assets.POINT;
            case KEY -> Assets.KEY;
            case GATE -> Assets.GATE;
        };
    }

    /**
     * Indicates whether this tile can be traversed by the player or ghosts.
     *
     * @return true if the tile is accessible, false if it is a wall (only walls are non accesible)
     */
    public boolean IsAccessible() {
        return this != WALL;
    }
}
