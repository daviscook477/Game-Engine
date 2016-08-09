//FRAGMENT SHADER
#version 330

uniform vec3 outlineColor; //The color to outline in

void main()
{
	gl_FragColor = vec4(outlineColor, 1); //Output the color to outline in
}