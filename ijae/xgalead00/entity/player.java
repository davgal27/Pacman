package ijae.xgalead00.entity;

import javafx.scene.image.Image;
import ijae.xgalead00.Assets;

// Player entity which uses entity logic. Direction handling set externally
public class Player extends Entity { 

	public Player(int initx, int inity, Image[] BaseImages) {
		super(initx, inity, Assets.PACMAN_FRAMES);
	}

}