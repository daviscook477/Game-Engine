package net.engine.matrix;

/**
 * A matrix stack used for positioning objects in openGL
 * @author Davis Cook
 *
 */
public class MatrixStack
{
    
    private static Matrix4f ignoreTranslation(Matrix4f matrix)
    {
        Matrix4f toReturn = matrix;
        
        matrix.matrix[0] [3] = 0;
        matrix.matrix[1] [3] = 0;
        matrix.matrix[2] [3] = 0;
        matrix.matrix[3] [3] = 1;
        matrix.matrix[3] [0] = 0;
        matrix.matrix[3] [1] = 0;
        matrix.matrix[3] [2] = 0;
        
        return toReturn;
    }
    
    private Matrix4f[] stack;
    
    private int topOfStack = -1; //The top of the stack

    /**
     * Creates a new matrix stack
     * @param maxSize the maximum amount of matrices that can be on the stack at one time
     */
    public MatrixStack(int maxSize)
    {
        stack = new Matrix4f[maxSize];
    }
    
    /**
     * Pushes a matrix onto the stack
     * @param toPush the matrix to push
     */
    public void pushMatrix(Matrix4f toPush)
    {
    	topOfStack++;
        stack[topOfStack] = toPush;
    }
    
    /**
     * Pops the top matrix off the stack
     * @return the matrix popped off
     */
    public Matrix4f popMatrix()
    {
        Matrix4f toReturn = stack[topOfStack]; //Get the matrix
        stack[topOfStack] = null; //Remove the matrix
        topOfStack--;
        
        return toReturn;
    }
    
    /**
     * Gets the transformation to apply
     * @return the transformation, null if there are no matrices in the stack
     */
    public Matrix4f getTransform()
    {
        if (topOfStack < 0)
        {
            return null;
        }
        
        Matrix4f transform = stack[0].clone();
        
        loop:
        for (int i = 1; i < stack.length; i++)
        {
        	if (stack[i] == null)
        	{
        		break loop;
        	}
            transform = transform.mul(stack[i]);
        }
        
        return transform;
    }
    
    /**
     * Gets the rotation to apply
     * @return the rotation
     */
    public Matrix4f getRotation()
    {
        if (topOfStack < 0)
        {
            return null;
        }
        
        Matrix4f rotate = ignoreTranslation(stack[0].clone());
        
        loop:
        for (int i = 1; i < stack.length; i++)
        {
        	if (stack[i] == null)
        	{
        		break loop;
        	}
            rotate.mul(ignoreTranslation(stack[i].clone()));
        }
        
        return rotate;
    }
    
}
