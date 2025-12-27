package ijae.xgalead00.entity;

import javafx.scene.image.Image;

// Player entity which uses entity logic. Direction handling set externally
public class Player extends Entity { 

	public Player(int initx, int inity, Image[] BaseImages, int TileWidth, int TileHeight) {
		super(initx, inity, BaseImages, TileWidth, TileHeight);
	}

}