package net.engine.render.mesh;

/**
 * A simple container class
 * @author Davis
 *
 */
public class Mesh
{

	public Vertex[] vertices;
	public int[] indices;
	public boolean texData, normalData;
	public int vertexSize;
	
	/**
	 * Create a blank mesh
	 */
	public Mesh() {}
	
	/**
	 * Create a new mesh with starting vertices and indices
	 * @param vertices the vertices
	 * @param indices the indices
	 * @param vertexSize the vertex size
	 * @param texData is there texture data
	 * @param normalData is there normal data
	 */
	public Mesh(Vertex[] vertices, int[] indices, int vertexSize, boolean texData, boolean normalData)
	{
		this.vertices = vertices;
		this.indices = indices;
		this.vertexSize = vertexSize;
		this.texData = texData;
		this.normalData = normalData;
	}
		
}