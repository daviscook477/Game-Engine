package net.engine.render;

import net.engine.math.Vector3f;

/**
 * A material
 * @author Davis
 *
 */
public class Material
{

	private Texture tex;
	private Vector3f color;
	
	/**
	 * Creates a new material
	 * @param tex its texture
	 * @param color its color
	 */
	public Material(Texture tex, Vector3f color)
	{
		this.tex = tex;
		this.color = color;
	}
	
	/**
	 * Gets the texture
	 * @return the texture
	 */
	public Texture getTexture()
	{
		return tex;
	}
	
	/**
	 * Gets the color
	 * @return the color
	 */
	public Vector3f getColor()
	{
		return color;
	}
	
}
