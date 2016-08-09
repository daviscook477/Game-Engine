package net.engine.model;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import net.engine.core.Engine;
import net.engine.math.Vector2f;
import net.engine.math.Vector3f;
import net.engine.matrix.MatrixStack;
import net.engine.render.Material;
import net.engine.render.mesh.Mesh;
import net.engine.render.mesh.Vertex;
import net.engine.transform.Orientation;
import net.engine.util.BufferUtil;

/**
 * A static model that is loaded from a file.
 * For efficiency purposes the file is loaded only once
 * @author Davis
 *
 */
public class FileModel extends Model
{
	
	public static final int BYTES_PER_FLOAT = 4;
	
	public static final int DRAW_TYPE = GL_STATIC_DRAW;
	
	public static final String VERTEX = "v";
	public static final String FACE = "f";
	public static final String COMMENT = "#";
	public static final String TEXTURE = "vt";
	public static final String NORMAL = "vn";
	
	private static HashMap<String, Mesh> meshes = new HashMap<String, Mesh>(); //The static set of base meshes
	
	/**
     * Creates a buffer of the indices of the mesh
     * @param indices the indices
     * @return the a buffer in the right format for openGL
     */
    private static IntBuffer createIndicesBuffer(int[] indices)
    {
    	IntBuffer  buffer = BufferUtil.createIntBuffer(indices.length); //Create an IntBuffer for the indices
    	
    	buffer.put(indices); //Add the values
    	
    	buffer.flip(); //Flip the buffer to put it in the correct format for openGL
    	
    	return buffer;
    }
    
    /**
     * Creates a buffer of the vertices of the mesh
     * @param vertices the vertices to put in the buffer
     * @return a buffer of the vertices in the right format for openGL
     */
    private static FloatBuffer createVertexBuffer(Vertex[] vertices)
    {
        FloatBuffer buffer = BufferUtil.createFloatBuffer(vertices.length * vertices[0].getSize()); //Create a buffer for the vertices
        
        for (int i = 0; i < vertices.length; i++) //Add the data into the buffer for each vertex
        {
        	//Put in the position coords
            buffer.put(vertices[i].pos.x);
            buffer.put(vertices[i].pos.y);
            buffer.put(vertices[i].pos.z);
            if (vertices[0].tex != null) //If there are texture coords
            {
            	//Put in the texture coords
            	buffer.put(vertices[i].tex.x);
            	buffer.put(vertices[i].tex.y);
            }
            if (vertices[0].normal != null) //If there are normals
            {
            	//Put in the normals
            	buffer.put(vertices[i].normal.x);
                buffer.put(vertices[i].normal.y);
                buffer.put(vertices[i].normal.z);
            }
        }
        
        buffer.flip();
        
        return buffer;
    }
	
	/**
	 * Forces a model to be loaded in
	 * @param fileName the model to load
	 */
	public static void forceLoad(String fileName)
	{
		if (meshes.get(fileName) == null)
		{
			meshes.put(fileName, FileModelLoader.loadMesh(fileName));
		}
	}

	private String name; //The name of this mesh
	
	private int vboHandle, iboHandle; //The pointers to the buffers
	
