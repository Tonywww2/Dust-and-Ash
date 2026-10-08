package com.tonywww.dustandash.platform;

import com.mojang.blaze3d.vertex.VertexConsumer;
import org.joml.Matrix4f;
import org.joml.Matrix3f;
public final class PlatformRendering {
    private PlatformRendering() {}
    public static Vertex vertex(VertexConsumer consumer, Matrix4f matrix, float x, float y, float z) {
        //? if forge {
        consumer.vertex(matrix, x, y, z);
        //?} else {
        /*consumer.addVertex(matrix, x, y, z);
        *///?}
        return new Vertex(consumer);
    }
    public record Vertex(VertexConsumer consumer) {
        public Vertex color(int r, int g, int b, int a) {
            //? if forge {
            consumer.color(r, g, b, a);
            //?} else {
            /*consumer.setColor(r, g, b, a);
            *///?}
            return this;
        }
        public Vertex normal(Matrix3f matrix, float x, float y, float z) {
            //? if forge {
            consumer.normal(matrix, x, y, z);
            //?} else {
            /*org.joml.Vector3f normal = matrix.transform(new org.joml.Vector3f(x, y, z)).normalize();
            consumer.setNormal(normal.x(), normal.y(), normal.z());
            *///?}
            return this;
        }
        public void endVertex() {
            //? if forge {
            consumer.endVertex();
            //?}
        }
    }
}
