package net.engine.transform;

import net.engine.core.Window;
import net.engine.matrix.Matrix4f;
import net.engine.util.Camera;

/**
 * A transformation that implements projection transformation
 * @author Davis
 *
 */
public class ProjectedOrientation extends Orientation
{

	private float zNear;
	private float zFar;
	private float width;
	private float height;
	private float fov;
	
	/**
	 * Creates a new projected transformation
	 * @param fov the fov
	 * @param zNear the znear
	 * @param zFar the zfar
	 */
	public ProjectedOrientation(float fov, float zNear, float zFar)
	{
		super();
		
		this.fov = fov;
		this.width = Window.getWidth();
		this.height = Window.getHeight();
		this.zNear = zNear;
		this.zFar = zFar;
	}
	
	/**
	 * Creates a new projected transformation
	 * @param fov the fov
	 * @param width the width
	 * @param height the height
	 * @param zNear the znear
	 * @param zFar the zfar
	 */
	public ProjectedOrientation(float fov, float width, float height, float zNear, float zFar)
	{
		super();
		
		this.fov = fov;
		this.width = width;
		this.height = height;
		this.zNear = zNear;
		this.zFar = zFar;
	}
	
	/**
	 * Gets the translation matrix for projection perspective
	 * @return the translation matrix for projection perspective
	 */
	public Matrix4f getProjectedTransformationMatrix()
	{
		Matrix4f transformationMatrix = getTransformationMatrix();
		Matrix4f projectionMatrix = new Matrix4f(); projectionMatrix.initProjection(fov, width, height, zNear, zFar);
		
		return projectionMatrix.mul(transformationMatrix);
	}
	
	/**
	 * Gets the translation matrix for project perspective from a camera
	 * @param camera the camera looking from
	 * @return the translation matrix
	 */
	public Matrix4f getCameraProjectedTransformationMatrix(Camera camera)
	{
		Matrix4f transformationMatrix = getTransformationMatrix();
		Matrix4f projectionMatrix = new Matrix4f(); projectionMatrix.initProjection(fov, width, height, zNear, zFar);
		Matrix4f cameraRotationMatrix = new Matrix4f(); cameraRotationMatrix.initCamera(camera.getForward(), camera.getUp());
		Matrix4f cameraTranslation = new Matrix4f(); cameraTranslation.initTranslation(-camera.pos.x, -camera.pos.y, -camera.pos.z);
		
		return projectionMatrix.mul(cameraRotationMatrix.mul(cameraTranslation.mul(transformationMatrix)));
	}
	
}
