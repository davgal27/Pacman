package ijae.xgalead00;

import javafx.scene.image.Image;

/**
 * Assets handles loading and storing all game images.
 * This includes tile images, Pacman frames, ghost frames, and enraged ghost frames.
 * <p>
 * All fields are static so they can be accessed globally.
 */
public final class Assets {

    /** Tile assets */
    public static Image WALL;
    public static Image EMPTY;
    public static Image POINT;
    public static Image KEY;
    public static Image GATE;

    /** Entity assets */
    public static Image[] PACMAN_FRAMES;
    public static Image[] GHOST_FRAMES;
    public static Image[] GHOST_ENRAGED_FRAMES;

    /**
     * Private constructor to prevent instantiation.
     */
    private Assets() {
        // Prevent instantiation
    }

    /**
     * Loads all game assets from resources.
     * Call this once before starting the game to initialize all images.
     */
    public static void load() {

        // Tile images
        WALL  = loadImage("assets/wall.png");
        EMPTY = loadImage("assets/empty.png");
        POINT = loadImage("assets/point.png");
        KEY   = loadImage("assets/key.png");
        GATE  = loadImage("assets/gate.png");
    
        // Pacman animation frames
        PACMAN_FRAMES = new Image[] {
            loadImage("assets/pacman1.png"),
            loadImage("assets/pacman2.png"),
            loadImage("assets/pacman3.png"),
            loadImage("assets/pacman4.png")
        };

        // Ghost animation frames
        GHOST_FRAMES = new Image[] {
            loadImage("assets/ghost1.png"),
            loadImage("assets/ghost2.png"),
            loadImage("assets/ghost3.png"),
            loadImage("assets/ghost4.png")
        };

        // Enraged ghost animation frames (used when player gets keys)
        GHOST_ENRAGED_FRAMES = new Image[] {
            loadImage("assets/ghost_enraged1.png"),
            loadImage("assets/ghost_enraged2.png"),
            loadImage("assets/ghost_enraged3.png"),
            loadImage("assets/ghost_enraged4.png")
        };
    }

    /**
     * Loads a single image from the resources folder.
     *
     * @param path the relative path to the image
     * @return the loaded Image
     */
    private static Image loadImage(String path) {
        return new Image(
            Assets.class.getResource(path).toExternalForm()
        );
    }
}
