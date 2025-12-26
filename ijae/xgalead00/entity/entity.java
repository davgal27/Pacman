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
	protected BufferedImage[] BaseImages;
	// rotated imaages based on direction
	protected BufferedImage[] RightImages, LeftImages, UpImages, DownImages

	// animation state
	protected int ImageIndex = 0; //current image in animation
	protected int AnimationSpeed = 5; //steps per image change
	protected int StepCounter = 0; // steps to switch frame

	// Tile sizes in pixels 
	protected int TileWidth;
	protected int TileHeight;


	// Constructor
	// initx: initial starting x coordinate
	// inity: initial starting y coordinate
	// BaseImage: right facing image 

	public Entity(int initx, int inity, BufferedImage[] BaseImages, int TileWidth, int TileHeight) {
		this.x = initx;
		this.y = inity;
		this.BaseImages = BaseImages;
		this.TileWidth = TileWidth;
		this.TileHeight = TileHeight;

		//rotation of right facing images
		RightImages = BaseImages;
		DownImages = new BufferedImage[BaseImages.length];
		LeftImages = new BufferedImage[BaseImages.length];
		UpImages = new BufferedImage[BaseImages.length];

		for (int i = 0; i < BaseImages.length; i++) {
			DownImages[i] = RotateImage(BaseImages[i], 90);
			LeftImages[i] = RotateImage(BaseImages[i], 180);
			UpImages[i] = RotateImage(BaseImages[i], -90);
		}
	}

	protected BufferedImage RotateImage(BufferedImage image, double angle) {
		int ImageWidth = img.getWidth();
		int Image
	}
}