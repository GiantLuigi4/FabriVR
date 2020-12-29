package com.tfc.fabrivr.mixin;

import com.tfc.fabrivr.FabriVR;
import com.tfc.fabrivr.client.FabriVRClient;
import com.tfc.fabrivr.client.FabriVRFrameBuffer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.gl.Framebuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.ovr.OVR;
import org.lwjgl.ovr.OVRGL;
import org.lwjgl.ovr.OVRTextureSwapChainDesc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.IntBuffer;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
	@Inject(at = @At("HEAD"),method = "close()V")
	public void close(CallbackInfo ci) {
		OVR.ovr_DestroyTextureSwapChain(FabriVRClient.session.get(0), FabriVRClient.textureSwapChain.get(0));
		OVR.ovr_Destroy(FabriVRClient.session.get(0));
		OVR.ovr_Shutdown();
	}
	
	@Inject(at = @At("TAIL"), method = "<init>")
	public void init(RunArgs args, CallbackInfo ci) {
//		FabriVRClient.textureSwapChain = PointerBuffer.allocateDirect(1);
//
//		OVRTextureSwapChainDesc desc = OVRTextureSwapChainDesc.calloc();
//		desc.set(OVR.ovrTexture_2D, OVR.OVR_FORMAT_R8G8B8A8_UNORM_SRGB, 1, FabriVRClient.bufferSize.w(), FabriVRClient.bufferSize.h(), 1, 1, true, desc.MiscFlags(), desc.BindFlags());
//		OVRGL.ovr_CreateTextureSwapChainGL(FabriVRClient.session.get(0), desc, FabriVRClient.textureSwapChain);
//
//		IntBuffer chainTexId = BufferUtils.createIntBuffer(1);
//		OVRGL.ovr_GetTextureSwapChainBufferGL(FabriVRClient.session.get(0), FabriVRClient.textureSwapChain.get(0), 0, chainTexId);
//		FabriVRClient.vrFBO = new FabriVRFrameBuffer(FabriVRClient.bufferSize, chainTexId.get(0));
//
//		FabriVRClient.layer.ColorTexture(0, FabriVRClient.textureSwapChain.get(0));
//		FabriVRClient.layer.ColorTexture(1, FabriVRClient.textureSwapChain.get(0));

		GL11.glEnable(GL30.GL_FRAMEBUFFER);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL30.GL_RENDERBUFFER);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		FabriVRClient.initVR();
	}
}
