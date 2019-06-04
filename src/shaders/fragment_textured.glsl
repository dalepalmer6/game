#version 150 core

uniform sampler2D texture_diffuse;
uniform float time;
uniform float amplitudex;
uniform float amplitudey;
uniform float freqx;
uniform float freqy;
uniform float speedx;
uniform float speedy;

in vec4 pass_Color;
in vec2 pass_TextureCoord;

//out vec4 out_Color;

vec2 distorsion(vec2 p)
{	
	vec2 res = vec2(0.0,0.0);
	vec2 r = vec2(floor(gl_FragCoord.x), floor(gl_FragCoord.y));
	if (mod(r.y,2) == 0.0) {
		res.x = p.x + amplitudex * sin( freqx + time*speedx);
	} else {
		res.x = p.x - amplitudex * sin( freqx + time*speedx);
	}
	
	//res.y = p.y + amplitudey * (freqy + time*speedy);
	res.y = p.y;
	return res;
}

void main(void) {
	 gl_FragColor = texture2D(texture_diffuse,distorsion(pass_TextureCoord));
	 
}