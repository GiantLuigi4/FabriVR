package com.tfc.fabrivr.mixin;

import com.tfc.fabrivr.mixin_code.PlayerEntityMixinCode;
import net.minecraft.client.network.ClientPlayerEntity;
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
}
