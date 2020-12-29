package com.tfc.fabrivr.mixin;

import com.tfc.fabrivr.client.FabriVRClient;
import com.tfc.fabrivr.mixin_code.GameRendererCode;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.opengl.GL30;
import org.lwjgl.ovr.OVR;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
	@Inject(at = @At("HEAD"), method = "render(FJZ)V")
	public void renderPre(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
		GameRendererCode.renderPre(tickDelta,startTime);
	}
	
	@Inject(at = @At("TAIL"), method = "render(FJZ)V")
	public void renderPost(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
		GameRendererCode.renderPost();
	}
}
