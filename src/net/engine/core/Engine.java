package net.engine.core;

import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_CW;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glFrontFace;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER_SRGB;

import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.opengl.Display;

import net.engine.gui.GUIManager;
import net.engine.gui.GUIWindow;
import net.engine.input.Input;
import net.engine.matrix.MatrixStack;
import net.engine.model.Model;
import net.engine.render.Material;
import net.engine.render.Sampler;
import net.engine.render.Shader;
import net.engine.transform.ProjectedOrientation;
import net.engine.util.Camera;

/**
 * A game engine
 * @author Davis Cook
 *
 */
public class Engine
{
	
	//Define the names of shader variables
	public static final String PROJECTED_CAMERA_MATRIX = "projectedCameraMatrix"; //The matrix that does projection and camera movement
	public static final String MODEL_MATRIX = "modelMatrix"; //The matrix that positions the model in the world
	public static final String NORMAL_MATRIX = "normalMatrix"; //The matrix that corrects the normal's directions
	public static final String SAMPLER = "sampler"; //The sampler for textures
	public static final String COLOR = "color"; //The color for textures
	
	public static final int TEXTURE_LOCATION = 1; //The location where textures are bound
	public static final int TEXTURE_LOCATION_OPENGL = GL_TEXTURE0 + TEXTURE_LOCATION; //The location where textures are bound defined in another way
	
	public static final int MATRIX_STACK_SIZE = 15; //The amount of matrices that can be on the matrix stack at max
	
	public static final String DEFAULT_TITLE = "Game Engine"; //The default title
	
	//Resource locations
	public static final String DEFAULT_RESOURCE_LOCATION = "./res/"; //The default resources folder
	public static final String DEFAULT_SHADER_LOCATION = "shaders/"; //The default shader folder
	public static final String DEFAULT_TEXTURE_LOCATION = "textures/"; //The default texture folder
	public static final String DEFAULT_MODEL_LOCATION = "models/"; //The default model folder
	
	public static final float MOVE_SPEED = 10f; //Camera move speed
	public static final float TURN_SPEED = 100f; //Camera turn speed
	
	public static final int DESIRED_FPS = 640; //The desired FPS
	
	private static Engine instance = null; //The single instance of the engine
	
	/**
	 * Gets the instance, null if it hasn't been created
	 * @return the instance
	 */
	public static Engine getInstance()
	{
		if (instance == null)
		{
			instance = new Engine();
		}
		return instance;
	}
	
	private Game game = null; //The game

	//Engine variables
	private HashMap<String, Shader> shaders; //List of shader
	private Shader currentShader; //Current in use shader
	
	private ArrayList<Model> models; //All the models the engine is responsible for rendering
	
	private HashMap<String, Sampler> samplers; //List of sampler
	
	private HashMap<String, Material> materials; //List of material
	private Material currentMaterial; //Current in use material
	
	private MatrixStack stack; //The matrix stack
	
	private Camera camera; //The camera
	
	private ProjectedOrientation worldOrientation; //The world orientation
	
	private String resourceLocation; //The base resource folder
	
	private String shaderLocation, textureLocation, modelLocation; //The resource locations
	
	private Shader guiShader; //A special shader for the gui
	
	private GUIManager guiManager; //The GUI manager
	
	//Engine control loop variables
	private boolean isRunning;
	
	/**
	 * Initializes the engine
	 */
	private Engine()
	{
		//Initialization
		Window.createWindow(1280, 720); //Create the window & initialize openGL
	    Input.createInput(); //Initialize the inputs
	    
	    isRunning = false;
		
		if (Window.getVersion() < 33) //Check for compatibility
		{
			System.err.println("At least openGL 3.3 required");
		}
		
		//Set default values
		setTitle(DEFAULT_TITLE);
		setResourceLocation(DEFAULT_RESOURCE_LOCATION);
		setShaderLocation(DEFAULT_SHADER_LOCATION);
		setTextureLocation(DEFAULT_TEXTURE_LOCATION);
		setModelLocation(DEFAULT_MODEL_LOCATION);
		
		//Shader
		shaders = new HashMap<String, Shader>();
		currentShader = null;
		
		//Gui Shader
		guiShader = new Shader(GUIManager.GUI_VERTEX_SHADER, GUIManager.GUI_FRAGMENT_SHADER, null);

		//Model
		models = new ArrayList<Model>();
		
		//Sampler
		samplers = new HashMap<String, Sampler>();
		
		//Material
		materials = new HashMap<String, Material>();
		
		//Matrix Stack
		stack = new MatrixStack(MATRIX_STACK_SIZE);
		
		//TODO: variables initialization
		
		//Camera
		camera = new Camera(MOVE_SPEED, TURN_SPEED);
		
		//Projection and Camera orientation
		worldOrientation = new ProjectedOrientation(70, .005f, 1000);
		
		//GUI
		guiManager = new GUIManager();
		
		//Graphics settings
		glClearColor(1.0f, 1.0f, 1.0f, 0.0f); //Set background color
		        
		glFrontFace(GL_CW); //The front face is defined in clockwise
		glCullFace(GL_BACK); //Cull the back face
		glEnable(GL_DEPTH_TEST); //Enable it such that pixels can be drawn in depth order
		        
		//TODO: Depth clamp for later
		        
		glEnable(GL_TEXTURE_2D); //Use textures
		glEnable(GL_FRAMEBUFFER_SRGB); //Gamma correction for things appearing darker than they are
	}
	
