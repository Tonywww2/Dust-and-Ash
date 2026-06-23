#version 150

uniform sampler2D DiffuseSampler;
uniform vec2 OutSize;
uniform float Time;

in vec2 texCoord;

out vec4 fragColor;

float hash(vec2 value) {
    return fract(sin(dot(value, vec2(12.9898, 78.233))) * 43758.5453);
}

void main() {
    vec4 baseColor = texture(DiffuseSampler, texCoord);
    float scan = step(0.92, fract((texCoord.y * OutSize.y + Time * 30.0) * 0.08));
    float grain = hash(floor(texCoord * OutSize * 0.35) + Time);
    vec3 tint = vec3(0.05, 0.75, 0.62) * (0.08 + scan * 0.18 + grain * 0.08);
    fragColor = vec4(baseColor.rgb + tint, baseColor.a);
}
