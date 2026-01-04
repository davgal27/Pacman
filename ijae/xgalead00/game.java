package ijae.xgalead00;

import ijae.xgalead00.gui.GameView;
import ijae.xgalead00.gui.MenuView;
import ijae.xgalead00.entity.Player;
import ijae.xgalead00.entity.Ghost;
import ijae.xgalead00.Tiles;

import javafx.animation.Timeline;     
import javafx.animation.KeyFrame;    
import javafx.util.Duration;          
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Main game controller class.
 * <p>
 * Manages the game board, entities, game loop, and user interface (views and HUD).
 */
public class Game {
    private final Stage stage;
    private final Board board;

    private GameView gameView;
    private MenuView menuView;

    private Timeline gameLoop;
    private int gameSpeed = 200;  // milliseconds per frame

    private String currentLevelPath = "levels/level1.txt";

    /**
     * Constructs a new Game tied to a Stage.
     *
     * @param stage the primary JavaFX stage
     */
    public Game(Stage stage) {
        this.stage = stage;
        this.board = new Board();
    }

    /**
     * Initializes the game UI and prepares the first level.
     * Sets up the game and menu views, input, and initial board rendering.
     */
    public void start() {
        gameView = new GameView(board, this);
        menuView = new MenuView(this);

        BorderPane gameLayout = new BorderPane();
        gameLayout.setCenter(gameView);
        gameLayout.setTop(menuView);

        StackPane root = new StackPane();
        root.getChildren().addAll(
            gameLayout,
            menuView.getStartOverlay(),
            menuView.getEndOverlay(),
            menuView.getPauseOverlay()
        );

        Scene scene = new Scene(root);
        gameView.registerInput(scene);

        stage.setTitle("Pacman");
        stage.setScene(scene);
        stage.show();

        loadLevel("ijae/xgalead00/levels/level1.txt");
        gameView.redraw();
    }

    /**
     * Loads a level from a given file path.
     *
     * @param path path to the level file
     */
    public void loadLevel(String path) {
        try {
            currentLevelPath = path;
            board.loadLevel(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Starts the main game loop, updating entities at intervals defined by gameSpeed.
     */
    private void startGameLoop() {
        if (gameLoop != null) gameLoop.stop();

        gameLoop = new Timeline(
            new KeyFrame(Duration.millis(gameSpeed), e -> update())
        );
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        gameLoop.play();
    }

    /**
     * Updates the game state.
     * <p>
     * Handles player and ghost movements, animations, tile events, collision detection,
     * win/loss conditions, and redraws the game view.
     */
    private void update() {
        Player player = board.getPlayer();
        if (player == null) return;

        int prevPlayerX = player.getX();
        int prevPlayerY = player.getY();

        // Move player and handle tile events
        player.move(board.getTiles());
        player.UpdateAnimation();
        player.TileEvents(board.getTiles());

        menuView.update();

        // Move ghosts and check collisions
        for (Ghost ghost : board.getGhosts()) {
            int prevGhostX = ghost.getX();
            int prevGhostY = ghost.getY();

            ghost.update(board.getTiles());
            ghost.UpdateAnimation();

            if (ghost.CollidesWith(player, prevPlayerX, prevPlayerY, prevGhostX, prevGhostY) 
                && player.isAlive()) {
                player.die();
                gameLoop.stop();
                gameView.redraw();
                Platform.runLater(() -> menuView.showEndOverlay("GAME OVER"));
                return;
            }
        }

        // Check if player reached gate with key
        if (board.getTile(player.getX(), player.getY()) == Tiles.GATE && player.hasKey()) {
            gameLoop.stop();
            Platform.runLater(() -> menuView.showEndOverlay("YOU WIN!"));
            return;
        }

        gameView.redraw();
    }

    /**
     * Sets the speed of the game in milliseconds per update.
     * Updates the timeline's KeyFrame without restarting the loop.
     *
     * @param speed milliseconds per frame
     */
    public void setGameSpeed(int speed) {
        this.gameSpeed = speed;

        if (gameLoop != null) {
            gameLoop.getKeyFrames().setAll(
                new KeyFrame(Duration.millis(gameSpeed), e -> update())
            );
        }
    }

    /** @return the game board */
    public Board getBoard() { return board; }

    /** @return the GameView instance */
    public GameView getGameView() { return gameView; }

    /** Starts or restarts the current level */
    public void startGame() {
        loadLevel(currentLevelPath);
        startGameLoop();
    }

    /** Resumes the game loop if paused, or starts it if not running */
    public void resumeGame() {
        if (gameLoop != null) {
            gameLoop.play();
        } else {
            startGameLoop();
        }
    }

    /** @return the Timeline object controlling the game loop */
    public Timeline getGameLoop() {
        return gameLoop;
    }
}
