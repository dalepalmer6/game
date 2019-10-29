#version 150 core

uniform sampler2D texture_diffuse;
uniform sampler2D texture_color_palette;	// 8x1 palette
uniform float time;
uniform float frames;
uniform float amplitudex;
uniform float amplitudey;
uniform float freqx;
uniform float freqy;
uniform float speedx;
uniform float speedy;
uniform float transx;
uniform float transy;

in vec2 pass_TextureCoord;

float offset(int y, float amp, float freq, float spd) {
	return amp * sin(freq*y + time*spd);
}

vec2 distorsion(vec2 p)
{	
	vec2 res = vec2(0.0,0.0);
	//vec2 r = vec2(floor(gl_FragCoord.x), floor(gl_FragCoord.y));
	vec2 r = gl_FragCoord.xy;
	int value = int(r.y/4);
	
	if (mod(value,2) == 0) {
		res.x = p.x + offset(value, amplitudex, freqx, speedx);
	} else {
		res.x = p.x - offset(value, amplitudex, freqx, speedx);
	}
	res.x = time*transx + res.x;

	res.y = time*transy + p.y + offset(value, amplitudey, freqy, speedy);
	
	return res;
}

vec4 swapPaletteColor(vec4 colorIndex)
{
	float indexX = time*4 + colorIndex.x*15;
	vec4 texel = texture2D(texture_color_palette,vec2(indexX,0.25));
	return texel;
}

void main(void) {
	gl_FragColor = swapPaletteColor(texture2D(texture_diffuse,distorsion(pass_TextureCoord)));
	
	//gl_FragColor = swapPaletteColor(texture2D(texture_diffuse,pass_TextureCoord));
	
	//gl_FragColor = swapPaletteColor(texture2D(texture_diffuse,(pass_TextureCoord)));
}