package ijae.xgalead00;

import javafx.application.Application;
import javafx.stage.Stage;

// Java FX entry point which creates and starts the game 
public class Main extends Application {

    @Override
    public void start(Stage stage) {
        Assets.load();
        Game game = new Game(stage);
        game.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
