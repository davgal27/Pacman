package ijae.xgalead00;

/**
 * Represents the four cardinal directions on the game board.
 * <p>
 * Provides utility methods to get the change in coordinates (dx, dy)
 * when moving in the given direction.
 */
public enum Direction { 
    UP, DOWN, LEFT, RIGHT;

    /**
     * Returns the horizontal component of movement for this direction.
     *
     * @return -1 for LEFT, 1 for RIGHT, 0 for UP or DOWN
     */
    public int dx() {
        switch(this) {
            case LEFT: return -1;
            case RIGHT: return 1;
            default: return 0; // no horizontal movement
        }
    }

    /**
     * Returns the vertical component of movement for this direction.
     *
     * @return -1 for UP, 1 for DOWN, 0 for LEFT or RIGHT
     */
    public int dy() {
        switch(this) {
            case UP: return -1;
            case DOWN: return 1;
            default: return 0; // no vertical movement
        }
    }
}
