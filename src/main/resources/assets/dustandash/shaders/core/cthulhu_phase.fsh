#version 150

uniform vec4 PhaseTint;

in vec4 vertexColor;

out vec4 fragColor;

void main() {
    fragColor = vertexColor * PhaseTint;
}
