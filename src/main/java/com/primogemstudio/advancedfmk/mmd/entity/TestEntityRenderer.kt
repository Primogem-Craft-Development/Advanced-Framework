package com.primogemstudio.advancedfmk.mmd.entity

import com.mojang.blaze3d.vertex.BufferBuilder
import com.mojang.blaze3d.vertex.BufferVertexConsumer.normalIntValue
import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import com.mojang.math.Axis
import com.primogemstudio.advancedfmk.interfaces.BufferBuilderExt
import com.primogemstudio.advancedfmk.mmd.renderer.CustomRenderType
import com.primogemstudio.mmdbase.io.PMXFile
import glm_.vec3.Vec3
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.resources.ResourceLocation
import org.joml.Matrix3f
import org.joml.Matrix4f
import org.joml.Vector3f
import org.joml.Vector4f
import java.nio.ByteBuffer

class TestEntityRenderer(context: EntityRendererProvider.Context) : EntityRenderer<TestEntity>(context) {
    companion object {
        var enable_pipeline = false
        var compatibility = true
        var render_model = true
        var render_bone_link = false
        var render_bone_parent = false
        var render_normals = false
        val matrix_buffer: ByteBuffer = ByteBuffer.allocateDirect(64)

        fun switchPipeline(vanilla: Boolean) {
            enable_pipeline = !vanilla
        }

        fun switchCompatibility(a: Boolean) {
            compatibility = a
        }
    }

    override fun getTextureLocation(entity: TestEntity): ResourceLocation {
        return ResourceLocation("")
    }

    override fun render(
        entity: TestEntity,
        entityYaw: Float,
        partialTick: Float,
        poseStack: PoseStack,
        buffer: MultiBufferSource,
        packedLight: Int
    ) {
        if (entity.model == null) return
        val buf = buffer.getBuffer(CustomRenderType.saba(entity.model!!.textureManager.id)) as BufferBuilder
        poseStack.pushPose()
        poseStack.scale(0.1f, 0.1f, 0.1f)
        poseStack.mulPose(Axis.YN.rotationDegrees(entityYaw))
        buf.vertices = entity.model!!.render(
            buf.buffer, poseStack.last().pose().get(matrix_buffer)
        )
        poseStack.popPose()
    }
}

fun VertexConsumer.directCommit(mat: Matrix4f, pos: Vec3, b: Int) {
    with(this as BufferBuilder) {
        vertex(mat, pos.x, pos.y, pos.z).color(b, b, b, 255).normal(pos.x / 16f, pos.y / 16f, pos.z / 16f)
        endVertex()
    }
}

fun VertexConsumer.directCommit(mat: Matrix4f, x: Float, y: Float, z: Float, u: Float, v: Float, light: Int) {
    with(this as BufferBuilder) {
        val p = mat.transform(Vector4f(x, y, z, 1.0f))
        putFloat(0, p.x)
        putFloat(4, p.y)
        putFloat(8, p.z)
        putFloat(12, u)
        putFloat(16, v)
        putShort(20, (light and 0xFFFF).toShort())
        putShort(22, (light shr 16 and 0xFFFF).toShort())
        (this as BufferBuilderExt).bumpNxt(24)
        endVertex()
    }
}

fun VertexConsumer.directCommitB(
    mat: Matrix4f,
    nom: Matrix3f,
    x: Float,
    y: Float,
    z: Float,
    u: Float,
    v: Float,
    light: Int,
    nx: Float,
    ny: Float,
    nz: Float
) {
    with(this as BufferBuilder) {
        val p = mat.transform(Vector4f(x, y, z, 1.0f))
        val n = nom.transform(Vector3f(nx, ny, nz))
        putFloat(0, p.x)
        putFloat(4, p.y)
        putFloat(8, p.z)
        putByte(12, 255.toByte())
        putByte(13, 255.toByte())
        putByte(14, 255.toByte())
        putByte(15, 255.toByte())
        putFloat(16, u)
        putFloat(20, v)
        val i: Int = if ((this as BufferBuilderExt).fullFormat()) {
            putShort(24, 0.toShort())
            putShort(26, 10.toShort())
            28
        } else 24
        putShort(i + 0, (light and 0xFFFF).toShort())
        putShort(i + 2, (light shr 16 and 0xFFFF).toShort())
        putByte(i + 4, normalIntValue(n.x))
        putByte(i + 5, normalIntValue(n.y))
        putByte(i + 6, normalIntValue(n.z))
        (this as BufferBuilderExt).bumpNxt(i + 8)
        this.endVertex()
    }
}

fun VertexConsumer.directCommit(
    mat: Matrix4f,
    nom: Matrix3f,
    x: Float,
    y: Float,
    z: Float,
    u: Float,
    v: Float,
    light: Int,
    nx: Float,
    ny: Float,
    nz: Float
) {
    with(this as BufferBuilder) {
        val p = mat.transform(Vector4f(x, y, z, 1.0f))
        val n = nom.transform(Vector3f(nx, ny, nz))
        this.vertex(p.x, p.y, p.z, 1f, 1f, 1f, 1f, u, v, OverlayTexture.NO_OVERLAY, light, n.x, n.y, n.z)
    }
}