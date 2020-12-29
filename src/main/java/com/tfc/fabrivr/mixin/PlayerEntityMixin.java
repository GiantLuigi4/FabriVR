package com.tfc.fabrivr.mixin;

import com.tfc.fabrivr.FabriVR;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
	@Inject(at = @At("TAIL"), method = "initDataTracker()V")
	public void registerData(CallbackInfo ci) {
		((Entity)(Object)this).getDataTracker().startTracking(FabriVR.OFF_X,0f);
		((Entity)(Object)this).getDataTracker().startTracking(FabriVR.OFF_Y,0f);
		((Entity)(Object)this).getDataTracker().startTracking(FabriVR.OFF_Z,0f);
	}
}
