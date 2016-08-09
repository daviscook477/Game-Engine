//VERTEX SHADER
#version 330

layout (location = 0) in vec3 position; //Take the position data
layout (location = 1) in vec2 texCoord; //Take the texture data

out vec2 texCoord0; //Output the texture data

uniform mat4 transform; //Obtain a transformation array
uniform mat4 modelMatrix; //Obtain the model transformation

void main()
{
	gl_Position = transform * modelMatrix * vec4(position, 1); //Output the vertices transformed by the matrix
	texCoord0 = texCoord;
}