package ijae.xgalead00.gui;

import ijae.xgalead00.Game;

import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;

//Menu bar 
public class MenuView extends HBox {

    public MenuView(Game game) {

        Slider speedSlider = new Slider(50, 500, 200);
        speedSlider.setShowTickLabels(true);
        speedSlider.setFocusTraversable(false); //avoids interfering with pacman movement

        speedSlider.valueProperty().addListener((obs, oldVal, newVal) ->
            game.setGameSpeed(newVal.intValue())
        );

        Button level1 = new Button("Level 1");
        level1.setOnAction(e -> game.loadLevel("levels/level1.txt"));

        getChildren().addAll(level1, speedSlider);
        setSpacing(10);
    }
}
