package ijae.xgalead00.gui;

import ijae.xgalead00.board;
import ijae.xgalead00.direction;
import ijae.xgalead00.game;
import ijae.xgalead00.entity.ghost;
import ijae.xgalead00.entity.player;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

// GUI of game itself 
public class GameView extends Pane {

    private final Board board;
    private final Game game;

    private final Canvas canvas;
    private Timeline gameLoop;

    private static final int TILE_SIZE = 32;

    public GameView(Board board, Game game) {
        this.board = board;
        this.game = game;

        canvas = new Canvas(800, 600);
        getChildren().add(canvas);
    }

    // Keyboard input logic
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
        });
    }

    // Game loop
    public void startGameLoop(int speedMs) {
        if (gameLoop != null) gameLoop.stop();

        gameLoop = new Timeline(
            new KeyFrame(Duration.millis(speedMs), e -> update())
        );
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        gameLoop.play();
    }

    // Game step
    private void update() {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Update entities
        board.getPlayer().move();
        board.getPlayer().updateAnimation();

        for (Ghost ghost : board.getGhosts()) {
            ghost.update(board.getTiles());
            ghost.updateAnimation();
        }

        // Draw everything
        draw(gc);
    }

    private void draw(GraphicsContext gc) {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // draw tiles here (later)
        board.getPlayer().draw(gc);
        board.getGhosts().forEach(g -> g.draw(gc));
    }
}
