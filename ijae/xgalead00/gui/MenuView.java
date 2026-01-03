package ijae.xgalead00.gui;

import ijae.xgalead00.Game;
import ijae.xgalead00.entity.Player;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class MenuView extends HBox {

    private final Label scoreLabel;
    private final Label messageLabel;
    private final Game game;

    // Overlays
    private StackPane startOverlay;
    private final StackPane endOverlay;
    private final Label endLabel; // dynamic text for Game Over / You Win

    public MenuView(Game game) {
        this.game = game;

        setSpacing(10);

        // -------- Score and Message --------
        scoreLabel = new Label("Score: 0");
        scoreLabel.setStyle("-fx-font-family: 'Monospaced'; -fx-font-size: 34px; -fx-text-fill: #473B78; -fx-font-weight: bold;");
        messageLabel = new Label("Get the KEY!");
        messageLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");

        // -------- Speed Slider --------
        Slider speedSlider = new Slider(50, 500, 200);
        speedSlider.setShowTickLabels(true);
        speedSlider.setFocusTraversable(false);
        speedSlider.valueProperty().addListener((obs, oldVal, newVal) ->
            game.setGameSpeed(newVal.intValue())
        );
        speedSlider.setOnMouseReleased(e -> game.getGameView().requestFocus());

        // -------- Levels Dropdown --------
        ComboBox<String> levelDropdown = new ComboBox<>();
        levelDropdown.getItems().addAll(
            "Level 1",
            "Level 2",
            "Level 3",
            "Level 4"
        );
        levelDropdown.setValue("Level 1");
        levelDropdown.setFocusTraversable(false);
        levelDropdown.setOnAction(e -> {
            String selected = levelDropdown.getValue();
            switch (selected) {
                case "Level 1" -> game.loadLevel("levels/level1.txt");
                case "Level 2" -> game.loadLevel("levels/level2.txt");
                case "Level 3" -> game.loadLevel("levels/level3.txt");
                case "Level 4" -> game.loadLevel("levels/level4.txt");
            }

            // Reset game state for new level
            if (game.getGameLoop() != null) game.getGameLoop().stop();

            // REDRAW the new level before showing the overlay
            game.getGameView().redraw();

            startOverlay.setVisible(true); // now overlay appears on the **new level**
            update(); // reset HUD
            game.getGameView().requestFocus();
        });

        getChildren().addAll(levelDropdown, speedSlider, scoreLabel, messageLabel);

        // -------- Start Button Overlay --------
        startOverlay = new StackPane();
        startOverlay.setAlignment(Pos.CENTER);
        startOverlay.setPrefSize(800, 600);
        startOverlay.setVisible(true); // visible at startup
        startOverlay.setStyle("-fx-background-color: rgba(0,0,0,0.4)");

        VBox startBox = new VBox();
        startBox.setAlignment(Pos.CENTER);

        Image startImage = new Image(
            getClass().getResourceAsStream("/ijae/xgalead00/assets/start_button.png")
        );
        ImageView startButton = new ImageView(startImage);
        startButton.setFitWidth(300);
        startButton.setFitHeight(150);
        startButton.setPreserveRatio(true);
        startButton.setFocusTraversable(false);

        startButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            game.startGame();                 
            game.getGameView().requestFocus();
            startOverlay.setVisible(false);
        });
        startButton.setOnMouseEntered(e -> startButton.setOpacity(0.8));
        startButton.setOnMouseExited(e -> startButton.setOpacity(1.0));

        startBox.getChildren().add(startButton);
        startOverlay.getChildren().add(startBox);

        // -------- End Overlay (Game Over / Win) --------
        endOverlay = new StackPane();
        endOverlay.setAlignment(Pos.CENTER);
        endOverlay.setPrefSize(800, 600);
        endOverlay.setVisible(false);
        endOverlay.setStyle("-fx-background-color: rgba(0,0,0,0.6)");

        VBox endBox = new VBox(20);
        endBox.setAlignment(Pos.CENTER);

        endLabel = new Label("GAME OVER");
        endLabel.setStyle("-fx-font-size: 36; -fx-text-fill: white; -fx-font-weight: bold;");

        Label restart = new Label("Restart");
        restart.setStyle("-fx-font-size: 24; -fx-text-fill: white;");
        restart.setOnMouseClicked(e -> {
            game.startGame();
            endOverlay.setVisible(false);
            startOverlay.setVisible(false); // hide start button overlay
            game.getGameView().requestFocus();
        });

        Label quit = new Label("Quit");
        quit.setStyle("-fx-font-size: 24; -fx-text-fill: white;");
        quit.setOnMouseClicked(e -> System.exit(0));

        endBox.getChildren().addAll(endLabel, restart, quit);
        endOverlay.getChildren().add(endBox);
    }

    // Returns Start overlay
    public StackPane getStartOverlay() {
        return startOverlay;
    }

    // Returns End overlay
    public StackPane getEndOverlay() {
        return endOverlay;
    }

    // Show Game Over / Win overlay dynamically
    public void showEndOverlay(String message) {
        endLabel.setText(message);
        endOverlay.setVisible(true);
    }

    // Update HUD labels every game tick
    public void update() {
        Player player = game.getBoard().getPlayer();
        if (player != null) {
            scoreLabel.setText("Score: " + player.getScore());

            if (player.hasKey()) {
                messageLabel.setText("Go to the Gate!");
                messageLabel.setStyle("-fx-font-family: 'Monospaced'; -fx-font-size: 34px; -fx-text-fill: green; -fx-font-weight: bold;");
            } else {
                messageLabel.setText("Get the Key!");
                messageLabel.setStyle("-fx-font-family: 'Monospaced'; -fx-font-size: 34px; -fx-text-fill: red; -fx-font-weight: bold;");;

            }
        }
    }
}
