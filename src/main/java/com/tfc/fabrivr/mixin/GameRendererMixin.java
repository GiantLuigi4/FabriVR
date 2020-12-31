package com.tfc.fabrivr.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.tfc.fabrivr.mixin_code.GameRendererCode;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
	@Inject(at = @At("HEAD"), method = "render(FJZ)V")
	public void renderPre(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
		GameRendererCode.renderPre(tickDelta, startTime);
	}
	
	@Inject(at = @At("TAIL"), method = "render(FJZ)V")
	public void renderPost(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
		GameRendererCode.renderPost();
	}
	
	@Inject(
			method = "render(FJZ)V",
			at = @At(value = "INVOKE", target = "net/minecraft/client/MinecraftClient.getLastFrameDuration()F", ordinal = 1)
	)
	public void preRenderGUI(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
		if (MinecraftClient.getInstance().world != null) {
			RenderSystem.pushMatrix();
			RenderSystem.scalef(0, 0, 0);
		}
	}
}
