package com.tfc.fabrivr.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.tfc.fabrivr.client.GameRendererHandler;
import com.tfc.fabrivr.utils.openvr.Tracking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Quaternion;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
	@Shadow private float zoom;
	@Shadow private float zoomX;
	@Shadow private float zoomY;
	
	@Shadow protected abstract double getFov(Camera camera, float tickDelta, boolean changingFov);
	
	@Shadow @Final private MinecraftClient client;
	@Shadow private float viewDistance;
	
	@Inject(at = @At("HEAD"), method = "renderHand",cancellable = true)
	public void FabriVR_preRender(MatrixStack matrices, Camera camera, float tickDelta, CallbackInfo ci) {
		ci.cancel();
	}
	
	@Inject(at = @At("HEAD"), method = "loadProjectionMatrix")
	public void FabriVR_preLoadProjectionMatrix(Matrix4f matrix4f, CallbackInfo ci) {
		GameRendererHandler.preLoadMatrix(matrix4f);
	}
}
