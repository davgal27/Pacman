package ijae.xgalead00.entity;

import javafx.scene.image.Image;
import ijae.xgalead00.Assets;
import ijae.xgalead00.Tiles;

// Player entity which uses entity logic. Direction handling set externally
public class Player extends Entity {
	
	private boolean alive = true;
	private int score = 0; 
	private boolean hasKey = false;

	public Player(int initx, int inity, Image[] BaseImages) {
		super(initx, inity, Assets.PACMAN_FRAMES);
	}
	private Runnable onKeyCollected;

	public void setOnKeyCollected(Runnable callback) {
	    this.onKeyCollected = callback;
	}

	// Called by Game.java during update to handle different events with different tiles 
	public void TileEvents (Tiles[][] tiles) {
		Tiles tile = tiles[y][x];

		if (tile == Tiles.POINT) {
			tiles[y][x] = Tiles.EMPTY;
			score += 10;
		}

		if (tile == Tiles.KEY) {
			tiles[y][x] = Tiles.EMPTY;
			hasKey = true;

	        if (onKeyCollected != null) {
	            onKeyCollected.run(); // tell the game something happened
	        }
		}
	}

	public void die() {
		alive = false;
	}
	public int getScore() {
		return score;
	}
	public boolean isAlive() {
		return alive;
	}
	public boolean hasKey() {
		return hasKey;
	}

}