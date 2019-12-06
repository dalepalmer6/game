#version 150

in vec2 pass_texCoords;

out vec4 out_color;

uniform sampler2D sampler;

void main(void) {
	vec4 texel = texture(sampler,pass_texCoords);
	if(texel.a < 0.5)
   		discard;
	out_color = texel;
}