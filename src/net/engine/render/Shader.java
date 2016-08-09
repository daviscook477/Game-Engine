package net.engine.render;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL32.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import net.engine.core.Engine;
import net.engine.math.Vector3f;
import net.engine.matrix.Matrix4f;
import net.engine.util.BufferUtil;

/**
 * A class that manages a single shader
 * 
 * Creation:
 * 1) Create an instance
 * 2) compile()
 * 3) addUniform()
 * 
 * Use:
 * 1) setUniform()
 * 2) bind()
 * 
 * @author Davis
 *
 */
public class Shader
{
	
	public static final int VERTEX_LOCATION = 0;
	public static final int TEXTURE_LOCATION = 1;
	public static final int NORMAL_LOCATION = 2;

	private int program; //The pointer to the shader
	
	private HashMap<String, Integer> uniforms; //The uniforms this shader has
	
	private ArrayList<String> uniformNames; //The names of the uniforms
	
	/**
	 * Creates a shader off of already loaded source code
	 * @param vText the vertex shader
	 * @param fText the fragment shader
	 * @param gText the geometry shader
	 */
	public Shader(String vText, String fText, String gText)
	{
		program = glCreateProgram(); //Get a pointer to the shader
		
		if (program == 0) //This probably won't fail
		{
			System.err.println("Shader creation failed: Could not find valid memory location in constructor");
			System.exit(1);
		}
		
		uniforms = new HashMap<String, Integer>(); //Initialize the uniform HashMap
		
		//Create the uniforms
	    uniformNames = new ArrayList<String>();
		
		if (vText != null)
		{
			addVertexShader(vText);
			uniformNames.addAll(getUniformNames(vText));
		}
		if (fText != null)
		{
			addFragmentShader(fText);
			uniformNames.addAll(getUniformNames(fText));
		}
		if (gText != null)
		{
			addGeometryShader(gText);
			uniformNames.addAll(getUniformNames(gText));
		}
	    compileShader(); //Compile the shader
		
		//Create the uniforms
	    for (String s : uniformNames)
	    {
	    	addUniform(s);
	    }
	}
	
	/**
	 * Creates a new shader
	 * @param shaderName the name of the shader where the vertex shader is shaderNameVertex.vs and so on
	 */
	public Shader(String shaderName)
	{
		program = glCreateProgram(); //Get a pointer to the shader
		
		if (program == 0) //This probably won't fail
		{
			System.err.println("Shader creation failed: Could not find valid memory location in constructor");
			System.exit(1);
		}
		
		uniforms = new HashMap<String, Integer>(); //Initialize the uniform HashMap
		
		String vText = loadShader(shaderName + "Vertex.vs");
		String fText = loadShader(shaderName + "Fragment.fs");
		String gText = loadShader(shaderName + "Geometry.gs");
		
		//Create the uniforms
	    uniformNames = new ArrayList<String>();
		
		if (vText != null)
		{
			addVertexShader(vText);
			uniformNames.addAll(getUniformNames(vText));
		}
		if (fText != null)
		{
			addFragmentShader(fText);
			uniformNames.addAll(getUniformNames(fText));
		}
		if (gText != null)
		{
			addGeometryShader(gText);
			uniformNames.addAll(getUniformNames(gText));
		}
	    compileShader(); //Compile the shader
		
		//Create the uniforms
	    for (String s : uniformNames)
	    {
	    	addUniform(s);
	    }
	}
	
	/**
	 * Gets the uniform names
	 * @return the uniform names
	 */
	public ArrayList<String> getUniformNames()
	{
		return uniformNames;
	}
	
	/**
	 * Gets the pointer to this shader
	 * @return the pointer to this shader
	 */
	public int getHandle()
	{
		return program;
	}
	
	/**
	 * Binds the shader to use
	 */
	public void bind()
	{
		glUseProgram(program); //Tell openGL to use this shader
	}
	
	/**
	 * Adds a uniform to the shader
	 * @param uniform the uniform's name
	 */
	public void addUniform(String uniform)
	{
		int uniformHandle = glGetUniformLocation(program, uniform); //Obtain a pointer to the uniform location
		
		if (uniformHandle == -1) //This will fail if this uniform is not defined in the actual shader
		{
			System.err.println("Error: Could not find uniform: " + uniform);
			new Exception().printStackTrace();
			System.exit(1);
		}
		
		uniforms.put(uniform, uniformHandle); //Add the value at the pointer
	}
	
	/**
	 * Sets a uniform to an integer
	 * @param uniformName the uniform's name
	 * @param value the integer to set it to
	 */
	public void setUniformi(String uniformName, int value)
	{
		glUniform1i(uniforms.get(uniformName), value); //Sets the value of a uniform
	}
	
	/**
	 * Sets a uniform to a float
	 * @param uniformName the uniform's name
	 * @param value the float to set it to
	 */
	public void setUniformf(String uniformName, float value)
	{
		glUniform1f(uniforms.get(uniformName), value); //Sets the value of a uniform
	}
	
	/**
	 * Sets a uniform to a vector3f
	 * @param uniformName the uniform's name
	 * @param value the vector3f to set it to
	 */
	public void setUniform(String uniformName, Vector3f value)
	{
		glUniform3f(uniforms.get(uniformName), value.x, value.y, value.z); //Sets the value of a uniform
	}
	
