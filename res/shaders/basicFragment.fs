//FRAGMENT SHADER
#version 330

in vec2 texCoord0; //Input the texture coordinate from the vs

uniform vec3 color; //Take in a color
uniform sampler2D sampler; //Take in a texture

void main()
{
	vec4 textureColor = texture2D(sampler, texCoord0.xy); //Create a color by sampling the texture
	gl_FragColor = textureColor * vec4(color, 1); //Output the texture masked with the color
}