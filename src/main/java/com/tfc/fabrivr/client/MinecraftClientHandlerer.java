package com.tfc.fabrivr.client;

import com.tfc.fabrivr.FabriVR;
import com.tfc.fabrivr.mixin.AbstractTextureAccessor;
import com.tfc.fabrivr.utils.openvr.Eye;
import com.tfc.fabrivr.utils.openvr.VRCompositor;
import com.tfc.fabrivr.utils.openvr.VRCompositorError;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.texture.ResourceTexture;
import net.minecraft.util.Identifier;
import org.lwjgl.openvr.Texture;
import org.lwjgl.openvr.VR;
import org.lwjgl.openvr.VRTextureBounds;

public class MinecraftClientHandlerer {
	public static void postRenderFBO(Framebuffer framebuffer) {
		if (FabriVRClient.guiFrameBuffer == null) FabriVRClient.guiFrameBuffer = new Framebuffer(854, 854, false, true);
		if (FabriVRClient.texture == null){
			FabriVRClient.texture = new NativeImageBackedTexture(1,1,true);
			FabriVRClient.texture.close();
			((AbstractTextureAccessor)FabriVRClient.texture).setGlID(FabriVRClient.guiFrameBuffer.getColorAttachment());
			FabriVRClient.texture.load(MinecraftClient.getInstance().getResourceManager());
			FabriVRClient.textureID = MinecraftClient.getInstance().getTextureManager().registerDynamicTexture("fabrivr_gui_texture", FabriVRClient.texture);
		}
		Texture texture = Texture.malloc();
		texture.set(framebuffer.fbo, VR.ETextureType_TextureType_OpenGL, VR.EColorSpace_ColorSpace_Auto);
		VRTextureBounds bounds = VRTextureBounds.malloc();
		bounds.uMin(0);
		bounds.uMax(1);
		bounds.vMin(0f);
		bounds.vMax(1f);
		VRCompositorError result = VRCompositor.upload(Eye.LEFT, texture, bounds, VR.EVRSubmitFlags_Submit_Default);
		if (result != VRCompositorError.NONE) System.out.println("Left Eye Submission returned " + result);
		result = VRCompositor.upload(Eye.RIGHT, texture, bounds, VR.EVRSubmitFlags_Submit_Default);
		if (result != VRCompositorError.NONE) System.out.println("Right Eye Submission returned " + result);
	}
}
