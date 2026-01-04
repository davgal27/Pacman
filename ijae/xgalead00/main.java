package ijae.xgalead00;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * JavaFX entry point for the Pacman game.
 * <p>
 * Loads assets and initializes the main Game instance.
 */
public class Main extends Application {

    /**
     * Called when the JavaFX application starts.
     * Initializes assets and starts the game.
     *
     * @param stage the primary stage for this application
     */
    @Override
    public void start(Stage stage) {
        Assets.load();            // Load all game assets
        Game game = new Game(stage);
        game.start();             // Start the game
    }

    /**
     * Main method launches the JavaFX application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
