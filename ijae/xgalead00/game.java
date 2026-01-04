package ijae.xgalead00;

import ijae.xgalead00.gui.GameView;
import ijae.xgalead00.gui.MenuView;
import ijae.xgalead00.entity.Player;
import ijae.xgalead00.entity.Ghost;
import ijae.xgalead00.Tiles;

import javafx.animation.Timeline;     
import javafx.animation.KeyFrame;    
import javafx.util.Duration;          
import javafx.scene.control.Alert;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Game {
    private final Stage stage;
    private final Board board;

    private GameView gameView;
    private MenuView menuView;

    private Timeline gameLoop;
    private int gameSpeed = 200;

    private String currentLevelPath = "levels/level1.txt";


    public Game(Stage stage) {
        this.stage = stage;
        this.board = new Board();
    }

    public void start() {
        gameView = new GameView(board, this);
        menuView = new MenuView(this);

        BorderPane gameLayout = new BorderPane();
        gameLayout.setCenter(gameView);
        gameLayout.setTop(menuView);

        StackPane root = new StackPane();
        root.getChildren().addAll(
            gameLayout,
            menuView.getStartOverlay(),   // centered overlay
            menuView.getEndOverlay(),
            menuView.getPauseOverlay()
        );

        Scene scene = new Scene(root);
        gameView.registerInput(scene);

        stage.setTitle("Pacman");
        stage.setScene(scene);
        stage.show();
        loadLevel("levels/level1.txt");  // prepare board
        gameView.redraw();               // draw paused game

    }

    public void loadLevel(String path) {
        try {
            currentLevelPath = path; // remember which level is loaded
            board.loadLevel(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void startGameLoop() {
        if (gameLoop != null) gameLoop.stop();

        gameLoop = new Timeline(
            new KeyFrame(Duration.millis(gameSpeed), e -> update())
        );
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        gameLoop.play();
    }

    
    private void update() {
        Player player = board.getPlayer();
        if (player == null) return;

        // Save previous positions
        int prevPlayerX = player.getX();
        int prevPlayerY = player.getY();

        // --- Move player ---
        player.move(board.getTiles());
        player.UpdateAnimation();
        player.TileEvents(board.getTiles());

        // Update menu/HUD after player moved
        menuView.update();

        // --- Move ghosts and check collisions ---
        for (Ghost ghost : board.getGhosts()) {
            int prevGhostX = ghost.getX();
            int prevGhostY = ghost.getY();

            ghost.update(board.getTiles());
            ghost.UpdateAnimation();

            // Check collision
            if (ghost.CollidesWith(player, prevPlayerX, prevPlayerY, prevGhostX, prevGhostY) && player.isAlive()) {
                player.die();
                gameLoop.stop();
                gameView.redraw();
                Platform.runLater(() -> menuView.showEndOverlay("GAME OVER"));
                return;
            }
        }

        // --- Check win condition ---
        if (board.getTile(player.getX(), player.getY()) == Tiles.GATE && player.hasKey()) {
            gameLoop.stop();
            Platform.runLater(() -> menuView.showEndOverlay("YOU WIN!"));
            return;
        }

        // --- Redraw everything ---
        gameView.redraw();
    }


    public void setGameSpeed(int speed) {
        this.gameSpeed = speed;

        if (gameLoop != null) {
            // Update timing WITHOUT restarting the loop
            gameLoop.getKeyFrames().setAll(
                new KeyFrame(Duration.millis(gameSpeed), e -> update())
            );
        }
    }


    public Board getBoard() { return board; }
    
    public GameView getGameView() { return gameView; }

    public void startGame() {
        loadLevel(currentLevelPath); // start the currently selected level
        startGameLoop();
    }

    public void resumeGame() {
        if (gameLoop != null) {
            gameLoop.play();
        } else {
            startGameLoop();
        }
    }
    public Timeline getGameLoop() {
        return gameLoop;
    }



}
