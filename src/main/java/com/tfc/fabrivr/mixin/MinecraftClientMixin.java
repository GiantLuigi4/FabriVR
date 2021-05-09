package com.tfc.fabrivr.mixin;

import com.tfc.fabrivr.utils.openvr.Session;
import com.tfc.fabrivr.utils.openvr.Tracking;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
	@Inject(at = @At("HEAD"), method = "tick")
	public void preTick(CallbackInfo ci) {
		Tracking.update();
	}
	
	@Inject(at = @At("TAIL"), method = "close")
	public void onClose(CallbackInfo ci) {
		Session.end();
	}
}
