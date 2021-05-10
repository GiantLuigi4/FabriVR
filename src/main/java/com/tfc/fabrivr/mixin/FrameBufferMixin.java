package com.tfc.fabrivr.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.Framebuffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Framebuffer.class)
public abstract class FramebufferMixin {
	@Shadow protected abstract void resizeInternal(int width, int height, boolean getError);
	
	@Overwrite
	public void resize(int width, int height, boolean getError) {
		if (!RenderSystem.isOnRenderThread()) {
			RenderSystem.recordRenderCall(() -> {
				this.resizeInternal(854, 854, getError);
			});
		} else {
			this.resizeInternal(854, 854, getError);
		}
		
	}
}
