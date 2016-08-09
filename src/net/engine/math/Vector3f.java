package net.engine.math;

import net.engine.matrix.Matrix4f;


/**
 * A vector of length three with floating point values
 * @author Davis
 */
public class Vector3f implements Cloneable
{

    public float x, y, z;
    
    /**
     * Creates a new vector of length three with floating point values
     * @param x the first floating point number
     * @param y the second floating point number
     * @param z the third floating point number
     */
    public Vector3f(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    @Override
    public String toString()
    {
        return "(" + x + ", " + y + ", " + z + ")";
    }
    
    @Override
    public Vector3f clone()
    {
    	return new Vector3f(x, y, z);
    }
    
    /**
     * Gets the state of the vector as an array
     * @return the vector in the form {x, y, z}
     */
    public float[] getAsArray()
    {
        return new float[] {x, y, z};
    }
    
    /**
     * Gets the length of the vector
     * @return the length of the vector
     */
    public float length()
    {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }
    
    /**
     * Gets the absolute value of the vector
     * @return the absolute value of the vector
     */
    public Vector3f abs()
    {
    	return (new Vector3f(Math.abs(x), Math.abs(y), Math.abs(z)));
    }
    
    /**
     * Gets the dot product of the vector and another
     * @param other the other vector
     * @return the dot product
     */
    public float dot(Vector3f other)
    {
        return x * other.x + y * other.y + z * other.z;
    }
    
    /**
     * Gets the normal of this vector
     * @return the normal
     */
    public Vector3f normal()
    {
        float inverseL = 1 / length();
        return (new Vector3f(x * inverseL, y * inverseL, z * inverseL));
    }
    
    /**
     * Gets the cross product of this vector and another
     * @param other the other
     * @return the cross product
     */
    public Vector3f cross(Vector3f other)
    {
        float x_ = y * other.z - z * other.y;
        float y_ = z * other.x - x * other.z;
        float z_ = x * other.y - y * other.x;
        
        return (new Vector3f(x_, y_, z_));
    }
    
    /**
     * Gets this vector after rotated through a 3d angle
     * @param angle the angle
     * @param axis the axis to rotate around
     * @return the vector after rotation
     */
    public Vector3f rotate(float angle, Vector3f axis)
    {
    	float sinHalfAngle = (float) Math.sin(angle / 2);
    	float cosHalfAngle = (float) Math.cos(angle / 2);
    	
    	float rX = axis.x * sinHalfAngle;
    	float rY = axis.y * sinHalfAngle;
    	float rZ = axis.z * sinHalfAngle;
    	float rW = cosHalfAngle;
    	
    	Quaternion rotation = new Quaternion(rX, rY, rZ, rW);
    	Quaternion conjugate = rotation.conjugate();
    	
    	Quaternion result = rotation.mul(this).mul(conjugate);
    	
    	return new Vector3f(result.x, result.y, result.z);
    }
    
    /**
     * Adds this vector and another
     * @param other the other
     * @return the vector of their addition
     */
    public Vector3f add(Vector3f other)
    {
        return (new Vector3f(x + other.x, y + other.y, z + other.z));
    }
    
    /**
     * Adds a scalar to this vector
     * @param scalar the scalar
     * @return the vector of the addition
     */
    public Vector3f add(float scalar)
    {
        return (new Vector3f(x + scalar, y + scalar, z + scalar));
    }
    
    /**
     * Subtracts this vector and another
     * @param other the other
     * @return the vector of their subtracts
     */
    public Vector3f sub(Vector3f other)
    {
        return (new Vector3f(x - other.x, y - other.y, z - other.z));
    }
    
    /**
     * Subtracts a scalar to this vector
     * @param scalar the scalar
     * @return the vector of the subtraction
     */
    public Vector3f sub(float scalar)
    {
        return (new Vector3f(x - scalar, y - scalar, z - scalar));
    }
    
    /**
     * Multiplies this vector and another
     * @param other the other
     * @return the vector of their multiplies
     */
    public Vector3f mul(Vector3f other)
    {
        return (new Vector3f(x * other.x, y * other.y, z * other.z));
    }
    
    /**
     * Multiplies a scalar to this vector
     * @param scalar the scalar
     * @return the vector of the multiplication
     */
    public Vector3f mul(float scalar)
    {
        return (new Vector3f(x * scalar, y * scalar, z * scalar));
    }
    
    /**
     * Multiplies this vector by a matrix
     * @param mat the matrix
     * @return the modified vector
     */
    public Vector3f mul(Matrix4f mat)
    {
    	float[] [] matrix = mat.matrix;
    	float x_ = matrix[0] [0] * x + matrix[0] [1] * y + matrix[0] [2] * z + matrix[0] [3];
    	float y_ = matrix[1] [0] * x + matrix[1] [1] * y + matrix[1] [2] * z + matrix[1] [3];
    	float z_ = matrix[2] [0] * x + matrix[2] [1] * y + matrix[2] [2] * z + matrix[2] [3];
    	return new Vector3f(x_, y_, z_);
    }
    
    /**
     * Divides this vector and another
     * @param other the other
     * @return the vector of their division
     */
    public Vector3f div(Vector3f other)
    {
        return (new Vector3f(x / other.x, y / other.y, z / other.z));
    }
    
    /**
     * Divides a scalar to this vector
     * @param scalar the scalar
     * @return the vector of the divides
     */
    public Vector3f div(float scalar)
    {
        float invertS = 1 / scalar;
        
        return (new Vector3f(x * invertS, y * invertS, z * invertS));
    }
    
}
