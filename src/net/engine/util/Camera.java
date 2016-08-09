package net.engine.util;

import net.engine.core.Time;
import net.engine.math.Vector3f;

/**
 * The location the world is looked at from
 * @author Davis
 *
 */
public class Camera
{
	
	public static final float DEFAULT_MOVE_SPEED = 10f;
	public static final float DEFAULT_TURN_SPEED = 100f;
	
	public static final Vector3f Y_AXIS = new Vector3f(0, 1, 0);

	public Vector3f pos;
	public Vector3f forward;
	public Vector3f up;
	
	private Vector3f posInit;
	private Vector3f forwardInit;
	private Vector3f upInit;
	
	public float moveSpeed;
	public float turnSpeed;
	
	/**
	 * Creates a camera with standard up, forward, and move speeds
	 */
	public Camera()
	{
		this(new Vector3f(0, 0, 0), new Vector3f(0, 0, 1), new Vector3f(0, 1, 0), DEFAULT_MOVE_SPEED, DEFAULT_TURN_SPEED);
	}
	
	/**
	 * Creates a camera with standard up, forward, and specified move speeds
	 * @param moveSpeed
	 * @param turnSpeed
	 */
	public Camera(float moveSpeed, float turnSpeed)
	{
		this(new Vector3f(0, 0, 0), new Vector3f(0, 0, 1), new Vector3f(0, 1, 0), moveSpeed, turnSpeed);
	}
	
	/**
	 * Creates a camera
	 * @param pos the location of the camera
	 * @param forward the forward
	 * @param up the up
	 * @param moveSpeed the speed at which the camera moves
	 */
	public Camera(Vector3f pos, Vector3f forward, Vector3f up, float moveSpeed, float turnSpeed)
	{
		this.pos = pos;
		this.forward = forward;
		this.up = up;
		this.moveSpeed = moveSpeed;
		this.turnSpeed = turnSpeed;
		
		up = up.normal();
		forward = forward.normal();
		
		this.posInit = pos;
		this.forwardInit = forward;
		this.upInit = up;
	}
	
	/**
	 * Moves the camera in a direction an amount
	 * @param direction the direction
	 * @param amount the amount
	 */
	public void move(Vector3f direction, float amount)
	{
		pos = pos.add(direction.normal().mul(amount));
	}
	
	/**
	 * Moves forward
	 */
	public void moveForward()
	{
		move(forward, (float) (moveSpeed * Time.getDelta()));
	}
	
	/**
	 * Moves backward
	 */
	public void moveBackward()
	{
		move(forward.mul(-1), (float) (moveSpeed * Time.getDelta()));
	}
	/**
	 * Moves left
	 */
	public void moveLeft()
	{
		move(getLeft(), (float) (moveSpeed * Time.getDelta()));
	}
	
	/**
	 * Moves right
	 */
	public void moveRight()
	{
		move(getRight(), (float) (moveSpeed * Time.getDelta()));
	}
	
	/**
	 * Turns up
	 */
	public void turnUp()
	{
		rotateX((float) Math.toRadians(-turnSpeed * Time.getDelta()));
	}
	
	/**
	 * Turns down
	 */
	public void turnDown()
	{
		rotateX((float) Math.toRadians(turnSpeed * Time.getDelta()));
	}
	
	/**
	 * Turns left
	 */
	public void turnLeft()
	{
		rotateY((float) Math.toRadians(-turnSpeed * Time.getDelta()));
	}
	
	/**
	 * Turns right
	 */
	public void turnRight()
	{
		rotateY((float) Math.toRadians(turnSpeed * Time.getDelta()));
	}
	
	/**
	 * Resets the camera's location to where it was created
	 */
	public void reset()
	{
		pos = posInit;
		forward = forwardInit;
		up = upInit;
	}
	
	/**
	 * Rotates along the yx plane;
	 * @param angle the angle to rotate
	 */
	public void rotateY(float angle)
	{
		Vector3f Haxis = Y_AXIS.cross(forward).normal();
		
		forward = forward.rotate(angle, Y_AXIS).normal();
		
		up = forward.cross(Haxis).normal();
	}
	/**
	 * Rotates along the xz plane
	 * @param angle the angle to rotate
	 */
	public void rotateX(float angle)
	{
		Vector3f Haxis = Y_AXIS.cross(forward).normal();
		
		forward = forward.rotate(angle, Haxis).normal();
		
		up = forward.cross(Haxis).normal();
	}
	
	/**
	 * Gets which way is left
	 * @return left
	 */
	public Vector3f getLeft()
	{
		Vector3f left = forward.cross(up);
		left = left.normal();
		return left;
	}
	
	/**
	 * Gets which way is right
	 * @return right
	 */
	public Vector3f getRight()
	{
		Vector3f right = up.cross(forward);
		right = right.normal();
		return right;
	}
	
	/**
	 * Gets which way is up
	 * @return up
	 */
	public Vector3f getUp()
	{
		return up;
	}
	
	/**
	 * Gets which was is forward
	 * @return forward
	 */
	public Vector3f getForward()
	{
		return forward;
	}
	
	/**
	 * Gets which was is backwards
	 * @return backwards
	 */
	public Vector3f getBackward()
	{
		return new Vector3f(-forward.x, -forward.y, -forward.z);
	}
	
}
