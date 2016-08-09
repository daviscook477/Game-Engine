//FRAGMENT SHADER
#version 330

in vec2 texCoord0; //Input the texture coordinate from the vs
in vec3 normal0; //Input the normal from the vs
in vec3 lightDir0; //Input the light dir from the vs

uniform vec3 color; //Take in a color
uniform sampler2D sampler; //Take in a texture

void main()
{
	float intensity = dot (lightDir0, normalize(normal0));

	vec4 newColor = vec4(color * .3, 1);
	
	if (intensity > .4)
	{
		newColor = vec4(color * .4, 1);
	}
	if (intensity > .5)
	{
		newColor = vec4(color * .5, 1);
	}
	if (intensity > .6)
	{
		newColor = vec4(color, 1);
	}
	
	
	
	vec4 textureColor = texture2D(sampler, texCoord0.xy); //Create a color by sampling the texture
	gl_FragColor = textureColor * newColor; //Output the texture masked with the color
}