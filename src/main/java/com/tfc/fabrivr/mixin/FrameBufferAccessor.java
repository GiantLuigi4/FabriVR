package com.tfc.fabrivr.mixin;

import net.minecraft.client.gl.Framebuffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Framebuffer.class)
public interface FrameBufferAccessor {
	@Accessor("depthAttachment")
	int getDepthAttachment();
	@Accessor("colorAttachment")
	int getColorAttachment();
	@Accessor("depthAttachment")
	void setDepthAttachment(int attachment);
	@Accessor("colorAttachment")
	void setColorAttachment(int attachment);
}
