package net.engine.model;

import java.util.HashMap;

import net.engine.matrix.MatrixStack;
import net.engine.render.Material;
import net.engine.transform.Orientation;

/**
 * It is an instance of a model in the world
 * @author Davis
 *
 */
public abstract class Model
{
	
	public HashMap<String, Integer> flags; //Flags that can be set to determine properties
	
	public Orientation modelMatrix; //The model matrix
	
	public Material material; //The material this texture uses
	
	/**
	 * Creates a new model
	 * @param material the material this model is rendered with
	 */
	public Model(Material material)
	{
		this(new HashMap<String, Integer>(), new Orientation(), material);
	}
	
	/**
	 * Creates a new model
	 * @param flags the model's properties
	 * @param modelMatrix the modelMatrix to start with
	 * @param material the material this model is rendered with
	 */
	public Model(HashMap<String, Integer> flags, Orientation modelMatrix, Material material)
	{
		this.flags = flags;
		this.modelMatrix = modelMatrix;
		this.material = material;
	}
	
	/**
	 * Renders the file
	 * @param stack the matrix stack
	 */
	public abstract void render(MatrixStack stack);
	
}
