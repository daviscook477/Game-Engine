package net.engine.util;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

/**
 * Mesh utilities
 * @author Davis
 */
public class BufferUtil
{
    
    /**
     * This class should not have instances
     */
    private BufferUtil() {}
    
    /**
     * Creates a float buffer of a size
     * @param size the size
     * @return a new float buffer
     */
    public static FloatBuffer createFloatBuffer(int size)
    {
        return BufferUtils.createFloatBuffer(size);
    }
    
    /**
     * Creates an int buffer of a size
     * @param size the size
     * @return a new int buffer
     */
    public static IntBuffer createIntBuffer(int size)
    {
    	return BufferUtils.createIntBuffer(size);
    }
    
}
