package com.tfc.fabrivr.client;

import com.tfc.fabrivr.utils.openvr.Eye;
import com.tfc.fabrivr.utils.openvr.VRCompositor;
import com.tfc.fabrivr.utils.openvr.VRCompositorError;
import net.minecraft.client.gl.Framebuffer;
import org.lwjgl.openvr.Texture;
import org.lwjgl.openvr.VR;
import org.lwjgl.openvr.VRTextureBounds;

public class MinecraftClientHandlerer {
	public static void postRenderFBO(Framebuffer framebuffer) {
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
