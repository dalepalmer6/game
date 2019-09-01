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

in vec4 pass_Color;
in vec2 pass_TextureCoord;

//out vec4 out_Color;

vec2 distorsion(vec2 p)
{	
	vec2 res = vec2(0.0,0.0);
	vec2 r = vec2(floor(gl_FragCoord.x), floor(gl_FragCoord.y));
	//if (mod(r.y,4) == 0.0) {
		res.x = p.x + amplitudex * sin( time*freqx + int(r.y/8)*speedx);
	//} else {
//		res.x = time*2 + p.x - amplitudex * sin( freqx + time*speedx);
	//}
	
	//res.x = time*2 + p.x;
	
	//res.y = p.y + amplitudey * (freqy + time*speedy);
	res.y = time*2 + p.y;
	return res;
}

vec4 swapPaletteColor(vec4 colorIndex)
{
	//vec4 texel = texture2D(texture_color_palette,vec2(2 + (mod((((colorIndex.x)+time)*255),13))/10,0.10));
	//float indexX = mod((colorIndex.x)*15,15);
	//float indexX = 0.0625;
	//float indexX = mod(time*4 + colorIndex.x*255/15,15);
	float indexX = time*4 + colorIndex.x*255/15;
	vec4 texel = texture2D(texture_color_palette,vec2(indexX,0));
	return texel;
}

void main(void) {
	//gl_FragColor = swapPaletteColor(texture2D(texture_diffuse,distorsion(pass_TextureCoord)));
	
	gl_FragColor = swapPaletteColor(texture2D(texture_diffuse,pass_TextureCoord));
	
	//gl_FragColor = swapPaletteColor(texture2D(texture_diffuse,(pass_TextureCoord)));
}