package ijae.xgalead00;

import javafx.scene.image.Image;

// asset loader 
public final class Assets {

    // Tile assets 
    public static Image WALL;
    public static Image EMPTY;
    public static Image POINT;
    public static Image KEY;

    public static Image GATE;

    // Entity assets 
    public static Image[] PACMAN_FRAMES;
    public static Image[] GHOST_FRAMES;

    private Assets() {
        // Prevent instantiation
    }

    // Loads assets
    public static void load() {

        // Tiles
        WALL  = loadImage("/assets/wall.png");
        EMPTY = loadImage("/assets/empty.png");
        POINT = loadImage("/assets/point.png");
        KEY   = loadImage("/assets/key.png");

        GATE = loadImage("/assets/gate.png");
    
        // Pacman animation frames 
        PACMAN_FRAMES = new Image[] {
            loadImage("/assets/pacman1.png"),
            loadImage("/assets/pacman2.png"),
            loadImage("/assets/pacman3.png"),
            loadImage("/assets/pacman4.png")
        };

        // Ghost 
        GHOST_FRAMES = new Image[] {
            loadImage("/assets/ghost1.png"),
            loadImage("/assets/ghost2.png"),
            loadImage("/assets/ghost3.png"),
            loadImage("/assets/ghost4.png")
        };
    }

    private static Image loadImage(String path) {
        return new Image(
            Assets.class.getResource(path).toExternalForm()
        );
    }
}
