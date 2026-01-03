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
import javafx.stage.Stage;

public class Game {
    private final Stage stage;
    private final Board board;

    private GameView gameView;
    private MenuView menuView;

    private Timeline gameLoop;
    private int gameSpeed = 200;

    public Game(Stage stage) {
        this.stage = stage;
        this.board = new Board();
    }

    public void start() {
        BorderPane root = new BorderPane();

        gameView = new GameView(board, this);
        menuView = new MenuView(this);

        root.setCenter(gameView);
        root.setTop(menuView);

        Scene scene = new Scene(root);
        gameView.registerInput(scene);

        stage.setTitle("Pacman");
        stage.setScene(scene);
        stage.show();

        loadLevel("levels/level1.txt");
        startGameLoop();
    }

    public void loadLevel(String path) {
        try {
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

        int prevPlayerX = player.getX();
        int prevPlayerY = player.getY();


        player.move(board.getTiles());
        player.UpdateAnimation();
        player.TileEvents(board.getTiles());
        menuView.update(); // update score and key/gate message in menu view 

        // Ghost collision
        for (Ghost ghost : board.getGhosts()) {
            int prevGhostX = ghost.getX();
            int prevGhostY = ghost.getY();
            ghost.update(board.getTiles());
            ghost.UpdateAnimation();

            if (ghost.CollidesWith(player, prevPlayerX, prevPlayerY, prevGhostX, prevGhostY) && player.isAlive()) {
                gameView.redraw();
                player.die();
                gameLoop.stop();

                // Show Game Over using JavaFX
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Game Over");
                    alert.setHeaderText(null);
                    alert.setContentText("Caught by a ghost!");
                    alert.showAndWait();
                });
                return;
            }
        }

        // Win condition
        if (board.getTile(player.getX(), player.getY()) == Tiles.GATE && player.hasKey()) {
            gameLoop.stop();

            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("You Win!");
                alert.setHeaderText(null);
                alert.setContentText("You found the key and opened the gate!");
                alert.showAndWait();
            });
        }

        gameView.redraw();
    }

    public void setGameSpeed(int speed) {
        this.gameSpeed = speed;
        if (gameLoop != null) {
            gameLoop.stop();
            startGameLoop();
        }
    }

    public Board getBoard() { return board; }
    public GameView getGameView() { return gameView; }


}
