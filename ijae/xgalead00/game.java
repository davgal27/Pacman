package ijae.xgalead00;

import ijae.xgalead00.gui.GameView;
import ijae.xgalead00.gui.MenuView;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Game {
    private final Stage stage;
    private final Board board;

    private GameView gameView;
    private MenuView menuView;

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
    }

    public void loadLevel(String path) {
        try {
            board.loadLevel(path);
            gameView.startGameLoop(gameSpeed);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setGameSpeed(int speed) {
        this.gameSpeed = speed;
        gameView.startGameLoop(gameSpeed);
    }

    public Board getBoard() { return board; }
}
