#version 150 core

in vec3 pos;
//in vec2 texCoords;

out vec2 pass_texCoords;

uniform vec2 texCoords1;
uniform vec2 texCoords2;
uniform vec2 texCoords3;
uniform vec2 texCoords4;

uniform mat4 transMatrix;
uniform mat4 projMatrix;

void main(void) {
	int index = gl_VertexID % 4;
	if (index == 0) {
		pass_texCoords = texCoords1;
	}
	else if (index == 1) {
		pass_texCoords = texCoords2;
	}
	else if (index == 2) {
		pass_texCoords = texCoords3;
	}
	else if (index == 3) {
		pass_texCoords = texCoords4;
	}
	gl_Position = projMatrix * transMatrix * vec4(pos,1.0);
}