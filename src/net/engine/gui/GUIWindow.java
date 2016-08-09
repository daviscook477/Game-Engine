package net.engine.gui;

import java.util.HashMap;

/**
 * A window for the GUI
 * @author Davis
 *
 */
public abstract class GUIWindow
{
	
	private HashMap<String, String> properties;

	private float width, height;
	
	/**
	 * Creates a new gui window
	 * @param width the width
	 * @param height the height
	 */
	public GUIWindow(float width, float height)
	{
		this.width = width;
		this.height = height;
		properties = new HashMap<String, String>();
		initializeProperties();
	}
	
	/**
	 * Sets one of this windows properties
	 * @param property the property
	 * @param value the value
	 */
	public void setProperty(String property, String value)
	{
		properties.put(property, value);
	}
	
	/***
	 * Gets the value of a property
	 * @param property the property to get the value of
	 * @return the value
	 */
	public String getProperty(String property)
	{
		return properties.get(property);
	}
	
	/**
	 * Gets the width
	 * @return the width
	 */
	public float getWidth()
	{
		return width;
	}
	
	/**
	 * Gets the height
	 * @return the height
	 */
	public float getHeight()
	{
		return height;
	}
	
	/**
	 * Intializes this windows properties
	 */
	public abstract void initializeProperties();
	
	/**
	 * Gets input of the mouse in this window's relative coordinates
	 * @param x the x
	 * @param y the y
	 * @return if the GUI used this input
	 */
	public abstract boolean input(float x, float y);
	
	/**
	 * Renders the window
	 * @param x the x
	 * @param y the y
	 * @param width the width
	 * @param height the height
	 */
	public abstract void render(float x, float y, float width, float height);
	
}
