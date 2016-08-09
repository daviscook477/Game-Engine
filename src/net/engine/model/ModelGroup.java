package net.engine.model;

import java.util.ArrayList;

import net.engine.matrix.MatrixStack;
import net.engine.render.Material;
import net.engine.transform.Orientation;

/**
 * A group of models that all share a modelMatrix (this is done for efficiency purposes)
 * @author Davis
 *
 */
public class ModelGroup extends Model
{

	public ArrayList<Model> models;
	
	/**
	 * Creates a new model group
	 * @param material the material
	 */
	public ModelGroup(Material material)
	{
		this(new ArrayList<Model>(), new Orientation(), material);
	}
	
	/**
	 * Creates a model group with starting models and a non default orientation
	 * @param models the models
	 * @param modelMatrix the model matrix
	 * @param material the material
	 */
	public ModelGroup(ArrayList<Model> models, Orientation modelMatrix, Material material)
	{
		super(material);
		this.models = models;
		this.modelMatrix = modelMatrix;
	}

	@Override
	public void render(MatrixStack stack)
	{
		stack.pushMatrix(modelMatrix.getTransformationMatrix());
		for (int i = 0; i < models.size(); i++)
		{
			models.get(i).render(stack);
		}
		stack.popMatrix();
	}
	
}
