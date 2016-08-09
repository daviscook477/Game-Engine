package net.engine.math;

/**
 * A vector of 2 length storing floating point values
 * @author Davis
 */
public class Vector2f implements Cloneable
{
    
    public float x, y;
    
    /**
     * Creates a new vector of length 2 that stores floating point numbers
     * @param x the first floating point number
     * @param y the second floating point number
     */
    public Vector2f(float x, float y)
    {
        this.x = x;
        this.y = y;
    }
    
    @Override
    public String toString()
    {
        return "(" + x + ", " + y + ")";
    }
    
    @Override
    public Vector2f clone()
    {
    	return new Vector2f(x, y);
    }
    
    /**
     * Gets the state of the vector as an array
     * @return an array of the form {x, y}
     */
    public float[] getAsArray()
    {
        return new float[] {x, y};
    }
    
    /**
     * Gets the length of the vector
     * @return  the length of the vector
     */
    public float length()
    {
        return (float) Math.sqrt(x * x + y * y);
    }
    
    /**
     * Gets the absolute value of the vector
     * @return the absolute value of the vector
     */
    public Vector2f abs()
    {
    	return (new Vector2f(Math.abs(x), Math.abs(y)));
    }
    
    /**
     * Gets the dot product of this vector and another
     * @param other the other vector
     * @return the dot product
     */
    public float dot(Vector2f other)
    {
        return x * other.x + y * other.y;
    }
    
    /**
     * Calculates the normal of the vector
     * @return the normal
     */
    public Vector2f normal()
    {
        float length = length();
        
        return (new Vector2f(x / length, y / length));
    }
    
    /**
     * Rotates this vector by angle angle
     * @param angle the angle to rotate by
     * @return the vector once rotated
     */
    public Vector2f rotate(float angle)
    {
        float cos = (float) Math.cos(angle);
        float sin = (float) Math.sin(angle);
        
        return (new Vector2f(x * cos - y * sin, x * sin + y * cos));
    }
    
    /**
     * Adds this vector and another
     * @param other the other
     * @return the vector of their addition
     */
    public Vector2f add(Vector2f other)
    {
        return (new Vector2f(x + other.x, y + other.y));
    }
    
    /**
     * Adds a scalar to this vector
     * @param scalar the scalar
     * @return the vector of the addition
     */
    public Vector2f add(float scalar)
    {
        return (new Vector2f(x + scalar, y + scalar));
    }
    
    /**
     * Subtracts this vector and another
     * @param other the other
     * @return the vector of their subtraction
     */
    public Vector2f sub(Vector2f other)
    {
        return (new Vector2f(x - other.x, y - other.y));
    }
    
    /**
     * Subtracts a scalar to this vector
     * @param scalar the scalar
     * @return the vector of the subtraction
     */
    public Vector2f sub(float scalar)
    {
        return (new Vector2f(x - scalar, y - scalar));
    }
    
    /**
     * Multiplies this vector and another
     * @param other the other
     * @return the vector of their multiplication
     */
    public Vector2f mul(Vector2f other)
    {
        return (new Vector2f(x * other.x, y * other.y));
    }
    
    /**
     * Multiplies a scalar to this vector
     * @param scalar the scalar
     * @return the vector of the multiplication
     */
    public Vector2f mul(float scalar)
    {
        return (new Vector2f(x * scalar, y * scalar));
    }
    
    /**
     * Divides this vector and another
     * @param other the other
     * @return the vector of their division
     */
    public Vector2f div(Vector2f other)
    {
        return (new Vector2f(x / other.x, y / other.y));
    }
    
    /**
     * Divides a scalar to this vector
     * @param scalar the scalar
     * @return the vector of the division
     */
    public Vector2f div(float scalar)
    {
        return (new Vector2f(x / scalar, y / scalar));
    }
    
}
