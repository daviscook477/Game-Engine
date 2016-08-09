package net.game;

import static org.lwjgl.opengl.GL11.*;
import net.engine.core.Engine;
import net.engine.core.Time;
import net.engine.core.Window;
import net.engine.gui.GUIWindow;
import net.engine.input.Input;
import net.engine.math.Vector3f;
import net.engine.model.FileModel;
import net.engine.model.ModelGroup;
import net.engine.render.Material;
import net.engine.render.Shader;
import net.engine.render.Texture;
import net.engine.transform.Orientation;
import net.engine.util.Camera;

import org.lwjgl.input.Keyboard;

/**
 * Runs the game logic
 * @author Davis
 *
 */
public class Game implements net.engine.core.Game
{	
	
	public static String OUTLINE_SHADER = "outline";
	public static String CEL_SHADER = "cel";
	
	public static String TEST_MATERIAL = "test";
	
	public static float WIDTH = 0.01f;
	
	private Vector3f outlineColor = new Vector3f(0f, 0f, 0f);
    
    private Vector3f lightDir = new Vector3f(0, 0, -1); //Light position
    
    private boolean moveLight = false; //Move the light automatically
    
    private ModelGroup models1;
    
    private GUIWindow test;

    
	/**
	 * Creates the game
	 */
	public Game()
	{	
        Engine engine = Engine.getInstance();
        
        engine.setTitle("Title");
        
        //TODO: set up resource locations (defaults work for now)
        
        engine.addShader(new Shader(OUTLINE_SHADER), OUTLINE_SHADER);
        engine.addShader(new Shader(CEL_SHADER), CEL_SHADER);
        
        engine.addMaterial(new Material(new Texture("Cool.png"), new Vector3f(1, 1, 1)), TEST_MATERIAL);
	    
	    //Mesh management
	   	models1 = new ModelGroup(engine.getMaterial(TEST_MATERIAL));
	   	
	   	//Load a 2*2 cube of boxes
	    for (int i = -1; i < 1; i++)
	   	{
	   		for (int j = -1; j < 1; j++)
		   	{
	   			for (int k = -1; k < 1; k++)
			   	{
	   				Orientation startPos5 = new Orientation();
	   				startPos5.setTranslation(i * 3f, j * 3f, k * 3f);
	   				models1.models.add(new FileModel("texGoodSphere.obj", startPos5, engine.getMaterial(TEST_MATERIAL)));
			   	}
		   	}
	   	}
	    
	    test = new GUIWindow(Window.getWidth() / 4, Window.getHeight())
	    {

			@Override
			public void initializeProperties() {
				setProperty("amount", "1");
				
			}

			@Override
			public boolean input(float x, float y) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void render(float x, float y, float width, float height) {
				float amount = Float.parseFloat(getProperty("amount"));
				glColor3f(0, 0, 0);
				glBegin(GL_QUADS);
				glVertex3f(x, (height * amount) + y, -.5f);
				glVertex3f(width + x, (height * amount) + y, -.5f);
				glVertex3f(width + x, y, -.5f);
				glVertex3f(x, y, -.5f);
				glEnd();
			}
	    	
	    };
	    
	    engine.addModel(models1);
	    engine.addGUI(test, .75f * Window.getWidth(), 0);
	    engine.addGUI(test, 0, 0);
	}
	
	@Override
	public void input()
	{
		Camera camera = Engine.getInstance().getCamera();
		//Move the camera using wasd for movement and the arrow keys for turning
		if (Input.isKeyDown(Keyboard.KEY_W))
		{
			camera.moveForward();
		}
		if (Input.isKeyDown(Keyboard.KEY_S))
		{
			camera.moveBackward();
		}
		if (Input.isKeyDown(Keyboard.KEY_A))
		{
			camera.moveLeft();
		}
		if (Input.isKeyDown(Keyboard.KEY_D))
		{
			camera.moveRight();
		}
		if (Input.isKeyDown(Keyboard.KEY_UP))
		{
			camera.turnUp();
		}
		if (Input.isKeyDown(Keyboard.KEY_DOWN))
		{
			camera.turnDown();
		}
		if (Input.isKeyDown(Keyboard.KEY_LEFT))
		{
			camera.turnLeft();
		}
		if (Input.isKeyDown(Keyboard.KEY_RIGHT))
		{
			camera.turnRight();
		}
		
		if (Input.keyJustDown(Keyboard.KEY_R)) //Reset button on the camera
		{
			camera.reset();
		}
		
		if (Input.keyJustDown(Keyboard.KEY_L)) //Change lighting direction
		{
			lightDir = camera.getBackward();
		}
		if (Input.keyJustDown(Keyboard.KEY_T)) //Lighting toggle
		{
			if (!moveLight)
			{
	        	moveLight = true;
	        }
	        else
	        {
	        	moveLight = false;
	        }
		}
		
		//TODO: other input
	}
	
	float temp = 0f;
	
	@Override
	public void update()
	{	
		Camera camera = Engine.getInstance().getCamera();
		
		temp += (.000001f / Time.getDelta());
		
		float sin = (float) (Math.abs(Math.sin(temp)));
		
		Orientation change = models1.modelMatrix;
		change.setRotation(temp, -7, 0);
		
		if (moveLight)
		{
			lightDir = camera.getBackward();
		}
		
		test.setProperty("amount", sin + "");
	}
	
	@Override
	public void render()
	{
		Engine engine = Engine.getInstance();
		
		engine.useMaterial(TEST_MATERIAL); //TODO: the engine does not need to be told which material to use
		//It should automatically do this when rendering models
		
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT); //Clear the screen first and foremost
		
		
		//Not wire frame
		glEnable(GL_CULL_FACE); //Enable face culling
    	glPolygonMode(GL_FRONT_AND_BACK, GL_FILL); //Disable wire frames
		
    	engine.useShader(OUTLINE_SHADER);
    	
    	Shader outline = engine.getCurrentShader();
    	outline.setUniformf("width", WIDTH);
    	outline.setUniform("outlineColor", outlineColor);
    	outline.setUniform("loc", engine.getCamera().pos);
		
		//Render the back faces through the first pass shader
		glCullFace(GL_FRONT);
		glEnable(GL_CULL_FACE);

		engine.renderModels();
		
		//Second pass: the black object is overwritten with the actual one
		
		engine.useShader(CEL_SHADER);
		
		Shader cel = engine.getCurrentShader();
    	cel.setUniform("lightDir", lightDir);
		
		//Render the normal faces through the second pass shader
		glCullFace(GL_BACK);
		glEnable(GL_CULL_FACE);
		
		engine.renderModels();		
	}
	
}