	/**
	 * Sets a uniform to a matrix4f
	 * @param uniformName the uniform's name
	 * @param value the matrix4f to set it to
	 */
	public void setUniform(String uniformName, Matrix4f value) //Sets the value of a uniform
	{
		glUniformMatrix4(uniforms.get(uniformName), true, formatMatrix(value));
	}
	
	/**
	 * Adds a vertex shader to this shader
	 * @param text the shader source
	 */
	public void addVertexShader(String text)
	{
		addProgram(text, GL_VERTEX_SHADER); //Add the vertex shader
	}
	
	/**
	 * Adds a fragment shader to this shader
	 * @param text the shader source
	 */
	public void addFragmentShader(String text)
	{
		addProgram(text, GL_FRAGMENT_SHADER); //Add the fragment shader
	}
	
	/**
	 * Adds a geometry shader to this shader
	 * @param text the shader source
	 */
	public void addGeometryShader(String text)
	{
		addProgram(text, GL_GEOMETRY_SHADER); //Add the geometry shader
	}
	
	/**
	 * Compiles the shader together
	 */
	public void compileShader()
	{
		glLinkProgram(program); //Link all the shader parts (e.g. vertex, fragment, geometry) to this shader
		
		if (glGetShaderi(program, GL_LINK_STATUS) == 0) //It couldn't be linked
		{
			System.err.println(glGetShaderInfoLog(program, 1024));
			System.exit(1);
		}
		
		glValidateProgram(program); //Validate that it linked correctly
		
		if (glGetProgrami(program, GL_VALIDATE_STATUS) == 0) //It couldn't be linked correctly
		{
			System.err.println(glGetShaderInfoLog(program, 1024));
			System.exit(1);
		}
	}
	
	/**
	 * Adds a shader to this shader
	 * @param text the shader
	 * @param type the type of shader
	 */
	private void addProgram(String text, int type)
	{	
		int shaderHandle = glCreateShader(type); //Create a pointer to the shader of the type we are making
		
		if (shaderHandle == 0) //If the handle couldn't be created
		{
			System.err.println("Shader creation failed: Could not find valid memory location when adding shader");
			System.exit(1);
		}
		
		glShaderSource(shaderHandle, text); //Bind the source code of the shader to its pointer
		glCompileShader(shaderHandle); //Compile the shader
		
		if (glGetShaderi(shaderHandle, GL_COMPILE_STATUS) == 0) //If the shader didn't compile (error in the shader code)
		{
			System.err.println(glGetShaderInfoLog(shaderHandle, 1024));
			System.exit(1);
		}
		
		glAttachShader(program, shaderHandle); //Attach the shader pointer to this shader
	}
	
	/**
	 * Loads a shader
	 * @param fileName the path to the file
	 * @return the String that stores the shader code
	 */
	private static String loadShader(String fileName)
	{
		StringBuilder shaderSource = new StringBuilder();
		BufferedReader shaderReader = null;
		
		try
		{
			shaderReader = new BufferedReader(new FileReader(Engine.getInstance().getShaderLocation() + fileName)); //Read the file in the shaders folder in the resources folder
			String line;
			
			while ((line = shaderReader.readLine()) != null) //While there are lines add them to the string builder
			{
				shaderSource.append(line).append('\n');
			}
			
			shaderReader.close(); //Close the reader
		} 
		catch (Exception e)
		{
			return null;
		}
		
		return shaderSource.toString();
	}
	
	/**
     * Puts the matrix in the right format for openGL
     * @param value the matrix
     * @return a float buffer of the matrix in the right format
     */
    public static FloatBuffer formatMatrix(Matrix4f value)
    {
    	FloatBuffer buffer = BufferUtil.createFloatBuffer(Matrix4f.SIZE * Matrix4f.SIZE); //Create a buffer for the matrix
    	
    	for (int i = 0 ; i < Matrix4f.SIZE; i++) //Store the matrix values
    	{
    		for (int j = 0; j < Matrix4f.SIZE; j++)
    		{
    			buffer.put(value.matrix[i] [j]);
    		}
    	}
    	
    	buffer.flip(); //Flip the buffer
    	
    	return buffer;
    }
    
    /**
     * Gets the uniform names for this shader
     * @param text the text of the shader
     * @return the uniform names
     */
    public static ArrayList<String> getUniformNames(String text)
    {
    	ArrayList<String> lines = new ArrayList<String>();
    	char[] chars = text.toCharArray();
    	String lastLine = "";
    	
    	for (char c : chars)
    	{
    		if (c != '\n')
    		{
    			lastLine += c;
    		}
    		else
    		{
    			lines.add(lastLine);
    			lastLine = "";
    		}
    	}
    	
    	ArrayList<String> uniformNames = new ArrayList<String>();
    	
    	for (String s : lines)
        {
        	if (s.startsWith("uniform"))
        	{
        		String[] uniform = s.split(" ");
        		uniformNames.add(uniform[2].replaceAll(";", ""));
        	}
        }
    	
    	return uniformNames;
    }
	
}
