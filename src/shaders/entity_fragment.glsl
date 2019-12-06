#version 400 core

in vec2 pass_texCoords;

out vec4 color;

uniform sampler2D sampler;

void main(void) {
	
	out_color = texture(sampler,pass_texCoords);

}