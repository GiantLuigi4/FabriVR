package com.tfc.fabrivr.mixin;

import net.minecraft.client.gl.Framebuffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Framebuffer.class)
public interface FramebufferAccessor {
	@Accessor("colorAttachment") void setColorAttachment(int attachment);
	@Accessor("depthAttachment") void setDepthAttachment(int attachment);
}
