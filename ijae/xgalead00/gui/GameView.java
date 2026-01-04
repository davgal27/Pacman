package ijae.xgalead00.gui;

import ijae.xgalead00.Board;
import ijae.xgalead00.Game;
import ijae.xgalead00.Tiles;
import ijae.xgalead00.entity.Ghost;
import ijae.xgalead00.entity.Player;
import ijae.xgalead00.Direction;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

/**
 * Represents the graphical view of the game.
 * Handles drawing the board, player, ghosts, and tile graphics.
 * Also manages keyboard input for the player.
 */
public class GameView extends Pane {

    private final Board board;
    private final Game game;
    private final Canvas canvas;

    /**
     * Constructs the GameView with a given board and game logic.
     *
     * @param board the game board
     * @param game the main game instance
     */
    public GameView(Board board, Game game) {
        this.board = board;
        this.game = game;
        this.canvas = new Canvas(800, 800);

        // Enable focus to receive keyboard input
        this.setFocusTraversable(true);
        this.requestFocus();

        getChildren().add(canvas);
    }

    /**
     * Registers keyboard input to control the player.
     *
     * @param scene the scene to listen for key presses
     */
    public void registerInput(Scene scene) {
        scene.setOnKeyPressed(e -> {
            Player player = board.getPlayer();
            if (player == null) return;

            // Update player direction based on arrow keys
            switch (e.getCode()) {
                case UP -> player.setDirection(Direction.UP);
                case DOWN -> player.setDirection(Direction.DOWN);
                case LEFT -> player.setDirection(Direction.LEFT);
                case RIGHT -> player.setDirection(Direction.RIGHT);
            }

            e.consume(); // prevent other handlers from receiving the event
        });
    }

    /**
     * Redraws the entire game view: tiles, player, and ghosts.
     */
    public void redraw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Draw all tiles
        for (int y = 0; y < board.getRows(); y++) {
            for (int x = 0; x < board.getCols(); x++) {
                Tiles tile = board.getTile(x, y);
                Image img = tile.GetImage();
                gc.drawImage(
                    img,
                    x * board.getTileWidth(),
                    y * board.getTileHeight(),
                    board.getTileWidth(),
                    board.getTileHeight()
                );
            }
        }

        // Draw player
        Player player = board.getPlayer();
        if (player != null) player.draw(gc, board.getTileWidth(), board.getTileHeight());

        // Draw ghosts
        for (Ghost ghost : board.getGhosts())
            ghost.draw(gc, board.getTileWidth(), board.getTileHeight());
    }
}