	/**
	 * Runs the engine with the specified game
	 * @param theGame the game to run
	 */
	public void start(Game theGame)
	{
		game = theGame;
		
		if (isRunning) //The game should not be running before run() is called
		{
			return;
		}
		
		run(); //Run the game
	}
	
	/**
	 * Stops the game
	 */
	public void stop()
	{
		if (!isRunning) //The game cannot be stopped when it isn't running
		{
			return;
		}
		
		isRunning = false; //Stop running
	}
	
	/**
	 * Runs the main game loop
	 */
	private void run()
	{
		isRunning = true; //Set the game to be running
		
		//This is for an fps count
		int frames = 0;
		long frameCounter = 0;
		
		//This determines how long a frame is
		final double frameTime = 1f / DESIRED_FPS;
		
		long lastTime = Time.getTime();
		double unprocessedTime = 0;
		
		while (isRunning)
		{
			boolean render = false; //Should the game render a frame
			
			long startTime = Time.getTime();
			long passedTime = startTime - lastTime;
			lastTime = startTime; //Calculate how much time passed
			
			//Add that time to the amount of time we check for frames
			unprocessedTime += passedTime / (double) Time.SECOND;
			frameCounter += passedTime;
			
			//Enough time has happened for a frame
			while (unprocessedTime > frameTime)
			{
				render = true; //Do render this frame
				
				unprocessedTime -= frameTime; //Reset the counter
				
				Time.setDelta(frameTime); //Set the delta as to much time has passed
				
				if (Window.isCloseRequested()) //If the window has the 'x' clicked
				{
					stop(); //Stop
				}
				
				Input.update(); //Update the input
				
				game.input(); //Allow the game to take input
				game.update(); //Allow the game to update its state
				
				frames++;
				
				//FPS counter stuff
				if (frameCounter >= Time.SECOND)
				{
					System.out.println(frames);
					frameCounter = 0;
					frames = 0;
				}
			}
			if (render)
			{
				render(); //Renders everything
			}
			else //So there wasn't enough time built up for a frame
			{
				try
				{
					Thread.sleep(1); //Wait to try to build up for a frame
				}
				catch (InterruptedException e)
				{
					
				}
			}
		}
		
		cleanUp(); //The game has stopped and now needs to clean-up resources and such
	}
	
	/**
	 * Cleans up resources and stuff
	 */
	private void cleanUp()
	{
		if (isRunning) //You cannot clean up while the game is running
		{
			return;
		}
		
		Window.dispose(); //Dispose of the main window
		Input.dispose(); //Dispose of the input
	}
	
	/**
	 * Renders all the models the engine has stored
	 * 
	 * The models are rendered according to their flags they have set
	 * 
	 * Flags:
	 * render, if not zero the model will be rendered else it will not be
	 * 
	 */
	private void render()
	{
		game.render(); //Tell the game to render
		
		guiShader.bind(); //Use the gui shader
		
		guiManager.render(); //Render the gui
		
		Window.render(); //Have the window render what we just rendered
	}
	
	//Setters for engine properties
	
	/**
	 * Sets the title
	 * @param title the title
	 */
	public void setTitle(String title)
	{
		Display.setTitle(title);
	}
	
	/**
	 * Sets the folder where resources are stored
	 * @param resourceLocation the folder where resources are stored
	 */
	public void setResourceLocation(String resourceLocation)
	{
		this.resourceLocation = resourceLocation;
	}
	
	//Getters

	/**
	 * Sets the folder where shaders are found
	 * @param shaderLocation where shaders are found
	 */
	public void setShaderLocation(String shaderLocation)
	{
		this.shaderLocation = shaderLocation;
	}

	/**
	 * Sets the folder where textures are found
	 * @param textureLocation the location textures are found
	 */
	public void setTextureLocation(String textureLocation)
	{
		this.textureLocation = textureLocation;
	}
	
	/**
	 * Sets the location where models are found
	 * @param modelLocation where models are found
	 */
	public void setModelLocation(String modelLocation)
	{
		this.modelLocation = modelLocation;
	}

