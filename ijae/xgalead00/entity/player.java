package ijae.xgalead00.entity;

import javafx.scene.image.Image;
import ijae.xgalead00.assets;

// Player entity which uses entity logic. Direction handling set externally
public class Player extends Entity { 

	public Player(int initx, int inity, Image[] BaseImages, int TileWidth, int TileHeight) {
		super(initx, inity, assets.PACMAN_FRAMES, TileWidth, TileHeight);
	}

}