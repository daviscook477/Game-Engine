//VERTEX SHADER
#version 330

layout (location = 0) in vec3 position; //Take the position data
//Ignore texture data
layout (location = 2) in vec3 normal; //Take the normal data

uniform vec3 loc; //Obtain the player location
uniform mat4 projectedCameraMatrix; //Obtain a transformation array
uniform float width; //The outline width in pixels
uniform mat4 modelMatrix;
uniform mat4 normalMatrix;

void main()
{
	vec4 transformedPosition = modelMatrix * vec4(position, 1);
	vec4 transformedNormal = normalize(normalMatrix * vec4(normal, 1));
	float dist = width * sqrt((transformedPosition.x - loc.x) * (transformedPosition.x - loc.x) + (transformedPosition.y - loc.y) * (transformedPosition.y - loc.y) + (transformedPosition.z - loc.z) * (transformedPosition.z - loc.z));
	vec4 toAdd = vec4(transformedNormal.x * dist, transformedNormal.y * dist, transformedNormal.z * dist, 0);
	gl_Position = projectedCameraMatrix * (transformedPosition + toAdd); //Output the vertices transformed by the matrix
}