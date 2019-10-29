#version 150 core

in vec4 in_Position;
in vec2 in_TextureCoord;

out vec2 pass_TextureCoord;

uniform mat4 transMatrix;
uniform mat4 projMatrix;

void main(void) {
	//gl_Position = projMatrix * transMatrix * in_Position;
	gl_Position = in_Position;
	pass_TextureCoord = in_TextureCoord;
}