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


// Text editor
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.geometry.Insets;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class MenuView extends HBox {

    private final Game game;

    // HUD
    private final Label scoreLabel;
    private final Label messageLabel;

    // Overlays
    private StackPane startOverlay;
    private StackPane pauseOverlay;
    private final StackPane endOverlay;
    private Label endLabel;

    // Separate dropdowns
    private ComboBox<String> pauseLevelDropdown;
    private ComboBox<String> endLevelDropdown;

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
    // Pause Overlay
    // =========================================================
    private StackPane createPauseOverlay() {
        StackPane overlay = baseOverlay(0.6);

        // ================= Speed slider =================
        Label speedLabel = new Label("Game Speed:");
        speedLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: white;");

        Slider speedSlider = new Slider(50, 500, game.getGameLoop() != null ? game.getGameLoop().getKeyFrames().get(0).getTime().toMillis() : 200); // normal min < max
        speedSlider.setShowTickLabels(true);
        speedSlider.setShowTickMarks(true);
        speedSlider.setMajorTickUnit(100);
        speedSlider.setBlockIncrement(10);
        speedSlider.setPrefWidth(250);
        speedSlider.setFocusTraversable(false);

        // Invert the value when updating game speed
        speedSlider.valueProperty().addListener((obs, oldVal, newVal) ->
            game.setGameSpeed(550 - newVal.intValue()) // 550 = 50 + 500
        );


        HBox speedBox = new HBox(10, speedLabel, speedSlider);
        speedBox.setAlignment(Pos.CENTER);

        // ================= Level dropdown =================
        pauseLevelDropdown = createLevelDropdown();

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

        // ================= Level dropdown =================
        endLevelDropdown = createLevelDropdown();

        VBox box = new VBox(20, endLabel, endLevelDropdown, restart, quit);
        box.setAlignment(Pos.CENTER);

        overlay.getChildren().add(box);
        overlay.setVisible(false);
        return overlay;
    }

    // =========================================================
    // Level dropdown factory (used for both overlays)
    // =========================================================
    private ComboBox<String> createLevelDropdown() {
        ComboBox<String> dropdown = new ComboBox<>();

        // Internal levels
        String[] internalLevels = {"Level 1", "Level 2", "Level 3", "Level 4"};
        for (String lvl : internalLevels) {
            dropdown.getItems().add(lvl);
        }

        // Load custom levels
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

        // =================== CUSTOM BUTTON CELL ===================
        // This forces the button (visible part) to always show "Level"
        dropdown.setButtonCell(new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText("Level"); // <-- ALWAYS shows "Level"
            }
        });

        // Handle selection normally
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

            // Show start overlay and hide others
            startOverlay.setVisible(true);
            pauseOverlay.setVisible(false);
            endOverlay.setVisible(false);
            update();
        });

        return dropdown;
    }

    // =========================================================
    // Level Editor
    // =========================================================
    private void openLevelEditor() {
        Stage stage = new Stage();

        TextArea editor = new TextArea();
        editor.setPrefSize(400, 300);

        // Starter template (VALID LEVEL)
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

    private void saveLevelToFile(String content) {
        try {
            // External folder for custom levels (writable even in JAR)
            File dir = new File("custom_levels");
            if (!dir.exists()) dir.mkdir();

            // Auto-number custom levels
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
    // Public API
    // =========================================================
    public StackPane getStartOverlay() { return startOverlay; }
    public StackPane getPauseOverlay() { return pauseOverlay; }
    public StackPane getEndOverlay() { return endOverlay; }

    public void showEndOverlay(String message) {
        endLabel.setText(message);
        // Force the dropdown to no selection so any click triggers reload
        // Clear selection safely
        endLevelDropdown.getSelectionModel().clearSelection();
        endLevelDropdown.setValue(null);

        endOverlay.setVisible(true);
    }

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
