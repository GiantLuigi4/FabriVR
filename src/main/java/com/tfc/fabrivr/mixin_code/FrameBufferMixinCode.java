package com.tfc.fabrivr.mixin_code;

import com.mojang.blaze3d.platform.FramebufferInfo;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.Framebuffer;

public class FrameBufferMixinCode {
	public static void bind(Framebuffer framebuffer, boolean updateViewport) {
		RenderSystem.assertThread(RenderSystem::isOnRenderThreadOrInit);
//		if (FabriVRClient.vrFBO != null) {
//			if (GameRendererCode.eye != -1)
//				GlStateManager.bindFramebuffer(FramebufferInfo.FRAME_BUFFER, FabriVRClient.vrFBO.getId());
//			else
//				GlStateManager.bindFramebuffer(FramebufferInfo.FRAME_BUFFER, framebuffer.fbo);
//		} else
		GlStateManager.bindFramebuffer(FramebufferInfo.FRAME_BUFFER, framebuffer.fbo);
//		if (FabriVRClient.vrFBO != null) GlStateManager.bindFramebuffer(FramebufferInfo.FRAME_BUFFER, FabriVRClient.vrFBO.getId());
		
		if (updateViewport)
			GlStateManager.viewport(0, 0, framebuffer.viewportWidth, framebuffer.viewportHeight);
	}
}
