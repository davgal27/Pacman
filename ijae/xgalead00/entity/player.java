package ijae.xgalead00.entity;

import javafx.scene.image.Image;
import ijae.xgalead00.Assets;

// Player entity which uses entity logic. Direction handling set externally
public class Player extends Entity {
	
	private boolean alive = true;
	private int score = 0; 
	private boolean haskey = false;

	public Player(int initx, int inity, Image[] BaseImages) {
		super(initx, inity, Assets.PACMAN_FRAMES);
	}

	// Called by Game.java during update to handle different events with different tiles 
	public void TileEvents (Tiles[][] tiles) {
		Tiles tile = tiles[y][x];

		if (tile == Tiles.POINT) {
			tiles[y][x] = Tiles.EMPTY;
			score += 10;
		}

		if (tile == tiles.KEY) {
			tiles[y][x] = Tiles.EMPTY;
			haskey = true;
		}

		if (tile == tiles.GATE && haskey = true) {
			tiles[y][x] = Tiles.EMPTY;
			Javafx overlay message you win!

		}
	}

	public void die() {
		alive = false;
	}
	public int getScore() {
		return score;
	}
	public int isAlive() {
		return alive
	}

}