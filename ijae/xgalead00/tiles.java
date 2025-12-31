package ijae.xgalead00;

import javafx.scene.image.Image;

public enum Tiles {
    WALL,
    GATE,
    EMPTY,
    POINT,
    KEY;

    public Image GetImage() {
        return switch(this) {
            case WALL -> Assets.WALL;
            case EMPTY -> Assets.EMPTY;
            case POINT -> Assets.POINT;
            case KEY -> Assets.KEY;
            case GATE -> Assets.GATE;
        };
    }

    public boolean IsAccessible() {
        return this != WALL;
    }
}