	/**
	 * Creates a new model from the file
	 * @param fileName the file
	 * @param startPos the starting orientation
	 * @param material the material to use
	 */
	public FileModel(String fileName, Orientation startPos, Material material)
	{
		super(material);
		if (meshes.get(fileName) == null) //Load the base model if it hasn't already
		{
			meshes.put(fileName, FileModelLoader.loadMesh(fileName));
		}
		
		name = fileName;
		
		modelMatrix = startPos;
		
		//Buffer creation code
		vboHandle = glGenBuffers();
		iboHandle = glGenBuffers();
		
		Mesh mesh = meshes.get(name);
		
		//Send the arrays to the buffers
		glBindBuffer(GL_ARRAY_BUFFER, vboHandle);
        glBufferData(GL_ARRAY_BUFFER, createVertexBuffer(mesh.vertices), GL_STATIC_DRAW);
        
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboHandle);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, createIndicesBuffer(mesh.indices), GL_STATIC_DRAW);

	}
	
	@Override
	public void render(MatrixStack stack)
	{
		Mesh mesh = meshes.get(name);
		stack.pushMatrix(modelMatrix.getTransformationMatrix());
		Engine.getInstance().setModelData();
		glBindBuffer(GL_ARRAY_BUFFER, vboHandle); //Bind the vertex buffer object to be the current buffer operated on
    	
        glEnableVertexAttribArray(0); //Enable vertex position data to be sent to the shader in location 0
        glVertexAttribPointer(0, 3, GL_FLOAT, false, mesh.vertexSize * BYTES_PER_FLOAT, 0); //Set the shader to receive in location zero, a vector3f, that is not normalized, that has a stride between each set of numbers equal to the length of each vertex, that starts at the begging of each vertex
        
        if (mesh.texData) //If there is texture data:
        {
        	glEnableVertexAttribArray(1); //Enable vertex texture coord data to be sent to the shader in location 1
        	glVertexAttribPointer(1, 2, GL_FLOAT, false, mesh.vertexSize * BYTES_PER_FLOAT, Vertex.POSITION_SIZE * BYTES_PER_FLOAT); //how many texture floats, last variable is offset for each vertex, each is offset by how many floats came before it in the array in bytes
        }
        
        if (mesh.normalData) //If there is normal data:
        {
        	glEnableVertexAttribArray(2); //Enable vertex normal data to be sent to the shader in location 2
        	int start = Vertex.POSITION_SIZE;
        	if (mesh.texData) //Add the texture data to ignore over
        	{
        		start += Vertex.TEXTURE_SIZE;
        	}
        	glVertexAttribPointer(2, 3, GL_FLOAT, false, mesh.vertexSize * BYTES_PER_FLOAT, start * BYTES_PER_FLOAT); //how many normal floats, last variable is offset for each vertex, each is offset by how many floats came before it in the array in bytes
        }
        
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboHandle);
        glDrawElements(GL_TRIANGLES, mesh.indices.length, GL_UNSIGNED_INT, 0); //draw triangles, where there are size triangles, they are unsigned ints, start at the beggining of the array
        
        //Disable the attribute sending
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        
        stack.popMatrix();
	}
	
	/**
	 * Loads a model from a file
	 * @author Davis
	 *
	 */
	private static class FileModelLoader
	{
		
		private FileModelLoader() {}
		
		/**
		 * Loads into this mesh the vertices and indices of a mesh
		 * @param fileName the name of the file
		 * @return the mesh that was pathed by the file name
		 */
		private static Mesh loadMesh(String fileName)
		{
			String[] splitArray = fileName.split("\\.");
			String ext = splitArray[splitArray.length - 1];
			
			if (!ext.equals("obj")) //Check the extension
			{
				System.err.println("Error: File format not supported for mesh data: " + ext);
				new Exception().printStackTrace();
				System.exit(1);
			}
			
			ArrayList<Vector3f> vertices = new ArrayList<Vector3f>();
			ArrayList<Vector2f> textures = new ArrayList<Vector2f>();
			ArrayList<Vector3f> normals = new ArrayList<Vector3f>();
			ArrayList<Integer[]> faces = new ArrayList<Integer[]>();
			
			BufferedReader meshReader = null;
			
			try //Read the file
			{
				meshReader = new BufferedReader(new FileReader(Engine.getInstance().getModelLocation() + fileName));
				String line;
				
				while ((line = meshReader.readLine()) != null)
				{
					String[] tokens = line.split(" ");
					tokens = removeEmptyStrings(tokens);
					
					if (tokens.length == 0 || tokens[0].equals(COMMENT)) //If the line is a comment
					{
						continue; //Ignore
					}
					
					if (tokens[0].equals(VERTEX)) //Add vertices
					{
						vertices.add(new Vector3f(Float.valueOf(tokens[1]), Float.valueOf(tokens[2]), Float.valueOf(tokens[3])));
					}
					if (tokens[0].equals(TEXTURE))
					{
						textures.add(new Vector2f(Float.valueOf(tokens[1]), Float.valueOf(tokens[2])));
					}
					if (tokens[0].equals(NORMAL)) //Add normals
					{
						normals.add(new Vector3f(Float.valueOf(tokens[1]), Float.valueOf(tokens[2]), Float.valueOf(tokens[3])));
					}
					if (tokens[0].equals(FACE)) //Add faces
					{
						String[] vertex1 = tokens[1].split("/");
						faces.add(parseVertex(vertex1)); //Determine the position, tex coord, and normal of this vertex
						String[] vertex2 = tokens[2].split("/");
						faces.add(parseVertex(vertex2)); //Determine the position, tex coord, and normal of this vertex
						String[] vertex3 = tokens[3].split("/");
						faces.add(parseVertex(vertex3)); //Determine the position, tex coord, and normal of this vertex
					}
				}
				
				meshReader.close(); //Close the reader
			}
			catch (Exception e)
			{
				System.out.println("Issues reading model. Bad data.");
				e.printStackTrace();
				System.exit(1);
			}
			
			ArrayList<Vertex> neededVertices = new ArrayList<Vertex>(faces.size()); //The vertex's that need to be created
			ArrayList<Integer> neededFaces = new ArrayList<Integer>(faces.size()); //The face's that need to be created
			
			boolean validMesh = true; //Is the mesh valid
			
			boolean texNull = (faces.get(0)[1] == -1); //Set the starting value
			
			boolean normalNull = (faces.get(0)[2] == -1); //Set the starting value
			
			faceLoop:
			for (int i = 0; i < faces.size(); i++) //Go through all the faces
			{
				Integer[] vertex = faces.get(i); //get the position, tex, and normal
				
				Vector3f pos = vertices.get(vertex[0]); //create the position vector3f
				Vector2f tex = null;
				if (vertex[1] != -1) //Check if there is a texture
				{
					tex = textures.get(vertex[1]); //Set the texture
				}
				if (tex == null && !texNull || (tex != null && texNull)) //If this texture exists and the other didn't and vice versa
				{
					validMesh = false; //the texture is invalidated because it must have only textures or only no textures
					break faceLoop;
				}
				Vector3f normal = null;
				if (vertex[2] != -1) //check if there is a normal
				{
					normal = normals.get(vertex[2]); //set the normal
				}
				if (normal == null && !normalNull || (normal != null && normalNull))//If this normal exists and the other didn't and vice versa
				{
					validMesh = false; //the normal is invalidated because it must have only normals or only no normals
					break faceLoop;
				}
				neededVertices.add(new Vertex(pos, tex, normal)); //Add a vertex for each vertex in a face
				neededFaces.add(neededVertices.size() - 1); //Add the vertex we just added to be the one in the face's indices
			}
			
			if(!validMesh)
			{
				System.err.println("An object must specify textures for all or none of its vertices. Same for normals.");
				new Exception().printStackTrace();
				System.exit(1);
			}
			
			Vertex[] vertexData = new Vertex[neededVertices.size()]; //convert to arrays from arraylists
			neededVertices.toArray(vertexData);
			
			int[] indexData = new int[neededFaces.size()]; //convert to arrays from arraylists
			
			for (int i = 0; i < indexData.length; i++)
			{
				indexData[i] = neededFaces.get(i);
			}
			
			int vertexSize = vertexData[0].getSize();
			boolean texData = (!(vertexData[0].tex == null));
	        boolean normalData = (!(vertexData[0].normal == null));
			
			return new Mesh(vertexData, indexData, vertexSize, texData, normalData);
		}
		
		/**
		 * Parses the vertex number, the texture number, and the normal number from tokens
		 * @param tokens the tokens to parse from
		 * @return an Integer array of the vertex number, texture number, and normal number: any that weren't there are -1
		 */
		private static Integer[] parseVertex(String[] tokens)
		{
			if (tokens.length == 1)
			{
				return new Integer[] {Integer.parseInt(tokens[0]) - 1, -1, -1};
			}
			else if (tokens.length == 2)
			{
				return new Integer[] {Integer.parseInt(tokens[0]) - 1, Integer.parseInt(tokens[1]) - 1, -1};
			}
			else
			{
				if (!tokens[1].equals("") && !tokens[2].equals(""))
				{
					return new Integer[] {Integer.parseInt(tokens[0]) - 1, Integer.parseInt(tokens[1]) - 1, Integer.parseInt(tokens[2]) - 1};
				}
				else if (tokens[1].equals(""))
				{
					return new Integer[] {Integer.parseInt(tokens[0]) - 1, -1, Integer.parseInt(tokens[2]) -1};
				}
				else if (tokens[2].equals(""))
				{
					return new Integer[] {Integer.parseInt(tokens[0]) - 1, Integer.parseInt(tokens[1]) -1, -1};
				}
				else
				{
					return new Integer[] {Integer.parseInt(tokens[0]) - 1, -1, -1};
				}
			}
		}
		
		/**
		 * Removes the empty strings from a list of strings
		 * @param tokens the array of strings to remove from
		 * @return the array without any empty strings
		 */
		private static String[] removeEmptyStrings(String[] tokens)
		{
			ArrayList<String> result = new ArrayList<String>();
			
			for (int i = 0; i < tokens.length; i++)
			{
				if (!tokens[i].equals(""))
				{
					result.add(tokens[i]);
				}
			}
			
			String[] array = new String[result.size()];
			result.toArray(array);
			
			return array;
		}
		
	}
	
}
