#version 150 core

uniform sampler2D texture_diffuse;
uniform float time;

in vec4 pass_Color;
in vec2 pass_TextureCoord;

//out vec4 out_Color;

vec2 distorsion(vec2 p, float amplitude, float frequency, float speed)
{	
	return vec2(p.x + amplitude*4 * sin(p.x*frequency + time*speed),
				p.y + amplitude * sin(p.y*frequency + time*speed));
}

void main(void) {
	 gl_FragColor = texture2D(texture_diffuse,distorsion(pass_TextureCoord,0.2,4,16.0));
}