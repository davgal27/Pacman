package ijae.xgalead00.gui;

import ijae.xgalead00.Game;
import ijae.xgalead00.entity.Player;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;

public class MenuView extends HBox {

    private final Label scoreLabel;
    private final Label messageLabel;
    private final Game game;

    public MenuView(Game game) {
        this.game = game;

        // Score label
        scoreLabel = new Label("Score: 0");

        // Dynamic message label
        messageLabel = new Label("Get the KEY!");

        // Game speed slider
        Slider speedSlider = new Slider(50, 500, 200);
        speedSlider.setShowTickLabels(true);
        speedSlider.setFocusTraversable(false);
        speedSlider.valueProperty().addListener((obs, oldVal, newVal) ->
            game.setGameSpeed(newVal.intValue())
        );

        // Make sure the game view regains focus after interacting
        speedSlider.setOnMouseReleased(e -> game.getGameView().requestFocus());

        // Levels dropdown
        ComboBox<String> levelDropdown = new ComboBox<>();
        levelDropdown.getItems().addAll(
            "Level 1",
            "Level 2",
            "Level 3",
            "Level 4"
        );
        levelDropdown.setValue("Level 1"); // default selection
        levelDropdown.setFocusTraversable(false);
        levelDropdown.setOnAction(e -> {
            String selected = levelDropdown.getValue();
            switch (selected) {
                case "Level 1" -> game.loadLevel("levels/level1.txt");
                case "Level 2" -> game.loadLevel("levels/level2.txt");
                case "Level 3" -> game.loadLevel("levels/level3.txt");
                case "Level 4" -> game.loadLevel("levels/level4.txt");
            }
            // return focus to the game view after level selection
            game.getGameView().requestFocus();
        });

        // Add all controls to HBox
        getChildren().addAll(levelDropdown, speedSlider, scoreLabel, messageLabel);
        setSpacing(10);
    }

    // Call this every game tick to update labels
    public void update() {
        Player player = game.getBoard().getPlayer();
        if (player != null) {
            // Update score
            scoreLabel.setText("Score: " + player.getScore());

            // Update message
            if (player.hasKey()) {
                messageLabel.setText("Go to the GATE!");
                messageLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
            } else {
                messageLabel.setText("Get the KEY!");
                messageLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            }
        }
    }
}
