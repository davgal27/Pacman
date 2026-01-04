package ijae.xgalead00;

import ijae.xgalead00.entity.Player;
import ijae.xgalead00.entity.Ghost;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the game board.
 * <p>
 * Handles loading levels from external text files, storing the tile layout,
 * and managing entities (player and ghosts) on the board.
 */
public class Board {
    private Tiles[][] tiles;
    private int rows;
    private int cols;

    private final int TileWidth = 32;   // width of a tile in pixels
    private final int TileHeight = 32;  // height of a tile in pixels

    private Player player;
    private final List<Ghost> ghosts = new ArrayList<>();

    /**
     * Loads a level from a given file path.
     * <p>
     * Reads the board dimensions and tiles, and initializes the player and ghosts.
     * The player's key collection triggers ghosts to become enraged.
     *
     * @param path path to the external level file (e.g., "custom_levels/custom1.txt")
     * @throws IOException if the file cannot be read or does not exist
     */
    public void loadLevel(String path) throws IOException {
        BufferedReader br = null;

        try {
            File file = new File(path);
            if (!file.exists()) throw new IOException("Level file not found: " + path);
            br = new BufferedReader(new FileReader(file));

            // --- Read board dimensions ---
            String[] dims = br.readLine().split("\\s+");
            rows = Integer.parseInt(dims[0]);
            cols = Integer.parseInt(dims[1]);

            tiles = new Tiles[rows][cols];
            ghosts.clear();
            player = null;

            // --- Read each tile and create entities ---
            for (int y = 0; y < rows; y++) {
                String line = br.readLine();
                for (int x = 0; x < cols; x++) {
                    char ch = line.charAt(x);
                    switch (ch) {
                        case 'W' -> tiles[y][x] = Tiles.WALL;
                        case 'G' -> tiles[y][x] = Tiles.GATE;
                        case '.' -> tiles[y][x] = Tiles.EMPTY;
                        case 'o' -> tiles[y][x] = Tiles.POINT;
                        case 'K' -> tiles[y][x] = Tiles.KEY;
                        case 'P' -> {
                            tiles[y][x] = Tiles.EMPTY;
                            // Initialize player and set key collection callback
                            player = new Player(x, y, Assets.PACMAN_FRAMES);
                            player.setOnKeyCollected(() -> {
                                for (Ghost ghost : ghosts) ghost.enraged();
                            });
                        }
                        case 'C' -> {
                            tiles[y][x] = Tiles.EMPTY;
                            // Initialize a ghost at this position
                            ghosts.add(new Ghost(x, y, Assets.GHOST_FRAMES, Assets.GHOST_ENRAGED_FRAMES));
                        }
                        default -> tiles[y][x] = Tiles.EMPTY; // fallback to empty
                    }
                }
            }
        } finally {
            if (br != null) br.close(); // ensure file is closed
        }
    }

    /**
     * Checks if the tile at the specified coordinates is accessible for movement.
     *
     * @param x tile column
     * @param y tile row
     * @return true if accessible, false otherwise
     */
    public boolean IsAccessible(int x, int y) {
        if (y < 0 || y >= rows || x < 0 || x >= cols) return false;
        return tiles[y][x].IsAccessible();
    }

    /**
     * Returns the tile at the specified coordinates.
     * If out of bounds, returns a WALL tile.
     *
     * @param x tile column
     * @param y tile row
     * @return the tile at (x, y)
     */
    public Tiles getTile(int x, int y) {
        if (y < 0 || y >= rows || x < 0 || x >= cols) return Tiles.WALL;
        return tiles[y][x];
    }

    /** @return the player entity on the board */
    public Player getPlayer() { return player; }

    /** @return list of ghosts on the board */
    public List<Ghost> getGhosts() { return ghosts; }

    /** @return number of rows in the board */
    public int getRows() { return rows; }

    /** @return number of columns in the board */
    public int getCols() { return cols; }

    /** @return the 2D array of tiles */
    public Tiles[][] getTiles() { return tiles; }

    /** @return width of each tile in pixels */
    public int getTileWidth() { return TileWidth; }

    /** @return height of each tile in pixels */
    public int getTileHeight() { return TileHeight; }
}
