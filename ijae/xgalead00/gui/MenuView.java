package ijae.xgalead00.gui;

import ijae.xgalead00.Game;
import ijae.xgalead00.entity.Player;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.geometry.Insets;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * MenuView handles the game HUD and overlays including:
 * - Score and message display
 * - Start, pause, and end overlays
 * - Level selection and custom level creation
 */
public class MenuView extends HBox {

    private final Game game;

    /** HUD elements */
    private final Label scoreLabel;
    private final Label messageLabel;

    /** Overlay panes */
    private StackPane startOverlay;
    private StackPane pauseOverlay;
    private final StackPane endOverlay;
    private Label endLabel;

    /** Level selection dropdowns */
    private ComboBox<String> pauseLevelDropdown;
    private ComboBox<String> endLevelDropdown;

    /**
     * Constructs the MenuView with HUD and overlays.
     *
     * @param game the main game instance
     */
    public MenuView(Game game) {
        this.game = game;

        setSpacing(10);
        setAlignment(Pos.CENTER_LEFT);

        // HUD elements
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

        // Create overlays
        startOverlay = createStartOverlay();
        pauseOverlay = createPauseOverlay();
        endOverlay = createEndOverlay();
    }

    /**
     * Creates the menu button that opens the pause overlay.
     */
    private ImageView createMenuButton() {
        Image image = new Image(
            getClass().getResourceAsStream("/ijae/xgalead00/assets/menu_button.png")
        );

        ImageView button = new ImageView(image);
        button.setFitWidth(200);
        button.setPreserveRatio(true);
        button.setFocusTraversable(false);

        // Stop game loop and show pause overlay on click
        button.setOnMouseClicked(e -> {
            if (game.getGameLoop() != null) game.getGameLoop().stop();
            pauseOverlay.setVisible(true);
        });

        button.setOnMouseEntered(e -> button.setOpacity(0.8));
        button.setOnMouseExited(e -> button.setOpacity(1.0));

        return button;
    }

    /**
     * Creates the start overlay with a start button.
     */
    private StackPane createStartOverlay() {
        StackPane overlay = baseOverlay(0.4);

        Image startImage = new Image(
            getClass().getResourceAsStream("/ijae/xgalead00/assets/start_button.png")
        );

        ImageView startButton = new ImageView(startImage);
        startButton.setFitWidth(300);
        startButton.setPreserveRatio(true);

        // Start the game and hide overlay on click
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

    /**
     * Creates the pause overlay with:
     * - Speed slider
     * - Level selection
     * - Resume, quit, and level editor buttons
     */
    private StackPane createPauseOverlay() {
        StackPane overlay = baseOverlay(0.6);

        // Speed slider controls game loop speed
        Label speedLabel = new Label("Game Speed:");
        speedLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: white;");

        Slider speedSlider = new Slider(50, 500, game.getGameLoop() != null ? game.getGameLoop().getKeyFrames().get(0).getTime().toMillis() : 200); // normal min < max
        speedSlider.setShowTickLabels(true);
        speedSlider.setShowTickMarks(true);
        speedSlider.setMajorTickUnit(100);
        speedSlider.setBlockIncrement(10);
        speedSlider.setPrefWidth(250);
        speedSlider.setFocusTraversable(false);

        // Invert the value to map slider to game speed correctly
        speedSlider.valueProperty().addListener((obs, oldVal, newVal) ->
            game.setGameSpeed(550 - newVal.intValue())
        );

        HBox speedBox = new HBox(10, speedLabel, speedSlider);
        speedBox.setAlignment(Pos.CENTER);

        // Level dropdown
        pauseLevelDropdown = createLevelDropdown();

        // Control buttons
        Label resume = menuLabel("Resume", () -> {
            overlay.setVisible(false);
            game.resumeGame();
            game.getGameView().requestFocus();
        });

        Label quit = menuLabel("Quit", () -> System.exit(0));
        Label createLevel = menuLabel("Create Level", this::openLevelEditor);

        VBox box = new VBox(20, pauseLevelDropdown, speedBox, createLevel, resume, quit);
        box.setAlignment(Pos.CENTER);

        overlay.getChildren().add(box);
        overlay.setVisible(false);
        return overlay;
    }

    /**
     * Creates the end overlay shown when the game ends, including:
     * - Message label
     * - Level selection
     * - Restart and quit buttons
     */
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

        endLevelDropdown = createLevelDropdown();

        VBox box = new VBox(20, endLabel, endLevelDropdown, restart, quit);
        box.setAlignment(Pos.CENTER);

        overlay.getChildren().add(box);
        overlay.setVisible(false);
        return overlay;
    }

