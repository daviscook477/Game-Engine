package net.engine.render;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL33.*;

/**
 * Wrapper for sampler objects
 * @author Davis
 *
 */
public class Sampler
{
	
	private int samplerHandle;

	/**
	 * Creates a new sampler
	 */
	public Sampler()
	{
		samplerHandle = glGenSamplers();
	}
	
	/**
	 * Gets the pointer to the sampler
	 * @return the pointer
	 */
	public int getSamplerHandle()
	{
		return samplerHandle;
	}
	
	/**
	 * Sets the min and mag filter type
	 * @param minFilter the min filter type
	 * @param magFilter the mag filter type
	 */
	public void setFiltering(int minFilter, int magFilter)
	{
		glSamplerParameteri(samplerHandle, GL_TEXTURE_MIN_FILTER, minFilter);
        glSamplerParameteri(samplerHandle, GL_TEXTURE_MAG_FILTER, magFilter);
	}
	
	/**
	 * Binds this sampler to a texture
	 * @param location the location of the texture
	 */
	public void bind(int location)
	{
		glBindSampler(location, samplerHandle);
	}
	
}
