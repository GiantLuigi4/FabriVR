package com.tfc.fabrivr.mixin;

import com.mojang.blaze3d.platform.FramebufferInfo;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.tfc.fabrivr.client.FabriVRClient;
import com.tfc.fabrivr.mixin_code.FrameBufferMixinCode;
import com.tfc.fabrivr.mixin_code.GameRendererCode;
import net.minecraft.client.gl.Framebuffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Framebuffer.class)
public class FrameBufferMixin {
	@Shadow public int fbo;
	@Shadow public int viewportWidth;
	@Shadow public int viewportHeight;
	
	/**
	 * @author TFC The Flying Creeper (aka GiantLuigi4)
	 * @reason I'd be concerned if someone decides to overwrite this, and I didn't want to double bind buffers
	 */
	@Overwrite
	private void bind(boolean updateViewport) {
		FrameBufferMixinCode.bind((Framebuffer)(Object)this,updateViewport);
	}
}
