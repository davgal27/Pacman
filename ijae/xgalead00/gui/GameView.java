package ijae.xgalead00.gui;

import ijae.xgalead00.Board;
import ijae.xgalead00.Game;
import ijae.xgalead00.Tiles;
import ijae.xgalead00.entity.Ghost;
import ijae.xgalead00.entity.Player;
import ijae.xgalead00.Direction;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class GameView extends Pane {

    private final Board board;
    private final Game game;
    private final Canvas canvas;

    public GameView(Board board, Game game) {
        this.board = board;
        this.game = game;
        this.canvas = new Canvas(1000, 800);
        this.setFocusTraversable(true);
        this.requestFocus();

        getChildren().add(canvas);
    }

    // Keyboard input
    public void registerInput(Scene scene) {
        scene.setOnKeyPressed(e -> {
            Player player = board.getPlayer();
            if (player == null) return;

            switch (e.getCode()) {
                case UP -> player.setDirection(Direction.UP);
                case DOWN -> player.setDirection(Direction.DOWN);
                case LEFT -> player.setDirection(Direction.LEFT);
                case RIGHT -> player.setDirection(Direction.RIGHT);
            }

            e.consume(); // prevents focus issues 
        });
    }

    // Update entities and redraw
    public void redraw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Draw tiles
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
