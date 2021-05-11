package com.tfc.fabrivr.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public abstract class ScreenMixin {
	@Shadow @Nullable protected MinecraftClient client;
	@Shadow public int width;
	@Shadow public int height;
	
	@Shadow public abstract void renderBackgroundTexture(int vOffset);
	
	@Inject(at = @At("HEAD"), method = "renderBackground(Lnet/minecraft/client/util/math/MatrixStack;I)V", cancellable = true)
	public void FabriVR_preRenderBackground(MatrixStack matrices, int vOffset, CallbackInfo ci) {
		ci.cancel();
	}
}
