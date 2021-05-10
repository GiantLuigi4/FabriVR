package com.tfc.fabrivr.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.util.Window;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Window.class)
public class WindowMixin {
	@Shadow private int framebufferWidth;
	@Shadow private int framebufferHeight;
	
	@Shadow @Final private long handle;
	
	@Inject(at = @At("TAIL"), method = "updateFramebufferSize")
	public void FabriVR_postUpdateFBOSize(CallbackInfo ci) {
		GLFW.glfwSetWindowSize(handle, 854, 854);
	}
	
	/**
	 * @author TFC The Flying Creeper
	 */
	@Overwrite
	public int getFramebufferWidth() {
		return 854;
	}
	
	/**
	 * @author TFC The Flying Creeper
	 */
	@Overwrite
	public int getFramebufferHeight() {
		return 854;
	}
}