	/**
	 * Gets the current shader
	 * @return the current shader
	 */
	public Shader getCurrentShader()
	{
		return currentShader;
	}
	
	/**
	 * Gets the matrix stack
	 * @return the matrix stack
	 */
	public MatrixStack getMatrixStack()
	{
		return stack;
	}
	
	//Shader methods
	
	/**
	 * Adds a shader to the engine
	 * @param s the shader
	 * @param name the name to call the shader
	 */
	public void addShader(Shader s, String name)
	{
		shaders.put(name, s);
	}
	
	/**
	 * Uses a shader
	 * @param name the shader to use
	 */
	public void useShader(String name)
	{
		Shader s = shaders.get(name);
		if (s != null)
		{
			s.bind();
			currentShader = s;
		}
		else
		{
			glUseProgram(0); //Stop using shaders
		}
	}
	
	//Sampler methods
	
	/**
	 * Adds a sampler to the engine
	 * @param sampler the sampler to add
	 * @param the name to call the sampler
	 */
	public void addSampler(Sampler s, String name)
	{
		samplers.put(name, s);
	}
	
	/**
	 * Uses a sampler
	 * @param samplerName the sampler
	 */
	public void useSampler(String samplerName)
	{
		Sampler s = samplers.get(samplerName);
		s.bind(TEXTURE_LOCATION);
	}
	
	//Material methods
	
	/**
	 * Adds a material to the list
	 * @param m the material to add
	 * @param name the name to call it
	 */
	public void addMaterial(Material m, String name)
	{
		materials.put(name, m);
	}
	
	/**
	 * Uses the material by binding
	 * @param name the name the material is called
	 */
	public void useMaterial(String name)
	{
		Material m = materials.get(name);
		m.getTexture().bind(TEXTURE_LOCATION_OPENGL);
		currentMaterial = m;
	}
	
	/**
	 * Gets the material
	 * @param name the material's name
	 * @return the material
	 */
	public Material getMaterial(String name)
	{
		return materials.get(name);
	}
	
	//Model methods
	
	/**
	 * Adds a model
	 * @param m the model to add
	 */
	public void addModel(Model m)
	{
		models.add(m);
	}
	
	//Camera methods
	
	/**
	 * Gets the camera
	 * @return the camera
	 */
	public Camera getCamera()
	{
		return camera;
	}
	
	//Resource Location Methods
	
	/**
	 * Gets where resources are stored
	 * @return where resources are stored
	 */
	public String getResourceLocation()
	{
		return resourceLocation;
	}
	
	/**
	 * Gets where shader are stored
	 * @return where shaders are stored
	 */
	public String getShaderLocation()
	{
		return resourceLocation + shaderLocation;
	}
	
	/**
	 * Gets where textures are stored
	 * @return where textures are stored
	 */
	public String getTextureLocation()
	{
		return resourceLocation + textureLocation;
	}
	
	/**
	 * Gets where models are stored
	 * @return where models are stored
	 */
	public String getModelLocation()
	{
		return resourceLocation + modelLocation;
	}
	
	//Gui methods
	
	/**
	 * Adds a new gui
	 * @param window the gui to add
	 * @param x the x
	 * @param y the y
	 */
	public void addGUI(GUIWindow window, float x, float y)
	{
		guiManager.addGUI(window, x, y);
	}
	
	/**
	 * Sets the shader's model matrix data
	 */
	public void setModelData()
	{
		if (currentShader.getUniformNames().contains(MODEL_MATRIX)) //If the shader uses textures set the texture location and the color
		{
			currentShader.setUniform(MODEL_MATRIX, stack.getTransform());
		}
		if (currentShader.getUniformNames().contains(NORMAL_MATRIX))
		{
			currentShader.setUniform(NORMAL_MATRIX, stack.getRotation());
		}
	}
	
	/**
	 * Renders all the models the engine has stored
	 * 
	 * The models are rendered according to their flags they have set
	 * 
	 * Flags:
	 * render, if not zero the model will be rendered else it will not be
	 * 
	 */
	public void renderModels()
	{
		currentShader.setUniform(PROJECTED_CAMERA_MATRIX, worldOrientation.getCameraProjectedTransformationMatrix(camera)); //Set up the projection and camera matrix
		if (currentShader.getUniformNames().contains(SAMPLER)) //If the shader uses textures set the texture location and the color
		{
			currentShader.setUniformi(SAMPLER, TEXTURE_LOCATION);
		}
		if (currentShader.getUniformNames().contains(COLOR))
		{
			currentShader.setUniform(COLOR, currentMaterial.getColor());
		}
		
		for (Model m : models) //Render each model
		{
			m.render(stack);
		}
	}
	
}
