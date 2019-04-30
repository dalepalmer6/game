#version 150 core

uniform sampler2D texture_diffuse;
uniform float time;

in vec4 pass_Color;
in vec2 pass_TextureCoord;

//out vec4 out_Color;

vec2 distorsion(vec2 p, float amplitude, float frequency, float speed)
{	
	vec2 res = vec2(0.0,0.0);
	vec2 r = vec2(floor(gl_FragCoord.x), floor(gl_FragCoord.y));
	if (mod(r.y,2) == 0.0) {
		res.x = p.x + amplitude * sin( frequency + time*speed);
	} else {
		res.x = p.x - amplitude * sin( frequency + time*speed);
	}
	
	res.y = p.y + amplitude * (frequency + time*speed);
	
	return res;
}

void main(void) {
	 gl_FragColor = texture2D(texture_diffuse,distorsion(pass_TextureCoord,0.1,0.5,8.0));
}