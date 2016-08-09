package net.engine.render;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.engine.core.Engine;

import org.newdawn.slick.opengl.TextureLoader;

/**
 * A texture in openGL
 * @author Davis
 *
 */
public class Texture
{
	
	private int[] bitMap; //The bitmap of data
	
	private int width; //Image width
	private int height; //Image height

	private int textureHandle; //The actual pointer
	
	private BufferedImage image; //The image
	
	/**
	 * Creates a new texture
	 * @param fileName the file to load from
	 */
	public Texture(String fileName)
	{
		this.textureHandle = loadTexture(fileName);
		this.image = loadImage(fileName);
		this.bitMap = getBitMap(image);
		this.width = image.getWidth();
		this.height = image.getHeight();
	}
	
	/**
	 * Gets the texture pointer
	 * @return the texture pointer
	 */
	public int getTextureHandle()
	{
		return textureHandle;
	}
	
	/**
	 * Binds the texture to the specified location
	 * @param location the location to bind to
	 */
	public void bind(int location)
	{
		glActiveTexture(location); //Set the operated on texture to the location
		glBindTexture(GL_TEXTURE_2D, textureHandle); //Bind the texture to that location
	}
	
	/**
	 * Gets the bit map
	 * @return the bit map
	 */
	public int[] getBitMap()
	{
		return bitMap;
	}
	
	/**
	 * Get the rgb data
	 * @param x the x position of the image
	 * @param y the y position of the image
	 * @return the rgb data as an int
	 */
	public int getData(int x, int y)
	{
		return bitMap[x + (y * width)];
	}
	
	/**
	 * Gets the image's width
	 * @return the width
	 */
	public int getWidth()
	{
		return width;
	}
	
	/**
	 * Gets the image's height
	 * @return the height
	 */
	public int getHeight()
	{
		return height;
	}
	
	/**
	 * Loads a texture and obtains the openGL pointer
	 * @param fileName the file name
	 * @return the texture's handle
	 */
	private static int loadTexture(String fileName)
	{
		String[] splitArray = fileName.split("\\.");
		String ext = splitArray[splitArray.length - 1];
		
		try
		{
			int id = TextureLoader.getTexture(ext, new FileInputStream(new File(Engine.getInstance().getTextureLocation() + fileName))).getTextureID();
			
			return id;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("Could not load texture");
			System.exit(1);
		}
		
		return 0;
	}
	
	/**
	 * Loads the bitmaps of a texture
	 * @param image the image to make a bitmap of
	 * @return the bitmap
	 */
	private static int[] getBitMap(BufferedImage image)
	{
		int[] bitMap = new int[image.getWidth() * image.getHeight()];
		
		for (int i = 0; i < image.getWidth(); i++)
		{
			for (int j = 0; j < image.getHeight(); j++)
			{
				bitMap[i + (j * image.getWidth())] = image.getRGB(i, j);
			}
		}
		
		return bitMap;
	}
	
	/**
	 * Loads an image
	 * @param fileName the file to load from
	 * @return the image
	 */
	private static BufferedImage loadImage(String fileName)
	{
		BufferedImage image = null;
		
		try
		{
			image = ImageIO.read(new File(Engine.getInstance().getTextureLocation() + fileName));
		}
		catch (IOException e)
		{
			System.out.println("Could not load texture: " + fileName);
			e.printStackTrace();
			System.exit(1);
		}
		
		return image;
	}
	
}
