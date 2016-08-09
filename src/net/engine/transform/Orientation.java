package net.engine.transform;

import net.engine.math.Vector3f;
import net.engine.matrix.Matrix4f;
import net.engine.util.Camera;

/**
 * Represents a transformation
 * @author Davis
 *
 */
public class Orientation
{

	private Vector3f translateVector; //The matrix is simply moved by this amount
	private Vector3f rotateVector; //This is the rotation individually on each plane in radians
	private Vector3f scaleVector; //The matrix is multiplied by this amount
	
	/**
	 * Creates a new blank transformation
	 */
	public Orientation()
	{
		translateVector = new Vector3f(0, 0, 0);
		rotateVector = new Vector3f(0, 0, 0);
		scaleVector = new Vector3f(1, 1, 1);
	}
	
	/**
	 * Gets a matrix that when multiplied with a vector will result in this transformation
	 * @return the matrix that when multiplied with a vector will result in this transformation
	 */
	public Matrix4f getTransformationMatrix()
	{
		Matrix4f translation = new Matrix4f(); translation.initTranslation(translateVector.x, translateVector.y, translateVector.z);
		Matrix4f rotation = new Matrix4f(); rotation.initRotation(rotateVector.x, rotateVector.y, rotateVector.z);
		Matrix4f scale = new Matrix4f(); scale.initScale(scaleVector.x, scaleVector.y, scaleVector.z);
		
		return translation.mul(rotation.mul(scale));
	}
	
	/**
	 * Gets a matrix that rotates this orientation
	 * @return the matrix that rotates
	 */
	public Matrix4f getRotationMatrix()
	{
		Matrix4f rotation = new Matrix4f(); rotation.initRotation(rotateVector.x, rotateVector.y, rotateVector.z);
		
		return rotation;
	}
	
	/**
	 * Gets the translation matrix from a camera
	 * @param camera the camera
	 * @return the translation matrix
	 */
	public Matrix4f getCameraTransformationMatrix(Camera camera)
	{
		Matrix4f transformationMatrix = getTransformationMatrix();
		Matrix4f cameraRotationMatrix = new Matrix4f(); cameraRotationMatrix.initCamera(camera.getForward(), camera.getUp());
		Matrix4f cameraTranslation = new Matrix4f(); cameraTranslation.initTranslation(-camera.pos.x, -camera.pos.y, -camera.pos.z);
		
		return cameraRotationMatrix.mul(cameraTranslation.mul(transformationMatrix));
	}
	
	/**
	 * Returns the translation vector
	 * @return the translation vector
	 */
	public Vector3f getTranslationVector()
	{
		return translateVector;
	}
	
	/**
	 * Sets the translation vector
	 * @param translate the vector to set it to
	 */
	public void setTranslation(Vector3f translate)
	{
		translateVector = translate;
	}
	
	/**
	 * Sets the translation vector
	 * @param translate the vector to set it to
	 */
	public void setTranslation(float x, float y, float z)
	{
		translateVector = new Vector3f(x, y, z);
	}
	
	/**
	 * Returns the rotation vector
	 * @return the rotation vector
	 */
	public Vector3f getRotationVector()
	{
		return rotateVector;
	}
	
	/**
	 * Sets the rotation vector
	 * @param rotate the vector to set it to
	 */
	public void setRotation(Vector3f rotate)
	{
		rotateVector = rotate;
	}
	
	/**
	 * Sets the rotation vector
	 * @param rotate the vector to set it to
	 */
	public void setRotation(float x, float y, float z)
	{
		rotateVector = new Vector3f(x, y, z);
	}
	
	/**
	 * Returns the scale vector
	 * @return the scale vector
	 */
	public Vector3f getScaleVector()
	{
		return scaleVector;
	}
	
	/**
	 * Sets the scale vector
	 * @param scale the vector to set it to
	 */
	public void setScale(Vector3f scale)
	{
		scaleVector = scale;
	}
	
	/**
	 * Sets the scale vector
	 * @param scale the vector to set it to
	 */
	public void setScale(float x, float y, float z)
	{
		scaleVector = new Vector3f(x, y, z);
	}
	
}
