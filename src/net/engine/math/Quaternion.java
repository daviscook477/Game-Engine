/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.engine.math;


/**
 * A quaternion
 * @author Davis
 */
public class Quaternion
{
    
    public float x, y, z, w;
    
    public Quaternion(float x, float y, float z, float w)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }
    
    /**
     * Returns the length of this quaternion
     * @return the length of this quaternion
     */
    public float length()
    {
        return (float) Math.sqrt(x * x + y * y + z * z + w * w);
    }
    
    /**
     * Normalizes this quaternion
     * @return a normalized version of this quaternion
     */
    public Quaternion normal()
    {
        float inverseL = 1 / length();
        
        return (new Quaternion(x * inverseL, y * inverseL, z * inverseL, w * inverseL));
    }
    
    /**
     * Returns the conjugate of this quaternion
     * @return the conjugate of this quaternion
     */
    public Quaternion conjugate()
    {
        return (new Quaternion(-x, -y, -z, w));
    }
    
    /**
     * Multiplies this quaternion by another
     * @param other the other
     * @return the resulting quaternion
     */
    public Quaternion mul(Quaternion other)
    {
        float _w = w * other.w - x * other.x - y * other.y - z * other.z;
        float _x = x * other.w + w * other.x + y * other.z - z * other.y;
        float _y = y * other.w + w * other.y + z * other.x - x * other.z;
        float _z = z * other.w + w * other.z + x * other.y - y * other.x;
        
        return (new Quaternion(_x, _y, _z, _w));
    }
    
    /**
     * Multiplies this quaternion by a vector
     * @param other the vector
     * @return the resulting quaternion
     */
    public Quaternion mul(Vector3f other)
    {
        float w_ = -x * other.x - y * other.y - z * other.z;
        float x_ = w * other.x + y * other.z - z * other.y;
        float y_ = w * other.y + z * other.x - x * other.z;
        float z_ = w * other.z + x * other.y - y * other.x;
        
        return (new Quaternion(x_, y_, z_, w_));
    }
    
}
