package ijae.xgalead00.gui;

import ijae.xgalead00.Game;
import ijae.xgalead00.entity.Player;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class MenuView extends HBox {

    private final Game game;

    // HUD
    private final Label scoreLabel;
    private final Label messageLabel;

    // Overlays
    private StackPane startOverlay;
    private StackPane pauseOverlay;
    private StackPane endOverlay;
    private Label endLabel;

    // Shared level dropdown (used in pause & end overlay)
    private final ComboBox<String> levelDropdown = new ComboBox<>();

    public MenuView(Game game) {
        this.game = game;

        setSpacing(10);
        setAlignment(Pos.CENTER_LEFT);

        // ================= HUD =================
        ImageView menuButton = createMenuButton();

        menuButton.setOnMouseClicked(e -> {
            if (game.getGameLoop() != null) game.getGameLoop().stop();
            pauseOverlay.setVisible(true);
        });

        scoreLabel = new Label("Score: 0");
        scoreLabel.setStyle(
            "-fx-font-family: 'Monospaced';" +
            "-fx-font-size: 34px;" +
            "-fx-text-fill: #473B78;" +
            "-fx-font-weight: bold;"
        );

        messageLabel = new Label("Get the Key!");
        messageLabel.setStyle(
            "-fx-font-family: 'Monospaced';" +
            "-fx-font-size: 34px;" +
            "-fx-text-fill: red;" +
            "-fx-font-weight: bold;"
        );

        getChildren().addAll(menuButton, scoreLabel, messageLabel);

        // ================= Initialize Level Dropdown =================
        levelDropdown.getItems().addAll("Level 1", "Level 2", "Level 3", "Level 4");
        levelDropdown.setValue("Level 1");
        levelDropdown.setPrefWidth(150);
        levelDropdown.setStyle("-fx-font-size: 18px;");
        levelDropdown.setOnAction(e -> {
            // Load selected level
            switch (levelDropdown.getValue()) {
                case "Level 1" -> game.loadLevel("levels/level1.txt");
                case "Level 2" -> game.loadLevel("levels/level2.txt");
                case "Level 3" -> game.loadLevel("levels/level3.txt");
                case "Level 4" -> game.loadLevel("levels/level4.txt");
            }

            if (game.getGameLoop() != null) game.getGameLoop().stop();
            game.getGameView().redraw();

            // Show start overlay for new level
            pauseOverlay.setVisible(false);
            endOverlay.setVisible(false);
            startOverlay.setVisible(true);

            update();
        });

        // ================= Overlays =================
        startOverlay = createStartOverlay();
        pauseOverlay = createPauseOverlay();
        endOverlay = createEndOverlay();
    }

    // =========================================================
    // Menu Button
    // =========================================================
    private ImageView createMenuButton() {
        Image image = new Image(
            getClass().getResourceAsStream("/ijae/xgalead00/assets/menu_button.png")
        );

        ImageView button = new ImageView(image);
        button.setFitWidth(200);
        button.setPreserveRatio(true);
        button.setFocusTraversable(false);

        button.setOnMouseClicked(e -> {
            if (game.getGameLoop() != null) game.getGameLoop().stop();
            pauseOverlay.setVisible(true);
        });

        button.setOnMouseEntered(e -> button.setOpacity(0.8));
        button.setOnMouseExited(e -> button.setOpacity(1.0));

        return button;
    }

    // =========================================================
    // Start Overlay
    // =========================================================
    private StackPane createStartOverlay() {
        StackPane overlay = baseOverlay(0.4);

        Image startImage = new Image(
            getClass().getResourceAsStream("/ijae/xgalead00/assets/start_button.png")
        );

        ImageView startButton = new ImageView(startImage);
        startButton.setFitWidth(300);
        startButton.setPreserveRatio(true);

        startButton.setOnMouseClicked(e -> {
            game.startGame();
            overlay.setVisible(false);
            game.getGameView().requestFocus();
        });

        startButton.setOnMouseEntered(e -> startButton.setOpacity(0.8));
        startButton.setOnMouseExited(e -> startButton.setOpacity(1.0));

        overlay.getChildren().add(startButton);
        overlay.setVisible(true);
        return overlay;
    }

    // =========================================================
    // Pause/Menu Overlay
    // =========================================================
    private StackPane createPauseOverlay() {
        StackPane overlay = baseOverlay(0.6);

        // ================= Speed slider with label =================
        Label speedLabel = new Label("Game Speed:");
        speedLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: white;");

        Slider speedSlider = new Slider(50, 500, game.getGameLoop() != null ? game.getGameLoop().getKeyFrames().get(0).getTime().toMillis() : 200);
        speedSlider.setShowTickLabels(true);
        speedSlider.setShowTickMarks(true);
        speedSlider.setMajorTickUnit(100);
        speedSlider.setBlockIncrement(10);
        speedSlider.setPrefWidth(250);
        speedSlider.setFocusTraversable(false);

        // Wrap label + slider in HBox
        HBox speedBox = new HBox(10, speedLabel, speedSlider);
        speedBox.setAlignment(Pos.CENTER);

        Label resume = menuLabel("Resume", () -> {
            overlay.setVisible(false);
            game.resumeGame();
            game.getGameView().requestFocus();
        });

        Label quit = menuLabel("Quit", () -> System.exit(0));

        VBox box = new VBox(20, levelDropdown, speedBox, resume, quit);
        box.setAlignment(Pos.CENTER);

        overlay.getChildren().add(box);
        overlay.setVisible(false);
        return overlay;
    }

    // =========================================================
    // End Overlay
    // =========================================================
    private StackPane createEndOverlay() {
        StackPane overlay = baseOverlay(0.6);

        endLabel = new Label("GAME OVER");
        endLabel.setStyle("-fx-font-size: 36; -fx-text-fill: white; -fx-font-weight: bold;");

        Label restart = menuLabel("Restart", () -> {
            game.startGame();
            overlay.setVisible(false);
            startOverlay.setVisible(false);
            game.getGameView().requestFocus();
        });

        Label quit = menuLabel("Quit", () -> System.exit(0));

        // Move shared levelDropdown into end overlay dynamically
        VBox box = new VBox(20, endLabel, restart, quit);
        box.setAlignment(Pos.CENTER);

        overlay.getChildren().add(box);
        overlay.setVisible(false);

        return overlay;
    }

    // =========================================================
    // Public API for End Overlay
    // =========================================================
    public void showEndOverlay(String message) {
        endLabel.setText(message);

        // Move shared levelDropdown from pauseOverlay into end overlay
        VBox endBox = (VBox) endOverlay.getChildren().get(0);
        if (!endBox.getChildren().contains(levelDropdown)) {
            endBox.getChildren().add(1, levelDropdown); // insert after endLabel
        }

        endOverlay.setVisible(true);
    }

    // =========================================================
    // Helpers
    // =========================================================
    private StackPane baseOverlay(double opacity) {
        StackPane pane = new StackPane();
        pane.setAlignment(Pos.CENTER);
        pane.setPrefSize(800, 600);
        pane.setStyle("-fx-background-color: rgba(0,0,0," + opacity + ")");
        return pane;
    }

    private Label menuLabel(String text, Runnable action) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 24; -fx-text-fill: white;");
        label.setOnMouseClicked(e -> action.run());
        label.setOnMouseEntered(e -> label.setOpacity(0.8));
        label.setOnMouseExited(e -> label.setOpacity(1.0));
        return label;
    }

    // =========================================================
    // Public API for HUD
    // =========================================================
    public StackPane getStartOverlay() { return startOverlay; }
    public StackPane getPauseOverlay() { return pauseOverlay; }
    public StackPane getEndOverlay() { return endOverlay; }

    public void update() {
        Player player = game.getBoard().getPlayer();
        if (player == null) return;

        scoreLabel.setText("Score: " + player.getScore());

        if (player.hasKey()) {
            messageLabel.setText("Go to the Gate!");
            messageLabel.setStyle(
                "-fx-font-family: 'Monospaced'; -fx-font-size: 34px; -fx-text-fill: green; -fx-font-weight: bold;"
            );
        } else {
            messageLabel.setText("Get the Key!");
            messageLabel.setStyle(
                "-fx-font-family: 'Monospaced'; -fx-font-size: 34px; -fx-text-fill: red; -fx-font-weight: bold;"
            );
        }
    }
}
