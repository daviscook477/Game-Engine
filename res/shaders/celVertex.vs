//VERTEX SHADER
#version 330

layout (location = 0) in vec3 position; //Take the position data
layout (location = 1) in vec2 texCoord; //Take the texture data
layout (location = 2) in vec3 normal; //Take the normal data


out vec2 texCoord0; //Output the texture data
out vec3 normal0; //Output the normal data
out vec3 lightDir0;

uniform mat4 projectedCameraMatrix; //Obtain a transformation array
uniform vec3 lightDir; //The direction of light
uniform mat4 modelMatrix;
uniform mat4 normalMatrix;

void main()
{
	gl_Position = projectedCameraMatrix * modelMatrix * vec4(position, 1); //Output the vertices transformed by the matrix
	texCoord0 = texCoord;
	normal0 = vec3(vec4(normal, 1));
	lightDir0 = vec3(vec4(lightDir, 1) * normalMatrix);
}