    /**
     * Creates a dropdown for selecting levels (internal or custom).
     */
    private ComboBox<String> createLevelDropdown() {
        ComboBox<String> dropdown = new ComboBox<>();

        // Add internal levels
        String[] internalLevels = {"Level 1", "Level 2", "Level 3", "Level 4"};
        for (String lvl : internalLevels) dropdown.getItems().add(lvl);

        // Load custom levels from folder
        File dir = new File("custom_levels");
        if (dir.exists() && dir.isDirectory()) {
            for (File f : dir.listFiles()) {
                if (f.getName().startsWith("custom") && f.getName().endsWith(".txt")) {
                    String name = f.getName().replace(".txt", "");
                    name = name.substring(0, 1).toUpperCase() + name.substring(1);
                    dropdown.getItems().add(name.replaceAll("custom", "Custom "));
                }
            }
        }

        dropdown.setPrefWidth(150);
        dropdown.setStyle("-fx-font-size: 18px;");
        dropdown.setPromptText("Level");

        // Always display "Level" as the visible text
        dropdown.setButtonCell(new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText("Level");
            }
        });

        // Load level when selected
        dropdown.setOnAction(e -> {
            String value = dropdown.getValue();
            if (value == null) return;

            try {
                if (value.startsWith("Custom")) {
                    int id = Integer.parseInt(value.replaceAll("\\D", ""));
                    game.loadLevel("custom_levels/custom" + id + ".txt");
                } else {
                    String path = switch (value) {
                        case "Level 1" -> "ijae/xgalead00/levels/level1.txt";
                        case "Level 2" -> "ijae/xgalead00/levels/level2.txt";
                        case "Level 3" -> "ijae/xgalead00/levels/level3.txt";
                        case "Level 4" -> "ijae/xgalead00/levels/level4.txt";
                        default -> throw new IllegalArgumentException("Unknown level: " + value);
                    };
                    game.loadLevel(path);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            if (game.getGameLoop() != null) game.getGameLoop().stop();
            game.getGameView().redraw();

            // Show start overlay, hide others
            startOverlay.setVisible(true);
            pauseOverlay.setVisible(false);
            endOverlay.setVisible(false);
            update();
        });

        return dropdown;
    }

    /**
     * Opens a text editor window to create a custom level.
     */
    private void openLevelEditor() {
        Stage stage = new Stage();

        TextArea editor = new TextArea();
        editor.setPrefSize(400, 300);

        // Starter template
        editor.setText(
            "3 4\n" +
            "P.KW\n" +
            "WWoo\n" +
            "WG.o\n"
        );

        Button save = new Button("Save Level");
        save.setOnAction(e -> {
            saveLevelToFile(editor.getText());
            stage.close();
        });

        VBox box = new VBox(10, editor, save);
        box.setPadding(new Insets(10));
        box.setAlignment(Pos.CENTER);

        stage.setScene(new Scene(box));
        stage.setTitle("Create Level");
        stage.show();
    }

    /**
     * Saves the custom level to a file and updates the dropdowns.
     *
     * @param content the level text
     */
    private void saveLevelToFile(String content) {
        try {
            File dir = new File("custom_levels");
            if (!dir.exists()) dir.mkdir();

            int id = 1;
            for (File f : dir.listFiles()) {
                if (f.getName().startsWith("custom")) id++;
            }

            File file = new File(dir, "custom" + id + ".txt");

            try (FileWriter writer = new FileWriter(file)) {
                writer.write(content);
            }

            // Add new level to dropdowns
            String levelName = "Custom " + id;
            pauseLevelDropdown.getItems().add(levelName);
            endLevelDropdown.getItems().add(levelName);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Creates a semi-transparent overlay pane.
     *
     * @param opacity the overlay opacity (0.0–1.0)
     */
    private StackPane baseOverlay(double opacity) {
        StackPane pane = new StackPane();
        pane.setAlignment(Pos.CENTER);
        pane.setPrefSize(800, 600);
        pane.setStyle("-fx-background-color: rgba(0,0,0," + opacity + ")");
        return pane;
    }

    /**
     * Creates a label styled as a menu option with click action.
     *
     * @param text the label text
     * @param action the action to execute on click
     */
    private Label menuLabel(String text, Runnable action) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 24; -fx-text-fill: white;");
        label.setOnMouseClicked(e -> action.run());
        label.setOnMouseEntered(e -> label.setOpacity(0.8));
        label.setOnMouseExited(e -> label.setOpacity(1.0));
        return label;
    }

    // Public API
    public StackPane getStartOverlay() { return startOverlay; }
    public StackPane getPauseOverlay() { return pauseOverlay; }
    public StackPane getEndOverlay() { return endOverlay; }

    /**
     * Displays the end overlay with a custom message.
     *
     * @param message the text to show
     */
    public void showEndOverlay(String message) {
        endLabel.setText(message);
        endLevelDropdown.getSelectionModel().clearSelection();
        endLevelDropdown.setValue(null);

        endOverlay.setVisible(true);
    }

    /**
     * Updates HUD elements based on player state (score, key).
     */
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
