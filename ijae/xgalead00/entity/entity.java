package ijae.xgalead00.entity;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.aw.geom.AffineTransform;
import ijae.xgalead00.Direction;


// abstract class for any entity on the bard (player or ghost) 
// handles: position, movement, animation cycling, rotration of images
public abstract class Entity{
	protected int x, y; // board coordinates 
	protected Direction direction = Direction.RIGHT; // starting direction

	// Base image: right facing
	protected BufferedImage[] BaseImage;
	// rotated imaages based on direction
	protected BufferedImage[] RightImage, LeftImage, UpImage, DownImage

	// animation state
	protected int ImageIndex = 0; //current image in animation
	protected int AnimationSpeed = 5; //steps per image change
	protected int StepCounter = 0; // steps to switch frame

	// Tile sizes in pixels 
	protected int TileWidth;
	protected int TileHeight;

}