package net.engine.gui;

import java.util.ArrayList;

import net.engine.core.Window;

/**
 * A manager of the gui
 * @author Davis
 *
 */
public class GUIManager
{
	
	public static final String GUI_VERTEX_SHADER = "//VERTEX SHADER\n"
	+ "#version 330\n"
	+ "layout (location = 0) in vec3 position;\n"
	+ "layout (location = 1) in vec2 texCoord;\n"
	+ "out vec2 texCoord0;\n"
	+ "void main()\n"
	+ "{\n"
	+ "gl_Position = vec4(position, 1);\n"
	+ "texCoord0 = texCoord;\n"
	+ "}";
	
	public static final String GUI_FRAGMENT_SHADER = "//FRAGMENT SHADER\n"
	+ "#version 330\n"
	+ "in vec2 texCoord0; //Input the texture coordinate from the vs\n"
	+ "uniform vec3 color; //Take in a color\n"
	+ "uniform sampler2D sampler; //Take in a texture\n"
	+ "void main()\n"
	+ "{\n"
	+ "vec4 textureColor = texture2D(sampler, texCoord0.xy); //Create a color by sampling the texture\n"
	+ "gl_FragColor = textureColor * vec4(color, 1); //Output the texture masked with the color\n"
	+ "}";

	private ArrayList<GUIElement> windows;
	
	
	/**
	 * Creates a new GUIManager
	 */
	public GUIManager()
	{
		windows = new ArrayList<GUIElement>();
	}
	
	/**
	 * Adds a gui to the list of running gui's
	 * @param window the window
	 * @param x the x for it to be loaded at
	 * @param y the y for it to be loaded at
	 */
	public void addGUI(GUIWindow window, float x, float y)
	{
		windows.add(new GUIElement(window, x, y));
	}
	
	/**
	 * Renders the gui
	 */
	public void render()
	{
		for (GUIElement e : windows)
		{
			//Convert pixel coordinates to opengl coords
			float x_ = (e.x - (Window.getWidth() / 2)) / (Window.getWidth() / 2);
			float y_ = (e.y - (Window.getHeight() / 2)) / (Window.getHeight() / 2);
			float width_ =  2 * (e.window.getWidth() / Window.getWidth());
			float height_ = 2 * (e.window.getHeight() / Window.getHeight());
			e.window.render(x_ , y_, width_, height_);
		}
	}
	
	/**
	 * A GUI window with a location in x and y
	 * @author Davis
	 *
	 */
	private static class GUIElement
	{
		
		public GUIWindow window;
		
		public float x, y;
		
		/**
		 * Creates a new element
		 * @param window the window
		 * @param x the x
		 * @param y the y
		 */
		public GUIElement(GUIWindow window, float x, float y)
		{
			this.window = window;
			this.x = x;
			this.y = y;
		}
		
	}
	
}
