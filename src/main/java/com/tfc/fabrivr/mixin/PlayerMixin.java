package com.tfc.fabrivr.mixin;

import com.tfc.fabrivr.mixin_code.PlayerEntityMixinCode;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class PlayerMixin {
	@Inject(at = @At("HEAD"), method = "tick()V")
	public void applyOffset(CallbackInfo ci) {
		ClientPlayerEntity player = ((ClientPlayerEntity) (Object) this);
		PlayerEntityMixinCode.move(player);
	}
	
	public Vec3d getVRCamOffset() {
		return PlayerEntityMixinCode.getCamOff((ClientPlayerEntity) (Object) this);
	}
	
	public Vec3d getRotation(float tickDelta) {
		return PlayerEntityMixinCode.getRotation(tickDelta, (Entity) (Object) this);
	}
}
