package net.engine.render.mesh;

import net.engine.math.Vector2f;
import net.engine.math.Vector3f;

/**
 * A vertex in a mesh that has a position and may have texture or normal data
 * @author Davis
 */
public class Vertex implements Cloneable
{
    public static final int POSITION_SIZE = 3;
    public static final int TEXTURE_SIZE = 2;
    public static final int NORMAL_SIZE = 3;
    
    public Vector3f pos;
    public Vector2f tex;
    public Vector3f normal;
    
    /**
     * Creates a new vertex
     * @param pos the vector3f that represents the position of this vertex
     * @param tex the vector3f that represents the texture coords
     * @param normal the vector3f that represents the surface normals
     */
    public Vertex(Vector3f pos, Vector2f tex, Vector3f normal)
    {
        this.pos = pos;
        this.tex = tex;
        this.normal = normal;
    }
    
    @Override
    public String toString()
    {
    	String result = "";
    	result += pos.toString() + " ";
    	if (tex != null)
    	{
    		result += tex.toString() + " ";
    	}
    	if (normal != null)
    	{
    		result += normal.toString();
    	}
    	return result;
    }
    
    @Override
    public Vertex clone()
    {
    	return new Vertex(pos.clone(), tex.clone(), normal.clone());
    }
    
    /**
     * Gets the size of the vertex
     * @return the size of the vertex
     */
    public int getSize()
    {
    	int size = POSITION_SIZE;
    	
    	if (tex != null)
    	{
    		size += TEXTURE_SIZE;
    	}
    	if (normal != null)
    	{
    		size += NORMAL_SIZE;
    	}
    	
    	return size;
    }
    
}
