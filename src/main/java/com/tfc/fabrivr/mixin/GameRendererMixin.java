package com.tfc.fabrivr.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.tfc.fabrivr.client.FabriVRClient;
import com.tfc.fabrivr.client.GameRendererHandler;
import com.tfc.fabrivr.utils.openvr.Tracking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
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
import org.spongepowered.asm.mixin.injection.Redirect;
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
	
	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;render(Lnet/minecraft/client/util/math/MatrixStack;IIF)V"))
	public void FabriVR_renderGUI(Screen screen, MatrixStack matrices, int mouseX, int mouseY, float delta) {
		FabriVRClient.guiFrameBuffer.setClearColor(0, 0, 0, 0);
		FabriVRClient.guiFrameBuffer.clear(true);
		FabriVRClient.guiFrameBuffer.beginWrite(true);
		screen.render(matrices, mouseX, mouseY, delta);
		MinecraftClient.getInstance().getFramebuffer().beginWrite(true);
	}
}
