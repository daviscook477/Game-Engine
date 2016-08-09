package net.engine.matrix;

import net.engine.math.Vector3f;


/**
 * A 4x4 matrix
 * @author Davis
 */
public class Matrix4f implements Cloneable
{
    
    public static final int SIZE = 4;
    
    public float[] [] matrix;
    
    /**
     * Creates a new matrix
     */
    public Matrix4f()
    {
        matrix = new float[SIZE] [SIZE];
    }
    
    /**
     * Initializes the matrix as the identity matrix
     */
    public void initIdentity()
    {
        matrix[0] [0] = 1; matrix[0] [1] = 0; matrix[0] [2] = 0; matrix[0] [3] = 0;
        matrix[1] [0] = 0; matrix[1] [1] = 1; matrix[1] [2] = 0; matrix[1] [3] = 0;
        matrix[2] [0] = 0; matrix[2] [1] = 0; matrix[2] [2] = 1; matrix[2] [3] = 0;
        matrix[3] [0] = 0; matrix[3] [1] = 0; matrix[3] [2] = 0; matrix[3] [3] = 1;
    }
    
    /**
     * Initializes the matrix as a translation
     * @param x the x coord of the translate vector
     * @param y the y coord of the translate vector
     * @param z the z coord of the translate vector
     */
    public void initTranslation(float x, float y, float z)
    {
    	matrix[0] [0] = 1; matrix[0] [1] = 0; matrix[0] [2] = 0; matrix[0] [3] = x;
        matrix[1] [0] = 0; matrix[1] [1] = 1; matrix[1] [2] = 0; matrix[1] [3] = y;
        matrix[2] [0] = 0; matrix[2] [1] = 0; matrix[2] [2] = 1; matrix[2] [3] = z;
        matrix[3] [0] = 0; matrix[3] [1] = 0; matrix[3] [2] = 0; matrix[3] [3] = 1;
    }
    
    /**
     * Initializes the matrix as a rotation
     * @param x the x coord of the rotate vector
     * @param y the y coord of the rotate vector
     * @param z the z coord of the rotate vector
     */
    public void initRotation(float x, float y, float z)
    {
    	Matrix4f rx = new Matrix4f();
    	Matrix4f ry = new Matrix4f();
    	Matrix4f rz = new Matrix4f();
    	
    	rx.matrix[0] [0] = 1; rx.matrix[0] [1] = 0; 				  rx.matrix[0] [2] = 0; 				   rx.matrix[0] [3] = 0;
    	rx.matrix[1] [0] = 0; rx.matrix[1] [1] = (float) Math.cos(x); rx.matrix[1] [2] = (float) -Math.sin(x); rx.matrix[1] [3] = 0;
    	rx.matrix[2] [0] = 0; rx.matrix[2] [1] = (float) Math.sin(x); rx.matrix[2] [2] = (float) Math.cos(x);  rx.matrix[2] [3] = 0;
    	rx.matrix[3] [0] = 0; rx.matrix[3] [1] = 0; 				  rx.matrix[3] [2] = 0; 				   rx.matrix[3] [3] = 1;
    	
    	ry.matrix[0] [0] = (float) Math.cos(y); ry.matrix[0] [1] = 0; ry.matrix[0] [2] = (float) -Math.sin(y); ry.matrix[0] [3] = 0;
    	ry.matrix[1] [0] = 0; 					ry.matrix[1] [1] = 1; ry.matrix[1] [2] = 0; 				   ry.matrix[1] [3] = 0;
    	ry.matrix[2] [0] = (float) Math.sin(y); ry.matrix[2] [1] = 0; ry.matrix[2] [2] = (float) Math.cos(y);  ry.matrix[2] [3] = 0;
    	ry.matrix[3] [0] = 0; 					ry.matrix[3] [1] = 0; ry.matrix[3] [2] = 0; 				   ry.matrix[3] [3] = 1;
    	
    	rz.matrix[0] [0] = (float) Math.cos(z); rz.matrix[0] [1] = (float) -Math.sin(z); rz.matrix[0] [2] = 0; rz.matrix[0] [3] = 0;
    	rz.matrix[1] [0] = (float) Math.sin(z); rz.matrix[1] [1] = (float) Math.cos(z);  rz.matrix[1] [2] = 0; rz.matrix[1] [3] = 0;
    	rz.matrix[2] [0] = 0; 					rz.matrix[2] [1] = 0; 					 rz.matrix[2] [2] = 1; rz.matrix[2] [3] = 0;
    	rz.matrix[3] [0] = 0; 					rz.matrix[3] [1] = 0; 					 rz.matrix[3] [2] = 0; rz.matrix[3] [3] = 1;
    	
    	matrix = rz.mul(ry.mul(rx)).matrix;
    }
    
