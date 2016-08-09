package net.engine.core;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.Display;

import static org.lwjgl.opengl.GL11.*;

/**
 * The window that will display the game
 * @author Davis
 *
 */
public class Window
{
	
	private static int version; //The opengl version we are running

	private Window() {}
	
	/**
	 * Gets the opengl version
	 * @return the version
	 */
	public static int getVersion()
	{
		return version;
	}
	
	/**
	 * Creates a window without setting the title
	 * @param width the width
	 * @param height the height
	 */
	public static void createWindow(int width, int height)
	{
		createWindow(width, height, null);
	}
	
	/**
	 * Creates a new window
	 * @param width the window's width
	 * @param height the window's height
	 * @param title the title of the window
	 */
	public static void createWindow(int width, int height, String title)
	{
		if (title != null)
		{
			Display.setTitle(title); //Set the title
		}
		try
		{
			Display.setDisplayMode(new DisplayMode(width, height)); //Set the size
			Display.create();
			String[] parts = glGetString(GL_VERSION).split("\\.");
			version = Integer.parseInt(parts[0]) * 10 + Integer.parseInt(parts[1]); //Store the version
		}
		catch (LWJGLException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Sets the title of the window
	 * @param title the title
	 */
	public static void setTitle(String title)
	{
		if (title != null)
		{
			Display.setTitle(title);
		}
	}
	
	/**
	 * Updates the window
	 */
	public static void update()
	{
		//TODO: something
	}
	
	/**
	 * Renders the window
	 */
	public static void render()
	{
		Display.update();
	}
	
	/**
	 * Cleans up the window
	 */
	public static void dispose()
	{
		Display.destroy();
	}
	
	/**
	 * Returns if the user has clicked the close button on the window
	 * @return if the user has clicked the close button on the window
	 */
	public static boolean isCloseRequested()
	{
		return Display.isCloseRequested();
	}
	
	/**
	 * Returns the current width of the window
	 * @return the width of the window
	 */
	public static int getWidth()
	{
		return Display.getDisplayMode().getWidth();
	}
	
	/**
	 * Returns the current height of the window
	 * @return the height of the window
	 */
	public static int getHeight()
	{
		return Display.getDisplayMode().getHeight();
	}
	
	/**
	 * Returns the title of the window
	 * @return the title of the window
	 */
	public static String getTitle()
	{
		return Display.getTitle();
	}
	
}
