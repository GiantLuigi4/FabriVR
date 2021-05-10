package com.tfc.fabrivr.mixin;

import com.tfc.fabrivr.client.MinecraftClientHandlerer;
import com.tfc.fabrivr.utils.openvr.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
	@Shadow
	@Final
	private Framebuffer framebuffer;
	
	@Inject(at = @At("HEAD"), method = "render")
	public void FabriVR_preTick(CallbackInfo ci) {
		if (MinecraftClient.getInstance().world == null) Tracking.update();
//		System.out.println(MinecraftClient.getInstance().getWindow().getWidth());
//		if (
//				MinecraftClient.getInstance().getFramebuffer().textureWidth != 854 ||
//				MinecraftClient.getInstance().getFramebuffer().textureHeight != 854
//		) {
//			MinecraftClient.getInstance().getFramebuffer().initFbo(
//					854, 854, true
//			);
//		}
	}
	
	@Inject(at = @At("TAIL"), method = "close")
	public void FabriVR_onClose(CallbackInfo ci) {
		Session.end();
	}
	
	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gl/Framebuffer;endWrite()V"), method = "render")
	public void FabriVR_postRenderFramebuffer(boolean tick, CallbackInfo ci) {
		MinecraftClientHandlerer.postRenderFBO(framebuffer);
	}
}