    /**
     * Initializes the matrix as a scaling
     * @param x the x coord of the scale vector
     * @param y the y coord of the scale vector
     * @param z the z coord of the scale vector
     */
    public void initScale(float x, float y, float z)
    {
    	matrix[0] [0] = x; matrix[0] [1] = 0; matrix[0] [2] = 0; matrix[0] [3] = 0;
        matrix[1] [0] = 0; matrix[1] [1] = y; matrix[1] [2] = 0; matrix[1] [3] = 0;
        matrix[2] [0] = 0; matrix[2] [1] = 0; matrix[2] [2] = z; matrix[2] [3] = 0;
        matrix[3] [0] = 0; matrix[3] [1] = 0; matrix[3] [2] = 0; matrix[3] [3] = 1;
    }
    
    /**
     * Initializes the matrix for projection
     * @param fov the field of view
     * @param width the width
     * @param height the height
     * @param zNear the near clipping
     * @param zFar the far clipping
     */
    public void initProjection(float fov, float width, float height, float zNear, float zFar)
    {
    	float tanHalfFOV = (float) Math.tan(fov / 2);
    	float aspectRatio = width / height;
    	float zRange = zNear - zFar;
    	
    	matrix[0] [0] = 1 / (tanHalfFOV * aspectRatio); matrix[0] [1] = 0;				matrix[0] [2] = 0; 						  matrix[0] [3] = 0;
        matrix[1] [0] = 0; 								matrix[1] [1] = 1 / tanHalfFOV; matrix[1] [2] = 0; 						  matrix[1] [3] = 0;
        matrix[2] [0] = 0; 								matrix[2] [1] = 0; 				matrix[2] [2] = (-zNear - zFar) / zRange; matrix[2] [3] = 2 * zFar * zNear / zRange;
        matrix[3] [0] = 0; 								matrix[3] [1] = 0; 				matrix[3] [2] = 1; 						  matrix[3] [3] = 0;
    }
    
    /**
     * Creates an orthogonal projection
     * @param width the width
     * @param height the height
     * @param zNear the near clipping
     * @param zFar the far clipping
     */
    /*public void initOrthographic(float right, float left, float top, float bottom, float zNear, float zFar)
    {
    	matrix[0] [0] = -2 / (right - left); matrix[0] [1] = 0; 					 matrix[0] [2] = 0; 					matrix[0] [3] = 0;
        matrix[1] [0] = 0; 		   		    matrix[1] [1] = -2 / (top - bottom); matrix[1] [2] = 0; 					matrix[1] [3] = 0;
        matrix[2] [0] = 0; 		   			matrix[2] [1] = 0; 					 matrix[2] [2] = 2 / (zFar - zNear); 	matrix[2] [3] = 0;
        matrix[3] [0] = 0; 								matrix[3] [1] = 0; 				matrix[3] [2] = 0; 						  matrix[3] [3] = 1;
    }*/
    
    /**
     * Initializes the matrix for camera changes
     * @param forward the forward direction
     * @param up the up direction
     */
    public void initCamera(Vector3f forward, Vector3f up)
    {
    	Vector3f f = forward;
    	f = f.normal();
    	
    	Vector3f r = up;
    	r = r.normal();
    	r = r.cross(f);
    	
    	Vector3f u = f.cross(r);
    	
    	matrix[0] [0] = r.x; matrix[0] [1] = r.y; matrix[0] [2] = r.z; matrix[0] [3] = 0;
        matrix[1] [0] = u.x; matrix[1] [1] = u.y; matrix[1] [2] = u.z; matrix[1] [3] = 0;
        matrix[2] [0] = f.x; matrix[2] [1] = f.y; matrix[2] [2] = f.z; matrix[2] [3] = 0;
        matrix[3] [0] = 0; matrix[3] [1] = 0; matrix[3] [2] = 0; matrix[3] [3] = 1;
    }
    
    @Override
    public String toString()
    {
    	String res = "";
    	for (int i = 0; i < matrix.length; i++)
    	{
    		for (int j = 0; j < matrix[i].length; j++)
    		{
    			res += matrix[i] [j];
    		}
    		
    		res += '\n';
    	}
    	
    	return res;
    }
    
    @Override
    public Matrix4f clone()
    {
    	Matrix4f toReturn = new Matrix4f();
    	for (int i = 0; i < matrix.length; i++)
    	{
    		for (int j = 0; j < matrix[i].length; j++)
    		{
    			toReturn.matrix[i] [j] = matrix[i] [j];
    		}
    	}
    	return toReturn;
    }
    
    /**
     * Multiples this matrix with another
     * @param other the other
     * @return the resulting matrix
     */
    public Matrix4f mul(Matrix4f other)
    {
        Matrix4f result = new Matrix4f();
        
        for (int i = 0; i < SIZE; i++)
        {
            for (int j = 0; j < SIZE; j++)
            {
                result.matrix[i] [j] = matrix[i] [0] * other.matrix[0] [j] +
                                       matrix[i] [1] * other.matrix[1] [j] +
                                       matrix[i] [2] * other.matrix[2] [j] +
                                       matrix[i] [3] * other.matrix[3] [j];
            }
        }
        
        return result;
    }
    
}
