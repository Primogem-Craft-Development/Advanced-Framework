package com.primogemstudio.advancedfmk.mixin;

import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Inject(method = "render", at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/client/gui/GuiGraphics;<init>(Lnet/minecraft/client/Minecraft;Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;)V"))
    private void preRender(float partialTicks, long nanoTime, boolean renderLevel, CallbackInfo ci) {

    }

    @Inject(method = "render", at = @At("TAIL"))
    private void postRender(float partialTicks, long nanoTime, boolean renderLevel, CallbackInfo ci) {

    }
}
