package ijae.xgalead00;

import ijae.xgalead00.entity.entity;
import ijae.xgalead00.entity.player;
import ijae.xgalead00.entity.ghost;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Class board: contains the tile matrix, and can load levels from text files 
/*
File format:
rows cols
followed by rows of tile characters:
W = Wall
G = Gate
. = Empty
P = Player
C = Ghost
K = Key
o = Point
*/

public class Board{
	private TileType[][] tiles;
	private int rows;
	private int cols;

	private Player player;
	private final List<Ghost> ghosts - new ArrayList<>();

	public void loadLevel(String path, ImageLoader loader, int tileSize) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String[] dims = br.readLine().split("\\s+");
            rows = Integer.parseInt(dims[0]);
            cols = Integer.parseInt(dims[1]);

            tiles = new TileType[rows][cols];

            ghosts.clear(); // Remove previous ghosts

            for (int y = 0; y < rows; y++) {
                String line = br.readLine();
                for (int x = 0; x < cols; x++) {
                    char ch = line.charAt(x);
                    switch (ch) {
                        case 'W': tiles[y][x] = TileType.WALL; break;
                        case 'G': tiles[y][x] = TileType.GATE; break;
                        case '.': tiles[y][x] = TileType.EMPTY; break;
                        case 'o': tiles[y][x] = TileType.POINT; break;
                        case 'K': tiles[y][x] = TileType.KEY; break;

                        case 'P': 
                            tiles[y][x] = TileType.EMPTY;
                            player = new Player(
                                x, y,
                                loader.getPlayerFrames(), // helper to get Image[]
                                tileSize, tileSize
                            );
                            break;

                        case 'C':
                            tiles[y][x] = TileType.EMPTY;
                            ghosts.add(new Ghost(
                                x, y,
                                loader.getGhostFrames(),
                                tileSize, tileSize
                            ));
                            break;

                        default:
                            tiles[y][x] = TileType.EMPTY;
                    }
                }
            }
        }
    }

    // Checks tile accessibility
    public boolean isAccessible(int x, int y) {
        if (y < 0 || y >= rows || x < 0 || x >= cols) return false;
        return tiles[y][x].IsAccessible();
    }

    //Returns the player
    public Player getPlayer() {
        return player;
    }

    //Returns all ghosts
    public List<Ghost> getGhosts() {
        return ghosts;
    }

    // Returns tile type at given coord
    public TileType getTile(int x, int y) {
        if (y < 0 || y >= rows || x < 0 || x >= cols) return TileType.WALL;
        return tiles[y][x];
    }

    // dimensions
    public int getRows() { return rows; }
    public int getCols() { return cols; }
}
