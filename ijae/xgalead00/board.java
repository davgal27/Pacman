package ijae.xgalead00;

import ijae.xgalead00.entity.Player;
import ijae.xgalead00.entity.Ghost;
import ijae.xgalead00.entity.Entity;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.InputStreamReader;

public class Board {
    private Tiles[][] tiles;
    private int rows;
    private int cols;

    private final int TileWidth = 32;   // width of a tile in pixels
    private final int TileHeight = 32;  // height of a tile in pixels

    private Player player;
    private final List<Ghost> ghosts = new ArrayList<>();

    public void loadLevel(String path) throws IOException {
        try (BufferedReader br = new BufferedReader(
            new InputStreamReader(Board.class.getResourceAsStream(path))
            )) {

            String[] dims = br.readLine().split("\\s+");
            rows = Integer.parseInt(dims[0]);
            cols = Integer.parseInt(dims[1]);

            tiles = new Tiles[rows][cols];
            ghosts.clear();
            player = null; // reset player

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
                            player = new Player(x, y, Assets.PACMAN_FRAMES);
                        }

                        case 'C' -> {
                            tiles[y][x] = Tiles.EMPTY;
                            ghosts.add(new Ghost(x, y, Assets.GHOST_FRAMES));
                        }

                        default -> tiles[y][x] = Tiles.EMPTY;
                    }
                }
            }
        }
    }

    public boolean IsAccessible(int x, int y) {
        if (y < 0 || y >= rows || x < 0 || x >= cols) return false;
        return tiles[y][x].IsAccessible();
    }

    public Tiles getTile(int x, int y) {
        if (y < 0 || y >= rows || x < 0 || x >= cols) return Tiles.WALL;
        return tiles[y][x];
    }

    public Player getPlayer() { return player; }
    public List<Ghost> getGhosts() { return ghosts; }
    public int getRows() { return rows; }
    public int getCols() { return cols; }
    public Tiles[][] getTiles() { return tiles; }

    public int getTileWidth() { return TileWidth; }
    public int getTileHeight() { return TileHeight; }
}
