package com.primogemstudio.advancedfmk.mixin;

import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.primogemstudio.advancedfmk.client.FontManager;
import com.primogemstudio.advancedfmk.ftwrap.Shaders;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenFontEngineMixin {
    @Unique
    private static final TextureTarget fontInternal = new TextureTarget(1, 1, true, Util.getPlatform() == Util.OS.OSX);

    static {
        fontInternal.setClearColor(1f, 1f, 1f, 0f);
    }

    @Inject(at = @At("RETURN"), method = "render")
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        fontInternal.clear(Util.getPlatform() == Util.OS.OSX);
        fontInternal.resize(Minecraft.getInstance().getWindow().getWidth() * 4, Minecraft.getInstance().getWindow().getHeight() * 4, Util.getPlatform() == Util.OS.OSX);
        fontInternal.bindWrite(true);
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        var buff = Tesselator.getInstance().getBuilder();
        var scale = (float) Minecraft.getInstance().getWindow().getGuiScale();
        var poseStack = graphics.pose();
        poseStack.pushPose();
        poseStack.scale(1 / scale, 1 / scale, 1);
        buff.begin(VertexFormat.Mode.TRIANGLES, DefaultVertexFormat.POSITION_COLOR);
        poseStack.popPose();
        FontManager.INSTANCE.drawText(buff, poseStack, "测试abcd？?!");
        RenderSystem.enableBlend();
        RenderSystem.disableCull();
        BufferUploader.drawWithShader(buff.end());
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        Shaders.INSTANCE.getTEXT_BLUR().setSamplerUniform("BaseLayer", fontInternal);
        Shaders.INSTANCE.getTEXT_BLUR().render(partialTick);
    }
}