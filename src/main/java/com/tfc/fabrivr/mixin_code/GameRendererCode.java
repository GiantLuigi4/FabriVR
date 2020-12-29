package com.tfc.fabrivr.mixin_code;

import com.mojang.blaze3d.platform.FramebufferInfo;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.tfc.fabrivr.FabriVR;
import com.tfc.fabrivr.Settings;
import com.tfc.fabrivr.client.FabriVRClient;
import com.tfc.fabrivr.client.FabriVRFrameBuffer;
import com.tfc.fabrivr.mixin.GameRendererAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.ovr.OVR;
import org.lwjgl.ovr.OVRQuatf;

import java.util.Arrays;

public class GameRendererCode {
	public static int eye = -1;
	
	public static void renderPre(float delta, long time) {
		boolean isFirst = eye == -1;
		if (isFirst) {
			FabriVRClient.update();
			OVR.ovr_WaitToBeginFrame(FabriVRClient.session.get(0), 0);
			OVR.ovr_BeginFrame(FabriVRClient.session.get(0), 0);
		}
		
		{
			if (FabriVRClient.headQuat != null) {
				OVRQuatf headQuat = FabriVRClient.headQuat;
				OVRQuatf quat0 = FabriVRClient.hmdToEyeViewPose0.Orientation();
				quat0.w(headQuat.w());
				quat0.x(headQuat.x());
				quat0.y(headQuat.y());
				quat0.z(headQuat.z());
				FabriVRClient.hmdToEyeViewPose0.Orientation(quat0);
				
				
				FabriVRClient.layer.RenderPose(0, FabriVRClient.hmdToEyeViewPose0);
				
				
				OVRQuatf quat1 = FabriVRClient.hmdToEyeViewPose1.Orientation();
				quat1.w(headQuat.w());
				quat1.x(headQuat.x());
				quat1.y(headQuat.y());
				quat1.z(headQuat.z());
				FabriVRClient.hmdToEyeViewPose1.Orientation(quat1);
				
				FabriVRClient.layer.RenderPose(1, FabriVRClient.hmdToEyeViewPose1);
			}
		}
		
		if (!isFirst) {
			viewPortEye(eye-1);
		} else {
			for (int i=0; i<=2;i++) {
				eye++;
				if (i != 0) {
//					GlStateManager.bindFramebuffer(FramebufferInfo.FRAME_BUFFER, FabriVRClient.vrFBO.getId());
//					FabriVRClient.vrFBO.draw(FabriVRClient.vrFBO.viewportWidth, FabriVRClient.vrFBO.viewportHeight,true);
//					FabriVRClient.vrFBO.clear(false);
//					GlStateManager.clearColor(0,0,1,1);
//					GlStateManager.multMatrix(
//							new MatrixStack().peek().getModel()
//					);
					
					viewPortEye(eye-1);
//					MinecraftClient.getInstance().gameRenderer.render(delta, time, false);
					
					if (i == 2) {
						OVR.ovr_CommitTextureSwapChain(FabriVRClient.session.get(0), FabriVRClient.textureSwapChain.get(0));
						
						PointerBuffer layerPtrList = BufferUtils.createPointerBuffer(1);
						layerPtrList.put(FabriVRClient.layer.address());
						layerPtrList.flip();
						
						OVR.ovr_EndFrame(FabriVRClient.session.get(0), 0, null, layerPtrList);
						
						GlStateManager.bindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
					}
				}
				GlStateManager.bindFramebuffer(FramebufferInfo.FRAME_BUFFER, MinecraftClient.getInstance().getFramebuffer().fbo);
			}
			eye = -1;
			viewPortEye(eye);
		}
	}
	
	public static void viewPortEye(int eye) {
		if(eye==0||eye==1) {
			RenderSystem.viewport(FabriVRClient.layer.Viewport(eye).Pos().x(), FabriVRClient.layer.Viewport(eye).Pos().y(), FabriVRClient.layer.Viewport(eye).Size().w(), FabriVRClient.layer.Viewport(eye).Size().h());
			RenderSystem.matrixMode(GL11.GL_MODELVIEW);
			RenderSystem.loadIdentity();
			if(eye==0) RenderSystem.translatef(Settings.INSTANCE.BETWEEN_EYES_DISTANCE/2f,0.0f,0.0f);
			else RenderSystem.translatef(-Settings.INSTANCE.BETWEEN_EYES_DISTANCE/2f,0.0f,0.0f);
		}
//		else RenderSystem.viewport(0, 0, FabriVRClient.vrFBO.getSize().w(), FabriVRClient.vrFBO.getSize().h());
	}
	
	public static void renderPost() {
//		FabriVRFrameBuffer framebuffer = FabriVRClient.vrFBO;
////		framebuffer.initFbo(framebuffer.viewportWidth,framebuffer.viewportHeight,false);
//		GlStateManager.bindFramebuffer(FramebufferInfo.FRAME_BUFFER, framebuffer.fbo);
//		framebuffer.draw(
//				FabriVRClient.vrFBO.getSize().w(),
//				FabriVRClient.vrFBO.getSize().h()
//		);
////		FabriVRClient.vrFBO.beginWrite(false);
//		GlStateManager.clearColor(0,0,1,1);
//		int i = 16384;
//		if (framebuffer.useDepthAttachment) {
//			GlStateManager.clearDepth(1.0D);
//			i |= 256;
//		}
//		GlStateManager.clear(i, false);
//		framebuffer.endWrite();
	}
}
