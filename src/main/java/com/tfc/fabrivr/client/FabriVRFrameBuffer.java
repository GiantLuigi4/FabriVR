package com.tfc.fabrivr.client;

import com.mojang.blaze3d.platform.FramebufferInfo;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.tfc.fabrivr.mixin.FrameBufferAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.texture.TextureUtil;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.ovr.OVRSizei;

import java.nio.IntBuffer;

//https://github.com/Gasteclair/LWJGL-libOVR-VR-in-JAVA-/blob/master/FBO.java
public class FabriVRFrameBuffer extends Framebuffer {
	
	private int id;
	private OVRSizei size;
	private int textureId;
	private int depthId;
	
	public FabriVRFrameBuffer(boolean useDepth, boolean getError, OVRSizei size, int textureId) {
		super(size.w(), size.h(), useDepth, getError);
		this.id = this.fbo;
		this.size = size;
		this.textureId = textureId;
		this.depthId = ((FrameBufferAccessor)this).getDepthAttachment();
		
		this.initFbo(viewportWidth,viewportHeight,getError);
		this.setClearColor(0,0,0,0);
	}
	
	@Override
	public void beginRead() {
		RenderSystem.assertThread(RenderSystem::isOnRenderThread);
//		GlStateManager.bindTexture(MinecraftClient.getInstance().getFramebuffer().getColorAttachment());
		GlStateManager.bindTexture(this.getColorAttachment());
	}
	
	@Override
	public void initFbo(int width, int height, boolean getError) {
		RenderSystem.assertThread(RenderSystem::isOnRenderThreadOrInit);
		this.viewportWidth = width;
		this.viewportHeight = height;
		this.textureWidth = width;
		this.textureHeight = height;
		this.fbo = GlStateManager.genFramebuffers();
		int colorAttachment = TextureUtil.generateId();
		if (this.useDepthAttachment) {
			int depthAttachment = TextureUtil.generateId();
			GlStateManager.bindTexture(depthAttachment);
			GlStateManager.texParameter(3553, 10241, 9728);
			GlStateManager.texParameter(3553, 10240, 9728);
			GlStateManager.texParameter(3553, 10242, 10496);
			GlStateManager.texParameter(3553, 10243, 10496);
			GlStateManager.texParameter(3553, 34892, 0);
			GlStateManager.texImage2D(3553, 0, 6402, this.textureWidth, this.textureHeight, 0, 6402, 5126, (IntBuffer)null);
			
			((FrameBufferAccessor)this).setDepthAttachment(depthAttachment);
		}
		((FrameBufferAccessor)this).setColorAttachment(colorAttachment);
		
		this.setTexFilter(9728);
		GlStateManager.bindTexture(colorAttachment);
		GlStateManager.texImage2D(3553, 0, 32856, this.textureWidth, this.textureHeight, 0, 6408, 5121, (IntBuffer)null);
		GlStateManager.bindFramebuffer(FramebufferInfo.FRAME_BUFFER, this.fbo);
		GlStateManager.framebufferTexture2D(FramebufferInfo.FRAME_BUFFER, FramebufferInfo.COLOR_ATTACHMENT, 3553, colorAttachment, 0);
		if (this.useDepthAttachment) {
			GlStateManager.framebufferTexture2D(FramebufferInfo.FRAME_BUFFER, FramebufferInfo.DEPTH_ATTACHMENT, 3553, depthId, 0);
		}
		
		if (this.size != null) {
			int[] textureId = new int[]{this.textureId};
			
			GlStateManager.bindTexture(textureId[0]);
			{
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
				
				GL11.glTexImage2D(
						GL11.GL_TEXTURE_2D,
						0,
						GL11.GL_RGBA,
						size.w(),
						size.h(),
						0,
						GL11.GL_RGBA,
						GL11.GL_FLOAT,
						0
				);
			}
			
			GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, textureId[0], 0);
		}
		
		this.checkFramebufferStatus();
		this.clear(getError);
		this.endRead();
	}
	
	//	public FabriVRFrameBuffer(OVRSizei size, int textureId2) {
//
//		this.size = size;
//
//		id = new int[1];
//		id[0]= GL30.glGenFramebuffers();
//		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, id[0]);
//		{
//			textureId = new int[1];
//			textureId[0] = textureId2;
//
//			GlStateManager.bindTexture(textureId[0]);
//			{
//				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
//				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
//				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
//				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
//
//				GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, size.w(), size.h(), 0, GL11.GL_RGBA, GL11.GL_FLOAT, 0);
//
//				GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, textureId[0], 0);
//			}
//			depthId = new int[1];
//			depthId[0] = GL11.glGenTextures();
//
//			GlStateManager.bindTexture(depthId[0]);
//			{
//				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
//				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
//				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
//				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
//
//				GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_DEPTH_COMPONENT, size.w(), size.h(), 0, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, 0);
//
//				GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, textureId[0], 0);
//			}
//			GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER,  GL30.GL_DEPTH_ATTACHMENT, GL11.GL_TEXTURE_2D, depthId[0], 0);
//
//			if (GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER) != GL30.GL_FRAMEBUFFER_COMPLETE) {
//
//				System.out.println("FrameBuffer incomplete!");
//			}
//		}
//
//		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, 0);
//		GlStateManager.bindTexture(0);
//		GlStateManager.bindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
//
//	}
	
	public int getTextureId() {
		return textureId;
	}
	
	public int getId() {
		return id;
	}
	
	public OVRSizei getSize() {
		return size;
	}
}

